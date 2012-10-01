package cl.solem.tyc.beans;

import java.io.Serializable;

public class Prestacion implements Serializable {

	private static final long serialVersionUID = -2804312323395768855L;

	/* Prestacion */

	private long nIdPrestacion;
	private long nIdResponsable;
	private String sCodPrestacion;
	private String sNombre;
	private String sDescripcion;
	private String sCodTipoPrestacion;
	private String sDesTipoPrestacion;
	private String sCodSubTipoPrestacion;
	private String sCodUnidadMedida;
	private String sIndCerrada;
	private String sIndCompuesta;
	private String sCodEstado;
	private String sDesEstado;
	private String sIndVigencia;
	private String sIndicador;
	private long nIdCreador;
	private String FecActualizacion;
	private long nIdActualizador;
	private long nCantidad;
	private long nIdPrestPadre;
	private String sFhoInicio;
	private String sFhoTermino;
	private String sFecCreacion;
	private String sFecModificacion;
	

	public long getnIdPrestacion() {
		return nIdPrestacion;
	}

	public void setnIdPrestacion(long nIdPrestacion) {
		this.nIdPrestacion = nIdPrestacion;
	}

	public long getnIdResponsable() {
		return nIdResponsable;
	}

	public void setnIdResponsable(long nIdResponsable) {
		this.nIdResponsable = nIdResponsable;
	}

	public String getsCodPrestacion() {
		return sCodPrestacion;
	}

	public void setsCodPrestacion(String sCodPrestacion) {
		this.sCodPrestacion = sCodPrestacion;
	}

	public String getsNombre() {
		return sNombre;
	}

	public void setsNombre(String sNombre) {
		this.sNombre = sNombre;
	}

	public String getsDescripcion() {
		return sDescripcion;
	}

	public void setsDescripcion(String sDescripcion) {
		this.sDescripcion = sDescripcion;
	}

	public String getsCodTipoPrestacion() {
		return sCodTipoPrestacion;
	}

	public void setsCodTipoPrestacion(String sCodTipoPrestacion) {
		this.sCodTipoPrestacion = sCodTipoPrestacion;
	}

	public String getsCodSubTipoPrestacion() {
		return sCodSubTipoPrestacion;
	}

	public void setsCodSubTipoPrestacion(String sCodSubTipoPrestacion) {
		this.sCodSubTipoPrestacion = sCodSubTipoPrestacion;
	}

	public String getsCodUnidadMedida() {
		return sCodUnidadMedida;
	}

	public void setsCodUnidadMedida(String sCodUnidadMedida) {
		this.sCodUnidadMedida = sCodUnidadMedida;
	}

	public String getsIndCerrada() {
		return sIndCerrada;
	}

	public void setsIndCerrada(String sIndCerrada) {
		this.sIndCerrada = sIndCerrada;
	}

	public String getsIndCompuesta() {
		return sIndCompuesta;
	}

	public void setsIndCompuesta(String sIndCompuesta) {
		this.sIndCompuesta = sIndCompuesta;
	}

	public String getsCodEstado() {
		return sCodEstado;
	}

	public void setsCodEstado(String sCodEstado) {
		this.sCodEstado = sCodEstado;
	}

	public String getsDesEstado() {
		return sDesEstado;
	}

	public void setsDesEstado(String sDesEstado) {
		this.sDesEstado = sDesEstado;
	}

	public String getsIndVigencia() {
		return sIndVigencia;
	}

	public void setsIndVigencia(String sIndVigencia) {
		this.sIndVigencia = sIndVigencia;
	}

	public long getnIdCreador() {
		return nIdCreador;
	}

	public void setnIdCreador(long nIdCreador) {
		this.nIdCreador = nIdCreador;
	}

	public String getFecActualizacion() {
		return FecActualizacion;
	}

	public void setFecActualizacion(String fecActualizacion) {
		FecActualizacion = fecActualizacion;
	}

	public long getnIdActualizador() {
		return nIdActualizador;
	}

	public void setnIdActualizador(long nIdActualizador) {
		this.nIdActualizador = nIdActualizador;
	}

	public long getnCantidad() {
		return nCantidad;
	}

	public void setnCantidad(long nCantidad) {
		this.nCantidad = nCantidad;
	}

	public long getnIdPrestPadre() {
		return nIdPrestPadre;
	}

	public void setnIdPrestPadre(long nIdPrestPadre) {
		this.nIdPrestPadre = nIdPrestPadre;
	}

	public String getsFhoInicio() {
		return sFhoInicio;
	}

	public void setsFhoInicio(String sFhoInicio) {
		this.sFhoInicio = sFhoInicio;
	}

	public String getsFhoTermino() {
		return sFhoTermino;
	}

	public void setsFhoTermino(String sFhoTermino) {
		this.sFhoTermino = sFhoTermino;
	}
	
	public String getsFecCreacion() {
       return sFecCreacion;
    }

	public void setsFecCreacion(String sFecCreacion) {
       this.sFecCreacion = sFecCreacion;
	}

	public String getsFecModificacion() {
		return sFecModificacion;
	}

	public void setsFecModificacion(String sFecModificacion) {
		this.sFecModificacion = sFecModificacion;
	}

	public String getsDesTipoPrestacion() {
		return sDesTipoPrestacion;
	}

	public void setsDesTipoPrestacion(String sDesTipoPrestacion) {
		this.sDesTipoPrestacion = sDesTipoPrestacion;
	}

	public String getsIndicador() {
		return sIndicador;
	}

	public void setsIndicador(String sIndicador) {
		this.sIndicador = sIndicador;
	}
	
}