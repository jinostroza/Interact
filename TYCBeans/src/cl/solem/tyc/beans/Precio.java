package cl.solem.tyc.beans;

import java.io.Serializable;

public class Precio implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long nIdConvenioPrecio;
	private long nIdConvDetalle;
	private long nIdAnexo; 
	private long nIdPrestacion; 
	private String sFhoInicio; 
	private String sFhoFin;
	private String sIndPrecioFormula;
	private long nPrecio; 
	private String sFormula; 
	private String sFhoCreacion; 
	private long nIdUsuarioCreacion;
	private String sFhoActualizacion;
	private long nIdActualizador;
	private String sNombrePrestacion;
	private String sCodTipoMoneda;
	
	public long getnIdConvenioPrecio() {
		return nIdConvenioPrecio;
	}
	public void setnIdConvenioPrecio(long nIdConvenioPrecio) {
		this.nIdConvenioPrecio = nIdConvenioPrecio;
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
	public long getnIdPrestacion() {
		return nIdPrestacion;
	}
	public void setnIdPrestacion(long nIdPrestacion) {
		this.nIdPrestacion = nIdPrestacion;
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
	public String getsIndPrecioFormula() {
		return sIndPrecioFormula;
	}
	public void setsIndPrecioFormula(String sIndPrecioFormula) {
		this.sIndPrecioFormula = sIndPrecioFormula;
	}
	public long getnPrecio() {
		return nPrecio;
	}
	public void setnPrecio(long nPrecio) {
		this.nPrecio = nPrecio;
	}
	public String getsFormula() {
		return sFormula;
	}
	public void setsFormula(String sFormula) {
		this.sFormula = sFormula;
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
	public String getsNombrePrestacion() {
		return sNombrePrestacion;
	}
	public void setsNombrePrestacion(String sNombrePrestacion) {
		this.sNombrePrestacion = sNombrePrestacion;
	}
	public String getsFhoActualizacion() {
		return sFhoActualizacion;
	}
	public void setsFhoActualizacion(String sFhoActualizacion) {
		this.sFhoActualizacion = sFhoActualizacion;
	}
	public long getnIdActualizador() {
		return nIdActualizador;
	}
	public void setnIdActualizador(long nIdActualizador) {
		this.nIdActualizador = nIdActualizador;
	}
	public String getsCodTipoMoneda() {
		return sCodTipoMoneda;
	}
	public void setsCodTipoMoneda(String sCodTipoMoneda) {
		this.sCodTipoMoneda = sCodTipoMoneda;
	}
}
