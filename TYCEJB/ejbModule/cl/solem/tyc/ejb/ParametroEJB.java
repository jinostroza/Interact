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
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.Parametro;
import cl.solem.tyc.beans.Sesion;
import cl.solem.tyc.ejb.interfaces.ParametroEJBRemote;
import cl.solem.tyc.ejb.interfaces.SesionEJBRemote;

@Stateless(mappedName = "tyc/ParametroEJB")
public class ParametroEJB extends EjbBase implements ParametroEJBRemote{
	
	private Logger log = Logger.getLogger(ParametroEJB.class);
	@Resource(mappedName = "java:/SolemBilling")
	protected DataSource solemDS;
	
	private Error error = null;
	private ErrorSPException errorSP = null;
	private Seguridad seg = null;
	
	@EJB(mappedName = "tyc/SesionEJB")
	private SesionEJBRemote ejbSesion;

	public ParametroEJB() {
		super();
		setSolemDataSource(solemDS);
	}


	public Map<String, Object> buscaParametros(Map<String, Object> moDataIn) throws ErrorSPException {
		// =============================================================
		// DECLARACION DE VARIABLES
		// =============================================================

		// Variables genericas para todo metodo

		iniciaParametros("BuscaParametros");
		log.info("iniciaParametros en buscaParametros");

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

		HashMap<String, Object> mapaSalida = null;

		mapaSalida = new HashMap<String, Object>();

		ArrayList<Parametro> listaParametros = null;

		Long nPar_numparametro;
		String sPar_codparametro01;
		String sPar_codparametro02;
		String sPar_codparametro03;
		String sPar_codparametro04;
		String sPar_codparametro05;

		nPar_numparametro = (Long) moDataIn.get("nPar_numparametro");
		sPar_codparametro01 = (String) moDataIn.get("sPar_codparametro01");
		sPar_codparametro02 = (String) moDataIn.get("sPar_codparametro02");
		sPar_codparametro03 = (String) moDataIn.get("sPar_codparametro03");
		sPar_codparametro04 = (String) moDataIn.get("sPar_codparametro04");
		sPar_codparametro05 = (String) moDataIn.get("sPar_codparametro05");

		// =============================================================
		// VERIFICACION SESION
		// =============================================================

		/*sesion = (Sesion) moDataIn.get("sesion");

		log.info("sesion-----------------------------> " + sesion.getSesNombreCompleto());

		moBeansIn.clear();

		moBeansIn.put("sesion", sesion);

		moBeansOut = ejbSesion.buscaPerfiles(moBeansIn);

		error = (Error) moBeansOut.get("error");

		if (!error.getNumError().equalsIgnoreCase("0")) {
			throw new ErrorSPException(error.getNumError(), error.getNumError(), error.getMsjError());
		}
		perfiles = (HashMap<String, String>) moBeansOut.get("aPerfiles");

		autorizacion = perfiles.containsKey("10000") && perfiles.containsKey("11000");

		if (autorizacion) {
			log.info("el perfil esta contenido: " + autorizacion);

			log.info("FuncionesEJB -> BuscaParametros IN");*/
			// =============================================================
			// CUERPO DE EJB
			// =============================================================

			try {
				dbConeccion = solemDS.getConnection();

				log.info("FuncionesEJB -> conectado y listo para llamar en BuscaParametros()");

				log.info("FuncionesEJB -> LOGUEO ");
				sNombreSP = "BIL$BUSCA_PARAMETROS.BUSCA_PARAMETROS";
				sTipoSP = "TX";

				log.info("FuncionesEJB -> " + sNombreSP + " Inicio");

				// =============================================================
				// Parametros de entrada
				// =============================================================

				creaParametroIn(Types.NUMERIC, nPar_numparametro);
				creaParametroIn(Types.VARCHAR, sPar_codparametro01);
				creaParametroIn(Types.VARCHAR, sPar_codparametro02);
				creaParametroIn(Types.VARCHAR, sPar_codparametro03);
				creaParametroIn(Types.VARCHAR, sPar_codparametro04);
				creaParametroIn(Types.VARCHAR, sPar_codparametro05);

				log.info("parametros----------------------");
				log.info("NumParametro -> " + nPar_numparametro);
				log.info("CodParametro01 -> " + sPar_codparametro01);
				log.info("CodParametro02 -> " + sPar_codparametro02);
				log.info("CodParametro03 -> " + sPar_codparametro03);
				log.info("CodParametro04 -> " + sPar_codparametro04);
				log.info("CodParametro05 -> " + sPar_codparametro05);
				log.info("--------------------------------");

				// =============================================================
				// Parametros de Salida
				// =============================================================

				creaParametroOut("CUR_PARAMETROS", OracleTypes.CURSOR);

				creaParametroOut("NumError", Types.INTEGER);
				creaParametroOut("MsjError", Types.VARCHAR);

				try {

					log.info("FuncionesEJB -> " + sNombreSP + " cStmt Inicio");
					cStmt = creaLlamadaSP(sNombreSP, sTipoSP);
					log.info("FuncionesEJB -> " + sNombreSP + " cStmt Fin");

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

						log.info("FuncionesEJB -> " + sNombreSP + " rsResultado Inicio");
						rsResultado = (ResultSet) getParametro(cStmt, "CUR_PARAMETROS");
						log.info("FuncionesEJB -> " + sNombreSP + " rsResultado Fin");

						if (rsResultado != null) {
							log.info("rsResultado != nulo");

							listaParametros = new ArrayList<Parametro>();

							while (rsResultado.next()) {

								Parametro parametro = new Parametro();
								parametro.setParNumParametro(rsResultado.getLong("PAR_NUMPARAMETRO"));
								parametro.setParCodParametro01(rsResultado.getString("PAR_CODPARAMETRO01"));
								parametro.setParCodParametro02(rsResultado.getString("PAR_CODPARAMETRO02"));
								parametro.setParCodParametro03(rsResultado.getString("PAR_CODPARAMETRO03"));
								parametro.setParCodParametro04(rsResultado.getString("PAR_CODPARAMETRO04"));
								parametro.setParCodParametro05(rsResultado.getString("PAR_CODPARAMETRO05"));
								parametro.setParDesLargo01(rsResultado.getString("PAR_DESLARGO01"));
								parametro.setParDesLargo02(rsResultado.getString("PAR_DESLARGO02"));
								parametro.setParDesLargo03(rsResultado.getString("PAR_DESLARGO03"));
								parametro.setParDesLargo04(rsResultado.getString("PAR_DESLARGO04"));
								parametro.setParDesLargo05(rsResultado.getString("PAR_DESLARGO05"));
								parametro.setParNumero01(rsResultado.getLong("PAR_NUMERO01"));
								parametro.setParNumero02(rsResultado.getLong("PAR_NUMERO02"));
								parametro.setParNumero03(rsResultado.getLong("PAR_NUMERO03"));
								parametro.setParNumero04(rsResultado.getLong("PAR_NUMERO04"));
								parametro.setParNumero05(rsResultado.getLong("PAR_NUMERO05"));
								parametro.setParValor01(rsResultado.getLong("PAR_VALOR01"));
								parametro.setParValor02(rsResultado.getLong("PAR_VALOR02"));
								parametro.setParValor03(rsResultado.getLong("PAR_VALOR03"));
								parametro.setParValor04(rsResultado.getLong("PAR_VALOR04"));
								parametro.setParValor05(rsResultado.getLong("PAR_VALOR05"));
								parametro.setParCodigo01(rsResultado.getString("PAR_CODIGO01"));
								parametro.setParCodigo02(rsResultado.getString("PAR_CODIGO02"));
								parametro.setParCodigo03(rsResultado.getString("PAR_CODIGO03"));
								parametro.setParCodigo04(rsResultado.getString("PAR_CODIGO04"));
								parametro.setParCodigo05(rsResultado.getString("PAR_CODIGO05"));
								parametro.setParFecha01(rsResultado.getString("PAR_FECHAHORA01"));
								parametro.setParFecha02(rsResultado.getString("PAR_FECHAHORA02"));
								parametro.setParFecha03(rsResultado.getString("PAR_FECHAHORA03"));
								parametro.setParFecha04(rsResultado.getString("PAR_FECHAHORA04"));
								parametro.setParFecha05(rsResultado.getString("PAR_FECHAHORA05"));
								parametro.setParIndicador01(rsResultado.getString("PAR_INDICADOR01"));
								parametro.setParIndicador02(rsResultado.getString("PAR_INDICADOR02"));
								parametro.setParIndicador03(rsResultado.getString("PAR_INDICADOR03"));
								parametro.setParIndicador04(rsResultado.getString("PAR_INDICADOR04"));
								parametro.setParIndicador05(rsResultado.getString("PAR_INDICADOR05"));
								parametro.setParDescorto01(rsResultado.getString("PAR_DESCORTO01"));
								parametro.setParDescorto02(rsResultado.getString("PAR_DESCORTO02"));
								parametro.setParDescorto03(rsResultado.getString("PAR_DESCORTO03"));
								parametro.setParDescorto04(rsResultado.getString("PAR_DESCORTO04"));
								parametro.setParDescorto05(rsResultado.getString("PAR_DESCORTO05"));
								parametro.setParIndactivo(rsResultado.getString("PAR_INDACTIVO"));
								parametro.setParFhoactivo(rsResultado.getString("PAR_FHOACTIVO"));

								listaParametros.add(parametro);

							}
							log.info("cantidad: " + listaParametros.size());
							mapaSalida.put("listaParametros", listaParametros);
							rsResultado.close();
						}

					}
				}

			} catch (SQLException e) {
				log.error("FuncionesEJB -> SQLException: " + e);
				e.printStackTrace();
				error = new Error(2, String.valueOf(e.getErrorCode()), e.getMessage(), Errores.getInstance().getMsgFamilia("2"));
				throw new ErrorSPException(2, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			} finally {

				log.info("FuncionesEJB -> " + sNombreSP + " Fin");

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

		//} else {
		//	error = new Error(4, 99, "Usuario NO puede acceder metodo en EJB", Errores.getInstance().getMsgFamilia("4"));
		//	throw new ErrorSPException(Long.valueOf(error.getNumFamiliaError()), Long.valueOf(error.getNumError()), error.getMsjError(),
		//			error.getMsjErrorUsuario());
		//}

		mapaSalida.put("error", error);

		terminaParametros("BuscaParametros");

		return mapaSalida;
	}

}
