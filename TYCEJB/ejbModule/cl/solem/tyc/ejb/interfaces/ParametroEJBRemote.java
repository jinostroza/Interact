package cl.solem.tyc.ejb.interfaces;

import java.util.Map;


import javax.ejb.Remote;

import cl.solem.jee.libsolem.exception.ErrorSPException;
@Remote
public interface ParametroEJBRemote {
	 public Map<String, Object> buscaParametros(Map<String, Object> moDataIn) throws ErrorSPException;
}
