package cl.solem.tyc.beans;

import java.io.Serializable;

public class ConvenioAnexoDetalle implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long nIdConvDetalle;
	private long nIdAnexo;
	private long nIdPrestacion;
	private long nIdConvenio;
	private long nIdCatalogo;
	private long nIdCatalogoPrestacion;
	private String sCodPrestacion;
	private String sCodPrestacionConvenio;
	private String sNombrePrestacion;
	private String sNombrePrestCatalogo;
	private String sNombrePrestConvenio;
	private String sIndCoSeguro;
	private String sCodPeriodoEve;
	private long nCantEventos;
	private String sCodPeriodoCup;
	private long nMontoOcupacion;
	private String sIndTipoPago;
	private long nTopePrecio;
	private long nCantCapacitacion;
	private long nPorCobertura;
	private String sFecCreacion;
	private long nIdCreador;
	private String sFecActualizacion;
	private long nIdActualizador;
	private String sNombreConvenio;
	private String sNombreAnexo;
	
	/* paginacion */
	private String sOrdenPor; /* orden por campo */
	private String tipoOrden; /* asc o desc */
	private int pagActual;
	private int cantRegPagina;
	private int totalRegistros;
	
	
	public long getnIdConvDetalle() {
		return nIdConvDetalle;
	}
	public void setnIdConvDetalle(long nIdConvDetalle) {
		this.nIdConvDetalle = nIdConvDetalle;
	}
	public long getnIdAnexo() {
		return nIdAnexo;
	}
	public void setnIdAnexo(long nIdAnexo) {
		this.nIdAnexo = nIdAnexo;
	}
	public long getnIdPrestacion() {
		return nIdPrestacion;
	}
	public void setnIdPrestacion(long nIdPrestacion) {
		this.nIdPrestacion = nIdPrestacion;
	}
	public long getnIdConvenio() {
		return nIdConvenio;
	}
	public void setnIdConvenio(long nIdConvenio) {
		this.nIdConvenio = nIdConvenio;
	}
	public String getsCodPrestacion() {
		return sCodPrestacion;
	}
	public void setsCodPrestacion(String sCodPrestacion) {
		this.sCodPrestacion = sCodPrestacion;
	}
	public String getsNombrePrestacion() {
		return sNombrePrestacion;
	}
	public void setsNombrePrestacion(String sNombrePrestacion) {
		this.sNombrePrestacion = sNombrePrestacion;
	}
	public String getsNombrePrestCatalogo() {
		return sNombrePrestCatalogo;
	}
	public void setsNombrePrestCatalogo(String sNombrePrestCatalogo) {
		this.sNombrePrestCatalogo = sNombrePrestCatalogo;
	}
	public String getsIndCoSeguro() {
		return sIndCoSeguro;
	}
	public void setsIndCoSeguro(String sIndCoSeguro) {
		this.sIndCoSeguro = sIndCoSeguro;
	}
	public String getsCodPeriodoEve() {
		return sCodPeriodoEve;
	}
	public void setsCodPeriodoEve(String sCodPeriodoEve) {
		this.sCodPeriodoEve = sCodPeriodoEve;
	}
	public long getnCantEventos() {
		return nCantEventos;
	}
	public void setnCantEventos(long nCantEventos) {
		this.nCantEventos = nCantEventos;
	}
	public String getsCodPeriodoCup() {
		return sCodPeriodoCup;
	}
	public void setsCodPeriodoCup(String sCodPeriodoCup) {
		this.sCodPeriodoCup = sCodPeriodoCup;
	}
	public long getnMontoOcupacion() {
		return nMontoOcupacion;
	}
	public void setnMontoOcupacion(long nMontoOcupacion) {
		this.nMontoOcupacion = nMontoOcupacion;
	}
	public String getsIndTipoPago() {
		return sIndTipoPago;
	}
	public void setsIndTipoPago(String sIndTipoPago) {
		this.sIndTipoPago = sIndTipoPago;
	}
	public long getnTopePrecio() {
		return nTopePrecio;
	}
	public void setnTopePrecio(long nTopePrecio) {
		this.nTopePrecio = nTopePrecio;
	}
	public long getnCantCapacitacion() {
		return nCantCapacitacion;
	}
	public void setnCantCapacitacion(long nCantCapacitacion) {
		this.nCantCapacitacion = nCantCapacitacion;
	}
	public long getnPorCobertura() {
		return nPorCobertura;
	}
	public void setnPorCobertura(long nPorCobertura) {
		this.nPorCobertura = nPorCobertura;
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
	public String getsNombreConvenio() {
		return sNombreConvenio;
	}
	public void setsNombreConvenio(String sNombreConvenio) {
		this.sNombreConvenio = sNombreConvenio;
	}
	public String getsNombreAnexo() {
		return sNombreAnexo;
	}
	public void setsNombreAnexo(String sNombreAnexo) {
		this.sNombreAnexo = sNombreAnexo;
	}
	public long getnIdCatalogo() {
		return nIdCatalogo;
	}
	public void setnIdCatalogo(long nIdCatalogo) {
		this.nIdCatalogo = nIdCatalogo;
	}
	public long getnIdCatalogoPrestacion() {
		return nIdCatalogoPrestacion;
	}
	public void setnIdCatalogoPrestacion(long nIdCatalogoPrestacion) {
		this.nIdCatalogoPrestacion = nIdCatalogoPrestacion;
	}
	public String getsCodPrestacionConvenio() {
		return sCodPrestacionConvenio;
	}
	public void setsCodPrestacionConvenio(String sCodPrestacionConvenio) {
		this.sCodPrestacionConvenio = sCodPrestacionConvenio;
	}
	public String getsNombrePrestConvenio() {
		return sNombrePrestConvenio;
	}
	public void setsNombrePrestConvenio(String sNombrePrestConvenio) {
		this.sNombrePrestConvenio = sNombrePrestConvenio;
	}
	
}
