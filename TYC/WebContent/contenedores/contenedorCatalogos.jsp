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

			<%-- CATALOGOS --%>
			
			<c:when test="${param.accion eq 'despacharCatalogos'}">		
				<%@ include file="/catalogos/listaCatalogos.jsp" %>
			</c:when>
			
			<c:when test="${param.accion eq 'agregarCatalogo'}">
				<%@ include file="/catalogos/agregaCatalogo.jsp" %>
			</c:when> 
			
			<c:when test="${param.accion eq 'cargarCatalogo'}">
				<%@ include file="/catalogos/detalleCatalogo.jsp" %>
			</c:when>
			
			<c:when test="${param.accion eq 'guardarCatalogo'}">
				<%@ include file="/catalogos/detalleCatalogo.jsp" %>
			</c:when> 
			
			<%-- INCLUIR PAGINA DE BIENVENIDA/ERROR --%>
			<c:otherwise></c:otherwise>
		</c:choose>
	</div>
</div>