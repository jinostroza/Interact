package cl.solem.tyc.beans;

import java.io.Serializable;

public class PrecioCatalogo implements Serializable{
	

	private static final long serialVersionUID = -2862694703231823406L;
	private long nIdCatalogoPrecio;
	private long nIdCatalogo;
	private long nIdCatalogoPrestacion;
	private long nIdPrestacion; 
	private String sFhoInicio; 
	private String sFhoFin;
	private String sIndPrecioFormula;
	private long nPrecio; 
	private String sFormula; 
	private String sFhoCreacion; 
	private long nIdUsuarioCreacion;
	private String sFhoModificacion;
	private long nIdUsuarioModificacion;
	private String sNombrePrestacion;
	private String sCodTipoMoneda;
	
	
	public long getnIdCatalogoPrecio() {
		return nIdCatalogoPrecio;
	}
	public void setnIdCatalogoPrecio(long nIdCatalogoPrecio) {
		this.nIdCatalogoPrecio = nIdCatalogoPrecio;
	}
	public long getnIdCatalogo() {
		return nIdCatalogo;
	}
	public void setnIdCatalogo(long nIdCatalogo) {
		this.nIdCatalogo = nIdCatalogo;
	}
	public long getnIdCatalogoPrestacion() {
		return nIdCatalogoPrestacion;
	}
	public void setnIdCatalogoPrestacion(long nIdCatalogoPrestacion) {
		this.nIdCatalogoPrestacion = nIdCatalogoPrestacion;
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
	public String getsNombrePrestacion() {
		return sNombrePrestacion;
	}
	public void setsNombrePrestacion(String sNombrePrestacion) {
		this.sNombrePrestacion = sNombrePrestacion;
	}
	public String getsCodTipoMoneda() {
		return sCodTipoMoneda;
	}
	public void setsCodTipoMoneda(String sCodTipoMoneda) {
		this.sCodTipoMoneda = sCodTipoMoneda;
	}
	
	
	
		
}
