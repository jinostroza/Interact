<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var cont = 0;

	$(function(){
		$('input[type="checkbox"]').ezMark();
		$('#presFhoIniAgrega').datepicker();
		$('#presFhoFinAgrega').datepicker(); 
 
		$('input#guardarPrestacion').click(function(){
			if (validaFiltrosAgregaPrestacion()){
				/*getXml("SvtPrestaciones", getUrlGuardaPrestacion(), function(xml){
					$('#formAgregaPrestacion').attr('action', getUrlCarPrestacion());
					$('#formAgregaPrestacion').submit();
					alert('${solemfmt:getLabel("alert.PrestacionAgregada", codIdioma)}');
				});*/
				$('#formAgregaPrestacion').attr('action', getUrlGuardaPrestacion());
				$('#formAgregaPrestacion').submit();
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
	});

	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}
	
	function getUrlGuardaPrestacion(){
		var sData = 'SvtPrestaciones';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarPrestacion';
		sData += '&prestacion.sCodPrestacion=' + $('#presCodAgrega').val();
		sData += '&prestacion.sNombre=' + $('#presNombreAgrega').val();
		sData += '&prestacion.sCodEstado=' + $('#presEstadoAgrega').val();
		sData += '&prestacion.sCodTipoPrestacion=' + $('#presTipoAgrega').val();
		sData += '&prestacion.sCodSubTipoPrestacion=' + $('#presSubTipoAgrega').val();
		sData += '&prestacion.sCodUnidadMedida=' + $('#presUniMedAgrega').val();
		sData += '&prestacion.sIndCerrada=' + devuelveEstadoCheck($('#presIndCerrada'));
		sData += '&prestacion.sIndCompuesta=' + devuelveEstadoCheck($('#presIndCompuesta'));
		sData += '&prestacion.sDescripcion=' + $('#presDescAgrega').val();
		return sData;
	}
  
	function validaFiltrosAgregaPrestacion(){
		var cont = 0; 
		$("#contieneAgregaPrestacion .obligatorio").each(function(){
			var value = $.trim($(this).val()); 
			if(value==""){
				$(this).parent().children("img").css("display","block");
				cont++;
			}else{
				$(this).parent().children("img").css("display","none");
			}  
		}); 
		
		if(cont==0)
			return true;
		else
			return false;	
	}	
</script> 

<div id="agregaConvenio" class="detalle">
	<form id="formAgregaPrestacion" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="contienetitulo" class="contieneTituloAgrega">
					<span>${solemfmt:getLabel("label.AgregaPrestacion", codIdioma)}</span>
				</div>
				<div class="divBtn" style="float: left;">
					<input class="button" type="button" id="guardarPrestacion" value="${solemfmt:getLabel('boton.Guardar', codIdioma)}" role="button" aria-disabled="false">
				</div> 
			</div>  
			<div id="contieneAgregaPrestacion" class="contenido_detalle sombra">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="presCodAgrega" class="caja_texto obligatorio" value="" maxlength="50">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="presNombreAgrega" class="caja_texto obligatorio" value="" maxlength="200">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="presEstadoAgrega" class="obligatorio">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaEstadosPrestacion}" var="listaEstados">
									<option value="${listaEstados.parCodParametro01}">${listaEstados.parDesLargo01}</option>
								</c:forEach>
							</select>
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>					
					</div>
				</div>
		
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Tipo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="presTipoAgrega" class="obligatorio">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaTipos}" var="listaTipos">
									<option value="${listaTipos.parCodParametro01}">${listaTipos.parDesLargo01}</option>
								</c:forEach>
							</select>
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>					
					</div>
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.SubTipo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="presSubTipoAgrega" class="obligatorio">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaSubTipos}" var="listaSubTipos">
									<option value="${listaSubTipos.parCodParametro01}">${listaSubTipos.parDesLargo01}</option>
								</c:forEach>
							</select>
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>					
					</div>
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.UnidadMedida", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="presUniMedAgrega" class="obligatorio">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.unidadMedida}" var="unidadMedida">
									<option value="${unidadMedida.parCodParametro01}">${unidadMedida.parDesLargo01}</option>
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
							<textarea id="presDescAgrega" class="descripcion obligatorio" maxlength="300"></textarea>	
							<img class="imgNoOk validadorCampoDirecc" alt='' src='${rutaImg}iconos/badCheck.png'>						
						</div>
					</div>
				</div> 
				<div id="filaCheckbox" class="fila alturaFila"> 
					<div class="campo campo_detalle">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.Cerrada", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox" style="float: left;">
							<input type="checkbox" id="presIndCerrada" style="margin-top: 5px;">
						</div>
					</div> 
					<div class="campo campo_det_small">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.Compuesta", codIdioma)}: </span> 
						</div>
						<div class="contineCheckbox" style="float: left;">
							<input type="checkbox" id="presIndCompuesta" style="margin-top: 5px;">
						</div>
					</div> 
				</div>
			</div>
		</div> 
	</form>
</div>