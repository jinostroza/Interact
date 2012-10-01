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
import cl.solem.tyc.beans.Anexo;
import cl.solem.tyc.beans.Beneficio;
import cl.solem.tyc.beans.CatalogoPrest;
import cl.solem.tyc.beans.Convenio;
import cl.solem.tyc.beans.ConvenioAnexoDetalle;
import cl.solem.tyc.beans.Empresa;
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.Modificador;
import cl.solem.tyc.beans.PlanBeneficio;
import cl.solem.tyc.beans.Precio;
import cl.solem.tyc.beans.Restriccion;
import cl.solem.tyc.beans.Sesion;
import cl.solem.tyc.beans.UniOrg;
import cl.solem.tyc.beans.Usuario;
import cl.solem.tyc.ejb.interfaces.ConvenioEJBRemote;
import cl.solem.tyc.ejb.interfaces.SesionEJBRemote;

@Stateless(mappedName = "tyc/ConvenioEJB")
public class ConvenioEJB extends EjbBase implements ConvenioEJBRemote{
	
	private Logger log = Logger.getLogger(ConvenioEJB.class);
	@Resource(mappedName = "java:/SolemBilling")
	protected DataSource solemDS;
	
	private Error error = null;
	private ErrorSPException errorSP = null;
	private Seguridad seg = null;
	
	@EJB(mappedName = "tyc/SesionEJB")
	private SesionEJBRemote ejbSesion;

	public ConvenioEJB() {
		super();
		setSolemDataSource(solemDS);
	}

	public Map<String, Object> buscarConvenios(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		log.info("Busca Convenios en  ConveniosEJB");
		iniciaParametros("buscarConvenios");

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

		ArrayList<Convenio> listaConvenios = null;
		Convenio m_convenio = (Convenio) moDataIn.get("convenio");
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");
		

		log.info("ConvenioEJB -> buscarConvenios IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarConvenios()");

			log.info("ConvenioEJB -> buscarConvenios");

			sNombreSP = "CON$BUSCA_CONVENIOS.LISTA_CONVENIOS";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			//creaParametroIn(Types.NUMERIC, m_convenio.getnIdPrestador());
			creaParametroIn(Types.VARCHAR, m_convenio.getsNomPrestador());
			creaParametroIn(Types.VARCHAR, m_convenio.getsCodConvenio());
			creaParametroIn(Types.VARCHAR, m_convenio.getsNombre());
			creaParametroIn(Types.VARCHAR, m_convenio.getsCodClase());
			//creaParametroIn(Types.VARCHAR, convenio.getsCodSubClase());
			creaParametroIn(Types.VARCHAR, m_convenio.getsCodEstado());
			creaParametroIn(Types.VARCHAR, m_convenio.getsCodTipo());
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);

			log.info("Id prestador: " + m_convenio.getnIdPrestador());
			log.info("Nombre prestador: " + m_convenio.getsNomPrestador());
			log.info("Codigo: " + m_convenio.getsCodConvenio());
			log.info("Nombre: " + m_convenio.getsNombre());
			log.info("Clase: " + m_convenio.getsCodClase());
			//log.info("SubClase: " + convenio.getsCodSubClase());
			log.info("Estado: " + m_convenio.getsCodEstado());
			log.info("Tipo: " + m_convenio.getsCodTipo());
			log.info("Ordenar Por: "+sOrdenarPor);
			log.info("Tipo Orden: "+sTipoOrden);
			log.info("Pag Actual: "+pagActual);			
			log.info("Cant reg x pag: "+regPorPagina);

			log.info("--------------------------------");
			
			// =============================================================
			// Parametros de Salida
			// =============================================================

			creaParametroOut("TotalRegistros", OracleTypes.INTEGER);
			creaParametroOut("CUR_CONVENIOS", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
				}else{
					try{
						//error = new Error(0, 0, "", "");
						error = new Error("0","");
	
						log.info("ConvenioEJB -> " + sNombreSP + " rsResultado Inicio");
						rsResultado = (ResultSet) getParametro(cStmt, "CUR_CONVENIOS");
						log.info("ConvenioEJB -> " + sNombreSP + " rsResultado Fin");
	
						if (rsResultado != null) {
							log.info("rsResultado != nulo");
	
							listaConvenios = new ArrayList<Convenio>();
	
							while (rsResultado.next()) {
								Convenio convenio = new Convenio();
								convenio.setnIdConvenio(rsResultado.getLong("CON_NIDCONVENIO"));
								convenio.setnIdPrestador(rsResultado.getLong("CON_NIDPRESTADOR"));
								convenio.setsNomPrestador(rsResultado.getString("EMP_SNOMBREFANTASIA"));
								convenio.setsCodTipo(rsResultado.getString("CON_SCODTIPO"));
								convenio.setsDesTipo(rsResultado.getString("CON_SDESTIPO"));
								convenio.setsCodConvenio(rsResultado.getString("CON_SCODCONVENIO"));
								convenio.setsNombre(rsResultado.getString("CON_SNOMBRE"));
								convenio.setsDesClase(rsResultado.getString("CONSDESCLASE"));
								convenio.setsDesSubClase(rsResultado.getString("CONSDESSUBCLASE"));
								convenio.setsDesEstado(rsResultado.getString("ESTADO"));
								
								listaConvenios.add(convenio);
							}
							log.info("cantidad: " + listaConvenios.size());
							moDataOut.put("listaConvenios", listaConvenios);
							moDataOut.put("totalRegistros", totalRegistros);
							rsResultado.close();
						}
					
					}catch(Exception e)
					{
						e.printStackTrace();
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

		terminaParametros("buscarConvenios");

		return moDataOut;
	}

	public Map<String, Object> cargarConvenio(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Convenio");

		// Variables genericas para todo metodo
		iniciaParametros("cargaConvenio");

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

		Convenio convenio = null;

		long idConvenio = 0;

		idConvenio = (Long) moDataIn.get("idConvenio");

		log.info("ConvenioEJB -> cargaConvenio IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en cargaConvenio()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$CARGA_CONVENIO.DETALLE_CONVENIO";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idConvenio);

			log.info("parametros de entrada------------");
			log.info("IdConvenio -> " + idConvenio);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_CONVENIO", OracleTypes.CURSOR);
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
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_CONVENIO");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							convenio = new Convenio();
							convenio.setnIdConvenio(rsResultado.getLong("CON_NIDCONVENIO"));
							convenio.setnIdPrestador(rsResultado.getLong("CON_NIDPRESTADOR"));
							convenio.setsNomPrestador(rsResultado.getString("NOMBRE_PREST"));
							convenio.setnIdCliente(rsResultado.getLong("CON_NIDCLIENTE"));
							convenio.setsNomCliente(rsResultado.getString("NOMBRE_CLIENTE"));
							convenio.setsCodConvenio(rsResultado.getString("CON_SCODCONVENIO"));
							convenio.setsNombre(rsResultado.getString("CON_SNOMBRE"));
							convenio.setsNomPoliza(rsResultado.getString("CON_SNOMPOLIZA"));
							convenio.setsCodClase(rsResultado.getString("CON_SCODCLASE"));
							convenio.setsDesClase(rsResultado.getString("CONSDESCLASE"));
							convenio.setsCodSubClase(rsResultado.getString("CON_SCODSUBCLASE"));
							convenio.setsDesSubClase(rsResultado.getString("CON_SDESCLASE"));
							convenio.setsCodTipo(rsResultado.getString("CON_SCODTIPO"));
							convenio.setsDesTipo(rsResultado.getString("CON_SDESTIPO"));
							convenio.setsDescripcion(rsResultado.getString("CON_SDESCRIPCION"));
							convenio.setsIndPlantilla(rsResultado.getString("CON_SINDPLANTILLA"));
							//convenio.setsIndUsoBecados(rsResultado.getString("CON_SINDUSOBECADOS"));
							convenio.setsIndRecaudacion(rsResultado.getString("CON_SINDRECAUDACION"));
							convenio.setsIndConvenioAbierto(rsResultado.getString("CON_SINDCONVENIOABIERTO"));
							convenio.setsIndFacturaPrestador(rsResultado.getString("CON_SINDFACTURAPRESTADOR"));
							convenio.setsFhoInicio(rsResultado.getString("CON_DFHOINICIO"));
							convenio.setsFhoTermino(rsResultado.getString("CON_DFHOTERMINO"));
							convenio.setnIdResponsable(rsResultado.getLong("CON_NIDRESPONSABLE"));
							convenio.setsNomResponsable(rsResultado.getString("CON_SNOMRESPONSABLE"));
							convenio.setnPorRecaudacion(rsResultado.getLong("CON_NPORRECAUDACION"));
							convenio.setnCupoConvenio(rsResultado.getLong("CON_NCUPOCONVENIO"));
							convenio.setsCodEstado(rsResultado.getString("CON_SCODESTADO"));
							convenio.setsIndVigencia(rsResultado.getString("CON_SINDVIGENCIA"));
						}
						moDataOut.put("convenio", convenio);
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

		terminaParametros("cargaConvenio");

		return moDataOut;
	}

