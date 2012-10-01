package cl.solem.tyc.beans;

import java.io.Serializable;

public class UniOrg implements Serializable {  
	
	private static final long serialVersionUID = 1L;
	
	private long   nHijoUorIdUnidadOrg;
	private String sHijoUorCodTipoUnidad; 
	private String sHijoUorDescripcion;
	private String sHijoUorCodUnidadOrg;
	private String sHijoUorCodEstado;   
    private String sFecCreacion;
    private String sHijoUorIndVigencia;
    private long   nPadreUorIdUnidadOrg;
    private String sPadreUorDescripcion;
    private String sHijoUorIndUltimoNivel;
	
    public long getnHijoUorIdUnidadOrg() {
		return nHijoUorIdUnidadOrg;
	}
	public void setnHijoUorIdUnidadOrg(long nHijoUorIdUnidadOrg) {
		this.nHijoUorIdUnidadOrg = nHijoUorIdUnidadOrg;
	}
	public String getsHijoUorCodTipoUnidad() {
		return sHijoUorCodTipoUnidad;
	}
	public void setsHijoUorCodTipoUnidad(String sHijoUorCodTipoUnidad) {
		this.sHijoUorCodTipoUnidad = sHijoUorCodTipoUnidad;
	}
	public String getsHijoUorDescripcion() {
		return sHijoUorDescripcion;
	}
	public void setsHijoUorDescripcion(String sHijoUorDescripcion) {
		this.sHijoUorDescripcion = sHijoUorDescripcion;
	}
	public String getsHijoUorCodUnidadOrg() {
		return sHijoUorCodUnidadOrg;
	}
	public void setsHijoUorCodUnidadOrg(String sHijoUorCodUnidadOrg) {
		this.sHijoUorCodUnidadOrg = sHijoUorCodUnidadOrg;
	}
	public String getsHijoUorCodEstado() {
		return sHijoUorCodEstado;
	}
	public void setsHijoUorCodEstado(String sHijoUorCodEstado) {
		this.sHijoUorCodEstado = sHijoUorCodEstado;
	}
	public String getsFecCreacion() {
		return sFecCreacion;
	}
	public void setsFecCreacion(String sFecCreacion) {
		this.sFecCreacion = sFecCreacion;
	}
	public String getsHijoUorIndVigencia() {
		return sHijoUorIndVigencia;
	}
	public void setsHijoUorIndVigencia(String sHijoUorIndVigencia) {
		this.sHijoUorIndVigencia = sHijoUorIndVigencia;
	}
	public long getnPadreUorIdUnidadOrg() {
		return nPadreUorIdUnidadOrg;
	}
	public void setnPadreUorIdUnidadOrg(long nPadreUorIdUnidadOrg) {
		this.nPadreUorIdUnidadOrg = nPadreUorIdUnidadOrg;
	}
	public String getsPadreUorDescripcion() {
		return sPadreUorDescripcion;
	}
	public void setsPadreUorDescripcion(String sPadreUorDescripcion) {
		this.sPadreUorDescripcion = sPadreUorDescripcion;
	}
	public String getsHijoUorIndUltimoNivel() {
		return sHijoUorIndUltimoNivel;
	}
	public void setsHijoUorIndUltimoNivel(String sHijoUorIndUltimoNivel) {
		this.sHijoUorIndUltimoNivel = sHijoUorIndUltimoNivel;
	} 
}