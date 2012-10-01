<%@ page language="java"%> 
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript"> 

	$(function() {
		$('input[type="radio"]').ezMark();
		$('.opcionalBtns').hide(); 
		
		$('#precio').parent().addClass('ez-selected');
		$('#precioPrecioAgrega').attr('readonly', false);  
		
 
		$("input[type='radio']").click(function(){
			getRadio();
		}); 
		
		$('#validar').click(function(){ 
			var desabilitado = $('#precioFormulaAgrega').attr("readonly"); 
			var valFormula = $.trim($('#precioFormulaAgrega').val());   
			var formula = "";
			
			if(desabilitado!="readonly" && valFormula!=""){
				getAjaxCall(getUrlValidarFormula(), function(xml){ 
					 
					$('#validar').val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
					$('#precioFormulaAgrega').attr("readonly",true); 
					$('#precioFormulaAgrega').parent().children("img.imgNoOk").css("display","none");
					$('#precioFormulaAgrega').parent().children("img.imgOk").css("display","block"); 
					$('textarea#precioFormulaAgrega').css("background","#99FF99");  //829664 99FF99
					formula = "valido";
		       	});  
			}else if(valFormula==""){  
				//$('#precioFormulaAgrega').css("background","white");
				$('#precioFormulaAgrega').parent().children("img.imgNoOk").css("display","none");
				$('#precioFormulaAgrega').parent().children("img.imgOk").css("display","none");  
			}else{ 
				$(this).val('${solemfmt:getLabel("boton.Validar",codIdioma)}');
				$('#precioFormulaAgrega').attr("readonly",false); 
				$('#precioFormulaAgrega').parent().children("img.imgNoOk").css("display","none");
				$('#precioFormulaAgrega').parent().children("img.imgOk").css("display","none");  
				$('#precioFormulaAgrega').css("background","white");
			}
			
			if($('#popup_container').is(":visible") && formula!="valido"){  
				$('#precioFormulaAgrega').css("background","#C86464");
				$('#precioFormulaAgrega').parent().children("img.imgNoOk").css("display","block");
				$('#precioFormulaAgrega').parent().children("img.imgOk").css("display","none");  
			}
		});  

		$('input#guardarPrecio').click(function(){  
			var precio    = $("#precioPrecioAgrega").attr("readonly"); 
			var formula   = $("#precioFormulaAgrega").attr("readonly");
		    var formulaOk = "${solemfmt:getLabel('boton.Modificar',codIdioma)}";
		    var valueBtnFormula = $("#validar").val(); 
		    
			if(validaFiltrosAgregaPrecio()){  
				$("#precioPrecioDetalle").removeClass("obligatorio").addClass("obligatorio");
				if((valueBtnFormula==formulaOk) && formula=="readonly"){
					$('#formAgregaPrecio').attr('action', getUrlAgregarPrecio());
					$('#formAgregaPrecio').submit();
				}else if(precio!="readonly"){
					if(soloNumerosCommaOnBlur("precioPrecioAgrega")){
						$('#formAgregaPrecio').attr('action', getUrlAgregarPrecio());
						$('#formAgregaPrecio').submit();
					} 
				}else 
					alert("${solemfmt:getLabel('alert.ValideFormula', codIdioma)}"); 
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
			var readOnly = $("#precioFormulaAgrega").attr("readonly");  
			
			if (readOnly!='readonly'){  
				var accion = "precioFormulaAgrega"; 
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
						alert("${solemfmt:getLabel('alert.SeleccioneVarGlobal', codIdioma)}");
	 
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#punteroFormula").attr("numChar",numChar);   
				}
			}else 
				alert("${solemfmt:getLabel('alert.SeleccioneCampoFormula', codIdioma)}");   
		});   

		$("#precioPrecioAgrega").click(function(){
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
			var indicador = $(this).attr("indicador");
			var numChar = $("#"+indicador).val();     
			$("#punteroPrecio").attr("numChar",numChar);  
			soloNumerosCommaOnBlur('precioPrecioAgrega');
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
			//if((posicion>=0 && veces>0) && (charCode > 31 && (charCode < 48 || charCode > 57))) 
			if((charCode > 31 && charCode!=44) && (charCode < 48 || charCode > 57) ) 
				retorno = false;   
			else if(charCode==44 && veces>0)
				retorno = false;  
			
			return retorno;	
		}); 
		 
		$("#precioFecIniAgrega").datepicker({  
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#precioFecFinAgrega").datepicker( "option", "minDate", selectedDate );
			}
		});
		
		$("#precioFecFinAgrega").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#precioFecIniAgrega").datepicker( "option", "maxDate", selectedDate );
			}
		});   
		
	});

	function getUrlValidarFormula(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=validarFormulaModifPrestConv';
		sData += '&sFormula=' + $('#precioFormulaAgrega').val();
		return sData;
	} 

	function getRadio(){
		var indicador;
		var idRadio =	$('#formAgregaPrecio .ez-selected').children().attr('id');
	
		if(idRadio == "precio"){
			indicador = "P";
			$('#precioPrecioAgrega').attr('readonly', false).addClass("obligatorio");
			$('#precioFormulaAgrega').attr('readonly', true).removeClass("obligatorio");
			$('#precioFormulaAgrega').val('');
			$("img").css("display","none"); 
			$('.opcionalBtns').hide();  
			$('#precioPrecioAgrega').val('');
			$('#precioPrecioAgrega').css("background","white"); 
			$('#precioFormulaAgrega').css("background","#D7D7B4"); 
		}else{
			indicador = "F";
			$('#precioFormulaAgrega').attr('readonly', false).addClass("obligatorio");
			$('#precioPrecioAgrega').attr('readonly', true).removeClass("obligatorio");
			$('#precioPrecioAgrega').val('');
			$("img").css("display","none");
			$('.opcionalBtns').show(); 
			$('#precioPrecioAgrega').val(''); 
			$('#precioPrecioAgrega').css("background","#D7D7B4"); 
			$('#precioFormulaAgrega').css("background","white"); 
		}
		return indicador;	
	}

	function getRadioButom(){ 
		var indicador;
		var idRadio =	$('#formAgregaPrecio .ez-selected').children().attr('id');
	
		if(idRadio == "precio") 
			indicador = "P"; 
		else 
			indicador = "F"; 
		 
		return indicador;	
	}

	function getUrlAgregarPrecio(){  
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarPrecio';
		sData += '&precio.nIdConvDetalle=' + ${idPrestacionAnexo};
		sData += '&precio.sCodTipoMoneda=' + $("#precioTipoMonAgrega").val();
		sData += '&precio.sFhoInicio=' + desformateaFechaSinHora('#precioFecIniAgrega');
		sData += '&precio.sFhoFin=' + desformateaFechaSinHora('#precioFecFinAgrega');
		sData += '&precio.sIndPrecioFormula=' + getRadioButom();
		
		if (getRadioButom()=='P') 
			sData += '&precio.nPrecio=' + $("#precioPrecioAgrega").val(); 
		else 
			sData += '&precio.sFormula=' + $("#precioFormulaAgrega").val();
		 
		sData += '&precio.nIdUsuarioCreacion=${sesion.sesIdUsuario}';	 
		//sData += '&indRuta=S';
		
		return sData;
	}  

	function validaFiltrosAgregaPrecio(){ 
		var precioReadOnly  = $("#precioPrecioAgrega").attr("readonly");
		var formulaReadOnly = $("#precioFormulaAgrega").attr("readonly");
		if (precioReadOnly=="readonly" && formulaReadOnly=="readonly");
			$("#precioPrecioAgrega").removeClass("obligatorio");
		
		var cont = 0; 
		$("#detalleAgregaPrecio .obligatorio").each(function(){
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
				<div id="" class="contieneTituloAgrega" style="width: 866px;"> 
					<span>${solemfmt:getLabel("label.Prestacion",codIdioma)} "Nombre Prestaci&oacute;n" - ${solemfmt:getLabel("boton.NuevoPrecio",codIdioma)}</span>
				</div>		
				<div class="floatLeftBtn">
					<div class="divBtn floatLeftBtn">
						<input class="button" type="button" id="guardarPrecio" value="${solemfmt:getLabel('boton.Guardar',codIdioma)}" role="button" aria-disabled="false">
					</div>
				</div>	
			</div> 
			<div id="detalleAgregaPrecio" class="contenido_detalle sombra">
				<form id="formAgregaPrecio" method="post">
					<div class="fila alturaFila">
						<div class="campo campo_detalle">
							<div class="label_campo detalle_label_campo">
								<span>${solemfmt:getLabel('boton.Prestacion',codIdioma)}: </span>
							</div>
							<div class="valor_campo float_left">
								<input type="text" id="" class="caja_texto" title="${nombrePrestacionAnexo}" value="${nombrePrestacionAnexo}" readonly>
								<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">
							</div>
						</div>
						<div class="campo campo_detalle">
							<div class="label_campo detalle_label_campo">
								<span>${solemfmt:getLabel("label.TipoMoneda",codIdioma)}: </span>
							</div>
							<div class="valor_campo float_left">
								<select id="precioTipoMonAgrega" class="obligatorio">
									<option value="">${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
									<c:forEach items="${requestScope.listaTiposMoneda}" var="listaTiposMoneda">
										<option value="${listaTiposMoneda.parCodParametro01}">${listaTiposMoneda.parDesLargo01}</option>
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
								<input type="text" id="precioFecIniAgrega" class="caja_texto obligatorio" style="background:white" value="" readonly>	
								<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">
							</div>																
						</div> 
						<div class="campo campo_detalle">
							<div class="label_campo detalle_label_campo">
								<span>${solemfmt:getLabel("label.FechaFin",codIdioma)}: </span>
							</div>
							<div class="valor_campo float_left">
								<input type="text" id="precioFecFinAgrega" class="caja_texto obligatorio" style="background:white" value="" readonly>							
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
								<input type="text" id="precioPrecioAgrega" name="display2" class="caja_texto obligatorio" indicador="indicadorPrecio" value="" readonly="readonly">
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
										<input type="button" id="validar" class="button" value="${solemfmt:getLabel('boton.Validar',codIdioma)}" name="enter" role="button" aria-disabled="false">
									</div>							
									<div class="valor_campo">
										<textarea id="precioFormulaAgrega" class="descripcion txtAreaConFor" indicador="indicadorFormula" readonly="readonly"></textarea>		
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