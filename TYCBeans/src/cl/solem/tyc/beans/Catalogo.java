package cl.solem.tyc.beans;

import java.io.Serializable;

public class Catalogo implements Serializable {

	private static final long serialVersionUID = -215772522459416065L;

	private long  nIdCatalogo;
	private long nIdPrestador;
	private String sCodCatalogo;
	private String sNombre;
	private String sDescripcion;
	private String sIndPlantilla;
	private String sIndPublico;
	private String sFecHoInicio;
	private String sFecHoTermino;
	private long nIdResponsable;
	private String sNomResponsable;
	private String sCodEstado;
	private String sDesEstado;
	private String sIndVigencia;
	private String sFecCreacion;
	private long nIdCreador;
	private String sFecActualizacion;
	private long nIdActualizador;
	private String sNomPrestador;
	
	/* Paginacion */
	private String sOrdenPor; /* orden por campo */
	private String tipoOrden; /* asc o desc */
	private int pagActual;
	private int cantRegPagina;
	private int totalRegistros;
	
	/* Empresa*/
	
	private String sNombreFantasia;

	public long getnIdCatalogo() {
		return nIdCatalogo;
	}

	public void setnIdCatalogo(long nIdCatalogo) {
		this.nIdCatalogo = nIdCatalogo;
	}

	public long getnIdPrestador() {
		return nIdPrestador;
	}

	public void setnIdPrestador(long nIdPrestador) {
		this.nIdPrestador = nIdPrestador;
	}

	public String getsCodCatalogo() {
		return sCodCatalogo;
	}

	public void setsCodCatalogo(String sCodCatalogo) {
		this.sCodCatalogo = sCodCatalogo;
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

	public String getsIndPlantilla() {
		return sIndPlantilla;
	}

	public void setsIndPlantilla(String sIndPlantilla) {
		this.sIndPlantilla = sIndPlantilla;
	}

	public String getsIndPublico() {
		return sIndPublico;
	}

	public void setsIndPublico(String sIndPublico) {
		this.sIndPublico = sIndPublico;
	}

	public String getsFecHoInicio() {
		return sFecHoInicio;
	}

	public void setsFecHoInicio(String sFecHoInicio) {
		this.sFecHoInicio = sFecHoInicio;
	}

	public String getsFecHoTermino() {
		return sFecHoTermino;
	}

	public void setsFecHoTermino(String sFecHoTermino) {
		this.sFecHoTermino = sFecHoTermino;
	}

	public long getnIdResponsable() {
		return nIdResponsable;
	}

	public void setnIdResponsable(long nIdResponsable) {
		this.nIdResponsable = nIdResponsable;
	}

	public String getsNomResponsable() {
		return sNomResponsable;
	}

	public void setsNomResponsable(String sNomResponsable) {
		this.sNomResponsable = sNomResponsable;
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

	public String getsNomPrestador() {
		return sNomPrestador;
	}

	public void setsNomPrestador(String sNomPrestador) {
		this.sNomPrestador = sNomPrestador;
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

	public String getsNombreFantasia() {
		return sNombreFantasia;
	}

	public void setsNombreFantasia(String sNombreFantasia) {
		this.sNombreFantasia = sNombreFantasia;
	}
	
	/* Atributos */
	

	
	
}