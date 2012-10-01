<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<script type="text/javascript">
 
	$(function(){
		$('input[type="checkbox"]').ezMark();
		
		var seccionDetalle = $('#detallePrestacionAnexo');
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
			if (validaFiltrosActPrestAnexo()){
				getXml("SvtConvenios", getUrlDetallePrestacionAnexo(), function(xml){
					alert('${solemfmt:getLabel("alert.PrestacionAnexoActualizado",codIdioma)}');
				});
			}
		});
		
	});

	function getUrlDetallePrestacionAnexo(){
		var sData = 'KSI=${sesion.sesIdSesion}';
		sData += '&accion=actualizarPrestacionAnexo';
		sData += '&convenioAnexoDetalle.nIdConvDetalle=' + $('#presIdDetalle').val();
		sData += '&convenioAnexoDetalle.sNombrePrestConvenio=' + $('#presAnexoNombreDetalle').val();
		sData += '&convenioAnexoDetalle.sIndTipoPago=' + devuelveEstadoCheck($('#presTipoPagoDetalle'));
		sData += '&convenioAnexoDetalle.nIdActualizador=${sesion.sesIdUsuario}';
		return sData;
	}
	 
	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}

	function validaFiltrosActPrestAnexo(){
		var cont = 0; 
		$("#detalle01 .obligatorio").each(function(){ 
			var value = $(this).val(); 
			if(value==""){
				$(this).parent().children("img").css("display","block");
				$(this).parent().parent().children("div.contieneButtonLupa").children("img").css("display","block");
				cont++;
			}else{
				$(this).parent().children("img").css("display","none");
				$(this).parent().parent().children("div.contieneButtonLupa").children("img").css("display","none");
			}  
		}); 
		
		if(cont==0)
			return true;
		else
			return false;	
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
					<span>${solemfmt:getLabel("label.Convenio",codIdioma)} "${prestacionAnexo.sNombreConvenio}" - ${solemfmt:getLabel("label.Anexo",codIdioma)} "${prestacionAnexo.sNombreAnexo}" - ${solemfmt:getLabel("label.Prestacion",codIdioma)} "${prestacionAnexo.sNombrePrestacion}"</span>
				</div>
				<div class="divBtn" style="float: left;">
					<input class="button" type="button" id="actualizarPrestacionAnexo" value="${solemfmt:getLabel('boton.Actualizar',codIdioma)}" role="button" aria-disabled="false">
				</div> 
			</div>  
			<div id="detalle01" class="contenido_detalle sombra">
				<input type="hidden" id="presIdDetalle" value="${prestacionAnexo.nIdConvDetalle}">
				<div class="fila alturaFila">
					<div class="campo campo_detalle" style="width: 280px">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo",codIdioma)}: </span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="text" id="caxCodDetalle" class="caja_texto obligatorio" title="${prestacionAnexo.sCodPrestacion}" value="${prestacionAnexo.sCodPrestacion}" readonly>	 
							<img class="imgNoOk validadorCampo2" alt='' style="left: 136px;" src='${rutaImg}iconos/badCheck.png'>	
						</div>
					</div> 
		
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Prestacion",codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="caxNombreDetalle" class="caja_texto obligatorio" title="${prestacionAnexo.sNombrePrestacion}" value="${prestacionAnexo.sNombrePrestacion}" readonly>	
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	 
						</div>
					</div>
					
					<div class="campo campo_detalle" style="width: 350px">
						<div class="label_campo detalle_label_campo" style="width: 123px;">
							<span>${solemfmt:getLabel("label.PrestacionConvenio",codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="presAnexoNombreDetalle" class="caja_texto obligatorio" value="${prestacionAnexo.sNombrePrestConvenio}">	
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	 
						</div>
					</div>
				</div>
				<div class="fila alturaFila">
					
					<div class="campo campo_detalle">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.PagoAnticipado",codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="presTipoPagoDetalle" <c:if test="${prestacionAnexo.sIndTipoPago == 'S'}">checked</c:if>>
						</div> 	
					</div>
				</div>
			</div>
		</div> 
		
		<div id="cont_sec_det_02" class="cont_sec_det_tab">  
			<div id="detalle02" class="contenido_detalle_tab sombra"> 
				<div class="contenidoTab">
					<div class="tabber"> 
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.Precios",codIdioma)}</h2>
							<p><%@ include file="precios/listaPrecios.jsp" %></p>
						</div>
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.Modificadores",codIdioma)}</h2>
							<p><%@ include file="modificadores/listaModificadores.jsp" %></p>
						</div> 
					</div>
				</div>
				<div class="espacio10"></div>
			</div>
		</div>
	</form>
</div>