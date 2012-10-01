package cl.solem.tyc.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ServletParametros implements Serializable {

	private static final long serialVersionUID = -9143064755436618927L;

	private Map<String, Object> moBeansIn;
	private Map<String, Object> moBeansOut;
	private Map<String, Object> moDataOut;
	private Error error;
	private Error errorSesion;
	private Sesion sesion;
	private HashMap<String, String> perfiles;
	private String pagDestino;
	private String remoteHost;
	private String ksi;
	private String accion;

	public Map<String, Object> getMoBeansIn() {
		return moBeansIn;
	}

	public void setMoBeansIn(Map<String, Object> moBeansIn) {
		this.moBeansIn = moBeansIn;
	}

	public Map<String, Object> getMoBeansOut() {
		return moBeansOut;
	}

	public void setMoBeansOut(Map<String, Object> moBeansOut) {
		this.moBeansOut = moBeansOut;
	}

	public Map<String, Object> getMoDataOut() {
		return moDataOut;
	}

	public void setMoDataOut(Map<String, Object> moDataOut) {
		this.moDataOut = moDataOut;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public Error getErrorSesion() {
		return errorSesion;
	}

	public void setErrorSesion(Error errorSesion) {
		this.errorSesion = errorSesion;
	}

	public Sesion getSesion() {
		return sesion;
	}

	public void setSesion(Sesion sesion) {
		this.sesion = sesion;
	}

	public HashMap<String, String> getPerfiles() {
		return perfiles;
	}

	public void setPerfiles(HashMap<String, String> perfiles) {
		this.perfiles = perfiles;
	}

	public String getPagDestino() {
		return pagDestino;
	}

	public void setPagDestino(String pagDestino) {
		this.pagDestino = pagDestino;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public String getKsi() {
		return ksi;
	}

	public void setKsi(String ksi) {
		this.ksi = ksi;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}
}