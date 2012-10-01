package cl.solem.tyc.ejb;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import cl.solem.jee.libsolem.ejb.EjbBase;
import cl.solem.jee.libsolem.exception.ErrorSPException;
import cl.solem.jee.libsolem.util.Seguridad;
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.Login;
import cl.solem.tyc.beans.Sesion;
import cl.solem.tyc.ejb.interfaces.SesionEJBRemote;


@Stateless(mappedName = "tyc/SesionEJB")
public class SesionEJB extends EjbBase implements SesionEJBRemote {
	
	private Logger log = Logger.getLogger(SesionEJB.class);
	
//----------------------------------------------------------------------	
	@Resource(mappedName = "java:/SolemBilling")
	protected DataSource solemDS;
	
	private Error error = null;
	private ErrorSPException errorSP = null;
	private Seguridad seg = null;
	private String codAplicacion = "BIL";
//-----------------------------------------------------------------------	

	/**
	 * @see EjbBase#EjbBase()
	 */
	public SesionEJB() {
		super();
		setSolemDataSource(solemDS);
	}

	public Map<String, Object> verificaDataLogueo(Map<String, Object> moDataIn) {
		// Variables genericas para todo metodo
		iniciaParametros("verificaDataLogueo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;
		String msjErrorUsuario = null;
		//Map<String, Map<String, Object>> mpSPParametros = null;

		// Variables especificas del metodo
		Login login = null;
		String sCodTransaccion = " ";
		Sesion sesion = null;
		
		login = (Login) moDataIn.get("login");
		String ip = login.getHost();

		log.info("SesionEJB -> verificaDataLogueo IN");

		login = (Login) moDataIn.get("login");

		try {
			dbConeccion = solemDS.getConnection();

			try {
				log.info("SesionEJB -> conectado y listo para llamar en verificaDataLogueo()");
				
				log.info("SesionEJB -> LOGUEO ");
				sNombreSP = "TYC$VALIDALOGUEO";
				sTipoSP = "TX";

				log.info("SesionEJB -> " + sNombreSP + " Inicio");

				// Parametros de entrada
				creaParametroIn(Types.VARCHAR, login.getUsername());
				creaParametroIn(Types.VARCHAR, login.getPassword());
				creaParametroIn(Types.VARCHAR, ip);
				creaParametroIn(Types.VARCHAR, codAplicacion);
				creaParametroIn(Types.VARCHAR, login.getIdioma());

				// Parametros de salida
				creaParametroOut("SES_IDSESION", Types.VARCHAR);
				creaParametroOut("SES_FHOLOGUEO", Types.VARCHAR);
				creaParametroOut("SES_FHOULTIMACONSULTA", Types.VARCHAR);
				creaParametroOut("SES_DATA01", Types.VARCHAR);				// cod Aplicacion
				creaParametroOut("SES_DATA02", Types.VARCHAR);				// numUsuario
				creaParametroOut("SES_DATA03", Types.VARCHAR);				// Rut
				creaParametroOut("SES_DATA04", Types.VARCHAR);				// nombre
				creaParametroOut("SES_DATA05", Types.VARCHAR);				// telefono
				creaParametroOut("SES_DATA06", Types.VARCHAR);				// mail
				creaParametroOut("SES_DATA07", Types.VARCHAR);				// vacios
				creaParametroOut("SES_DATA08", Types.VARCHAR);
				creaParametroOut("SES_DATA09", Types.VARCHAR);
				creaParametroOut("SES_DATA10", Types.VARCHAR);
				creaParametroOut("EXPIRACONTRASENA", Types.VARCHAR);
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);

				if (cStmt != null) {
					// Ejecucion SP
					cStmt.execute();

					// Recuperacion parametros de salida
					numError = (Integer) getParametro(cStmt, "NumError");
					msjError = (String) getParametro(cStmt, "MsjError");
					
					// Datos Usuario
					sesion = new Sesion();
					
					String idSesion = (String) getParametro(cStmt, "SES_IDSESION");
					seg = new Seguridad();
			        String newIdSesion = seg.encriptaKSI(idSesion, ip);
			        seg = null;
					
			        sesion.setSesIdSesion(newIdSesion);					
					sesion.setSesFhoLogueo((String) getParametro(cStmt, "SES_FHOLOGUEO"));
					sesion.setSesFhoUltimaConsulta((String) getParametro(cStmt, "SES_FHOULTIMACONSULTA"));
					sesion.setSesCodAplicacion((String) getParametro(cStmt,"SES_DATA01"));
					sesion.setSesIdUsuario((String) getParametro(cStmt, "SES_DATA02"));
					sesion.setSesRutUsuario((String) getParametro(cStmt, "SES_DATA03"));
					sesion.setSesNombreCompleto((String) getParametro(cStmt, "SES_DATA04"));					
					sesion.setSesIndExpiraContrasena((String) getParametro(cStmt, "EXPIRACONTRASENA"));
					sesion.setSesRutaSistema((String) getParametro(cStmt, "SES_DATA09"));
					sesion.setSesCodIdioma((String) getParametro(cStmt, "SES_DATA10"));

					if (numError != 0) {
						error = new Error(4, Long.toString(numError), msjError, msjErrorUsuario);
						log.error(error.getMsjError());
					} else {
						error = new Error("0", "");
					}

				}
			} catch (SQLException e) {
				log.error("SesionEJB -> ERROR EN: " + sNombreSP, e);
				error = new Error(3, Long.toString(e.getErrorCode()), e.getMessage(), "ERROR EN: " + sNombreSP);
				log.error(error.getMsjError());
			} catch (Exception e) {
				e.printStackTrace();
				if (!(e instanceof ErrorSPException)) {

					log.error("SesionEJB -> ERROR DESCONOCIDO", e);
					error = new Error(3, "9999", e.getMessage());
					log.error(error.getMsjError());
				} else {
					errorSP = (ErrorSPException) e;

					log.info("SesionEJB -> ERROR CONTROLADO ("+ Long.toString(errorSP.getNumError()) + ")");
					error = new Error(errorSP.getNumFamiliaError(), Long.toString(errorSP.getNumError()), errorSP.getMessage());
					log.error(error.getMsjError());
				}
			} finally {

				log.info("SesionEJB -> " + sNombreSP + " Fin");

				try {

					dbConeccion.close();
					cStmt.close();
				} catch (Exception e) {
					e.printStackTrace();
					error = new Error(3, "9999", e.getMessage(), "Error desconectando de la Base de Datos");
					log.error(error.getMsjError());
				}
				dbConeccion = null;
			}

			// valida si es que hubo algun error en la validacion y lo devuelve
			if (sCodTransaccion.equals("113") || sCodTransaccion.equals("103")) {

				log.info("SesionEJB -> ERROR DE VALIDACION DE DATOS LOGIN");
				error = new Error(3, "9999", "Error al validar los datos", "Error al validar los datos");
				log.error(error.getMsjError());
			}

			log.info("SesionEJB -> prepara respuesta");
			moDataOut.put("error", error);
			moDataOut.put("sesion", sesion);
			log.info("SesionEJB -> KSI: " + sesion.getSesIdSesion() + " Largo:  " + sesion.getSesIdSesion().length());
			log.info("SesionEJB -> verificaDataLogueo OUT");
		} catch (SQLException e) {
			log.error(e);
			error = new Error(e.getErrorCode(), Long.toString(e.getErrorCode()), e.getMessage());
			try {
				throw new ErrorSPException(3, e.getErrorCode(), e.getMessage());
			} catch (ErrorSPException e1) {
				error = new Error(e1.getNumError(), Long.toString(e1.getNumError()), e1.getMessage());
				log.error("dbConeccion = solemDS.getConnection(); " + e1);
				log.error(error.getMsjError());
			}
		}
		moDataOut.put("error", error);

		terminaParametros("verificaDataLogueo");

		return moDataOut;
	}

