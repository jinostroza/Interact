package cl.solem.tyc.ejb;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.hsqldb.Types;

import cl.solem.jee.libsolem.ejb.EjbBase;
import cl.solem.jee.libsolem.exception.ErrorSPException;
import cl.solem.jee.libsolem.util.Errores;
import cl.solem.jee.libsolem.util.Seguridad;
import cl.solem.tyc.beans.Catalogo;
import cl.solem.tyc.beans.CatalogoPrest;
import cl.solem.tyc.beans.ConvenioAnexoDetalle;
import cl.solem.tyc.beans.Empresa;
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.ModificadorCatalogo;
import cl.solem.tyc.beans.PrecioCatalogo;
import cl.solem.tyc.beans.Restriccion;
import cl.solem.tyc.beans.Sesion;
import cl.solem.tyc.beans.UniOrg;
import cl.solem.tyc.beans.Usuario;
import cl.solem.tyc.ejb.interfaces.CatalogoEJBRemote;
import cl.solem.tyc.ejb.interfaces.SesionEJBRemote;



@Stateless(mappedName = "tyc/CatalogoEJB")
public class CatalogoEJB extends EjbBase implements CatalogoEJBRemote{
	
	private Logger log = Logger.getLogger(CatalogoEJB.class);
	@Resource(mappedName = "java:/SolemBilling")
	protected DataSource solemDS;
	
	private Error error = null;
	private ErrorSPException errorSP = null;
	private Seguridad seg = null;
	
	@EJB(mappedName = "tyc/SesionEJB")
	private SesionEJBRemote ejbSesion;

	public CatalogoEJB() {
		super();
		setSolemDataSource(solemDS);
	}

