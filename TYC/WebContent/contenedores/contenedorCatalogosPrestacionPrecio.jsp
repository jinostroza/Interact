<%@ page language="java"%>
<%@ include file="../general/includes/declaraciones.jsp" %>
<%@ include file="../general/includes/htmlEncabezado.jsp" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<script type="text/javascript">
var tituloBotonAgregar = "${solemfmt:getLabel('boton.Agregar', codIdioma)}";
var tituloBotonEliminar = "${solemfmt:getLabel('boton.Eliminar', codIdioma)}";

$(function(){
	$.datepicker.setDefaults($.datepicker.regional['es']);
});
</script>

<div class="contenido" align="center">

	<div id="contenidoSuperior" class="contenidoSuperior">
		<%@ include file="../general/includes/contenidoSuperior.jsp" %>
	</div>

	<div id="contenidoPrincipal" class="contenidoPrincipal">
		<c:choose>

			
			<%-- PRESTACIONESCATALOGOPRECIO --%>
			
			<c:when test="${param.accion eq 'agregarPrecio'}">		
				<%@ include file="/catalogos/prestaciones/precios/agregaPrecio.jsp" %>
			</c:when>
			
			<c:when test="${param.accion eq 'guardarPreciosCatalogo'}">
				<%@ include file="/catalogos/prestaciones/precios/detallePrecio.jsp" %>
			</c:when>
			
			<c:when test="${param.accion eq 'cargarPrecioCatalogo'}">
				<%@ include file="/catalogos/prestaciones/precios/detallePrecio.jsp" %>
			</c:when>
			


			<%-- INCLUIR PAGINA DE BIENVENIDA/ERROR --%>
			<c:otherwise></c:otherwise>
		</c:choose>
	</div>
</div>