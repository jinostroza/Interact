package cl.solem.tyc.beans;

import java.io.Serializable;



public class PlanBeneficio implements Serializable{
	private static final long serialVersionUID = 1L;

	private long nIdPlanBeneficio;
	private long nIdEmpresa;
	private String sCodPlan;
	private String sNombrePlan;
	private String sDescripcion;
	private long nIdResponsable;
	private String sFechaInicioPlan;
	private String sFechaTerminoPlan;
	private String sIndVigencia;
	private String sFecCreacion;
	private long nIdCreador;
	private String sFecActualizacion;
	private long nIdActualizador;
	
	public long getnIdPlanBeneficio() {
		return nIdPlanBeneficio;
	}
	public void setnIdPlanBeneficio(long nIdPlanBeneficio) {
		this.nIdPlanBeneficio = nIdPlanBeneficio;
	}
	public long getnIdEmpresa() {
		return nIdEmpresa;
	}
	public void setnIdEmpresa(long nIdEmpresa) {
		this.nIdEmpresa = nIdEmpresa;
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
	public long getnIdResponsable() {
		return nIdResponsable;
	}
	public void setnIdResponsable(long nIdResponsable) {
		this.nIdResponsable = nIdResponsable;
	}
	public String getsFechaInicioPlan() {
		return sFechaInicioPlan;
	}
	public void setsFechaInicioPlan(String sFechaInicioPlan) {
		this.sFechaInicioPlan = sFechaInicioPlan;
	}
	public String getsFechaTerminoPlan() {
		return sFechaTerminoPlan;
	}
	public void setsFechaTerminoPlan(String sFechaTerminoPlan) {
		this.sFechaTerminoPlan = sFechaTerminoPlan;
	}
	public String getsIndVigencia() {
		return sIndVigencia;
	}
	public void setsIndVigencia(String sIndVigencia) {
		this.sIndVigencia = sIndVigencia;
	}
	public String getsFecCreacion() {
		return sFecCreacion;
	}
	public void setsFecCreacion(String sFecCreacion) {
		this.sFecCreacion = sFecCreacion;
	}
	public long getnIdCreador() {
		return nIdCreador;
	}
	public void setnIdCreador(long nIdCreador) {
		this.nIdCreador = nIdCreador;
	}
	public String getsFecActualizacion() {
		return sFecActualizacion;
	}
	public void setsFecActualizacion(String sFecActualizacion) {
		this.sFecActualizacion = sFecActualizacion;
	}
	public long getnIdActualizador() {
		return nIdActualizador;
	}
	public void setnIdActualizador(long nIdActualizador) {
		this.nIdActualizador = nIdActualizador;
	}
	
}
