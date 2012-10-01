<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">

    $(function(){
		$('input[type="checkbox"]').ezMark();
	 
		$('input#actualizarPrestacion').click(function(){ 
			if (validaFiltrosActualizaPrestaciones()) {
				getXml("SvtPrestaciones", getUrlActualizaPrestacion(), function(xml){
					alert('${solemfmt:getLabel("alert.PrestacionActualizada", codIdioma)}'); 
				});
			}
		});

		function getUrlActualizaPrestacion()
		{
			var sData = 'KSI=${sesion.sesIdSesion}';
			sData += '&accion=actualizarPrestacion';
			sData += '&prestacion.nIdPrestacion=' + $('#idPrestacion').val();
			sData += '&prestacion.sCodPrestacion=' + $('#presCodDetalle').val();
			sData += '&prestacion.sNombre=' + $('#presNombreDetalle').val();
			sData += '&prestacion.sDescripcion=' + $('#presDescDetalle').val();
			sData += '&prestacion.sCodEstado=' + $('#presEstadoDetalle').val();
			sData += '&prestacion.sCodTipoPrestacion=' + $('#presTipoDetalle').val();
			sData += '&prestacion.sCodSubTipoPrestacion=' + $('#presSubTipoDetalle').val();
			sData += '&prestacion.sCodUnidadMedida=' + $('#presUniMedDetalle').val();
			sData += '&prestacion.sIndCerrada=' + devuelveEstadoCheck($('#presIndCerrada'));
			sData += '&prestacion.sIndCompuesta=' + devuelveEstadoCheck($('#presIndCompuesta'));
			return sData;
		}
		
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

    function validaFiltrosActualizaPrestaciones(){
		var cont = 0; 
		$("#contieneDetallePrestacion .obligatorio").each(function(){ 
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

	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}
</script> 

<div id="detallePrestacion" class="detalle">
	<form id="formDetallePrestacion" method="post">
		<input type="hidden" id="idPrestacion" value="${prestacion.nIdPrestacion}">
		<div id="contenedorPrestacionDetalle1" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="contieneFlecha" class="contieneFlecha"> 
					<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">  
				</div>
				<div id="contienetitulo" class="contienetitulo" style="width:716px;"> 
					<span>${solemfmt:getLabel("label.Prestacion", codIdioma)} "${prestacion.sNombre}"</span>  
				</div>
				<div class="divBtn">
					<input class="button" type="button" id="actualizarPrestacion" value="${solemfmt:getLabel('boton.Actualizar', codIdioma)}" role="button" aria-disabled="false">
				</div>		
			</div>  
			<div id="contieneDetallePrestacion" class="contenido_detalle sombra">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="presCodDetalle" class="caja_texto obligatorio" value="${prestacion.sCodPrestacion}" maxlength="50" readonly>
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="presNombreDetalle" class="caja_texto obligatorio" value="${prestacion.sNombre}" maxlength="200">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="presEstadoDetalle" class="obligatorio">
								<c:forEach items="${requestScope.listaEstadosPrestacion}" var="listaEstados">
									<option value="${listaEstados.parCodParametro01}" <c:if test='${prestacion.sCodEstado == listaEstados.parCodParametro01}'> selected</c:if>>${listaEstados.parDesLargo01}</option>
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
							<select id="presTipoDetalle" class="obligatorio">
								<c:forEach items="${requestScope.listaTipos}" var="listaTipos">
									<option value="${listaTipos.parCodParametro01}" <c:if test='${prestacion.sCodTipoPrestacion == listaTipos.parCodParametro01}'> selected</c:if>>${listaTipos.parDesLargo01}</option>
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
							<select id="presSubTipoDetalle" class="obligatorio">
								<c:forEach items="${requestScope.listaSubTipos}" var="listaSubTipos">
									<option value="${listaSubTipos.parCodParametro01}" <c:if test='${prestacion.sCodSubTipoPrestacion == listaSubTipos.parCodParametro01}'> selected</c:if>>${listaSubTipos.parDesLargo01}</option>
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
							<select id="presUniMedDetalle" class="obligatorio">
								<c:forEach items="${requestScope.unidadMedida}" var="unidadMedida">
									<option value="${unidadMedida.parCodParametro01}" <c:if test='${prestacion.sCodUnidadMedida == unidadMedida.parCodParametro01}'> selected</c:if>>${unidadMedida.parDesLargo01}</option>
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
							<textarea id="presDescDetalle" class="descripcion obligatorio" maxlength="300">${prestacion.sDescripcion}</textarea>	
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
							<input type="checkbox" id="presIndCerrada" <c:if test="${prestacion.sIndCerrada == 'S'}">checked</c:if> style="margin-top: 5px;">
						</div>
					</div> 
					<div class="campo campo_det_small">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.Compuesta", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox" style="float: left;">
							<input type="checkbox" id="presIndCompuesta" <c:if test="${prestacion.sIndCompuesta == 'S'}">checked</c:if> style="margin-top: 5px;">
						</div>
					</div> 
				</div>
			</div>
		</div>  
		<div id="contenedorPrestacionDetalle2" class="cont_sec_det_tab">  
			<div id="detalle02" class="contenido_detalle_tab sombra"> 
				<div class="contenidoTab">
					<div class="tabber"> 
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.PrestacionesCompuestas", codIdioma)}</h2>
							<p><%@ include file="compuestas/listaPrestacionesCompuestas.jsp" %></p>
						</div> 
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.PrestacionesNacionales", codIdioma)}</h2> 
							<p><%@ include file="prestacion_nacional/listaPrestacionNacional.jsp" %></p>
						</div>
					</div>
				</div>
				<div class="espacio10"></div>
			</div>
		</div>			
	</form>
</div>