package cl.solem.tyc.beans;

import java.io.Serializable;

public class ModificadorCatalogo implements Serializable{
	

	private static final long serialVersionUID = 784684397319601832L;
	
	private long nIdCatalogoModificador;
	private long nIdCatalogoPrestacion;
	private long nIdCatalogo;
	private long nIdPrestacion;
	private String sNombre;
	private String sCodTipo;
	private String sDesCodTipo;
	private String sCondicion;
	private String sFormula;
	private String sFhoInicio;
	private String sFhoFin;
	private String sFecCreacion;
	private long nIdCreador;
	private String sFecActualizacion;
	private long nIdActualizador;
	
	
	public long getnIdCatalogoModificador() {
		return nIdCatalogoModificador;
	}
	public void setnIdCatalogoModificador(long nIdCatalogoModificador) {
		this.nIdCatalogoModificador = nIdCatalogoModificador;
	}
	public long getnIdCatalogoPrestacion() {
		return nIdCatalogoPrestacion;
	}
	public void setnIdCatalogoPrestacion(long nIdCatalogoPrestacion) {
		this.nIdCatalogoPrestacion = nIdCatalogoPrestacion;
	}
	public long getnIdCatalogo() {
		return nIdCatalogo;
	}
	public void setnIdCatalogo(long nIdCatalogo) {
		this.nIdCatalogo = nIdCatalogo;
	}
	public long getnIdPrestacion() {
		return nIdPrestacion;
	}
	public void setnIdPrestacion(long nIdPrestacion) {
		this.nIdPrestacion = nIdPrestacion;
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
	public String getsDesCodTipo() {
		return sDesCodTipo;
	}
	public void setsDesCodTipo(String sDesCodTipo) {
		this.sDesCodTipo = sDesCodTipo;
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