	public Map<String, Object> actualizarConvenio(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizaConvenio");

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
		Convenio convenio = (Convenio) moDataIn.get("convenio");

		log.info("ConvenioEJB -> actualizaConvenio IN");

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en actualizaConvenio()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$ACTUALIZA_CONVENIO";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, convenio.getnIdConvenio());
			creaParametroIn(Types.NUMERIC, convenio.getnIdPrestador());
			creaParametroIn(Types.NUMERIC, convenio.getnIdCliente());
			creaParametroIn(Types.VARCHAR, convenio.getsCodTipo());
			creaParametroIn(Types.VARCHAR, convenio.getsCodConvenio());
			creaParametroIn(Types.VARCHAR, convenio.getsNombre());
			creaParametroIn(Types.VARCHAR, convenio.getsNomPoliza());
			creaParametroIn(Types.VARCHAR, convenio.getsCodClase());
			creaParametroIn(Types.VARCHAR, convenio.getsCodSubClase());
			creaParametroIn(Types.VARCHAR, convenio.getsCodEstado());
			creaParametroIn(Types.VARCHAR, convenio.getsDescripcion());
			creaParametroIn(Types.VARCHAR, convenio.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, convenio.getsFhoTermino());
			creaParametroIn(Types.NUMERIC, convenio.getnIdResponsable());
			creaParametroIn(Types.VARCHAR, convenio.getsIndRecaudacion());
			creaParametroIn(Types.VARCHAR, convenio.getsIndPlantilla());
			creaParametroIn(Types.VARCHAR, convenio.getsIndConvenioAbierto());
			creaParametroIn(Types.VARCHAR, convenio.getsIndFacturaPrestador());
			creaParametroIn(Types.NUMERIC, convenio.getnPorRecaudacion());
			creaParametroIn(Types.NUMERIC, convenio.getnCupoConvenio());
			creaParametroIn(Types.NUMERIC, convenio.getnIdActualizador());

			log.info("parametros de entrada convenio------------");
			log.info("Id Convenio -> " + convenio.getnIdConvenio());
			log.info("Id Prestador -> " + convenio.getnIdPrestador());
			log.info("Id Cliente -> " + convenio.getnIdCliente());
			log.info("Cod Tipo -> " + convenio.getsCodTipo());
			log.info("Cod Convenio -> " + convenio.getsCodConvenio());
			log.info("Nombre -> " + convenio.getsNombre());
			log.info("Nombre Poliza -> " + convenio.getsNomPoliza());
			log.info("Cod Clase -> " + convenio.getsCodClase());
			log.info("Cod SubClase -> " + convenio.getsCodSubClase());
			log.info("Cod Estado -> " + convenio.getsCodEstado());
			log.info("Cod Descripcion -> " + convenio.getsDescripcion());
			log.info("Fecha Inicio -> " + convenio.getsFhoInicio());
			log.info("Fecha termino -> " + convenio.getsFhoTermino());
			log.info("Responsable -> " + convenio.getnIdResponsable());
			log.info("Ind Recaudacion -> " + convenio.getsIndRecaudacion());
			log.info("Ind plantilla -> " + convenio.getsIndPlantilla());
			log.info("Ind Convenio Abierto -> " + convenio.getsIndConvenioAbierto());
			log.info("Ind Factura Prestador -> " + convenio.getsIndFacturaPrestador());
			log.info("Porcentaje recaudacion -> " + convenio.getnPorRecaudacion());
			log.info("Cupo -> " + convenio.getnCupoConvenio());
			log.info("Actualizador -> " + convenio.getnIdActualizador());
			log.info("--------------------------------");
			 

			// =============================================================
			// Parametros de Salida
			// =============================================================
			/* Error */
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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

		terminaParametros("actualizaConvenio");

		return moDataOut;
	}

