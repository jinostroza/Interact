<%@ page language="java"%>
<%@ include file="../../../general/includes/declaraciones.jsp"%>
<%@ include file="../../../general/includes/htmlEncabezado.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<%
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
%>

<script type="text/javascript"> 

	$(function() {
		$.datepicker.setDefaults($.datepicker.regional['es']);

		var seccionDetalle = $('#contenedorDetalleModificadorCatalogo'); 
		
		$('input[type="radio"]').ezMark(); 
		
		$("#fecInicioMod").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#fecTerminoMod").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$("#fecTerminoMod").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#fecInicioMod").datepicker( "option", "maxDate", selectedDate );
			}
		});
    	
		var detalleCondicion = "";		
		
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
			//var dataObjeto = $("#listadoVarCond").getRowData(selectFila);

			if(selectFila != null){
				//var idBtn = $(this).attr("id"); 
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
			
			if(idTextArea=='detalleCondicion') 
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
			var desTextAreaCondicion = $("#detalleCondicion").attr("readonly");
			var desTextAreaFormula   = $("#detalleFormula").attr("readonly"); 
			var puntero= $(this).attr("puntero");  
			  
			if($("#variablesCondicionales").is(":visible")){ 
				puntero="punteroCondicion";  
				$(".char").attr("puntero",puntero);  
				$('.btnUsar').attr("puntero",puntero);
				$("#"+puntero).attr("accion","detalleCondicion"); 
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
					accion="detalleCondicion";  
					
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
					
					if($("#gridValoresPosibles").is(":visible")){ 
						var selectFila02 = $("#listadoValoresPosibles").jqGrid('getGridParam','selrow'); 
						var dataObjGrid02 = $("#listadoValoresPosibles").getRowData(selectFila02); 
						var valor02 = (dataObjGrid02.codigo);
						var strAgregar = strA+"("+valor01+" "+valorBtn+" \u0022" + valor02 +"\u0022)"+strB;
										
						if((valor01!=null) && (valorBtn!=null) && (valor02!=null)){
							$("#"+accion).val(strAgregar);
						}else 
							alert("${solemfmt:getLabel('alert.FaltaSelecionarUnObj', codIdioma)}");
		 
						var strModif = strA+"("+valor01+" "+valorBtn+" '" + valor02 +"')";
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
							alert("${solemfmt:getLabel('alert.FaltaSelecionarUnObj', codIdioma)}");
						
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
								 alert("${solemfmt:getLabel('alert.FaltaSelecionarUnObj', codIdioma)}");

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
					if(valor!=null) 
						$("#"+accion).val(strAgregar);
					else 
						alert("${solemfmt:getLabel('alert.SeleccioneVarPrest', codIdioma)}");  
	 				var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar); 
				}else if(puntero == "punteroFormula" && desTextAreaFormula!="readonly"){
					if(valor!=null) 
						$("#"+accion).val(strAgregar);
					else 
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
					if(valor!=null)
						$("#"+accion).val(strAgregar);
					else
						alert("${solemfmt:getLabel('alert.SeleccioneVarReferencial', codIdioma)}");  
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar);   
				}else if(puntero == "punteroFormula" && desTextAreaFormula!="readonly"){
					if(valor!=null)
						$("#"+accion).val(strAgregar);
					else
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
					if(valor!=null)
						$("#"+accion).val(strAgregar);
					else 
						alert("${solemfmt:getLabel('alert.SeleccioneVarGlobal', codIdioma)}"); 
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar);
				}else if(puntero == "punteroFormula" && desTextAreaFormula!="readonly"){
					if(valor!=null)
						$("#"+accion).val(strAgregar);
					else
						alert("${solemfmt:getLabel('alert.SeleccioneVarGlobal', codIdioma)}");  
					var strModif = strA+valor;
					var numChar = strModif.length;
					$("#"+puntero).attr("numChar",numChar);   
				}   
			}	 
		});

		$('#catDetallecondicion').click(function(){   
			var desabilitado = $('#detalleCondicion').attr("readonly"); 
			var valCondicion = $.trim($('#detalleCondicion').val());   
			var condicion = "";
			
			if(desabilitado!="readonly" && valCondicion!=""){
				getAjaxCall(getUrlValidarCondicion(), function(xml){  
					$('#catDetallecondicion').val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
					$('#detalleCondicion').attr("readonly",true); 
					$('#detalleCondicion').parent().children("img.imgNoOk").css("display","none");
					$('#detalleCondicion').parent().children("img.imgOk").css("display","block"); 
					$('textarea#detalleCondicion').css("background","#99FF99");  //829664 99FF99
					detalleCondicion = "valido";
		       	});  
			}else if(valCondicion==""){  
				$('#detalleCondicion').css("background","#white");
				$('#detalleCondicion').parent().children("img.imgNoOk").css("display","none");
				$('#detalleCondicion').parent().children("img.imgOk").css("display","none");  
			}else{ 
				$(this).val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
				$('#detalleCondicion').attr("readonly",false); 
				$('#detalleCondicion').parent().children("img.imgNoOk").css("display","none");
				$('#detalleCondicion').parent().children("img.imgOk").css("display","none");  
				$('#detalleCondicion').css("background","white");
			}

			if($('#popup_container').is(":visible") && detalleCondicion!="valido"){  
				$('#detalleCondicion').css("background","#C86464");
				$('#detalleCondicion').parent().children("img.imgNoOk").css("display","block");
				$('#detalleCondicion').parent().children("img.imgOk").css("display","none");  
			}
		}); 

		$('#detalleCondicion').focus(function(){   
			var detalleCondicion = $('#detalleCondicion').attr("readonly");
			if(detalleCondicion!="readonly"){
				$('#detalleCondicion').css("background","white"); 
				$('#detalleCondicion').parent().children("img.imgNoOk").css("display","none");
				$('#detalleCondicion').parent().children("img.imgOk").css("display","none");  
			}
		});

		$('#catDetalleFormula').click(function(){    
			var desabilitado = $('#detalleFormula').attr("readonly"); 
			var valFormula = $.trim($('#detalleFormula').val());   
			var formula = "";
			
			if(desabilitado!="readonly" && valFormula!=""){
				getAjaxCall(getUrlValidarFormula(), function(xml){  
					$('#catDetalleformula').val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
					$('#detalleFormula').attr("readonly",true); 
					$('#detalleFormula').parent().children("img.imgNoOk").css("display","none");
					$('#detalleFormula').parent().children("img.imgOk").css("display","block"); 
					$('textarea#detalleFormula').css("background","#99FF99");  //829664 99FF99
					detalleFormula = "valido";
		       	});  
			}else if(valFormula==""){  
				$('#detalleFormula').css("background","#white");
				$('#detalleFormula').parent().children("img.imgNoOk").css("display","none");
				$('#detalleFormula').parent().children("img.imgOk").css("display","none");  
			}else{ 
				$(this).val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
				$('#detalleFormula').attr("readonly",false); 
				$('#detalleFormula').parent().children("img.imgNoOk").css("display","none");
				$('#detalleFormula').parent().children("img.imgOk").css("display","none");  
				$('#detalleFormula').css("background","white");
			}

			if($('#popup_container').is(":visible") && detalleFormula!="valido"){  
				$('#detalleFormula').css("background","#C86464");
				$('#detalleFormula').parent().children("img.imgNoOk").css("display","block");
				$('#detalleFormula').parent().children("img.imgOk").css("display","none");  
			}
		});

		$('input#guardarFiltros').click(function(){ 
			var detalleCondicion = $("#detalleCondicion").attr("readonly");
			var detalleFormula   = $("#detalleFormula").attr("readonly");
			var mjeVacio  = ""; 
			
			if(validaFiltrosAgregaModificador()){ 
				if(detalleCondicion!="readonly"){
					mjeVacio = "${solemfmt:getLabel('label.Condicion', codIdioma)}";
					if(detalleFormula!="readonly")	
						mjeVacio = "${solemfmt:getLabel('label.CondicionYFormula', codIdioma)}";
					
				}else if(detalleFormula!="readonly"){
						mjeVacio = "${solemfmt:getLabel('label.Formula', codIdioma)}"; 
				}
								
				if (detalleCondicion=="readonly" && detalleFormula=="readonly"){
					//$('#formdetalleModificadores').attr('action', getUrlDetalleModificador());
					//$('#formdetalleModificadores').submit();
					getXml('SvtCatalogos', getUrlDetalleModificador(), function(xml){
						alert('${solemfmt:getLabel("alert.ModificadorActualizado", codIdioma)}');
			    	});
				}else 
					alert("${solemfmt:getLabel('alert.NecesitasValidar', codIdioma)} "+ mjeVacio+"!!");	
			}  
		}); 
		
		verificaDatosCargados();
	});

	function verificaDatosCargados(){
		var valorsCondicion = '${modificador.sCondicion}';
		var valorsFormula = '${modificador.sFormula}';
		 
		if(valorsCondicion!=""){
			$("#catDetallecondicion").val("${solemfmt:getLabel('boton.Modificar',codIdioma)}");
			$("#detalleCondicion").attr("readonly",true).css("background","#9F9");
		}
		if(valorsFormula!=""){
			$("#catDetalleformula").val("${solemfmt:getLabel('boton.Modificar',codIdioma)}");
			$("#detalleFormula").attr("readonly",true).css("background","#9F9");
		}
	}

	function getUrlDetalleModificador(){ 
		var sData = 'KSI=${sesion.sesIdSesion}';
	    sData += '&accion=actualizarModificadoresCatalogo';
		sData += '&modificadorCatalogo.nIdCatalogoModificador='+ $('#catIdMod').val();
		sData += '&modificadorCatalogo.sCodTipo=' + $('#catTipoMod').val();
		sData += '&modificadorCatalogo.sNombre=' + $('#catnombreMod').val();
		sData += '&modificadorCatalogo.sFhoInicio=' + desformateaFechaSinHora('#fecInicioMod');
		sData += '&modificadorCatalogo.sFhoFin=' + desformateaFechaSinHora('#fecTerminoMod');
		sData += '&modificadorCatalogo.sCondicion=' + $('#detalleCondicion').val();
		sData += '&modificadorCatalogo.sFormula=' + $('#detalleFormula').val();
		sData += '&modificadorCatalogo.nIdActualizador=${sesion.sesIdUsuario}';
		return sData;
	} 

	function getUrlValidarCondicion(){
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=validarCondicionModificadoresCatalogo';
		sData += '&sCondicion=' + $('#detalleCondicion').val();
		return sData;
	} 
	
	function validaFiltrosAgregaModificador(){ 
		var cont = 0; 
		$("#formdetalleModificadores .obligatorio").each(function(){
			var value = $.trim($(this).val());   
			if(value==""){
				$(this).parent().children("img").css("display","block");
				$(this).parent().parent().children("div.contieneButtonLupa").children("img").css("display","block");
				cont++;
			}else {
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

<input type="hidden" id="indicadorCondicion" value="0">
<input type="hidden" id="indicadorFormula" value="0">
<input type="hidden" id="punteroCondicion" numChar="" indicador="indicadorCondicion" accion="detalleCondicion" value="0">
<input type="hidden" id="punteroFormula"  numChar="" indicador="indicadorFormula" accion="detalleFormula" value="0">
<div id="contenedorDetalleModificadorCatalogo" class="detalle"> 
	<div id="" class="cont_sec_det"> 
		<div class="titulo">  
			<div id="" class="contieneTituloAgrega"> 
				<span>${solemfmt:getLabel("label.Prestacion",codIdioma)} ${nombreCatalogo} - ${nombrePrestacionCatalogo} - ${solemfmt:getLabel("label.DetalleModificador",codIdioma)}</span>
			</div>		 
			<div class="floatLeftBtn">
				<div class="divBtn floatLeftBtn">
					<input class="button" type="button" id="guardarFiltros" value="${solemfmt:getLabel('boton.Guardar',codIdioma)}" role="button" aria-disabled="false" align="right">
				</div>
			</div>	
		</div>  
		<div class="contenido_detalle sombra">
		<input type= hidden id="catIdMod" value="${modificador.nIdCatalogoModificador}">
			<form id="formdetalleModificadores" method="post">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>Tipo Modificador: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="catTipoMod" class=" obligatorio">
								<c:forEach items="${requestScope.listamodificador}" var="tipomodificador">
									<option value="${tipomodificador.parCodParametro01}" <c:if test='${modificador.sCodTipo == tipomodificador.parCodParametro01}'> selected</c:if>>${tipomodificador.parDesLargo01}</option>
								</c:forEach>
							</select>
							<img class="imgNoOk validadorCampo2" alt='' src='${rutaImg}iconos/badCheck.png'>
						</div>
					</div>					
					<div class="campo campo_detalleLargo">
						<div class="label_campo detalle_label_campo label_Largo">
							<span>Nombre Modificador: </span>
						</div>
						<div class="valor_campo float_left left2">
							<input type="text" id="catnombreMod" class="caja_texto" value="${modificador.sNombre}">
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">
						</div>
					</div>
				</div> 
				<div class="fila alturaFila"> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>Fecha Inicio: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="fecInicioMod" class="caja_texto" value="${solemfmt:stringToFecha(modificador.sFhoInicio)}">	
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">											
						</div>																
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>Fecha T&eacute;rmino: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="fecTerminoMod" class="caja_texto" value="${solemfmt:stringToFecha(modificador.sFhoFin)}">	
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">										
						</div>																	
					</div>
				</div>
				<div class="seccionTxtAreaBtns">
					<div id="contieneTextAreas"> 
						<div class="fila alturaFilaTxA">
							<div class="campo campo_detalleTxA">
								<div class="label_campo label_campoTxa">
									<br><span>Condici&oacute;n: </span> 
								</div>
								<div class="divBtn"> 
									<input type="button" id="catDetallecondicion" class="button" value='${solemfmt:getLabel("boton.Modificar", codIdioma)}' name="enter" role="button" aria-disabled="false">
								</div>							
								<div class="valor_campo">
									<textarea id="detalleCondicion" name="" puntero="punteroCondicion" class="descripcion txtAreaConFor obligatorio" indicador="indicadorCondicion" readonly>${modificador.sCondicion}</textarea>
									<img class="imgNoOk areaForCon" alt="" src="general/imagenes/iconos/badCheck.png">
									<img class="imgOk areaForCon" alt="" src="general/imagenes/iconos/okCheck.png">															
								</div>
							</div>   
						</div> 
						<div class="fila alturaFilaTxA">
							<div class="campo campo_detalleTxA">
								<div class="label_campo label_campoTxa">
									<br><span>F&oacute;rmula: </span> 
								</div>
								<div class="divBtn"> 
									<input type="button" id="catDetalleFormula" class="button" value='${solemfmt:getLabel("boton.Modificar", codIdioma)}' name="enter" role="button" aria-disabled="false" >
								</div>							
								<div class="valor_campo">
									<textarea id="detalleFormula" puntero="punteroFormula" name="" class="descripcion txtAreaConFor obligatorio" indicador="indicadorFormula" readonly>${modificador.sFormula}</textarea>
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="detalleCondicion" value="${solemfmt:getLabel('boton.Usar',codIdioma)}" role="button" aria-disabled="false">
								</div>
					 			<div class="divBtn btnPosicion margenBtnAyuda">
									<input class="button" type="button" id="ayuda" value="${solemfmt:getLabel('boton.Ayuda',codIdioma)}" role="button" aria-disabled="false">
								</div>	 					
							</div>																															
						</div>
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="detalleCondicion" value="${solemfmt:getLabel('boton.Usar',codIdioma)}" role="button" aria-disabled="false">
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="detalleCondicion" value="${solemfmt:getLabel('boton.Usar',codIdioma)}" role="button" aria-disabled="false">
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="detalleCondicion" value="${solemfmt:getLabel('boton.Usar',codIdioma)}" role="button" aria-disabled="false">
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