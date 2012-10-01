package cl.solem.tyc.beans;

import java.io.Serializable;

public class Convenio implements Serializable {

	private static final long serialVersionUID = -6375411005842793717L;
	private long nIdConvenio;//llave 
	private long nIdPrestador;//cliente
	private long nIdCliente;
	private String sCodConvenio;//codigo
	private String sNombre;//nombre
	private String sNomPoliza;
	private String sCodClase;//clase
	private String sDesClase;
	private String sCodSubClase;
	private String sDesSubClase;
	private String sCodTipo; //Tipo Cliente
	private String sDesTipo;
	private String sDescripcion;//descripcion
	private String sIndPlantilla;//plantilla
	private String sIndConvenioAbierto;
	private String sIndUsoBecados;//uso becados
	private String sIndFacturaPrestador;
	private String sIndRecaudacion;
	private long nPorRecaudacion;
	private long nIdResponsable;//responsable
	private String sNomResponsable;
	private long nCupoConvenio;
	private String sFhoInicio;//fecha inicio
	private String sFhoTermino;//fecha termino
	private String sCodEstado;
	private String sDesEstado;
	private String sFecCreacion;
	private String sFecActualizacion;
	private long nIdCreador;
	private long nIdActualizador;
	private String sNomPrestador;
	private String sNomCliente;
	private String sIndVigencia;
	
	/* paginacion */
	private String sOrdenPor; /* orden por campo */
	private String tipoOrden; /* asc o desc */
	private int pagActual;
	private int cantRegPagina;
	private int totalRegistros;
	
	/* RED */
	
	private long nIdRed;
	
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
	public String getsNomPoliza() {
		return sNomPoliza;
	}
	public void setsNomPoliza(String sNomPoliza) {
		this.sNomPoliza = sNomPoliza;
	}
	public String getsIndRecaudacion() {
		return sIndRecaudacion;
	}
	public void setsIndRecaudacion(String sIndRecaudacion) {
		this.sIndRecaudacion = sIndRecaudacion;
	}
	public long getnIdCliente() {
		return nIdCliente;
	}
	public void setnIdCliente(long nIdCliente) {
		this.nIdCliente = nIdCliente;
	}
	public String getsDesEstado() {
		return sDesEstado;
	}
	public void setsDesEstado(String sDesEstado) {
		this.sDesEstado = sDesEstado;
	}

	public long getnIdConvenio() {
		return nIdConvenio;
	}
	public void setnIdConvenio(long nIdConvenio) {
		this.nIdConvenio = nIdConvenio;
	}
	public long getnIdPrestador() {
		return nIdPrestador;
	}
	public void setnIdPrestador(long nIdPrestador) {
		this.nIdPrestador = nIdPrestador;
	}
	public String getsCodConvenio() {
		return sCodConvenio;
	}
	public void setsCodConvenio(String sCodConvenio) {
		this.sCodConvenio = sCodConvenio;
	}
	public String getsNombre() {
		return sNombre;
	}
	public void setsNombre(String sNombre) {
		this.sNombre = sNombre;
	}
	public String getsCodClase() {
		return sCodClase;
	}
	public void setsCodClase(String sCodClase) {
		this.sCodClase = sCodClase;
	}
	public String getsDesClase() {
		return sDesClase;
	}
	public void setsDesClase(String sDesClase) {
		this.sDesClase = sDesClase;
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
	public String getsIndUsoBecados() {
		return sIndUsoBecados;
	}
	public void setsIndUsoBecados(String sIndUsoBecados) {
		this.sIndUsoBecados = sIndUsoBecados;
	}
	public long getnIdResponsable() {
		return nIdResponsable;
	}
	public void setnIdResponsable(long nIdResponsable) {
		this.nIdResponsable = nIdResponsable;
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
	public long getnIdRed() {
		return nIdRed;
	}
	public void setnIdRed(long nIdRed) {
		this.nIdRed = nIdRed;
	}
	public String getsCodSubClase() {
		return sCodSubClase;
	}
	public void setsCodSubClase(String sCodSubClase) {
		this.sCodSubClase = sCodSubClase;
	}
	public String getsDesSubClase() {
		return sDesSubClase;
	}
	public void setsDesSubClase(String sDesSubClase) {
		this.sDesSubClase = sDesSubClase;
	}
	public String getsIndConvenioAbierto() {
		return sIndConvenioAbierto;
	}
	public void setsIndConvenioAbierto(String sIndConvenioAbierto) {
		this.sIndConvenioAbierto = sIndConvenioAbierto;
	}
	public String getsIndFacturaPrestador() {
		return sIndFacturaPrestador;
	}
	public void setsIndFacturaPrestador(String sIndFacturaPrestador) {
		this.sIndFacturaPrestador = sIndFacturaPrestador;
	}
	public long getnPorRecaudacion() {
		return nPorRecaudacion;
	}
	public void setnPorRecaudacion(long nPorRecaudacion) {
		this.nPorRecaudacion = nPorRecaudacion;
	}
	public long getnCupoConvenio() {
		return nCupoConvenio;
	}
	public void setnCupoConvenio(long nCupoConvenio) {
		this.nCupoConvenio = nCupoConvenio;
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
	public String getsFecActualizacion() {
		return sFecActualizacion;
	}
	public void setsFecActualizacion(String sFecActualizacion) {
		this.sFecActualizacion = sFecActualizacion;
	}
	public long getnIdCreador() {
		return nIdCreador;
	}
	public void setnIdCreador(long nIdCreador) {
		this.nIdCreador = nIdCreador;
	}
	public long getnIdActualizador() {
		return nIdActualizador;
	}
	public void setnIdActualizador(long nIdActualizador) {
		this.nIdActualizador = nIdActualizador;
	}
	public String getsIndVigencia() {
		return sIndVigencia;
	}
	public void setsIndVigencia(String sIndVigencia) {
		this.sIndVigencia = sIndVigencia;
	}
	public String getsNomCliente() {
		return sNomCliente;
	}
	public void setsNomCliente(String sNomCliente) {
		this.sNomCliente = sNomCliente;
	}
	
}