	public Map<String, Object> buscaPerfiles(Map<String, Object> moDataIn) {
		// Variables genericas para todo metodo
		iniciaParametros("buscaPerfiles");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		String sNombreSP = null;
		String sTipoSP = null;
		ResultSet rsResultado = null;
		int numError = 0;
		String msjError = null;
		String msjErrorUsuario = null;
		
		// Variables especificas del metodo
		HashMap<String, String> perfiles = null;
		long lNumUsuario = 0;
		String sCodAplicacion = null;

		log.info("SesionEJB -> buscaPerfiles IN");

		Sesion sesion = (Sesion) moDataIn.get("sesion");

		lNumUsuario = Long.parseLong((String) sesion.getSesIdUsuario());
		sCodAplicacion = (String) sesion.getSesCodAplicacion();
		
		log.info("lNumUsuario " + lNumUsuario);
		log.info("sCodAplicacion " + sCodAplicacion);

		try {
			dbConeccion = solemDS.getConnection();
			
			try {
				log.info("SesionEJB -> conectado y listo para llamar en buscaPerfiles()");
				
				log.info("SesionEJB -> BUSCA PERFILES ");
				sNombreSP = "BIL$BUSCAPERFILES.BUSCAPERFILES";
				sTipoSP = "TX";

				log.info("SesionEJB -> " + sNombreSP + " Inicio");

				// Parametros de entrada
				creaParametroIn(Types.LONGVARCHAR, lNumUsuario);
				creaParametroIn(Types.VARCHAR, sCodAplicacion);

				// Parametros de salida
				creaParametroOut("Perfiles", OracleTypes.CURSOR);
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);

				if (cStmt != null) {
					// Ejecucion SP
					cStmt.execute();

					// Recuperacion parametros de salida
					numError = (Integer) getParametro(cStmt, "NumError");
					msjError = (String) getParametro(cStmt, "MsjError");
					
					// Datos Perfiles
					rsResultado = (ResultSet) getParametro(cStmt, "Perfiles");

					if (rsResultado != null) {
						
						perfiles = new HashMap<String, String>();

						while (rsResultado.next()) 
						{
							log.info("COD PERFIL: "+rsResultado.getString("UPE_SCODPERFIL"));
							log.info("COD APP: "+rsResultado.getString("UPE_SCODAPLICACION"));
							perfiles.put(rsResultado.getString("UPE_SCODPERFIL"), rsResultado.getString("UPE_SCODAPLICACION"));							
						}
						
						rsResultado.close();
					}

					if (numError != 0) {
						log.info("numError != 0");
						error = new Error(4, Long.toString(numError), msjError, msjErrorUsuario);
					} else {
						log.info("numError == 0");
						error = new Error("0", "");
						moDataOut.put("sesion", sesion);
						moDataOut.put("perfiles", perfiles);
					}
				}
			} catch (SQLException e) {
				error = new Error(3, Long.toString(e.getErrorCode()), e.getMessage(), "ERROR EN: " + sNombreSP);
				log.error("SesionEJB -> ERROR EN: " + sNombreSP, e);				

			} catch (Exception e) {

				if (!(e instanceof ErrorSPException)) {
					log.error("SesionEJB -> ERROR DESCONOCIDO", e);
					error = new Error(3, "9999", e.getMessage(), "ERROR DESCONOCIDO");
				} else {
					errorSP = (ErrorSPException) e;
					log.info("SesionEJB -> ERROR CONTROLADO ("
							+ Long.toString(errorSP.getNumError()) + ")");
					error = new Error(errorSP.getNumFamiliaError(), Long.toString(errorSP.getNumError()), errorSP.getMessage());
				}

			} finally {

				log.info("SesionEJB -> " + sNombreSP + " Fin");

				try {

					dbConeccion.close();
					cStmt.close();

				} catch (Exception e) {
					error = new Error(3, "9999", e.getMessage(), "Error desconectando la Base de Datos");
				}
				dbConeccion = null;
			}

			log.info("SesionEJB -> prepara respuesta");
			log.info("SesionEJB -> buscaPerfiles OUT");
			
		} catch (SQLException e) {
			log.error(e);
			error = new Error(e.getErrorCode(), Long.toString(e.getErrorCode()), e.getMessage());
			try {
				throw new ErrorSPException(3, e.getErrorCode(), e.getMessage());
			} catch (ErrorSPException e1) {
				error = new Error(e1.getNumError(), Long.toString(e1.getNumError()), e1.getMessage());
				log.error("dbConeccion = solemDS.getConnection(); " + e1);
			}
		}
		
