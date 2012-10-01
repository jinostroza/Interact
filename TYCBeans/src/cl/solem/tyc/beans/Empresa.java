package cl.solem.tyc.beans;

import java.io.Serializable;

public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long nIdEmpresa;
	private String sRut;
	private String sRazonSocial;
	private String sDescripcion;
	private String sNombreFantasia;
	private String sAlias;
	private String sCodEmpresa;
	private String sIndEmpresaCliente;
	private String sIndEmpresaSalud;
	private String sIndPrestador;
	private String sCodCategoria;
	private String sCodActividadComercial;
	private String sCodCobranza;
	private String sCodTipoCliente;
	private long nCreditoPermitido;
	private long nIdEjecutivoResponsable;
	private String sDireccion;
	private String sCodComuna;
	private String sCodRegion;
	private String sCodPais;
	private String sTelefono1;
	private String sTelefono2;
	private String sFax;
	private String sCodEstado;
	private String sIndVigencia;
	private String sFecCreacion;
	private long nIdUsuario;
	private String sFecModificacion;
	private long nIdActualizador;
	
	/* paginacion */
	private String sOrdenPor; /* orden por campo */
	private String tipoOrden; /* asc o desc */
	private int pagActual;
	private int cantRegPagina;
	private int totalRegistros;
	
	/* RED */
	
	private long nIdRed;
	
	public Empresa(){
	}
	
	public Empresa(long nIdEmpresa){  //Más otros campos que se puedan necesitar 
		this.nIdEmpresa=nIdEmpresa;
	}

	public long getnIdEmpresa() {
		return nIdEmpresa;
	}
	public void setnIdEmpresa(long nIdEmpresa) {
		this.nIdEmpresa = nIdEmpresa;
	}
	public String getsRut() {
		return sRut;
	}
	public void setsRut(String sRut) {
		this.sRut = sRut;
	}
	public String getsRazonSocial() {
		return sRazonSocial;
	}
	public void setsRazonSocial(String sRazonSocial) {
		this.sRazonSocial = sRazonSocial;
	}
	public String getsDescripcion() {
		return sDescripcion;
	}
	public void setsDescripcion(String sDescripcion) {
		this.sDescripcion = sDescripcion;
	}
	public String getsNombreFantasia() {
		return sNombreFantasia;
	}
	public void setsNombreFantasia(String sNombreFantasia) {
		this.sNombreFantasia = sNombreFantasia;
	}
	public String getsAlias() {
		return sAlias;
	}
	public void setsAlias(String sAlias) {
		this.sAlias = sAlias;
	}
	public String getsCodEmpresa() {
		return sCodEmpresa;
	}
	public void setsCodEmpresa(String sCodEmpresa) {
		this.sCodEmpresa = sCodEmpresa;
	}
	public String getsIndEmpresaCliente() {
		return sIndEmpresaCliente;
	}
	public void setsIndEmpresaCliente(String sIndEmpresaCliente) {
		this.sIndEmpresaCliente = sIndEmpresaCliente;
	}
	public String getsIndEmpresaSalud() {
		return sIndEmpresaSalud;
	}
	public void setsIndEmpresaSalud(String sIndEmpresaSalud) {
		this.sIndEmpresaSalud = sIndEmpresaSalud;
	}
	public String getsIndPrestador() {
		return sIndPrestador;
	}
	public void setsIndPrestador(String sIndPrestador) {
		this.sIndPrestador = sIndPrestador;
	}
	public String getsCodCategoria() {
		return sCodCategoria;
	}
	public void setsCodCategoria(String sCodCategoria) {
		this.sCodCategoria = sCodCategoria;
	}
	public String getsCodActividadComercial() {
		return sCodActividadComercial;
	}
	public void setsCodActividadComercial(String sCodActividadComercial) {
		this.sCodActividadComercial = sCodActividadComercial;
	}
	public String getsCodCobranza() {
		return sCodCobranza;
	}
	public void setsCodCobranza(String sCodCobranza) {
		this.sCodCobranza = sCodCobranza;
	}
	public String getsCodTipoCliente() {
		return sCodTipoCliente;
	}
	public void setsCodTipoCliente(String sCodTipoCliente) {
		this.sCodTipoCliente = sCodTipoCliente;
	}
	public long getnCreditoPermitido() {
		return nCreditoPermitido;
	}
	public void setnCreditoPermitido(long nCreditoPermitido) {
		this.nCreditoPermitido = nCreditoPermitido;
	}
	public long getnIdEjecutivoResponsable() {
		return nIdEjecutivoResponsable;
	}
	public void setnIdEjecutivoResponsable(long nIdEjecutivoResponsable) {
		this.nIdEjecutivoResponsable = nIdEjecutivoResponsable;
	}
	public String getsDireccion() {
		return sDireccion;
	}
	public void setsDireccion(String sDireccion) {
		this.sDireccion = sDireccion;
	}
	public String getsCodComuna() {
		return sCodComuna;
	}
	public void setsCodComuna(String sCodComuna) {
		this.sCodComuna = sCodComuna;
	}
	public String getsCodRegion() {
		return sCodRegion;
	}
	public void setsCodRegion(String sCodRegion) {
		this.sCodRegion = sCodRegion;
	}
	public String getsCodPais() {
		return sCodPais;
	}
	public void setsCodPais(String sCodPais) {
		this.sCodPais = sCodPais;
	}
	public String getsTelefono1() {
		return sTelefono1;
	}
	public void setsTelefono1(String sTelefono1) {
		this.sTelefono1 = sTelefono1;
	}
	public String getsTelefono2() {
		return sTelefono2;
	}
	public void setsTelefono2(String sTelefono2) {
		this.sTelefono2 = sTelefono2;
	}
	public String getsFax() {
		return sFax;
	}
	public void setsFax(String sFax) {
		this.sFax = sFax;
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
	public long getnIdUsuario() {
		return nIdUsuario;
	}
	public void setnIdUsuario(long nIdUsuario) {
		this.nIdUsuario = nIdUsuario;
	}
	public String getsFecModificacion() {
		return sFecModificacion;
	}
	public void setsFecModificacion(String sFecModificacion) {
		this.sFecModificacion = sFecModificacion;
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

	public long getnIdRed() {
		return nIdRed;
	}

	public void setnIdRed(long nIdRed) {
		this.nIdRed = nIdRed;
	}
	
}
