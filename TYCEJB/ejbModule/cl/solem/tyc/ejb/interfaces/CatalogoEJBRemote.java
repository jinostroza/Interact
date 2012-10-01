package cl.solem.tyc.ejb.interfaces;

import java.util.Map;

import javax.ejb.Remote;

import cl.solem.jee.libsolem.exception.ErrorSPException;

@Remote
public interface CatalogoEJBRemote {
	
	public Map<String, Object> buscarCatalogos(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarPrecios(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarModificadores(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarEmpresasInst(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarPrestacionesCatDispon(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarPrestacionesCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException; 
	public Map<String, Object> validarCondicionModificadoresCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarResponsablesCatalogos(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarPreciosCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarPrecioCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarPreciosCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarPreciosCatalogo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> validarFormula(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarValoresUniOrg(Map<String, Object> moDataIn) throws ErrorSPException;
	
	
	
	
	
	
	
}