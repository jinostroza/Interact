package cl.solem.tyc.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cl.solem.jee.libsolem.exception.ErrorSPException;
import cl.solem.jee.libsolem.util.Errores;
import cl.solem.tyc.beans.Catalogo;
import cl.solem.tyc.beans.CatalogoPrest;
import cl.solem.tyc.beans.Empresa;
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.ModificadorCatalogo;
import cl.solem.tyc.beans.PrecioCatalogo;
import cl.solem.tyc.beans.ServletParametros;
import cl.solem.tyc.beans.Sesion;
import cl.solem.tyc.ejb.interfaces.CatalogoEJBRemote;
import cl.solem.tyc.ejb.interfaces.ParametroEJBRemote;

public class SvtCatalogos extends ServletHandler {

	private static final long serialVersionUID = 7125948037943117105L;
	private Logger log = Logger.getLogger(SvtCatalogos.class);
	private static String separadorLog = "----------------------------------------------------------------------------------------------------------";

	@EJB(mappedName = "tyc/CatalogoEJB")
	private CatalogoEJBRemote ejbCatalogo;

	@EJB(mappedName = "tyc/ParametroEJB")
	private ParametroEJBRemote ejbParametro;

	public SvtCatalogos() {
		super();
	}

	public void despacharCatalogos(HttpServletRequest request, HttpServletResponse response) {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();
		
		try {
			log.info(separadorLog);
			log.info("[Rescatando Estado de un catalogo: ");
			moBeansIn.clear();
			moBeansIn.put("nPar_numparametro", (long) 71);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("error", error);

			request.setAttribute("listacatalogo", moBeansOut.get("listaParametros"));

			moBeansIn.clear();
			moBeansOut.clear();

			log.info(separadorLog);

			log.info("[Método: " + nombreMetodo + "] Iniciando");
			pagDestino = "contenedores/contenedorCatalogos.jsp";
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

	public void buscarCatalogos(HttpServletRequest request, HttpServletResponse response) {
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
			moBeansIn = procesaForm(request);
			moBeansIn.put("sOrdenarPor", sOrdenarPor);
			moBeansIn.put("sTipoOrden", sTipoOrden);
			moBeansIn.put("pagActual", pagActual);
			moBeansIn.put("regPorPagina", regPorPagina);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbCatalogo.buscarCatalogos(moBeansIn);
			request.setAttribute("listaCatalogos", moBeansOut.get("listaCatalogos"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			log.info("NUM ERROR: " + error.getNumError());

			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);

			request.setAttribute("error", error);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			pagDestino = "catalogos/listaCatalogosXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/listaCatalogosXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/listaCatalogosXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void cargarCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			log.info(separadorLog);
			log.info("[Rescatando Estado de un catalogo: ");
			moBeansIn.clear();
			moBeansIn.put("nPar_numparametro", (long) 71);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("error", error);

			request.setAttribute("listacatalogo", moBeansOut.get("listaParametros"));

			moBeansIn.clear();
			moBeansOut.clear();

			log.info(separadorLog);
			log.info("[Método: " + nombreMetodo + "] Iniciando");

			long idCatalogo = 0;

			if (request.getAttribute("idCatalogo") != null)
				idCatalogo = (Long) request.getAttribute("idCatalogo");
			else
				idCatalogo = Long.parseLong((String) request.getParameter("idCatalogo"));

			log.info(nombreMetodo + " idCatalogo: " + idCatalogo);

			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idCatalogo", idCatalogo);
			log.info("llegue o no? " + idCatalogo);
			moBeansOut = ejbCatalogo.cargarCatalogo(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			Catalogo catalogo = (Catalogo) moBeansOut.get("catalogo");
			request.setAttribute("catalogo", catalogo);
			pagDestino = "contenedores/contenedorCatalogos.jsp";
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

	public void actualizarCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.actualizarCatalogo(moBeansIn);
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

	public void buscarPrestacionesCatalogo(HttpServletRequest request, HttpServletResponse response) {
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

			log.info(separadorLog);
			log.info("[Método: " + nombreMetodo + "] Iniciando");
			log.info("ID CAT: " + request.getParameter("idCatalogo"));
			moBeansIn.put("idCatalogo", Long.parseLong(request.getParameter("idCatalogo")));
			moBeansIn.put("sOrdenarPor", sOrdenarPor);
			moBeansIn.put("sTipoOrden", sTipoOrden);
			moBeansIn.put("pagActual", pagActual);
			moBeansIn.put("regPorPagina", regPorPagina);
			moBeansIn.put("sesion", sesion);

			moBeansOut = ejbCatalogo.buscarPrestacionesCatalogo(moBeansIn);
			request.setAttribute("listaPrestacionesCatalogo", moBeansOut.get("listaPrestacionesCatalogo"));
			error = (Error) moBeansOut.get("error");
			log.info("NUM ERROR: " + error.getNumError());
			log.info("MSJ ERROR: " + error.getMsjError());
			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);

			request.setAttribute("error", error);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);

			pagDestino = "catalogos/prestaciones/listaPrestacionesCatalogoXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/prestaciones/listaPrestacionesCatalogoXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/prestaciones/listaPrestacionesCatalogoXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void cargarPrestacionesCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			long idCatalogoPrestacion = 0;

			if (request.getAttribute("idCatalogoPrestacion") != null)
				idCatalogoPrestacion = (Long) request.getAttribute("idCatalogoPrestacion");
			else
				idCatalogoPrestacion = Long.parseLong((String) request.getParameter("idCatalogoPrestacion"));

			log.info(nombreMetodo + " idCatalogoPrestacion: " + idCatalogoPrestacion);

			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idCatalogoPrestacion", idCatalogoPrestacion);
			moBeansOut = ejbCatalogo.cargarPrestacionesCatalogo(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			CatalogoPrest prestacioncatalogo = (CatalogoPrest) moBeansOut.get("prestacioncatalogo");
			request.setAttribute("prestacioncatalogo", prestacioncatalogo);
			pagDestino = "/contenedores/contenedorCatalogosPrestacion.jsp";
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
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idCatalogoPrestacion", Long.parseLong(request.getParameter("idCatalogoPrestacion")));
			moBeansOut = ejbCatalogo.buscarPrecios(moBeansIn);
			request.setAttribute("listaPrecios", moBeansOut.get("listaPrecios"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "catalogos/prestaciones/precios/listaPreciosXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/prestaciones/precios/listaPreciosXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/prestaciones/precios/listaPreciosXml.jsp";
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
			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idCatalogoPrestacion", Long.parseLong(request.getParameter("idCatalogoPrestacion")));
			moBeansOut = ejbCatalogo.buscarModificadores(moBeansIn);
			request.setAttribute("listaModificadores", moBeansOut.get("listaModificadores"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "catalogos/prestaciones/modificadores/listaModificadoresXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/prestaciones/modificadores/listaModificadoresXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/prestaciones/modificadores/listaModificadoresXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void agregarCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			pagDestino = "contenedores/contenedorCatalogos.jsp";
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

	public void guardarCatalogo(HttpServletRequest request, HttpServletResponse response) {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();
		long idCatalogo = 0;
		
		try {
			log.info("[Metodo: " + nombreMetodo + "] Iniciando");
			System.out.println("sesion: " + sesion.getSesNombre());
			moBeansIn = procesaForm(request);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbCatalogo.guardarCatalogo(moBeansIn);
			idCatalogo = (Long) moBeansOut.get("idCatalogo");
			request.setAttribute("idCatalogo", idCatalogo);

			/* codigo necesario para generar la nueva url del carga */
			String url = request.getRequestURL().toString();
			String urlCarga = url + "?KSI=" + sesion.getSesIdSesion() + "&accion=cargarCatalogo&idCatalogo=" + idCatalogo + "&indRuta=S";
			request.setAttribute("urlCarga", urlCarga);
			verificaUsuario(request);
			servletParametros.setSesion(((Sesion) request.getAttribute("sesion")));
			/* fin codigo */

			cargarCatalogo(request, response);
			pagDestino = "/contenedores/contenedorCatalogos.jsp";
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
			pagDestino = "/catalogos/popups/listaInstituciones.jsp";
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
			moBeansOut = ejbCatalogo.buscarEmpresasInst(moBeansIn);
			request.setAttribute("listaEmpresasInst", moBeansOut.get("listaEmpresasInst"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "catalogos/popups/listaInstitucionesXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/popups/listaInstitucionesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/popups/listaInstitucionesXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void eliminarCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idCatalogo", Long.parseLong(request.getParameter("idCatalogo")));
			moBeansOut = ejbCatalogo.eliminarCatalogo(moBeansIn);
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
			long idCatalogo = Long.parseLong(request.getParameter("idCatalogo"));
			request.setAttribute("idCatalogo", idCatalogo);
			pagDestino = "contenedores/contenedorCatalogosPrestacion.jsp";
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

	public void cargaPrestaciones(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

			long idCatalogo = Long.parseLong(request.getParameter("idCatalogo"));
			request.setAttribute("idCatalogo", idCatalogo);
			pagDestino = "/catalogos/popups/listaPrestacionesCatalogoDisponibles.jsp";
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

	public void buscarPrestacionesCatDispon(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn = procesaForm(request);
			moBeansIn.put("sOrdenarPor", sOrdenarPor);
			moBeansIn.put("sTipoOrden", sTipoOrden);
			moBeansIn.put("pagActual", pagActual);
			moBeansIn.put("regPorPagina", regPorPagina);
			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbCatalogo.buscarPrestacionesCatDispon(moBeansIn);
			request.setAttribute("listaPrestacionesCatDispon", moBeansOut.get("listaPrestacionesCatDispon"));
			error = (Error) moBeansOut.get("error");

			totalRegistros = (Integer) moBeansOut.get("totalRegistros");
			totalPaginas = Math.ceil((double) totalRegistros / (double) regPorPagina);

			request.setAttribute("error", error);
			request.setAttribute("pagActual", pagActual);
			request.setAttribute("totalPaginas", totalPaginas);
			request.setAttribute("totalRegistros", totalRegistros);
			pagDestino = "catalogos/popups/listaPrestacionesCatalogoDisponiblesXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/popups/listaPrestacionesCatalogoDisponiblesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/popups/listaPrestacionesCatalogoDisponiblesXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void guardarPrestacionesCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.guardarPrestacionesCatalogo(moBeansIn);
			error = (Error) moBeansOut.get("error");
			long IdCatalogoPrestacion = (Long) moBeansOut.get("IdCatalogoPrestacion");
			
			/* codigo necesario para generar la nueva url del carga */
			String url = request.getRequestURL().toString();
			String urlCarga = url + "?KSI=" + sesion.getSesIdSesion() + "&accion=cargarPrestacionesCatalogo&IdCatalogoPrestacion=" + IdCatalogoPrestacion + "&indRuta=S";
			request.setAttribute("urlCarga", urlCarga);
			verificaUsuario(request);
			servletParametros.setSesion(((Sesion) request.getAttribute("sesion")));
			/* fin codigo */
			
			log.info("estoy aca " + IdCatalogoPrestacion);
			request.setAttribute("error", error);
			request.setAttribute("sesion", sesion);
			request.setAttribute("idCatalogoPrestacion", IdCatalogoPrestacion);
			cargarPrestacionesCatalogo(request, response);
			pagDestino = "/contenedores/contenedorCatalogosPrestacion.jsp";
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

	public void eliminarPrestacionesCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansIn.put("idCatalogo", Long.parseLong(request.getParameter("idCatalogo")));

			String lista = request.getParameter("aIds");
			moBeansIn.put("lista", lista);
			log.info(" LISTASSSS --> " + lista);
			moBeansOut = ejbCatalogo.eliminarPrestacionesCatalogo(moBeansIn);
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

	public void actualizarPrestacionesCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.actualizarPrestacionesCatalogo(moBeansIn);
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

	public void cargarModificadoresCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {

			log.info(separadorLog);
			log.info("[Rescatando un tipo de un modifacador: ");
			moBeansIn.clear();
			moBeansIn.put("nPar_numparametro", (long) 82);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("error", error);

			request.setAttribute("listamodificador", moBeansOut.get("listaParametros"));

			moBeansIn.clear();
			moBeansOut.clear();

			log.info(separadorLog);
			log.info("[Método: " + nombreMetodo + "] Iniciando");

			long idCatalogoModificador = 0;

			if (request.getAttribute("idCatalogoModificador") != null)
				idCatalogoModificador = (Long) request.getAttribute("idCatalogoModificador");
			else
				idCatalogoModificador = Long.parseLong((String) request.getParameter("idCatalogoModificador"));

			log.info(nombreMetodo + " idCatalogoModificador: " + idCatalogoModificador);

			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idCatalogoModificador", idCatalogoModificador);
			log.info("llegue o no? " + idCatalogoModificador);
			moBeansOut = ejbCatalogo.cargarModificadoresCatalogo(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			ModificadorCatalogo modificador = (ModificadorCatalogo) moBeansOut.get("modificador");
			request.setAttribute("modificador", modificador);
			pagDestino = "contenedores/contenedorCatalogosPrestacionMod.jsp";
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

	public void actualizarModificadoresCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.actualizarModificadoresCatalogo(moBeansIn);
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

	public void guardarModificadoresCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.guardarModificadoresCatalogo(moBeansIn);
			error = (Error) moBeansOut.get("error");
			long IdCatalogoModificador = (Long) moBeansOut.get("IdCatalogoModificador");
			log.info("estoy aca " + IdCatalogoModificador);
			request.setAttribute("error", error);
			request.setAttribute("sesion", sesion);
			request.setAttribute("idCatalogoModificador", IdCatalogoModificador);
			cargarModificadoresCatalogo(request, response);
			pagDestino = "/contenedores/contenedorCatalogosPrestacionMod.jsp";
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
			log.info(separadorLog);
			log.info("[Rescatando un tipo de un modifacador: ");
			moBeansIn.clear();
			moBeansIn.put("nPar_numparametro", (long) 82);
			moBeansIn.put("sPar_codparametro01", null);
			moBeansIn.put("sPar_codparametro02", null);
			moBeansIn.put("sPar_codparametro03", null);
			moBeansIn.put("sPar_codparametro04", null);
			moBeansIn.put("sPar_codparametro05", null);

			moBeansIn.put("sesion", sesion);
			moBeansOut = ejbParametro.buscaParametros(moBeansIn);
			request.setAttribute("error", error);

			request.setAttribute("listamodificador", moBeansOut.get("listaParametros"));

			log.info("[Metodo: " + nombreMetodo + "] Iniciando");

			long idCatalogoPrestacion = Long.parseLong(request.getParameter("idCatalogoPrestacion"));
			request.setAttribute("idCatalogoPrestacion", idCatalogoPrestacion);

			long idCatalogo = Long.parseLong(request.getParameter("idCatalogo"));
			request.setAttribute("idCatalogo", idCatalogo);
			log.info("idCatalogo" + idCatalogo);

			String nombreCatalogo = (String) request.getParameter("nombreCatalogo");
			request.setAttribute("nombreCatalogo", nombreCatalogo);
			String nombrePrestacionCatalogo = (String) request.getParameter("nombrePrestacionCatalogo");
			request.setAttribute("nombrePrestacionCatalogo", nombrePrestacionCatalogo);

			pagDestino = "contenedores/contenedorCatalogosPrestacionMod.jsp";
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

	public void eliminarModificadoresCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.eliminarModificadoresCatalogo(moBeansIn);
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

	public void validarCondicionModificadoresCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.validarCondicionModificadoresCatalogo(moBeansIn);
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
			pagDestino = "catalogos/prestaciones/modificadores/varcond/listaVarCondXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarValoresPosibles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			pagDestino = "catalogos/prestaciones/modificadores/valoresPosibles/listaValoresPosiblesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
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
			pagDestino = "catalogos/prestaciones/modificadores/valoresPosiblesFecha/listaValoresPosiblesFechaXml.jsp";
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
			pagDestino = "catalogos/prestaciones/modificadores/varprestacion/listaVarPrestacionXml.jsp";
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
			pagDestino = "catalogos/prestaciones/modificadores/vardereferencia/listaVarRefeXml.jsp";
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
			pagDestino = "catalogos/prestaciones/modificadores/varblobales/listaVarGlobalesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarResponsablesCatalogos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.buscarResponsablesCatalogos(moBeansIn);
			request.setAttribute("listaResponsables", moBeansOut.get("listaResponsables"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			request.setAttribute("pagina", 1);
			request.setAttribute("totalpaginas", 2);
			request.setAttribute("numTotalReg", 1);
			pagDestino = "catalogos/popups/listaResponsablesXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/popups/listaResponsablesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/popups/listaResponsablesXml.jsp";
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
			moBeansOut = ejbCatalogo.buscarEmpresasInst(moBeansIn);

			request.setAttribute("listaInstituciones", moBeansOut.get("listaEmpresasInst"));

			long idInst = Long.parseLong(request.getParameter("idInst"));
			request.setAttribute("idInst", idInst);
			log.info("id Institucion:" + idInst);

			pagDestino = "/catalogos/popups/listaResponsables.jsp";
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

	public void guardarPreciosCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.guardarPreciosCatalogo(moBeansIn);
			error = (Error) moBeansOut.get("error");
			long idCatalogoPrecio = (Long) moBeansOut.get("idCatalogoPrecio");
			log.info("estoy aca " + idCatalogoPrecio);
			request.setAttribute("error", error);
			request.setAttribute("sesion", sesion);
			request.setAttribute("idCatalogoPrecio", idCatalogoPrecio);
			cargarPrecioCatalogo(request, response);
			pagDestino = "/contenedores/contenedorCatalogosPrestacionPrecio.jsp";
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
			
			moBeansIn.put("sesion", sesion);
			log.info("[Metodo: " + nombreMetodo + "] Iniciando");
			long idCatalogoPrestacion = Long.parseLong(request.getParameter("idCatalogoPrestacion"));
			request.setAttribute("idCatalogoPrestacion", idCatalogoPrestacion);

			long idCatalogo = Long.parseLong(request.getParameter("idCatalogo"));
			request.setAttribute("idCatalogo", idCatalogo);
			log.info("idCatalogo" + idCatalogo);

			String nombreCatalogo = (String) request.getParameter("nombreCatalogo");
			request.setAttribute("nombreCatalogo", nombreCatalogo);

			String nombrePrestacionCatalogo = (String) request.getParameter("nombrePrestacionCatalogo");
			request.setAttribute("nombrePrestacionCatalogo", nombrePrestacionCatalogo);
			pagDestino = "contenedores/contenedorCatalogosPrestacionPrecio.jsp";
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

	public void cargarPrecioCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nombreMetodo = new Exception().getStackTrace()[0].getMethodName();
		String pagDestino = "";
		ServletParametros servletParametros = (ServletParametros) request.getAttribute("servletParametros");
		Sesion sesion = servletParametros.getSesion();
		Error error = servletParametros.getError();
		Map<String, Object> moBeansIn = servletParametros.getMoBeansIn();
		Map<String, Object> moBeansOut = servletParametros.getMoBeansOut();
		Map<String, Object> moDataOut = servletParametros.getMoBeansOut();

		try {
			moBeansIn.put("sesion", sesion);
			request.setAttribute("error", error);
			moBeansIn.clear();
			moBeansOut.clear();

			log.info(separadorLog);
			log.info("[Método: " + nombreMetodo + "] Iniciando");

			long idCatalogoPrecio = 0;

			if (request.getAttribute("idCatalogoPrecio") != null)
				idCatalogoPrecio = (Long) request.getAttribute("idCatalogoPrecio");
			else
				idCatalogoPrecio = Long.parseLong((String) request.getParameter("idCatalogoPrecio"));

			log.info(nombreMetodo + " idCatalogoPrecio: " + idCatalogoPrecio);

			moBeansIn.put("sesion", sesion);
			moBeansIn.put("idCatalogoPrecio", idCatalogoPrecio);
			log.info("llegue o no? " + idCatalogoPrecio);
			moBeansOut = ejbCatalogo.cargarPrecioCatalogo(moBeansIn);
			request.setAttribute("error", moBeansOut.get("error"));
			PrecioCatalogo preciocatalogo = (PrecioCatalogo) moBeansOut.get("preciocatalogo");
			request.setAttribute("preciocatalogo", preciocatalogo);
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
			pagDestino = "contenedores/contenedorCatalogosPrestacionPrecio.jsp";
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

	public void actualizarPreciosCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.actualizarPreciosCatalogo(moBeansIn);
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

	public void eliminarPreciosCatalogo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.eliminarPreciosCatalogo(moBeansIn);
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

	public void validarFormula(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			moBeansOut = ejbCatalogo.validarFormula(moBeansIn);
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
			pagDestino = "catalogos/prestaciones/modificadores/valoresTipoAdmision/listaValoresTipoAdmisionXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/prestaciones/modificadores/valoresTipoAdmision/listaValoresTipoAdmisionXml.jsp";
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
			moBeansIn.put("idCatalogo", (long) Long.parseLong(request.getParameter("idCatalogo")));
			moBeansOut = ejbCatalogo.buscarValoresUniOrg(moBeansIn);
			request.setAttribute("listaUniOrg", moBeansOut.get("listaUniOrg"));
			error = (Error) moBeansOut.get("error");
			request.setAttribute("error", error);
			pagDestino = "catalogos/prestaciones/modificadores/valoresUniOrg/listaValoresUniOrgXml.jsp";
		} catch (ErrorSPException e) {
			e.printStackTrace();
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), e.getMsjErrorUsuario());
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/prestaciones/modificadores/valoresUniOrg/listaValoresUniOrgXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
			error = new Error(5, e.hashCode(), e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
			log.info("Error Numero --> " + error.getNumError() + " Mensaje Error: " + error.getMsjError());
			log.info("Familia Error Numero --> " + error.getNumFamiliaError() + " Mensaje Error Usuario: " + error.getMsjErrorUsuario());
			request.setAttribute("error", error);
			log.error(e);
			pagDestino = "catalogos/prestaciones/modificadores/valoresUniOrg/listaValoresUniOrgXml.jsp";
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarVariablesPrestacionPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			pagDestino = "catalogos/prestaciones/precios/varprestacion/listaVarPrestacionXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarVariablesDeReferenciaPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			pagDestino = "catalogos/prestaciones/precios/vardereferencia/listaVarRefeXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}

	public void buscarVariablesGlobalesPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			pagDestino = "catalogos/prestaciones/precios/varblobales/listaVarGlobalesXml.jsp";
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletParametros.setError(error);
		servletParametros.setPagDestino(pagDestino);
	}
}