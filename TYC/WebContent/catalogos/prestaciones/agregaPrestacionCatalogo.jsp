<%@ page language="java"%>
<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezado.jsp"%>
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
		var seccionDetalle = $('#guardarPrestacion');
		
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
	
		$('input#guardarPrestacion').click(function(){
			if (validaFiltrosAgregaAnexo()){
				$('#formAgregaPrestacion').attr('action', getUrlAgregarPrestacion());
				$('#formAgregaPrestacion').submit();
			}
		});

		$('input#buscaPrestAgregaCat').click(function(){
			$('#titlePopup').parent().remove();
			$('iframe').remove();
		    var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y; 

			var url = "SvtCatalogos?KSI=${sesion.sesIdSesion}&accion=cargaPrestaciones&idCatalogo="+ $('#prestacionIdCatalogoAgrega').val();
			var iframe =  $('<iframe id="ifrmPrestaciones" src="'+url+'"/>'); 
			
			iframe.dialog({
				autoOpen: true,
				modal: true,
				height: 382,
				width: 780,
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title: '${solemfmt:getLabel("label.InstitucionesPrestadores", codIdioma)}' 
			}).width(780).height(382);
			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
		});


		$('#catPrestHonorarios').blur(function(e){
			soloNumerosOnBlur('catPrestHonorarios');
		});

	    $('#catPrestHonorarios').keypress(function(e){
	    	return soloNumeros(e);
	    });

	    $('#catPrestGastos').blur(function(e){
			soloNumerosOnBlur('catPrestGastos');
		});

	    $('#catPrestGastos').keypress(function(e){
	    	return soloNumeros(e);
	    });
	}); 

	function getUrlAgregarPrestacion(){
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarPrestacionesCatalogo';
		sData += '&catalogoPrest.nIdCatalogo=' + $('#prestacionIdCatalogoAgrega').val();
		sData += '&catalogoPrest.nIdPrestacion=' + $('#catIdPres').val();
		sData += '&catalogoPrest.sCodPrestacion=' + $('#catCodPres').val();
		sData += '&catalogoPrest.sNomPrestacion=' + $('#catNomPres').val();
		sData += '&catalogoPrest.sIndTipoPago=S';// + $('#catPrestIndPagoAnticpado').val();
		sData += '&catalogoPrest.sNomPrestCatalogo=' + $('#catPrestNombre').val();
		sData += '&catalogoPrest.nPorHonorarios=' + $('#catPrestHonorarios').val();
		sData += '&catalogoPrest.nPorGastos=' + $('#catPrestGastos').val();
		sData += '&catalogoPrest.nIdCreador=${sesion.sesIdUsuario}';		
		sData += '&indRuta=S';
		return sData;
	} 
	
	function validaFiltrosAgregaAnexo(){
		var cont = 0; 
		$("#detalleAgregaPrestAnx .obligatorio").each(function(){
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


	function validaPorcentajeHonorarios()
	{
		var porcentaje =  $('#catPrestHonorarios').val();
		if(porcentaje<0 || porcentaje>100)
		{
			alert('${solemfmt:getLabel("alert.PorcentajeEntre0y100", codIdioma)}');
			$('#catPrestHonorarios').val('');	
		}
	}

	function validaPorcentajeGastos()
	{
		var porcentaje =  $('#catPrestGastos').val();
		if(porcentaje<0 || porcentaje>100)
		{
			alert('${solemfmt:getLabel("alert.PorcentajeEntre0y100", codIdioma)}');
			$('#catPrestGastos').val('');	
		}
	}
</script> 

<div id="guardarPrestacion" class="detalle">
	<form id="formAgregaPrestacion" method="post">
		<div id="cont_sec_agrega" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="contienetitulo" class="contieneTituloAgrega">
					<span>${solemfmt:getLabel("label.AgregaPrestacion", codIdioma)}</span>
				</div> 
				<div class="divBtn" style="float: left;"> 
					<input class="button" type="button" id="guardarPrestacion" value="${solemfmt:getLabel('boton.Guardar', codIdioma)}" role="button" aria-disabled="false">
				</div>	 
			</div>
			<div id="detalleAgregaPrestAnx" class="contenido_detalle sombra">	
				<input type="hidden" id="prestacionIdCatalogoAgrega" value="${idCatalogo}">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="hidden" id="catIdPres" value="">
							<input type="text" id="catCodPres" class="obligatorio" readonly>
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaPrestAgregaCat" value="" role="button" aria-disabled="false">
							 <img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>						
						</div>
					</div>
					<div class="campo campo_detalle" style="width: 423px">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Prestacion", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left" style="width: 295px">
							<input type="text" id="catNomPres" class="caja_texto obligatorio" value="" readonly> 
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png" style="left: 271px;">
						</div>
					</div>
					<div class="campo campo_det_small">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.PagoAnticipado", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="catPrestIndPagoAnticpado" style="margin-top: 5px;">
						</div> 	
					</div> 
				</div>
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo" style="top: -6px">
							<span>${solemfmt:getLabel("label.Prestacion", codIdioma)}</span><br>
							<span>${solemfmt:getLabel("label.Catalogo", codIdioma)}:</span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catPrestNombre" class="caja_texto obligatorio" value="">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'> 
						</div>
					</div>
					<div class="campo campo_detalle" style="width: 250px;">
						<div class="label_campo detalle_label_campo">
							<span>% ${solemfmt:getLabel("label.Honorarios", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left" style="width: 64px;">
							<input type="text" id="catPrestHonorarios" onChange="validaPorcentajeHonorarios();" class="caja_texto obligatorio" style="width: 60px;" value="">
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png" style="left: 66px;">
						</div>
					</div>
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo" style="width: 73px;">
							<span>% ${solemfmt:getLabel("label.Gastos", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left" style="width: 65px;">
							<input type="text" id="catPrestGastos" onChange="validaPorcentajeGastos();" class="caja_texto obligatorio" style="width: 60px;" value="">
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png" style="left: 66px;"> 
						</div> 
					</div> 
				</div> 
				<div class="espacio5"></div>
			</div>
		</div> 
	</form>
</div>