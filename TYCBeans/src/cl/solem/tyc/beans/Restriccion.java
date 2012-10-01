package cl.solem.tyc.beans;

import java.io.Serializable;

public class Restriccion implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long nIdRestriccion;
	private long nIdConvenio;
	private String sNombre;
	private String sFhoInicio;
	private String sFhoFin;
	private String sCodTipo;
	private String sDesTipo;
	private String sValor;
	private String sRango1;
	private String sRango2;
	private String sFhoCreacion;
	private long nIdUsuarioCreacion;
	private String sFhoModificacion;
	private long nIdUsuarioModificacion;
	private String sIndVigencia;
	
	public long getnIdRestriccion() {
		return nIdRestriccion;
	}
	public void setnIdRestriccion(long nIdRestriccion) {
		this.nIdRestriccion = nIdRestriccion;
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
	public String getsValor() {
		return sValor;
	}
	public void setsValor(String sValor) {
		this.sValor = sValor;
	}
	public String getsRango1() {
		return sRango1;
	}
	public void setsRango1(String sRango1) {
		this.sRango1 = sRango1;
	}
	public String getsRango2() {
		return sRango2;
	}
	public void setsRango2(String sRango2) {
		this.sRango2 = sRango2;
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
	public String getsIndVigencia() {
		return sIndVigencia;
	}
	public void setsIndVigencia(String sIndVigencia) {
		this.sIndVigencia = sIndVigencia;
	}
	
}
