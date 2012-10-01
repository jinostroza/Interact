<%@ page language="java"%>
<%@ include file="../../../general/includes/declaraciones.jsp"%>
<%@ include file="../../../general/includes/htmlEncabezado.jsp"%>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<script type="text/javascript">
 
	$(function()
	{
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
	
		$('input#guardarPrestacionAnexo').click(function(){
			if (validaFiltrosAgregaPrestacionAnexo()){
				$('#formAgregaPrestacionAnexo').attr('action', getUrlAgregarPrestacionAnexo());
				$('#formAgregaPrestacionAnexo').submit();
			}
		});  

		$('input#buscaPresAgregaPresAnexo').click(function(){
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y; 

			var url = "SvtConvenios?KSI=${sesion.sesIdSesion}&accion=cargaPrestacionesSinAnexo&idAnexo="+$('#presAnexoIdAnexoAgrega').val()+"&idConvenio="+$('#presAnexoIdConvPres').val();
			var iframe =  $('<iframe id="ifrmPrestaciones" src="'+url+'"/>'); 
			
			iframe.dialog({
				autoOpen: true,
				modal: true,
				height: 382,
				width: 777,
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title:"Prestaciones disponibles"
			}).width(777).height(382);
			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
		});
	}); 

	function getUrlAgregarPrestacionAnexo(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarPrestacionAnexo';
		sData += '&convenioAnexoDetalle.nIdConvenio=' + $('#presAnexoIdConvPres').val();
		sData += '&convenioAnexoDetalle.nIdAnexo=' + $('#presAnexoIdAnexoAgrega').val();
		sData += '&convenioAnexoDetalle.nIdPrestacion=' + $('#presAnexoIdPres').val();
		sData += '&convenioAnexoDetalle.sNombrePrestConvenio=' + $('#presAnexoNombreAgrega').val();
		sData += '&convenioAnexoDetalle.sIndTipoPago=' + devuelveEstadoCheck($('#presAnexoTipoPagoAgrega'));
		sData += '&convenioAnexoDetalle.nIdCreador=${sesion.sesIdUsuario}';		
		
		return sData;
	} 

	function validaFiltrosAgregaPrestacionAnexo(){
		var cont = 0; 
		$("#detalle01 .obligatorio").each(function(){
			var value = $.trim($(this).val());   
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

	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}
 
</script>
<div id="guardarPrestacionAnexo" class="detalle">
	<form id="formAgregaPrestacionAnexo" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="contienetitulo" class="contieneTituloAgrega">
					<span>${solemfmt:getLabel("label.AgregaPrestacionAnexo",codIdioma)}</span> 
				</div>
				<div class="divBtn" style="float: left;">
					<input class="button" type="button" id="guardarPrestacionAnexo" value="${solemfmt:getLabel('boton.Guardar',codIdioma)}" role="button" aria-disabled="false">
				</div>
			</div> 
			<div id="detalle01" class="contenido_detalle sombra">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Prestacion",codIdioma)}: </span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="hidden" id="presAnexoIdConvPres" value="${idConvenio}">
							<input type="hidden" id="presAnexoIdAnexoAgrega" value="${idAnexo}">
							<input type="hidden" id="catPresAnexoIdConvPres" value="">
							<input type="hidden" id="presAnexoIdPres" value="">
							<input type="text" id="presAnexoNomPres" class="obligatorio" value="" title="" readonly>
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaPresAgregaPresAnexo" value="" role="button" aria-disabled="false">
							 <img class="imgNoOk validadorLupa" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					
					<div class="campo campo_detalle" style="width: 333px;">
						<div class="label_campo detalle_label_campo" style="width: 123px;">
							<span>${solemfmt:getLabel("label.PrestacionConvenio",codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="presAnexoNombreAgrega" class="caja_texto obligatorio" value="">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>
					</div>
					
					<div class="campo campo_det_smalls">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.PagoAnticipado",codIdioma)}: </span>
						</div>
						<div class="contineCheckbox" style="float: left;">
							<input type="checkbox" id=presAnexoTipoPagoAgrega style="margin-top: 5px;">
						</div> 	
					</div>
				</div>
				<div class="espacio10"></div> 
			</div>  
		</div>	
	</form>
</div>