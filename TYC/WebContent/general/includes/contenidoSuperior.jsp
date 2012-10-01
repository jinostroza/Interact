<%@ page language="java"%> 
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">

$(function() {
	var rutaSistema = "${sesion.sesRutaSistema}";	
	var arregloRuta = rutaSistema.split("|");

	$("#contenedorRuta").empty();

	for(var i=0; i < arregloRuta.length-1; i++)
	{
		var accionArray = arregloRuta[i].split("accion=");
		var accionArray2 = accionArray[1].split("&");
		var accion = accionArray2[0];
		var accionFinal = "";

		if(accion.indexOf("despachar") >= 0)
			accionFinal = accion.replace('despachar', 'Lista de ');
		else if(accion.indexOf("cargar") >= 0)
			accionFinal = accion.replace('cargar', 'Detalle ');
		else if(accion.indexOf("agregar") >= 0)
			if(accion.indexOf("Prestacion") >= 0)
				accionFinal = accion.replace('agregar', 'Nueva ');
			else
				accionFinal = accion.replace('agregar', 'Nuevo ');

		if(arregloRuta.length == 2)	//unico elemento
			sHtml = "<span id=\""+accion+"\" class='ruta_last'>"+accionFinal+"</span>";
		
		else if(i == 0)	//primer elemento
			sHtml = "<a id=\""+accion+"\" href=\"\">"+accionFinal+"</a>";

		else if (i== arregloRuta.length-2) //ultimo elemento
			sHtml = "<span id=\"sep_"+accion+"\"> > </span> <span id=\""+accion+"\" class='ruta_last'>"+accionFinal+"</span>";//agrego ">" + accion

		else
			sHtml = "<span id=\"sep_"+accion+"\"> > </span> <a id=\""+accion+"\" href=\"\">"+accionFinal+"</a>";//agrego ">" + accion
		
		$("#contenedorRuta").append(sHtml);

		if($('#contenedorRuta #'+accion).is('a'))
			$('#'+accion).attr("href", arregloRuta[i]);	
	}

	$("#contenedorRuta").children().each(function()
	{
		var id = $(this).attr("id");

		if(id.indexOf('agregar') >= 0)
		{
			if(!$('#contenedorRuta #'+id).is('span')){
			    $('#contenedorRuta #'+id).remove();
			    $('#contenedorRuta #sep_'+id).remove();
			}
		}
	});
});
		
	setTimeout("recargar();", 500);
	
	function recargar(){
		$("#hora").html(devuelveHora(i));
		i++;
		setTimeout("recargar();", 500); 
	}
</script>

<div id="banner" class="banner">
	<div class="logoEmpresa"></div>
	<div id="seccionInfoUser" class="seccionInfoUser">
		<div id="datosUsuario">
			<div id="espaciadorDatosUsuario"></div>
			<div id="contieneInfoUsuario">
				<span>${sesion.sesNombreCompleto}</span>
			</div>
		</div>
		<div id="sistema">
			<div id="espaciadorSistema"></div>
			<div id="contieneFechaSistema">
				 <span> ${solemfmt:devuelveFecha(sesion.sesFhoUltimaConsulta)} </span>
			</div>
			<div id="divHora">
				<div id="hora"></div>
			</div>	
		</div>
	</div>
</div>

<div id="menuSuperior" class="menu">
	<%@ include file="menu.jsp" %>
</div>

<div id="divCerrarSession" class="divCerrarSession">
	<div class="contieneHrefSesion">
		<a href="login.jsp">Cerrar Sesi&oacute;n</a>    
	</div>
</div>

<div id="contenedorRuta" class="ruta"></div>