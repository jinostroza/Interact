package cl.solem.tyc.ejb.interfaces;

import java.util.Map;

import javax.ejb.Remote;

import cl.solem.jee.libsolem.exception.ErrorSPException;

@Remote
public interface PrestacionEJBRemote {
	
	public Map<String, Object> buscarPrestaciones(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarPrestacion(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarPrestacionesCompuestas(Map<String, Object> moDataIn) throws ErrorSPException; 
	public Map<String, Object> eliminarPrestacion(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarPrestacionCompuesta(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarPrestacion(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarPrestacionesNomencladorNacional(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarPrestacion(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarCompuestas(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> agregarCompuesta(Map<String, Object> moDataIn) throws ErrorSPException; 
	public Map<String, Object> buscarNomencladores(Map<String, Object> moDataIn) throws ErrorSPException;
    public Map<String, Object> eliminarPrestacionNacional(Map<String, Object> moDataIn) throws ErrorSPException; 
    public Map<String, Object> agregarPrestNomec(Map<String, Object> moDataIn) throws ErrorSPException; 
}