package cl.solem.tyc.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.log4j.Logger;

import cl.solem.tyc.ejb.interfaces.SesionEJBRemote;
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.ServletParametros;
import cl.solem.tyc.beans.Sesion;
import cl.solem.jee.libsolem.exception.ErrorSPException;
import cl.solem.jee.libsolem.util.Errores;
import cl.solem.jee.libsolem.web.ServletBase;

/**
 * Servlet implementation class ServletHandler
 */
public class ServletHandler extends ServletBase {

	private static final long serialVersionUID = 8399045706860653587L;
	private Logger log = Logger.getLogger(ServletHandler.class);
	
	@EJB(mappedName = "tyc/SesionEJB")
	private SesionEJBRemote ejbSesion;

	
    /**
     * Default constructor.
     */
    public ServletHandler() {
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = new HashMap<String, Object>();;
		Map<String, Object> moDataOut = new HashMap<String, Object>();
		Error error = new Error("0","");
		Error errorSesion = null;
		Sesion sesion = null;
		HashMap<String, String> perfiles = null;
		String pagDestino = "";
		String accion = "";
		
		try {
			log.info("Service en servlet handler");
			accion = request.getParameter("accion");
			
			if(accion == null) {
				accion = "servicioBase";
			}
			
			moDataOut = verificaUsuario(request);
			sesion = (Sesion)moDataOut.get("sesion");
			errorSesion = (Error)moDataOut.get("error");
			perfiles =  (HashMap<String, String>)moDataOut.get("perfiles");
			
			if(!(errorSesion.getNumError().equalsIgnoreCase("0"))) {
				throw new ErrorSPException(5, Long.valueOf(errorSesion.getNumError()), errorSesion.getMsjError(), errorSesion.getMsjErrorUsuario());
			}

			try {
				log.info("Metodo a invocar: "+accion);
				
				ServletParametros servletParametros = new ServletParametros();
				servletParametros.setMoBeansIn(moBeansIn);
				servletParametros.setMoBeansOut(moBeansOut);
				servletParametros.setMoDataOut(moDataOut);
				servletParametros.setError(error);
				servletParametros.setErrorSesion(errorSesion);
				servletParametros.setSesion(sesion);
				servletParametros.setPerfiles(perfiles);
				servletParametros.setPagDestino(pagDestino);
				request.setAttribute("servletParametros", servletParametros);
				
				log.info("Invocando al metodo ["+accion+"]");
				
				MethodUtils.invokeMethod(this, accion, new Object[]{request,response});
				
				pagDestino = servletParametros.getPagDestino();
				error = servletParametros.getError();
				sesion = servletParametros.getSesion();
				
				log.info("DATOS POST");
				log.info("PAG DESTINO: "+pagDestino);
				log.info("SESION: "+sesion.getSesIdSesion());
				log.info("RUTA SISTEMA: "+sesion.getSesRutaSistema());
	 		} catch (NoSuchMethodException e) {
	 			log.error("No Such Method Exception --> ", e);
				error = new Error(5, "2345", e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
				throw new ErrorSPException(5, Long.valueOf(error.getNumError()), error.getMsjError(), Errores.getInstance().getMsgFamilia("5"));
			} catch (IllegalAccessException e) {
				log.error("Illegal Access Exception --> ", e);
				error = new Error(5, "2346", e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
				throw new ErrorSPException(5, Long.valueOf(error.getNumError()), error.getMsjError(), Errores.getInstance().getMsgFamilia("5"));
			} catch (InvocationTargetException e) {
				log.error("Invocation Target Exception --> ", e);
				error = new Error(5, "2347", e.getMessage(), Errores.getInstance().getMsgFamilia("5"));
				throw new ErrorSPException(5, Long.valueOf(error.getNumError()), error.getMsjError(), error.getMsjErrorUsuario());
			}
		} catch (ErrorSPException e) {
			e.printStackTrace();
			log.error("Error SP Exception --> ", e);
			errorSesion = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), Errores.getInstance().getMsgFamilia(String.valueOf(e.getNumFamiliaError())));
			error = new Error(e.getNumFamiliaError(), e.getNumError(), e.getMessage(), Errores.getInstance().getMsgFamilia(String.valueOf(e.getNumFamiliaError())));
			pagDestino = "login.jsp";
		} 
		
		request.setAttribute("sesion", sesion);
		request.setAttribute("perfiles", perfiles);		
		request.setAttribute("error", error);
		request.setAttribute("errorSesion", errorSesion);
		
		if(!pagDestino.equalsIgnoreCase(""))
		{
			despacha(request, response, pagDestino);
			log.info("Pag destino: "+pagDestino);
		}
	}
	
