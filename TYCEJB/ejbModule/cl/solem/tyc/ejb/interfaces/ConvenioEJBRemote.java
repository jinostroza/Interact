package cl.solem.tyc.ejb.interfaces;

import java.util.Map;

import javax.ejb.Remote;

import cl.solem.jee.libsolem.exception.ErrorSPException;

@Remote
public interface ConvenioEJBRemote {
	
	public Map<String, Object> cargarConvenio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarConvenios(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarConvenio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarConvenio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarConvenio(Map<String, Object> moDataIn) throws ErrorSPException;
	
	public Map<String, Object> buscarAnexosConvenio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	
	public Map<String, Object> buscarBeneficiosConvenio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarBeneficio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarBeneficio(Map<String, Object> moDataIn) throws ErrorSPException;
	
	public Map<String, Object> buscarRestriccionesConvenio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarRestriccion(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarRestriccion(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarRestriccion(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarRestriccion(Map<String, Object> moDataIn) throws ErrorSPException;
	
	public Map<String, Object> buscarPrestacionesAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarPrestacionAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarPrestacionAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarPrestacionAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarPrestacionAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	
	public Map<String, Object> buscarPrecios(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarPrecio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarPrecio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarPrecio(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarPrecio(Map<String, Object> moDataIn) throws ErrorSPException;
	
	public Map<String, Object> buscarModificadores(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> cargarModificador(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> actualizarModificador(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> guardarModificador(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> eliminarModificador(Map<String, Object> moDataIn) throws ErrorSPException;

	public Map<String, Object> buscarEmpresasInst(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarEmpresasCli(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarPrestacionesSinAnexo(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarResponsables(Map<String, Object> moDataIn) throws ErrorSPException;
	public Map<String, Object> buscarPlanesBeneficio(Map<String, Object> moDataIn) throws ErrorSPException;
	
	public Map<String, Object> validarCondicionModificadorPrestacionConvenio(Map<String, Object> moBeansIn) throws ErrorSPException;
	public Map<String, Object> validarFormulaModifPrestConv(Map<String, Object> moBeansIn) throws ErrorSPException;
	public Map<String, Object> buscarValoresUniOrg(Map<String, Object> moDataIn) throws ErrorSPException;
}