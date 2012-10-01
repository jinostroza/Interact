package cl.solem.tyc.beans;

import java.io.Serializable;

public class Aplicacion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String sCodAplicacion;
	private String sNomAplicacion;
	private String sDescripcion;
	private String sIndVigencia;
	private String dFecCreacion;
	private long nIdCreador;
	private String dFecActualizacion;
	private long nIdActualizador;
	private String sIdiomaEsp;
	private String sIdiomaIng;
	private String sIdiomaFra;
	private String sIdiomaPor;
	private String sIdiomaAle;
	private String sIdiomaIta;
	
	
	public Aplicacion()
	{
	}
	
	public Aplicacion(String sCodAplicacion)
	{
		this.sCodAplicacion = sCodAplicacion;
	}
	
	public String getsCodAplicacion() {
		return sCodAplicacion;
	}
	public void setsCodAplicacion(String sCodAplicacion) {
		this.sCodAplicacion = sCodAplicacion;
	}
	public String getsNomAplicacion() {
		return sNomAplicacion;
	}
	public void setsNomAplicacion(String sNomAplicacion) {
		this.sNomAplicacion = sNomAplicacion;
	}
	public String getsDescripcion() {
		return sDescripcion;
	}
	public void setsDescripcion(String sDescripcion) {
		this.sDescripcion = sDescripcion;
	}
	public String getsIndVigencia() {
		return sIndVigencia;
	}
	public void setsIndVigencia(String sIndVigencia) {
		this.sIndVigencia = sIndVigencia;
	}
	public String getdFecCreacion() {
		return dFecCreacion;
	}
	public void setdFecCreacion(String dFecCreacion) {
		this.dFecCreacion = dFecCreacion;
	}
	public long getnIdCreador() {
		return nIdCreador;
	}
	public void setnIdCreador(long nIdCreador) {
		this.nIdCreador = nIdCreador;
	}
	public String getdFecActualizacion() {
		return dFecActualizacion;
	}
	public void setdFecActualizacion(String dFecActualizacion) {
		this.dFecActualizacion = dFecActualizacion;
	}
	public long getnIdActualizador() {
		return nIdActualizador;
	}
	public void setnIdActualizador(long nIdActualizador) {
		this.nIdActualizador = nIdActualizador;
	}

	public String getsIdiomaEsp() {
		return sIdiomaEsp;
	}

	public void setsIdiomaEsp(String sIdiomaEsp) {
		this.sIdiomaEsp = sIdiomaEsp;
	}

	public String getsIdiomaIng() {
		return sIdiomaIng;
	}

	public void setsIdiomaIng(String sIdiomaIng) {
		this.sIdiomaIng = sIdiomaIng;
	}

	public String getsIdiomaFra() {
		return sIdiomaFra;
	}

	public void setsIdiomaFra(String sIdiomaFra) {
		this.sIdiomaFra = sIdiomaFra;
	}

	public String getsIdiomaPor() {
		return sIdiomaPor;
	}

	public void setsIdiomaPor(String sIdiomaPor) {
		this.sIdiomaPor = sIdiomaPor;
	}

	public String getsIdiomaAle() {
		return sIdiomaAle;
	}

	public void setsIdiomaAle(String sIdiomaAle) {
		this.sIdiomaAle = sIdiomaAle;
	}

	public String getsIdiomaIta() {
		return sIdiomaIta;
	}

	public void setsIdiomaIta(String sIdiomaIta) {
		this.sIdiomaIta = sIdiomaIta;
	}

}
