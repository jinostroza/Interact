package cl.solem.tyc.beans;

import java.io.Serializable;

import cl.solem.tyc.beans.Perfil;
import cl.solem.tyc.beans.Aplicacion;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 4982528384624023837L;

	public Usuario() {
	}

	private long nIdUsuario;
	private String sRunUsuario;
	private String sNombre;
	private String sApePaterno;
	private String sApeMaterno;
	private String sNomCompleto;
	private String sNombreUsuario;
	private String sContrasena;
	private String sNickName;
	private String sIniciales;
	private String sFecNacimiento;
	private String sCodGenero;
	private String sCodNacionalidad;
	private String sCodEstadoCivil;
	private String sCodCategoria;
	private String sNumColegiado;
	private String sNumProfesional;
	private String sCodProfesion;
	private String sDesProfesion;
	private String sCodCargo;
	private String sDesCargo;
	private String sTelefonoFijo;
	private String sTelefonoMovil;
	private String sEmail;
	private String sEmailLaboral;
	private String sDireccion;
	private String sCodComuna;
	private String sCodRegion;
	private String sCodPais;
	private String sClaveAcceso;
	private String sContrasena1;
	private String sContrasena2;
	private String dFecContrasena;
	private String dFecLogueo;
	private long nCantIntentos;
	private String sCodIdiomaPreferente;
	private String sCodEstado;
	private String sIndVigencia;
	private String dFecCreacion;
	private long nIdCreador;
	private String dFecActualizacion;
	private long nIdActualizador;
	private String sIndEstado;
	private String sCodAplicacion;
	private String sDesAplicacion;
	private String sCodPerfil;
	private String sDesPerfil;
	private Aplicacion aplicacion;
	private Perfil perfil;
	private long nIdEmpresa;
	private String sNombreEmpresa;

	public long getnIdUsuario() {
		return nIdUsuario;
	}

	public void setnIdUsuario(long nIdUsuario) {
		this.nIdUsuario = nIdUsuario;
	}

	public String getsRunUsuario() {
		return sRunUsuario;
	}

	public void setsRunUsuario(String sRunUsuario) {
		this.sRunUsuario = sRunUsuario;
	}

	public String getsNombre() {
		return sNombre;
	}

	public void setsNombre(String sNombre) {
		this.sNombre = sNombre;
	}

	public String getsApePaterno() {
		return sApePaterno;
	}

	public void setsApePaterno(String sApePaterno) {
		this.sApePaterno = sApePaterno;
	}

	public String getsApeMaterno() {
		return sApeMaterno;
	}

	public void setsApeMaterno(String sApeMaterno) {
		this.sApeMaterno = sApeMaterno;
	}

	public String getsNomCompleto() {
		return sNomCompleto;
	}

	public void setsNomCompleto(String sNomCompleto) {
		this.sNomCompleto = sNomCompleto;
	}

	public String getsNombreUsuario() {
		return sNombreUsuario;
	}

	public void setsNombreUsuario(String sNombreUsuario) {
		this.sNombreUsuario = sNombreUsuario;
	}

	public String getsContrasena() {
		return sContrasena;
	}

	public void setsContrasena(String sContrasena) {
		this.sContrasena = sContrasena;
	}

	public String getsNickName() {
		return sNickName;
	}

	public void setsNickName(String sNickName) {
		this.sNickName = sNickName;
	}

	public String getsIniciales() {
		return sIniciales;
	}

	public void setsIniciales(String sIniciales) {
		this.sIniciales = sIniciales;
	}

	public String getsFecNacimiento() {
		return sFecNacimiento;
	}

	public void setsFecNacimiento(String sFecNacimiento) {
		this.sFecNacimiento = sFecNacimiento;
	}

	public String getsCodGenero() {
		return sCodGenero;
	}

	public void setsCodGenero(String sCodGenero) {
		this.sCodGenero = sCodGenero;
	}

	public String getsCodNacionalidad() {
		return sCodNacionalidad;
	}

	public void setsCodNacionalidad(String sCodNacionalidad) {
		this.sCodNacionalidad = sCodNacionalidad;
	}

	public String getsCodEstadoCivil() {
		return sCodEstadoCivil;
	}

	public void setsCodEstadoCivil(String sCodEstadoCivil) {
		this.sCodEstadoCivil = sCodEstadoCivil;
	}

	public String getsCodCategoria() {
		return sCodCategoria;
	}

	public void setsCodCategoria(String sCodCategoria) {
		this.sCodCategoria = sCodCategoria;
	}

	public String getsNumColegiado() {
		return sNumColegiado;
	}

	public void setsNumColegiado(String sNumColegiado) {
		this.sNumColegiado = sNumColegiado;
	}

	public String getsNumProfesional() {
		return sNumProfesional;
	}

	public void setsNumProfesional(String sNumProfesional) {
		this.sNumProfesional = sNumProfesional;
	}

	public String getsCodProfesion() {
		return sCodProfesion;
	}

	public void setsCodProfesion(String sCodProfesion) {
		this.sCodProfesion = sCodProfesion;
	}

	public String getsDesProfesion() {
		return sDesProfesion;
	}

	public void setsDesProfesion(String sDesProfesion) {
		this.sDesProfesion = sDesProfesion;
	}

	public String getsCodCargo() {
		return sCodCargo;
	}

	public void setsCodCargo(String sCodCargo) {
		this.sCodCargo = sCodCargo;
	}

	public String getsDesCargo() {
		return sDesCargo;
	}

	public void setsDesCargo(String sDesCargo) {
		this.sDesCargo = sDesCargo;
	}

	public String getsTelefonoFijo() {
		return sTelefonoFijo;
	}

	public void setsTelefonoFijo(String sTelefonoFijo) {
		this.sTelefonoFijo = sTelefonoFijo;
	}

	public String getsTelefonoMovil() {
		return sTelefonoMovil;
	}

	public void setsTelefonoMovil(String sTelefonoMovil) {
		this.sTelefonoMovil = sTelefonoMovil;
	}

	public String getsEmail() {
		return sEmail;
	}

	public void setsEmail(String sEmail) {
		this.sEmail = sEmail;
	}

	public String getsEmailLaboral() {
		return sEmailLaboral;
	}

	public void setsEmailLaboral(String sEmailLaboral) {
		this.sEmailLaboral = sEmailLaboral;
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

	public String getsClaveAcceso() {
		return sClaveAcceso;
	}

	public void setsClaveAcceso(String sClaveAcceso) {
		this.sClaveAcceso = sClaveAcceso;
	}

	public String getsContrasena1() {
		return sContrasena1;
	}

	public void setsContrasena1(String sContrasena1) {
		this.sContrasena1 = sContrasena1;
	}

	public String getsContrasena2() {
		return sContrasena2;
	}

	public void setsContrasena2(String sContrasena2) {
		this.sContrasena2 = sContrasena2;
	}

	public String getdFecContrasena() {
		return dFecContrasena;
	}

	public void setdFecContrasena(String dFecContrasena) {
		this.dFecContrasena = dFecContrasena;
	}

	public String getdFecLogueo() {
		return dFecLogueo;
	}

	public void setdFecLogueo(String dFecLogueo) {
		this.dFecLogueo = dFecLogueo;
	}

	public long getnCantIntentos() {
		return nCantIntentos;
	}

	public void setnCantIntentos(long nCantIntentos) {
		this.nCantIntentos = nCantIntentos;
	}

	public String getsCodIdiomaPreferente() {
		return sCodIdiomaPreferente;
	}

	public void setsCodIdiomaPreferente(String sCodIdiomaPreferente) {
		this.sCodIdiomaPreferente = sCodIdiomaPreferente;
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

	public String getdFecCreacion() {
		return dFecCreacion;
	}

	public void setdFecCreacion(String dFecCreacion) {
		this.dFecCreacion = dFecCreacion;
	}

	public long getnIdCreador() {
		return nIdCreador;
	}

	public void setnIdCreador(long nIdCreador) {
		this.nIdCreador = nIdCreador;
	}

	public String getdFecActualizacion() {
		return dFecActualizacion;
	}

	public void setdFecActualizacion(String dFecActualizacion) {
		this.dFecActualizacion = dFecActualizacion;
	}

	public long getnIdActualizador() {
		return nIdActualizador;
	}

	public void setnIdActualizador(long nIdActualizador) {
		this.nIdActualizador = nIdActualizador;
	}

	public String getsIndEstado() {
		return sIndEstado;
	}

	public void setsIndEstado(String sIndEstado) {
		this.sIndEstado = sIndEstado;
	}

	public String getsCodAplicacion() {
		return sCodAplicacion;
	}

	public void setsCodAplicacion(String sCodAplicacion) {
		this.sCodAplicacion = sCodAplicacion;
	}

	public String getsDesAplicacion() {
		return sDesAplicacion;
	}

	public void setsDesAplicacion(String sDesAplicacion) {
		this.sDesAplicacion = sDesAplicacion;
	}

	public String getsCodPerfil() {
		return sCodPerfil;
	}

	public void setsCodPerfil(String sCodPerfil) {
		this.sCodPerfil = sCodPerfil;
	}

	public String getsDesPerfil() {
		return sDesPerfil;
	}

	public void setsDesPerfil(String sDesPerfil) {
		this.sDesPerfil = sDesPerfil;
	}

	public Aplicacion getAplicacion() {
		return aplicacion;
	}

	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public long getnIdEmpresa() {
		return nIdEmpresa;
	}

	public void setnIdEmpresa(long nIdEmpresa) {
		this.nIdEmpresa = nIdEmpresa;
	}

	public String getsNombreEmpresa() {
		return sNombreEmpresa;
	}

	public void setsNombreEmpresa(String sNombreEmpresa) {
		this.sNombreEmpresa = sNombreEmpresa;
	}
	
}