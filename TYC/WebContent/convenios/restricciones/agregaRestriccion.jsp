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
		 
		$("#restFechaIniAgrega").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#restFechaFinAgrega").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$("#restFechaFinAgrega").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#restFechaIniAgrega").datepicker( "option", "maxDate", selectedDate );
			}
		});   
		
		$('#restRango1Agrega').timepicker({});
		$('#restRango2Agrega').timepicker({});
	      
		$('input#guardarRestriccion').click(function(){
			if (validaFiltrosAgregaRestriccion())
			{
				agregarRestriccion(recargar);
			} 
		}); 

		$('input#cancelarAgregar').click(function(){
			window.parent.$('#ifrmRestriccion').dialog('close');  
		});

	});
	
	function agregarRestriccion(callback)
	{
	    $.ajax({
	        async: false,
		    type: "POST",
		    url: getUrlAgregarRestriccion(),
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
	
	function getUrlAgregarRestriccion()
	{
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarRestriccion';
		sData += '&restriccion.nIdConvenio=' + $('#restIdConvenioAgrega').val();
		sData += '&restriccion.sNombre=' + $('#restNombreAgrega').val();
		sData += '&restriccion.sCodTipo=' + $('#restTipoAgrega').val();
		if($('#restTipoAgrega').val() == 'HORARIA'){
			sData += '&restriccion.sValor=';// + $('#restValorAgrega').val();
			sData += '&restriccion.sRango1=' + $('#restRango1Agrega').val();
			sData += '&restriccion.sRango2=' + $('#restRango2Agrega').val();
		}
		if($('#restTipoAgrega').val() == "ADMISION")
		{
			sData += '&restriccion.sValor=' + $('#restValorAdmisionAgrega').val();
			sData += '&restriccion.sRango1=';// + $('#restRango1Agrega').val();
			sData += '&restriccion.sRango2=';// + $('#restRango2Agrega').val();
		}
		else{
			sData += '&restriccion.sValor=' + $('#restValorCategoriaAgrega').val();
			sData += '&restriccion.sRango1=';// + $('#restRango1Agrega').val();
			sData += '&restriccion.sRango2=';// + $('#restRango2Agrega').val();
		}
		sData += '&restriccion.sFhoInicio=' + desformateaFechaSinHora('#restFechaIniAgrega');
		sData += '&restriccion.sFhoFin=' + desformateaFechaSinHora('#restFechaFinAgrega');
		sData += '&restriccion.nIdUsuarioCreacion=${sesion.sesIdUsuario}';		
		//console.log(sData);
		return sData;
	}

	function validaFiltrosAgregaRestriccion()
	{
		var cont = 0; 
		$("#contieneAgregaRestriccion .obligatorio").each(function(){
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
		if($('#restTipoAgrega').val() == "HORARIA")
		{
			$('#filaRangos').show();
			$('#filaValorCategoria').hide();
			$('#filaValorAdmision').hide();
			return;
		}
		if($('#restTipoAgrega').val() == "ADMISION")
		{
			$('#filaValorAdmision').show();
			$('#filaValorCategoria').hide();
			$('#filaRangos').hide();
			return;
		}
		if($('#restTipoAgrega').val() == "CATEGORIA")
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
<div id="guardarRestriccion" class="detalle" style="height:208px;background:#C4C3A5">
	<form id="formAgregaRestriccion" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="height: 164px;width: 910px">
			<div id="titulo01" class="titulo" style="width: 907px">
				<div id="contieneFlecha" style="float: left;height: 25px;width: 14px;">
			 	</div>
				<div id="contienetitulo" class="contienetitulo">
					<span>${solemfmt:getLabel("label.AgregaRestriccion", codIdioma)}</span> 
				</div> 
			</div> 
			<div id="contieneAgregaRestriccion" class="contenido_detalle sombra" style="width: 905px">
				<div class="filaDialog alturaFila">
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.IdConvenio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="restIdConvenioAgrega" class="caja_texto" value="${idConvenio}" readonly class="obligatorio">
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>	 
						</div>
					</div>
					
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="restNombreAgrega" class="caja_texto" value="" style="width: 472px;" class="obligatorio">
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
							<input type="text" id="restFechaIniAgrega" class="caja_texto" value="" class="obligatorio">	 
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.FechaFin", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="restFechaFinAgrega" class="caja_texto" value="" class="obligatorio">
							<img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Tipo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="restTipoAgrega" class=" obligatorio" style="width:89%;" onChange="habilitaRangos();">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaTipos}" var="listaTipos">
									<option value="${listaTipos.parCodParametro01}">${listaTipos.parDesLargo01}</option>
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
							<select id="restValorCategoriaAgrega" style="width:89%;">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaCategorias}" var="listaCategorias">
									<option value="${listaCategorias.parCodParametro01}">${listaCategorias.parDesLargo01}</option>
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
							<select id="restValorAdmisionAgrega" style="width:89%;">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaTiposAdmision}" var="listaTiposAdmision">
									<option value="${listaTiposAdmision.parCodParametro01}">${listaTiposAdmision.parDesLargo01}</option>
								</c:forEach>
							</select>	  
						</div>
					</div>
				</div>
				<div class="filaDialog alturaFila" id="filaRangos" style="display: none;">	
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Rango1", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="hidden" id="conIdPlan">
							<input type="text" id="restRango1Agrega" class="caja_texto" value="" maxlength="5" class="obligatorio">
						</div>
					</div>
				
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Rango2", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="restRango2Agrega" class="caja_texto" value="" maxlength="5" class="obligatorio">
						</div>
					</div>
				</div>
				
				<div class="espacio10"></div> 
			</div>
		</div>	
		<div style="width: 274px;height: 28px;margin-left: 360px;margin-top: 4px;">
			<div id="btnAgregarRestriccion" style="height: 25px;padding-top: 3px;text-align: right;margin-right: 18px;float: left;">
				<input class="button" type="button" id="guardarRestriccion" value="${solemfmt:getLabel('boton.Guardar', codIdioma)}" role="button" aria-disabled="false">
			</div>
			<div id="btnCancelarAgregar" style="height: 25px;padding-top: 3px;text-align: right;margin-right: 18px;float: left;">
				<input class="button" type="button" id="cancelarAgregar" value="${solemfmt:getLabel('boton.Cancelar', codIdioma)}" role="button" aria-disabled="false">
			</div>
		</div>		  
	</form>
</div>