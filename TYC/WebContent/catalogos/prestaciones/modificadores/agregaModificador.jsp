<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript"> 

	$(function() {
		var condicion = "";
		var seccionDetalle = $('#contenedorAgregaModificadorCatalogo'); 
		
		$('input[type="radio"]').ezMark(); 

		$("#fecAgregaInicioMod").datepicker({ 
			dateFormat: 'dd/mm/yy', 
			changeMonth: true, 
			onSelect: function( selectedDate ) {
				$("#fecAgregaTerminoMod").datepicker( "option", "minDate", selectedDate );
			}
		}); 
		
		$("#fecAgregaTerminoMod").datepicker({
			dateFormat: 'dd/mm/yy',
			changeMonth: true,
			onSelect: function( selectedDate ) {
				$("#fecAgregaInicioMod").datepicker( "option", "maxDate", selectedDate );
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
			
			if(idTextArea=='Agregacondicion') 
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
			var desTextAreaCondicion = $("#Agregacondicion").attr("readonly");
			var desTextAreaFormula   = $("#Agregaformula").attr("readonly"); 
			var puntero= $(this).attr("puntero");  
			  
			if($("#variablesCondicionales").is(":visible")){ 
				puntero="punteroCondicion";  
				$(".char").attr("puntero",puntero);  
				$('.btnUsar').attr("puntero",puntero);
				$("#"+puntero).attr("accion","Agregacondicion"); 
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
					accion="Agregacondicion";  
					
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
							alert("${solemfmt:getLabel('alert.FaltaSelecionarUnObj', codIdioma)}"); 
		 
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
							alert("${solemfmt:getLabel('alert.FaltaSelecionarUnObj', codIdioma)}");
		
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
					if(valor!=null){
						$("#"+accion).val(strAgregar);
					}else 
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
		 
		$('#catAgregaCondicion').click(function(){   
			var desabilitado = $('#Agregacondicion').attr("readonly"); 
			var valCondicion = $.trim($('#Agregacondicion').val());   
			var condicion = "";
			
			if(desabilitado!="readonly" && valCondicion!=""){
				getAjaxCall(getUrlValidarCondicion(), function(xml){   
					$('#catAgregaCondicion').val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
					$('#Agregacondicion').attr("readonly",true); 
					$('#Agregacondicion').parent().children("img.imgNoOk").css("display","none");
					$('#Agregacondicion').parent().children("img.imgOk").css("display","block"); 
					$('textarea#Agregacondicion').css("background","#99FF99");  //829664 99FF99
					condicion = "valido";
		       	});  
			}else if(valCondicion==""){  
				$('#Agregacondicion').css("background","#white");
				$('#Agregacondicion').parent().children("img.imgNoOk").css("display","none");
				$('#Agregacondicion').parent().children("img.imgOk").css("display","none");  
			}else{ 
				$(this).val('${solemfmt:getLabel("boton.Validar", codIdioma)}');
				$('#Agregacondicion').attr("readonly",false); 
				$('#Agregacondicion').parent().children("img.imgNoOk").css("display","none");
				$('#Agregacondicion').parent().children("img.imgOk").css("display","none");  
				$('#Agregacondicion').css("background","white");
			}

			if($('#popup_container').is(":visible") && condicion!="valido"){  
				$('#Agregacondicion').css("background","#C86464");
				$('#Agregacondicion').parent().children("img.imgNoOk").css("display","block");
				$('#Agregacondicion').parent().children("img.imgOk").css("display","none");  
			}
		}); 

		$('#Agregacondicion').focus(function(){   
			var condicion = $('#Agregacondicion').attr("readonly");
			if(condicion!="readonly"){
				$('#Agregacondicion').css("background","white"); 
				$('#Agregacondicion').parent().children("img.imgNoOk").css("display","none");
				$('#Agregacondicion').parent().children("img.imgOk").css("display","none");  
			}
		}); 
		/*   Agregado linea 309 noriginal*/ 
		$('#catAgregaFormula').click(function(){   
			var desabilitado = $('#Agregaformula').attr("readonly"); 
			var valFormula = $.trim($('#Agregaformula').val());   
			var formula = "";
			
			if(desabilitado!="readonly" && valFormula!=""){
				getAjaxCall(getUrlValidarFormula(), function(xml){  
					$('#catAgregaFormula').val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
					$('#Agregaformula').attr("readonly",true); 
					$('#Agregaformula').parent().children("img.imgNoOk").css("display","none");
					$('#Agregaformula').parent().children("img.imgOk").css("display","block"); 
					$('textarea#Agregaformula').css("background","#99FF99");  //829664 99FF99
					formula = "valido";
		       	});  
			}else if(valFormula==""){  
				$('#Agregaformula').css("background","#white");
				$('#Agregaformula').parent().children("img.imgNoOk").css("display","none");
				$('#Agregaformula').parent().children("img.imgOk").css("display","none");  
			}else{ 
				$(this).val('${solemfmt:getLabel("boton.Modificar", codIdioma)}');
				$('#Agregaformula').attr("readonly",false); 
				$('#Agregaformula').parent().children("img.imgNoOk").css("display","none");
				$('#Agregaformula').parent().children("img.imgOk").css("display","none");  
				$('#Agregaformula').css("background","white");
			}

			if($('#popup_container').is(":visible") && formula!="valido"){  
				$('#Agregaformula').css("background","#C86464");
				$('#Agregaformula').parent().children("img.imgNoOk").css("display","block");
				$('#Agregaformula').parent().children("img.imgOk").css("display","none");  
			} 
		}); 

		$('input#guardarFiltros').click(function(){ 
			var condicion = $("#Agregacondicion").attr("readonly");
			var formula   = $("#Agregaformula").attr("readonly");
			var mjeVacio  = ""; 
			
			if(validaFiltrosAgregaModificador()){ 
				if(condicion!="readonly"){
					mjeVacio = "${solemfmt:getLabel('label.Condicion', codIdioma)}";
					if(formula!="readonly")	
						mjeVacio = "${solemfmt:getLabel('label.CondicionYFormula', codIdioma)}";
					
				}else if(formula!="readonly"){
						mjeVacio = "${solemfmt:getLabel('label.Formula', codIdioma)}"; 
						
				}
								
				if (condicion=="readonly" && formula=="readonly"){
					$('#formAgregaModificadores').attr('action', getUrlAgregarModificador());
					$('#formAgregaModificadores').submit();
				}else 
					alert("${solemfmt:getLabel('alert.NecesitasValidar', codIdioma)} "+ mjeVacio+"!!");
			}  
		});        
	}); 

	function getUrlAgregarModificador(){
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=guardarModificadoresCatalogo';
		sData += '&modificadorCatalogo.nIdCatalogoPrestacion='+ ${idCatalogoPrestacion};
		sData += '&modificadorCatalogo.sCodTipo=' + $('#catAgregaTipoMod').val();
		sData += '&modificadorCatalogo.sNombre=' + $('#catAgreganombreMod').val();
		sData += '&modificadorCatalogo.sFhoInicio=' + desformateaFechaSinHora('#fecAgregaInicioMod');
		sData += '&modificadorCatalogo.sFhoFin=' + desformateaFechaSinHora('#fecAgregaTerminoMod');
		sData += '&modificadorCatalogo.sCondicion=' + $('#Agregacondicion').val();
		sData += '&modificadorCatalogo.sFormula=' + $('#Agregaformula').val();
		sData += '&modificadorCatalogo.nIdCreador=${sesion.sesIdUsuario}';	
		sData += '&indRuta=S';
		return sData;
	} 

	function getUrlValidarCondicion(){
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=validarCondicionModificadoresCatalogo';
		sData += '&sCondicion=' + $('#Agregacondicion').val();
		return sData;
	} 

	function getUrlValidarFormula(){
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=validarFormula';
		sData += '&sFormula=' + $('#Agregaformula').val();
		return sData;
	} 
	
	function validaFiltrosAgregaModificador(){
		var cont = 0; 
		$("#formAgregaModificadores .obligatorio").each(function(){
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
<input type="hidden" id="punteroCondicion" numChar="" indicador="indicadorCondicion" accion="Agregacondicion" value="0">
<input type="hidden" id="punteroFormula"  numChar="" indicador="indicadorFormula" accion="Agregaformula" value="0">
<div id="contenedorAgregaModificadorCatalogo" class="detalle"> 
	<div id="" class="cont_sec_det"> 
		<div class="titulo">  
			<div id="" class="contieneTituloAgrega"> 
				<span>${nombreCatalogo} - ${nombrePrestacionCatalogo} - Nuevo Modificador</span>
			</div>		 
			<div class="floatLeftBtn">
				<!--  <div class="divBtn floatLeftBtn">
					<input class="button" type="button" id="eliminarFiltros" value="Eliminar" role="button" aria-disabled="false">
				</div>-->
				<div class="divBtn floatLeftBtn">
					<input class="button" type="button" id="guardarFiltros" value="${solemfmt:getLabel('boton.Guardar',codIdioma)}" role="button" aria-disabled="false">
				</div>
			</div>	
		</div> 
		<div class="contenido_detalle sombra">
			<form id="formAgregaModificadores" method="post">
				<div class="fila alturaFila">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>Tipo Modificador: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="catAgregaTipoMod" class=" obligatorio">
								<c:forEach items="${requestScope.listamodificador}" var="tipomodificador">
									<option value="${tipomodificador.parCodParametro01}">${tipomodificador.parDesLargo01}</option>
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
							<input type="text" id="catAgreganombreMod" class="caja_texto obligatorio" value="">
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
							<input type="text" id="fecAgregaInicioMod" class="caja_texto obligatorio" style="background: white" value="" readonly>	
							<img class="imgNoOk validadorCampo2" alt="" src="general/imagenes/iconos/badCheck.png">											
						</div>																
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>Fecha T&eacute;rmino: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="fecAgregaTerminoMod" class="caja_texto obligatorio" style="background: white" value="" readonly>
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
									<input type="button" id="catAgregaCondicion" class="button" value='${solemfmt:getLabel("boton.Modificar", codIdioma)}' role="button" aria-disabled="false">
								</div>							
								<div class="valor_campo">
									<textarea id="Agregacondicion" name="" puntero="punteroCondicion" class="descripcion txtAreaConFor obligatorio" indicador="indicadorCondicion"></textarea>
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
									<input type="button" id="catAgregaFormula" class="button" value='${solemfmt:getLabel("boton.Modificar", codIdioma)}'  role="button" aria-disabled="false">
								</div>							
								<div class="valor_campo">
									<textarea id="Agregaformula"  puntero="punteroFormula" name="" class="descripcion txtAreaConFor obligatorio" indicador="indicadorFormula"></textarea>
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
											<span>Valor: </span>
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="condicion" value="Usar" role="button" aria-disabled="false">
								</div>
					 			<div class="divBtn btnPosicion margenBtnAyuda">
									<input class="button" type="button" id="ayuda" value="Ayuda" role="button" aria-disabled="false">
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="condicion" value="Usar" role="button" aria-disabled="false">
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="condicion" value="Usar" role="button" aria-disabled="false">
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
									<input class="btnUsar button" type="button" id="btnUsar" puntero="punteroCondicion" numChar=""  indicador="indicadorCondicion" accion="condicion" value="Usar" role="button" aria-disabled="false">
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