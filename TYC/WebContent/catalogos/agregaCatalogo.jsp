<%@ page language="java"%>
<%@ include file="../general/includes/declaraciones.jsp"%>
<%@ include file="../general/includes/htmlEncabezado.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<script type="text/javascript">
 
	$(function()
	{

		$.datepicker.setDefaults($.datepicker.regional['es']);
		
		$('input[type="checkbox"]').ezMark();
		 		 
		$("#catagregaFechaInicio").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#catagregaFechaTermino").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$("#catagregaFechaTermino").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#catagregaFechaInicio").datepicker( "option", "maxDate", selectedDate );
			}
		});    
		
		$('input#guardarCatalogo').click(function(){
			 
			if (validaFiltrosAgregaCatalogos()){  
				$('#formAgregaCatalogo').attr('action', getUrlAgregarCatalogo());
				$('#formAgregaCatalogo').submit();
			} 
		});  

		$('input#catagregabuscaInstitucion').click(function(){
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			//window.parent.$('#ifrmCatalogo').css("height","450px");	
			var myDiv = document.getElementById('contenidoPrincipal');		
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y;  
	
			var url = "SvtCatalogos?KSI=${sesion.sesIdSesion}&accion=cargaEmpresasInst";
			var iframe =  $('<iframe id="ifrmInstituciones" src="'+url+'"/>'); 
			
			iframe.dialog({
				autoOpen: true,
				modal: true,
				height: 382,
				width: 777, 
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title: '${solemfmt:getLabel("label.Institucion", codIdioma)}'  
			}).width(777).height(382);
			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
		});

		$('input#catagregabuscaResponsable').click(function(){
			if($('#catIdPrestador').val())
			{
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y; 

			var url = "SvtCatalogos?KSI=${sesion.sesIdSesion}&accion=cargaResponsables&idInst="+$('#catIdPrestador').val();
			var iframe =  $('<iframe id="ifrmResponsables" src="'+url+'"/>'); 
			
			iframe.dialog({
				autoOpen: true,
				modal: true,
				height: 382,
				width: 777,
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title:'${solemfmt:getLabel("label.Responsables", codIdioma)}' 
			}).width(777).height(382);
			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
		}
		else
		{
			alert("${solemfmt:getLabel('alert.DebeSeleccionarPrestador', codIdioma)}");
		}
		});	
	}); 
	
	function getUrlAgregarCatalogo()
	{
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarCatalogo';
		sData += '&catalogo.nIdCatalogo=' + $('#catagregaIdCatalogo').val();
		sData += '&catalogo.nIdPrestador='  + $('#catIdPrestador').val();
		sData += '&catalogo.sCodCatalogo=' + $('#catagregaCodigo').val();
		sData += '&catalogo.sNombre=' + $('#catagregaNombre').val();
		sData += '&catalogo.sDescripcion=' + $('#catagregaDescripcion').val();
		sData += '&catalogo.sFecHoInicio=' + desformateaFechaSinHora('#catagregaFechaInicio');
		sData += '&catalogo.sFecHoTermino='  + desformateaFechaSinHora('#catagregaFechaTermino');
		sData += '&catalogo.nIdResponsable='   + $('#catIdResp').val();
		sData += '&catalogo.sIndPlantilla=' + devuelveEstadoCheck($('#catagregaIndPlantilla'));
		sData += '&catalogo.sIndPublico='  + devuelveEstadoCheck($('#catagregaIndPublico'));
		sData += '&catalogo.nIdCreador=${sesion.sesIdUsuario}';		
		//console.log(sData);
		return sData;
	}

	function  validaFiltrosAgregaCatalogos(){
		var cont = 0; 
		$("#contieneAgregaCatalogo .obligatorio").each(function(){ 
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

	function buscarCatalogos(){
		
	}
 
</script>
<div id="agregaCatalogo" class="detalle">
	<form id="formAgregaCatalogo" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det">
			<div id="titulo01" class="titulo">  
				<div id="contienetitulo" class="contieneTituloAgrega">
					<span>${solemfmt:getLabel("label.AgregaCatalogo", codIdioma)}</span>
				</div>
				<div class="divBtn" style="float: left;">
					<input class="button" type="button" id="guardarCatalogo" value="${solemfmt:getLabel('boton.Guardar', codIdioma)}" role="button" aria-disabled="false">
				</div> 
			</div>  
			<div id="contieneAgregaCatalogo" class="contenido_detalle sombra">
				<input type="hidden" id="catagregaIdCatalogo" value="${catalogo.nIdCatalogo}">
				<input type="hidden" id="catIdPrestador" value="${catalogo.nIdPrestador}">  
				
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Prestador", codIdioma)}: </span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="hidden" id="catIdPrestador" value="">
							<input type="text" id="catNombrePrestador" class="obligatorio" value="" readonly>
						</div>
						<div class="contieneButtonLupa">
							<input class="buttonLupa" type="button" id="catagregabuscaInstitucion" value="" role="button" aria-disabled="false">
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catagregaCodigo" class="caja_texto obligatorio" value="">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	 
						</div>
					</div>
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catagregaNombre" class="caja_texto obligatorio" value="">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div> 
				</div> 
				<div class="fila alturaFilaTextArea">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Descripcion", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<textarea id="catagregaDescripcion" class="descripcion obligatorio"></textarea>
 						</div>
					</div>
				</div>  
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaInicio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catagregaFechaInicio" class="caja_texto obligatorio" value="">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>		 
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaFin", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catagregaFechaTermino" class="caja_texto obligatorio" value="">	
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>
					</div>
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo"> 
							<span>${solemfmt:getLabel("label.Responsable", codIdioma)}: </span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="text" id="catNomResp" title="" value="" readonly>
							<input type="hidden" id="catIdResp" value="">
						</div>	
						<div class="contieneButtonLupa">
							<input class="buttonLupa" type="button" id="catagregabuscaResponsable" value="" role="button" aria-disabled="false">
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>	 		
					</div>
				</div>  
				<div class="fila alturaFila">
					<div class="campo campo_det_small">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.Plantilla", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="catagregaIndPlantilla" style="margin-top: 5px;">
						</div> 	
					</div> 
					<div class="campo campo_det_small">
						<div class="label_campo labelCampoHorizontal anchoMedium">
							<span>${solemfmt:getLabel("label.CatalogoPublico", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="catagregaIndPublico" style="margin-top: 5px;">
						</div> 	
					</div> 
				</div> 
			</div> 
		</div>		  
	</form>
</div>