	protected Map<String, Object>verificaUsuario(HttpServletRequest request) throws ErrorSPException {

		Error error = new Error("0", "");
		Sesion sesion = new Sesion();
		Map<String, Object> moBeansIn = new HashMap<String, Object>();
		Map<String, Object> moBeansOut = new HashMap<String, Object>();
		Map<String, Object> moDataOut = new HashMap<String, Object>();
		HashMap<String, String> perfiles = new HashMap<String, String>();
		String remoteHost = "";
		String ksi = "";
		String ruta = "";
		String indRuta = "";
		String url = "";
		String url2 = "";
		String accion = "";
	
		ksi = request.getParameter("KSI");
		remoteHost = request.getRemoteAddr();
		indRuta = request.getParameter("indRuta"); 
		accion = request.getParameter("accion");
		ruta =  (String) request.getAttribute("urlCarga");
		
		if(accion.contains("guarda") && ruta !=null)
		{
			indRuta = "S";
			ruta = (String)request.getAttribute("urlCarga");
		}
		else
		{
			if(indRuta==null)
				indRuta = "N";
			
			if(request.getRequestURL()!= null)
				url = request.getRequestURL().toString();
			
			if(request.getQueryString()!= null)
				url2 = request.getQueryString().toString();
			
			ruta = url + "?" + url2;
		}
		
		log.info("KSI: "+ ksi);
		log.info("HOST: "+ remoteHost);
		log.info("IND RUTA: "+ indRuta);
		log.info("RUTA en servlet: "+ ruta);
		
		if(ksi != null) 
		{
			moBeansIn.put("ksi", ksi);
			moBeansIn.put("host", remoteHost);
			moBeansIn.put("ruta", ruta);
			moBeansIn.put("indRuta", indRuta);
			moBeansOut = ejbSesion.validaSesion(moBeansIn);
			
			sesion = (Sesion)moBeansOut.get("sesion"); //se obtiene la sesion
			error = (Error)moBeansOut.get("error");
	
			if ((sesion != null) && (error.getNumError().equals("0")))
			{
				moBeansIn.put("sesion", sesion);
				moBeansOut = ejbSesion.buscaPerfiles(moBeansIn);
				
				error = (Error) moBeansOut.get("error");
				perfiles = (HashMap<String, String>) moBeansOut.get("perfiles");
				
				moDataOut.put("sesion", sesion);
				moDataOut.put("perfiles", perfiles);
			} 
			else 
			{
				if(!(error.getNumError().equals("0"))) {
					throw new ErrorSPException("4", error.getNumError(), error.getMsjError(), Errores.getInstance().getMsgFamilia("4"));
				} else {
					error = new Error(5, 2, "No se puede obtener la Sesion", Errores.getInstance().getMsgFamilia("5"));
					throw new ErrorSPException(5, 2, "No se puede obtener la Sesion", Errores.getInstance().getMsgFamilia("5"));
				}
			}
		}
		else
		{
			error = new Error(5, 1, "KSI Nulo", "KSI Nulo");
			throw new ErrorSPException(5, 1, "KSI Nulo", "KSI Nulo");
		}
		
		request.setAttribute("sesion", sesion); //SE MODIFICA LA SESION PARA AGREGAR LA NUEVA RUTA SISTEMA
		moDataOut.put("error", error);
		
		return moDataOut;
	}
}