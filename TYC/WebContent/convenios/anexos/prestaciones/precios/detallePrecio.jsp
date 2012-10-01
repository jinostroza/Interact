<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript"> 

	var indPrecioFormula = "${precio.sIndPrecioFormula}"; 
	
	$(function() {
		 
		$('input[type="radio"]').ezMark();
		$('.opcionalBtns').hide();
		 
		if(indPrecioFormula == 'P'){ 
			$('#precioPrecioDetalle').attr('readonly', false);
			$('#precio').parent().addClass('ez-selected');
			$('#validar').val('${solemfmt:getLabel("boton.Validar",codIdioma)}');
			$('#precioFormulaDetalle').val(''); 
		}else if(indPrecioFormula == 'F'){ 
			$('#formula').parent().addClass('ez-selected');
			$('#validar').val("${solemfmt:getLabel('boton.Modificar',codIdioma)}");
			$('#precioPrecioDetalle').val(''); 
			$('#precioFormulaDetalle').attr("readonly",true).css('background','#9F9');
			$('.opcionalBtns').show();
		}else
			alert("${solemfmt:getLabel('alert.NoRecataPrecioNiFormula', codIdioma)}");	 
		
		$("input[type='radio']").click(function(){
			getRadio();
		}); 

		$('#validar').click(function(){   
			var desabilitado = $('#precioFormulaDetalle').attr("readonly"); 
			var valFormula = $.trim($('#precioFormulaDetalle').val());   
			var formula = "";
			
			if(desabilitado!="readonly" && valFormula!=""){
				getAjaxCall(getUrlValidarFormulaDetalle(), function(xml){  
					$('#validar').val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
					$('#precioFormulaDetalle').attr("readonly",true); 
					$('#precioFormulaDetalle').parent().children("img.imgNoOk").css("display","none");
					$('#precioFormulaDetalle').parent().children("img.imgOk").css("display","block"); 
					$('textarea#precioFormulaDetalle').css("background","#99FF99");  //829664 99FF99
					formula = "valido";
		       	});  
			}else if(valFormula==""){   
				//$('#precioFormulaDetalle').css("background","white");
				$('#precioFormulaDetalle').parent().children("img.imgNoOk").css("display","none");
				$('#precioFormulaDetalle').parent().children("img.imgOk").css("display","none"); 
			}else{ 
				$(this).val('${solemfmt:getLabel("boton.Validar",codIdioma)}');
				$('#precioFormulaDetalle').attr("readonly",false); 
				$('#precioFormulaDetalle').parent().children("img.imgNoOk").css("display","none");
				$('#precioFormulaDetalle').parent().children("img.imgOk").css("display","none");  
				$('#precioFormulaDetalle').css("background","white");
			}
			if($('#popup_container').is(":visible") && formula!="valido"){  
				$('#precioFormulaDetalle').css("background","#C86464");
				$('#precioFormulaDetalle').parent().children("img.imgNoOk").css("display","block");
				$('#precioFormulaDetalle').parent().children("img.imgOk").css("display","none");  
			}
		});  
		
		$('input#actualizarPrecio').click(function(){  
			var precio    = $("#precioPrecioDetalle").attr("readonly"); 
			var formula   = $("#precioFormulaDetalle").attr("readonly");
		    var formulaOk = "${solemfmt:getLabel('boton.Modificar',codIdioma)}";
		    var valueBtnFormula = $("#validar").val(); 
		    
			if(validaFiltrosDetallePrecio()){  
				$("#precioPrecioDetalle").removeClass("obligatorio").addClass("obligatorio");
				if((valueBtnFormula==formulaOk) && formula=="readonly"){ 
					getXml('SvtConvenios', getUrlDetallePrecio(), function(xml){
						alert('Detalle Precio Actualizado!!');
			    	});
				}else if(precio!="readonly"){
					getXml('SvtConvenios', getUrlDetallePrecio(), function(xml){
						alert('Detalle Precio Actualizado!!');
			    	});
				}else 
					alert("${solemfmt:getLabel('alert.SeleccioneCampoFormula', codIdioma)}");   
			} 
		});   
 
		$("textarea").click(function(){ 
			var readonlyTxt = $(this).attr('readonly'); 

			if(readonlyTxt!='readonly'){
				$(this).css("background","white"); 
				$(this).parent().children("img.imgNoOk").css("display","none");
				$(this).parent().children("img.imgOk").css("display","none"); 	
			}
				 
			var idTextArea = $(this).attr("id"); 
			var indTextArea = $(this).attr("indicador");  
			var indicator = document.getElementById(indTextArea); 
			var textarea = document.getElementById(idTextArea);   

			setInterval(function(){
				indicator.value = caret(textarea); 
			}, 100);  
			
			if($(this).parent().children("img").is(":visible"))
				$(this).parent().children("img.imgNoOk").css("display","none");
		}).blur(function(){  
			var indicador = $(this).attr("indicador");
			var numChar = $("#"+indicador).val();     
			$("#punteroFormula").attr("numChar",numChar);  
		});  

		$('.btnUsar').click(function(){ 
			var readOnly = $("#precioFormulaDetalle").attr("readonly");  
			var valorFormula = $("#precioFormulaDetalle").val(); 
			var valorBtn = $("#validar").val(); 
			
			if (readOnly!='readonly'){  
				var accion = "precioFormulaDetalle"; 
				var indicadorValue = $("#punteroFormula").attr("numChar");  
				var strDividir = $("#"+accion).val();
				var strA = strDividir.substring(0,indicadorValue);
				var strB = strDividir.substring(indicadorValue);
				var numCaract = strDividir.length; 
	
				if (strDividir!='' && indicadorValue==0 && strA!='' && strB!=''){  
					indicadorValue=numCaract; 
					strA = strDividir.substring(0,indicadorValue);
					strB = strDividir.substring(indicadorValue);
				}else if($("#variablesprestacion").is(":visible")){
					var selectFila  =  $("#listadoVarPrestacion").jqGrid('getGridParam','selrow');  
					var dataObjGrid =  $("#listadoVarPrestacion").getRowData(selectFila);
					var valor       =  (dataObjGrid.varPres);  
					var strAgregar  =  strA+valor+strB;
					
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else 
						alert("${solemfmt:getLabel('alert.SeleccioneVarPrest', codIdioma)}");   
	 
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#punteroFormula").attr("numChar",numChar);   
					
				}else if($("#variablesDeReferencia").is(":visible")){
					var selectFila  =  $("#listadoVarRefe").jqGrid('getGridParam','selrow');  
					var dataObjGrid =  $("#listadoVarRefe").getRowData(selectFila);
					var valor       =  (dataObjGrid.varRef);  
					var strAgregar  =  strA+valor+strB;
					
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else
						alert("${solemfmt:getLabel('alert.SeleccioneVarReferencial', codIdioma)}");   
	 
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#punteroFormula").attr("numChar",numChar);   
						
				}else if($("#variablesGlobales").is(":visible")){
					var selectFila  =  $("#listadoVarGlobales").jqGrid('getGridParam','selrow');  
					var dataObjGrid =  $("#listadoVarGlobales").getRowData(selectFila);
					var valor       =  (dataObjGrid.varGlob);  
					var strAgregar  =  strA+valor+strB;
					
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else
						alert("Debe seleccionar una Variable de Global!!");
	 
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#punteroFormula").attr("numChar",numChar);   
				}
			}else if(readOnly=='readonly' && valorFormula!=""){
				if(valorBtn=="Validar")
					alert("${solemfmt:getLabel('alert.SeleccioneModificar', codIdioma)}");
				else
					alert("${solemfmt:getLabel('alert.FormulaValidada', codIdioma)}"); 
			}else
				alert("${solemfmt:getLabel('alert.SeleccioneCampoFormula', codIdioma)}");  
		});   
  
		$("#precioPrecioDetalle").click(function(){  
			var readOnlyPrecio = $(this).attr("readonly"); 
			if(readOnlyPrecio!="readonly")
				$(this).css("background","white"); 
			$(this).parent().children("img.imgNoOk").css("display","none");
			$(this).parent().children("img.imgOk").css("display","none");   
			var idPrecio = $(this).attr("id"); 
			var indPrecio = $(this).attr("indicador");  
			var indicator = document.getElementById(indPrecio); 
			var precio = document.getElementById(idPrecio);   
			setInterval(function(){
				indicator.value = caret(precio); 
			}, 100);  
			if($(this).parent().children("img").is(":visible"))
				$(this).parent().children("img.imgNoOk").css("display","none");
		}).blur(function(e){ 
			var idPrecio = $(this).attr("id"); 
			var indicador = $(this).attr("indicador");
			var numChar = $("#"+indicador).val();     
			$("#punteroPrecio").attr("numChar",numChar);    
			soloNumerosCommaOnBlur(idPrecio); 
		}).keypress(function(evt){  
			var retorno = true;	
			var charCode = (evt.which) ? evt.which : event.keyCode; 
 			var valorNumerico = $(this).val();  
			var numCar = valorNumerico.length;  
			var veces = 0;  
			var letra = ",";
			var posicion = valorNumerico.indexOf(letra);  
			var posfinal = valorNumerico.lastIndexOf(letra);
			posfinal = numCar-posfinal;  
		
			while(numCar>=posfinal) {
			   posicion = valorNumerico.indexOf(letra);  
			   posicion++;
			   valorNumerico = valorNumerico.substring(posicion); 
			   numCar -= posicion;  
			   veces++;  
			}  
			if((charCode > 31 && charCode!=44) && (charCode < 48 || charCode > 57)) 
				retorno = false;   
			else if(charCode==44 && veces>0)
				retorno = false;  
			
			return retorno;	
		}); 
		 
		$("#precioFecIniDetalle").datepicker({  
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function(selectedDate){
				$("#precioFecFinDetalle").datepicker("option", "minDate", selectedDate);
			}
		});
		
		$("#precioFecFinDetalle").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#precioFecIniDetalle").datepicker( "option", "maxDate", selectedDate );
			}
		});   
		
	}); 
	 
	function getUrlValidarFormulaDetalle(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=validarFormulaModifPrestConv';
		sData += '&sFormula=' + $('#precioFormulaDetalle').val();
		return sData;
	} 

	function getRadio(){
		var indicador;
		var idRadio =	$('#formDetallePrecio .ez-selected').children().attr('id');
	
		if(idRadio == "precio"){
			indicador = "P";
			$('#precioPrecioDetalle').attr('readonly', false).addClass("obligatorio");
			$('#precioFormulaDetalle').attr('readonly', true).removeClass("obligatorio");
			$('#precioFormulaDetalle').val('');
			$("img").css("display","none"); 
			$('.opcionalBtns').hide();
			$('#precioPrecioDetalle').val('');
			$('#precioPrecioDetalle').css("background","white"); 
			$('#precioFormulaDetalle').css("background","#D7D7B4"); 
		}else{
			indicador = "F";
			$('#precioFormulaDetalle').attr('readonly', false).addClass("obligatorio");
			$('#precioPrecioDetalle').attr('readonly', true).removeClass("obligatorio"); 
			$("img").css("display","none");
			$('.opcionalBtns').show();
			$('#precioPrecioDetalle').val(''); 
			$('#precioPrecioDetalle').css("background","#D7D7B4"); 
			$('#precioFormulaDetalle').css("background","white"); 
		}
		return indicador;	
	}

	function getRadioButom(){
		var indicador;
		var idRadio =	$('#formDetallePrecio .ez-selected').children().attr('id');
	
		if(idRadio == "precio") 
			indicador = "P"; 
		else if(idRadio == "formula")
			indicador = "F"; 
		 
		return indicador;	
	}

	function getUrlDetallePrecio(){  
		var sData = 'KSI=${sesion.sesIdSesion}'; 
		sData += '&accion=actualizarPrecio';
		sData += '&precio.nIdConvenioPrecio=' + ${precio.nIdConvenioPrecio};
		sData += '&precio.sCodTipoMoneda=' + $("#precioTipoMonDetalle").val();
		sData += '&precio.sFhoInicio=' + desformateaFechaSinHora('#precioFecIniDetalle');
		sData += '&precio.sFhoFin=' + desformateaFechaSinHora('#precioFecFinDetalle');
		sData += '&precio.sIndPrecioFormula=' + getRadioButom();

		if (getRadioButom()=='P'){
			sData += '&precio.nPrecio=' + $('#precioPrecioDetalle').val(); 
		}else if(getRadioButom()=='F'){ 
			sData += '&precio.sFormula=' + $('#precioFormulaDetalle').val();
		}

		sData += '&precio.nIdActualizador=${sesion.sesIdUsuario}';	 
		//sData += '&indRuta=S';
		
		return sData;
	} 

	function validaFiltrosDetallePrecio(){ 
		var precioReadOnly  = $("#precioPrecioDetalle").attr("readonly")
		var formulaReadOnly = $("#precioFormulaDetalle").attr("readonly")
		if (precioReadOnly=="readonly" && formulaReadOnly=="readonly")
			$("#precioPrecioDetalle").removeClass("obligatorio");
		
		var cont = 0; 
		$("#detalleDetallePrecio .obligatorio").each(function(){
			var value = $.trim($(this).val());   
			if(value==""){
				$(this).parent().children("img.imgNoOk").css("display","block");
				$(this).parent().parent().children("div.contieneButtonLupa").children("img.imgNoOk").css("display","block"); 
				cont++;
			}else{
				$(this).parent().children("img.imgNoOk").css("display","none");
				$(this).parent().parent().children("div.contieneButtonLupa").children("img.imgNoOk").css("display","none"); 
			}  
		});  
		if(cont==0)
			return true;
		else
			return false;		
	}	
	 	
