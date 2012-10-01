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
import cl.solem.tyc.beans.Anexo;
import cl.solem.tyc.beans.Convenio;
import cl.solem.tyc.beans.ConvenioAnexoDetalle;
import cl.solem.tyc.beans.Empresa;
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.Modificador;
import cl.solem.tyc.beans.Precio;
import cl.solem.tyc.beans.Restriccion;
import cl.solem.tyc.beans.ServletParametros;
import cl.solem.tyc.beans.Sesion;
import cl.solem.tyc.ejb.interfaces.ConvenioEJBRemote;
import cl.solem.tyc.ejb.interfaces.ParametroEJBRemote;

public class SvtConvenios extends ServletHandler {
	private static final long serialVersionUID = 1L;

	private Logger log = Logger.getLogger(SvtConvenios.class);

	@EJB(mappedName = "tyc/ConvenioEJB")
	private ConvenioEJBRemote ejbConvenio;

	@EJB(mappedName = "tyc/ParametroEJB")
	private ParametroEJBRemote ejbParametro;

	public SvtConvenios() {
		super();
	}

	public void despacharConvenios(HttpServletRequest request, HttpServletResponse response) {
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

			log.info("Busca Lista de Estados Convenio");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 67);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaEstadosConvenio", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Clases");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 73);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaClases", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Tipos");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 74);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTipos", moBeansOut.get("listaParametros"));

			pagDestino = "contenedores/contenedorConvenios.jsp";
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

	public void buscarConvenios(HttpServletRequest request, HttpServletResponse response) {
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
			regPorPagina = Integer.parseInt((String) request.getParameter("rows"));
			pagActual = Integer.parseInt((String) request.getParameter("page"));
			sOrdenarPor = (String) request.getParameter("sidx");
			sTipoOrden = (String) request.getParameter("sord");

			log.info("[Método: " + nombreMetodo + "] Iniciando");
			String codConvenio = request.getParameter("convenio.sCodConvenio");
			log.info("Busca Lista de Estados Convenio");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 67);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaEstadosConvenio", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Clases");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 73);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaClases", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Tipos");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 74);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTipos", moBeansOut.get("listaParametros"));

			moBeansIn.clear();
			moBeansOut.clear();

			moBeansIn = procesaForm(request);
			moBeansIn.put("sOrdenarPor", sOrdenarPor);
			moBeansIn.put("sTipoOrden", sTipoOrden);
			moBeansIn.put("pagActual", pagActual);
			moBeansIn.put("regPorPagina", regPorPagina);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbConvenio.buscarConvenios(moBeansIn);
			request.setAttribute("listaConvenios", moBeansOut.get("listaConvenios"));
			error = (Error) moBeansOut.get("error");

			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);

			request.setAttribute("error", error);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			pagDestino = "convenios/listaConveniosXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/listaConveniosXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/listaConveniosXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void cargarConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			long idConvenio = 0;

			if (request.getAttribute("idConvenio") != null)
				idConvenio = (Long) request.getAttribute("idConvenio");
			else
				idConvenio = Long.parseLong((String) request.getParameter("idConvenio"));

			log.info("idConvenio: " + idConvenio);

			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idConvenio", idConvenio);
			moBeansOut = ejbConvenio.cargarConvenio(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			Convenio convenio = (Convenio) moBeansOut.get("convenio");
			request.setAttribute("convenio", convenio);

			log.info("Busca Lista de Estados Convenio");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 67);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaEstadosConvenio", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Clases");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 73);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaClases", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Tipos");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 74);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTipos", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de SubClases");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 70);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaSubClases", moBeansOut.get("listaParametros"));

			pagDestino = "contenedores/contenedorConvenios.jsp";
		} catch (ErrorSPException e) {
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

	public void actualizarConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbConvenio.actualizarConvenio(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
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

	public void agregarConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			log.info("Busca Lista de Estados Convenio");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 67);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaEstadosConvenio", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Clases");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 73);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaClases", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Tipos");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 74);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTipos", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de SubClases");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 70);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaSubClases", moBeansOut.get("listaParametros"));

			pagDestino = "contenedores/contenedorConvenios.jsp";
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

	public void guardarConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			System.out.println("sesion: " + sesion.getSesNombre());
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbConvenio.guardarConvenio(moBeansIn);
			long idConvenio = (Long) moBeansOut.get("idConvenio");
			request.setAttribute("idConvenio", idConvenio);

			/* codigo necesario para generar la nueva url del carga */
			String url = request.getRequestURL().toString();
			String urlCarga = url + "?KSI=" + sesion.getSesIdSesion() + "&accion=cargarConvenio&idConvenio=" + idConvenio + "&indRuta=S";
			request.setAttribute("urlCarga", urlCarga);
			verificaUsuario(request);
			servletParametros.setSesion(((Sesion) request.getAttribute("sesion")));
			/* fin codigo */

			cargarConvenio(request, response);
			pagDestino = "/contenedores/contenedorConvenios.jsp";
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

	public void eliminarConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idConvenio", Long.parseLong(request.getParameter("idConvenio")));
			moBeansOut = ejbConvenio.eliminarConvenio(moBeansIn);
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

	public void buscarAnexosConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idConvenio", (long) Long.parseLong(request.getParameter("idConvenio")));
			moBeansOut = ejbConvenio.buscarAnexosConvenio(moBeansIn);
			request.setAttribute("listaAnexos", moBeansOut.get("listaAnexos"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "convenios/anexos/listaAnexosXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/anexos/listaAnexosXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/anexos/listaAnexosXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void cargarAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			long idAnexo = 0;

			if (request.getAttribute("idAnexo") != null)
				idAnexo = (Long) request.getAttribute("idAnexo");
			else
				idAnexo = Long.parseLong((String) request.getParameter("idAnexo"));

			log.info("idAnexo: " + idAnexo);

			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idAnexo", idAnexo);
			moBeansOut = ejbConvenio.cargarAnexo(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			Anexo anexo = (Anexo) moBeansOut.get("anexo");
			request.setAttribute("anexo", anexo);

			log.info("Busca Lista de Estados Anexo");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 80);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaEstadosAnexo", moBeansOut.get("listaParametros"));

			pagDestino = "contenedores/contenedorAnexos.jsp";
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

	public void agregarAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			log.info("Busca Lista de Estados Anexo");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 80);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaEstadosAnexo", moBeansOut.get("listaParametros"));

			long idConvenio = Long.parseLong(request.getParameter("idConvenio"));
			request.setAttribute("idConvenio", idConvenio);
			// pagDestino = "/convenios/anexos/agregaAnexo.jsp";
			pagDestino = "contenedores/contenedorAnexos.jsp";
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

	public void guardarAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			System.out.println("sesion: " + sesion.getSesNombre());
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbConvenio.guardarAnexo(moBeansIn);
			error = (Error) moBeansOut.get("error");
			long idAnexo = (Long) moBeansOut.get("idAnexo");
			request.setAttribute("error", error);
			request.setAttribute("sesion", sesion);
			request.setAttribute("idAnexo", idAnexo);

			/* codigo necesario para generar la nueva url del carga */
			String url = request.getRequestURL().toString();
			String urlCarga = url + "?KSI=" + sesion.getSesIdSesion() + "&accion=cargarAnexo&idAnexo=" + idAnexo + "&indRuta=S";
			request.setAttribute("urlCarga", urlCarga);
			verificaUsuario(request);
			servletParametros.setSesion(((Sesion) request.getAttribute("sesion")));
			/* fin codigo */

			cargarAnexo(request, response);
			pagDestino = "/contenedores/contenedorAnexos.jsp";
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

	public void actualizarAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbConvenio.actualizarAnexo(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
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

	public void eliminarAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idAnexo", Long.parseLong(request.getParameter("idAnexo")));
			moBeansOut = ejbConvenio.eliminarAnexo(moBeansIn);
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

	public void buscarRestriccionesConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idConvenio", (long) Long.parseLong(request.getParameter("idConvenio")));
			moBeansOut = ejbConvenio.buscarRestriccionesConvenio(moBeansIn);
			request.setAttribute("listaRestricciones", moBeansOut.get("listaRestricciones"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "convenios/restricciones/listaRestriccionesXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/restricciones/listaRestriccionesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/restricciones/listaRestriccionesXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void agregarRestriccion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			log.info("Busca Lista de Tipos de Restriccion");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 8);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTipos", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Tipos de Admisión");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 52);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTiposAdmision", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Categorías del Paciente");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 49);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaCategorias", moBeansOut.get("listaParametros"));

			long idConvenio = Long.parseLong(request.getParameter("idConvenio"));
			request.setAttribute("idConvenio", idConvenio);

			pagDestino = "convenios/restricciones/agregaRestriccion.jsp";
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

	public void guardarRestriccion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			System.out.println("sesion: " + sesion.getSesNombre());
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbConvenio.guardarRestriccion(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("sesion", sesion);

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

	public void eliminarRestriccion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idRestriccion", Long.parseLong(request.getParameter("idRestriccion")));
			moBeansOut = ejbConvenio.eliminarRestriccion(moBeansIn);
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

	public void cargarRestriccion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			long idRestriccion = 0;

			if (request.getAttribute("idRestriccion") != null)
				idRestriccion = (Long) request.getAttribute("idRestriccion");
			else
				idRestriccion = Long.parseLong((String) request.getParameter("idRestriccion"));

			log.info("idRestriccion: " + idRestriccion);

			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idRestriccion", idRestriccion);
			moBeansOut = ejbConvenio.cargarRestriccion(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			Restriccion restriccion = (Restriccion) moBeansOut.get("restriccion");
			request.setAttribute("restriccion", restriccion);

			log.info("Busca Lista de Tipos de Restriccion");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 8);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTipos", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Tipos de Admisión");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 52);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTiposAdmision", moBeansOut.get("listaParametros"));

			log.info("Busca Lista de Categorías del Paciente");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 49);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaCategorias", moBeansOut.get("listaParametros"));

			pagDestino = "convenios/restricciones/detalleRestriccion.jsp";
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

	public void actualizarRestriccion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbConvenio.actualizarRestriccion(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			// pagDestino = "general/includes/respuestaXml.jsp";
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

	public void buscarBeneficiosConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idConvenio", (long) Long.parseLong(request.getParameter("idConvenio")));
			moBeansOut = ejbConvenio.buscarBeneficiosConvenio(moBeansIn);
			request.setAttribute("listaBeneficios", moBeansOut.get("listaBeneficios"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "convenios/beneficios/listaBeneficiosXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/includes/errorXml.jsp";
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

	public void agregarBeneficio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			long idConvenio = Long.parseLong(request.getParameter("idConvenio"));
			request.setAttribute("idConvenio", idConvenio);
			long idEmpresa = Long.parseLong(request.getParameter("idEmpresa"));
			request.setAttribute("idEmpresa", idEmpresa);

			pagDestino = "convenios/beneficios/agregaBeneficio.jsp";
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

	public void guardarBeneficio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			System.out.println("sesion: " + sesion.getSesNombre());
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbConvenio.guardarBeneficio(moBeansIn);
			error = (Error) moBeansOut.get("error");
			// long idBeneficio = (Long) moBeansOut.get("idBeneficio");
			request.setAttribute("error", error);
			request.setAttribute("sesion", sesion);
			// request.setAttribute("idBeneficio", idBeneficio);
			// cargarAnexo(request, response);

			// pagDestino = "/contenedores/contenedorConvenios.jsp";
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

	public void eliminarBeneficio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idBeneficio", Long.parseLong(request.getParameter("idBeneficio")));
			moBeansOut = ejbConvenio.eliminarBeneficio(moBeansIn);
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

	public void buscarPrestacionesAnexoConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idAnexo", (long) Long.parseLong(request.getParameter("idAnexo")));
			moBeansOut = ejbConvenio.buscarPrestacionesAnexo(moBeansIn);
			request.setAttribute("listaPrestacionesAnexo", moBeansOut.get("listaConvenioAnexoDetalles"));
			error = (Error) moBeansOut.get("error");

			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);

			request.setAttribute("error", error);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			pagDestino = "convenios/anexos/prestaciones/listaPrestacionesAnexoXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/includes/errorXml.jsp";
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

	public void cargarPrestacionAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			long idPrestacionAnexo = 0;

			if (request.getAttribute("idPrestacionAnexo") != null)
				idPrestacionAnexo = (Long) request.getAttribute("idPrestacionAnexo");
			else
				idPrestacionAnexo = Long.parseLong((String) request.getParameter("idPrestacionAnexo"));

			log.info("idPrestacionAnexo: " + idPrestacionAnexo);

			log.info(request.getParameter("idPrestacionAnexo"));
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idPrestacionAnexo", idPrestacionAnexo);
			moBeansOut = ejbConvenio.cargarPrestacionAnexo(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			ConvenioAnexoDetalle prestacionAnexo = (ConvenioAnexoDetalle) moBeansOut.get("prestacionAnexo");
			request.setAttribute("prestacionAnexo", prestacionAnexo);
			pagDestino = "/contenedores/contenedorPrestAnexos.jsp";
		} catch (ErrorSPException e) {
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/error.jsp";
		} catch (Exception e) {
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

	public void actualizarPrestacionAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbConvenio.actualizarPrestacionAnexo(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
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

	public void agregarPrestacionAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			long idConvenio = Long.parseLong(request.getParameter("idConvenio"));
			long idAnexo = Long.parseLong(request.getParameter("idAnexo"));
			request.setAttribute("idConvenio", idConvenio);
			request.setAttribute("idAnexo", idAnexo);
			// pagDestino =
			// "/convenios/anexos/prestaciones/agregaPrestacionAnexo.jsp";
			pagDestino = "/contenedores/contenedorPrestAnexos.jsp";
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

	public void guardarPrestacionAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			System.out.println("sesion: " + sesion.getSesNombre());
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbConvenio.guardarPrestacionAnexo(moBeansIn);
			error = (Error) moBeansOut.get("error");
			long idPrestacionAnexo = (Long) moBeansOut.get("idPrestacionAnexo");
			request.setAttribute("error", error);
			request.setAttribute("sesion", sesion);
			request.setAttribute("idPrestacionAnexo", idPrestacionAnexo);

			/* codigo necesario para generar la nueva url del carga */
			String url = request.getRequestURL().toString();
			String urlCarga = url + "?KSI=" + sesion.getSesIdSesion() + "&accion=cargarPrestacionAnexo&idPrestacionAnexo=" + idPrestacionAnexo + "&indRuta=S";
			request.setAttribute("urlCarga", urlCarga);
			verificaUsuario(request);
			servletParametros.setSesion(((Sesion) request.getAttribute("sesion")));
			/* fin codigo */

			cargarPrestacionAnexo(request, response);
			pagDestino = "/contenedores/contenedorPrestAnexos.jsp";
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

	public void eliminarPrestacionAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idPrestacionAnexo", Long.parseLong(request.getParameter("idPrestacionAnexo")));
			moBeansOut = ejbConvenio.eliminarPrestacionAnexo(moBeansIn);
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

	public void buscarPrecios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idPrestacionAnexo", Long.parseLong(request.getParameter("idPrestacionAnexo")));
			log.info("ID PRESTACION" + request.getParameter("idPrestacionAnexo"));
			moBeansOut = ejbConvenio.buscarPrecios(moBeansIn);
			request.setAttribute("listaPrecios", moBeansOut.get("listaPrecios"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "convenios/anexos/prestaciones/precios/listaPreciosXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/anexos/prestaciones/precios/listaPreciosXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/anexos/prestaciones/precios/listaPreciosXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarModificadores(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idPrestacionAnexo", Long.parseLong(request.getParameter("idPrestacionAnexo")));
			moBeansOut = ejbConvenio.buscarModificadores(moBeansIn);
			request.setAttribute("listaModificadores", moBeansOut.get("listaModificadores"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "convenios/anexos/prestaciones/modificadores/listaModificadoresXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/anexos/prestaciones/modificadores/listaModificadoresXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/anexos/prestaciones/modificadores/listaModificadoresXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void cargarModificador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			long idModificador = 0;

			if (request.getAttribute("idModificador") != null)
				idModificador = (Long) request.getAttribute("idModificador");
			else
				idModificador = Long.parseLong((String) request.getParameter("idModificador"));

			log.info("idModificador: " + idModificador);

			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idModificador", idModificador);
			moBeansOut = ejbConvenio.cargarModificador(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			Modificador modificador = (Modificador) moBeansOut.get("modificador");
			request.setAttribute("modificador", modificador);

			log.info("Busca Lista de Tipos Modificador");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 82);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTiposModificador", moBeansOut.get("listaParametros"));

			pagDestino = "contenedores/contenedorPrecioModificador.jsp";
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

	public void agregarModificador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			long idPrestacionAnexo = Long.parseLong(request.getParameter("idPrestacionAnexo"));
			request.setAttribute("idPrestacionAnexo", idPrestacionAnexo);

			long idConvenio = Long.parseLong(request.getParameter("idConvenio")); // ${idConvenio}
			request.setAttribute("idConvenio", idConvenio);
			log.info("llego con el idConvenio: " + idConvenio);

			log.info("Busca Lista de Tipos Modificador");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 82);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTiposModificador", moBeansOut.get("listaParametros"));

			pagDestino = "contenedores/contenedorPrecioModificador.jsp";
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

	public void guardarModificador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			System.out.println("sesion: " + sesion.getSesNombre());
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbConvenio.guardarModificador(moBeansIn);
			error = (Error) moBeansOut.get("error");
			long idModificador = (Long) moBeansOut.get("idModificador");
			request.setAttribute("error", error);
			request.setAttribute("sesion", sesion);
			request.setAttribute("idModificador", idModificador);

			/* codigo necesario para generar la nueva url del carga */
			String url = request.getRequestURL().toString();
			String urlCarga = url + "?KSI=" + sesion.getSesIdSesion() + "&accion=cargarModificador&idModificador=" + idModificador + "&indRuta=S";
			request.setAttribute("urlCarga", urlCarga);
			verificaUsuario(request);
			servletParametros.setSesion(((Sesion) request.getAttribute("sesion")));
			/* fin codigo */

			cargarModificador(request, response);
			pagDestino = "/contenedores/contenedorPrecioModificador.jsp";
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

	public void actualizarModificador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbConvenio.actualizarModificador(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
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

	public void eliminarModificador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idModificador", Long.parseLong(request.getParameter("idModificador")));
			moBeansOut = ejbConvenio.eliminarModificador(moBeansIn);
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

	public void cargaEmpresasInst(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			pagDestino = "/convenios/popups/listaInstituciones.jsp";
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

	public void cargaEmpresasCli(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			pagDestino = "/convenios/popups/listaClientes.jsp";
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

	public void cargaResponsables(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			log.info("Busca Lista de Cargos");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 81);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaCargos", moBeansOut.get("listaParametros"));

			moBeansIn.clear();
			moBeansOut.clear();

			log.info("Busca Lista de Instituciones");
			Empresa empresa = new Empresa();
			moBeansIn.put("empresa", empresa);
			moBeansOut = ejbConvenio.buscarEmpresasInst(moBeansIn);

			request.setAttribute("listaInstituciones", moBeansOut.get("listaEmpresasInst"));

			long idInst = Long.parseLong(request.getParameter("idInst"));
			request.setAttribute("idInst", idInst);
			log.info("id Institucion:" + idInst);

			pagDestino = "/convenios/popups/listaResponsables.jsp";
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

	public void cargaPrestacionesSinAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			long idAnexo = Long.parseLong(request.getParameter("idAnexo"));
			request.setAttribute("idAnexo", idAnexo);
			long idConvenio = Long.parseLong(request.getParameter("idConvenio"));
			request.setAttribute("idConvenio", idConvenio);
			pagDestino = "/convenios/popups/listaPrestacionesSinAnexo.jsp";
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

	public void cargaPlanes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			long idEmpresa = Long.parseLong(request.getParameter("idEmpresa"));
			request.setAttribute("idEmpresa", idEmpresa);
			long idConvenio = Long.parseLong(request.getParameter("idConvenio"));
			request.setAttribute("idConvenio", idConvenio);
			pagDestino = "/convenios/popups/listaPlanes.jsp";
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

	public void buscarEmpresasInst(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbConvenio.buscarEmpresasInst(moBeansIn);
			request.setAttribute("listaEmpresasInst", moBeansOut.get("listaEmpresasInst"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "convenios/popups/listaInstitucionesXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaInstitucionesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaInstitucionesXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarEmpresasCli(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbConvenio.buscarEmpresasCli(moBeansIn);
			request.setAttribute("listaEmpresasCli", moBeansOut.get("listaEmpresasCli"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "convenios/popups/listaClientesXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaClientesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaClientesXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarPrestacionesSinAnexo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idAnexo", Long.parseLong(request.getParameter("idAnexo")));
			moBeansIn.put("idConvenio", Long.parseLong(request.getParameter("idConvenio")));
			moBeansOut = ejbConvenio.buscarPrestacionesSinAnexo(moBeansIn);
			request.setAttribute("listaPrestacionesSinAnexo", moBeansOut.get("listaPrestacionesSinAnexo"));
			error = (Error) moBeansOut.get("error");

			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);

			request.setAttribute("error", error);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			pagDestino = "convenios/popups/listaPrestacionesSinAnexoXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaPrestacionesSinAnexoXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaPrestacionesSinAnexoXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarPlanesBeneficio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idConvenio", Long.parseLong(request.getParameter("idConvenio")));
			moBeansOut = ejbConvenio.buscarPlanesBeneficio(moBeansIn);
			request.setAttribute("listaPlanesBeneficio", moBeansOut.get("listaPlanesBeneficio"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "convenios/popups/listaPlanesXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaPlanesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaPlanesXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void cargarPrecio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			long idPrecio = 0;

			if (request.getAttribute("idPrecio") != null)
				idPrecio = (Long) request.getAttribute("idPrecio");
			else
				idPrecio = Long.parseLong((String) request.getParameter("idPrecio"));

			log.info("idPrecio: " + idPrecio);

			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idPrecio", idPrecio);
			moBeansOut = ejbConvenio.cargarPrecio(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			Precio precio = (Precio) moBeansOut.get("precio");
			request.setAttribute("precio", precio);
			log.info("Busca Lista de Tipos Modificador");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 85);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTiposMoneda", moBeansOut.get("listaParametros"));
			pagDestino = "contenedores/contenedorPrecioModificador.jsp";
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

	public void agregarPrecio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			log.info("Busca Lista de Tipos Modificador");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("nPar_numparametro", (long) 85);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaTiposMoneda", moBeansOut.get("listaParametros"));

			long idPrestacionAnexo = Long.parseLong(request.getParameter("idPrestacionAnexo"));
			request.setAttribute("idPrestacionAnexo", idPrestacionAnexo);

			String nombrePrestacionAnexo = (String) request.getParameter("nombrePrestacionAnexo");
			request.setAttribute("nombrePrestacionAnexo", nombrePrestacionAnexo);
			pagDestino = "contenedores/contenedorPrecioModificador.jsp";
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

	public void guardarPrecio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			System.out.println("sesion: " + sesion.getSesNombre());
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbConvenio.guardarPrecio(moBeansIn);
			error = (Error) moBeansOut.get("error");
			long idPrecio = (Long) moBeansOut.get("idPrecio");
			request.setAttribute("error", error);
			request.setAttribute("sesion", sesion);
			request.setAttribute("idPrecio", idPrecio);

			/* codigo necesario para generar la nueva url del carga */
			String url = request.getRequestURL().toString();
			String urlCarga = url + "?KSI=" + sesion.getSesIdSesion() + "&accion=cargarPrecio&idPrecio=" + idPrecio + "&indRuta=S";
			request.setAttribute("urlCarga", urlCarga);
			verificaUsuario(request);
			servletParametros.setSesion(((Sesion) request.getAttribute("sesion")));
			/* fin codigo */

			cargarPrecio(request, response);
			pagDestino = "contenedores/contenedorPrecioModificador.jsp";
			log.info("[Metodo: " + nombreMetodo + "] Despachando a " + pagDestino);
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "/general/error.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "/general/error.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void eliminarPrecio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idPrecio", Long.parseLong(request.getParameter("idPrecio")));
			moBeansOut = ejbConvenio.eliminarPrecio(moBeansIn);
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

	public void actualizarPrecio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbConvenio.actualizarPrecio(moBeansIn);
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			pagDestino = "general/includes/respuestaXml.jsp";
			log.info("[Metodo: " + nombreMetodo + "] Despachando a: " + pagDestino);
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "/general/error.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("Error", error);
			log.error(e);
			pagDestino = "/general/error.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarVariablesCondicionales(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			error = new Error("0", "");
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			pagDestino = "convenios/anexos/prestaciones/modificadores/varcond/listaVarCondXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarValoresTipoAdmision(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info("Busca Lista de Tipos");
			moBeansIn.clear();
			moBeansOut.clear();
			moBeansIn.put("nPar_numparametro", (long) 52);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("listaValTipAdm", moBeansOut.get("listaParametros"));

			pagDestino = "convenios/anexos/prestaciones/modificadores/valoresTipoAdmision/listaValoresTipoAdmisionXml.jsp";
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

	public void buscarValoresUniOrg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idConvenio", (long) Long.parseLong(request.getParameter("idConvenio")));
			moBeansOut = ejbConvenio.buscarValoresUniOrg(moBeansIn);

			request.setAttribute("listaUniOrg", moBeansOut.get("listaUniOrg"));

			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			pagDestino = "convenios/anexos/prestaciones/modificadores/valoresUniOrg/listaValoresUniOrgXml.jsp";

		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/anexos/prestaciones/modificadores/valoresUniOrg/listaValoresUniOrgXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/anexos/prestaciones/modificadores/valoresUniOrg/listaValoresUniOrgXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarValPosFec(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			error = new Error("0", "");
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			pagDestino = "convenios/anexos/prestaciones/modificadores/valoresPosiblesFecha/listaValoresPosiblesFechaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarVariablesPrestacion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			error = new Error("0", "");
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			pagDestino = "convenios/anexos/prestaciones/modificadores/varprestacion/listaVarPrestacionXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarVariablesDeReferencia(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			error = new Error("0", "");
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			pagDestino = "convenios/anexos/prestaciones/modificadores/vardereferencia/listaVarRefeXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarVariablesGlobales(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			error = new Error("0", "");
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			pagDestino = "convenios/anexos/prestaciones/modificadores/varblobales/listaVarGlobalesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void validarCondicionModificadorPrestacionConvenio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			String condicion = request.getParameter("sCondicion");
			log.info("Condicion: " + condicion);
			moBeansIn.put("condicion", condicion);
			moBeansOut = ejbConvenio.validarCondicionModificadorPrestacionConvenio(moBeansIn);
			error = (Error) moBeansOut.get("error");
			log.info("numError: " + error.getNumError());
			log.info("msjError: " + error.getMsjError());
			pagDestino = "/general/includes/respuestaXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void validarFormulaModifPrestConv(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			String formula = request.getParameter("sFormula");
			log.info("Formula: " + formula);
			moBeansIn.put("formula", formula);
			moBeansOut = ejbConvenio.validarFormulaModifPrestConv(moBeansIn);
			error = (Error) moBeansOut.get("error");
			log.info("numError: " + error.getNumError());
			log.info("msjError: " + error.getMsjError());
			pagDestino = "/general/includes/respuestaXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/includes/respuestaXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "/general/includes/respuestaXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarResponsables(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			log.info("ID EMPRESA" + request.getParameter("idEmpresa"));
			moBeansIn.put("idEmpresa", Long.parseLong(request.getParameter("idEmpresa")));
			moBeansOut = ejbConvenio.buscarResponsables(moBeansIn);
			request.setAttribute("listaResponsables", moBeansOut.get("listaResponsables"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "convenios/popups/listaResponsablesXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaResponsablesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "convenios/popups/listaResponsablesXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}
}