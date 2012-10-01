package cl.solem.tyc.beans;

import java.io.Serializable;

public class Modificador implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long nIdModificador; 
	private long nIdConvDetalle; 
	private long nIdAnexo; 
	private long nIdConvenio; 
	private String sNombre; 
	private String sCodTipo;
	private String sDesTipo;
	private String sCondicion; 
	private String sFormula; 
	private String sFhoInicio; 
	private String sFhoFin; 
	private String sFhoCreacion; 
	private long nIdUsuarioCreacion; 
	private String sFhoModificacion; 
	private long nIdUsuarioModificacion;
	
	public long getnIdModificador() {
		return nIdModificador;
	}
	public void setnIdModificador(long nIdModificador) {
		this.nIdModificador = nIdModificador;
	}
	public long getnIdConvDetalle() {
		return nIdConvDetalle;
	}
	public void setnIdConvDetalle(long nIdConvDetalle) {
		this.nIdConvDetalle = nIdConvDetalle;
	}
	public long getnIdAnexo() {
		return nIdAnexo;
	}
	public void setnIdAnexo(long nIdAnexo) {
		this.nIdAnexo = nIdAnexo;
	}
	public long getnIdConvenio() {
		return nIdConvenio;
	}
	public void setnIdConvenio(long nIdConvenio) {
		this.nIdConvenio = nIdConvenio;
	}
	public String getsNombre() {
		return sNombre;
	}
	public void setsNombre(String sNombre) {
		this.sNombre = sNombre;
	}
	public String getsCodTipo() {
		return sCodTipo;
	}
	public void setsCodTipo(String sCodTipo) {
		this.sCodTipo = sCodTipo;
	}
	public String getsDesTipo() {
		return sDesTipo;
	}
	public void setsDesTipo(String sDesTipo) {
		this.sDesTipo = sDesTipo;
	}
	public String getsCondicion() {
		return sCondicion;
	}
	public void setsCondicion(String sCondicion) {
		this.sCondicion = sCondicion;
	}
	public String getsFormula() {
		return sFormula;
	}
	public void setsFormula(String sFormula) {
		this.sFormula = sFormula;
	}
	public String getsFhoInicio() {
		return sFhoInicio;
	}
	public void setsFhoInicio(String sFhoInicio) {
		this.sFhoInicio = sFhoInicio;
	}
	public String getsFhoFin() {
		return sFhoFin;
	}
	public void setsFhoFin(String sFhoFin) {
		this.sFhoFin = sFhoFin;
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
	public String getsFhoModificacion() {
		return sFhoModificacion;
	}
	public void setsFhoModificacion(String sFhoModificacion) {
		this.sFhoModificacion = sFhoModificacion;
	}
	public long getnIdUsuarioModificacion() {
		return nIdUsuarioModificacion;
	}
	public void setnIdUsuarioModificacion(long nIdUsuarioModificacion) {
		this.nIdUsuarioModificacion = nIdUsuarioModificacion;
	}
	
}
