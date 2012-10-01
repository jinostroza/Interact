package cl.solem.tyc.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import cl.solem.jee.libsolem.util.Seguridad;
import cl.solem.jee.libsolem.web.ServletBase;
import cl.solem.tyc.beans.Error;
import cl.solem.tyc.beans.Login;
import cl.solem.tyc.beans.Sesion;
import cl.solem.tyc.ejb.interfaces.SesionEJBRemote;

public class SvtSession extends ServletBase {

	private static final long serialVersionUID = -4095671460020305942L;
	private Logger log = Logger.getLogger(SvtSession.class);
	
	@EJB(mappedName = "tyc/SesionEJB")
	private SesionEJBRemote ejbSesion;
	String pagDestino = "";
	
    public SvtSession() {
        super();
    	log.info("Servlet Sesion");
    }

	@SuppressWarnings("unchecked")
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> moBeansIn = null;
		Map<String, Object> moBeansOut = null;
		Error error = null;
		Sesion sesion = null;
		HashMap<String, String> perfiles = null;
		
		log.info("Service en servlet handler");
		
		pagDestino = "login.jsp";

		if (request.getMethod().endsWith("GET")) {

			pagDestino = "login.jsp";

		} else if (request.getMethod().endsWith("POST")) {

			try {
				String remoteHost = request.getParameter("login.host");
				if (remoteHost.equalsIgnoreCase(request.getRemoteAddr())) {

					moBeansIn = procesaForm(request);
					
					Login login = (Login)moBeansIn.get("login");
					
					Seguridad seguridad = new Seguridad();
					String password = "";
					String passwIn = login.getPassword();
					
					if(!passwIn.equalsIgnoreCase("")){
						password = seguridad.encrypt(passwIn);
					}
					
					log.info("Password -->" + password);
					
					login.setPassword(password);
					
					moBeansIn.clear();
					
					moBeansIn.put("login", login);
					
					// Verifico el Ingreso
					moBeansOut = ejbSesion.verificaDataLogueo(moBeansIn);

					error = (Error) moBeansOut.get("error");
					sesion = (Sesion) moBeansOut.get("sesion");

					moBeansIn.clear();

					if (sesion != null && error.getMsjError().equalsIgnoreCase("")) {
						moBeansIn.put("sesion", sesion);
						moBeansOut = ejbSesion.buscaPerfiles(moBeansIn);

						error = (Error) moBeansOut.get("error");
						perfiles = (HashMap<String, String>) moBeansOut.get("perfiles");

						request.setAttribute("sesion", sesion);
						request.setAttribute("perfiles", perfiles);
						log.info("Cantidad de Registros en los Perfiles: " + perfiles.size());
						pagDestino = "/contenedores/contenedor.jsp";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);				
			}
		}
		request.setAttribute("error", error);
		
		HttpSession session = request.getSession(false);
		if(session!=null) {
			session.invalidate();
		}
		session = request.getSession(true); 
		 
		despacha(request, response, pagDestino);
	}
}