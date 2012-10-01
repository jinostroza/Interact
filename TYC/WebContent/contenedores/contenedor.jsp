<%@ page language="java"%>
<%@ include file="../general/includes/declaraciones.jsp" %>
<%@ include file="../general/includes/htmlEncabezado.jsp" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<script type="text/javascript">

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
			<c:when test="${param.accion eq 'inicio'}">
				<%@ include file="../bienvenida.jsp" %>
			</c:when>

			<%-- INCLUIR PAGINA DE BIENVENIDA/ERROR --%>
			<c:otherwise></c:otherwise>
		</c:choose>
	</div>
</div>