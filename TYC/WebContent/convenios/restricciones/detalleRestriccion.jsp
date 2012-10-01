<%@ page language="java"%>
<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezado.jsp"%>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<script type="text/javascript">
 
	$(function()
	{
		$('input[type="checkbox"]').ezMark();	
		$.datepicker.setDefaults($.datepicker.regional['es']);
		 
		$("#restFechaIniActualiza").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#restFechaFinActualiza").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$("#restFechaFinActualiza").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#restFechaIniActualiza").datepicker( "option", "maxDate", selectedDate );
			}
		}); 
		
		$('#restRango1Actualiza').timepicker({});
		$('#restRango2Actualiza').timepicker({});

		$('#filaRangos').hide();
		$('#filaValorCategoria').hide();
		$('#filaValorAdmision').hide();
	      
		$('input#actualizarRestriccion').click(function(){
			if (validaFiltrosActualizaRestriccion())
			{
				actualizarRestriccion(recargar);
			} 
		}); 

		$('input#cancelarActualizar').click(function(){
			window.parent.$('#ifrmRestriccion').dialog('close');  
		});

		habilitaRangos();
	});
	
	function actualizarRestriccion(callback)
	{
	    $.ajax({
	        async: false,
		    type: "POST",
		    url: getUrlActualizarRestriccion(),
		    cache: false,
		    dataType: "html",
		    beforeSend: function(data){ 
	            capaLoading("show");
		    },
		    error: function(requestData, strError, strTipoError){
		    	var sMensajeError = "error carga contenido (" + strTipoError + " : " + strError + ")";
		    	var oError = null;
		        oError = new Object;
	            oError.numero = strError;
	            oError.mensaje = sMensajeError;
	  	        capaLoading("hide");
				if (oError){
					alert("("+oError.numero+")"+oError.mensaje);
				}
		    },
		    success: function(respuestaHtml){
		    	capaLoading("hide");
		    	if(callback) callback();
		    }
		});
	}

	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}

	function recargar(){
		window.parent.recargarBusquedaRestricciones();
		window.parent.$('#ifrmRestriccion').dialog('close');
	}
	
	function getUrlActualizarRestriccion()
	{
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=actualizarRestriccion';
		sData += '&restriccion.nIdRestriccion=' + $('#restIdRestriccionActualiza').val();
		sData += '&restriccion.sNombre=' + $('#restNombreActualiza').val();
		sData += '&restriccion.sCodTipo=' + $('#restTipoActualiza').val();
		if($('#restTipoActualiza').val() == 'HORARIA'){
			sData += '&restriccion.sValor=';// + $('#restValorActualiza').val();
			sData += '&restriccion.sRango1=' + $('#restRango1Actualiza').val();
			sData += '&restriccion.sRango2=' + $('#restRango2Actualiza').val();
		}
		if($('#restTipoActualiza').val() == "ADMISION")
		{
			sData += '&restriccion.sValor=' + $('#restValorAdmisionActualiza').val();
			sData += '&restriccion.sRango1=';// + $('#restRango1Actualiza').val();
			sData += '&restriccion.sRango2=';// + $('#restRango2Actualiza').val();
		}
		else{
			sData += '&restriccion.sValor=' + $('#restValorCategoriaActualiza').val();
			sData += '&restriccion.sRango1=';// + $('#restRango1Actualiza').val();
			sData += '&restriccion.sRango2=';// + $('#restRango2Actualiza').val();
		}
		sData += '&restriccion.sFhoInicio=' + desformateaFechaSinHora('#restFechaIniActualiza');
		sData += '&restriccion.sFhoFin=' + desformateaFechaSinHora('#restFechaFinActualiza');
		sData += '&restriccion.nIdUsuarioCreacion=${sesion.sesIdUsuario}';		
		//console.log(sData);
		return sData;
	}

	function validaFiltrosActualizaRestriccion()
	{
		var cont = 0; 
		$("#contieneActualizaRestriccion .obligatorio").each(function(){
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

	function habilitaRangos()
	{
		if($('#restTipoActualiza').val() == "HORARIA")
		{
			$('#filaRangos').show();
			$('#filaValorCategoria').hide();
			$('#filaValorAdmision').hide();
			return;
		}

		if($('#restTipoActualiza').val() == "ADMISION")
		{
			$('#filaValorAdmision').show();
			$('#filaValorCategoria').hide();
			$('#filaRangos').hide();
			return;
		}
		if($('#restTipoActualiza').val() == "CATEGORIA")
		{
			$('#filaValorAdmision').hide();
			$('#filaValorCategoria').show();
			$('#filaRangos').hide();
		}
		else
		{
			$('#filaRangos').hide();
			$('#filaValorCategoria').hide();
			$('#filaValorAdmision').hide();
			return;
		}
	}	
 
</script>
<div id="actualizarRestriccion" class="detalle" style="height:188px;background:#C4C3A5">
	<form id="formActualizaRestriccion" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="width: 910px">
			<div id="titulo01" class="titulo" style="width: 907px">
				<div id="contieneFlecha" style="float: left;height: 25px;width: 14px;">
			 	</div>
				<div id="contienetitulo" class="contienetitulo">
					<span>${solemfmt:getLabel("label.DetalleRestriccion", codIdioma)}</span> 
				</div> 
			</div> 
			<div id="contieneActualizaRestriccion" class="contenido_detalle sombra" style="width: 905px">
				<div class="filaDialog alturaFila">
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.IdConvenio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="hidden" id="restIdRestriccionActualiza" value="${restriccion.nIdRestriccion}">
							<input type="text" id="restIdConvenioActualiza" class="caja_texto" value="${restriccion.nIdConvenio}" readonly class="obligatorio">
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>	 
						</div>
					</div>
					
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="restNombreActualiza" class="caja_texto" value="${restriccion.sNombre}" style="width: 472px;" class="obligatorio">
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
				
				</div>
				
				<div class="filaDialog alturaFila">
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.FechaInicio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="restFechaIniActualiza" class="caja_texto" value="${solemfmt:stringToFecha(restriccion.sFhoInicio)}" class="obligatorio">	 
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.FechaFin", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="restFechaFinActualiza" class="caja_texto" value="${solemfmt:stringToFecha(restriccion.sFhoFin)}" class="obligatorio">
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Tipo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="restTipoActualiza" class=" obligatorio" style="width:89%;" onChange="habilitaRangos();">
								<c:forEach items="${requestScope.listaTipos}" var="listaTipos">
									<option value="${listaTipos.parCodParametro01}" <c:if test='${restriccion.sCodTipo == listaTipos.parCodParametro01}'> selected</c:if>>${listaTipos.parDesLargo01}</option>
								</c:forEach>
							</select>
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
				</div>
				
				<div class="filaDialog alturaFila" id="filaValorCategoria" style="display: none;">
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Valor", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="restValorCategoriaActualiza" style="width:89%;">
								<c:forEach items="${requestScope.listaCategorias}" var="listaCategorias">
									<option value="${listaCategorias.parCodParametro01}" <c:if test='${restriccion.sValor == listaCategorias.parCodParametro01}'> selected</c:if>>${listaCategorias.parDesLargo01}</option>
								</c:forEach>
							</select>	 
						</div>
					</div>
				</div>
				<div class="filaDialog alturaFila" id="filaValorAdmision" style="display: none;">
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Valor", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="restValorAdmisionActualiza" style="width:89%;">
								<c:forEach items="${requestScope.listaTiposAdmision}" var="listaTiposAdmision">
									<option value="${listaTiposAdmision.parCodParametro01}" <c:if test='${restriccion.sValor == listaTiposAdmision.parCodParametro01}'> selected</c:if>>${listaTiposAdmision.parDesLargo01}</option>
								</c:forEach>
							</select>	  
						</div>
					</div>
				</div>
				
				<div class="filaDialog alturaFila" id="filaRangos">	
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Rango1", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="hidden" id="conIdPlan">
							<input type="text" id="restRango1Actualiza" class="caja_texto" value="${restriccion.sRango1}" maxlength="5" class="obligatorio">
						</div>
					</div>
				
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Rango2", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="restRango2Actualiza" class="caja_texto" value="${restriccion.sRango2}" maxlength="5" class="obligatorio">
						</div>
					</div>
				</div>
				
				<div class="espacio10"></div> 
			</div>
		</div>	
		<div style="width: 274px;height: 28px;margin-left: 360px;margin-top: 4px;">
			<div id="btnActualizarRestriccion" style="height: 25px;padding-top: 3px;text-align: right;margin-right: 18px;float: left;">
				<input class="button" type="button" id="actualizarRestriccion" value="Guardar" role="button" aria-disabled="false">
			</div>
			<div id="btnCancelarActualizar" style="height: 25px;padding-top: 3px;text-align: right;margin-right: 18px;float: left;">
				<input class="button" type="button" id="cancelarActualizar" value="Cancelar" role="button" aria-disabled="false">
			</div>
			
		</div>		  
	</form>
</div>