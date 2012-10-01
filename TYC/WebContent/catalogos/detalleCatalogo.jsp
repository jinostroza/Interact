<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<script type="text/javascript">

	$(function(){
		var seccionDetalle = $('#contenedorDetalleCatalogo');  
		 
		$("#catFechaInicio").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#catFechaTermino").datepicker( "option", "minDate", selectedDate );
			}
		}); 

		$("#catFechaTermino").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#catFechaInicio").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$('input[type="checkbox"]').ezMark(); 
	
		$('input#actualizarCatalogo').click(function(){
			//if(seccionDetalle.is(":visible") && validaFiltrosActualizaCatalogo()){ 	
			if(validaFiltrosActualizaCatalogo()){ 	
				getXml('SvtCatalogos', getUrlDetalleCatalogo(), function(xml){
	  	     		alert('${solemfmt:getLabel("alert.CatalogoActualizado", codIdioma)}');
	  	    	});
			}	 
			 
		});

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

		$('input#buscaRespDetCat').click(function(){
			if($('#catIdPrestador').val()){
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y+85; 

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

	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}
	
	function getUrlDetalleCatalogo(){
		var sData =  'KSI=${sesion.sesIdSesion}';
		sData += '&accion=actualizarCatalogo';
		sData += '&catalogo.nIdPrestador='+ $('#catIdPrestador').val();
		sData += '&catalogo.nIdCatalogo=' + $('#catIdCatalogo').val();
		sData += '&catalogo.sNombreFantasia=' + $('#catNombreEmpresa').val();
		sData += '&catalogo.sCodCatalogo=' + $('#catCodigo').val();
		sData += '&catalogo.sNombre=' + $('#catNombre').val();
		sData += '&catalogo.sCodEstado=' + $('#catDetEstado').val();
		sData += '&catalogo.sDescripcion=' + $('#catDescripcion').val();
		sData += '&catalogo.sFecHoInicio=' + desformateaFechaSinHora('#catFechaInicio');
		sData += '&catalogo.sFecHoTermino=' + desformateaFechaSinHora('#catFechaTermino');
		sData += '&catalogo.nIdResponsable=' + $('#catIdResp').val();
		sData += '&catalogo.sIndPlantilla=' +  devuelveEstadoCheck($('#catIndPlantilla'));
		sData += '&catalogo.sIndPublico=' +  devuelveEstadoCheck($('#catIndPublico'));
		sData += '&catalogo.sIndVigencia=' +  devuelveEstadoCheck($('#catIndVigencia'));
		sData += '&catalogo.nIdActualizador=${sesion.sesIdUsuario}';
		return sData; 
	}

    function devuelveEstadoCheck(){
		if($('#detalleCatalogo input[type="checkbox"]').is(':checked'))
			return 'S';
		else
			return 'N';
    } 

	function validaFiltrosActualizaCatalogo(){ 
		var cont = 0; 
		$("#detalleCatalogo .obligatorio").each(function(){
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
	
</script> 

<div id="contenedorDetalleCatalogo" class="detalle">
	<form id="formDetalleCatalogo" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="contieneFlecha" class="contieneFlecha"> 
					<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">  
				</div>
				<div id="contienetitulo" class="contienetitulo">
					<span>${solemfmt:getLabel("label.Detalle", codIdioma)} "${catalogo.sNombre}"</span>
				</div> 
				<div class="divBtn" >
					<input class="button" type="button" id="actualizarCatalogo" value="${solemfmt:getLabel('boton.Actualizar', codIdioma)}" role="button" aria-disabled="false">
				</div>  
			</div>  
			
			<div id="detalleCatalogo" class="contenido_detalle sombra">
				<input type="hidden" id="catIdCatalogo" value="${catalogo.nIdCatalogo}">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Prestador", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left" >
							<input type="text" id="catNombreEmpresa" class="caja_texto obligatorio" title="${catalogo.sNombreFantasia}" title="${catalogo.sNombreFantasia}" value="${catalogo.sNombreFantasia}" readonly>
							<input type="hidden" id="catIdPrestador" value="${catalogo.nIdPrestador}">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>
					</div> 
				</div> 
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catCodigo" class="caja_texto obligatorio" value="${catalogo.sCodCatalogo}"  maxlength="20">	 
							 <img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>
					</div>
		
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catNombre" class="caja_texto obligatorio" value="${catalogo.sNombre}" maxlength="80">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	 
						</div>
					</div>
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="catDetEstado" name="buscaCatalogos.sCodEstado" class="obligatorio">
								<option value="" >${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listacatalogo}" var="tipocatalogo">
				   					<option value="${tipocatalogo.parCodParametro01}" <c:if test='${catalogo.sCodEstado == tipocatalogo.parCodParametro01}'> selected</c:if>>${tipocatalogo.parDesLargo01}</option>
			      		 		</c:forEach>
							</select>	
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
							<textarea id="catDescripcion" class="descripcion obligatorio" title="${catalogo.sDescripcion}">${catalogo.sDescripcion} </textarea>		
							<img class="imgNoOk validadorTextArea2" alt='' src='${rutaImg}iconos/badCheck.png' maxlength="200">				
						</div>
					</div>
				</div>  
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaInicio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catFechaInicio" class="caja_texto obligatorio" value="${solemfmt:stringToFecha(catalogo.sFecHoInicio)}">	
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	 
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaFin", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catFechaTermino" class="caja_texto obligatorio" value="${solemfmt:stringToFecha(catalogo.sFecHoTermino)}">	
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>
					</div>
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Responsable", codIdioma)}:</span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="text" id="catNomResp" class="obligatorio" title="${catalogo.sNomResponsable}" value="${catalogo.sNomResponsable}" readonly>
							<input type="hidden" id="catIdResp" value="${catalogo.nIdResponsable}">
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaRespDetCat" value="" role="button" aria-disabled="false">
							 <img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>	
				</div>  
				
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.Plantilla", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="catIndPlantilla" <c:if test="${catalogo.sIndPlantilla == 'S'}">checked</c:if>>
						</div> 	
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.CatalogoPublico", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="catIndPublico" <c:if test="${catalogo.sIndPublico == 'S'}">checked</c:if>>
						</div> 	
					</div> 
				</div> 
			</div>
		</div>  
		<div id="cont_sec_det_prest" class="cont_sec_det_tab">  
			<div id="detalleprest" class="contenido_detalle_tab sombra"> 
				<div class="contenidoTab">
					<div class="tabber"> 
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.Prestaciones", codIdioma)}</h2>
							<p><%@ include file="prestaciones/listaPrestacionesCatalogo.jsp"%></p>
						</div> 
					</div>
				</div>
				<div class="espacio10"></div>
			</div>
		</div>					
	</form>
</div>