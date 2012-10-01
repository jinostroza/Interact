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
		
		$.datepicker.setDefaults($.datepicker.regional['es']);
		$('input[type="checkbox"]').ezMark(); 

		$("#anexoFecIniAgrega").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#anexoFecFinAgrega").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$("#anexoFecFinAgrega").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#anexoFecIniAgrega").datepicker( "option", "maxDate", selectedDate );
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
	
		$('input#guardarAnexo').click(function(){
			if (validaFiltrosAgregaAnexo()){
				$('#formAgregaAnexo').attr('action', getUrlAgregarAnexo());
				$('#formAgregaAnexo').submit();
			}
		});  
	}); 

	function getUrlAgregarAnexo(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarAnexo';
		sData += '&anexo.nIdConvenio=' + $('#anexoIdConvenioAgrega').val();
		sData += '&anexo.sNombreAnexo=' + $('#anexoNombreAgrega').val();
		sData += '&anexo.sDescripcion=' + $('#anexoDescAgrega').val();
		sData += '&anexo.sFhoInicio=' + desformateaFechaSinHora('#anexoFecIniAgrega');
		sData += '&anexo.sFhoTermino=' + desformateaFechaSinHora('#anexoFecFinAgrega');
		sData += '&anexo.sCodEstado=' + $('#anexoEstadoAgrega').val();
		sData += '&anexo.nIdCreador=${sesion.sesIdUsuario}';		
		//console.log(sData);
		return sData;
	} 
	
	function validaFiltrosAgregaAnexo(){
		var cont = 0; 
		$("#detalleAgregaAnexo .obligatorio").each(function(){
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

<div id="guardarAnexo" class="detalle">
	<form id="formAgregaAnexo" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="contienetitulo" class="contieneTituloAgrega">
					<span>${solemfmt:getLabel("label.AgregaAnexo", codIdioma)}</span>
				</div> 
				<div class="divBtn">
					<input class="button" type="button" id="guardarAnexo" value="${solemfmt:getLabel('boton.Guardar', codIdioma)}" role="button" aria-disabled="false">
				</div>
			</div>  
			<div id="detalleAgregaAnexo" class="contenido_detalle sombra">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.IdConvenio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="anexoIdConvenioAgrega" class="caja_texto" value="${idConvenio}" readonly>
						</div>
					</div>		
					<div class="campo campo_detalle" style="width: 670px;">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left" style="width: 365px;">
							<input type="text" id="anexoNombreAgrega" class="caja_texto obligatorio" value="">
							<img class="imgNoOk validadorCampo2" alt='' style="left: 333px;" src='${rutaImg}iconos/badCheck.png'>	
						</div>
					</div>
				</div>
		
				<div class="fila alturaFilaTextArea">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Descripcion", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<textarea id="anexoDescAgrega" class="descripcion obligatorio" ></textarea>	
							<img class="imgNoOk validadorTextArea2" alt='' src='${rutaImg}iconos/badCheck.png'>														
						</div>
					</div>
				</div>
				
				<div class="fila alturaFila"> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaInicio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="anexoFecIniAgrega" class="caja_texto obligatorio" value="">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>	
						</div>
					</div>
					
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaFin", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="anexoFecFinAgrega" class="caja_texto obligatorio" value="">	
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>							
						</div>
					</div>
				
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="anexoEstadoAgrega" class="obligatorio">
								<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaEstadosAnexo}" var="listaEstados">
									<option value="${listaEstados.parCodParametro01}">${listaEstados.parDesLargo01}</option>
								</c:forEach>
							</select>
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>						
						</div>
					</div>
				</div>
			</div>
		</div> 
	</form>
</div>