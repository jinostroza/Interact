package cl.solem.tyc.servlets;

import java.io.IOException;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.solem.jee.libsolem.exception.ErrorSPException;
import cl.solem.jee.libsolem.util.Errores;
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.Prestacion;
import cl.solem.tyc.beans.ServletParametros;
import cl.solem.tyc.beans.Sesion;
import cl.solem.tyc.ejb.interfaces.ParametroEJBRemote;
import cl.solem.tyc.ejb.interfaces.PrestacionEJBRemote;

public class SvtPrestaciones extends ServletHandler {

	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(SvtPrestaciones.class);

	@EJB(mappedName = "tyc/PrestacionEJB")
	private PrestacionEJBRemote ejbPrestacion;
	private static String separadorLog = "----------------------------------------------------------------------------------------------------------";

	@EJB(mappedName = "tyc/ParametroEJB")
	private ParametroEJBRemote ejbParametro;

	public SvtPrestaciones() {
		super();
	}

	public void despacharPrestaciones(HttpServletRequest request, HttpServletResponse response) {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Rescatando Tipos de Prestacion: ");
			moBeansIn.put("nPar_numparametro", (long) 37);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("error", error);
			request.setAttribute("listaTipoPrestacion", moBeansOut.get("listaParametros"));
			moBeansIn.clear();
			moBeansOut.clear();
			log.info(separadorLog);
			log.info("[Rescatando Estado de una Prestacion: ");
			moBeansIn.put("nPar_numparametro", (long) 78);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("error", error);

			request.setAttribute("listaEstadoPrestacion", moBeansOut.get("listaParametros"));

			moBeansIn.clear();
			moBeansOut.clear();

			log.info(separadorLog);

			log.info("[Método: " + nombreMetodo + "] Iniciando");
			pagDestino = "contenedores/contenedorPrestaciones.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/includes/errorXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarPrestaciones(HttpServletRequest request, HttpServletResponse response) {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();
		int pagActual = 0;
		int totalRegistros = 0;
		int regPorPagina = 0;
		double totalPaginas = 0;
		String sOrdenarPor = "";
		String sTipoOrden = "";

		try {
			log.info("[Método: " + nombreMetodo + "] Iniciando");

			regPorPagina = Integer.parseInt((String) request.getParameter("rows"));
			pagActual = Integer.parseInt((String) request.getParameter("page"));
			sOrdenarPor = (String) request.getParameter("sidx");
			sTipoOrden = (String) request.getParameter("sord");

			moBeansIn = procesaForm(request);
			moBeansIn.put("sOrdenarPor", sOrdenarPor);
			moBeansIn.put("sTipoOrden", sTipoOrden);
			moBeansIn.put("pagActual", pagActual);
			moBeansIn.put("regPorPagina", regPorPagina);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbPrestacion.buscarPrestaciones(moBeansIn);
			request.setAttribute("listaPrestacion", moBeansOut.get("listaPrestaciones"));
			error = (Error) moBeansOut.get("error");

			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);

			request.setAttribute("error", error);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			pagDestino = "prestaciones/listaPrestacionesXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "prestaciones/listaPrestacionesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "prestaciones/listaPrestacionesXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void cargarPrestacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			long nIdPrestacion = 0;
			log.info("[Método: " + nombreMetodo + "] Iniciando");

			if (request.getAttribute("idPrestacion") != null) {
				log.info("idPrestacion: " + request.getAttribute("idPrestacion"));
				nIdPrestacion = (Long) request.getAttribute("idPrestacion");
			} else
				nIdPrestacion = Long.parseLong(request.getParameter("nIdPrestacion"));

			log.info("Busca Lista de Estados Prestacion");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 78);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaEstadosPrestacion", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Tipos de Prestacion");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 37);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTipos", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Sub Tipos de Prestacion");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 38);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaSubTipos", moBeansOut.get("listaParametros"));

			log.info("Busca Unidad de Medida");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 15);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("unidadMedida", moBeansOut.get("listaParametros"));

			moBeansIn.put("sesion", sesion);
			log.info("RUTA SESION en carga: " + sesion.getSesRutaSistema());
			log.info("ID Prestacion: " + nIdPrestacion);
			moBeansIn.put("nIdPrestacion", nIdPrestacion);
			moBeansOut = ejbPrestacion.cargarPrestacion(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", moBeansOut.get("error"));
			Prestacion prestacion = (Prestacion) moBeansOut.get("prestacion");
			request.setAttribute("prestacion", prestacion);
			request.setAttribute("error", error);
			pagDestino = "contenedores/contenedorPrestaciones.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/error.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/error.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarPrestacionesCompuestas(HttpServletRequest request, HttpServletResponse response) {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();
		int pagActual = 0;
		int totalRegistros = 0;
		int regPorPagina = 0;
		double totalPaginas = 0;
		String sOrdenarPor = "";
		String sTipoOrden = "";

		try {
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			long nIdPrestacion = Long.parseLong(request.getParameter("nIdPrestacion"));
			regPorPagina = Integer.parseInt((String) request.getParameter("rows"));
			pagActual = Integer.parseInt((String) request.getParameter("page"));
			sOrdenarPor = (String) request.getParameter("sidx");
			sTipoOrden = (String) request.getParameter("sord");
			moBeansIn.put("sesion", sesion);
			log.info("ID Prestacion: " + nIdPrestacion);
			moBeansIn.put("nIdPrestacion", nIdPrestacion);
			moBeansIn.put("sOrdenarPor", sOrdenarPor);
			moBeansIn.put("sTipoOrden", sTipoOrden);
			moBeansIn.put("pagActual", pagActual);
			moBeansIn.put("regPorPagina", regPorPagina);
			moBeansOut = ejbPrestacion.buscarPrestacionesCompuestas(moBeansIn);
			request.setAttribute("listaPrestacion", moBeansOut.get("listaPrestaciones"));
			error = (Error) moBeansOut.get("error");
			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			request.setAttribute("error", error);
			pagDestino = "prestaciones/compuestas/listaPrestacionesCompuestasXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "prestaciones/compuestas/listaPrestacionesCompuestasXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "prestaciones/compuestas/listaPrestacionesCompuestasXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void eliminarPrestacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			String codPrestacion = request.getParameter("codPrestacion");
			long nIdPrestacion = (long) Long.parseLong(request.getParameter("nIdPrestacion"));
			moBeansIn.put("sesion", sesion);
			log.info("Id de la prestacion: " + nIdPrestacion);
			log.info("Codigo de la prestacion: " + codPrestacion);
			moBeansIn.put("codPrestacion", codPrestacion);
			moBeansIn.put("nIdPrestacion", nIdPrestacion);
			moBeansOut = ejbPrestacion.eliminarPrestacion(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", moBeansOut.get("error"));
			request.setAttribute("sesion", sesion);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void eliminarPrestacionCompuesta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			long nIdPrestacion = (long) Long.parseLong(request.getParameter("nIdPrestacion"));
			moBeansIn.put("sesion", sesion);
			log.info("Id de la prestacion: " + nIdPrestacion);
			moBeansIn.put("nIdPrestacion", nIdPrestacion);
			String lista = request.getParameter("aIds");
			moBeansIn.put("lista", lista);
			log.info(" Lista de Prestaciones --> " + lista);
			moBeansOut = ejbPrestacion.eliminarPrestacionCompuesta(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", moBeansOut.get("error"));
			request.setAttribute("sesion", sesion);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void agregarPrestacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Metodo: " + nombreMetodo + "] Iniciando");

			log.info("Busca Lista de Tipos de Prestacion");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 37);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTipos", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Sub Tipos de Prestacion");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 38);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaSubTipos", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Estados Prestacion");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 78);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaEstadosPrestacion", moBeansOut.get("listaParametros"));

			log.info("Busca Unidad de Medida");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 15);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("unidadMedida", moBeansOut.get("listaParametros"));

			pagDestino = "contenedores/contenedorPrestaciones.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void guardarPrestacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Metodo: " + nombreMetodo + "] Iniciando");
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbPrestacion.guardarPrestacion(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			request.setAttribute("sesion", sesion);
			long nidPrestacion = (Long) moBeansOut.get("idPrestacion");
			log.info("idPrestacion: " + nidPrestacion);
			request.setAttribute("idPrestacion", nidPrestacion);

			/* codigo necesario para generar la nueva url del carga */
			String url = request.getRequestURL().toString();
			String urlCarga = url + "?KSI=" + sesion.getSesIdSesion() + "&accion=cargarPrestacion&nIdPrestacion=" + nidPrestacion + "&indRuta=S";
			request.setAttribute("urlCarga", urlCarga);
			verificaUsuario(request);
			servletParametros.setSesion(((Sesion) request.getAttribute("sesion")));
			/* fin codigo */

			cargarPrestacion(request, response);
			pagDestino = "/contenedores/contenedorPrestaciones.jsp";
			//pagDestino = "general/includes/respuestaXml.jsp";
			log.info("[Metodo: " + nombreMetodo + "] Despachando a " + pagDestino);
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void actualizarPrestacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();
		
		try {
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbPrestacion.actualizarPrestacion(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			pagDestino = "general/includes/respuestaXml.jsp";
			log.info("[Metodo: " + nombreMetodo + "] Despachando a: " + pagDestino);
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarPrestacionesNomencladorNacional(HttpServletRequest request, HttpServletResponse response) {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();
		int pagActual = 0;
		int totalRegistros = 0;
		int regPorPagina = 0;
		double totalPaginas = 0;
		String sOrdenarPor = "";
		String sTipoOrden = "";

		try {
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			long nIdPrestacion = Long.parseLong(request.getParameter("nIdPrestacion"));
			log.info("ID Prestacion: " + nIdPrestacion);
			regPorPagina = Integer.parseInt((String) request.getParameter("rows"));
			pagActual = Integer.parseInt((String) request.getParameter("page"));
			sOrdenarPor = (String) request.getParameter("sidx");
			sTipoOrden = (String) request.getParameter("sord");
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nIdPrestacion", nIdPrestacion);
			moBeansIn.put("sOrdenarPor", sOrdenarPor);
			moBeansIn.put("sTipoOrden", sTipoOrden);
			moBeansIn.put("pagActual", pagActual);
			moBeansIn.put("regPorPagina", regPorPagina);
			moBeansOut = ejbPrestacion.buscarPrestacionesNomencladorNacional(moBeansIn);
			request.setAttribute("listaPrestaciones", moBeansOut.get("listaPrestaciones"));
			error = (Error) moBeansOut.get("error");
			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			request.setAttribute("error", error);
			pagDestino = "prestaciones/prestacion_nacional/listaPrestacionNacionalXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "prestaciones/prestacion_nacional/listaPrestacionNacionalXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "prestaciones/prestacion_nacional/listaPrestacionNacionalXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void cargarPopUpCompuesta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Metodo: " + nombreMetodo + "] Iniciando");
			request.setAttribute("nIdPrestacion", request.getParameter("nIdPrestacion"));
			pagDestino = "/prestaciones/popups/listaCompuestas.jsp";
		} catch (Exception e) {
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarCompuestas(HttpServletRequest request, HttpServletResponse response) {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();
		int pagActual = 0;
		int totalRegistros = 0;
		int regPorPagina = 0;
		double totalPaginas = 0;
		String sOrdenarPor = "";
		String sTipoOrden = "";

		try {
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			long nIdPrestacion = Long.parseLong(request.getParameter("nIdPrestacion"));
			log.info("ID Prestacion: " + nIdPrestacion);
			regPorPagina = Integer.parseInt((String) request.getParameter("rows"));
			pagActual = Integer.parseInt((String) request.getParameter("page"));
			sOrdenarPor = (String) request.getParameter("sidx");
			sTipoOrden = (String) request.getParameter("sord");
			moBeansIn = procesaForm(request);
			moBeansIn.put("nIdPrestacion", nIdPrestacion);
			moBeansIn.put("sOrdenarPor", sOrdenarPor);
			moBeansIn.put("sTipoOrden", sTipoOrden);
			moBeansIn.put("pagActual", pagActual);
			moBeansIn.put("regPorPagina", regPorPagina);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbPrestacion.buscarCompuestas(moBeansIn);
			request.setAttribute("listaPrestacion", moBeansOut.get("listaPrestaciones"));
			error = (Error) moBeansOut.get("error");

			error = new Error("0", "");

			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);

			request.setAttribute("error", error);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			pagDestino = "prestaciones/popups/listaCompuestasXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "prestaciones/popups/listaCompuestasXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "prestaciones/popups/listaCompuestasXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void agregarCompuesta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Metodo: " + nombreMetodo + "] Iniciando");
			long idPrestacion = (long) Long.parseLong(request.getParameter("nIdPrestacion"));
			moBeansIn.put("sesion", sesion);
			log.info("Id de la prestacion: " + idPrestacion);
			moBeansIn.put("idPrestacion", idPrestacion);
			String lista = request.getParameter("aIds");
			moBeansIn.put("lista", lista);
			log.info(" Lista de Prestaciones --> " + lista);
			String cantidad = request.getParameter("cantidad");
			moBeansIn.put("cantidad", cantidad);
			log.info(" Lista de cantidad--> " + cantidad);
			moBeansOut = ejbPrestacion.agregarCompuesta(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			request.setAttribute("sesion", sesion);

		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void cargarPrestacionNacional(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Metodo: " + nombreMetodo + "] Iniciando");
			request.setAttribute("nIdPrestacion", request.getParameter("nIdPrestacion"));
			pagDestino = "/prestaciones/popups/listaPrestacionNomenclador.jsp";
		} catch (Exception e) {
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarNomencladores(HttpServletRequest request, HttpServletResponse response) {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();
		int pagActual = 0;
		int totalRegistros = 0;
		int regPorPagina = 0;
		double totalPaginas = 0;
		String sOrdenarPor = "";
		String sTipoOrden = "";

		try {
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			long nIdPrestacion = Long.parseLong(request.getParameter("nIdPrestacion"));
			log.info("ID Prestacion: " + nIdPrestacion);
			regPorPagina = Integer.parseInt((String) request.getParameter("rows"));
			pagActual = Integer.parseInt((String) request.getParameter("page"));
			sOrdenarPor = (String) request.getParameter("sidx");
			sTipoOrden = (String) request.getParameter("sord");
			moBeansIn = procesaForm(request);
			moBeansIn.put("nIdPrestacion", nIdPrestacion);
			moBeansIn.put("sOrdenarPor", sOrdenarPor);
			moBeansIn.put("sTipoOrden", sTipoOrden);
			moBeansIn.put("pagActual", pagActual);
			moBeansIn.put("regPorPagina", regPorPagina);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbPrestacion.buscarNomencladores(moBeansIn);
			request.setAttribute("listaNomencladores", moBeansOut.get("listaNomencladores"));
			error = (Error) moBeansOut.get("error");

			error = new Error("0", "");

			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);

			request.setAttribute("error", error);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			pagDestino = "prestaciones/popups/listaPrestacionNomencladorXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "prestaciones/popups/listaPrestacionNomencladorXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "prestaciones/popups/listaPrestacionNomencladorXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void eliminarPrestacionNacional(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			long nIdPrestacion = Long.parseLong(request.getParameter("nIdPrestacion"));
			moBeansIn.put("sesion", sesion);
			log.info("Id de la prestacion: " + nIdPrestacion);
			moBeansIn.put("nIdPrestacion", nIdPrestacion);
			String lista = request.getParameter("aIds");
			moBeansIn.put("lista", lista);
			log.info(" Lista de Prestaciones --> " + lista);
			String tablas = request.getParameter("tablas");
			moBeansIn.put("tablas", tablas);
			log.info(" Lista de Identificadores de Tablas Nomenclador --> " + tablas);
			moBeansOut = ejbPrestacion.eliminarPrestacionNacional(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", moBeansOut.get("error"));
			request.setAttribute("sesion", sesion);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void agregarPrestNomec(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Metodo: " + nombreMetodo + "] Iniciando");
			long idPrestacion = (long) Long.parseLong(request.getParameter("nIdPrestacion"));
			moBeansIn.put("sesion", sesion);
			log.info("Id de la prestacion: " + idPrestacion);
			moBeansIn.put("idPrestacion", idPrestacion);
			String lista = request.getParameter("aIds");
			moBeansIn.put("lista", lista);
			log.info(" Lista de Prestaciones Nomenclador--> " + lista);
			String cantidad = request.getParameter("cantidad");
			moBeansIn.put("cantidad", cantidad);
			log.info(" Lista de cantidad--> " + cantidad);
			String tabla = request.getParameter("tabla");
			moBeansIn.put("tabla", tabla);
			log.info(" Lista de Identificadores de Tablas Nomenclador --> " + tabla);
			moBeansOut = ejbPrestacion.agregarPrestNomec(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			request.setAttribute("sesion", sesion);

		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void actualizarPrestacionNomenclador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("[Metodo: " + nombreMetodo + "] Iniciando");
			log.info("Cantidad: " + request.getParameter("cantidad"));
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "general/includes/respuestaXml.jsp";
		}
	}
}