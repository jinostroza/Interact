<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">   
	
	$(function() {
		var condicion = "";  
		var textAreaCondicion = $('#condicion');
		var textAreaFormula = $('#formula');
		
		$('input[type="radio"]').ezMark();  

		$("#conDetalleFecIniMod").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#conDetalleFecFinMod").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		$("#conDetalleFecFinMod").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#conDetalleFecIniMod").datepicker( "option", "maxDate", selectedDate );
			}
		});   
		
		$('.btnVertical').click(function(){ 
			var selectFila = $("#listadoVarCond").jqGrid('getGridParam','selrow');  
			if(selectFila != null){
				$('#botoneraVertical input').each(function(){  
					$(this).removeClass("btnSelected").removeClass("sombra"); 
				}); 
				$(this).addClass("btnSelected").addClass("sombra");
			}else 
				alert("${solemfmt:getLabel('alert.SeleccioneVarCond', codIdioma)}");  
		});   
  
		$('#valor').blur(function(e){
			soloNumerosOnBlur('valor');
		});  
		
		$('#valor').keypress(function(e){ 
			  return soloNumeros(e);
		});   
  
		$('.btnVertical').click(function(){ 
			var selectFila = $("#listadoVarCond").jqGrid('getGridParam','selrow');   
			if(selectFila != null){ 
				$('#botoneraVertical input').each(function(){  
					$(this).removeClass("btnSelected").removeClass("sombra"); 
				}); 
				$(this).addClass("btnSelected").addClass("sombra");
			}else 
				alert("${solemfmt:getLabel('alert.SeleccioneVarCond', codIdioma)}");    
		});  

		$("textarea").click(function(){    
			var puntero = $(this).attr("puntero");
			var idTextArea = $(this).attr("id"); 
			var indTextArea = $(this).attr("indicador");  
			var indicator = document.getElementById(indTextArea); 
			var textarea = document.getElementById(idTextArea);  
			 
			setInterval(function(){
				indicator.value = caret(textarea); 
			}, 100);

			$("#"+puntero).attr("accion",idTextArea); 
			$("#"+puntero).attr("indicador",indTextArea);   
			
			if(idTextArea=='condicion') 
				$(".botonesOpcionales").show();
			else
				$(".botonesOpcionales").hide();   
		});

		$("textarea").blur(function(){   
			var puntero = $(this).attr("puntero");
			var indicador = $(this).attr("indicador");
			var numChar = $("#"+indicador).val();     
			$("#"+puntero).attr("numChar",numChar);  
			$(".char").attr("puntero",puntero);  
			$(".btnUsar").attr("puntero",puntero);
		});
					
		$('.btnUsar').click(function(){   
			var desTextAreaCondicion = textAreaCondicion.attr("readonly");
			var desTextAreaFormula   = textAreaFormula.attr("readonly"); 
			var puntero= $(this).attr("puntero");  
			  
			if($("#variablesCondicionales").is(":visible")){ 
				puntero="punteroCondicion";  
				$(".char").attr("puntero",puntero);  
				$('.btnUsar').attr("puntero",puntero);
				$("#"+puntero).attr("accion","condicion"); 
				$("#"+puntero).attr("indicador","indicadorCondicion");  
			} 
			
			var accion = $("#"+puntero).attr('accion');
			var indicador = $("#"+puntero).attr('indicador');     
			var indicadorValue = $("#"+puntero).attr("numChar");
			
			if(indicadorValue=="")
				indicadorValue = $("#"+indicador).val();
			
			var strDividir = $("#"+accion).val();
			var strA = strDividir.substring(0,indicadorValue);
			var strB = strDividir.substring(indicadorValue);
			var numCaract = strDividir.length; 

			if (strDividir!='' && indicadorValue==0 && strA!='' && strB!=''){  
				indicadorValue=numCaract; 
				strA = strDividir.substring(0,indicadorValue);
				strB = strDividir.substring(indicadorValue);
			}   
			
			if($("#variablesCondicionales").is(":visible")){  
				if(desTextAreaCondicion!="readonly"){  
					accion="condicion";   
					
					/*para el SelectRow 1ra GRILLA*/
					var selectFila01 = $("#listadoVarCond").jqGrid('getGridParam','selrow');  
					var dataObjGrid01 = $("#listadoVarCond").getRowData(selectFila01);
					var valor01 = (dataObjGrid01.codigo);
			
					/*para el opc BOTONERA VERTICAL*/
					var id = $(".btnSelected").attr("id");
					var valorBtn = $("#"+id).val();    
			
					/*para el opc hora*/
					var campoHora ="";
					var campoMinuto = "";
					
					/*para el opc valor*/
					var valorInput = "";
					
					if($("#gridValoresTipoAdmision").is(":visible")){ 
						var selectFila02 = $("#listadoValoresTipoAdmision").jqGrid('getGridParam','selrow'); 
						var dataObjGrid02 = $("#listadoValoresTipoAdmision").getRowData(selectFila02); 
						var valor02 = (dataObjGrid02.codValTA);
						var strAgregar = strA+"("+valor01+" "+valorBtn+" \u0022" + valor02 +"\u0022)"+strB;
										
						if((valor01!=null) && (valorBtn!=null) && (valor02!=null)){
							$("#"+accion).val(strAgregar);
						}else
							alert('${solemfmt:getLabel("alert.FaltaObjetoSeleccionar",codIdioma)}');
		 
						var strModif = strA+"("+valor01+" "+valorBtn+" '" + valor02 +"')";
						var numChar = strModif.length;
						$("#"+puntero).attr("numChar",numChar);  //
						
					}else if($("#gridValoresUniOrg").is(":visible")){
						
						var selectFilaUO = $("#listadoValoresUniOrg").jqGrid('getGridParam','selrow'); 
						var dataObjGridUO = $("#listadoValoresUniOrg").getRowData(selectFilaUO); 
						var valor03 = (dataObjGridUO.codUO);
						var strAgregar = strA+"("+valor01+" "+valorBtn+" '" + valor03 +"')"+strB;
						
						if((valor01!=null) && (valorBtn!=null) && (valor03!=null)){
							$("#"+accion).val(strAgregar);
						}else
							alert('${solemfmt:getLabel("alert.FaltaObjetoSeleccionar",codIdioma)}');
		
						var strModif = strA+"("+valor01+" "+valorBtn+" '" + valor03 +"')";
						var numChar = strModif.length;
						$("#"+puntero).attr("numChar",numChar);  
						
					}else if($("#gridValoresPosiblesFecha").is(":visible")){
						var selectFila03 = $("#listadoValPosFec").jqGrid('getGridParam','selrow'); 
						var dataObjGrid03 = $("#listadoValPosFec").getRowData(selectFila03); 
						var valor03 = (dataObjGrid03.codigo);
						var strAgregar = strA+"("+valor01+" "+valorBtn+" '" + valor03 +"')"+strB;
						
						if((valor01!=null) && (valorBtn!=null) && (valor03!=null)){
							$("#"+accion).val(strAgregar);
						}else
							alert('${solemfmt:getLabel("alert.FaltaObjetoSeleccionar",codIdioma)}');
		
						var strModif = strA+"("+valor01+" "+valorBtn+" '" + valor03 +"')";
						var numChar = strModif.length;
						$("#"+puntero).attr("numChar",numChar);  
						
					}else if($("#opcValorHora").is(":visible")){   
						var horaSeleccionada = $("#horaCondicion").val();
						var numCaracteres = horaSeleccionada.length;
						var campoHora = horaSeleccionada.split(":")[0]; 
						var campoMinuto = horaSeleccionada.split(":")[1];
						var lengthHora = campoHora.length; 
						
						if(numCaracteres==5 && lengthHora==2){ 
							var strAgregar = strA+"("+valor01+" "+valorBtn+" '" + campoHora + campoMinuto +"')"+strB;
							
							if((valor01!=null) && (valorBtn!=null) && (campoHora!=null) && (campoMinuto!=null)){
								$("#"+accion).val(strAgregar);
							}else
								alert('${solemfmt:getLabel("alert.FaltaObjetoSeleccionar",codIdioma)}');

							var strModif = strA+"("+valor01+" "+valorBtn+" '" + campoHora + campoMinuto +"')";
							var numChar = strModif.length;
							$("#"+puntero).attr("numChar",numChar);   
						}else{ 
							alert("${solemfmt:getLabel('alert.HoraSelecIncorrecta', codIdioma)}");    
							$("#horaCondicion").val("");
						}
					}else if($("#opcValorInput").is(":visible")){ 
						valorInput = $("#valor").val();  
						var strAgregar = strA+"("+valor01+" "+valorBtn+" " + valorInput +")"+strB;
							
						if((valor01!=null) && (valorBtn!=null) && (valorInput!=null)){
							$("#"+accion).val(strAgregar);
						}else 
							alert("${solemfmt:getLabel('alert.FaltaSelecionarUnObj', codIdioma)}");    
		
						var strModif = strA+"("+valor01+" "+valorBtn+" " + valorInput +")";
						var numChar = strModif.length;
						$("#"+puntero).attr("numChar",numChar);  
					} 
				}else{ 
					alert("${solemfmt:getLabel('alert.CondicionValidada', codIdioma)}");
				}	
				
			}else if($("#variablesprestacion").is(":visible")){
				var selectFila  =  $("#listadoVarPrestacion").jqGrid('getGridParam','selrow');  
				var dataObjGrid =  $("#listadoVarPrestacion").getRowData(selectFila);
				var valor       =  (dataObjGrid.varPres);  
				var strAgregar  =  strA+valor+strB; 
				
				if(puntero == "punteroCondicion" && desTextAreaCondicion!="readonly"){
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else 
						alert("${solemfmt:getLabel('alert.SeleccioneVarPrest', codIdioma)}");
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar); 
				}else if(puntero == "punteroFormula" && desTextAreaFormula!="readonly"){
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else
						alert("${solemfmt:getLabel('alert.SeleccioneVarPrest', codIdioma)}");
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar); 
				}   
			}else if($("#variablesDeReferencia").is(":visible")){
				var selectFila  =  $("#listadoVarRefe").jqGrid('getGridParam','selrow');  
				var dataObjGrid =  $("#listadoVarRefe").getRowData(selectFila);
				var valor       =  (dataObjGrid.varRef);  
				var strAgregar  =  strA+valor+strB; 
				
				if(puntero == "punteroCondicion" && desTextAreaCondicion!="readonly"){
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else
						alert("${solemfmt:getLabel('alert.SeleccioneVarReferencial', codIdioma)}"); 
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar);
				}else if(puntero == "punteroFormula" && desTextAreaFormula!="readonly"){
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else
						alert("${solemfmt:getLabel('alert.SeleccioneVarReferencial', codIdioma)}"); 
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar);
				}  	
			}else if($("#variablesGlobales").is(":visible")){
				var selectFila  =  $("#listadoVarGlobales").jqGrid('getGridParam','selrow');  
				var dataObjGrid =  $("#listadoVarGlobales").getRowData(selectFila);
				var valor       =  (dataObjGrid.varGlob);  
				var strAgregar  =  strA+valor+strB; 
				
				if(puntero == "punteroCondicion" && desTextAreaCondicion!="readonly"){
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else
						alert("${solemfmt:getLabel('alert.SeleccioneVarGlobal', codIdioma)}"); 
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar); 
				}else if(puntero == "punteroFormula" && desTextAreaFormula!="readonly"){
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else
						alert("${solemfmt:getLabel('alert.SeleccioneVarGlobal', codIdioma)}"); 
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar);
				}  
			}	 
		});  
		 
		$('#conDetalleCondicion').click(function(){   
			var desabilitado = textAreaCondicion.attr("readonly"); 
			var valCondicion = $.trim(textAreaCondicion.val());   
			var condicion = "";
			
			if(desabilitado!="readonly" && valCondicion!=""){
				getAjaxCall(getUrlValidarCondicion(), function(xml){   
					$('#conDetalleCondicion').val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
					textAreaCondicion.attr("readonly",true); 
					textAreaCondicion.parent().children("img.imgNoOk").css("display","none");
					textAreaCondicion.parent().children("img.imgOk").css("display","block"); 
					$('textarea#condicion').css("background","#99FF99");  //829664 99FF99
					condicion = "valido";
		       	});  
			}else if(valCondicion==""){  
				textAreaCondicion.css("background","#white");
				textAreaCondicion.parent().children("img.imgNoOk").css("display","none");
				textAreaCondicion.parent().children("img.imgOk").css("display","none");  
			}else{ 
				$(this).val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
				textAreaCondicion.attr("readonly",false); 
				textAreaCondicion.parent().children("img.imgNoOk").css("display","none");
				textAreaCondicion.parent().children("img.imgOk").css("display","none");  
				textAreaCondicion.css("background","white");
			}

			if($('#popup_container').is(":visible") && condicion!="valido"){  
				textAreaCondicion.css("background","#C86464");
				textAreaCondicion.parent().children("img.imgNoOk").css("display","block");
				textAreaCondicion.parent().children("img.imgOk").css("display","none");  
			}
		}); 
		 
		textAreaCondicion.focus(function(){   
			var condicion = textAreaCondicion.attr("readonly");
			if(condicion!="readonly"){
				textAreaCondicion.css("background","white"); 
				textAreaCondicion.parent().children("img.imgNoOk").css("display","none");
				textAreaCondicion.parent().children("img.imgOk").css("display","none");  
			}
		});
		
		$('#conDetalleFormula').click(function(){   
			var desabilitado = textAreaFormula.attr("readonly"); 
			var valFormula = $.trim(textAreaFormula.val());   
			var formula = "";
			
			if(desabilitado!="readonly" && valFormula!=""){
				getAjaxCall(getUrlValidarFormula(), function(xml){  
					$('#conDetalleFormula').val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
					textAreaFormula.attr("readonly",true); 
					textAreaFormula.parent().children("img.imgNoOk").css("display","none");
					textAreaFormula.parent().children("img.imgOk").css("display","block"); 
					$('textarea#formula').css("background","#99FF99");  //829664 99FF99
					formula = "valido";
		       	});  
			}else if(valFormula==""){  
				textAreaFormula.css("background","#white");
				textAreaFormula.parent().children("img.imgNoOk").css("display","none");
				textAreaFormula.parent().children("img.imgOk").css("display","none");  
			}else{ 
				$(this).val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
				textAreaFormula.attr("readonly",false); 
				textAreaFormula.parent().children("img.imgNoOk").css("display","none");
				textAreaFormula.parent().children("img.imgOk").css("display","none");  
				textAreaFormula.css("background","white");
			}

			if($('#popup_container').is(":visible") && formula!="valido"){  
				textAreaFormula.css("background","#C86464");
				textAreaFormula.parent().children("img.imgNoOk").css("display","block");
				textAreaFormula.parent().children("img.imgOk").css("display","none");  
			}
			 
		}); 
		
		$('input#guardarModificador').click(function(){  
			var condicion = textAreaCondicion.attr("readonly");
			var formula   = textAreaFormula.attr("readonly");
			var mjeVacio  = ""; 
			
			if(validaFiltrosDetalleModificador()){ 
				if(condicion!="readonly"){
					mjeVacio = "Condicion";
					if(formula!="readonly")	
						mjeVacio = "${solemfmt:getLabel('label.CondicionYFormula', codIdioma)}";
				}else if(formula!="readonly"){
						mjeVacio = "${solemfmt:getLabel('label.Formula', codIdioma)}";  
				}
								
				if (condicion=="readonly" && formula=="readonly"){ 
					getXml('SvtConvenios', getUrlActualizarModificador(), function(xml){
						alert('${solemfmt:getLabel("alert.ModificadorActualizado", codIdioma)}');
			    	});
				}else{ 
					alert("${solemfmt:getLabel('alert.NecesitasValidar', codIdioma)} "+ mjeVacio+"!!");
				}	
			}  
		});  	

		verificaDatosCargados();	
	}); 

	function verificaDatosCargados(){
		var valorsCondicion = '${modificador.sCondicion}';
		var valorsFormula = '${modificador.sFormula}';
		
		if(valorsCondicion!=""){
			$("#conDetalleCondicion").val("${solemfmt:getLabel('boton.Modificar',codIdioma)}");
			$("#condicion").attr("readonly",true).css("background","#9F9");
		}
		if(valorsFormula!=""){
			$("#conDetalleFormula").val("${solemfmt:getLabel('boton.Modificar',codIdioma)}");
			$("#formula").attr("readonly",true).css("background","#9F9");
		}
	}
	 
	function getUrlActualizarModificador(){  
		var sData = 'KSI=${sesion.sesIdSesion}'; 
		sData += '&accion=actualizarModificador';
		sData += '&modificador.nIdModificador=' + $('#conDetalleIdMod').val();
		sData += '&modificador.sCodTipo=' + $('#conDetalleTipoMod').val();
		sData += '&modificador.sNombre=' + $('#conDetalleNombreMod').val();
		sData += '&modificador.sFhoInicio=' + desformateaFechaSinHora('#conDetalleFecIniMod');
		sData += '&modificador.sFhoFin=' + desformateaFechaSinHora('#conDetalleFecFinMod');
		sData += '&modificador.sCondicion=' + $('#condicion').val();
		sData += '&modificador.sFormula=' + $('#formula').val();
		sData += '&modificador.nIdUsuarioModificacion=${sesion.sesIdUsuario}';	
 
		return sData;
	}  
	
	function getUrlValidarCondicion(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=validarCondicionModificadorPrestacionConvenio';
		sData += '&sCondicion=' + $('#condicion').val();
		return sData;
	} 

	function getUrlValidarFormula(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=validarFormulaModifPrestConv';
		sData += '&sFormula=' + $('#formula').val();
		return sData;
	} 
	
	function validaFiltrosDetalleModificador(){
		var cont = 0; 
		$("#formDetalleModificadores .obligatorio").each(function(){
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

<input type="hidden" id="indicadorCondicion" value="0">
<input type="hidden" id="indicadorFormula" value="0">
<input type="hidden" id="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="condicion" value="0">
<input type="hidden" id="punteroFormula"  numChar=""  indicador="indicadorFormula" accion="formula" value="0">
<div class="detalle">   
	<div id="" class="cont_sec_det"> 
		<div class="titulo">  
			<div id="" class="contieneTituloAgrega">  
				<span>${solemfmt:getLabel("label.Prestacion",codIdioma)} "Nombre Prestaci&oacute;n" - ${solemfmt:getLabel("label.DetalleModificador",codIdioma)}</span>
			</div>		 
			<div class="floatLeftBtn">
				<!--  <div class="divBtn floatLeftBtn">
					<input class="button" type="button" id="eliminarFiltros" value="Eliminar" role="button" aria-disabled="false">
				</div>-->
				<div class="divBtn floatLeftBtn">
					<input class="button" type="button" id="guardarModificador" value="Guardar" role="button" aria-disabled="false">
				</div>
			</div>	
		</div> 
		<div class="contenido_detalle sombra">
			<form id="formDetalleModificadores" method="post">
				<input type="hidden" id="conDetalleIdMod" value="${modificador.nIdModificador}">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Tipo",codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="conDetalleTipoMod" class="obligatorio">
								<c:forEach items="${requestScope.listaTiposModificador}" var="listaTiposModificador">
									<option value="${listaTiposModificador.parCodParametro01}" <c:if test='${modificador.sCodTipo == listaTiposModificador.parCodParametro01}'> selected</c:if>>${listaTiposModificador.parDesLargo01}</option>
								</c:forEach>
							</select>	
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">
						</div>
					</div>					
					<div class="campo campo_detalleLargo">
						<div class="label_campo detalle_label_campo label_Largo">
							<span>${solemfmt:getLabel("label.Nombre",codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left left2">
							<input type="text" id="conDetalleNombreMod" class="caja_texto obligatorio" value="${modificador.sNombre}">
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
							<input type="text" id="conDetalleFecIniMod" class="caja_texto obligatorio" style="background:white" value="${solemfmt:stringToFecha(modificador.sFhoInicio)}" readonly>		
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">					
						</div>																
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.FechaFin",codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="conDetalleFecFinMod" class="caja_texto obligatorio" style="background:white" value="${solemfmt:stringToFecha(modificador.sFhoFin)}" readonly>	
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">						
						</div>																	
					</div>
				</div>
				<div class="seccionTxtAreaBtns">
					<div id="contieneTextAreas"> 
						<div class="fila alturaFilaTxA">
							<div class="campo campo_detalleTxA">
								<div class="label_campo label_campoTxa">
									<br><span>${solemfmt:getLabel("label.Condicion",codIdioma)}: </span> 
								</div>
								<div class="divBtn"> 
									<input type="button" id="conDetalleCondicion" class="button" value="${solemfmt:getLabel('boton.Validar',codIdioma)}" role="button" aria-disabled="false">
								</div>							
								<div class="valor_campo">
									<textarea id="condicion" name="" puntero="punteroCondicion" class="descripcion txtAreaConFor obligatorio" indicador="indicadorCondicion">${modificador.sCondicion}</textarea>	
									<img class="imgNoOk areaForCon" alt="" src="general/imagenes/iconos/badCheck.png">		
									<img class="imgOk areaForCon" alt="" src="general/imagenes/iconos/okCheck.png">				
								</div>
							</div>   
						</div> 
						<div class="fila alturaFilaTxA">
							<div class="campo campo_detalleTxA">
								<div class="label_campo label_campoTxa">
									<br><span>${solemfmt:getLabel("label.Formula",codIdioma)}: </span> 
								</div>
								<div class="divBtn"> 
									<input type="button" id="conDetalleFormula" class="button" value="${solemfmt:getLabel('boton.Validar',codIdioma)}" role="button" aria-disabled="false">
								</div>							
								<div class="valor_campo">
									<textarea id="formula" name="" puntero="punteroFormula" class="descripcion txtAreaConFor obligatorio" indicador="indicadorFormula">${modificador.sFormula}</textarea>
									<img class="imgNoOk areaForCon" alt="" src="general/imagenes/iconos/badCheck.png">	
									<img class="imgOk areaForCon" alt="" src="general/imagenes/iconos/okCheck.png">						
								</div>
							</div>   
						</div>
					</div> 
					<div id="divBotones">
  						<%@ include file="botoneraFull.jsp" %> 				
					</div>
				</div>
			</form>
		</div> 
	</div> 
	<div id="seccionTabs" class="cont_sec_det_tab">  
		<div id="" class="titulo">
			<div id="contieneFlecha" class="contieneFlecha">
				<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">
			</div>
			<div id="contienetitulo" class="contienetitulo"> 
			</div>
		</div> 
		<div id="detalle02" class="contenido_detalle sombra"> 
			<div class="contenidoTab">
				<div class="tabber"> 
					<div class="tabbertab">
						<h2>${solemfmt:getLabel("label.VariablesCondicionales",codIdioma)}</h2> 
						<p>
						<div id="variablesCondicionales" class="seccionTab">
							<div id="gridOpcVarCond"> 
								<%@ include file="varcond/listaVarCond.jsp" %> 
							</div>		
							<div class="contieneBotoneraVert">
								<div id="botoneraVertical">
									<div class="alturaBtn margenAlturaBtn">
										<div class="contieneBtnVertical">
											 <input class="button btnVertical" type="button" id="igualdad" value="=" role="button" aria-disabled="false">
										</div> 
									</div>			
									<div class="alturaBtn">
										<div class="contieneBtnVertical">
											 <input class="button btnVertical" type="button" id="menorMayorMenor" value="<>" role="button" aria-disabled="false"> 
										</div>											
									</div>	
									<div id="botonesVerOpc">
										<div class="alturaBtn">
											<div class="contieneBtnVertical">
												 <input class="button btnVertical" type="button" id="MayormenorMenor" value=">=" role="button" aria-disabled="false"> 
											</div>											
										</div>	
										<div class="alturaBtn">
											<div class="contieneBtnVertical">
												 <input class="button btnVertical" type="button" id="menorMayorigual" value="<=" role="button" aria-disabled="false"> 
											</div>											
										</div>	
									</div>																											
								</div>									
							</div>	
							<div id="gridValoresTipoAdmision" class="divDinamico">
								<%@ include file="valoresTipoAdmision/listaValoresTipoAdmision.jsp" %> 
							</div>	
							<div id="gridValoresUniOrg" class="divDinamico hidden_div">
								<%@ include file="valoresUniOrg/listaValoresUniOrg.jsp" %> 
							</div>							
							<div id="gridValoresPosiblesFecha" class="divDinamico hidden_div">
								<%@ include file="valoresPosiblesFecha/listaValoresPosiblesFecha.jsp" %> 
							</div>	
							<div id="opcValorInput" class="divDinamico hidden_div">
								<div class="fila_valor">
									<div class="campo">
										<div class="label_campo">
											<span>${solemfmt:getLabel("label.Valor",codIdioma)}: </span>
										</div>
										<div class="valor_campo anchoValor">
											<input type="text" id="valor" value="">
										</div>
									</div> 
								</div> 
							</div>  
							 
							<div id="opcValorHora" class="divDinamico hidden_div">
								<%@ include file="moduloHora.jsp" %> 
							</div> 	
							
							<div id="contenedorBotoneraUsarAyuda"> 
				 				<div class="divBtn btnPosicion margenbtnUsar">
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="condicion" value="${solemfmt:getLabel('boton.Usar',codIdioma)}" role="button" aria-disabled="false">
								</div>
					 			<div class="divBtn btnPosicion margenBtnAyuda">
									<input class="button" type="button" id="ayuda" value="${solemfmt:getLabel('boton.Ayuda',codIdioma)}" role="button" aria-disabled="false">
								</div>	 					
							</div>																															
						</div>
						</p> 
					</div> 
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="condicion" value="${solemfmt:getLabel('boton.Usar',codIdioma)}" role="button" aria-disabled="false">
								</div>
					 			<div class="divBtn btnPosicion margenBtnAyuda">
									<input class="button" type="button" id="ayuda" value="${solemfmt:getLabel('boton.Ayuda',codIdioma)}" role="button" aria-disabled="false">
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="condicion" value="${solemfmt:getLabel('boton.Usar',codIdioma)}" role="button" aria-disabled="false">
								</div>
					 			<div class="divBtn btnPosicion margenBtnAyuda">
									<input class="button" type="button" id="ayuda" value="${solemfmt:getLabel('boton.Ayuda',codIdioma)}" role="button" aria-disabled="false">
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="condicion" value="${solemfmt:getLabel('boton.Usar',codIdioma)}" role="button" aria-disabled="false">
								</div>
					 			<div class="divBtn btnPosicion margenBtnAyuda">
									<input class="button" type="button" id="ayuda" value="${solemfmt:getLabel('boton.Ayuda',codIdioma)}" role="button" aria-disabled="false">
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