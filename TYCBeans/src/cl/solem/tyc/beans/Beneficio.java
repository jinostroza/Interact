package cl.solem.tyc.beans;

import java.io.Serializable;

public class Beneficio implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long nIdConvBeneficio;
	private long nIdConvenio;
	private long nIdPlanBeneficio;
	private String sCodPlan;
	private String sNombrePlan;
	private String sDescripcion;
	private String sCodEstado;
	private String sFhoActualizacion;
	private long nIdUsuarioActualizacion;
	private String sFhoCreacion;
	private long nIdUsuarioCreacion;
	private String sIndVigencia;
	
	public long getnIdConvBeneficio() {
		return nIdConvBeneficio;
	}
	public void setnIdConvBeneficio(long nIdConvBeneficio) {
		this.nIdConvBeneficio = nIdConvBeneficio;
	}
	public long getnIdConvenio() {
		return nIdConvenio;
	}
	public void setnIdConvenio(long nIdConvenio) {
		this.nIdConvenio = nIdConvenio;
	}
	public long getnIdPlanBeneficio() {
		return nIdPlanBeneficio;
	}
	public void setnIdPlanBeneficio(long nIdPlanBeneficio) {
		this.nIdPlanBeneficio = nIdPlanBeneficio;
	}
	public String getsCodPlan() {
		return sCodPlan;
	}
	public void setsCodPlan(String sCodPlan) {
		this.sCodPlan = sCodPlan;
	}
	public String getsNombrePlan() {
		return sNombrePlan;
	}
	public void setsNombrePlan(String sNombrePlan) {
		this.sNombrePlan = sNombrePlan;
	}
	public String getsDescripcion() {
		return sDescripcion;
	}
	public void setsDescripcion(String sDescripcion) {
		this.sDescripcion = sDescripcion;
	}
	public String getsCodEstado() {
		return sCodEstado;
	}
	public void setsCodEstado(String sCodEstado) {
		this.sCodEstado = sCodEstado;
	}
	public String getsFhoActualizacion() {
		return sFhoActualizacion;
	}
	public void setsFhoActualizacion(String sFhoActualizacion) {
		this.sFhoActualizacion = sFhoActualizacion;
	}
	public long getnIdUsuarioActualizacion() {
		return nIdUsuarioActualizacion;
	}
	public void setnIdUsuarioActualizacion(long nIdUsuarioActualizacion) {
		this.nIdUsuarioActualizacion = nIdUsuarioActualizacion;
	}
	public String getsFhoCreacion() {
		return sFhoCreacion;
	}
	public void setsFhoCreacion(String sFhoCreacion) {
		this.sFhoCreacion = sFhoCreacion;
	}
	public long getnIdUsuarioCreacion() {
		return nIdUsuarioCreacion;
	}
	public void setnIdUsuarioCreacion(long nIdUsuarioCreacion) {
		this.nIdUsuarioCreacion = nIdUsuarioCreacion;
	}
	public String getsIndVigencia() {
		return sIndVigencia;
	}
	public void setsIndVigencia(String sIndVigencia) {
		this.sIndVigencia = sIndVigencia;
	}
	
}
