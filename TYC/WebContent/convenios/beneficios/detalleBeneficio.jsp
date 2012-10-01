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

		$('input#guardarBeneficio').click(function(){
			if (validaFiltrosDetalleBeneficio())
			{  
				guardarBeneficio(recargar);
			} 
		}); 

		$('input#cancelarGuardar').click(function(){ 
			window.parent.$('#ifrmBeneficio').dialog('close');  
		});
	});
	
	function guardarBeneficio(callback)
	{
	    $.ajax({
	        async: false,
		    type: "POST",
		    url: getUrlGuardarBeneficio(),
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
		window.parent.recargarBusqueda();
		window.parent.$('#ifrmBeneficio').dialog('close');
	}
	
	
	function getUrlGuardarBeneficio()
	{
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarBeneficio';
		sData += '&beneficio.nIdConvenio=' + $('#beneficioIdConvenioGuarda').val();
		sData += '&beneficio.nIdPlanBeneficio=' + $('#conIdPlan').val();
		sData += '&beneficio.nIdUsuarioCreacion=${sesion.sesIdUsuario}';		
		//console.log(sData);
		return sData;
	}

	function validaFiltrosGuardaBeneficio()
	{
		var cont = 0; 
		$("#detalle01 .obligatorio").each(function(){
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
<div id="guardarBeneficio" class="detalle" style="height:188px;background:#C4C3A5">
	<form id="formDetalleBeneficio" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="width: 910px">
			<div id="titulo01" class="titulo" style="width: 907px">
				<div id="contieneFlecha" style="float: left;height: 25px;width: 14px;">
			 	</div>
				<div id="contienetitulo" class="contienetitulo">
					<span>${solemfmt:getLabel("label.DetalleNuevoBeneficio", codIdioma)}</span> 
				</div> 
			</div> 
			<div id="detalle01" class="contenido_detalle sombra" style="width: 905px">
				<div class="filaDialog alturaFila">
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.IdConvenio", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="beneficioIdConvenioDetalle" class="caja_texto" value="${idConvenio}" readonly>	 
						</div>
					</div>
					
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.CodigoPlan", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="hidden" id="conIdPlan">
							<input type="text" id="conCodPlan" class="caja_texto" value="" readonly>
						</div>
					</div>
				
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.NombrePlan", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conNomPlan" class="caja_texto" value="" readonly>
						</div>
					</div>
				</div>
				<div class="filaDialog alturaFila">
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Descripcion", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conDescPlan" class="caja_texto" value="" readonly>
						</div>
					</div>
					<div class="campo campo_medium">
						<div class="label_campo label_campo_dialog">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conEstadoPlan" class="caja_texto" value="" readonly>
						</div>
					</div>
				</div>
				
				<div class="espacio10"></div> 
			</div>
		</div>	
		<div style="width: 274px;height: 28px;margin-left: 360px;margin-top: 4px;">
			<div id="btnCancelarGuardar" style="height: 25px;padding-top: 3px;text-align: right;margin-right: 18px;float: left;">
				<input class="button" type="button" id="cancelarGuardar" value="Cancelar" role="button" aria-disabled="false">
			</div>
			<div id="btnGuardarBeneficio" style="height: 25px;padding-top: 3px;text-align: right;margin-right: 18px;float: left;">
				<input class="button" type="button" id="guardarBeneficio" value="Guardar" role="button" aria-disabled="false">
			</div>
		</div>		  
	</form>
</div>