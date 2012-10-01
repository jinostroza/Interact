package cl.solem.tyc.beans;

import java.io.Serializable;

public class Login implements Serializable {

	private static final long serialVersionUID = -6614525230779032827L;
	private String tipoLogueo;
	private String id;
	private String username;
	private String password;
	private String host;
	private String idioma;

	public String getTipoLogueo() {
		return tipoLogueo;
	}

	public void setTipoLogueo(String tipoLogueo) {
		this.tipoLogueo = tipoLogueo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIdioma() {
		return idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
}