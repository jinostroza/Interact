package cl.solem.tyc.beans;

import java.io.Serializable;

public class CatalogoPrest implements Serializable {

	private static final long serialVersionUID = 4186506091339400923L;

	/* Catalogo_Prestacion */
	
	private long nIdCatalogoPrestacion;
	private long nIdCatalogo;
	private long nIdPrestacion;
	private String sCodPrestacion;
	private String sNomCatalogo;
	private String sNomPrestacion;
	private String sNomPrestCatalogo;
	private String sIndCargoAutomatico;
	private long nPorGastos;
	private long nPorHonorarios;
	private String sIndVigencia;
	private String sFecCreacion;
	private long nIdCreador;
	private String sFecActualizacion;
	private long nIdActualizador;
	private String sIndTipoPago;
	private String sNombreCatalogo;
	
	/* Paginacion */
	
	private String sOrdenPor; /* orden por campo */
	private String tipoOrden; /* asc o desc */
	private int pagActual;
	private int cantRegPagina;
	private int totalRegistros;
	
			/* Atributos */
	
	public long getnIdCatalogoPrestacion() {
		return nIdCatalogoPrestacion;
	}
	public void setnIdCatalogoPrestacion(long nIdCatalogoPrestacion) {
		this.nIdCatalogoPrestacion = nIdCatalogoPrestacion;
	}
	public long getnIdCatalogo() {
		return nIdCatalogo;
	}
	public void setnIdCatalogo(long nIdCatalogo) {
		this.nIdCatalogo = nIdCatalogo;
	}
	public long getnIdPrestacion() {
		return nIdPrestacion;
	}
	public void setnIdPrestacion(long nIdPrestacion) {
		this.nIdPrestacion = nIdPrestacion;
	}
	public String getsCodPrestacion() {
		return sCodPrestacion;
	}
	public void setsCodPrestacion(String sCodPrestacion) {
		this.sCodPrestacion = sCodPrestacion;
	}
	public String getsNomPrestacion() {
		return sNomPrestacion;
	}
	public void setsNomPrestacion(String sNomPrestacion) {
		this.sNomPrestacion = sNomPrestacion;
	}
	public String getsNomPrestCatalogo() {
		return sNomPrestCatalogo;
	}
	public void setsNomPrestCatalogo(String sNomPrestCatalogo) {
		this.sNomPrestCatalogo = sNomPrestCatalogo;
	}
	public String getsIndCargoAutomatico() {
		return sIndCargoAutomatico;
	}
	public void setsIndCargoAutomatico(String sIndCargoAutomatico) {
		this.sIndCargoAutomatico = sIndCargoAutomatico;
	}
	
	public long getnPorGastos() {
		return nPorGastos;
	}
	public void setnPorGastos(long nPorGastos) {
		this.nPorGastos = nPorGastos;
	}
	public long getnPorHonorarios() {
		return nPorHonorarios;
	}
	public void setnPorHonorarios(long nPorHonorarios) {
		this.nPorHonorarios = nPorHonorarios;
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
	public String getsIndTipoPago() {
		return sIndTipoPago;
	}
	public void setsIndTipoPago(String sIndTipoPago) {
		this.sIndTipoPago = sIndTipoPago;
	}
	public String getsNombreCatalogo() {
		return sNombreCatalogo;
	}
	public void setsNombreCatalogo(String sNombreCatalogo) {
		this.sNombreCatalogo = sNombreCatalogo;
	}
	public String getsNomCatalogo() {
		return sNomCatalogo;
	}
	public void setsNomCatalogo(String sNomCatalogo) {
		this.sNomCatalogo = sNomCatalogo;
	}
	
}