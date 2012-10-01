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
		 
		$("#caxFhoIniDetalle").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#caxFhoFinDetalle").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$("#caxFhoFinDetalle").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#caxFhoIniDetalle").datepicker( "option", "maxDate", selectedDate );
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
	
		$('input#actualizarAnexo').click(function(){
			if (validaFiltrosActualizaAnexo()){
				getXml("SvtConvenios", getUrlDetalleAnexo(), function(xml){
					alert('${solemfmt:getLabel("alert.AnexoActualizado",codIdioma)}'); 
		       	});	
			}
		});
	}); 

	function getUrlDetalleAnexo(){
		var sData = 'KSI=${sesion.sesIdSesion}';
		sData += '&accion=actualizarAnexo';
		sData += '&anexo.nIdAnexo=' + $('#caxIdDetalle').val();
		sData += '&anexo.sNombreAnexo=' + $('#caxNombreDetalle').val();
		sData += '&anexo.sDescripcion=' + $('#caxDescDetalle').val();
		sData += '&anexo.sFhoInicio=' + desformateaFechaSinHora('#caxFhoIniDetalle');
		sData += '&anexo.sFhoTermino=' + desformateaFechaSinHora('#caxFhoFinDetalle');
		sData += '&anexo.sCodEstado=' + $('#caxEstadoDetalle').val();
		sData += '&anexo.nIdActualizador=${sesion.sesIdUsuario}';
		return sData;
	}

	function validaFiltrosActualizaAnexo(){
		var cont = 0; 
		$("#detalleActualizaAnexo .obligatorio").each(function(){
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

<div id="detalleAnexo" class="detalle">
	<form id="formDetalleAnexo" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="caxtieneFlecha" class="contieneFlecha">
					<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">
				</div>
				<div id="contienetitulo" class="contienetitulo">
					<span>${solemfmt:getLabel("label.Detalle", codIdioma)}  "${anexo.sNombreConvenio}" - "${anexo.sNombreAnexo}"</span>
				</div> 
				<div class="divBtn" >
					<input class="button" type="button" id="actualizarAnexo" value="${solemfmt:getLabel('boton.Actualizar', codIdioma)}" role="button" aria-disabled="false">
				</div> 
			</div>  
			<div id="detalleActualizaAnexo" class="contenido_detalle sombra">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.IdAnexo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="caxIdDetalle" class="caja_texto" value="${anexo.nIdAnexo}" readonly>	 
						</div>
					</div>
					<div class="campo campo_detalle" style="width: 670px;">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)} : </span>
						</div>
						<div class="valor_campo float_left" style="width: 365px;">
							<input type="text" id="caxNombreDetalle" class="caja_texto obligatorio" value="${anexo.sNombreAnexo}">
							<img class="imgNoOk validadorCampo2" alt='' style="left: 333px;" src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
				</div>
		
				<div class="fila alturaFilaTextArea">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Descripcion", codIdioma)} : </span>
						</div>
						<div class="valor_campo float_left">
							<textarea id="caxDescDetalle" class="descripcion obligatorio"  title="${anexo.sDescripcion}">${anexo.sDescripcion}</textarea>	
							<img class="imgNoOk validadorTextArea2" alt='' src='${rutaImg}iconos/badCheck.png'>								
						</div>
					</div>
				</div>
				
				<div class="fila alturaFila"> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaInicio", codIdioma)} : </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="caxFhoIniDetalle" class="caja_texto obligatorio" value="${solemfmt:stringToFecha(anexo.sFhoInicio)}">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>							
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaFin", codIdioma)} : </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="caxFhoFinDetalle" class="caja_texto obligatorio" value="${solemfmt:stringToFecha(anexo.sFhoTermino)}">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>								
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)} : </span>
						</div>
						<div class="valor_campo float_left">
							<select id="caxEstadoDetalle" class="obligatorio">
								<c:forEach items="${requestScope.listaEstadosAnexo}" var="listaEstados">
									<option value="${listaEstados.parCodParametro01}" <c:if test='${anexo.sCodEstado == listaEstados.parCodParametro01}'> selected</c:if>>${listaEstados.parDesLargo01}</option>
								</c:forEach>
							</select>		
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>						
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
							<h2>${solemfmt:getLabel("label.Prestaciones", codIdioma)} </h2>
							<p><%@ include file="prestaciones/listaPrestacionesAnexo.jsp" %></p>
						</div> 
					</div>
				</div>
				<div class="espacio10"></div>
			</div>
		</div>
	</form>
</div>