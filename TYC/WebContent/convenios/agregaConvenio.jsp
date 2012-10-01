<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
 
	$(function(){
		
		$.datepicker.setDefaults($.datepicker.regional['es']);
		
		$('input[type="checkbox"]').ezMark();

		$("#conFhoIniAgrega").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#conFhoFinAgrega").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$("#conFhoFinAgrega").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#conFhoIniAgrega").datepicker( "option", "maxDate", selectedDate );
			}
		});    
 
		$('input#guardarConvenio').click(function()
		{
			if (validaFiltrosAgregaConvenio()){
				$('#formAgregaConvenio').attr('action', getUrlAgregaConvenio());
				$('#formAgregaConvenio').submit();
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

		$('input#buscaInstAgregaConv').click(function(){
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

		$('input#buscaCliAgregaConv').click(function(){
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

		$('input#buscaRespAgregaConv').click(function(){
			if($('#conIdPres').val()){
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
			}
			else
			{
				alert("${solemfmt:getLabel('alert.DebeSeleccionarPrestador', codIdioma)}");
			}
		});

		$('#conPorcRecAgrega').blur(function(e){
	    	soloNumerosOnBlur('conPorcRecAgrega');
		});

	    $('#conPorcRecAgrega').keypress(function(e){
	    	return soloNumeros(e);
	    });	

	    $('#conCupoAgrega').blur(function(e){
	    	soloNumerosOnBlur('conCupoAgrega');
		});

	    $('#conCupoAgrega').keypress(function(e){
	    	return soloNumeros(e);
	    });	
	}); 

	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}
	
	function getUrlAgregaConvenio()
	{
		//PARA GETXML
		//var sData  = 'KSI=${sesion.sesIdSesion}';
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarConvenio';
		sData += '&convenio.nIdPrestador=' + $('#conIdPres').val();
		sData += '&convenio.nIdCliente=' + $('#conIdCliente').val();
		sData += '&convenio.sCodTipo=' + $('#conTipoAgrega').val();
		sData += '&convenio.sCodConvenio=' + $('#conCodAgrega').val();
		sData += '&convenio.sNombre=' + $('#conNombreAgrega').val();
		sData += '&convenio.sNomPoliza=' + $('#conNomPolizaAgrega').val();
		sData += '&convenio.sCodClase=' + $('#conClaseAgrega').val();
		sData += '&convenio.sCodSubClase=' + $('#conSubClaseAgrega').val();
		sData += '&convenio.sCodEstado=' + $('#conEstadoAgrega').val();
		sData += '&convenio.sDescripcion=' + $('#conDescAgrega').val();
		sData += '&convenio.sFhoInicio=' + desformateaFechaSinHora('#conFhoIniAgrega');
		sData += '&convenio.sFhoTermino=' + desformateaFechaSinHora('#conFhoFinAgrega');
		sData += '&convenio.nIdResponsable=' + $('#conIdResp').val();
		sData += '&convenio.sIndRecaudacion=' +  devuelveEstadoCheck($('#conIndRecaudAgrega'));
		sData += '&convenio.sIndPlantilla=' +  devuelveEstadoCheck($('#conIndPlantillaAgrega'));
		sData += '&convenio.sIndConvenioAbierto=' +  devuelveEstadoCheck($('#conConvAbiertoAgrega'));
		sData += '&convenio.sIndFacturaPrestador=' +  devuelveEstadoCheck($('#conIndFacturaAgrega'));
		sData += '&convenio.nPorRecaudacion=' + $('#conPorcRecAgrega').val();
		sData += '&convenio.nCupoConvenio=' + $('#conCupoAgrega').val();
		sData += '&convenio.nIdCreador=${sesion.sesIdUsuario}';
		return sData;
	}

    function getUrlCargaConvenio() 
    {      
    	var sData = 'SvtConvenios';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=cargarConvenio';
      	sData += '&idConvenio=${idConvenio}';
      	sData += '&indRuta=S';
    	return sData;
    }

	function validaFiltrosAgregaConvenio()
	{
		var cont = 0; 
		$("#contieneAgregaConvenio .obligatorio").each(function(){ 
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

	//funcion necesaria para cargar institucion 
	function buscarConvenios()
	{
		return null;
	}

	function validaPorcentaje()
	{
		var porcentaje =  $('#conPorcRecAgrega').val();
		if(porcentaje<0 || porcentaje>100)
		{
			alert('${solemfmt:getLabel("alert.PorcentajeEntre0y100", codIdioma)}');
			$('#conPorcRecAgrega').val('');	
		}
	}

</script> 

<div id="agregaConvenio" class="detalle">
	<form id="formAgregaConvenio" method="post">
		<input type="hidden" id="idConvenio" value="${idConvenio}">
		<div id="cont_sec_det_01" class="cont_sec_det">
			<div id="titulo01" class="titulo">  
				<div id="contienetitulo" class="contieneTituloAgrega">
					<span>${solemfmt:getLabel("label.AgregaConvenio", codIdioma)}</span>
				</div>
				<div class="divBtn" style="float: left;">
					<input class="button" type="button" id="guardarConvenio" value="${solemfmt:getLabel('boton.Guardar', codIdioma)}" role="button" aria-disabled="false">
				</div> 
			</div>  
			<div id="contieneAgregaConvenio" class="contenido_detalle sombra">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Prestador", codIdioma)}: </span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="hidden" id="conIdPres" value="">
							<input type="text" id="conNomPres" class="obligatorio" readonly>
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaInstAgregaConv" value="" role="button" aria-disabled="false">
							 <img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>
					</div>  
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Cliente", codIdioma)}: </span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="hidden" id="conIdCliente">
							<input type="text" id="conNomCliente" class="obligatorio" readonly>
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaCliAgregaConv" value="" role="button" aria-disabled="false">
							 <img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>	
					</div>
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.TipoConvenio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="conTipoAgrega" class="obligatorio">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaTipos}" var="listaTipos">
									<option value="${listaTipos.parCodParametro01}">${listaTipos.parDesLargo01}</option>
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
							<input type="text" id="conCodAgrega" class="caja_texto obligatorio" value="" maxlength="20">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conNombreAgrega" class="caja_texto obligatorio" value="" maxlength="200">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Poliza", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conNomPolizaAgrega" class="caja_texto obligatorio" value="" maxlength="200">
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
							<select id="conClaseAgrega" class="obligatorio">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaClases}" var="listaClases">
									<option value="${listaClases.parCodParametro01}">${listaClases.parDesLargo01}</option>
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
							<select id="conSubClaseAgrega" class="obligatorio">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaSubClases}" var="listaSubClases">
									<option value="${listaSubClases.parCodParametro01}">${listaSubClases.parDesLargo01}</option>
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
							<select id="conEstadoAgrega" class="obligatorio">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaEstadosConvenio}" var="listaEstados">
									<option value="${listaEstados.parCodParametro01}">${listaEstados.parDesLargo01}</option>
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
							<textarea id="conDescAgrega" class="descripcion obligatorio" maxlength="200"></textarea>	
							<img class="imgNoOk validadorCampoDirecc" alt='' src='${rutaImg}iconos/badCheck.png'>						
						</div>
					</div>
				</div> 
				<div class="fila alturaFila"> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaInicio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conFhoIniAgrega" class="caja_texto obligatorio" value="">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>							
						</div>																
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaFin", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conFhoFinAgrega" class="caja_texto obligatorio" value="">	
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>						
						</div>																	
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Responsable", codIdioma)}:</span>
						</div>
						<div class="valor_campolupa campolupa_large">
							<input type="text" id="conNomResp" class="obligatorio" title="" value="" readonly>
							<input type="hidden" id="conIdResp" value="">
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaRespAgregaConv" value="" role="button" aria-disabled="false">
							 <img class="imgNoOk validadorLupa2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
				</div>
			
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.Recaudacion", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox" style="float: left;">
							<input type="checkbox" id=conIndRecaudAgrega style="margin-top: 5px;">
 						</div> 	
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>% ${solemfmt:getLabel("label.Recaudacion", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conPorcRecAgrega" onChange="validaPorcentaje();" class="caja_texto obligatorio" value="" style="text-align:right">	
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>						
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.CupoConvenio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conCupoAgrega" class="caja_texto obligatorio" value=""  style="text-align:right">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>		
						</div>
					</div>
				</div> 
				<div class="fila alturaFila">	
					<div class="campo campo_detalle">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.Plantilla", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox" style="float: left;">
							<input type="checkbox" id="conIndPlantillaAgrega" style="margin-top: 5px;">
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.ConvenioAbierto", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox" style="float: left;">
							<input type="checkbox" id="conConvAbiertoAgrega" style="margin-top: 5px;">
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.FacturaPrestador", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox" style="float: left;">
							<input type="checkbox" id="conIndFacturaAgrega" style="margin-top: 5px;">
						</div>
					</div>	
				</div> 
			</div>
		</div> 
	</form>
</div>