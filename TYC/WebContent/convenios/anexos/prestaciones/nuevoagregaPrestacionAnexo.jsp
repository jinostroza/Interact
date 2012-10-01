<%@ page language="java"%>
<%@ include file="../../../general/includes/declaraciones.jsp"%>
<%@ include file="../../../general/includes/htmlEncabezado.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<script type="text/javascript">
 
	$(function(){
		var seccionDetalle = $('#detallePrestacionAnexo');
		$('input[type="checkbox"]').ezMark();
		
		$(".titulo img").click(function(event){ 
			var id = $(this).attr("id");
			var div = $(this).parent().parent().parent().attr("id"); 
			if(id=="flechaUp"){
				$("#"+div+" .contenido_detalle").slideUp();
				$(this).attr("id","flechaDown");	
				$(this).attr("src","${rutaImg}template/"+$(this).attr("imgDown"));
				//cambiar a reloadGrid cuando sea con paginacion
				var t = setTimeout("borderBottomRadius('"+div+"')",400); 
				$(this).parent().parent().removeClass("borderBottomRadius");
			}else{
				$("#"+div+" .contenido_detalle").slideDown(); 
				$(this).attr("id","flechaUp");	
				$(this).attr("src","${rutaImg}template/"+$(this).attr("imgUp")); 
				$(this).parent().parent().removeClass("borderBottomRadius");
			}
		});
	
		$('input#actualizarPrestacionAnexo').click(function(){
			getXml("SvtConvenios", getUrlDetallePrestacionAnexo(), function(xml){ 
				alert("${solemfmt:getLabel('alert.AnexoPrestActualizada', codIdioma)}"); 
			});
		});
	}); 

	function getUrlDetallePrestacionAnexo(){
		var sData = 'KSI=${sesion.sesIdSesion}';
		sData += '&accion=actualizarPrestacionAnexo';
		sData += '&convenioAnexoDetalle.nIdConvDetalle=' + $('#presIdDetalle').val();
		sData += '&convenioAnexoDetalle.sIndTipoPago=' + devuelveEstadoCheck($('#presTipoPagoDetalle'));
		sData += '&convenioAnexoDetalle.nIdActualizador=${sesion.sesIdUsuario}';
		return sData;
	}
	 
	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}

</script>

<div id="detallePrestacionAnexo" class="detalle">
	<form id="formDetallePrestacionAnexo" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="caxtieneFlecha" class="contieneFlecha">
					<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">
				</div>
				<div id="contienetitulo" class="contienetitulo">
					<span>Convenio "${prestacionAnexo.sNombreConvenio}" - Anexo "${prestacionAnexo.sNombreAnexo}" - Prestaci&oacute;n "${prestacionAnexo.sNombrePrestacion}"</span>
				</div> 
				<div class="divBtn" style="float: left;">
					<input class="button" type="button" id="actualizarPrestacionAnexo" value="Guardar" role="button" aria-disabled="false">
				</div>  
			</div>  
			<div id="detalle01" class="contenido_detalle sombra">
				<input type="hidden" id="presIdDetalle" value="${prestacionAnexo.nIdConvDetalle}">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>C&oacute;digo: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="caxNueAgCodDetalle" class="caja_texto" value="${prestacionAnexo.sCodPrestacion}" readonly>	 
						</div>
					</div>
		
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>Prestaci&oacute;n: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="caxNueAgNombreDetalle" class="caja_texto" value="${prestacionAnexo.sNombrePrestacion}" readonly>	 
						</div>
					</div>
					<div class="campo campo_det_small"">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>Pago Anticipado: </span>
						</div>
						<div class="contineCheckbox" style="float: left;">
							<input type="checkbox" id=presTipoPagoDetalle style="margin-top: 5px;" <c:if test="${prestacionAnexo.sIndTipoPago == 'S'}">checked</c:if>>
						</div> 	
					</div>
				</div>
			</div>
		</div> 
		<div id="cont_sec_det_02" class="cont_sec_det_tab">  
			<div id="titulo02" class="titulo">
				<div id="contieneFlecha" class="contieneFlecha">
					<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">
				</div>
				<div id="contienetitulo" class="contienetitulo">
				</div>
			</div> 
			<div id="detalle02" class="contenido_detalle sombra"> 
				<div style="width: 988px;margin-left: 7px;">
					<div class="tabber"> 
						<div class="tabbertab">
							<h2>Precios</h2>
							<p><%@ include file="precios/listaPrecios.jsp" %></p>
						</div>
						<div class="tabbertab">
							<h2>Modificadores</h2>
							<p><%@ include file="modificadores/listaModificadores.jsp" %></p>
						</div> 
					</div>
				</div>
				<div class="espacio10"></div>
			</div>
		</div>
	</form>
</div>