</script>  

	<input type="hidden" id="indicadorFormula" value="0">
	<input type="hidden" id="indicadorPrecio" value="0"> 
	<input type="hidden" id="punteroFormula" numChar="0" value="0">
	<input type="hidden" id="punteroPrecio" numChar="0" value="0">
  
	<div class="detalle">   
		<div class="cont_sec_det"> 
			<div id="" class="titulo">  
				<div id="" class="contieneTituloAgrega"> 
					<span>${solemfmt:getLabel("label.Prestacion",codIdioma)} "Nombre Prestaci&oacute;n" - ${solemfmt:getLabel("boton.NuevoPrecio",codIdioma)}</span>
				</div>		
				<div class="floatLeftBtn">
					<div class="divBtn floatLeftBtn">
						<input class="button" type="button" id="actualizarPrecio" value="${solemfmt:getLabel('boton.Actualizar',codIdioma)}" role="button" aria-disabled="false"> 
					</div>
				</div>	
			</div> 		
			<div id="detalleDetallePrecio" class="contenido_detalle sombra">
				<form id="formDetallePrecio" method="post">
					<div class="fila alturaFila">
						<div class="campo campo_detalle">
							<div class="label_campo detalle_label_campo">
								<span>${solemfmt:getLabel('boton.Prestacion',codIdioma)}: </span>
							</div>
							<div class="valor_campo float_left">
								<input type="text" id="" class="caja_texto" title="${precio.sNombrePrestacion}" value="${precio.sNombrePrestacion}" readonly>
								<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">
							</div>
						</div>
						<div class="campo campo_detalle">
							<div class="label_campo detalle_label_campo">
								<span>${solemfmt:getLabel("label.TipoMoneda",codIdioma)}: </span>
							</div>
							<div class="valor_campo float_left">
								<select id="precioTipoMonDetalle" class="obligatorio">
									<c:forEach items="${requestScope.listaTiposMoneda}" var="listaTiposMoneda">
										<option value="${listaTiposMoneda.parCodParametro01}" <c:if test='${precio.sCodTipoMoneda == listaTiposMoneda.parCodParametro01}'> selected</c:if>>${listaTiposMoneda.parDesLargo01}</option>
									</c:forEach>
								</select>
								<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">
							</div>
						</div>	
					</div>  
					<div class="fila alturaFila"> 
						<div class="campo campo_detalle">
							<div class="label_campo detalle_label_campo">
								<span>${solemfmt:getLabel("label.FechaInicio",codIdioma)}: </span>
							</div>
							<div class="valor_campo float_left">
								<input type="text" id="precioFecIniDetalle" class="caja_texto obligatorio" style="background:white" value="${solemfmt:stringToFecha(precio.sFhoInicio)}" readonly>	
								<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">
							</div>																
						</div> 
						<div class="campo campo_detalle">
							<div class="label_campo detalle_label_campo">
								<span>${solemfmt:getLabel("label.FechaFin",codIdioma)}: </span>
							</div>
							<div class="valor_campo float_left">
								<input type="text" id="precioFecFinDetalle" class="caja_texto obligatorio" style="background:white" value="${solemfmt:stringToFecha(precio.sFhoFin)}" readonly>							
								<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">
							</div>																	
						</div>
					</div>	
					<div id="contieneRadioButtom" class="fila alturaFila"> 
						<div class="campo" style="width: 127px;">
							<div class="contineRadioButton" style="float: left;margin-left: 11px;" >  
								<input type="radio" id="precio" class="radio" name="precioFormula"/> 
							</div> 
							<div class="label_campo labelCampoHorizontal anchoLarge" style="width: 69px;">
								<span>${solemfmt:getLabel("label.Precio",codIdioma)}</span>
							</div> 
						</div>	
						<div class="campo" style="width: 127px;"> 
							<div class="contineRadioButton" style="float: left;margin-left: 11px;" >  
								<input type="radio" id="formula" class="radio" name="precioFormula"/> 
							</div> 	
							<div class="label_campo labelCampoHorizontal anchoLarge" style="width: 69px;">
								<span>${solemfmt:getLabel("label.Formula",codIdioma)}</span>
							</div>
						</div>
					</div>
					<div class="fila alturaFila">
						<div class="campo campo_detalle">
							<div class="label_campo detalle_label_campo">
								<span>${solemfmt:getLabel("label.Precio",codIdioma)}: </span>
							</div>
							<div class="valor_campo float_left">
								<input type="text" id="precioPrecioDetalle" name="display2" class="caja_texto obligatorio" indicador="indicadorPrecio" value="${precio.nPrecio}" readonly="readonly">  
								<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">
							</div>
						</div>
					</div>  
					<div class="seccionTxtAreaBtns" style="height: 94px;">
						<div id="contieneTextAreas"> 
							<div class="fila alturaFilaTxA">
								<div class="campo campo_detalleTxA">
									<div class="label_campo label_campoTxa">
										<br><span>${solemfmt:getLabel("label.Formula",codIdioma)}: </span> 
									</div>
									<div class="divBtn"> 
										<input type="button" id="validar" class="button" value="" name="enter" role="button" aria-disabled="false">
									</div>							
									<div class="valor_campo">
										<textarea id="precioFormulaDetalle" class="descripcion txtAreaConFor" indicador="indicadorFormula" readonly="readonly">${precio.sFormula}</textarea>		
										<img class="imgNoOk areaForCon" alt="" src="general/imagenes/iconos/badCheck.png">		
										<img class="imgOk areaForCon" alt="" src="general/imagenes/iconos/okCheck.png">					
									</div>
								</div>   
							</div>  
						</div> 
						<div id="divBotones" style="margin-top: -52px;">
	  						<%@ include file="botoneraFull.jsp" %> 				
						</div>
					</div>
				</form>
			</div> 
		</div> 

		<div id="seccionTabs" class="cont_sec_det_tab">  
			<div id="" class="titulo"> 
			</div> 
			<div id="detalle02" class="contenido_detalle sombra"> 
				<div class="contenidoTab">
					<div class="tabber"> 
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.VariablesPrestacion",codIdioma)}</h2>
							<p></p> 
							<div id="variablesprestacion" class="seccionTab">
								<div id="gridOpcVarCond">
									<%@ include file="varprestacion/listaVarPrestacion.jsp" %> 
								</div>		
								<div class="contieneBotoneraVert"></div>	 
								<div class="divDinamico"></div> 	
								<div id="contenedorBotoneraUsarAyuda"> 
					 				<div class="divBtn btnPosicion margenbtnUsar">
										<input class="btnUsar button" type="button" id="btnUsar" numChar=""  indicador="indicadorCondicion" accion="condicion" value="Usar" role="button" aria-disabled="false">
									</div>
						 			<div class="divBtn btnPosicion margenBtnAyuda">
										<input class="button" type="button" id="ayuda" value="Ayuda" role="button" aria-disabled="false">
									</div>	 					
								</div>																															
							</div>
						</div>
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.VariablesReferencia",codIdioma)}</h2>
							<p></p> 
							<div id="variablesDeReferencia" class="seccionTab">
								<div id="gridOpcVarCond">
									<%@ include file="vardereferencia/listaVarRefe.jsp" %>  
								</div>		
								<div class="contieneBotoneraVert"></div>	 
								<div class="divDinamico"></div> 	
								<div id="contenedorBotoneraUsarAyuda"> 
					 				<div class="divBtn btnPosicion margenbtnUsar">
										<input class="btnUsar button" type="button" id="btnUsar" numChar=""  indicador="indicadorCondicion" accion="condicion" value="Usar" role="button" aria-disabled="false">
									</div>
						 			<div class="divBtn btnPosicion margenBtnAyuda">
										<input class="button" type="button" id="ayuda" value="Ayuda" role="button" aria-disabled="false">
									</div>	 					
								</div>																															
							</div>
						</div>					
						<div class="tabbertab">
							<h2>${solemfmt:getLabel("label.VariablesGlobales",codIdioma)}</h2>
							<p></p> 
							<div id="variablesGlobales" class="seccionTab">
								<div id="gridOpcVarCond">
									<%@ include file="varblobales/listaVarGlobales.jsp" %>  
								</div>		
								<div class="contieneBotoneraVert"></div>	 
								<div class="divDinamico"></div> 	
								<div id="contenedorBotoneraUsarAyuda"> 
					 				<div class="divBtn btnPosicion margenbtnUsar">
										<input class="btnUsar button" type="button" id="btnUsar" numChar=""  indicador="indicadorCondicion" accion="condicion" value="Usar" role="button" aria-disabled="false">
									</div>
						 			<div class="divBtn btnPosicion margenBtnAyuda">
										<input class="button" type="button" id="ayuda" value="Ayuda" role="button" aria-disabled="false">
									</div>	 					
								</div>																															
							</div>
						</div>		 
					</div>
				</div>
				<div class="espacio10"></div>
			</div>
		</div>	 
	</div>		