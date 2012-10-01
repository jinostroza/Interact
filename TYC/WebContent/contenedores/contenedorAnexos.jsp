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

			<%-- ANEXOS --%>

			<c:when test="${param.accion eq 'cargarAnexo'}">		
				<%@ include file="/convenios/anexos/detalleAnexo.jsp" %>
			</c:when>
			
			<c:when test="${param.accion eq 'guardarAnexo'}">
				<%@ include file="/convenios/anexos/detalleAnexo.jsp" %>
			</c:when>
			
			<c:when test="${param.accion eq 'agregarAnexo'}">
				<%@ include file="/convenios/anexos/agregaAnexo.jsp" %>
			</c:when>
			
			<%-- ANEXOS 

			<c:when test="${param.accion eq 'agregarModificador'}">		
				<%@ include file="/convenios/anexos/prestaciones/modificadores/agregaModificador.jsp" %>
			</c:when>
			--%>
			
			<%-- INCLUIR PAGINA DE BIENVENIDA/ERROR --%>
			<c:otherwise></c:otherwise>
		</c:choose>
	</div>
</div>