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
			<%-- PRECIOS CONVENIO --%>
			<c:when test="${param.accion eq 'cargarPrecio'}">		
				<%@ include file="/convenios/anexos/prestaciones/precios/detallePrecio.jsp" %>
			</c:when>
			 
			<c:when test="${param.accion eq 'agregarPrecio'}">		
				<%@ include file="/convenios/anexos/prestaciones/precios/agregaPrecio.jsp" %>
			</c:when>
			 
			<c:when test="${param.accion eq 'guardarPrecio'}">		
				<%@ include file="/convenios/anexos/prestaciones/precios/detallePrecio.jsp" %>
			</c:when>
			
			<%-- MODIFICADORES CONVENIO --%>
			<c:when test="${param.accion eq 'cargarModificador'}">		
				<%@ include file="/convenios/anexos/prestaciones/modificadores/detalleModificador.jsp" %>
			</c:when>
			
			<c:when test="${param.accion eq 'agregarModificador'}">		
				<%@ include file="/convenios/anexos/prestaciones/modificadores/agregaModificador.jsp" %>
			</c:when>
			 
			<c:when test="${param.accion eq 'guardarModificador'}">		
				<%@ include file="/convenios/anexos/prestaciones/modificadores/detalleModificador.jsp" %>
			</c:when>
			<%-- INCLUIR PAGINA DE BIENVENIDA/ERROR --%>
			<c:otherwise></c:otherwise>
		</c:choose>
	</div>
</div>	
	