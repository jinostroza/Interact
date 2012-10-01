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
import cl.solem.tyc.beans.Prestacion;
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.ServletParametros;
import cl.solem.tyc.beans.Sesion;
import cl.solem.tyc.ejb.interfaces.PrestacionEJBRemote;
import cl.solem.tyc.ejb.interfaces.SesionEJBRemote;

@Stateless(mappedName = "tyc/PrestacionEJB")
public class PrestacionEJB extends EjbBase implements PrestacionEJBRemote{
	
	private Logger log = Logger.getLogger(PrestacionEJB.class);
	@Resource(mappedName = "java:/SolemBilling")
	protected DataSource solemDS;
	
	private Error error = null;
		
	@EJB(mappedName = "tyc/SesionEJB")
	private SesionEJBRemote ejbSesion;

	public PrestacionEJB() {
		super();
		setSolemDataSource(solemDS);
	}

	public Map<String, Object> buscarPrestaciones(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		iniciaParametros("buscar Prestaciones");

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

		HashMap<String, Object> mapaSalida = null;
		mapaSalida = new HashMap<String, Object>();
		ArrayList<Prestacion> listaPrestaciones = null;
		Prestacion m_prestacion = (Prestacion)moDataIn.get("prestacion");
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");
		
		try {
			dbConeccion = solemDS.getConnection();

			log.info("PrestacionEJB -> conectado y listo para llamar en buscarPrestacion()");

			log.info("PrestacionEJB -> buscarPrestacion");

			sNombreSP = "PRE$BUSCA_PRESTACIONES.LISTA_PRESTACIONES";
			sTipoSP = "TX";

			log.info("PrestacionEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			log.info("Codigo Prestacion: " + m_prestacion.getsCodPrestacion());
			log.info("Nombre Prestacion: " + m_prestacion.getsNombre());
			log.info("Tipo Prestacion: " + m_prestacion.getsCodTipoPrestacion());
			log.info("Estado: " + m_prestacion.getsCodEstado());
			log.info("Ordenar Por: "+sOrdenarPor);
			log.info("Tipo Orden: "+sTipoOrden);
			log.info("Pag Actual: "+pagActual);			
			log.info("Cant reg x pag: "+regPorPagina);
			
			creaParametroIn(Types.VARCHAR, m_prestacion.getsCodPrestacion());
			creaParametroIn(Types.VARCHAR, m_prestacion.getsNombre());
			creaParametroIn(Types.VARCHAR, m_prestacion.getsCodTipoPrestacion());
			creaParametroIn(Types.VARCHAR, m_prestacion.getsCodEstado());
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);
			
			// =============================================================
			// Parametros de Salida
			// =============================================================

			creaParametroOut("TotalRegistros", OracleTypes.INTEGER);
			creaParametroOut("CUR_PRESTACIONES", OracleTypes.CURSOR);
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

					error = new Error(0, 0, "", "");

					log.info("PrestacionEJB -> " + sNombreSP + " rsResultado Inicio");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRESTACIONES");
					log.info("PrestacionEJB -> " + sNombreSP + " rsResultado Fin");

					if (rsResultado != null) {
						log.info("rsResultado != nulo");

						listaPrestaciones = new ArrayList<Prestacion>();

						while (rsResultado.next()) {
							Prestacion prestacion = new Prestacion();
							prestacion.setnIdPrestacion(rsResultado.getLong("PRE_NIDPRESTACION"));
							prestacion.setsCodPrestacion(rsResultado.getString("PRE_SCODPRESTACION"));
							prestacion.setsNombre(rsResultado.getString("PRE_SNOMBRE"));
							prestacion.setsDescripcion(rsResultado.getString("PRE_SDESCRIPCION"));
							prestacion.setsIndVigencia(rsResultado.getString("PRE_SINDVIGENCIA").equals("S")?"SI":"NO");
							prestacion.setsCodTipoPrestacion(rsResultado.getString("PRE_SCODTIPOPRESTACION"));
							prestacion.setsDesEstado(rsResultado.getString("PRE_SDESESTADO"));
							
							listaPrestaciones.add(prestacion);
						}
						log.info("Cantidad de Prestaciones: " + listaPrestaciones.size());
						moDataOut.put("listaPrestaciones", listaPrestaciones);
						moDataOut.put("totalRegistros", totalRegistros);
						rsResultado.close();
					}
				}
			}

		} catch (SQLException e) {
			log.error("PrestacionEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("PrestacionEJB -> " + sNombreSP + " Fin");

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
		terminaParametros("buscarPrestacion");
		return moDataOut;
	}
	
	public Map<String, Object> cargarPrestacion(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Prestacion");

		// Variables genericas para todo metodo
		iniciaParametros("cargarPrestacion");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		ResultSet rsResultado = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;
		String sEstado = "";

		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================

		Prestacion prestacion = null;
		long nIdPrestacion = (Long) moDataIn.get("nIdPrestacion");

		log.info("AplicacionEJB -> cargarPrestacion IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("AplicacionEJB -> conectado y listo para llamar en cargarPrestacion()");

			log.info("AplicacionEJB -> LOGUEO ");
			sNombreSP = "PRE$CARGA_PRESTACION.DETALLE_PRESTACION";
			sTipoSP = "TX";

			log.info("AplicacionEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, nIdPrestacion);

			log.info("parametros de entrada------------");
			log.info("ID Prestacion -> " + nIdPrestacion);
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

					error = new Error(0, 0, "", "");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRESTACION");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							prestacion = new Prestacion();
							prestacion.setsCodPrestacion(rsResultado.getString("PRE_SCODPRESTACION"));
							prestacion.setsNombre(rsResultado.getString("PRE_SNOMBRE"));
							prestacion.setsCodEstado(rsResultado.getString("PRE_SCODESTADO"));
							prestacion.setsDescripcion(rsResultado.getString("PRE_SDESCRIPCION"));
							prestacion.setsCodTipoPrestacion(rsResultado.getString("PRE_SCODTIPOPRESTACION"));
							prestacion.setsCodSubTipoPrestacion(rsResultado.getString("PRE_SCODSUBTIPOPRESTACION"));
							prestacion.setsCodUnidadMedida(rsResultado.getString("PRE_SCODUNIDADMEDIDA"));
							prestacion.setsIndCerrada(rsResultado.getString("PRE_SINDCERRADA"));
							prestacion.setsIndCompuesta(rsResultado.getString("PRE_SINDCOMPUESTA"));
							prestacion.setsIndVigencia(rsResultado.getString("PRE_SINDVIGENCIA"));
							prestacion.setnIdPrestacion(rsResultado.getLong("PRE_NIDPRESTACION"));
							prestacion.setsFecCreacion(rsResultado.getString("PRE_DFHOCREACION"));
							prestacion.setnIdCreador(rsResultado.getLong("PRE_NIDCREADOR"));
							prestacion.setFecActualizacion(rsResultado.getString("PRE_DFHOMODIFICACION"));
							prestacion.setnIdActualizador(rsResultado.getLong("PRE_NIDACTUALIZADOR"));
						}
						moDataOut.put("prestacion", prestacion);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("AplicacionEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("AplicacionEJB -> " + sNombreSP + " Fin");

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
		terminaParametros("cargarPrestacion");
		return moDataOut;
	}
	
	public Map<String, Object> buscarPrestacionesCompuestas(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Prestacion Compuesta");

		// Variables genericas para todo metodo
		iniciaParametros("buscarPrestacionesCompuestas");

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

		ArrayList<Prestacion> listaPrestaciones = null;
		Prestacion prestacion = null;
		long nIdPrestacion = (Long) moDataIn.get("nIdPrestacion");
		log.info("AplicacionEJB -> buscarPrestacionesCompuestas IN");
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("AplicacionEJB -> conectado y listo para llamar en buscarPrestacionesCompuestas()");

			log.info("AplicacionEJB -> LOGUEO ");
			sNombreSP = "PRE$BUSCA_COMPOSICION.LISTA_COMPOSICION";
			sTipoSP = "TX";

			log.info("AplicacionEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, nIdPrestacion);
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);

			log.info("parametros de entrada------------");
			log.info("ID Prestacion -> " + nIdPrestacion);
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

					error = new Error(0, 0, "", "");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRESTACION");
					
					if (rsResultado != null) {
						log.info("rsResultado != nulo");
						listaPrestaciones = new ArrayList<Prestacion>();

						while (rsResultado.next()) 
						{
							prestacion = new Prestacion();
							prestacion.setnIdPrestacion(rsResultado.getLong("PCO_NIDPRESTACIONHIJA"));
							prestacion.setnCantidad(rsResultado.getLong("PCO_NCANTIDAD"));
							prestacion.setsFecCreacion(rsResultado.getString("PCO_DFECCREACION"));
							prestacion.setnIdPrestPadre(rsResultado.getLong("PCO_NIDPRESTACION"));
							prestacion.setsCodPrestacion(rsResultado.getString("PRE_SCODPRESTACION"));
							prestacion.setsNombre(rsResultado.getString("PRE_SNOMBRE"));
							listaPrestaciones.add(prestacion);
						}
						log.info("Cantidad de Prestaciones Compuestas: " + listaPrestaciones.size());
						moDataOut.put("listaPrestaciones", listaPrestaciones);
						moDataOut.put("totalRegistros", totalRegistros);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("AplicacionEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("AplicacionEJB -> " + sNombreSP + " Fin");

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
		terminaParametros("buscarPrestacionesCompuestas");
		return moDataOut;
	}
	
	public Map<String, Object> buscarPrestacionesNomencladorNacional(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Prestacion Nacional");

		// Variables genericas para todo metodo
		iniciaParametros("buscarPrestacionesNomencladorNacional");

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

		ArrayList<Prestacion> listaPrestaciones = null;
		Prestacion prestacion = null;
		long nIdPrestacion = (Long) moDataIn.get("nIdPrestacion");
		log.info("PrestacionEJB -> buscarPrestacion Nacional IN");
		String ordenarPor = (String)moDataIn.get("sOrdenarPor");
        String orden = (String)moDataIn.get("sTipoOrden");
        int pagActual = (Integer)moDataIn.get("pagActual");
        int canRegxPag = (Integer)moDataIn.get("regPorPagina");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("PrestacionEJB -> conectado y listo para llamar en buscarPrestacionNacional()");

			log.info("PrestacionEJB -> LOGUEO ");
			sNombreSP = "PRE$BUSCA_NOMEN_NACYLE.LISTANOMEN_NACYLE";
			sTipoSP = "TX";

			log.info("PrestacionEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, nIdPrestacion);
            creaParametroIn(Types.VARCHAR, ordenarPor);
            creaParametroIn(Types.VARCHAR, orden);
            creaParametroIn(Types.INTEGER, pagActual);
            creaParametroIn(Types.INTEGER, canRegxPag);
			
			log.info("parametros de entrada------------");
			log.info("ID Prestacion -> " + nIdPrestacion);
			log.info("Ordenar Por: "+ordenarPor);
            log.info("Tipo Orden: "+orden);
            log.info("Pag Actual: "+pagActual);         
            log.info("Cant reg x pag: "+canRegxPag);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("TotalRegistros", OracleTypes.INTEGER);
			creaParametroOut("CUR_BUSCA_NOMEN_NACYLE", OracleTypes.CURSOR);
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

					error = new Error(0, 0, "", "");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_BUSCA_NOMEN_NACYLE");
					
					if (rsResultado != null) {
						log.info("rsResultado != nulo");
						listaPrestaciones = new ArrayList<Prestacion>();

						while (rsResultado.next()) 
						{
							prestacion = new Prestacion();
							prestacion.setnIdPrestacion(rsResultado.getLong("PNN_SSECUENCIANACIONAL"));
							prestacion.setnCantidad(rsResultado.getLong("PNN_NCANTIDAD"));
							prestacion.setsCodPrestacion(rsResultado.getString("NOM_SCODIGO"));
							prestacion.setsDescripcion(rsResultado.getString("NOM_SGLOSA"));
							prestacion.setsDesTipoPrestacion(rsResultado.getString("PNN_SINDPRINCIPAL"));
							prestacion.setsFecModificacion(rsResultado.getString("NOM_FECHAMODIF"));
							prestacion.setsIndicador(rsResultado.getString("PNN_SINDPRINCIPAL"));
							listaPrestaciones.add(prestacion);
						}
						log.info("Cantidad de Prestaciones Nacionales: " + listaPrestaciones.size());
						moDataOut.put("listaPrestaciones", listaPrestaciones);
						moDataOut.put("totalRegistros", totalRegistros);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("PrestacionEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("PrestacionEJB -> " + sNombreSP + " Fin");

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
		terminaParametros("buscarPrestacionesNomencladorNacional");
		return moDataOut;
	}
	
	
	
	public Map<String, Object> eliminarPrestacion(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarPrestacion");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
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
		
		long nIdAPrestacion = (Long) moDataIn.get("nIdPrestacion");
		String codPrestacion = (String) moDataIn.get("codPrestacion");
		
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");
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

			log.info("PrestacionEJB -> eliminarPrestacion IN");

			try {
				dbConeccion = solemDS.getConnection();
				
				log.info("PrestacionEJB -> conectado y listo para llamar en eliminarPrestacion()");

				log.info("PrestacionEJB -> LOGUEO ");
				sNombreSP = "PRE$ELIMINA_PRESTACION";
				sTipoSP = "TX";

				log.info("PrestacionEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				log.info("codigo Prestacion a eliminar: " + codPrestacion);
				log.info("Id de la Prestacion a eliminar: " + nIdAPrestacion);

				creaParametroIn(Types.VARCHAR, codPrestacion);
				creaParametroIn(Types.NUMERIC, nIdAPrestacion);
				creaParametroIn(Types.NUMERIC, Long.parseLong(sesion.getSesIdUsuario()));

				// =============================================================
				// Parametros de Salida
				// =============================================================
				
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("PrestacionEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("PrestacionEJB -> " + sNombreSP + " cStmt Fin");

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
				log.error("AplicacionEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally PrestacionEJB -> " + sNombreSP + " Fin");
					if (numError == 0) {
						log.info("Commit en " + sNombreSP);
						
					} else {
						log.info("Rollback en " + sNombreSP);
						
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

		terminaParametros("eliminarPrestacion");

		return moDataOut;
	}
	public Map<String, Object> eliminarPrestacionCompuesta(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarPrestacionCompuesta");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
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
		
		long nIdAPrestacion = (Long) moDataIn.get("nIdPrestacion");
		String listaPrestaciones = (String)moDataIn.get("lista");
		
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");
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

			log.info("PrestacionEJB -> eliminarPrestacionCompuesta IN");

			try {
				dbConeccion = solemDS.getConnection();
				
				log.info("PrestacionEJB -> conectado y listo para llamar en eliminarPrestacionCompuesta()");

				log.info("PrestacionEJB -> LOGUEO ");
				
				String listaId[] = listaPrestaciones.split(",");
				log.info("Tamaño de la lista: " + listaId.length);
				for (int i= 0;i<listaId.length;i++ ){
					log.info("Id Elemento a eliminar: "+listaId[i]);
				}
								
				for (int i= 0;i<listaId.length;i++){	
					
					long prestacionComposicion = Long.parseLong(listaId[i].trim());
					log.info("prestacionComposicion: " + prestacionComposicion);
					
					sNombreSP = "PRE$ELIMINA_COMPOSICION";
					sTipoSP = "TX";
	
					log.info("PrestacionEJB -> " + sNombreSP + " Inicio");
	
					// =============================================================
					// Parametros de entrada
					// =============================================================
	
					log.info("id Prestacion Hija a eliminar: " + prestacionComposicion);
					log.info("Id de la Prestacion padre: " + nIdAPrestacion);
	
					creaParametroIn(Types.NUMERIC, prestacionComposicion);
					creaParametroIn(Types.NUMERIC, nIdAPrestacion);
					creaParametroIn(Types.NUMERIC, Long.parseLong(sesion.getSesIdUsuario()));
	
					// =============================================================
					// Parametros de Salida
					// =============================================================
					
					creaParametroOut("NumError", Types.INTEGER);
					creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("PrestacionEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("PrestacionEJB -> " + sNombreSP + " cStmt Fin");

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
				terminaParametros("eliminarPrestacionCompuesta");
			}

			} catch (SQLException e) {
				log.error("AplicacionEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally PrestacionEJB -> " + sNombreSP + " Fin");
					if (numError == 0) {
						log.info("Commit en " + sNombreSP);
						
					} else {
						log.info("Rollback en " + sNombreSP);
						
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
		return moDataOut;
	}
	
	public Map<String, Object> guardarPrestacion(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarPrestacion");
		log.info("Inicia parametros en: guardarPrestacion()");

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

		Prestacion prestacion = (Prestacion) moDataIn.get("prestacion");
		log.info("PrestacionEJB -> guardarPrestacion IN");
		long idPrestacion=0; 

		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");

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

			log.info("PrestacionJB -> guardarPrestacion IN");

			// =============================================================
			// CUERPO DE EJB
			// =============================================================

			try {
				dbConeccion = solemDS.getConnection();
				
				log.info("PrestacionEJB -> conectado y listo para llamar en guardarPrestacion()");
				log.info("PrestacionoEJB -> guardarPrestacion ");

				sNombreSP = "PRE$AGREGA_PRESTACION";
				sTipoSP = "TX";

				log.info("EJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				creaParametroIn(Types.VARCHAR, prestacion.getsCodPrestacion());
				creaParametroIn(Types.VARCHAR, prestacion.getsNombre());
				creaParametroIn(Types.VARCHAR, prestacion.getsCodEstado());
				creaParametroIn(Types.VARCHAR, prestacion.getsCodTipoPrestacion());
				creaParametroIn(Types.VARCHAR, prestacion.getsCodSubTipoPrestacion());
				creaParametroIn(Types.VARCHAR, prestacion.getsCodUnidadMedida());
				creaParametroIn(Types.NUMERIC, prestacion.getnIdResponsable());
				creaParametroIn(Types.VARCHAR, prestacion.getsIndVigencia());
				creaParametroIn(Types.VARCHAR, prestacion.getsIndCerrada());
				creaParametroIn(Types.VARCHAR, prestacion.getsIndCompuesta());
				creaParametroIn(Types.VARCHAR, prestacion.getsDescripcion());
				creaParametroIn(Types.NUMERIC, Long.parseLong(sesion.getSesIdUsuario()));

				log.info("parametros de entrada guardarPrestacion------------");
				log.info("Codigo Prestacion -> " + prestacion.getsCodPrestacion());
				log.info("Nombre Prestacion-> " + prestacion.getsNombre());
				log.info("Estado Prestacion -> " + prestacion.getsCodEstado());
				log.info("Tipo de Prestacion -> " + prestacion.getsCodTipoPrestacion());
				log.info("SubTipo de Prestacion -> " + prestacion.getsCodSubTipoPrestacion());
				log.info("Unidad de Medida -> " + prestacion.getsCodUnidadMedida());
				log.info("Id Responsable -> " + prestacion.getnIdResponsable());
				log.info("Indicador de Vigencia -> " + prestacion.getsIndVigencia());
				log.info("Indicador de Cerrada -> " + prestacion.getsIndCerrada());
				log.info("Indicador de Compuesta -> " + prestacion.getsIndCompuesta());
				log.info("Descripcion -> " + prestacion.getsDescripcion());
				log.info("Id usuario -> " + Long.parseLong(sesion.getSesIdUsuario()));
				log.info("--------------------------------");
				 

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("idPrestacion", Types.INTEGER);
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {
					log.info("PrestacionEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("PrestacionEJB -> " + sNombreSP + " cStmt Fin");

				} catch (Exception e) {
					log.error(e);
					error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				}

				if (cStmt != null) {
					log.info("Cursor != nulo");

					cStmt.execute();

					// Recuperacion parametros de salida
					idPrestacion = (Integer) getParametro(cStmt, "idPrestacion");
					log.info("idPrestacion: " + idPrestacion);
					
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
				log.error("PrestacionEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			
			} finally {

				log.info("PrestacionEJB -> " + sNombreSP + " Fin");

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

		moDataOut.put("idPrestacion", idPrestacion);
		moDataOut.put("error", error);

		terminaParametros("guardarPrestacion");
		return moDataOut;
	}
	public Map<String, Object> actualizarPrestacion(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizarPrestacion");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
		String sNombreSP = null;
		String sTipoSP = null;
		int numError = 0;
		String msjError = null;

		// Variables para verificacion de sesion y perfiles
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = null;
		
		Sesion sesion = null;
		
		
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");
		moBeansIn.clear();
		moBeansIn.put("sesion", sesion);
		moBeansOut = ejbSesion.buscaPerfiles(moBeansIn);

		error = (Error) moBeansOut.get("error");

		if (!error.getNumError().equalsIgnoreCase("0")) {
			throw new ErrorSPException(error.getNumError(), error.getNumError(), error.getMsjError());
		}
					
		// =============================================================
		// VARIABLES ESPECIFICAS DEL METODO
		// =============================================================
		Prestacion prestacion = (Prestacion) moDataIn.get("prestacion");

		log.info("PrestacionEJB -> actualizarPrestacion IN");

		try {
			dbConeccion = solemDS.getConnection();

			log.info("PrestacionEJB -> conectado y listo para llamar en actualizarPrestacion()");

			log.info("PrestacionEJB -> LOGUEO ");
			sNombreSP = "PRE$ACTUALIZA_PRESTACION";
			sTipoSP = "TX";

			log.info("PrestacionEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, prestacion.getnIdPrestacion());
			creaParametroIn(Types.VARCHAR, prestacion.getsCodPrestacion());
			creaParametroIn(Types.VARCHAR, prestacion.getsNombre());
			creaParametroIn(Types.VARCHAR, prestacion.getsDescripcion());
			creaParametroIn(Types.VARCHAR, prestacion.getsCodEstado());
			creaParametroIn(Types.VARCHAR, prestacion.getsCodTipoPrestacion());
			creaParametroIn(Types.VARCHAR, prestacion.getsCodSubTipoPrestacion());
			creaParametroIn(Types.VARCHAR, prestacion.getsCodUnidadMedida());
			creaParametroIn(Types.VARCHAR, prestacion.getsIndCerrada());
			creaParametroIn(Types.VARCHAR, prestacion.getsIndCompuesta());
			creaParametroIn(Types.NUMERIC, Long.parseLong(sesion.getSesIdUsuario()));
			
			log.info("parametros de entrada Prestacion------------");
			log.info("Id Prestacion -> " + prestacion.getnIdPrestacion());
			log.info("Codigo Prestacion -> " + prestacion.getsCodPrestacion());
			log.info("Nombre Prestacion -> " + prestacion.getsNombre());
			log.info("Nombre Prestacion -> " + prestacion.getsDescripcion());
			log.info("Estado -> " + prestacion.getsCodEstado());
			log.info("Tipo Prestacion -> " + prestacion.getsCodTipoPrestacion());
			log.info("SubTipo Prestacion -> " + prestacion.getsCodSubTipoPrestacion());
			log.info("Unidad de Medida -> " + prestacion.getsCodUnidadMedida());
			log.info("Cerrada -> " + prestacion.getsIndCerrada());
			log.info("Compuesta -> " + prestacion.getsIndCompuesta());
			log.info("Id usuario -> " + Long.parseLong(sesion.getSesIdUsuario()));
			log.info("--------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("PrestacionEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("PrestacionEJB -> " + sNombreSP + " cStmt Fin");

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
			log.error("PrestacionEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			
		} finally {

			log.info("PrestacionEJB -> " + sNombreSP + " Fin");

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
	terminaParametros("actualizarPrestacion");
	return moDataOut;
	}
	
	public Map<String, Object> buscarCompuestas(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		iniciaParametros("buscarCompuestas");

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

		long nIdAPrestacion = (Long) moDataIn.get("nIdPrestacion");
		HashMap<String, Object> mapaSalida = null;
		mapaSalida = new HashMap<String, Object>();
		ArrayList<Prestacion> listaPrestaciones = null;
		Prestacion m_prestacion = (Prestacion)moDataIn.get("prestacion");
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");
		
		
		try {
			dbConeccion = solemDS.getConnection();

			log.info("PrestacionEJB -> conectado y listo para llamar en buscarCompuestas()");

			log.info("PrestacionEJB -> buscarCompuestas");

			sNombreSP = "PRE$BUSCA_COMPOPRESTDISPON.LISTA_COMPOSICION";
			sTipoSP = "TX";

			log.info("PrestacionEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			log.info("parametros de entrada------------");
			log.info("ID Prestacion -> " + nIdAPrestacion);
			log.info("Codigo Prestacion: " + m_prestacion.getsCodPrestacion());
			log.info("Nombre Prestacion: " + m_prestacion.getsNombre());
			log.info("Ordenar Por: "+sOrdenarPor);
			log.info("Tipo Orden: "+sTipoOrden);
			log.info("Pag Actual: "+pagActual);			
			log.info("Cant reg x pag: "+regPorPagina);
			
			creaParametroIn(Types.NUMERIC, nIdAPrestacion);
			creaParametroIn(Types.VARCHAR, m_prestacion.getsCodPrestacion());
			creaParametroIn(Types.VARCHAR, m_prestacion.getsNombre());
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);
			
			
			// =============================================================
			// Parametros de Salida
			// =============================================================

			creaParametroOut("TotalRegistros", OracleTypes.INTEGER);
			creaParametroOut("CUR_PRESTACIONES", OracleTypes.CURSOR);
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

					error = new Error(0, 0, "", "");

					log.info("PrestacionEJB -> " + sNombreSP + " rsResultado Inicio");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRESTACIONES");
					log.info("PrestacionEJB -> " + sNombreSP + " rsResultado Fin");

					if (rsResultado != null) {
						log.info("rsResultado != nulo");

						listaPrestaciones = new ArrayList<Prestacion>();

						while (rsResultado.next()) {
							Prestacion prestacion = new Prestacion();
							prestacion.setnIdPrestacion(rsResultado.getLong("PRE_NIDPRESTACION"));
							prestacion.setsCodPrestacion(rsResultado.getString("PRE_SCODPRESTACION"));
							prestacion.setsNombre(rsResultado.getString("PRE_SNOMBRE"));
							prestacion.setsDescripcion(rsResultado.getString("PRE_SDESCRIPCION"));
							prestacion.setsIndVigencia(rsResultado.getString("PRE_SINDVIGENCIA").equals("S")?"SI":"NO");
							prestacion.setsCodTipoPrestacion(rsResultado.getString("PRE_SCODTIPOPRESTACION"));
							prestacion.setsDesTipoPrestacion(rsResultado.getString("PRE_SDESTIPOPRESTACION"));
							prestacion.setsDesEstado(rsResultado.getString("PRE_SDESESTADO"));
							listaPrestaciones.add(prestacion);
						}
						log.info("Cantidad de Prestaciones: " + listaPrestaciones.size());
						moDataOut.put("listaPrestaciones", listaPrestaciones);
						moDataOut.put("totalRegistros", totalRegistros);
						rsResultado.close();
					}
				}
			}

		} catch (SQLException e) {
			log.error("PrestacionEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("PrestacionEJB -> " + sNombreSP + " Fin");

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
		terminaParametros("buscarCompuestas");
		return moDataOut;
	}
	public Map<String, Object> agregarCompuesta(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("agregarCompuesta");
		log.info("Inicia parametros en: agregarCompuesta()");

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

		long nIdAPrestacion = (Long) moDataIn.get("idPrestacion");
		String listaPrestaciones = (String)moDataIn.get("lista");
		String cantidad = (String) moDataIn.get("cantidad");
				
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");

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

			log.info("PrestacionJB -> agregarCompuesta IN");

			// =============================================================
			// CUERPO DE EJB
			// =============================================================

			try {
				dbConeccion = solemDS.getConnection();
				
				log.info("PrestacionEJB -> conectado y listo para llamar en agregarCompuesta()");
				log.info("PrestacionEJB -> agregarCompuesta");

				sNombreSP = "PRE$AGREGA_COMPOSICION";
				sTipoSP = "TX";
				
				String listaId[] = listaPrestaciones.split(",");
				log.info("Tamaño de la lista: " + listaId.length);
				String cantidades[] = cantidad.split(",");
				log.info("Tamaño de la lista Cantidades: " + listaId.length);
				
				for (int i= 0;i<listaId.length;i++ ){
					log.info("Id Elementor: "+listaId[i]);
				}
								
				for (int i= 0;i<listaId.length;i++){	
					
					long prestacionComposicion = Long.parseLong(listaId[i].trim());
					log.info("prestacionComposicion: " + prestacionComposicion);
					long cant = Long.parseLong(cantidades[i].trim());
					log.info("cantidad: " + cant);
					log.info("EJB -> " + sNombreSP + " Inicio");
	
					// =============================================================
					// Parametros de entrada
					// =============================================================
	
					log.info("id Prestacion Hija: " + prestacionComposicion);
					log.info("Id de la Prestacion padre: " + nIdAPrestacion);
	
					creaParametroIn(Types.NUMERIC, prestacionComposicion);
					creaParametroIn(Types.NUMERIC, nIdAPrestacion);
					creaParametroIn(Types.NUMERIC, cant);
					creaParametroIn(Types.NUMERIC, Long.parseLong(sesion.getSesIdUsuario()));
	
					// =============================================================
					// Parametros de Salida
					// =============================================================
					
					creaParametroOut("NumError", Types.INTEGER);
					creaParametroOut("MsjError", Types.VARCHAR);
	
					try {
						log.info("PrestacionEJB -> " + sNombreSP + " cStmt Inicio");
						cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
						log.info("PrestacionEJB -> " + sNombreSP + " cStmt Fin");
	
					} catch (Exception e) {
						log.error(e);
						error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
						throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
					}
	
					if (cStmt != null) {
						log.info("Cursor != nulo");
	
						cStmt.execute();
						
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
					terminaParametros("agregarCompuesta");
				}	
			} catch (SQLException e) {
				log.error("PrestacionEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			
			} finally {

				log.info("PrestacionEJB -> " + sNombreSP + " Fin");

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
		return moDataOut;
	}
	
	public Map<String, Object> buscarNomencladores(Map<String, Object> moDataIn) throws ErrorSPException 
    {
		iniciaParametros("buscarNomencladores");

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

		long nIdAPrestacion = (Long) moDataIn.get("nIdPrestacion");
		HashMap<String, Object> mapaSalida = null;
		mapaSalida = new HashMap<String, Object>();
		ArrayList<Prestacion> listaNomencladores = null;
		Prestacion m_prestacion = (Prestacion)moDataIn.get("prestacion");
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");
		String tabla = m_prestacion.getsIndicador();
				
		try {
			dbConeccion = solemDS.getConnection();

			log.info("PrestacionEJB -> conectado y listo para llamar en buscarNomencladores()");

			log.info("PrestacionEJB -> buscarNomencladores");

			sNombreSP = "PRE$BUSCA_NACIO_LIBREL_AGR.BUSCA_NACIO_LIBREL_AGR";
			sTipoSP = "TX";

			log.info("PrestacionEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			log.info("parametros de entrada------------");
			log.info("ID Prestacion -> " + nIdAPrestacion);
			log.info("Indicador de Tabla a Utilizar: " + m_prestacion.getsIndicador());
			log.info("Nombre Prestacion: " + m_prestacion.getsNombre());
			log.info("Codigo Prestacion: " + m_prestacion.getsCodPrestacion());
			
			
			log.info("Ordenar Por: "+sOrdenarPor);
			log.info("Tipo Orden: "+sTipoOrden);
			log.info("Pag Actual: "+pagActual);			
			log.info("Cant reg x pag: "+regPorPagina);
			
			creaParametroIn(Types.NUMERIC, nIdAPrestacion);
			creaParametroIn(Types.VARCHAR, m_prestacion.getsIndicador());
			creaParametroIn(Types.VARCHAR, m_prestacion.getsNombre());
			creaParametroIn(Types.VARCHAR, m_prestacion.getsCodPrestacion());
			
			
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);
			
			
			// =============================================================
			// Parametros de Salida
			// =============================================================

			creaParametroOut("TotalRegistros", OracleTypes.INTEGER);
			creaParametroOut("CUR_NOMENCLADORES", OracleTypes.CURSOR);
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

					error = new Error(0, 0, "", "");

					log.info("PrestacionEJB -> " + sNombreSP + " rsResultado Inicio");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_NOMENCLADORES");
					log.info("PrestacionEJB -> " + sNombreSP + " rsResultado Fin");

					if (rsResultado != null) {
						log.info("rsResultado != nulo");
						listaNomencladores = new ArrayList<Prestacion>();
							
						if(tabla.equals("S")){
						
							while (rsResultado.next()) {
								Prestacion nomenclador = new Prestacion();
								nomenclador.setnIdPrestacion(rsResultado.getLong("NOM_SSECUENCIA"));
								nomenclador.setsCodPrestacion(rsResultado.getString("NOM_SCODIGO"));
								nomenclador.setsNombre(rsResultado.getString("NOM_SGLOSA"));
								nomenclador.setsIndicador("S");
								listaNomencladores.add(nomenclador);
							}
						}
						else
						{
							while (rsResultado.next()) {
								Prestacion nomenclador = new Prestacion();
								nomenclador.setnIdPrestacion(rsResultado.getLong("NLE_SSECUENCIA"));
								nomenclador.setsCodPrestacion(rsResultado.getString("NLE_SCODIGO"));
								nomenclador.setsNombre(rsResultado.getString("NLE_SGLOSA"));
								nomenclador.setsIndicador("N");
								listaNomencladores.add(nomenclador);
							}
						}
						log.info("Cantidad de Nomencladores: " + listaNomencladores.size());
						moDataOut.put("listaNomencladores", listaNomencladores);
						moDataOut.put("totalRegistros", totalRegistros);
						rsResultado.close();
					}
				}
			}
		} catch (SQLException e) {
			log.error("PrestacionEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
		} finally {

			log.info("PrestacionEJB -> " + sNombreSP + " Fin");

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
		terminaParametros("buscarNomencladores");
		return moDataOut;
    }
	
	public Map<String, Object> eliminarPrestacionNacional(Map<String, Object> moDataIn) throws ErrorSPException 
    {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarPrestacionNacional");

		// Variables para manejo de llamadas a Base de Datos
		CallableStatement cStmt = null;
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
		
		long nIdAPrestacion = (Long)moDataIn.get("nIdPrestacion");
		String listaPrestaciones = (String)moDataIn.get("lista");
		String listaTablasNomec = (String)moDataIn.get("tablas");
		
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");
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

			log.info("PrestacionEJB -> eliminarPrestacionNacional IN");

			try {
				dbConeccion = solemDS.getConnection();
				
				log.info("PrestacionEJB -> conectado y listo para llamar en eliminarPrestacionNacional()");

				log.info("PrestacionEJB -> LOGUEO ");
				
				String listaId[] = listaPrestaciones.split(",");
				String tablaId[] = listaTablasNomec.split(",");
				log.info("Tamaño de la lista: " + listaId.length);
				for (int i= 0;i<listaId.length;i++ ){
					log.info("Id Elemento a eliminar: "+listaId[i]);
					log.info("Tabla Elemento a eliminar: "+tablaId[i]);
				}
								
				for (int i= 0;i<listaId.length;i++){	
					
					String prestacionComposicion = listaId[i].trim();
					String tablaNomeclador = tablaId[i].trim();
					String Indicador ="";
					
					if(tablaNomeclador.equals("INSTITUCIONAL")) 
						Indicador = "S";
					else 
						Indicador = "N";
					
					log.info("PrestacionNacional: " + prestacionComposicion);
					log.info("tablaNomeclador: " + Indicador);
					sNombreSP = "PRE$ELIMINA_NNACIONAL";
					sTipoSP = "TX";
	
					log.info("PrestacionEJB -> " + sNombreSP + " Inicio");
	
					// =============================================================
					// Parametros de entrada
					// =============================================================
	
					log.info("Id de la Prestacion padre: " + nIdAPrestacion);
					log.info("id Prestacion Hija a eliminar: " + prestacionComposicion);
					log.info("Tabla de la que se eliminara: " + Indicador);
	
					creaParametroIn(Types.NUMERIC, nIdAPrestacion);
					creaParametroIn(Types.VARCHAR, prestacionComposicion);
					creaParametroIn(Types.VARCHAR, Indicador);
					creaParametroIn(Types.NUMERIC, Long.parseLong(sesion.getSesIdUsuario()));
	
					// =============================================================
					// Parametros de Salida
					// =============================================================
					
					creaParametroOut("NumError", Types.INTEGER);
					creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("PrestacionEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("PrestacionEJB -> " + sNombreSP + " cStmt Fin");

				} catch (Exception e) {
					e.printStackTrace();
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
				terminaParametros("eliminarPrestacionNacional");
			}

			} catch (SQLException e) {
				log.error("AplicacionEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally PrestacionEJB -> " + sNombreSP + " Fin");
					if (numError == 0) {
						log.info("Commit en " + sNombreSP);
						
					} else {
						log.info("Rollback en " + sNombreSP);
						
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
		return moDataOut;
	} 
	public Map<String, Object> agregarPrestNomec(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("agregarPrestNomec");
		log.info("Inicia parametros en: agregarPrestNomec()");

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

		long nIdAPrestacion = (Long) moDataIn.get("idPrestacion");
		String listaPrestaciones = (String)moDataIn.get("lista");
		String TablaNomec = (String)moDataIn.get("tabla");
		String cantidad = (String) moDataIn.get("cantidad");
				
		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		sesion = (Sesion) moDataIn.get("sesion");

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

			log.info("PrestacionJB -> agregarPrestNomec IN");

			// =============================================================
			// CUERPO DE EJB
			// =============================================================

			try {
				dbConeccion = solemDS.getConnection();
				
				log.info("PrestacionEJB -> conectado y listo para llamar en agregarPrestNomec()");
				log.info("PrestacionEJB -> agregarPrestNomec");

				sNombreSP = "PRE$AGREGA_NNACIONAL";
				sTipoSP = "TX";
				
				String listaId[] = listaPrestaciones.split(",");
				log.info("Tamaño de la lista: " + listaId.length);
				String cantidades[] = cantidad.split(",");
				log.info("Tamaño de la lista Cantidades: " + listaId.length);
				
				for (int i= 0;i<listaId.length;i++ ){
					log.info("Id Elemento: "+listaId[i]);
					log.info("cantidad: "+cantidades[i]);
				}
								
				for (int i= 0;i<listaId.length;i++){	
					
					String prestacionComposicion = (String)(listaId[i].trim());
					log.info("prestacionNomenclador: " + prestacionComposicion);
				
					long cant = Long.parseLong(cantidades[i].trim());
					log.info("cantidad: " + cant);

					log.info("EJB -> " + sNombreSP + " Inicio");
	
					// =============================================================
					// Parametros de entrada
					// =============================================================
	
					log.info("id Prestacion Secuencia: " + prestacionComposicion);
					log.info("Id de la Prestacion padre: " + nIdAPrestacion);
	
					creaParametroIn(Types.NUMERIC, nIdAPrestacion);
					creaParametroIn(Types.VARCHAR, prestacionComposicion);
					creaParametroIn(Types.VARCHAR, TablaNomec);
					creaParametroIn(Types.NUMERIC, cant);
					creaParametroIn(Types.NUMERIC, Long.parseLong(sesion.getSesIdUsuario()));
	
					// =============================================================
					// Parametros de Salida
					// =============================================================
					
					creaParametroOut("NumError", Types.INTEGER);
					creaParametroOut("MsjError", Types.VARCHAR);
	
					try {
						log.info("PrestacionEJB -> " + sNombreSP + " cStmt Inicio");
						cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
						log.info("PrestacionEJB -> " + sNombreSP + " cStmt Fin");
	
					} catch (Exception e) {
						log.error(e);
						error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
						throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
					}
	
					if (cStmt != null) {
						log.info("Cursor != nulo");
	
						cStmt.execute();
						
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
					terminaParametros("agregarPrestNomec");
				}	
			} catch (SQLException e) {
				log.error("PrestacionEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			
			} finally {

				log.info("PrestacionEJB -> " + sNombreSP + " Fin");

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
		return moDataOut;
	}
}