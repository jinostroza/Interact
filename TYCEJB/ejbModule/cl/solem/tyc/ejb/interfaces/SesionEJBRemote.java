package cl.solem.tyc.ejb.interfaces;

import java.util.Map;

import javax.ejb.Remote;

@Remote
public interface SesionEJBRemote {
	public Map<String, Object> verificaDataLogueo(Map<String, Object> moDataIn);
	 Map<String, Object> buscaPerfiles(Map<String, Object> moDataIn);
	 Map<String, Object> validaSesion(Map<String, Object> moDataIn);
}