	public Map<String, Object> buscarCatalogos(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		log.info("Busca Catalogos en  CatalogosEJB");
		iniciaParametros("buscarCatalogos");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;
		int totalRegistros = 0;
		
		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;
		
		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================

		ArrayList<Catalogo> listaCatalogos = null;
		Catalogo m_catalogo = (Catalogo) moDataIn.get("catalogo");
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");
		
		

		log.info("CatalogoEJB -> buscarCatalogos IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en buscarCatalogos()");

			log.info("CatalogoEJB -> buscarCatalogos");

			sNombreSP = "CAT$BUSCA_CATALOGOS.LISTA_CATALOGOS";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, m_catalogo.getnIdPrestador());
			creaParametroIn(Types.VARCHAR, m_catalogo.getsCodCatalogo());
			creaParametroIn(Types.VARCHAR, m_catalogo.getsNombre());
			creaParametroIn(Types.VARCHAR, m_catalogo.getsCodEstado());
			creaParametroIn(Types.VARCHAR,m_catalogo.getsNombreFantasia());
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);
		

			log.info("Id prestador: " + m_catalogo.getnIdPrestador());
			log.info("Codigo Catalogo: " + m_catalogo.getsCodCatalogo());
			log.info("Nombre Catalogo: " + m_catalogo.getsNombre());
			log.info("Estado Catalogo: " + m_catalogo.getsCodEstado());
			log.info("Nombre prestador: " + m_catalogo.getsNombreFantasia());
			log.info("Ordenar Por: "+sOrdenarPor);
			log.info("Tipo Orden: "+sTipoOrden);
			log.info("Pag Actual: "+pagActual);			
			log.info("Cant reg x pag: "+regPorPagina);

			log.info("--------------------------------");
			
			// =============================================================
			// Parametros de Salida
			// =============================================================

			creaParametroOut("TotalRegistros", OracleTypes.INTEGER);
			creaParametroOut("CUR_CATALOGOS", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();
				
				// Recuperacion parametros de salida
				
				totalRegistros = (Integer) getParametro(cStmt, "TotalRegistros");
				log.info("Total Registros: "+totalRegistros);
				
				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {
					
					try{
						error = new Error("0","");
	
						log.info("CatalogoEJB -> " + sNombreSP + " rsResultado Inicio");
						rsResultado = (ResultSet) getParametro(cStmt, "CUR_CATALOGOS");
						log.info("CatalogoEJB -> " + sNombreSP + " rsResultado Fin");
	
						if (rsResultado != null) {
							log.info("rsResultado != nulo");
	
							listaCatalogos = new ArrayList<Catalogo>();
	
							while (rsResultado.next()) {
								Catalogo catalogo = new Catalogo();
								catalogo.setnIdCatalogo(rsResultado.getLong("CAT_NIDCATALOGO"));
								catalogo.setnIdPrestador(rsResultado.getLong("CAT_NIDPRESTADOR"));
								catalogo.setsCodCatalogo(rsResultado.getString("CAT_SCODCATALOGO"));
								catalogo.setsNombre(rsResultado.getString("CAT_SNOMBRE"));
								catalogo.setsDescripcion(rsResultado.getString("CAT_SDESCRIPCION"));
								catalogo.setsIndPlantilla(rsResultado.getString("CAT_SINDPLANTILLA"));
								catalogo.setsIndPublico(rsResultado.getString("CAT_SINDPUBLICO"));
								catalogo.setsFecHoInicio(rsResultado.getString("CAT_DFHOINICIO"));
								catalogo.setsFecHoTermino(rsResultado.getString("CAT_DFHOTERMINO"));
								catalogo.setnIdResponsable(rsResultado.getLong("CAT_NIDRESPONSABLE"));
								catalogo.setsCodEstado(rsResultado.getString("CAT_SCODESTADO"));
								catalogo.setsIndVigencia(rsResultado.getString("CAT_SINDVIGENCIA"));
								catalogo.setsFecCreacion(rsResultado.getString("CAT_DFECCREACION"));
								catalogo.setnIdCreador(rsResultado.getLong("CAT_NIDCREADOR"));
								catalogo.setsFecActualizacion(rsResultado.getString("CAT_DFECACTUALIZACION"));
								catalogo.setnIdActualizador(rsResultado.getLong("CAT_NIDACTUALIZADOR")); 
								catalogo.setsNombreFantasia(rsResultado.getString("EMP_SNOMBREFANTASIA"));
								listaCatalogos.add(catalogo);
							}
							log.info("cantidad: " + listaCatalogos.size());
							moDataOut.put("listaCatalogos", listaCatalogos);
							moDataOut.put("totalRegistros", totalRegistros);
							rsResultado.close();
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}

		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("buscarCatalogos");

		return moDataOut;
	}
	
	public Map<String, Object> cargarCatalogo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Catalolgo");

		// Variables genericas para todo metodo
		iniciaParametros("cargaCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================

		Catalogo catalogo = null;

		long idCatalogo = 0;

		idCatalogo = (Long) moDataIn.get("idCatalogo");

		log.info("CatalogoEJB -> cargaCatalogo IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en cargaCatalogo()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$CARGA_CATALAGO.DETALLE_CATALOGO";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idCatalogo);

			log.info("parametros de entrada------------");
			log.info("IdCatalogo -> " + idCatalogo);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_CATALOGOS", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					//error = new Error(0, 0, "", "");
					error = new Error("0","");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_CATALOGOS");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							catalogo = new Catalogo();
							catalogo.setnIdCatalogo(rsResultado.getLong("CAT_NIDCATALOGO"));
							catalogo.setnIdPrestador(rsResultado.getLong("CAT_NIDPRESTADOR"));
							catalogo.setsNombreFantasia(rsResultado.getString("EMP_SNOMBREFANTASIA"));
							catalogo.setsCodCatalogo(rsResultado.getString("CAT_SCODCATALOGO"));
							catalogo.setsNombre(rsResultado.getString("CAT_SNOMBRE"));
							catalogo.setsDescripcion(rsResultado.getString("CAT_SDESCRIPCION"));
							catalogo.setsIndPlantilla(rsResultado.getString("CAT_SINDPLANTILLA"));
							catalogo.setsIndPublico(rsResultado.getString("CAT_SINDPUBLICO"));
							catalogo.setsFecHoInicio(rsResultado.getString("CAT_DFHOINICIO"));
							catalogo.setsFecHoTermino(rsResultado.getString("CAT_DFHOTERMINO"));
							catalogo.setsCodEstado(rsResultado.getString("CAT_SCODESTADO"));
							catalogo.setsDesEstado(rsResultado.getString("SDESESTADO"));
							catalogo.setsIndVigencia(rsResultado.getString("CAT_SINDVIGENCIA"));
							catalogo.setnIdCreador(rsResultado.getLong("CAT_NIDCREADOR"));
							catalogo.setnIdResponsable(rsResultado.getLong("CAT_NIDRESPONSABLE"));
							catalogo.setsNomResponsable(rsResultado.getString("CAT_SNOMRESPONSABLE"));
							
						}
						moDataOut.put("catalogo", catalogo);
						rsResultado.close();
						
					}
				}
			}
		} catch (SQLException e) {
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("ConvenioEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("cargaCatalogo");

		return moDataOut;
	}
	
	
	public Map<String, Object> actualizarCatalogo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizaCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		Catalogo actualizacatalogo = (Catalogo) moDataIn.get("catalogo");

		// =============================================================
		// VERIFICACION SESION
		// =============================================================


			try {
				dbConeccion = solemDS.getConnection();

				log.info("CatalogoEJB -> conectado y listo para llamar en actualizaCatalogo()");

				log.info("CatalogoEJB -> LOGUEO ");
				sNombreSP = "CAT$ACTUALIZA_CATALOGO";
				sTipoSP = "TX";

				log.info("EmpresaEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				creaParametroIn(Types.NUMERIC, actualizacatalogo.getnIdCatalogo());
				creaParametroIn(Types.NUMERIC, actualizacatalogo.getnIdPrestador());
				creaParametroIn(Types.VARCHAR, actualizacatalogo.getsCodCatalogo());
				creaParametroIn(Types.VARCHAR, actualizacatalogo.getsNombre());
				creaParametroIn(Types.VARCHAR, actualizacatalogo.getsCodEstado());
				creaParametroIn(Types.VARCHAR, actualizacatalogo.getsDescripcion());
				creaParametroIn(Types.VARCHAR, actualizacatalogo.getsFecHoInicio());
				creaParametroIn(Types.VARCHAR, actualizacatalogo.getsFecHoTermino());
				creaParametroIn(Types.NUMERIC, actualizacatalogo.getnIdResponsable());
				creaParametroIn(Types.VARCHAR, actualizacatalogo.getsIndPlantilla());
				creaParametroIn(Types.VARCHAR, actualizacatalogo.getsIndPublico());
				creaParametroIn(Types.VARCHAR, actualizacatalogo.getsIndVigencia());
				creaParametroIn(Types.NUMERIC, actualizacatalogo.getnIdActualizador());

				log.info("parametros de entrada catalogo------------");
				log.info("Id Catalogo -> " + actualizacatalogo.getnIdCatalogo());
				log.info("Id Prestador -> " + actualizacatalogo.getnIdPrestador());
				log.info("Codigo Catalogo-> " + actualizacatalogo.getsCodCatalogo());
				log.info("Nombre -> " + actualizacatalogo.getsNombre());
				log.info("Estado -> " + actualizacatalogo.getsCodEstado());
				log.info("Descripcion -> " + actualizacatalogo.getsDescripcion());
				log.info("Fecha inicio -> " + actualizacatalogo.getsFecHoInicio());
				log.info("Fecha Termino -> " + actualizacatalogo.getsFecHoTermino());
				log.info("Id Responsable -> " + actualizacatalogo.getnIdResponsable());
				log.info("Indicador Plantilla -> " + actualizacatalogo.getsIndPlantilla());
				log.info("Indicador Publico  -> " + actualizacatalogo.getsIndPublico());
				log.info("Indicador Vigencia -> " + actualizacatalogo.getsIndVigencia());
				log.info("Id Actualizador -> " + actualizacatalogo.getnIdActualizador());
				
				log.info("--------------------------------");
				 

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

				} catch (Exception e) {
					log.error(e);
					error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				}

				if (cStmt != null) {
					log.info("Cursor != nulo");

					cStmt.execute();

					// Recuperacion parametros de salida
					numError = (Integer) getParametro(cStmt, "NumError");
					log.info("numError: " + numError);

					msjError = (String) getParametro(cStmt, "MsjError");
					log.info("msjError: " + msjError);

					if (numError != 0) {
						error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
						throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
					} else {
						error = new Error(0, 0, "", "");
					}
					// utx.commit();
				}

			} catch (SQLException e) {
				log.error("CatalogoEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				// utx.rollback();
			} finally {

				log.info("CatalogoEJB -> " + sNombreSP + " Fin");

				try {
					dbConeccion.close();
					dbConeccion = null;
				} catch (SQLException e) {
					log.error(e);
					error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
					throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
				}

			}

			// =============================================================
			// FIN CUERPO DE EJB
			// =============================================================

		

		moDataOut.put("error", error);

		terminaParametros("actualizaCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> buscarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Prestaciones Asociadas a un Catalogo");

		// Variables genericas para todo metodo
		iniciaParametros("buscarPrestacionesCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;
		int totalRegistros = 0;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		ArrayList<CatalogoPrest> listaPrestacionesCatalogo = new ArrayList<CatalogoPrest>();

		long idCatalogo = 0;

		idCatalogo = (Long) moDataIn.get("idCatalogo");
		
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");
		

		log.info("CatalogoEJB -> buscarPrestacionesCatalogo IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en buscarPrestacionesCatalogo()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$BUSCA_PRESTACIONES.LISTA_PRESTACIONES";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idCatalogo);
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);

			log.info("parametros de entrada------------");
			log.info("idCatalogo -> " + idCatalogo);
			log.info("Ordenar Por: "+sOrdenarPor);
			log.info("Tipo Orden: "+sTipoOrden);
			log.info("Pag Actual: "+pagActual);			
			log.info("Cant reg x pag: "+regPorPagina);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("TotalRegistros", OracleTypes.INTEGER);
			creaParametroOut("CUR_PRESTACION", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();
				
				// Recuperacion parametros de salida
				totalRegistros = (Integer) getParametro(cStmt, "TotalRegistros");
				log.info("Total Registros: "+totalRegistros);

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					//error = new Error(0, 0, "", "");
					error = new Error("0","");
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRESTACION");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							CatalogoPrest catalogoprest = new CatalogoPrest();
							
							catalogoprest.setnIdCatalogo(rsResultado.getLong("CPR_NIDCATALOGO"));
						    
							catalogoprest.setnIdPrestacion(rsResultado.getLong("CPR_NIDPRESTACION"));
							catalogoprest.setnIdCatalogoPrestacion(rsResultado.getLong("CPR_NIDCATALOGOPRESTACION"));
							catalogoprest.setsNomPrestacion(rsResultado.getString("CPR_SNOMPRESTACION"));
							catalogoprest.setsCodPrestacion(rsResultado.getString("CPR_SCODPRESTACION"));
							catalogoprest.setsNomPrestCatalogo(rsResultado.getString("CPR_SNOMPRESTCATALOGO"));
							catalogoprest.setsIndTipoPago(rsResultado.getString("CPR_SINDTIPOPAGO"));
							catalogoprest.setnPorGastos(rsResultado.getLong("CPR_NPORGASTOS"));
							catalogoprest.setnPorHonorarios(rsResultado.getLong("CPR_NPORHONORARIOS"));
							catalogoprest.setsIndVigencia(rsResultado.getString("CPR_SINDVIGENCIA"));
							catalogoprest.setsFecCreacion(rsResultado.getString("CPR_DFECCREACION"));
							catalogoprest.setnIdCreador(rsResultado.getLong("CPR_NIDCREADOR"));
							catalogoprest.setsFecActualizacion(rsResultado.getString("CPR_DFECACTUALIZACION"));
							catalogoprest.setnIdActualizador(rsResultado.getLong("CPR_NIDACTUALIZADOR"));
							
							
							listaPrestacionesCatalogo.add(catalogoprest);
						}
						log.info("cantidad: " + listaPrestacionesCatalogo.size());
						moDataOut.put("listaPrestacionesCatalogo", listaPrestacionesCatalogo);
						moDataOut.put("totalRegistros", totalRegistros);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("buscarPrestacionesCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> cargarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle de una prestacion Catalolgo");

		// Variables genericas para todo metodo
		iniciaParametros("cargarPrestacionesCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================

		CatalogoPrest prestacioncatalogo = null;

		long idCatalogoPrestacion = 0;

		idCatalogoPrestacion = (Long) moDataIn.get("idCatalogoPrestacion");

		log.info("CatalogoEJB -> cargarPrestacionesCatalogo IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en cargarPrestacionesCatalogo()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$CARGA_PRESTACION.DETALLE_PRESTACION";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idCatalogoPrestacion);

			log.info("parametros de entrada------------");
			log.info("idCatalogoPrestacion -> " + idCatalogoPrestacion);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_PRESTACION", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					//error = new Error(0, 0, "", "");
					error = new Error("0","");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRESTACION");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							prestacioncatalogo = new CatalogoPrest();
							prestacioncatalogo.setnIdCatalogo(rsResultado.getLong("CPR_NIDCATALOGO"));
							prestacioncatalogo.setnIdCatalogoPrestacion(rsResultado.getLong("CPR_NIDCATALOGOPRESTACION"));
							prestacioncatalogo.setnIdPrestacion(rsResultado.getLong("CPR_NIDPRESTACION"));
							prestacioncatalogo.setsCodPrestacion(rsResultado.getString("CPR_SCODPRESTACION"));
							prestacioncatalogo.setsNomPrestacion(rsResultado.getString("CPR_SNOMPRESTACION"));
							prestacioncatalogo.setsNomPrestCatalogo(rsResultado.getString("CPR_SNOMPRESTCATALOGO"));
							prestacioncatalogo.setsIndTipoPago(rsResultado.getString("CPR_SINDTIPOPAGO"));
							prestacioncatalogo.setnPorGastos(rsResultado.getLong("CPR_NPORGASTOS"));
							prestacioncatalogo.setnPorHonorarios(rsResultado.getLong("CPR_NPORHONORARIOS"));
							prestacioncatalogo.setsIndVigencia(rsResultado.getString("CPR_SINDVIGENCIA"));
							prestacioncatalogo.setsNombreCatalogo(rsResultado.getString("CAT_SNOMBRE"));
							
							
						}
						moDataOut.put("prestacioncatalogo", prestacioncatalogo);
						rsResultado.close();
						
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("cargarPrestacionesCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> buscarPrecios(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("busca lista de precios de una prestacion asociada a un catalogo");

		// Variables genericas para todo metodo
		iniciaParametros("buscarPrecios");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		ArrayList<PrecioCatalogo> listaPrecios = new ArrayList<PrecioCatalogo>();

		long idCatalogoPrestacion = 0;

		idCatalogoPrestacion = (Long) moDataIn.get("idCatalogoPrestacion");
		
		//PrecioCatalogo precioCatalogo = (PrecioCatalogo) moDataIn.get("precioCatalogo");

		log.info("CatalogoEJB -> buscarPrecios IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en buscarPrecios()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$BUSCA_PRECIOS_PREST.LISTA_PRECIOS_PREST";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idCatalogoPrestacion);

			log.info("parametros de entrada------------");
			log.info("idCatalogoPrestacion -> " + idCatalogoPrestacion);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_PRECIO_PREST", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					//error = new Error(0, 0, "", "");
					error = new Error("0","");
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRECIO_PREST");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							PrecioCatalogo precios = new PrecioCatalogo();
							
							precios.setnIdCatalogoPrecio(rsResultado.getLong("CPC_NIDCATALOGOPRECIO"));
						    precios.setnIdCatalogoPrestacion(rsResultado.getLong("CPC_NIDCATALOGOPRESTACION"));
							precios.setnIdCatalogo(rsResultado.getLong("CPC_NIDCATALOGO"));
							precios.setnIdPrestacion(rsResultado.getLong("CPC_NIDPRESTACION"));
							precios.setsFhoInicio(rsResultado.getString("CPC_DFHOINICIO"));
							precios.setsFhoFin(rsResultado.getString("CPC_DFHOFIN"));
							precios.setsIndPrecioFormula(rsResultado.getString("CPC_SINDPRECIOOFORMULA"));
							precios.setsFormula(rsResultado.getString("CPC_SFORMULA"));
							precios.setnPrecio(rsResultado.getLong("CPC_NPRECIO"));
							precios.setsFhoCreacion(rsResultado.getString("CPC_DFHOCREACION"));
							precios.setnIdUsuarioCreacion(rsResultado.getLong("CPC_NIDUSUARIOCREACION"));
							precios.setsFhoModificacion(rsResultado.getString("CPC_DFHOMODIFICACION"));
							precios.setnIdUsuarioModificacion(rsResultado.getLong("CPC_NIDUSUARIOMODIFICACION"));
							
							listaPrecios.add(precios);
						}
						log.info("cantidad: " + listaPrecios.size());
						moDataOut.put("listaPrecios", listaPrecios);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("buscarPrecios");

		return moDataOut;
	}

	
	public Map<String, Object> buscarModificadores(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("busca lista de precios de una prestacion asociada a un catalogo");

		// Variables genericas para todo metodo
		iniciaParametros("buscarModificadores");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		ArrayList<ModificadorCatalogo> listaModificadores = new ArrayList<ModificadorCatalogo>();

		long idCatalogoPrestacion = 0;

		idCatalogoPrestacion = (Long) moDataIn.get("idCatalogoPrestacion");
		
		//ModificadorCatalogo modificadorCatalogo = (ModificadorCatalogo) moDataIn.get("modificadorCatalogo");

		log.info("CatalogoEJB -> buscarModificadores IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en buscarModificadores()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$BUSCA_MODIFICADORES.LISTA_MODIFICADORES";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idCatalogoPrestacion);

			log.info("parametros de entrada------------");
			log.info("idCatalogoPrestacion -> " + idCatalogoPrestacion );
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_MODIFICADOR", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					//error = new Error(0, 0, "", "");
					error = new Error("0","");
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_MODIFICADOR");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							ModificadorCatalogo modificador = new ModificadorCatalogo();
							
							modificador.setnIdCatalogoModificador(rsResultado.getLong("CPM_NIDCATALOGOMODIFICADOR"));
							modificador.setnIdCatalogoPrestacion(rsResultado.getLong("CPM_NIDCATALOGOPRESTACION"));
							modificador.setnIdCatalogo(rsResultado.getLong("CPM_NIDCATALOGO"));
							modificador.setnIdPrestacion(rsResultado.getLong("CPM_NIDPRESTACION"));
							modificador.setsNombre(rsResultado.getString("CPM_SNOMBRE"));
							modificador.setsCodTipo(rsResultado.getString("CPM_SCODTIPO"));  
							modificador.setsDesCodTipo(rsResultado.getString("CPM_DESCODTIPO"));
							modificador.setsCondicion(rsResultado.getString("CPM_SCONDICION"));
							modificador.setsFormula(rsResultado.getString("CPM_SFORMULA"));
							modificador.setsFhoInicio(rsResultado.getString("CPM_DFHOINICIO"));
							modificador.setsFhoFin(rsResultado.getString("CPM_DFHOFIN"));
							modificador.setsFecCreacion(rsResultado.getString("CPM_FECCREACION"));
							modificador.setnIdCreador(rsResultado.getLong("CPM_NIDCREADOR"));
							modificador.setsFecActualizacion(rsResultado.getString("CPM_FECACTUALIZACION"));
							modificador.setnIdActualizador(rsResultado.getLong("CPM_NIDACTUALIZADOR"));
							
							
							listaModificadores.add(modificador);
						}
						log.info("cantidad: " + listaModificadores.size());
						moDataOut.put("listaModificadores", listaModificadores);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("buscarModificadores");

		return moDataOut;
	}
	
	
	public Map<String, Object> guardarCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {

		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================
		
		// Variables genericas para todo metodo
		
		iniciaParametros("guardarCatalogo");
		log.info("Inicia parametros en: guardarCatalogo()");
		
		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;
		
		// Variables para verificacion de sesion yperfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;
		
		Sesion oSesion = null;
		HashMap<String, String> perfiles = null;
		
		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		
		long nIdCatalogo = 0;
		
		Catalogo agregarCatalogo = (Catalogo) moDataIn.get("catalogo");
		log.info("CatalogoEJB -> guardarCatalogo IN");
		
		// =============================================================
		// VERIFICACION SESION
		// =============================================================
		
		oSesion = (Sesion) moDataIn.get("sesion");
		
		log.info("oesion-----------------------------> " + oSesion.getSesNombreCompleto());
		
		moBeansIn.clear();
		
		moBeansIn.put("sesion", oSesion);
		
		moBeansOut = ejbSesion.buscaPerfiles(moBeansIn);
		
		error = (Error) moBeansOut.get("error");
		
		if (!error.getNumError().equalsIgnoreCase("0")) {
			throw new ErrorSPException(error.getNumError(), error.getNumError(), error.getMsjError());
		}
		perfiles = (HashMap<String, String>) moBeansOut.get("perfiles");
		
		autorizacion = perfiles.containsKey("10000") && perfiles.containsKey("11000");
		
		if (autorizacion) {
			log.info("el perfil esta contenido: " + autorizacion);
		
			log.info("CatalogoEJB -> guardarCatalogo IN");
		
			// =============================================================
			// CUERPO DE EJB
			// =============================================================
		
			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();
		
				log.info("CatalogoEJB -> conectado y listo para llamar en guardarCatalogo()");
		
				log.info("CatalogoEJB -> guardarCatalogo");
		
				sNombreSP = "CAT$AGREGA_CATALOGO";
				sTipoSP = "TX";
		
				log.info("CatalogoEJB -> " + sNombreSP + " Inicio");
		
				// =============================================================
				// Parametros de entrada
				// =============================================================
		
				
				creaParametroIn(Types.NUMERIC, agregarCatalogo.getnIdPrestador());
				creaParametroIn(Types.VARCHAR, agregarCatalogo.getsCodCatalogo());
				creaParametroIn(Types.VARCHAR, agregarCatalogo.getsNombre());
				creaParametroIn(Types.VARCHAR, agregarCatalogo.getsDescripcion());
				creaParametroIn(Types.VARCHAR, agregarCatalogo.getsIndPlantilla());
				creaParametroIn(Types.VARCHAR, agregarCatalogo.getsIndPublico());
				creaParametroIn(Types.VARCHAR, agregarCatalogo.getsFecHoInicio());
				creaParametroIn(Types.VARCHAR, agregarCatalogo.getsFecHoTermino());
				creaParametroIn(Types.NUMERIC, agregarCatalogo.getnIdResponsable());
				creaParametroIn(Types.VARCHAR, agregarCatalogo.getsFecCreacion());
				creaParametroIn(Types.NUMERIC, agregarCatalogo.getnIdCreador());
				creaParametroIn(Types.VARCHAR, agregarCatalogo.getsFecActualizacion());
				creaParametroIn(Types.NUMERIC, Long.parseLong(oSesion.getSesIdUsuario()));
				
		
				log.info("parametros de entrada guardarCatalogo------------");
				log.info("Prestador -> " + agregarCatalogo.getnIdPrestador());
				log.info("Codigo catalogo -> " + agregarCatalogo.getsCodCatalogo());
				log.info("nombre -> " + agregarCatalogo.getsNombre());
				log.info("Descripcion -> " + agregarCatalogo.getsDescripcion());
				log.info("Nombre -> " + agregarCatalogo.getsNombre());
				log.info("Indicador plantilla -> " + agregarCatalogo.getsIndPlantilla());
				log.info("Indicador publico -> " + agregarCatalogo.getsIndPublico());
				log.info("Fecha inicio-> " + agregarCatalogo.getsFecHoInicio());
				log.info("Fecha termino -> " + agregarCatalogo.getsFecHoTermino());
				log.info("Id Responsable -> " + agregarCatalogo.getnIdResponsable());
				log.info("Fecha creacion -> " + agregarCatalogo.getsFecCreacion());
				log.info("id Creador -> " + agregarCatalogo.getnIdCreador());
				log.info("Fecha actualizacion -> " + agregarCatalogo.getsFecActualizacion());
				log.info("Id Usuario -> " + oSesion.getSesIdUsuario());
				
				
				log.info("--------------------------------");
		
				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("nIdCatalogo", Types.INTEGER);
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);
		
				try {
		
					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");
		
				} catch (Exception e) {
					log.error(e);
					error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				}
		
				if (cStmt != null) {
					log.info("Cursor != nulo");
		
					cStmt.execute();
		
					// Recuperacion parametros de salida
					numError = (Integer) getParametro(cStmt, "NumError");
					log.info("numError: " + numError);
		
					msjError = (String) getParametro(cStmt, "MsjError");
					log.info("msjError: " + msjError);
		
					if (numError != 0) {
						error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
						throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
					} else {
						nIdCatalogo = (Integer) getParametro(cStmt, "nIdCatalogo");
						log.info("Id Catalogo: " + nIdCatalogo);
						error = new Error(0, 0, "", "");
					}
					// utx.commit();
				}
		
			} catch (SQLException e) {
				log.error("CatalogoEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				// utx.rollback();
			} finally {
		
				log.info("CatalogoEJB -> " + sNombreSP + " Fin");
		
				try {
					dbConeccion.close();
					dbConeccion = null;
				} catch (SQLException e) {
					log.error(e);
					error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
					throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
				}
		
			}
		
			// =============================================================
			// FIN CUERPO DE EJB
			// =============================================================
		
		} else {
			error = new Error(4, 99, "Usuario NO puede acceder metodo en EJB", Errores.getInstance().getMsgFamilia("4"));
			throw new ErrorSPException(Long.valueOf(error.getNumFamiliaError()), Long.valueOf(error.getNumError()), error.getMsjError(),
					error.getMsjErrorUsuario());
		}
		
		moDataOut.put("error", error);
		moDataOut.put("idCatalogo", nIdCatalogo);
		
		terminaParametros("guardarCatalogo");
		
		return moDataOut;
	}
	
	public Map<String, Object> buscarEmpresasInst(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		log.info("Busca Instituciones en  CatalogoEJB");
		iniciaParametros("buscarEmpresasInst");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;
		
		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;
		
		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================

		ArrayList<Empresa> listaEmpresas = null;
		Empresa buscaEmpresa = (Empresa) moDataIn.get("empresa");

		log.info("CatalogoEJB -> buscarEmpresasInst IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en buscarEmpresasInst()");

			log.info("CatalogoEJB -> buscarEmpresasInst");

			sNombreSP = "TYC$BUSCA_INSTITUCIONES.LISTA_INSTITUCIONES";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.VARCHAR, buscaEmpresa.getsRut());
			creaParametroIn(Types.VARCHAR, buscaEmpresa.getsNombreFantasia());

			log.info("Rut Empresa: " + buscaEmpresa.getsRut());
			log.info("Nombre: " + buscaEmpresa.getsNombreFantasia());

			log.info("--------------------------------");
			
			// =============================================================
			// Parametros de Salida
			// =============================================================

			creaParametroOut("CUR_INSTITUCIONES", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();
				
				// Recuperacion parametros de salida
				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					error = new Error(0, 0, "", "");

					log.info("CatalogoEJB -> " + sNombreSP + " rsResultado Inicio");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_INSTITUCIONES");
					log.info("CatalogoEJB -> " + sNombreSP + " rsResultado Fin");

					if (rsResultado != null) {
						log.info("rsResultado != nulo");

						listaEmpresas = new ArrayList<Empresa>();

						while (rsResultado.next()) {

							Empresa emp = new Empresa();
							emp.setnIdEmpresa(rsResultado.getLong("EMP_NIDEMPRESA"));
							emp.setsRut(rsResultado.getString("EMP_SRUT"));
							emp.setsNombreFantasia(rsResultado.getString("EMP_SNOMBREFANTASIA"));
							
							listaEmpresas.add(emp);
						}
						log.info("cantidad: " + listaEmpresas.size());
						moDataOut.put("listaEmpresasInst", listaEmpresas);
						rsResultado.close();
					}
				}
			}

		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}

		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("buscarEmpresasInst");

		return moDataOut;
	}
	
	public Map<String, Object> eliminarCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		
		long idCatalogo = 0;
		idCatalogo = (Long) moDataIn.get("idCatalogo");
		
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");

		log.info("sesion-----------------------------> " + sesion.getSesNombreCompleto());

		moBeansIn.clear();

		moBeansIn.put("sesion", sesion);

		moBeansOut = ejbSesion.buscaPerfiles(moBeansIn);

		error = (Error) moBeansOut.get("error");

		if (!error.getNumError().equalsIgnoreCase("0")) {
			throw new ErrorSPException(error.getNumError(), error.getNumError(), error.getMsjError());
		}
		perfiles = (HashMap<String, String>) moBeansOut.get("perfiles");

		autorizacion = perfiles.containsKey("10000") && perfiles.containsKey("11000");

		if (autorizacion) {
			log.info("el perfil esta contenido: " + autorizacion);

			log.info("CatalogoEJB -> eliminarCatalogo IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("CatalogoEJB -> conectado y listo para llamar en eliminarCatalogo()");

				log.info("CatalogoEJB -> LOGUEO ");
				sNombreSP = "CAT$ELIMINA_CATALOGO";
				sTipoSP = "TX";

				log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				log.info("id Catalogo a eliminar: " + idCatalogo);

				creaParametroIn(Types.NUMERIC, idCatalogo);

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

				} catch (Exception e) {
					log.error(e);
					error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				}

				if (cStmt != null) {
					log.info("Cursor != nulo");

					cStmt.execute();

					// Recuperacion parametros de salida
					numError = (Integer) getParametro(cStmt, "NumError");
					log.info("numError: " + numError);

					msjError = (String) getParametro(cStmt, "MsjError");
					log.info("msjError: " + msjError);

					if (numError != 0) {
						error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
						throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
					} else {
						error = new Error(0, 0, "", "");
					}
				}

			} catch (SQLException e) {
				log.error("CatalogoEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally CatalogoEJB -> " + sNombreSP + " Fin");
					if (numError == 0) {
						log.info("Commit en " + sNombreSP);
						//utx.commit();
					} else {
						log.info("Rollback en " + sNombreSP);
						//utx.rollback();
					}

					dbConeccion.close();
					dbConeccion = null;
				} catch (SQLException e) {
					log.error(e);
					error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
					throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			}

			// =============================================================
			// FIN CUERPO DE EJB
			// =============================================================

		} else {
			error = new Error(4, 99, "Usuario NO puede acceder metodo en EJB", Errores.getInstance().getMsgFamilia("4"));
			throw new ErrorSPException(Long.valueOf(error.getNumFamiliaError()), Long.valueOf(error.getNumError()), error.getMsjError(),
					error.getMsjErrorUsuario());
		}

		moDataOut.put("error", error);

		terminaParametros("eliminaCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> guardarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarPrestacionesCatalogo");
		log.info("Inicia parametros en: guardarPrestacionesCatalogo()");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion yperfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================

		CatalogoPrest agregaPrestacion = (CatalogoPrest) moDataIn.get("catalogoPrest");
		log.info("CatalogoEJB -> guardarPrestacionesCatalogo IN");
		long IdCatalogoPrestacion=0; 
		
		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();
			// utx.begin();

			log.info("CatalogoEJB -> conectado y listo para llamar en guardarPrestacionesCatalogo()");

			log.info("CatalogoEJB -> guardarPrestacionesCatalogo ");

			sNombreSP = "CAT$AGREGA_PRESTACION";
			sTipoSP = "TX";

			log.info("EJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, agregaPrestacion.getnIdCatalogo());
			creaParametroIn(Types.NUMERIC, agregaPrestacion.getnIdPrestacion());
			creaParametroIn(Types.VARCHAR, agregaPrestacion.getsCodPrestacion());
			creaParametroIn(Types.VARCHAR, agregaPrestacion.getsNomPrestacion());
			creaParametroIn(Types.VARCHAR, agregaPrestacion.getsNomPrestCatalogo());
			creaParametroIn(Types.NUMERIC, agregaPrestacion.getnPorHonorarios());
			creaParametroIn(Types.NUMERIC, agregaPrestacion.getnPorGastos());
			creaParametroIn(Types.VARCHAR, agregaPrestacion.getsIndTipoPago());
			creaParametroIn(Types.NUMERIC, agregaPrestacion.getnIdCreador());
			
			log.info("parametros de entrada guardarPrestacionesCatalogo------------");
			log.info("Id Catalogo -> " +  agregaPrestacion.getnIdCatalogo());
			log.info("Id prestacion -> " +  agregaPrestacion.getnIdPrestacion());
			log.info("Codigo Prestacion -> " +  agregaPrestacion.getsCodPrestacion());
			log.info("Nombre Prestacion -> " +  agregaPrestacion.getsNomPrestacion());
			log.info("Nombre prestacioncatalogo -> " +  agregaPrestacion.getsNomPrestCatalogo());
			log.info("Honorarios -> " +  agregaPrestacion.getnPorHonorarios());
			log.info("Gastos-> " +  agregaPrestacion.getnPorGastos());
			log.info("Indicador tipo Pago -> " +  agregaPrestacion.getsIndTipoPago());
			log.info("Id Creador-> " +  agregaPrestacion.getnIdCreador());
			
			log.info("--------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			/* Error */
			creaParametroOut("IdCatalogoPrestacion", Types.INTEGER);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
				IdCatalogoPrestacion = (Integer) getParametro(cStmt, "IdCatalogoPrestacion");
				log.info("IdCatalogoPrestacion: " + IdCatalogoPrestacion);
				
				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {
					error = new Error(0, 0, "", "");
				}
				// utx.commit();
			}

		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}

		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================
		moDataOut.put("IdCatalogoPrestacion", IdCatalogoPrestacion);
		moDataOut.put("error", error);

		terminaParametros("guardarPrestacionesCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> buscarPrestacionesCatDispon(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================


		// Variables genericas para todo metodo
		iniciaParametros("buscarPrestacionesCatDispon");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;
		int totalRegistros = 0;
		
		// Variables para verificacion de sesion y perfiles
				Map<String, Object> moBeansIn = new HashMap<String, Object>();
				Map<String, Object> moBeansOut = null;
				boolean autorizacion = false;

				Sesion sesion = null;
				HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		ArrayList<CatalogoPrest> listaPrestacionesCatDispon = new ArrayList<CatalogoPrest>();

		CatalogoPrest catalogoPres = (CatalogoPrest) moDataIn.get("catalogoPrest");
		
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");
		
		log.info("CatalogoEJB -> buscarPrestacionesCatDispon IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en buscarPrestacionesCatDispon()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$BUSCA_PREST_CAT_DISPON.LISTA_PREST_CAT_DISPON";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC,catalogoPres.getnIdCatalogo());
			creaParametroIn(Types.VARCHAR,catalogoPres.getsCodPrestacion());
			creaParametroIn(Types.VARCHAR,catalogoPres.getsNomPrestacion());
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);
			

			log.info("parametros de entrada------------");
			log.info("idCatalogo -> " + catalogoPres.getnIdCatalogo());
			log.info("Cod Prestacion -> " + catalogoPres.getsCodPrestacion());
			log.info("Nombre Prestacion -> " + catalogoPres.getsNomPrestacion());
			log.info("Ordenar Por: "+sOrdenarPor);
			log.info("Tipo Orden: "+sTipoOrden);
			log.info("Pag Actual: "+pagActual);			
			log.info("Cant reg x pag: "+regPorPagina);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("TotalRegistros", OracleTypes.INTEGER);
			creaParametroOut("CUR_CATALOGOS", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();
				
				// Recuperacion parametros de salida
				totalRegistros = (Integer) getParametro(cStmt, "TotalRegistros");
				log.info("Total Registros: "+totalRegistros);

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {
					error = new Error("0","");
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_CATALOGOS");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							CatalogoPrest prest = new CatalogoPrest();
							prest.setnIdPrestacion(rsResultado.getLong("PRE_NIDPRESTACION"));
							prest.setsCodPrestacion(rsResultado.getString("PRE_SCODPRESTACION"));
							prest.setsNomPrestacion(rsResultado.getString("PRE_SNOMBRE"));				                
							listaPrestacionesCatDispon.add(prest);
						}
						log.info("cantidad: " + listaPrestacionesCatDispon.size());
						moDataOut.put("listaPrestacionesCatDispon", listaPrestacionesCatDispon);
						moDataOut.put("totalRegistros", totalRegistros);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("buscarPrestacionesCatDispon");

		return moDataOut;
	}
	
	public Map<String, Object> eliminarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarPrestacion");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		
		long idCatalogo = 0;
		idCatalogo = (Long) moDataIn.get("idCatalogo");
		String listaIdCatalogos = (String)moDataIn.get("lista");
		
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");

		log.info("sesion-----------------------------> " + sesion.getSesNombreCompleto());

		moBeansIn.clear();

		moBeansIn.put("sesion", sesion);

		moBeansOut = ejbSesion.buscaPerfiles(moBeansIn);

		error = (Error) moBeansOut.get("error");

		if (!error.getNumError().equalsIgnoreCase("0")) {
			throw new ErrorSPException(error.getNumError(), error.getNumError(), error.getMsjError());
		}
		perfiles = (HashMap<String, String>) moBeansOut.get("perfiles");

		autorizacion = perfiles.containsKey("10000") && perfiles.containsKey("11000");

		if (autorizacion) {
			log.info("el perfil esta contenido: " + autorizacion);

			log.info("CatalogoEJB -> eliminarPrestacion IN");

			try {
				
				
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("CatalogoEJB -> conectado y listo para llamar en eliminarPrestacion()");

				log.info("CatalogoEJB -> LOGUEO ");
				
				String listaId[] = listaIdCatalogos.split(",");
				
				log.info("tamaño" + listaId.length);
				
				for (int i= 0;i<listaId.length;i++ )
				{
					log.info("LISTA : " + listaId[i]);
				}
				
				for (int i= 0;i<listaId.length;i++)
				{	
					long idCatPrestacion = Long.parseLong(listaId[i].trim());
					
					log.info("idCatPrestacion: " + idCatPrestacion);
					
					sNombreSP = "CAT$ELIMINA_PRESTACION";
					sTipoSP = "TX";
	
					log.info("CatalogoEJB -> " + sNombreSP + " Inicio");
	
					// =============================================================
					// Parametros de entrada
					// =============================================================
	
					log.info("id Catalogo: " + idCatalogo);
					log.info("id Catalogo Prestacion a eliminar: " + idCatPrestacion);
	
					creaParametroIn(Types.NUMERIC, idCatalogo);
					creaParametroIn(Types.NUMERIC, idCatPrestacion);
	
					// =============================================================
					// Parametros de Salida
					// =============================================================
					/* Error */
					creaParametroOut("NumError", Types.INTEGER);
					creaParametroOut("MsjError", Types.VARCHAR);
	
					try {
	
						log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
						cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
						log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");
	
					} catch (Exception e) {
						log.error(e);
						error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
						throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
					}
	
					if (cStmt != null) {
						log.info("Cursor != nulo");
	
						cStmt.execute();
	
						// Recuperacion parametros de salida
						numError = (Integer) getParametro(cStmt, "NumError");
						log.info("numError: " + numError);
	
						msjError = (String) getParametro(cStmt, "MsjError");
						log.info("msjError: " + msjError);
	
						if (numError != 0) {
							error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
							throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
						} else {
							error = new Error(0, 0, "", "");
						}
					}
					terminaParametros("eliminarPrestacion");
				}
			} catch (SQLException e) {
				log.error("CatalogoEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally CatalogoEJB -> " + sNombreSP + " Fin");

					dbConeccion.close();
					dbConeccion = null;
				} catch (SQLException e) {
					log.error(e);
					error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
					throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			}

			// =============================================================
			// FIN CUERPO DE EJB
			// =============================================================

		} else {
			error = new Error(4, 99, "Usuario NO puede acceder metodo en EJB", Errores.getInstance().getMsgFamilia("4"));
			throw new ErrorSPException(Long.valueOf(error.getNumFamiliaError()), Long.valueOf(error.getNumError()), error.getMsjError(),
					error.getMsjErrorUsuario());
		}

		moDataOut.put("error", error);
		
		return moDataOut;
	}
	
	public Map<String, Object> actualizarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizarPrestacionesCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		CatalogoPrest actualizaprestacioncatalogo = (CatalogoPrest) moDataIn.get("catalogoPrest");

		// =============================================================
		// VERIFICACION SESION
		// =============================================================


			try {
				dbConeccion = solemDS.getConnection();

				log.info("CatalogoEJB -> conectado y listo para llamar en actualizarPrestacionesCatalogo()");

				log.info("CatalogoEJB -> LOGUEO ");
				sNombreSP = "CAT$ACTUALIZA_PRESTACION";
				sTipoSP = "TX";

				log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				creaParametroIn(Types.NUMERIC, actualizaprestacioncatalogo.getnIdCatalogoPrestacion());
				creaParametroIn(Types.VARCHAR, actualizaprestacioncatalogo.getsNomPrestCatalogo());
				creaParametroIn(Types.VARCHAR, actualizaprestacioncatalogo.getsIndTipoPago());
				creaParametroIn(Types.NUMERIC, actualizaprestacioncatalogo.getnPorGastos());
				creaParametroIn(Types.NUMERIC, actualizaprestacioncatalogo.getnPorHonorarios());
				/*creaParametroIn(Types.VARCHAR, actualizaprestacioncatalogo.getsFecActualizacion());*/
				creaParametroIn(Types.NUMERIC, actualizaprestacioncatalogo.getnIdActualizador());

				log.info("parametros de entrada catalogo------------");
				log.info("Id CatalogoPrestacion -> " + actualizaprestacioncatalogo.getnIdCatalogoPrestacion());
				log.info("nombre prestacion -> " + actualizaprestacioncatalogo.getsNomPrestCatalogo());
				log.info("Indicadro tipopago-> " + actualizaprestacioncatalogo.getsIndTipoPago());
				log.info("Gastos -> " + actualizaprestacioncatalogo.getnPorGastos());
				log.info("Honorarios -> " + actualizaprestacioncatalogo.getnPorHonorarios());
				/*log.info("Fecha Actualizador -> " + actualizaprestacioncatalogo.getsFecActualizacion());*/
				log.info("Usuario -> " + actualizaprestacioncatalogo.getnIdActualizador());
				
				
				log.info("--------------------------------");
				 

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

				} catch (Exception e) {
					log.error(e);
					error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				}

				if (cStmt != null) {
					log.info("Cursor != nulo");

					cStmt.execute();

					// Recuperacion parametros de salida
					numError = (Integer) getParametro(cStmt, "NumError");
					log.info("numError: " + numError);

					msjError = (String) getParametro(cStmt, "MsjError");
					log.info("msjError: " + msjError);

					if (numError != 0) {
						error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
						throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
					} else {
						error = new Error(0, 0, "", "");
					}
					// utx.commit();
				}

			} catch (SQLException e) {
				log.error("CatalogoEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				// utx.rollback();
			} finally {

				log.info("CatalogoEJB -> " + sNombreSP + " Fin");

				try {
					dbConeccion.close();
					dbConeccion = null;
				} catch (SQLException e) {
					log.error(e);
					error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
					throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
				}

			}

			// =============================================================
			// FIN CUERPO DE EJB
			// =============================================================

		
		moDataOut.put("error", error);

		terminaParametros("actualizarPrestacionesCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> cargarModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Modificador");

		// Variables genericas para todo metodo
		iniciaParametros("cargarModificadoresCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		
		
		

		ModificadorCatalogo modificador = null;

		long idCatalogoModificador = 0;
		

		idCatalogoModificador = (Long) moDataIn.get("idCatalogoModificador");

		log.info("CatalogoEJB -> cargarModificadoresCatalogo IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en cargarModificadoresCatalogo()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$CARGA_MODIFICADORES.CATPRESTACIONM";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idCatalogoModificador);

			log.info("parametros de entrada------------");
			log.info("idCatalogoModificador -> " + idCatalogoModificador);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_CATPRESTACION", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					//error = new Error(0, 0, "", "");
					error = new Error("0","");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_CATPRESTACION");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							modificador = new ModificadorCatalogo();
							modificador.setnIdCatalogoModificador(rsResultado.getLong("CPM_NIDCATALOGOMODIFICADOR"));
							modificador.setnIdCatalogo(rsResultado.getLong("CPM_NIDCATALOGO"));
							modificador.setnIdPrestacion(rsResultado.getLong("CPM_NIDPRESTACION"));
							modificador.setsCodTipo(rsResultado.getString("CPM_SCODTIPO"));
							modificador.setsNombre(rsResultado.getString("CPM_SNOMBRE"));
							modificador.setsFhoInicio(rsResultado.getString("CPM_DFHOINICIO"));
							modificador.setsFhoFin(rsResultado.getString("CPM_DFHOFIN"));
							modificador.setsCondicion(rsResultado.getString("CPM_SCONDICION"));
							modificador.setsFormula(rsResultado.getString("CPM_SFORMULA"));

						}
						moDataOut.put("modificador", modificador);
						rsResultado.close();
						
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("cargarModificadoresCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> actualizarModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizarModificadoresCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		ModificadorCatalogo modificadorCatalogo = (ModificadorCatalogo) moDataIn.get("modificadorCatalogo");

		log.info("CatalogoEJB -> actualizarModificadoresCatalogo IN");

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en actualizarModificadoresCatalogo()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$ACTUALIZA_MODIFICADOR";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, modificadorCatalogo.getnIdCatalogoModificador());
			creaParametroIn(Types.VARCHAR, modificadorCatalogo.getsCodTipo());
			creaParametroIn(Types.VARCHAR, modificadorCatalogo.getsNombre());
			creaParametroIn(Types.VARCHAR, modificadorCatalogo.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, modificadorCatalogo.getsFhoFin());
			creaParametroIn(Types.VARCHAR, modificadorCatalogo.getsCondicion());
			creaParametroIn(Types.VARCHAR, modificadorCatalogo.getsFormula());
			creaParametroIn(Types.NUMERIC, modificadorCatalogo.getnIdActualizador());

			log.info("parametros de entrada anexo------------");
			log.info("Id Modificador -> " + modificadorCatalogo.getnIdCatalogoModificador());
			log.info("Tipo -> " + modificadorCatalogo.getsCodTipo());
			log.info("Nombre -> " + modificadorCatalogo.getsNombre());
			log.info("Fecha inicio -> " + modificadorCatalogo.getsFhoInicio());
			log.info("Fecha Fin -> " + modificadorCatalogo.getsFhoFin());
			log.info("Condicion -> " + modificadorCatalogo.getsCondicion());
			log.info("Formula -> " + modificadorCatalogo.getsFormula());
			log.info("Actualizador -> " + modificadorCatalogo.getnIdActualizador());
			log.info("--------------------------------");
			 

			// =============================================================
			// Parametros de Salida
			// =============================================================
			/* Error */
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {
					error = new Error(0, 0, "", "");
				}
				// utx.commit();
			}

		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}

		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("actualizarModificadoresCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> guardarModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarModificadoresCatalogo");
		log.info("Inicia parametros en: guardarModificadoresCatalogo()");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion yperfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================

		ModificadorCatalogo agregaModificador = (ModificadorCatalogo) moDataIn.get("modificadorCatalogo");
		log.info("CatalogoEJB -> guardarModificadoresCatalogo IN");
		long IdCatalogoModificador=0; 
		
		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();
			// utx.begin();

			log.info("CatalogoEJB -> conectado y listo para llamar en guardarModificadoresCatalogo()");

			log.info("CatalogoEJB -> guardarModificadoresCatalogo ");

			sNombreSP = "CAT$AGREGA_PRESTMODIFICADO";
			sTipoSP = "TX";

			log.info("EJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, agregaModificador.getnIdCatalogoPrestacion());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsNombre());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsCodTipo());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsCondicion());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsFormula());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsFhoFin());
			creaParametroIn(Types.NUMERIC, agregaModificador.getnIdCreador());
			
			
			log.info("parametros de entrada guardarModificadoresCatalogo------------");
			log.info("Id Catalogo Prestacion   ->   "  +  agregaModificador.getnIdCatalogoPrestacion());
			log.info("Nombre Modificador       ->   "  +  agregaModificador.getsNombre());
			log.info("Tipo Modificador         ->   "  +  agregaModificador.getsCodTipo());
			log.info("Condicion Modificado     ->   "  +  agregaModificador.getsCondicion());
			log.info("Formula Modificador      ->   "  +  agregaModificador.getsFormula());
			log.info("Fecha Inicio Modificador ->   "  +  agregaModificador.getsFhoInicio());
			log.info("Fecha Fin Modificador    ->   "  +  agregaModificador.getsFhoFin());
			log.info("Id Creador               ->   "  +  agregaModificador.getnIdCreador());
			log.info("-------------------------------------------------------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			/* Error */
			creaParametroOut("IdCatalogoModificador", Types.INTEGER);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
				IdCatalogoModificador = (Integer) getParametro(cStmt, "IdCatalogoModificador");
				log.info("IdCatalogoModificador: " + IdCatalogoModificador);
				
				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {
					error = new Error(0, 0, "", "");
				}
				// utx.commit();
			}

		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}

		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================
		moDataOut.put("IdCatalogoModificador", IdCatalogoModificador);
		moDataOut.put("error", error);

		terminaParametros("guardarModificadoresCatalogo");

		return moDataOut;
	}

	public Map<String, Object> eliminarModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarModificadoresCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		
		ModificadorCatalogo eliminarmodificadorCatalogo = (ModificadorCatalogo) moDataIn.get("modificadorCatalogo");
		
		
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");

		log.info("sesion-----------------------------> " + sesion.getSesNombreCompleto());

		moBeansIn.clear();

		moBeansIn.put("sesion", sesion);

		moBeansOut = ejbSesion.buscaPerfiles(moBeansIn);

		error = (Error) moBeansOut.get("error");

		if (!error.getNumError().equalsIgnoreCase("0")) {
			throw new ErrorSPException(error.getNumError(), error.getNumError(), error.getMsjError());
		}
		perfiles = (HashMap<String, String>) moBeansOut.get("perfiles");

		autorizacion = perfiles.containsKey("10000") && perfiles.containsKey("11000");

		if (autorizacion) {
			log.info("el perfil esta contenido: " + autorizacion);

			log.info("CatalogoEJB -> eliminarModificadoresCatalogo IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("CatalogoEJB -> conectado y listo para llamar en eliminarModificadoresCatalogo()");

				log.info("CatalogoEJB -> LOGUEO ");
				sNombreSP = "CAT$ELIMINA_MODIFICADOR";
				sTipoSP = "TX";

				log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				

				creaParametroIn(Types.NUMERIC, eliminarmodificadorCatalogo.getnIdCatalogoModificador());
				creaParametroIn(Types.NUMERIC,eliminarmodificadorCatalogo.getnIdCreador());
				
				log.info("parametros de eliminar modificador-----------");
				log.info("Id Modificador -> " + eliminarmodificadorCatalogo.getnIdCatalogoModificador());
				log.info("Actualizador -> " + eliminarmodificadorCatalogo.getnIdActualizador());
				log.info("------------------------------------------------------------------------------");

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

				} catch (Exception e) {
					log.error(e);
					error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				}

				if (cStmt != null) {
					log.info("Cursor != nulo");

					cStmt.execute();

					// Recuperacion parametros de salida
					numError = (Integer) getParametro(cStmt, "NumError");
					log.info("numError: " + numError);

					msjError = (String) getParametro(cStmt, "MsjError");
					log.info("msjError: " + msjError);

					if (numError != 0) {
						error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
						throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
					} else {
						error = new Error(0, 0, "", "");
					}
				}

			} catch (SQLException e) {
				log.error("CatalogoEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally CatalogoEJB -> " + sNombreSP + " Fin");
					if (numError == 0) {
						log.info("Commit en " + sNombreSP);
						//utx.commit();
					} else {
						log.info("Rollback en " + sNombreSP);
						//utx.rollback();
					}

					dbConeccion.close();
					dbConeccion = null;
				} catch (SQLException e) {
					log.error(e);
					error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
					throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			}

			// =============================================================
			// FIN CUERPO DE EJB
			// =============================================================

		} else {
			error = new Error(4, 99, "Usuario NO puede acceder metodo en EJB", Errores.getInstance().getMsgFamilia("4"));
			throw new ErrorSPException(Long.valueOf(error.getNumFamiliaError()), Long.valueOf(error.getNumError()), error.getMsjError(),
					error.getMsjErrorUsuario());
		}

		moDataOut.put("error", error);

		terminaParametros("eliminarModificadoresCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> validarCondicionModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("valida condicion del modificador de la prestacion de un Convenio");

		// Variables genericas para todo metodo
		iniciaParametros("validarCondicion");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		

		String sCondicion = "";

		sCondicion = (String) moDataIn.get("condicion");

		log.info("CatalogoEJB -> validarCondicion IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en buscarRestricciones()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "TYC$VALIDA_CONDICION";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.VARCHAR, sCondicion);

			log.info("parametros de entrada------------");
			log.info("sCondicion -> " + sCondicion);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR); 

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {
					error = new Error("0","");			
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("validarCondicion");

		return moDataOut;
	}
	
	public Map<String, Object> buscarResponsablesCatalogos(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Usuarios que pueden ser repsonsables");

		// Variables genericas para todo metodo
		iniciaParametros("buscarResponsables");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		ArrayList<Usuario> listaResponsables = new ArrayList<Usuario>();

		long idEmpresa = 0;
		Usuario buscaResponsable = (Usuario) moDataIn.get("usuario");

		idEmpresa = (Long) moDataIn.get("idEmpresa");
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");

		log.info("CatalogoEJB -> buscarResponsables IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en buscarResponsables()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "TYC$BUSCA_RESPONSABLES.BUSCA_RESPONSABLES";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idEmpresa);
			creaParametroIn(Types.VARCHAR, buscaResponsable.getsCodCargo());
			creaParametroIn(Types.VARCHAR, buscaResponsable.getsNombre());
			creaParametroIn(Types.VARCHAR, buscaResponsable.getsApePaterno());
			creaParametroIn(Types.VARCHAR, buscaResponsable.getsApeMaterno());
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);

			log.info("parametros de entrada------------");
			log.info("Nombre Responsable -> " + buscaResponsable.getsNombre());
			log.info("ApPaterno	Responsable -> " + buscaResponsable.getsApePaterno());
			log.info("Cod Cargo-> " + buscaResponsable.getsCodCargo());
			log.info("Id Empresa -> " + idEmpresa);
			log.info("Ordenar Por: "+sOrdenarPor);
			log.info("Tipo Orden: "+sTipoOrden);
			log.info("Pag Actual: "+pagActual);			
			log.info("Cant reg x pag: "+regPorPagina);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("TotalRegistros", Types.INTEGER);
			creaParametroOut("CUR_RESPONSABLES", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					//error = new Error(0, 0, "", "");
					error = new Error("0","");
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_RESPONSABLES");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							Usuario usuario = new Usuario();
							usuario.setnIdUsuario(rsResultado.getLong("USU_NIDUSUARIO"));
							usuario.setsRunUsuario(rsResultado.getString("USU_SRUNUSUARIO"));
							usuario.setsApePaterno(rsResultado.getString("USU_SAPEPATERNO"));
							usuario.setsApeMaterno(rsResultado.getString("USU_SAPEMATERNO"));
							usuario.setsNombre(rsResultado.getString("USU_SNOMBRES"));
							usuario.setsCodCargo(rsResultado.getString("USU_SCODCARGO"));
							usuario.setsDesCargo(rsResultado.getString("USU_SDESCARGO"));
							usuario.setnIdEmpresa(rsResultado.getLong("EMP_NIDEMPRESA"));
							usuario.setsNombreEmpresa(rsResultado.getString("EMP_SNOMBREFANTASIA"));
							
							listaResponsables.add(usuario);
						}
						log.info("cantidad: " + listaResponsables.size());
						moDataOut.put("listaResponsables", listaResponsables);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("buscarResponsables");

		return moDataOut;
	}
	
	public Map<String, Object> guardarPreciosCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarPreciosCatalogo");
		log.info("Inicia parametros en: guardarPreciosCatalogo()");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion yperfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================

		PrecioCatalogo agregaPrecio = (PrecioCatalogo) moDataIn.get("precioCatalogo");
		log.info("CatalogoEJB -> guardarPreciosCatalogo IN");
		long idCatalogoPrecio=0; 
		
		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();
			// utx.begin();

			log.info("CatalogoEJB -> conectado y listo para llamar en guardarPreciosCatalogo()");

			log.info("CatalogoEJB -> guardarPreciosCatalogo ");

			sNombreSP = "CAT$AGREGA_PRESTPRECIO";
			sTipoSP = "TX";

			log.info("EJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, agregaPrecio.getnIdCatalogoPrestacion());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsCodTipoMoneda());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsFhoFin());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsIndPrecioFormula());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsFormula());
			creaParametroIn(Types.NUMERIC, agregaPrecio.getnPrecio());
			creaParametroIn(Types.NUMERIC, agregaPrecio.getnIdUsuarioCreacion());
			
			
			log.info("parametros de entrada guardarPreciosCatalogo------------");
			log.info("Id Catalogo Prestacion   ->   "  +  agregaPrecio.getnIdCatalogoPrestacion());
			log.info("Cod Tipo Moneda -> " + agregaPrecio.getsCodTipoMoneda());
			log.info("Fecha Inicio Precio      ->   "  +  agregaPrecio.getsFhoInicio());
			log.info("Fecha Fin Precio        ->   "  +  agregaPrecio.getsFhoFin());
			log.info("Indicador Formula Precio     ->   "  +  agregaPrecio.getsIndPrecioFormula());
			log.info("Formula Precio     ->   "  +  agregaPrecio.getsFormula());
			log.info("Precio ->   "  +  agregaPrecio.getnPrecio());
			log.info("Id Usuario    ->   "  +  agregaPrecio.getnIdUsuarioCreacion());
			
			log.info("-------------------------------------------------------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			/* Error */
			creaParametroOut("idCatalogoPrecio", Types.INTEGER);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
				idCatalogoPrecio = (Integer) getParametro(cStmt, "idCatalogoPrecio");
				log.info("idCatalogoPrecio: " + idCatalogoPrecio);
				
				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {
					error = new Error(0, 0, "", "");
				}
				// utx.commit();
			}

		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}

		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================
		moDataOut.put("idCatalogoPrecio", idCatalogoPrecio);
		moDataOut.put("error", error);

		terminaParametros("guardarPreciosCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> cargarPrecioCatalogo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga precio catalogo");

		// Variables genericas para todo metodo
		iniciaParametros("cargarPrecioCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		
		
		

		PrecioCatalogo preciocatalogo = null;

		long idCatalogoPrecio = 0;
		

		idCatalogoPrecio = (Long) moDataIn.get("idCatalogoPrecio");

		log.info("CatalogoEJB -> cargarPrecioCatalogo IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en cargarPrecioCatalogo()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$CARGA_PRECIO.CATPRESTACIONP";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idCatalogoPrecio);

			log.info("parametros de entrada------------");
			log.info("idCatalogoPrecio -> " + idCatalogoPrecio);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_PRECIO_PREST", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					//error = new Error(0, 0, "", "");
					error = new Error("0","");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRECIO_PREST");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							preciocatalogo = new PrecioCatalogo();
							
							preciocatalogo.setnIdCatalogoPrecio(rsResultado.getLong("CPC_NIDCATALOGOPRECIO"));
							preciocatalogo.setnIdCatalogo(rsResultado.getLong("CPC_NIDCATALOGO"));
							preciocatalogo.setnIdPrestacion(rsResultado.getLong("CPC_NIDPRESTACION"));
							preciocatalogo.setsFhoInicio(rsResultado.getString("CPC_DFHOINICIO"));
							preciocatalogo.setsFhoFin(rsResultado.getString("CPC_DFHOFIN"));
							preciocatalogo.setsIndPrecioFormula(rsResultado.getString("CPC_SINDPRECIOOFORMULA"));
							preciocatalogo.setsFormula(rsResultado.getString("CPC_SFORMULA"));
							preciocatalogo.setnPrecio(rsResultado.getLong("CPC_NPRECIO"));
							preciocatalogo.setsNombrePrestacion(rsResultado.getString("CPR_SNOMPRESTACION"));
							preciocatalogo.setsCodTipoMoneda(rsResultado.getString("CPC_SCODTIPOMONEDA"));

						}
						moDataOut.put("preciocatalogo", preciocatalogo);
						rsResultado.close();
						
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("cargarPrecioCatalogo");

		return moDataOut;
	}
		
	public Map<String, Object> actualizarPreciosCatalogo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizarPreciosCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		PrecioCatalogo precioCatalogo = (PrecioCatalogo) moDataIn.get("precioCatalogo");

		log.info("CatalogoEJB -> actualizarPreciosCatalogo IN");

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en actualizarPreciosCatalogo()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "CAT$ACTUALIZA_PRECIO";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, precioCatalogo.getnIdCatalogoPrecio());
			creaParametroIn(Types.VARCHAR, precioCatalogo.getsCodTipoMoneda());
			creaParametroIn(Types.VARCHAR, precioCatalogo.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, precioCatalogo.getsFhoFin());
			creaParametroIn(Types.VARCHAR, precioCatalogo.getsIndPrecioFormula());
			creaParametroIn(Types.VARCHAR, precioCatalogo.getsFormula());
			creaParametroIn(Types.NUMERIC, precioCatalogo.getnPrecio());
			creaParametroIn(Types.NUMERIC, precioCatalogo.getnIdUsuarioModificacion());

			log.info("parametros de entrada Precio Catalogo------------");
			
			log.info("Id Precio -> " + precioCatalogo.getnIdCatalogoPrecio());
			log.info("Cod Tipo Moneda -> " + precioCatalogo.getsCodTipoMoneda());
			log.info("Fecha Inicio -> " + precioCatalogo.getsFhoInicio());
			log.info("Fecha fin -> " + precioCatalogo.getsFhoFin());
			log.info("Indicador Precioformula -> " + precioCatalogo.getsIndPrecioFormula());
			log.info("Formula -> " + precioCatalogo.getsFormula());
			log.info("Precio -> " + precioCatalogo.getnPrecio());
			log.info("Actualizador -> " + precioCatalogo.getnIdUsuarioModificacion());
			log.info("--------------------------------");
			 

			// =============================================================
			// Parametros de Salida
			// =============================================================
			/* Error */
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {
					error = new Error(0, 0, "", "");
				}
				// utx.commit();
			}

		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}

		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("actualizarPreciosCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> eliminarPreciosCatalogo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarPreciosCatalogo");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		boolean autorizacion = false;

		Sesion sesion = null;
		HashMap<String, String> perfiles = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		
		PrecioCatalogo eliminarprecioCatalogo = (PrecioCatalogo) moDataIn.get("precioCatalogo");
		
		
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");

		log.info("sesion-----------------------------> " + sesion.getSesNombreCompleto());

		moBeansIn.clear();

		moBeansIn.put("sesion", sesion);

		moBeansOut = ejbSesion.buscaPerfiles(moBeansIn);

		error = (Error) moBeansOut.get("error");

		if (!error.getNumError().equalsIgnoreCase("0")) {
			throw new ErrorSPException(error.getNumError(), error.getNumError(), error.getMsjError());
		}
		perfiles = (HashMap<String, String>) moBeansOut.get("perfiles");

		autorizacion = perfiles.containsKey("10000") && perfiles.containsKey("11000");

		if (autorizacion) {
			log.info("el perfil esta contenido: " + autorizacion);

			log.info("CatalogoEJB -> eliminarPreciosCatalogo IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("CatalogoEJB -> conectado y listo para llamar en eliminarPreciosCatalogo()");

				log.info("CatalogoEJB -> LOGUEO ");
				sNombreSP = "CAT$ELIMINA_PRECIO";
				sTipoSP = "TX";

				log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				

				creaParametroIn(Types.NUMERIC, eliminarprecioCatalogo.getnIdCatalogoPrecio());
				creaParametroIn(Types.NUMERIC,eliminarprecioCatalogo.getnIdUsuarioModificacion());
				
				log.info("parametros de eliminar modificador-----------");
				log.info("Id precio -> " + eliminarprecioCatalogo.getnIdCatalogoPrecio());
				log.info("Id Actualizador -> " + eliminarprecioCatalogo.getnIdUsuarioModificacion());
				log.info("------------------------------------------------------------------------------");

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("CatalogoEJB -> " + sNombreSP + " cStmt Fin");

				} catch (Exception e) {
					log.error(e);
					error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				}

				if (cStmt != null) {
					log.info("Cursor != nulo");

					cStmt.execute();

					// Recuperacion parametros de salida
					numError = (Integer) getParametro(cStmt, "NumError");
					log.info("numError: " + numError);

					msjError = (String) getParametro(cStmt, "MsjError");
					log.info("msjError: " + msjError);

					if (numError != 0) {
						error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
						throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
					} else {
						error = new Error(0, 0, "", "");
					}
				}

			} catch (SQLException e) {
				log.error("CatalogoEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally CatalogoEJB -> " + sNombreSP + " Fin");
					if (numError == 0) {
						log.info("Commit en " + sNombreSP);
						//utx.commit();
					} else {
						log.info("Rollback en " + sNombreSP);
						//utx.rollback();
					}

					dbConeccion.close();
					dbConeccion = null;
				} catch (SQLException e) {
					log.error(e);
					error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
					throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
				} catch (Exception e) {
					log.error(e);
					e.printStackTrace();
				}
			}

			// =============================================================
			// FIN CUERPO DE EJB
			// =============================================================

		} else {
			error = new Error(4, 99, "Usuario NO puede acceder metodo en EJB", Errores.getInstance().getMsgFamilia("4"));
			throw new ErrorSPException(Long.valueOf(error.getNumFamiliaError()), Long.valueOf(error.getNumError()), error.getMsjError(),
					error.getMsjErrorUsuario());
		}

		moDataOut.put("error", error);

		terminaParametros("eliminarPreciosCatalogo");

		return moDataOut;
	}
	
	public Map<String, Object> validarFormula(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("valida formula");

		// Variables genericas para todo metodo
		iniciaParametros("validarFormula");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================

		String sFormula = "";

		sFormula = (String) moDataIn.get("formula");

		log.info("CatalogoEJB -> validarFormula IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en validarFormula()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = "TYC$VALIDA_FORMULA";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.VARCHAR, sFormula);

			log.info("parametros de entrada------------");
			log.info("sFormula -> " + sFormula);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR); 

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {
					error = new Error("0","");			
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("validarFormula");

		return moDataOut;
	}
	
	public Map<String, Object> buscarValoresUniOrg(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Valores Unidades Organizacionales");

		// Variables genericas para todo metodo
		iniciaParametros("buscarUniOrg");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		ArrayList<UniOrg> listaUniOrg = new ArrayList<UniOrg>();

		long idCatalogo = 0;

		idCatalogo = (Long) moDataIn.get("idCatalogo");

		log.info("CatalogoEJB -> buscarUniOrg IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("CatalogoEJB -> conectado y listo para llamar en buscarUniOrg()");

			log.info("CatalogoEJB -> LOGUEO ");
			sNombreSP = " CAT$BUSCA_UNI_ORG.LISTA_UNIDADORG";
			sTipoSP = "TX";

			log.info("CatalogoEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idCatalogo);

			log.info("parametros de entrada------------");
			log.info("idCatalogo -> " + idCatalogo);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_UNID_ORG", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {

				cStmt.execute();

				numError = (Integer) getParametro(cStmt, "NumError");
				log.info("numError: " + numError);

				msjError = (String) getParametro(cStmt, "MsjError");
				log.info("msjError: " + msjError);

				if (numError != 0) {
					error = new Error(3, numError, msjError, Errores.getInstance().getMsgFamilia("3"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				} else {

					//error = new Error(0, 0, "", "");
					error = new Error("0","");
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_UNID_ORG");
					
					if (rsResultado != null) {
						 
						while (rsResultado.next()) 
						{
							log.info("cantidad: " + listaUniOrg.size());
							UniOrg uniOrg = new UniOrg();
							
							uniOrg.setnHijoUorIdUnidadOrg(rsResultado.getLong("UOR_NIDUNIDADORG")); 
							uniOrg.setsHijoUorDescripcion(rsResultado.getString("UOR_SDESCRIPCION")); 
							uniOrg.setsHijoUorCodUnidadOrg(rsResultado.getString("UOR_SCODUNIDADORG"));   
					          
							/*
							uniOrg.setsHijoUorCodTipoUnidad(rsResultado.getString("HIJO.UOR_SCODTIPOUNIDAD")); 
							uniOrg.setsHijoUorCodEstado(rsResultado.getString("HIJO.UOR_SCODESTADO")); 
							uniOrg.setsFecCreacion(rsResultado.getString("DFECCREACION")); 
							uniOrg.setsHijoUorIndVigencia(rsResultado.getString("HIJO.UOR_SINDVIGENCIA")); 
							uniOrg.setnPadreUorIdUnidadOrg(rsResultado.getLong("PADRE.UOR_NIDUNIDADORG")); 
							uniOrg.setsPadreUorDescripcion(rsResultado.getString("PADRE.UOR_SDESCRIPCION")); 
							uniOrg.setsHijoUorIndUltimoNivel(rsResultado.getString("HIJO.UOR_SINDULTNIVEL"));  
							*/
							
							listaUniOrg.add(uniOrg);
						}
						log.info("cantidad: " + listaUniOrg.size());
						moDataOut.put("listaUniOrg", listaUniOrg);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("CatalogoEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("CatalogoEJB -> " + sNombreSP + " Fin");

			try {
				dbConeccion.close();
				dbConeccion = null;
			} catch (SQLException e) {
				log.error(e);
				error = new Error(2, "9999", e.getMessage(), "Error desconectando de la Base de Datos", Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, 9999, "Error desconectando de la Base de Datos", error.getMsjErrorUsuario());
			}
		}

		// =============================================================
		// FIN CUERPO DE EJB
		// =============================================================

		moDataOut.put("error", error);

		terminaParametros("buscarUniOrg");

		return moDataOut;
	}
	
}
