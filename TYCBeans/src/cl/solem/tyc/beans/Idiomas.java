package cl.solem.tyc.beans;

import java.io.Serializable;

public class Idiomas implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String sCodApAplicacion;
	private String sCodIdiomaAplicacion;
	private String sCodIdiomaSistema;
	private String sDescriptorIdioma;
	private long nIdParametro;
	private String sCodApSistema;
	private String sIndActivo;
		
	public Idiomas()
	{
	}

	public String getsCodApAplicacion() {
		return sCodApAplicacion;
	}

	public void setsCodApAplicacion(String sCodApAplicacion) {
		this.sCodApAplicacion = sCodApAplicacion;
	}

	public String getsCodIdiomaAplicacion() {
		return sCodIdiomaAplicacion;
	}

	public void setsCodIdiomaAplicacion(String sCodIdiomaAplicacion) {
		this.sCodIdiomaAplicacion = sCodIdiomaAplicacion;
	}

	public String getsCodIdiomaSistema() {
		return sCodIdiomaSistema;
	}

	public void setsCodIdiomaSistema(String sCodIdiomaSistema) {
		this.sCodIdiomaSistema = sCodIdiomaSistema;
	}

	public String getsDescriptorIdioma() {
		return sDescriptorIdioma;
	}

	public void setsDescriptorIdioma(String sDescriptorIdioma) {
		this.sDescriptorIdioma = sDescriptorIdioma;
	}

	public long getnIdParametro() {
		return nIdParametro;
	}

	public void setnIdParametro(long nIdParametro) {
		this.nIdParametro = nIdParametro;
	}

	public String getsCodApSistema() {
		return sCodApSistema;
	}

	public void setsCodApSistema(String sCodApSistema) {
		this.sCodApSistema = sCodApSistema;
	}

	public String getsIndActivo() {
		return sIndActivo;
	}

	public void setsIndActivo(String sIndActivo) {
		this.sIndActivo = sIndActivo;
	}
	
}
