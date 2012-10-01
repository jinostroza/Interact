package cl.solem.tyc.beans;

import java.io.Serializable;

public class Error implements Serializable {

	private static final long serialVersionUID = 6909988011990856771L;
	private long numFamiliaError = 0;
	private String numError = null;
	private String msjError = null;
	private String msjErrorUsuario = null;
	private String indDetalle = null;

	public Error(long numFamiliaError, String numError, String msjError, String msjErrorUsuario, String indDetalle) {
		super();
		this.numFamiliaError = numFamiliaError;
		this.numError = numError;
		this.msjError = msjError;
		this.msjErrorUsuario = msjErrorUsuario;
		this.indDetalle = indDetalle;
	}

	public Error(String numError, String msjError) {
		super();
		this.numError = numError;
		this.msjError = msjError;
	}

	public Error(long numFamiliaError, String numError, String msjError) {
		super();
		this.numFamiliaError = numFamiliaError;
		this.numError = numError;
		this.msjError = msjError;
	}

	public Error(long numFamiliaError, long numError, String msjError) {
		super();
		this.numFamiliaError = numFamiliaError;
		this.numError = String.valueOf(numError);
		this.msjError = msjError;
	}

	public Error(long numFamiliaError, String numError, String msjError, String msjErrorUsuario) {
		super();
		this.numFamiliaError = numFamiliaError;
		this.numError = numError;
		this.msjError = msjError;
		this.msjErrorUsuario = msjErrorUsuario;
	}

	public Error(long numFamiliaError, long numError, String msjError, String msjErrorUsuario) {
		super();
		this.numFamiliaError = numFamiliaError;
		this.numError = String.valueOf(numError);
		this.msjError = msjError;
		this.msjErrorUsuario = msjErrorUsuario;
	}

	public String getIndDetalle() {
		return indDetalle;
	}

	public void setIndDetalle(String indDetalle) {
		this.indDetalle = indDetalle;
	}

	public String getMsjError() {
		return msjError;
	}

	public void setMsjError(String msjError) {
		this.msjError = msjError;
	}

	public String getMsjErrorUsuario() {
		return msjErrorUsuario;
	}

	public void setMsjErrorUsuario(String msjErrorUsuario) {
		this.msjErrorUsuario = msjErrorUsuario;
	}

	public String getNumError() {
		return numError;
	}

	public void setNumError(String numError) {
		this.numError = numError;
	}

	public long getNumFamiliaError() {
		return numFamiliaError;
	}

	public void setNumFamiliaError(long numFamiliaError) {
		this.numFamiliaError = numFamiliaError;
	}
}