		moDataOut.put("error", error);
		terminaParametros("buscaPerfiles");
		return moDataOut;
	}

	/**
	 * @param Map<String, Object> moDataIn
	 * @return Map<String, Object> moDataOut
	 */
	public Map<String, Object> validaSesion(Map<String, Object> moDataIn) {
		// Variables genericas para todo metodo
		iniciaParametros("validaSesion");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;
		String msjErrorUsuario = null;
		Sesion sesion = null;

		// Variables especificas del metodo
		String ksi = (String) moDataIn.get("ksi");
		String host = (String) moDataIn.get("host");
		String ruta = (String) moDataIn.get("ruta");
		String indRuta = (String) moDataIn.get("indRuta");
		
		log.info("EJB VALIDA SESION RUTA: "+ruta);
		log.info("EJB VALIDA SESION IND RUTA: "+indRuta);
		
		if(indRuta.equals("N"))
			ruta = "";
		else
			ruta += "|";
		
		log.info("EJB VALIDA SESION RUTA: "+ruta);
		log.info("EJB VALIDA SESION IND RUTA: "+indRuta);
		
		ksi = ksi.replace(' ', '+');
		
		log.info("KSI: "+ksi);
		log.info("HOST: "+host);
		log.info("RUTA: "+ruta);
		
		seg = new Seguridad();
		
		String ksiD = seg.desencriptaKSI(ksi);
        String ipD = seg.desencriptaIp(ksi);

		log.info("SesionEJB -> validaSesion IN");
		
		try {
			dbConeccion = solemDS.getConnection();

			try {
				log.info("SesionEJB -> conectado y listo para llamar en ValidaSesion()");

				log.info("SesionEJB -> ValidaSesion");
				sNombreSP = "TYC$VALIDASESION";
				sTipoSP = "TX";

				log.info("SesionEJB -> " + sNombreSP + " Inicio");

				// Parametros de entrada
				creaParametroIn(Types.VARCHAR, ksiD);
				creaParametroIn(Types.VARCHAR, host);
				creaParametroIn(Types.VARCHAR, ruta);
				creaParametroIn(Types.VARCHAR, indRuta);
				
				// Parametros de salida
				creaParametroOut("SES_IDSESION", Types.VARCHAR);
				creaParametroOut("SES_FHOLOGUEO", Types.VARCHAR);
				creaParametroOut("SES_FHOULTIMACONSULTA", Types.VARCHAR);
				creaParametroOut("SES_DATA01", Types.VARCHAR);// cod Aplicacion
				creaParametroOut("SES_DATA02", Types.VARCHAR);// numUsuario
				creaParametroOut("SES_DATA03", Types.VARCHAR);// Rut
				creaParametroOut("SES_DATA04", Types.VARCHAR);// nombre
				creaParametroOut("SES_DATA05", Types.VARCHAR);// telefono
				creaParametroOut("SES_DATA06", Types.VARCHAR);// mail
				creaParametroOut("SES_DATA07", Types.VARCHAR);
				creaParametroOut("SES_DATA08", Types.VARCHAR);
				creaParametroOut("SES_DATA09", Types.VARCHAR);
				creaParametroOut("SES_DATA10", Types.VARCHAR);//idioma
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);

				if (cStmt != null) {
					// Ejecucion SP
					cStmt.execute();

					// Recuperacion parametros de salida
					numError = (Integer) getParametro(cStmt, "NumError");
					msjError = (String) getParametro(cStmt, "MsjError");
					
					// Datos Usuario
					sesion = new Sesion();
					
					String idSesion = (String) getParametro(cStmt, "SES_IDSESION");
					String newIdSesion = seg.encriptaKSI(idSesion, ipD);
			        seg = null;
			        
			        sesion.setSesIdSesion(newIdSesion);
					sesion.setSesFhoLogueo((String) getParametro(cStmt, "SES_FHOLOGUEO"));
					sesion.setSesFhoUltimaConsulta((String) getParametro(cStmt, "SES_FHOULTIMACONSULTA"));
					sesion.setSesCodAplicacion((String) getParametro(cStmt, "SES_DATA01"));
					sesion.setSesIdUsuario((String) getParametro(cStmt, "SES_DATA02"));
					sesion.setSesRutUsuario((String) getParametro(cStmt, "SES_DATA03"));
					sesion.setSesNombreCompleto((String) getParametro(cStmt, "SES_DATA04"));
					sesion.setSesNombre((String) getParametro(cStmt, "SES_DATA04"));
					sesion.setSesRutaSistema((String) getParametro(cStmt, "SES_DATA09"));
					sesion.setSesCodIdioma((String) getParametro(cStmt, "SES_DATA10"));
					
					if (numError != 0) {
						error = new Error(4, Long.toString(numError), msjError, msjErrorUsuario);
					} else {
						error = new Error("0", "");
						log.info("SesionEJB -> Se ha Validado la Sesion");
					}
				}
			} catch (SQLException e) {
				log.error("SesionEJB -> ERROR EN: " + sNombreSP, e);
				error = new Error(3, Long.toString(e.getErrorCode()), e.getMessage(), "ERROR EN: " + sNombreSP);
			} catch (Exception e) {
				if (!(e instanceof ErrorSPException)) {
					log.error("SesionEJB -> ERROR DESCONOCIDO", e);
					error = new Error(3, "9999", e.getMessage(), "Error Desconocido");
				} else {
					errorSP = (ErrorSPException) e;
					log.info("SesionEJB -> ERROR CONTROLADO ("+Long.toString(errorSP.getNumError()) + ")");
					error = new Error(errorSP.getNumFamiliaError(), Long.toString(errorSP.getNumError()), errorSP.getMessage());
				}
			} finally {

				log.info("SesionEJB -> " + sNombreSP + " Fin");

				try {

					dbConeccion.close();
					cStmt.close();
				} catch (Exception e) {
					error = new Error(3, "9999", e.getMessage(), "Error desconectando la Base de Datos");
				}
				dbConeccion = null;
			}

			log.info("SesionEJB -> prepara respuesta");
			moDataOut.put("error", error);
			moDataOut.put("sesion", sesion);

			log.info("SesionEJB -> verificaDataLogueo OUT");
		} catch (SQLException e) {
			log.error(e);
			error = new Error(e.getErrorCode(), Long.toString(e.getErrorCode()), e.getMessage(), "Error de Base de Datos");
			try {
				throw new ErrorSPException(3, e.getErrorCode(), e.getMessage());
			} catch (ErrorSPException e1) {
				error = new Error(e1.getNumError(), Long.toString(e1.getNumError()), e1.getMessage());
				log.error("dbConeccion = solemDS.getConnection(); " + e1);
			}
		}
		moDataOut.put("error", error);

		terminaParametros("validaSesion");

		return moDataOut;
	}
}