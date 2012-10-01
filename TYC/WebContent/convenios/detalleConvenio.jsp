<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<script type="text/javascript">
 
	$(function(){
		$.datepicker.setDefaults($.datepicker.regional['es']);
		$('input[type="checkbox"]').ezMark();
		 
		$("#conFhoIniDetalle").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#conFhoFinDetalle").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$("#conFhoFinDetalle").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#conFhoIniDetalle").datepicker( "option", "maxDate", selectedDate );
			}
		});    
		 
		$('input#actualizarConvenio').click(function(){
			if (validaFiltrosActualizaConvenio()){
				getXml("SvtConvenios", getUrlDetalleConvenio(), function(xml)
				{
					alert('${solemfmt:getLabel("alert.ConvenioActualizado", codIdioma)}'); 
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
		
		$('input#buscaInstDetConv').click(function(){
				$('#titlePopup').parent().remove();
				$('iframe').remove();
				var myDiv = document.getElementById('contenidoPrincipal');
				var xDiv = getDimensions(myDiv).x+104;
				var yDiv = getDimensions(myDiv).y+85; 

				var url = "SvtConvenios?KSI=${sesion.sesIdSesion}&accion=cargaEmpresasInst";
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
					title:'${solemfmt:getLabel("label.InstitucionesPrestadoras", codIdioma)}'
				}).width(777).height(382);
				$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
				$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
				$('.ui-dialog-titlebar').css("display","none"); 
		});

		$('input#buscaCliDetConv').click(function(){
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y+85; 

			var url = "SvtConvenios?KSI=${sesion.sesIdSesion}&accion=cargaEmpresasCli";
			var iframe =  $('<iframe id="ifrmClientes" src="'+url+'"/>'); 
			
			iframe.dialog({
				autoOpen: true,
				modal: true,
				height: 382,
				width: 777,
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title:'${solemfmt:getLabel("label.EmpresasClientes", codIdioma)}' 
			}).width(777).height(382);
			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
		});			

		$('input#buscaRespDetConv').click(function(){
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y+85; 

			var url = "SvtConvenios?KSI=${sesion.sesIdSesion}&accion=cargaResponsables&idInst="+$('#conIdPres').val();
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
		});	

		$('#conPorcRecDetalle').blur(function(e){
	    	soloNumerosOnBlur('conPorcRecDetalle');
		});

	    $('#conPorcRecDetalle').keypress(function(e){
	    	return soloNumeros(e);
	    });	

	    $('#conCupoDetalle').blur(function(e){
	    	soloNumerosOnBlur('conCupoDetalle');
		});

	    $('#conCupoDetalle').keypress(function(e){
	    	return soloNumeros(e);
	    });	
	}); 

	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}
	
	function getUrlDetalleConvenio(){
		var sData ='KSI=${sesion.sesIdSesion}';
		sData += '&accion=actualizarConvenio';
		sData += '&convenio.nIdConvenio=' + $('#conIdDetalle').val();
		sData += '&convenio.nIdPrestador=' + $('#conIdPres').val();
		sData += '&convenio.nIdCliente=' + $('#conIdCliente').val();
		sData += '&convenio.sCodTipo=' + $('#conTipoDetalle').val();
		sData += '&convenio.sCodConvenio=' + $('#conCodDetalle').val();
		sData += '&convenio.sNombre=' + $('#conNombreDetalle').val();
		sData += '&convenio.sNomPoliza=' + $('#conNomPolizaDetalle').val();
		sData += '&convenio.sCodClase=' + $('#conClaseDetalle').val();
		sData += '&convenio.sCodSubClase=' + $('#conSubClaseDetalle').val();
		sData += '&convenio.sCodEstado=' + $('#conEstadoDetalle').val();
		sData += '&convenio.sDescripcion=' + $('#conDescDetalle').val();
		sData += '&convenio.sFhoInicio=' + desformateaFechaSinHora('#conFhoIniDetalle');
		sData += '&convenio.sFhoTermino=' + desformateaFechaSinHora('#conFhoFinDetalle');
		sData += '&convenio.nIdResponsable=' + $('#conIdResp').val();
		sData += '&convenio.sIndRecaudacion=' + devuelveEstadoCheck($('#conIndRecaudDetalle'));
		sData += '&convenio.sIndPlantilla=' + devuelveEstadoCheck($('#conIndPlantillaDetalle'));
		sData += '&convenio.sIndConvenioAbierto=' + devuelveEstadoCheck($('#conConvAbiertoDetalle'));
		sData += '&convenio.sIndFacturaPrestador=' + devuelveEstadoCheck($('#conIndFacturaDetalle'));
		sData += '&convenio.nPorRecaudacion=' + $('#conPorcRecDetalle').val();
		sData += '&convenio.nCupoConvenio=' + $('#conCupoDetalle').val();
		sData += '&convenio.nIdActualizador=${sesion.sesIdUsuario}';
		return sData;
	}

	function validaFiltrosActualizaConvenio(){
		var cont = 0; 
		$("#contieneDetalleConvenio .obligatorio").each(function(){
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

	function buscarConvenios(){
		
	}

	function validaPorcentaje()
	{
		var porcentaje =  $('#conPorcRecDetalle').val();
		if(porcentaje<0 || porcentaje>100)
		{
			alert('${solemfmt:getLabel("alert.PorcentajeEntre0y100", codIdioma)}');
			$('#conPorcRecDetalle').val('');	
		}
	}
	
</script> 

<div id="detalleConvenio" class="detalle">
	<form id="formDetalleConvenio" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="contieneFlecha" class="contieneFlecha"> 
					<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">  
				</div>
				<div id="contienetitulo" class="contienetitulo">
					<span>${solemfmt:getLabel("label.Detalle", codIdioma)} "${convenio.sNombre}"</span>
				</div> 
				<div class="divBtn" >
					<input class="button" type="button" id="actualizarConvenio" value="${solemfmt:getLabel('boton.Actualizar', codIdioma)}" role="button" aria-disabled="false">
				</div> 
			</div> 
			<div id="contieneDetalleConvenio" class="contenido_detalle sombra">
				<input type="hidden" id="conIdDetalle" value="${convenio.nIdConvenio}">
				
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Prestador", codIdioma)}: </span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="hidden" id="conIdPres" value="${convenio.nIdPrestador}">
							<input type="text" id="conNomPres" class="obligatorio" value="${convenio.sNomPrestador}" title="${convenio.sNomPrestador}" readonly>
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaInstDetConv" value="" role="button" aria-disabled="false">
							 <img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
		
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Cliente", codIdioma)}: </span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="hidden" id="conIdCliente" value="${convenio.nIdCliente}">
							<input type="text" id="conNomCliente" class=" obligatorio" value="${convenio.sNomCliente}" title="${convenio.sNomCliente}" readonly>
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaCliDetConv" value="" role="button" aria-disabled="false">
							 <img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.TipoConvenio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="conTipoDetalle" class=" obligatorio">
								<c:forEach items="${requestScope.listaTipos}" var="listaTipos">
									<option value="${listaTipos.parCodParametro01}" <c:if test='${convenio.sCodTipo == listaTipos.parCodParametro01}'> selected</c:if>>${listaTipos.parDesLargo01}</option>
								</c:forEach>
							</select>
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
							<input type="text" id="conCodDetalle" class="caja_texto obligatorio" value="${convenio.sCodConvenio}" maxlength="20">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conNombreDetalle" class="caja_texto obligatorio" value="${convenio.sNombre}" maxlength="200">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
				
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Poliza", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conNomPolizaDetalle" class="caja_texto obligatorio" value="${convenio.sNomPoliza}" maxlength="200">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div> 
					
				</div>
					
				<div class="fila alturaFila">
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Clase", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="conClaseDetalle" class=" obligatorio">
								<c:forEach items="${requestScope.listaClases}" var="listaClases">
									<option value="${listaClases.parCodParametro01}" <c:if test='${convenio.sCodClase == listaClases.parCodParametro01}'> selected</c:if>>${listaClases.parDesLargo01}</option>
								</c:forEach>
							</select>
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
				
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.SubClase", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="conSubClaseDetalle" class="obligatorio">
								<c:forEach items="${requestScope.listaSubClases}" var="listaSubClases">
									<option value="${listaSubClases.parCodParametro01}" <c:if test='${convenio.sCodSubClase == listaSubClases.parCodParametro01}'> selected</c:if>>${listaSubClases.parDesLargo01}</option>
								</c:forEach>
							</select>
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>	 					
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="conEstadoDetalle" class="obligatorio">
								<c:forEach items="${requestScope.listaEstadosConvenio}" var="listaEstados">
									<option value="${listaEstados.parCodParametro01}" <c:if test='${convenio.sCodEstado == listaEstados.parCodParametro01}'> selected</c:if>>${listaEstados.parDesLargo01}</option>
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
							<textarea id="conDescDetalle" class="descripcion obligatorio" title="${convenio.sDescripcion}">${convenio.sDescripcion}</textarea>		
							<img class="imgNoOk validadorTextArea2" alt='' src='${rutaImg}iconos/badCheck.png' maxlenght="2000">					
						</div>
					</div>
				</div>
				
				<div class="fila alturaFila"> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaInicio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conFhoIniDetalle" class="caja_texto obligatorio" value="${solemfmt:stringToFecha(convenio.sFhoInicio)}">			
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>				
						</div>																
					</div>
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaFin", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conFhoFinDetalle" class="caja_texto obligatorio" value="${solemfmt:stringToFecha(convenio.sFhoTermino)}">	
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>						
						</div>																	
					</div>
				
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Responsable", codIdioma)}:</span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="text" id="conNomResp" class="obligatorio" title="${convenio.sNomResponsable}" value="${convenio.sNomResponsable}" readonly>
							<input type="hidden" id="conIdResp" value="${convenio.nIdResponsable}">
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaRespDetConv" value="" role="button" aria-disabled="false">
							 <img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
				</div>
			
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.Recaudacion", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="conIndRecaudDetalle" <c:if test="${convenio.sIndRecaudacion == 'S'}">checked</c:if>>
						</div> 	
					</div>
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>% ${solemfmt:getLabel("label.Recaudacion", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conPorcRecDetalle" class="caja_texto obligatorio" value="${convenio.nPorRecaudacion}" style="text-align:right">		
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>					
						</div>
					</div>
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.CupoConvenio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conCupoDetalle" class="caja_texto obligatorio" value="${convenio.nCupoConvenio}" style="text-align:right" >
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
				</div>
					
				<div class="fila alturaFila">	
					<div class="campo campo_det_small">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.Plantilla", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="conIndPlantillaDetalle" <c:if test="${convenio.sIndPlantilla == 'S'}">checked</c:if>>
						</div>
					</div>
					
					<div class="campo campo_det_small">
						<div class="label_campo labelCampoHorizontal anchoMedium">
							<span>${solemfmt:getLabel("label.ConvenioAbierto", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="conConvAbiertoDetalle" <c:if test="${convenio.sIndConvenioAbierto == 'S'}">checked</c:if>>
						</div>
					</div>
					
					<div class="campo campo_det_small">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.FacturaPrestador", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="conIndFacturaDetalle" <c:if test="${convenio.sIndFacturaPrestador == 'S'}">checked</c:if>>
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
							<h2>${solemfmt:getLabel("label.Anexos", codIdioma)}</h2>
							<p><%@ include file="anexos/listaAnexos.jsp" %></p>
						</div> 
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.Restricciones", codIdioma)}</h2>
							<p><%@ include file="restricciones/listaRestricciones.jsp" %></p>
						</div>
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.Beneficios", codIdioma)}</h2>
							<p><%@ include file="beneficios/listaBeneficios.jsp" %></p>
						</div>
					</div>
				</div>
				<div class="espacio10"></div>
			</div>
		</div>
	</form>
</div>