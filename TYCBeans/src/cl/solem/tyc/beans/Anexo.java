package cl.solem.tyc.beans;

import java.io.Serializable;

public class Anexo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public long nIdAnexo;
	public long nIdConvenio;
	public String sNombreConvenio;
	public String sNombreAnexo;
	public String sDescripcion;
	public String sFhoInicio;
	public String sFhoTermino;
	public String sCodEstado;
	public String sDesEstado;
	public String sIndVigencia;
	public String sFecCreacion;
	public long nIdCreador;
	public String sFecActualizacion;
	public long nIdActualizador;
	
	/* paginacion */
	private String sOrdenPor; /* orden por campo */
	private String tipoOrden; /* asc o desc */
	private int pagActual;
	private int cantRegPagina;
	private int totalRegistros;
	
	public long getnIdAnexo() {
		return nIdAnexo;
	}
	public void setnIdAnexo(long nIdAnexo) {
		this.nIdAnexo = nIdAnexo;
	}
	public long getnIdConvenio() {
		return nIdConvenio;
	}
	public void setnIdConvenio(long nIdConvenio) {
		this.nIdConvenio = nIdConvenio;
	}
	public String getsNombreAnexo() {
		return sNombreAnexo;
	}
	public void setsNombreAnexo(String sNombreAnexo) {
		this.sNombreAnexo = sNombreAnexo;
	}
	public String getsDescripcion() {
		return sDescripcion;
	}
	public void setsDescripcion(String sDescripcion) {
		this.sDescripcion = sDescripcion;
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
	public String getsCodEstado() {
		return sCodEstado;
	}
	public void setsCodEstado(String sCodEstado) {
		this.sCodEstado = sCodEstado;
	}
	public String getsIndVigencia() {
		return sIndVigencia;
	}
	public void setsIndVigencia(String sIndVigencia) {
		this.sIndVigencia = sIndVigencia;
	}
	public String getsFecCreacion() {
		return sFecCreacion;
	}
	public void setsFecCreacion(String sFecCreacion) {
		this.sFecCreacion = sFecCreacion;
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
	public String getsOrdenPor() {
		return sOrdenPor;
	}
	public void setsOrdenPor(String sOrdenPor) {
		this.sOrdenPor = sOrdenPor;
	}
	public String getTipoOrden() {
		return tipoOrden;
	}
	public void setTipoOrden(String tipoOrden) {
		this.tipoOrden = tipoOrden;
	}
	public int getPagActual() {
		return pagActual;
	}
	public void setPagActual(int pagActual) {
		this.pagActual = pagActual;
	}
	public int getCantRegPagina() {
		return cantRegPagina;
	}
	public void setCantRegPagina(int cantRegPagina) {
		this.cantRegPagina = cantRegPagina;
	}
	public int getTotalRegistros() {
		return totalRegistros;
	}
	public void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;
	}
	public String getsDesEstado() {
		return sDesEstado;
	}
	public void setsDesEstado(String sDesEstado) {
		this.sDesEstado = sDesEstado;
	}
	public String getsNombreConvenio() {
		return sNombreConvenio;
	}
	public void setsNombreConvenio(String sNombreConvenio) {
		this.sNombreConvenio = sNombreConvenio;
	}
	public long getnIdCreador() {
		return nIdCreador;
	}
	public void setnIdCreador(long nIdCreador) {
		this.nIdCreador = nIdCreador;
	}
	
}
