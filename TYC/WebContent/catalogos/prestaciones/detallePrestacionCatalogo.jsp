<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<script type="text/javascript">

	$(function(){
		var seccionDetalle = $('#contenedorDetallePrestacionCatalogo'); 
		$('#catFechaInicio').datepicker();
		$('#catFechaTermino').datepicker();
		$('input[type="checkbox"]').ezMark(); 
	
		$('input#actualizarPrestacion').click(function(){
			if(validaFiltrosActualizaCatalogo() && seccionDetalle.is(":visible")){ 
				getXml('SvtCatalogos', getUrlDetallePrestacion(), function(xml){
	  	     		alert('${solemfmt:getLabel("alert.PrestacionActualizada", codIdioma)}');
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

		$('#catPrestHonorarios').blur(function(e){
			soloNumerosOnBlur('catPrestHonorarios');
		});

	    $('#catPrestHonorarios').keypress(function(e){
	    	return soloNumeros(e);
	    });

	    $('#catPrestGastos').blur(function(e){
			soloNumerosOnBlur('catPrestGastos');
		});

	    $('#catPrestGastos').keypress(function(e){
	    	return soloNumeros(e);
	    });
	}); 

	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius"); 
	}
	
	function getUrlDetallePrestacion(){
		var sData =  'KSI=${sesion.sesIdSesion}';
		sData += '&accion=actualizarPrestacionesCatalogo';
		sData += '&catalogoPrest.nIdCatalogoPrestacion='+ $('#catIdprest').val();
		sData += '&catalogoPrest.sNomPrestCatalogo=' + $('#catPrestNombre').val();
		sData += '&catalogoPrest.sIndTipoPago=' +  devuelveEstadoCheck($('#catPrestIndPagoAnticpado'));
		sData += '&catalogoPrest.nPorGastos=' + $('#catPrestGastos').val();
		sData += '&catalogoPrest.nPorHonorarios=' + $('#catPrestHonorarios').val();
		sData += '&catalogoPrest.nIdActualizador=${sesion.sesIdUsuario}';
		return sData;
	}
	
	function validaFiltrosActualizaCatalogo(){
		var cont = 0; 
		$("#detallePrestacionCatalogo .obligatorio").each(function(){
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
    
	function validaFiltrosActualizaCatalogos()
	{
		if($.trim($("#catNombreEmpresa").val()) == "")
		{
			alert('Debe Ingresar la Instituci&oacute;n');
	        return false;
		}

		if($.trim($("#catCodigo").val()) == "")
		{
			alert('Debe Ingresar el Codigo catalogo');
	        return false;
		}

		if($.trim($("#catNombre").val()) == "")
		{
			alert('Debe Ingresar el Nombre del catalogo');
	        return false;
		}

		if($.trim($("#catDetEstado").val()) == "")
		{
			alert('Debe Ingresar el Nombre del Estado catalogo');
	        return false;
		}

		if($.trim($("#catDescripcion").val()) == "")
		{
			alert('Debe Ingresar el Nombre de la Descripcion del catalogo');
	        return false;
		}

		if($.trim($("#catFechaInicio").val()) == "")
		{
			alert('Debe Ingresar la fecha inicio del catalogo');
	        return false;
		}

		if($.trim($("#catFechaTermino").val()) == "")
		{
			alert('Debe Ingresar la fecha de termino del catalogo');
	        return false;
		}

		if($.trim($("#catIndPublico").val()) == "")
		{
			alert('Debe Ingresar el Nombre del catalogo');
	        return false;
		}

		if($.trim($("#catIndPlantilla").val()) == "")
		{
			alert('Debe Ingresar el Nombre del catalogo');
	        return false;
		}

		if($.trim($("#catIndVigencia").val()) == "")
		{
			alert('Debe Ingresar el Nombre del catalogo');
	        return false;
		} 
			
		return true;
	} 

	function validaPorcentajeHonorarios()
	{
		var porcentaje =  $('#catPrestHonorarios').val();
		if(porcentaje<0 || porcentaje>100)
		{
			alert('${solemfmt:getLabel("alert.PorcentajeEntre0y100", codIdioma)}');
			$('#catPrestHonorarios').val('');	
		}
	}

	function validaPorcentajeGastos()
	{
		var porcentaje =  $('#catPrestGastos').val();
		if(porcentaje<0 || porcentaje>100)
		{
			alert('${solemfmt:getLabel("alert.PorcentajeEntre0y100", codIdioma)}');
			$('#catPrestGastos').val('');	
		}
	}
	
</script> 


<div id="contenedorDetallePrestacionCatalogo" class="detalle">
	<form id="formDetallePrestacionCatalogo" method="post">
		<div id="DetallePrestacionCatalogo" class="cont_sec_det">
			<div id="titulo01" class="titulo"> 
				<div id="contieneFlecha" class="contieneFlecha"> 
					<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">  
				</div>
				<div id="contienetitulo" class="contienetitulo">
					<span>${solemfmt:getLabel("label.Detalle", codIdioma)} "${prestacioncatalogo.sNombreCatalogo}" - "${prestacioncatalogo.sNomPrestacion}" </span>
				</div> 
				<div class="divBtn" >
					<input class="button" type="button" id="actualizarPrestacion" value="${solemfmt:getLabel('boton.Actualizar', codIdioma)}" role="button" aria-disabled="false">
				</div>  
			</div>  
			
			<div id="detallePrestacionCatalogo" class="contenido_detalle sombra">
				<input type="hidden" id="catIdprest" value="${prestacioncatalogo.nIdCatalogoPrestacion}"> 
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span> 
						</div>
						<div class="valor_campo float_left" style="width: 200px">
							<input type="text" id="catPrestCodigo" class="caja_texto obligatorio" value="${prestacioncatalogo.sCodPrestacion}" readonly>
							<img class="imgNoOk validadorCampo2" alt='' style="left: 270px;" src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div> 
					<div class="campo campo_detalle" style="width: 423px">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Prestacion", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left" style="width: 295px">
							<input type="text" id="catPrestNombre" class="caja_texto obligatorio" value="${prestacioncatalogo.sNomPrestacion}" readonly>
							<img class="imgNoOk validadorCampo2" alt='' style="left: 270px;" src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					<div class="campo campo_detalle" style="width: 175px">
						<div class="label_campo labelCampoHorizontal anchoLarge">
							<span>${solemfmt:getLabel("label.PagoAnticipado", codIdioma)}: </span>
						</div>
						<div class="contineCheckbox">
							<input type="checkbox" id="catPrestIndPagoAnticpado" <c:if test="${prestacioncatalogo.sIndTipoPago == 'S'}">checked</c:if>>
						</div> 	
					</div> 
				</div>
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo" style="top: -6px">
							<span>${solemfmt:getLabel("label.Prestacion", codIdioma)}</span><br>
							<span>${solemfmt:getLabel("label.Catalogo", codIdioma)}:</span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="catPrestNombre" class="caja_texto obligatorio" value="${prestacioncatalogo.sNomPrestCatalogo}">
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					<div class="campo campo_detalle" style="width: 245px;">
						<div class="label_campo detalle_label_campo">
							<span>% ${solemfmt:getLabel("label.Honorarios", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left" style="width: 64px;">
							<input type="text" id="catPrestHonorarios" class="caja_texto obligatorio" onChange="validaPorcentajeHonorarios();" style="width: 60px;" value="${prestacioncatalogo.nPorHonorarios}">
							<img class="imgNoOk validadorCampo2" style="left: 65px;" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>
					<div class="campo campo_detalle" style="width: 178px;">
						<div class="label_campo detalle_label_campo" style="width: 78px">
							<span>% ${solemfmt:getLabel("label.Gastos", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left" style="width: 65px;">
							<input type="text" id="catPrestGastos" class="caja_texto obligatorio" onChange="validaPorcentajeGastos();" style="width: 60px;" value="${prestacioncatalogo.nPorGastos}">
							<img class="imgNoOk validadorCampo2" style="left: 65px;" alt='' src='${rutaImg}iconos/badCheck.png'>
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
							<h2>${solemfmt:getLabel("label.Precios", codIdioma)}</h2>
							<p><%@ include file="precios/listaPrecios.jsp" %></p>
						</div>
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.Modificadores", codIdioma)}</h2>
							<p><%@ include file="modificadores/listaModificadores.jsp" %></p>
						</div> 
					</div>
				</div>
				<div class="espacio10"></div>
			</div>
		</div>				
	</form>
</div>