	public Map<String, Object> guardarConvenio(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarConvenio");
		log.info("Inicia parametros en: guardarConvenio()");

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

		Convenio agregaConvenio = (Convenio) moDataIn.get("convenio");
		log.info("ConvenioEJB -> guardarConvenio IN");
		long idConvenio=0; 

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

			log.info("ConvenioEJB -> guardarConvenio IN");

			// =============================================================
			// CUERPO DE EJB
			// =============================================================

			try {
				dbConeccion = solemDS.getConnection();
				// utx.begin();

				log.info("ConvenioEJB -> conectado y listo para llamar en guardarConvenio()");

				log.info("ConvenioEJB -> guardarConvenio ");

				sNombreSP = "CON$AGREGA_CONVENIO";
				sTipoSP = "TX";

				log.info("EJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				creaParametroIn(Types.NUMERIC, agregaConvenio.getnIdPrestador());
				creaParametroIn(Types.NUMERIC, agregaConvenio.getnIdCliente());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsCodTipo());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsCodConvenio());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsNombre());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsNomPoliza());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsCodClase());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsCodSubClase());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsCodEstado());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsDescripcion());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsFhoInicio());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsFhoTermino());
				creaParametroIn(Types.NUMERIC, agregaConvenio.getnIdResponsable());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsIndRecaudacion());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsIndPlantilla());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsIndConvenioAbierto());
				creaParametroIn(Types.VARCHAR, agregaConvenio.getsIndFacturaPrestador());
				creaParametroIn(Types.NUMERIC, agregaConvenio.getnPorRecaudacion());
				creaParametroIn(Types.NUMERIC, agregaConvenio.getnCupoConvenio());
				creaParametroIn(Types.NUMERIC, agregaConvenio.getnIdCreador());
				

				log.info("parametros de entrada guardarConvenio------------");
				log.info("Id Prestador -> " + agregaConvenio.getnIdPrestador());
				log.info("Id Cliente -> " + agregaConvenio.getnIdCliente());
				log.info("Cod Tipo -> " + agregaConvenio.getsCodTipo());
				log.info("Cod Convenio -> " + agregaConvenio.getsCodConvenio());
				log.info("Nombre -> " + agregaConvenio.getsNombre());
				log.info("Nombre poliza -> " + agregaConvenio.getsNomPoliza());
				log.info("Cod Clase -> " + agregaConvenio.getsCodClase());
				log.info("Cod SubClase -> " + agregaConvenio.getsCodSubClase());
				log.info("Cod Estado -> " + agregaConvenio.getsCodEstado());
				log.info("Descripcion -> " + agregaConvenio.getsDescripcion());
				log.info("Fecha Inicio -> " + agregaConvenio.getsFhoInicio());
				log.info("Fecha termino -> " + agregaConvenio.getsFhoTermino());
				log.info("Id Responsable -> " + agregaConvenio.getnIdResponsable());
				log.info("Ind Recaudacion -> " + agregaConvenio.getsIndRecaudacion());
				log.info("Ind Plantilla -> " + agregaConvenio.getsIndPlantilla());
				log.info("Ind Convenio Abierto -> " + agregaConvenio.getsIndConvenioAbierto());
				log.info("Ind Factura Prestador -> " + agregaConvenio.getsIndFacturaPrestador());
				log.info("Porc. recaudacion -> " + agregaConvenio.getnPorRecaudacion());
				log.info("Cupo Convenio -> " + agregaConvenio.getnCupoConvenio());
				log.info("Id usuario -> " + agregaConvenio.getnIdCreador());
				log.info("--------------------------------");
				 

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("idConvenio", Types.INTEGER);
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

				} catch (Exception e) {
					log.error(e);
					error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
					throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				}

				if (cStmt != null) {
					log.info("Cursor != nulo");

					cStmt.execute();

					// Recuperacion parametros de salida
					idConvenio = (Integer) getParametro(cStmt, "idConvenio");
					log.info("idConvenio: " + idConvenio);
					
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
				log.error("ConvenioEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
				// utx.rollback();
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

		} else {
			error = new Error(4, 99, "Usuario NO puede acceder metodo en EJB", Errores.getInstance().getMsgFamilia("4"));
			throw new ErrorSPException(Long.valueOf(error.getNumFamiliaError()), Long.valueOf(error.getNumError()), error.getMsjError(),
					error.getMsjErrorUsuario());
		}

		moDataOut.put("idConvenio", idConvenio);
		moDataOut.put("error", error);

		terminaParametros("guardarConvenio");

		return moDataOut;
	}

	public Map<String, Object> eliminarConvenio(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminaConvenio");

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
		
		long idConvenio = 0;
		idConvenio = (Long) moDataIn.get("idConvenio");
		
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

			log.info("ConvenioEJB -> eliminaConvenio IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("ConvenioEJB -> conectado y listo para llamar en eliminaConvenio()");

				log.info("ConvenioEJB -> LOGUEO ");
				sNombreSP = "CON$ELIMINA_CONVENIO";
				sTipoSP = "TX";

				log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				log.info("id Convenio a eliminar: " + idConvenio);

				creaParametroIn(Types.NUMERIC, idConvenio);

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
				log.error("ConvenioEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally ConvenioEJB -> " + sNombreSP + " Fin");
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

		terminaParametros("eliminaConvenio");

		return moDataOut;
	}


	public Map<String, Object> buscarAnexosConvenio(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Anexos del Convenio");

		// Variables genericas para todo metodo
		iniciaParametros("buscarAnexos");

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
		ArrayList<Anexo> listaAnexos = new ArrayList<Anexo>();

		long idConvenio = 0;

		idConvenio = (Long) moDataIn.get("idConvenio");

		log.info("ConvenioEJB -> buscarAnexos IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarAnexos()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$BUSCA_ANEXOS.LISTA_ANEXOS";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idConvenio);

			log.info("parametros de entrada------------");
			log.info("idConvenio -> " + idConvenio);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_ANEXOS", OracleTypes.CURSOR);
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
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_ANEXOS");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							Anexo anexo = new Anexo();
							anexo.setnIdConvenio(rsResultado.getLong("CAX_NIDCONVENIO"));
							anexo.setnIdAnexo(rsResultado.getLong("CAX_NIDANEXO"));
							anexo.setsNombreAnexo(rsResultado.getString("CAX_SNOMBREANEXO"));
							anexo.setsCodEstado(rsResultado.getString("CAX_SCODESTADO"));
							//anexo.setsDesEstado(rsResultado.getString("CAX_SDESESTADO"));
							anexo.setsIndVigencia(rsResultado.getString("CAX_SINDVIGENCIA"));
							anexo.setsFhoInicio(rsResultado.getString("CAX_DFHOINICIO"));
							anexo.setsFhoTermino(rsResultado.getString("CAX_DFHOTERMINO"));
							
							listaAnexos.add(anexo);
						}
						log.info("cantidad: " + listaAnexos.size());
						moDataOut.put("listaAnexos", listaAnexos);
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

		terminaParametros("buscarAnexos");

		return moDataOut;
	}

	public Map<String, Object> cargarAnexo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Anexo Convenio");

		// Variables genericas para todo metodo
		iniciaParametros("cargarAnexoConvenio");

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

		Anexo anexo = null;

		long idAnexo = 0;

		idAnexo = (Long) moDataIn.get("idAnexo");

		log.info("ConvenioEJB -> cargarAnexoConvenio IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en cargarAnexoConvenio()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$CARGA_ANEXO.DETALLE_ANEXO";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idAnexo);

			log.info("parametros de entrada------------");
			log.info("idAnexo -> " + idAnexo);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_ANEXO", OracleTypes.CURSOR);
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
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_ANEXO");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							anexo = new Anexo();
							anexo.setnIdConvenio(rsResultado.getLong("CAX_NIDCONVENIO"));
							anexo.setsNombreConvenio(rsResultado.getString("CON_SNOMBRE"));
							anexo.setsNombreAnexo(rsResultado.getString("CAX_SNOMBREANEXO"));
							anexo.setnIdAnexo(rsResultado.getLong("CAX_NIDANEXO"));
							anexo.setsCodEstado(rsResultado.getString("CAX_SCODESTADO"));
							anexo.setsDescripcion(rsResultado.getString("CAX_SDESCRIPCION"));
							anexo.setsFhoInicio(rsResultado.getString("CAX_DFHOINICIO"));
							anexo.setsFhoTermino(rsResultado.getString("CAX_DFHOTERMINO"));
						}
						moDataOut.put("anexo", anexo);
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

		terminaParametros("cargarAnexoConvenio");

		return moDataOut;
	}

	public Map<String, Object> actualizarAnexo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizarAnexo");

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
		Anexo anexo = (Anexo) moDataIn.get("anexo");

		log.info("ConvenioEJB -> actualizarAnexo IN");

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en actualizarAnexo()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$ACTUALIZA_ANEXO";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, anexo.getnIdAnexo());
			creaParametroIn(Types.VARCHAR, anexo.getsNombreAnexo());
			creaParametroIn(Types.VARCHAR, anexo.getsDescripcion());
			creaParametroIn(Types.VARCHAR, anexo.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, anexo.getsFhoTermino());
			creaParametroIn(Types.VARCHAR, anexo.getsCodEstado());
			creaParametroIn(Types.NUMERIC, anexo.getnIdActualizador());

			log.info("parametros de entrada anexo------------");
			log.info("Id Anexo -> " + anexo.getnIdAnexo());
			log.info("Nombre -> " + anexo.getsNombreAnexo());
			log.info("Cod Estado -> " + anexo.getsCodEstado());
			log.info("Cod Descripcion -> " + anexo.getsDescripcion());
			log.info("Fecha Inicio -> " + anexo.getsFhoInicio());
			log.info("Fecha termino -> " + anexo.getsFhoTermino());
			log.info("Actualizador -> " + anexo.getnIdActualizador());
			log.info("--------------------------------");
			 

			// =============================================================
			// Parametros de Salida
			// =============================================================
			/* Error */
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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

		terminaParametros("actualizarAnexo");

		return moDataOut;
	}

	public Map<String, Object> guardarAnexo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarAnexo");
		log.info("Inicia parametros en: guardarAnexo()");

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

		Anexo agregaAnexo = (Anexo) moDataIn.get("anexo");
		log.info("ConvenioEJB -> guardarAnexo IN");
		long idAnexo=0; 
		
		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();
			// utx.begin();

			log.info("ConvenioEJB -> conectado y listo para llamar en guardarAnexo()");

			log.info("ConvenioEJB -> guardarAnexo ");

			sNombreSP = "CON$AGREGA_ANEXO";
			sTipoSP = "TX";

			log.info("EJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, agregaAnexo.getnIdConvenio());
			creaParametroIn(Types.VARCHAR, agregaAnexo.getsNombreAnexo());
			creaParametroIn(Types.VARCHAR, agregaAnexo.getsDescripcion());
			creaParametroIn(Types.VARCHAR, agregaAnexo.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, agregaAnexo.getsFhoTermino());
			creaParametroIn(Types.VARCHAR, agregaAnexo.getsCodEstado());
			creaParametroIn(Types.NUMERIC, agregaAnexo.getnIdCreador());
			
			log.info("parametros de entrada guardarAnexo------------");
			log.info("Id Convenio -> " +  agregaAnexo.getnIdConvenio());
			log.info("Nombre Anexo -> " +  agregaAnexo.getsNombreAnexo());
			log.info("Descripcion -> " +  agregaAnexo.getsDescripcion());
			log.info("Fecha Inicio -> " +  agregaAnexo.getsFhoInicio());
			log.info("Fecha Fin -> " +  agregaAnexo.getsFhoTermino());
			log.info("Estado -> " +  agregaAnexo.getsCodEstado());
			log.info("Id actualizador-> " +  agregaAnexo.getnIdCreador());
			log.info("--------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			/* Error */
			creaParametroOut("idAnexo", Types.INTEGER);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
				idAnexo = (Integer) getParametro(cStmt, "idAnexo");
				log.info("idAnexo: " + idAnexo);
				
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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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
		moDataOut.put("idAnexo", idAnexo);
		moDataOut.put("error", error);

		terminaParametros("guardarAnexo");

		return moDataOut;
	}

	public Map<String, Object> eliminarAnexo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarAnexo");

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
		
		long idAnexo = 0;
		idAnexo = (Long) moDataIn.get("idAnexo");
		
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

			log.info("ConvenioEJB -> eliminarAnexo IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("ConvenioEJB -> conectado y listo para llamar en eliminarAnexo()");

				log.info("ConvenioEJB -> LOGUEO ");
				sNombreSP = "CON$ELIMINA_ANEXO";
				sTipoSP = "TX";

				log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				log.info("id Anexo a eliminar: " + idAnexo);

				creaParametroIn(Types.NUMERIC, idAnexo);

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
				log.error("ConvenioEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally ConvenioEJB -> " + sNombreSP + " Fin");
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

		terminaParametros("eliminarAnexo");

		return moDataOut;
	}

	public Map<String, Object> buscarPrestacionesAnexo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Prestaciones del Anexo");

		// Variables genericas para todo metodo
		iniciaParametros("buscarPrestacionesAnexo");

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
		ArrayList<ConvenioAnexoDetalle> listaConvenioAnexoDetalles = new ArrayList<ConvenioAnexoDetalle>();

		long idAnexo = 0;

		idAnexo = (Long) moDataIn.get("idAnexo");
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");

		log.info("ConvenioEJB -> buscarPrestacionesAnexo IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarPrestacionesAnexo()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$BUSCA_PREST_ANEXO.LISTA_PREST_ANEXO";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idAnexo);
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);

			log.info("parametros de entrada------------");
			log.info("idAnexo -> " + idAnexo);
			log.info("Ordenar Por: "+sOrdenarPor);
			log.info("Tipo Orden: "+sTipoOrden);
			log.info("Pag Actual: "+pagActual);			
			log.info("Cant reg x pag: "+regPorPagina);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("TotalRegistros", OracleTypes.INTEGER);
			creaParametroOut("CUR_PRESTANEXO", OracleTypes.CURSOR);
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
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRESTANEXO");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							ConvenioAnexoDetalle anexo = new ConvenioAnexoDetalle();
							anexo.setnIdConvDetalle(rsResultado.getLong("CDE_NIDCONVDETALLE"));
							anexo.setsCodPrestacion(rsResultado.getString("CDE_SCODPRESTACION"));
							anexo.setsNombrePrestacion(rsResultado.getString("CDE_SNOMBREPRESTACION"));
							anexo.setsFecCreacion(rsResultado.getString("CDE_DFHOCREACION"));
							
							listaConvenioAnexoDetalles.add(anexo);
						}
						log.info("cantidad: " + listaConvenioAnexoDetalles.size());
						moDataOut.put("listaConvenioAnexoDetalles", listaConvenioAnexoDetalles);
						moDataOut.put("totalRegistros", totalRegistros);
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
		terminaParametros("buscarPrestacionesAnexo");
		return moDataOut;
	}

	public Map<String, Object> cargarPrestacionAnexo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Prestacion Anexo");

		// Variables genericas para todo metodo
		iniciaParametros("cargarPrestacionAnexo");

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

		ConvenioAnexoDetalle prestacionAnexo = null;

		long idPrestacionAnexo = 0;

		idPrestacionAnexo = (Long) moDataIn.get("idPrestacionAnexo");

		log.info("ConvenioEJB -> cargarPrestacionAnexo IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en cargarPrestacionAnexo()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$CARGA_PREST_ANEXO.DETALLE_PREST_ANEXO";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idPrestacionAnexo);

			log.info("parametros de entrada------------");
			log.info("idPrestacionAnexo -> " + idPrestacionAnexo);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_PRESTANEXO", OracleTypes.CURSOR);
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
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRESTANEXO");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							prestacionAnexo = new ConvenioAnexoDetalle();
							prestacionAnexo.setnIdConvDetalle(rsResultado.getLong("CDE_NIDCONVDETALLE"));
							prestacionAnexo.setnIdAnexo(rsResultado.getLong("CDE_NIDANEXO"));
							prestacionAnexo.setnIdPrestacion(rsResultado.getLong("CDE_NIDPRESTACION"));
							prestacionAnexo.setnIdConvenio(rsResultado.getLong("CDE_NIDCONVENIO"));
							prestacionAnexo.setsNombreConvenio(rsResultado.getString("CON_SNOMBRE"));
							prestacionAnexo.setsNombreAnexo(rsResultado.getString("CAX_SNOMBREANEXO"));
							prestacionAnexo.setsCodPrestacion(rsResultado.getString("CDE_SCODPRESTACION"));
							prestacionAnexo.setsNombrePrestacion(rsResultado.getString("CDE_SNOMBREPRESTACION"));
							prestacionAnexo.setsNombrePrestCatalogo(rsResultado.getString("CDE_SNOMBREPRESTCATALOGO"));
							prestacionAnexo.setsNombrePrestConvenio(rsResultado.getString("CDE_SNOMBREPRESTCONVENIO"));
							prestacionAnexo.setsIndCoSeguro(rsResultado.getString("CDE_SINDCOSEGURO"));
							prestacionAnexo.setsCodPeriodoEve(rsResultado.getString("CDE_SCODPERIODOEVE"));
							prestacionAnexo.setnCantEventos(rsResultado.getLong("CDE_NCANTEVENTOS"));
							prestacionAnexo.setsCodPeriodoCup(rsResultado.getString("CDE_SCODPERIODOCUP"));
							prestacionAnexo.setnMontoOcupacion(rsResultado.getLong("CDE_NMONTOOCUPACION"));
							prestacionAnexo.setsIndTipoPago(rsResultado.getString("CDE_SINDTIPOPAGO"));
							prestacionAnexo.setnTopePrecio(rsResultado.getLong("CDE_NTOPEPRECIO"));
							prestacionAnexo.setnCantCapacitacion(rsResultado.getLong("CDE_NCANTCAPITACION"));
							prestacionAnexo.setnPorCobertura(rsResultado.getLong("CDE_NPORCCOBERTURA"));
							prestacionAnexo.setsFecCreacion(rsResultado.getString("CDE_DFECCREACION"));
							prestacionAnexo.setnIdCreador(rsResultado.getLong("CDE_NIDCREADOR"));
							prestacionAnexo.setsFecActualizacion(rsResultado.getString("CDE_DFECACTUALIZACION"));
							prestacionAnexo.setnIdActualizador(rsResultado.getLong("CDE_NIDACTUALIZADOR"));
						}
						moDataOut.put("prestacionAnexo", prestacionAnexo);
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

		terminaParametros("cargarPrestacionAnexo");

		return moDataOut;
	}

	public Map<String, Object> actualizarPrestacionAnexo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizarPrestacionAnexo");

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
		ConvenioAnexoDetalle convenioAnexoDetalle = (ConvenioAnexoDetalle) moDataIn.get("convenioAnexoDetalle");

		log.info("ConvenioEJB -> actualizarPrestacionAnexo IN");

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en actualizarPrestacionAnexo()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$ACTUALIZA_PRESTACION";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, convenioAnexoDetalle.getnIdConvDetalle());
			creaParametroIn(Types.VARCHAR, convenioAnexoDetalle.getsIndTipoPago());
			creaParametroIn(Types.NUMERIC, convenioAnexoDetalle.getnIdActualizador());

			log.info("parametros de entrada anexo------------");
			log.info("Id Conv Anexo Detalle -> " + convenioAnexoDetalle.getnIdConvDetalle());
			log.info("Ind Tipo pago -> " + convenioAnexoDetalle.getsIndTipoPago());
			log.info("Actualizador -> " + convenioAnexoDetalle.getnIdActualizador());
			log.info("--------------------------------");
			 

			// =============================================================
			// Parametros de Salida
			// =============================================================
			/* Error */
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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

		terminaParametros("actualizarPrestacionAnexo");

		return moDataOut;
	}

	public Map<String, Object> guardarPrestacionAnexo(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarPrestacionAnexo");
		log.info("Inicia parametros en: guardarPrestacionAnexo()");

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

		ConvenioAnexoDetalle prestacionAnexo = (ConvenioAnexoDetalle) moDataIn.get("convenioAnexoDetalle");
		log.info("ConvenioEJB -> guardarPrestacionAnexo IN");
		long idPrestacionAnexo=0; 
		
		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();
			// utx.begin();

			log.info("ConvenioEJB -> conectado y listo para llamar en guardarPrestacionAnexo()");

			log.info("ConvenioEJB -> guardarPrestacionAnexo ");

			sNombreSP = "CON$AGREGA_PRES_ANEXO";
			sTipoSP = "TX";

			log.info("EJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, prestacionAnexo.getnIdConvenio());
			creaParametroIn(Types.NUMERIC, prestacionAnexo.getnIdAnexo());
			creaParametroIn(Types.NUMERIC, prestacionAnexo.getnIdPrestacion());
			creaParametroIn(Types.VARCHAR, prestacionAnexo.getsNombrePrestConvenio());
			creaParametroIn(Types.VARCHAR, prestacionAnexo.getsIndTipoPago());
			creaParametroIn(Types.NUMERIC, prestacionAnexo.getnIdCreador());
			
			log.info("parametros de entrada guardarPrestacionAnexo------------");
			log.info("Id Convenio -> " +  prestacionAnexo.getnIdConvenio());
			log.info("Id Anexo -> " +  prestacionAnexo.getnIdAnexo());
			log.info("Id Prestacion -> " +  prestacionAnexo.getnIdPrestacion());
			log.info("Nombre prestacion convenio -> " +  prestacionAnexo.getsNombrePrestConvenio());
			log.info("Ind tipo pago -> " +  prestacionAnexo.getsIndTipoPago());
			log.info("Id creador-> " +  prestacionAnexo.getnIdCreador());
			log.info("--------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			/* Error */
			creaParametroOut("idPrestacionAnexo", Types.INTEGER);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
				idPrestacionAnexo = (Integer) getParametro(cStmt, "idPrestacionAnexo");
				log.info("idAnexo: " + idPrestacionAnexo);
				
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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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
		moDataOut.put("idPrestacionAnexo", idPrestacionAnexo);
		moDataOut.put("error", error);

		terminaParametros("guardarPrestacionAnexo");

		return moDataOut;
	}

	public Map<String, Object> eliminarPrestacionAnexo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarPrestacionAnexo");

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
		
		long idPrestacionAnexo = 0;
		idPrestacionAnexo = (Long) moDataIn.get("idPrestacionAnexo");
		
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

			log.info("ConvenioEJB -> eliminarPrestacionAnexo IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("ConvenioEJB -> conectado y listo para llamar en eliminarPrestacionAnexo()");

				log.info("ConvenioEJB -> LOGUEO ");
				sNombreSP = "CON$ELIMINA_PRES_ANEXO";
				sTipoSP = "TX";

				log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				log.info("id PrestacionAnexo a eliminar: " + idPrestacionAnexo);

				creaParametroIn(Types.NUMERIC, idPrestacionAnexo);

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
				log.error("ConvenioEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally ConvenioEJB -> " + sNombreSP + " Fin");
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

		terminaParametros("eliminarPrestacionAnexo");

		return moDataOut;
	}

	public Map<String, Object> buscarPrecios(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Precios de la Prestacion");
		iniciaParametros("buscarPrecios");

		// Variables genericas para todo metodo

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
		ArrayList<Precio> listaPrecios = new ArrayList<Precio>();

		long idPrestacionAnexo = 0;

		idPrestacionAnexo = (Long) moDataIn.get("idPrestacionAnexo");
		log.info("id prestacion anexo:"+idPrestacionAnexo);

		log.info("ConvenioEJB -> buscarPrecios IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarPrecios()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$BUSCA_PRECIOS.LISTA_PRECIOS";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idPrestacionAnexo);

			log.info("parametros de entrada------------");
			log.info("idPrestacionAnexo -> " + idPrestacionAnexo);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_PRECIOS", OracleTypes.CURSOR);
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
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRECIOS");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							Precio precio = new Precio();
							precio.setnIdConvenioPrecio(rsResultado.getLong("CPR_NIDCONVENIOPRECIO"));
							precio.setnIdConvDetalle(rsResultado.getLong("CPR_NIDCONVDETALLE"));
							precio.setnPrecio(rsResultado.getLong("CPR_NPRECIO"));
							precio.setsFormula(rsResultado.getString("CPR_SFORMULA"));
							precio.setsFhoInicio(rsResultado.getString("CPR_DFHOINICIO"));
							precio.setsFhoFin(rsResultado.getString("CPR_DFHOFIN"));
							
							listaPrecios.add(precio);
						}
						log.info("cantidad: " + listaPrecios.size());
						moDataOut.put("listaPrecios", listaPrecios);
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

		terminaParametros("buscarPrecios");

		return moDataOut;
	}


	public Map<String, Object> cargarPrecio(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Precio Convenio");

		// Variables genericas para todo metodo
		iniciaParametros("cargarPrecioConvenio");

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

		Precio precio = null;

		long idPrecio = 0;

		idPrecio = (Long) moDataIn.get("idPrecio");

		log.info("ConvenioEJB -> cargarPrecioConvenio IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en cargarPrecioConvenio()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$CARGA_PRECIO.DETALLE_PRECIO";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idPrecio);

			log.info("parametros de entrada------------");
			log.info("idPrecio -> " + idPrecio);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_PRECIO", OracleTypes.CURSOR);
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
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_PRECIO");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							precio = new Precio();
							precio.setnIdConvenioPrecio(rsResultado.getLong("CPR_NIDCONVENIOPRECIO"));
							precio.setnIdConvDetalle(rsResultado.getLong("CPR_NIDCONVDETALLE"));
							precio.setnIdAnexo(rsResultado.getLong("CPR_NIDANEXO"));
							precio.setnIdPrestacion(rsResultado.getLong("CPR_NIDPRESTACION"));
							precio.setsFhoInicio(rsResultado.getString("CPR_DFHOINICIO"));
							precio.setsFhoFin(rsResultado.getString("CPR_DFHOFIN"));
							precio.setsIndPrecioFormula(rsResultado.getString("CPR_SINDPRECIOFORMULA"));
							precio.setnPrecio(rsResultado.getLong("CPR_NPRECIO"));
							precio.setsFormula(rsResultado.getString("CPR_SFORMULA"));
							precio.setsNombrePrestacion(rsResultado.getString("CDE_SNOMBREPRESTACION"));
							precio.setsCodTipoMoneda(rsResultado.getString("CPR_SCODTIPOMONEDA"));
						}
						
						moDataOut.put("precio", precio);
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

		terminaParametros("cargarPrecioConvenio");

		return moDataOut;
	}

	public Map<String, Object> actualizarPrecio(Map<String, Object> moDataIn) throws ErrorSPException {
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizarPrecio");

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
		Precio precio = (Precio) moDataIn.get("precio");

		log.info("ConvenioEJB -> actualizarPrecio IN");

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en actualizarPrecio()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$ACTUALIZA_PRECIO";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, precio.getnIdConvenioPrecio());
			creaParametroIn(Types.VARCHAR, precio.getsCodTipoMoneda());
			creaParametroIn(Types.VARCHAR, precio.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, precio.getsFhoFin());
			creaParametroIn(Types.VARCHAR, precio.getsIndPrecioFormula());
			creaParametroIn(Types.NUMERIC, precio.getnPrecio());
			creaParametroIn(Types.VARCHAR, precio.getsFormula());
			creaParametroIn(Types.NUMERIC, precio.getnIdActualizador());

			log.info("parametros de entrada guardarPrecio------------");
			log.info("Id ConvDetalle -> " +  precio.getnIdConvDetalle());
			log.info("Cod Tipo Moneda -> " + precio.getsCodTipoMoneda());
			log.info("Fecha Inicio -> " +  precio.getsFhoInicio());
			log.info("Fecha Fin -> " +  precio.getsFhoFin());
			log.info("Ind Precio Formula -> " +  precio.getsIndPrecioFormula());
			log.info("Precio -> " +  precio.getnPrecio());
			log.info("Formula -> " +  precio.getsFormula());
			log.info("Id usuario -> " +  precio.getnIdActualizador());
			log.info("--------------------------------");
			 

			// =============================================================
			// Parametros de Salida
			// =============================================================
			/* Error */
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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

		terminaParametros("actualizarPrecio");

		return moDataOut;
	}

	public Map<String, Object> guardarPrecio(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarPrecio");
		log.info("Inicia parametros en: guardarPrecio()");

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

		Precio agregaPrecio = (Precio) moDataIn.get("precio");
		log.info("ConvenioEJB -> guardarPrecio IN");
		long idPrecio=0; 
		
		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();
			// utx.begin();

			log.info("ConvenioEJB -> conectado y listo para llamar en guardarPrecio()");

			log.info("ConvenioEJB -> guardarPrecio ");

			sNombreSP = "CON$AGREGA_PRECIO";
			sTipoSP = "TX";

			log.info("EJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, agregaPrecio.getnIdConvDetalle());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsCodTipoMoneda());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsFhoFin());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsIndPrecioFormula());
			creaParametroIn(Types.NUMERIC, agregaPrecio.getnPrecio());
			creaParametroIn(Types.VARCHAR, agregaPrecio.getsFormula());
			creaParametroIn(Types.NUMERIC, agregaPrecio.getnIdUsuarioCreacion());
			
			log.info("parametros de entrada guardarPrecio------------");
			log.info("Id ConvDetalle -> " +  agregaPrecio.getnIdConvDetalle());
			log.info("Cod Tipo Moneda -> " + agregaPrecio.getsCodTipoMoneda());
			log.info("Fecha Inicio -> " +  agregaPrecio.getsFhoInicio());
			log.info("Fecha Fin -> " +  agregaPrecio.getsFhoFin());
			log.info("Ind Precio Formula -> " +  agregaPrecio.getsIndPrecioFormula());
			log.info("Precio -> " +  agregaPrecio.getnPrecio());
			log.info("Formula -> " +  agregaPrecio.getsFormula());
			log.info("Id usuario -> " +  agregaPrecio.getnIdUsuarioCreacion());
			log.info("--------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			/* Error */
			creaParametroOut("idPrecio", Types.INTEGER);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
				idPrecio = (Integer) getParametro(cStmt, "idPrecio");
				log.info("idPrecio: " + idPrecio);
				
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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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
		moDataOut.put("idPrecio", idPrecio);
		moDataOut.put("error", error);

		terminaParametros("guardarPrecio");

		return moDataOut;
	}

	public Map<String, Object> eliminarPrecio(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarPrecio");

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
		
		long idPrecio = 0;
		idPrecio = (Long) moDataIn.get("idPrecio");
		
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

			log.info("ConvenioEJB -> eliminarPrecio IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("ConvenioEJB -> conectado y listo para llamar en eliminarPrecio()");

				log.info("ConvenioEJB -> LOGUEO ");
				sNombreSP = "CON$ELIMINA_PRECIO";
				sTipoSP = "TX";

				log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				log.info("id Precio a eliminar: " + idPrecio);

				creaParametroIn(Types.NUMERIC, idPrecio);

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
				log.error("ConvenioEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally ConvenioEJB -> " + sNombreSP + " Fin");
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
		terminaParametros("eliminarPrecio");
		return moDataOut;
	}

	public Map<String, Object> buscarModificadores(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Modificadores de la Prestacion");
		
		iniciaParametros("buscarModificadores");

		// Variables genericas para todo metodo

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
		ArrayList<Modificador> listaModificadores = new ArrayList<Modificador>();

		long idPrestacionAnexo = 0;

		idPrestacionAnexo = (Long) moDataIn.get("idPrestacionAnexo");

		log.info("ConvenioEJB -> buscarModificadores IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarModificadores()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$BUSCA_MODIFICADORES.LISTA_MODIFICADORES";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");
			log.info("idPrestacionAnexo -> " + idPrestacionAnexo);
			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idPrestacionAnexo);

			log.info("parametros de entrada------------");
			log.info("idPrestacionAnexo -> " + idPrestacionAnexo);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_MODIFICADORES", OracleTypes.CURSOR);
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
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_MODIFICADORES");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							Modificador modificador = new Modificador();
							modificador.setnIdModificador(rsResultado.getLong("CMO_NIDMODIFICADOR"));
							modificador.setnIdConvDetalle(rsResultado.getLong("CMO_NIDCONVDETALLE"));
							modificador.setsCodTipo(rsResultado.getString("CMO_SCODTIPO"));
							modificador.setsNombre(rsResultado.getString("CMO_SNOMBRE"));
							modificador.setsFhoInicio(rsResultado.getString("CMO_DFHOINICIO"));
							modificador.setsFhoFin(rsResultado.getString("CMO_DFHOFIN"));
							
							listaModificadores.add(modificador);
						}
						log.info("cantidad: " + listaModificadores.size());
						moDataOut.put("listaModificadores", listaModificadores);
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

		terminaParametros("buscarModificadores");

		return moDataOut;
	}

	public Map<String, Object> cargarModificador(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Modificador Convenio");

		// Variables genericas para todo metodo
		iniciaParametros("cargarModificadorConvenio");

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

		Modificador modificador = null;

		long idModificador = 0;

		idModificador = (Long) moDataIn.get("idModificador");

		log.info("ConvenioEJB -> cargarModificadorConvenio IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en cargarModificadorConvenio()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$CARGA_MODIFICADOR.DETALLE_MODIFICADOR";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idModificador);

			log.info("parametros de entrada------------");
			log.info("idModificador -> " + idModificador);
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
							modificador = new Modificador();
							modificador.setnIdModificador(rsResultado.getLong("CMO_NIDMODIFICADOR"));
							modificador.setnIdConvDetalle(rsResultado.getLong("CMO_NIDCONVDETALLE"));
							modificador.setnIdAnexo(rsResultado.getLong("CMO_NIDANEXO"));
							modificador.setnIdConvenio(rsResultado.getLong("CMO_NIDCONVENIO"));
							modificador.setsNombre(rsResultado.getString("CMO_SNOMBRE"));
							modificador.setsCodTipo(rsResultado.getString("CMO_SCODTIPO"));
							modificador.setsCondicion(rsResultado.getString("CMO_SCONDICION"));
							modificador.setsFormula(rsResultado.getString("CMO_SFORMULA"));
							modificador.setsFhoInicio(rsResultado.getString("CMO_DFHOINICIO"));
							modificador.setsFhoFin(rsResultado.getString("CMO_DFHOFIN"));
						}
						
						moDataOut.put("modificador", modificador);
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

		terminaParametros("cargarModificadorConvenio");

		return moDataOut;
	}


	public Map<String, Object> actualizarModificador(Map<String, Object> moDataIn) throws ErrorSPException {
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizarModificador");

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
		Modificador modificador = (Modificador) moDataIn.get("modificador");

		log.info("ConvenioEJB -> actualizarModificador IN");

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en actualizarModificador()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$ACTUALIZA_MODIFICADOR";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, modificador.getnIdModificador());
			creaParametroIn(Types.VARCHAR, modificador.getsNombre());
			creaParametroIn(Types.VARCHAR, modificador.getsCodTipo());
			creaParametroIn(Types.VARCHAR, modificador.getsCondicion());
			creaParametroIn(Types.VARCHAR, modificador.getsFormula());
			creaParametroIn(Types.VARCHAR, modificador.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, modificador.getsFhoFin());
			creaParametroIn(Types.NUMERIC, modificador.getnIdUsuarioModificacion());

			log.info("parametros de entrada guardarModificador------------");
			log.info("Id Modificador -> " +  modificador.getnIdModificador());
			log.info("Nombre -> " +  modificador.getsNombre());
			log.info("Cod Tipo -> " +  modificador.getsCodTipo());
			log.info("Condicion -> " +  modificador.getsCondicion());
			log.info("Formula -> " +  modificador.getsFormula());
			log.info("Fecha Inicio -> " +  modificador.getsFhoInicio());
			log.info("Fecha Fin -> " +  modificador.getsFhoFin());
			log.info("Id usuario -> " +  modificador.getnIdUsuarioModificacion());
			log.info("--------------------------------");

			// =============================================================
			// Parametros de Salida
			// =============================================================
			/* Error */
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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

		terminaParametros("actualizarModificador");

		return moDataOut;
	}

	public Map<String, Object> guardarModificador(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarModificador");
		log.info("Inicia parametros en: guardarModificador()");

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

		Modificador agregaModificador = (Modificador) moDataIn.get("modificador");
		log.info("ConvenioEJB -> guardarModificador IN");
		long idModificador=0; 
		
		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();
			// utx.begin();

			log.info("ConvenioEJB -> conectado y listo para llamar en guardarModificador()");

			log.info("ConvenioEJB -> guardarModificador ");

			sNombreSP = "CON$AGREGA_MODIFICADOR";
			sTipoSP = "TX";

			log.info("EJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, agregaModificador.getnIdConvDetalle());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsNombre());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsCodTipo());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsCondicion());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsFormula());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, agregaModificador.getsFhoFin());
			creaParametroIn(Types.NUMERIC, agregaModificador.getnIdUsuarioCreacion());
			
			log.info("parametros de entrada guardarModificador------------");
			log.info("Id ConvDetalle -> " +  agregaModificador.getnIdConvDetalle());
			log.info("Nombre -> " +  agregaModificador.getsNombre());
			log.info("Cod Tipo -> " +  agregaModificador.getsCodTipo());
			log.info("Condicion -> " +  agregaModificador.getsCondicion());
			log.info("Formula -> " +  agregaModificador.getsFormula());
			log.info("Fecha Inicio -> " +  agregaModificador.getsFhoInicio());
			log.info("Fecha Fin -> " +  agregaModificador.getsFhoFin());
			log.info("Id usuario -> " +  agregaModificador.getnIdUsuarioCreacion());
			log.info("--------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			/* Error */
			creaParametroOut("idModificador", Types.INTEGER);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
				idModificador = (Integer) getParametro(cStmt, "idModificador");
				log.info("idModificador: " + idModificador);
				
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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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
		moDataOut.put("idModificador", idModificador);
		moDataOut.put("error", error);

		terminaParametros("guardarModificador");

		return moDataOut;
	}

	public Map<String, Object> eliminarModificador(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarModificador");

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
		
		long idModificador = 0;
		idModificador = (Long) moDataIn.get("idModificador");
		
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

			log.info("ConvenioEJB -> eliminarModificador IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("ConvenioEJB -> conectado y listo para llamar en eliminarModificador()");

				log.info("ConvenioEJB -> LOGUEO ");
				sNombreSP = "CON$ELIMINA_MODIFICADOR";
				sTipoSP = "TX";

				log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				log.info("id Modificador a eliminar: " + idModificador);

				creaParametroIn(Types.NUMERIC, idModificador);

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
				log.error("ConvenioEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally ConvenioEJB -> " + sNombreSP + " Fin");
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
		terminaParametros("eliminarModificador");
		return moDataOut;
	}

	public Map<String, Object> buscarEmpresasInst(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		log.info("Busca Instituciones en  ConvenioEJB");
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

		log.info("ConvenioEJB -> buscarEmpresasInst IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarEmpresasInst()");

			log.info("ConvenioEJB -> buscarEmpresasInst");

			sNombreSP = "TYC$BUSCA_INSTITUCIONES.LISTA_INSTITUCIONES";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

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

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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

					log.info("ConvenioEJB -> " + sNombreSP + " rsResultado Inicio");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_INSTITUCIONES");
					log.info("ConvenioEJB -> " + sNombreSP + " rsResultado Fin");

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

		terminaParametros("buscarEmpresasInst");

		return moDataOut;
	}

	public Map<String, Object> buscarEmpresasCli(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		log.info("Busca Clientes en ConvenioEJB");
		iniciaParametros("buscarEmpresasCli");

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

		log.info("ConvenioEJB -> buscarEmpresasCli IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarEmpresasCli()");

			log.info("ConvenioEJB -> buscarEmpresasCli");

			sNombreSP = "TYC$BUSCA_CLIENTES.LISTA_CLIENTES";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

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

			creaParametroOut("CUR_CLIENTES", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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

					log.info("ConvenioEJB -> " + sNombreSP + " rsResultado Inicio");
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_CLIENTES");
					log.info("ConvenioEJB -> " + sNombreSP + " rsResultado Fin");

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
						moDataOut.put("listaEmpresasCli", listaEmpresas);
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

		terminaParametros("buscarEmpresasCli");

		return moDataOut;
	}

	public Map<String, Object> buscarPrestacionesSinAnexo(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Prestaciones que no son del Anexo");

		// Variables genericas para todo metodo
		iniciaParametros("buscarPrestacionesSinAnexo");

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
		ArrayList<CatalogoPrest> listaCatalogosPrest = new ArrayList<CatalogoPrest>();

		long idAnexo = 0;
		long idConvenio = 0;
		CatalogoPrest catalogoPres = (CatalogoPrest) moDataIn.get("catalogoPrest");

		idAnexo = (Long) moDataIn.get("idAnexo");
		idConvenio = (Long) moDataIn.get("idConvenio");
		
		String sOrdenarPor = (String)moDataIn.get("sOrdenarPor");
		String sTipoOrden = (String)moDataIn.get("sTipoOrden");
		int pagActual = (Integer)moDataIn.get("pagActual");
		int regPorPagina = (Integer)moDataIn.get("regPorPagina");

		log.info("ConvenioEJB -> buscarPrestacionesSinAnexo IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarPrestacionesSinAnexo()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$BUSCA_PRES_SINANEXO.LISTA_PRES_SINANEXO";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idAnexo);
			creaParametroIn(Types.NUMERIC, idConvenio);
			creaParametroIn(Types.VARCHAR,catalogoPres.getsCodPrestacion());
			creaParametroIn(Types.VARCHAR,catalogoPres.getsNomPrestacion());
			creaParametroIn(Types.VARCHAR, sOrdenarPor);
			creaParametroIn(Types.VARCHAR, sTipoOrden);
			creaParametroIn(Types.INTEGER, pagActual);
			creaParametroIn(Types.INTEGER, regPorPagina);

			log.info("parametros de entrada------------");
			log.info("idAnexo -> " + idAnexo);
			log.info("idConvenio -> "+ idConvenio);
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
			creaParametroOut("CUR_CAT_PRES", OracleTypes.CURSOR);
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
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_CAT_PRES");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							CatalogoPrest prest = new CatalogoPrest();
							prest.setnIdCatalogoPrestacion(rsResultado.getLong("CPR_NIDCATALOGOPRESTACION"));
							prest.setnIdPrestacion(rsResultado.getLong("CPR_NIDPRESTACION"));
							prest.setsCodPrestacion(rsResultado.getString("CPR_SCODPRESTACION"));
							prest.setsNomPrestacion(rsResultado.getString("CPR_SNOMPRESTACION"));
							prest.setsNomPrestCatalogo(rsResultado.getString("CPR_SNOMPRESTCATALOGO"));
							prest.setsNomCatalogo(rsResultado.getString("CAT_SNOMBRE"));
							
							listaCatalogosPrest.add(prest);
						}
						log.info("cantidad: " + listaCatalogosPrest.size());
						moDataOut.put("listaPrestacionesSinAnexo", listaCatalogosPrest);
						moDataOut.put("totalRegistros", totalRegistros);
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

		terminaParametros("buscarPrestacionesSinAnexo");

		return moDataOut;
	}

	public Map<String, Object> buscarBeneficiosConvenio(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Beneficios del Convenio");

		// Variables genericas para todo metodo
		iniciaParametros("buscarBeneficios");

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
		ArrayList<Beneficio> listaBeneficios = new ArrayList<Beneficio>();

		long idConvenio = 0;

		idConvenio = (Long) moDataIn.get("idConvenio");

		log.info("ConvenioEJB -> buscarBeneficios IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarBeneficios()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$BUSCA_BENEFICIARIOS.LISTA_BENEFICIARIOS";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idConvenio);

			log.info("parametros de entrada------------");
			log.info("idConvenio -> " + idConvenio);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_BENEFI", OracleTypes.CURSOR);
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
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_BENEFI");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							Beneficio beneficio = new Beneficio();
							beneficio.setnIdConvBeneficio(rsResultado.getLong("COB_NIDCONVBENEFICIO"));
							beneficio.setnIdConvenio(rsResultado.getLong("COB_NIDCONVENIO"));
							beneficio.setnIdPlanBeneficio(rsResultado.getLong("COB_NIDPLANBENEFICIO"));
							beneficio.setsCodPlan(rsResultado.getString("COB_SCODPLAN"));
							beneficio.setsNombrePlan(rsResultado.getString("COB_SNOMBREPLAN"));
							beneficio.setsCodEstado(rsResultado.getString("COB_SCODESTADO"));
							
							listaBeneficios.add(beneficio);
						}
						log.info("cantidad: " + listaBeneficios.size());
						moDataOut.put("listaBeneficios", listaBeneficios);
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

		terminaParametros("buscarBeneficios");

		return moDataOut;
	}

	public Map<String, Object> guardarBeneficio(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarBeneficio");
		log.info("Inicia parametros en: guardarBeneficio()");

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

		Beneficio agregaBeneficio = (Beneficio) moDataIn.get("beneficio");
		log.info("ConvenioEJB -> guardarBeneficio IN");
		long idBeneficio=0; 
		
		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();
			// utx.begin();

			log.info("ConvenioEJB -> conectado y listo para llamar en guardarBeneficio()");

			log.info("ConvenioEJB -> guardarBeneficio ");

			sNombreSP = "CON$AGREGA_BENEFICIARIO";
			sTipoSP = "TX";

			log.info("EJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, agregaBeneficio.getnIdConvenio());
			creaParametroIn(Types.NUMERIC, agregaBeneficio.getnIdPlanBeneficio());
			creaParametroIn(Types.NUMERIC, agregaBeneficio.getnIdUsuarioCreacion());
			
			
			log.info("parametros de entrada guardarBeneficio------------");
			log.info("Id Convenio -> " +  agregaBeneficio.getnIdConvenio());
			log.info("Id Plan Beneficio -> " +  agregaBeneficio.getnIdPlanBeneficio());
			log.info("Id Usuario -> " +  agregaBeneficio.getnIdUsuarioCreacion());
			log.info("--------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			/* Error */
			//creaParametroOut("idBeneficio", Types.INTEGER);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
//				idBeneficio = (Integer) getParametro(cStmt, "idBeneficio");
//				log.info("idBeneficio: " + idBeneficio);
				
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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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
		//moDataOut.put("idBeneficio", idBeneficio);
		moDataOut.put("error", error);

		terminaParametros("guardarBeneficio");

		return moDataOut;
	}

	public Map<String, Object> eliminarBeneficio(Map<String, Object> moDataIn) throws ErrorSPException 
	{
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarBeneficio");

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
		
		long idBeneficio = 0;
		idBeneficio = (Long) moDataIn.get("idBeneficio");
		
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

			log.info("ConvenioEJB -> eliminarBeneficio IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("ConvenioEJB -> conectado y listo para llamar en eliminarBeneficio()");

				log.info("ConvenioEJB -> LOGUEO ");
				sNombreSP = "CON$ELIMINA_BENEFICIO";
				sTipoSP = "TX";

				log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				log.info("id Beneficio a eliminar: " + idBeneficio);

				creaParametroIn(Types.NUMERIC, idBeneficio);

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
				log.error("ConvenioEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally ConvenioEJB -> " + sNombreSP + " Fin");
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

		terminaParametros("eliminarBeneficio");

		return moDataOut;
	}
	
	public Map<String, Object> buscarResponsables(Map<String, Object> moDataIn) throws ErrorSPException {
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

		log.info("ConvenioEJB -> buscarResponsables IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarResponsables()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "TYC$BUSCA_RESPONSABLES.BUSCA_RESPONSABLES";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

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

		terminaParametros("buscarResponsables");

		return moDataOut;
	}

	public Map<String, Object> buscarPlanesBeneficio(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		log.info("Busca Convenios en  ConveniosEJB");
		iniciaParametros("buscarPlanesBeneficio");

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

		ArrayList<PlanBeneficio> listaPlanesBeneficio = null;
		PlanBeneficio plan = (PlanBeneficio) moDataIn.get("planBeneficio");
		long idConvenio = (Long) moDataIn.get("idConvenio");

		log.info("ConvenioEJB -> buscarPlanesBeneficio IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarPlanesBeneficio()");

			log.info("ConvenioEJB -> buscarPlanesBeneficio");

			sNombreSP = "CON$BUSCA_PLANES_BENEFICIO.LISTA_PLANES";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			//creaParametroIn(Types.NUMERIC, plan.getnIdPrestador());
			creaParametroIn(Types.NUMERIC, plan.getnIdEmpresa());
			creaParametroIn(Types.NUMERIC, idConvenio);
			creaParametroIn(Types.VARCHAR, plan.getsCodPlan());
			creaParametroIn(Types.VARCHAR, plan.getsNombrePlan());

			log.info("Id empresa: " + plan.getnIdEmpresa());
			log.info("Id Convenio: " + idConvenio);
			log.info("Codigo: " + plan.getsCodPlan());
			log.info("Nombre: " + plan.getsNombrePlan());

			log.info("--------------------------------");
			
			// =============================================================
			// Parametros de Salida
			// =============================================================

			creaParametroOut("CUR_PLANES", OracleTypes.CURSOR);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
				}else{
					try{
						//error = new Error(0, 0, "", "");
						error = new Error("0","");
	
						log.info("ConvenioEJB -> " + sNombreSP + " rsResultado Inicio");
						rsResultado = (ResultSet) getParametro(cStmt, "CUR_PLANES");
						log.info("ConvenioEJB -> " + sNombreSP + " rsResultado Fin");
	
						if (rsResultado != null) {
							log.info("rsResultado != nulo");
	
							listaPlanesBeneficio = new ArrayList<PlanBeneficio>();
	
							while (rsResultado.next()) {
								PlanBeneficio planBeneficio = new PlanBeneficio();
								planBeneficio.setnIdPlanBeneficio(rsResultado.getLong("EPB_NIDPLANBENEFICIO"));
								planBeneficio.setsCodPlan(rsResultado.getString("EPB_SCODPLAN"));
								planBeneficio.setsNombrePlan(rsResultado.getString("EPB_SNOMBREPLAN"));
								
								listaPlanesBeneficio.add(planBeneficio);
							}
							log.info("cantidad: " + listaPlanesBeneficio.size());
							moDataOut.put("listaPlanesBeneficio", listaPlanesBeneficio);
							rsResultado.close();
						}
					
					}catch(Exception e)
					{
						e.printStackTrace();
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

		terminaParametros("buscarPlanesBeneficio");

		return moDataOut;
	}

	public Map<String, Object> buscarRestriccionesConvenio(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga lista de Restricciones del Convenio");

		// Variables genericas para todo metodo
		iniciaParametros("buscarRestricciones");

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
		ArrayList<Restriccion> listaRestricciones = new ArrayList<Restriccion>();

		long idConvenio = 0;

		idConvenio = (Long) moDataIn.get("idConvenio");

		log.info("ConvenioEJB -> buscarRestricciones IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarRestricciones()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$BUSCA_RESTRICCIONES.LISTA_RESTRICCIONES";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idConvenio);

			log.info("parametros de entrada------------");
			log.info("idConvenio -> " + idConvenio);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_RESTRICCION", OracleTypes.CURSOR);
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
					
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_RESTRICCION");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							Restriccion restriccion = new Restriccion();
							restriccion.setnIdRestriccion(rsResultado.getLong("CRE_NIDRESTRICCION"));
							restriccion.setnIdConvenio(rsResultado.getLong("CRE_NIDCONVENIO"));
							restriccion.setsNombre(rsResultado.getString("CRE_SNOMBRE"));
							restriccion.setsFhoInicio(rsResultado.getString("CRE_DFHOINICIO"));
							restriccion.setsFhoFin(rsResultado.getString("CRE_DFHOFIN"));
							restriccion.setsCodTipo(rsResultado.getString("CRE_SCODTIPO"));
							restriccion.setsDesTipo(rsResultado.getString("CRE_SDESTIPO"));
							restriccion.setsValor(rsResultado.getString("CRE_SVALOR"));
							restriccion.setsRango1(rsResultado.getString("CRE_SRANGO1"));
							restriccion.setsRango2(rsResultado.getString("CRE_SRANGO2"));
							
							listaRestricciones.add(restriccion);
						}
						log.info("cantidad: " + listaRestricciones.size());
						moDataOut.put("listaRestricciones", listaRestricciones);
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

		terminaParametros("buscarRestricciones");

		return moDataOut;
	}

	public Map<String, Object> cargarRestriccion(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		log.info("carga detalle Restriccion Convenio");

		// Variables genericas para todo metodo
		iniciaParametros("cargarRestriccionConvenio");

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

		Restriccion restriccion = null;

		long idRestriccion = 0;

		idRestriccion = (Long) moDataIn.get("idRestriccion");

		log.info("ConvenioEJB -> cargarRestriccionConvenio IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en cargarRestriccionConvenio()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$CARGA_RESTRICCION.DETALLE_RESTRICCION";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idRestriccion);

			log.info("parametros de entrada------------");
			log.info("idRestriccion -> " + idRestriccion);
			log.info("--------------------------------");
		
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			creaParametroOut("CUR_RESTRICCION", OracleTypes.CURSOR);
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
					rsResultado = (ResultSet) getParametro(cStmt, "CUR_RESTRICCION");
					
					if (rsResultado != null) {

						while (rsResultado.next()) 
						{
							restriccion = new Restriccion();
							restriccion.setnIdRestriccion(rsResultado.getLong("CRE_NIDRESTRICCION"));
							restriccion.setnIdConvenio(rsResultado.getLong("CRE_NIDCONVENIO"));
							restriccion.setsNombre(rsResultado.getString("CRE_SNOMBRE"));
							restriccion.setsFhoInicio(rsResultado.getString("CRE_DFHOINICIO"));
							restriccion.setsFhoFin(rsResultado.getString("CRE_DFHOFIN"));
							restriccion.setsCodTipo(rsResultado.getString("CRE_SCODTIPO"));
							restriccion.setsDesTipo(rsResultado.getString("CRE_SDESTIPO"));
							restriccion.setsValor(rsResultado.getString("CRE_SVALOR"));
							restriccion.setsRango1(rsResultado.getString("CRE_SRANGO1"));
							restriccion.setsRango2(rsResultado.getString("CRE_SRANGO2"));
						}
						moDataOut.put("restriccion", restriccion);
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

		terminaParametros("cargarRestriccionConvenio");

		return moDataOut;
	}

	public Map<String, Object> actualizarRestriccion(Map<String, Object> moDataIn) throws ErrorSPException {
		// =================================================
		// DECLARACION DE VARIABLES
		// =================================================

		// Variables genericas para todo metodo

		iniciaParametros("actualizarRestriccion");

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
		Restriccion restriccion = (Restriccion) moDataIn.get("restriccion");

		log.info("ConvenioEJB -> actualizarRestriccion IN");

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en actualizarRestriccion()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "CON$ACTUALIZA_RESTRICCION";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, restriccion.getnIdRestriccion());
			creaParametroIn(Types.VARCHAR, restriccion.getsNombre());
			creaParametroIn(Types.VARCHAR, restriccion.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, restriccion.getsFhoFin());
			creaParametroIn(Types.VARCHAR, restriccion.getsCodTipo());
			creaParametroIn(Types.VARCHAR, restriccion.getsValor());
			creaParametroIn(Types.VARCHAR, restriccion.getsRango1());
			creaParametroIn(Types.VARCHAR, restriccion.getsRango2());
			creaParametroIn(Types.NUMERIC, restriccion.getnIdUsuarioModificacion());

			log.info("parametros de entrada restriccion------------");
			log.info("Id Restriccion -> " + restriccion.getnIdRestriccion());
			log.info("Nombre -> " + restriccion.getsNombre());
			log.info("Cod Tipo -> " + restriccion.getsCodTipo());
			log.info("Valor -> " + restriccion.getsValor());
			log.info("Rango 1 -> " + restriccion.getsRango1());
			log.info("Rango 2 -> " + restriccion.getsRango2());
			log.info("Fecha Inicio -> " + restriccion.getsFhoInicio());
			log.info("Fecha Fin -> " + restriccion.getsFhoFin());
			log.info("Actualizador -> " + restriccion.getnIdUsuarioModificacion());
			log.info("--------------------------------");
			 

			// =============================================================
			// Parametros de Salida
			// =============================================================
			/* Error */
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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

		terminaParametros("actualizarRestriccion");

		return moDataOut;
	}


	public Map<String, Object> guardarRestriccion(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("guardarRestriccion");
		log.info("Inicia parametros en: guardarRestriccion()");

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

		Restriccion agregaRestriccion = (Restriccion) moDataIn.get("restriccion");
		log.info("ConvenioEJB -> guardarRestriccion IN");
		long idRestriccion=0; 
		
		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();
			// utx.begin();

			log.info("ConvenioEJB -> conectado y listo para llamar en guardarRestriccion()");

			log.info("ConvenioEJB -> guardarRestriccion ");

			sNombreSP = "CON$AGREGA_RESTRICCION";
			sTipoSP = "TX";

			log.info("EJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================

			creaParametroIn(Types.NUMERIC, agregaRestriccion.getnIdConvenio());
			creaParametroIn(Types.VARCHAR, agregaRestriccion.getsNombre());
			creaParametroIn(Types.VARCHAR, agregaRestriccion.getsFhoInicio());
			creaParametroIn(Types.VARCHAR, agregaRestriccion.getsFhoFin());
			creaParametroIn(Types.VARCHAR, agregaRestriccion.getsCodTipo());
			creaParametroIn(Types.VARCHAR, agregaRestriccion.getsValor());
			creaParametroIn(Types.VARCHAR, agregaRestriccion.getsRango1());
			creaParametroIn(Types.VARCHAR, agregaRestriccion.getsRango2());
			creaParametroIn(Types.NUMERIC, agregaRestriccion.getnIdUsuarioCreacion());
			
			log.info("parametros de entrada guardarRestriccion------------");
			log.info("Id Convenio -> " +  agregaRestriccion.getnIdConvenio());
			log.info("Nombre -> " +  agregaRestriccion.getsNombre());
			log.info("Fecha Inicio -> " +  agregaRestriccion.getsFhoInicio());
			log.info("Fecha Fin -> " +  agregaRestriccion.getsFhoFin());
			log.info("Cod Tipo -> " +  agregaRestriccion.getsCodTipo());
			log.info("Valor -> " +  agregaRestriccion.getsValor());
			log.info("Rango 1 -> " +  agregaRestriccion.getsRango1());
			log.info("Rango 2 -> " +  agregaRestriccion.getsRango2());
			log.info("Id Usuario -> " +  agregaRestriccion.getnIdUsuarioCreacion());
			log.info("--------------------------------");
			 
			// =============================================================
			// Parametros de Salida
			// =============================================================
			
			/* Error */
			//creaParametroOut("idRestriccion", Types.INTEGER);
			creaParametroOut("NumError", Types.INTEGER);
			creaParametroOut("MsjError", Types.VARCHAR);

			try {

				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
				cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
				log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

			} catch (Exception e) {
				log.error(e);
				error = new Error(4, 23456, e.getMessage(), Errores.getInstance().getMsgFamilia("4"));
				throw new ErrorSPException(4, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}

			if (cStmt != null) {
				log.info("Cursor != nulo");

				cStmt.execute();

				// Recuperacion parametros de salida
//						idRestriccion = (Integer) getParametro(cStmt, "idRestriccion");
//						log.info("idRestriccion: " + idRestriccion);
				
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
			log.error("ConvenioEJB -> SQLException: " + e);
			e.printStackTrace();
			error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
			throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			// utx.rollback();
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
		//moDataOut.put("idRestriccion", idRestriccion);
		moDataOut.put("error", error);

		terminaParametros("guardarRestriccion");

		return moDataOut;
	}

	public Map<String, Object> eliminarRestriccion(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("eliminarRestriccion");

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
		
		long idRestriccion = 0;
		idRestriccion = (Long) moDataIn.get("idRestriccion");
		
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

			log.info("ConvenioEJB -> eliminarRestriccion IN");

			try {
				dbConeccion = solemDS.getConnection();
				//utx.begin();

				log.info("ConvenioEJB -> conectado y listo para llamar en eliminarRestriccion()");

				log.info("ConvenioEJB -> LOGUEO ");
				sNombreSP = "CON$ELIMINA_RESTRICCION";
				sTipoSP = "TX";

				log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				log.info("id Restriccion a eliminar: " + idRestriccion);

				creaParametroIn(Types.NUMERIC, idRestriccion);

				// =============================================================
				// Parametros de Salida
				// =============================================================
				/* Error */
				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("ConvenioEJB -> " + sNombreSP + " cStmt Fin");

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
				log.error("ConvenioEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				try {
					log.info("Finally ConvenioEJB -> " + sNombreSP + " Fin");
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

		terminaParametros("eliminarRestriccion");

		return moDataOut;
	}

	public Map<String, Object> validarCondicionModificadorPrestacionConvenio(Map<String, Object> moDataIn) throws ErrorSPException {
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

		log.info("ConvenioEJB -> validarCondicion IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarRestricciones()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "TYC$VALIDA_CONDICION";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

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

		terminaParametros("validarCondicion");

		return moDataOut;
	}
	
	public Map<String, Object> validarFormulaModifPrestConv(Map<String, Object> moDataIn) throws ErrorSPException {
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

		log.info("ConvenioEJB -> validarFormula IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarRestricciones()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = "TYC$VALIDA_FORMULA";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

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

		long idConvenio = 0;

		idConvenio = (Long) moDataIn.get("idConvenio");

		log.info("ConvenioEJB -> buscarUniOrg IN");

		// =============================================================
		// CUERPO DE EJB
		// =============================================================

		try {
			dbConeccion = solemDS.getConnection();

			log.info("ConvenioEJB -> conectado y listo para llamar en buscarUniOrg()");

			log.info("ConvenioEJB -> LOGUEO ");
			sNombreSP = " CON$BUSCA_UNI_ORG.LISTA_UNIDADORG";
			sTipoSP = "TX";

			log.info("ConvenioEJB -> " + sNombreSP + " Inicio");

			// =============================================================
			// Parametros de entrada
			// =============================================================
			
			creaParametroIn(Types.NUMERIC, idConvenio);

			log.info("parametros de entrada------------");
			log.info("idConvenio -> " + idConvenio);
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

		terminaParametros("buscarUniOrg");

		return moDataOut;
	}	
}
	 