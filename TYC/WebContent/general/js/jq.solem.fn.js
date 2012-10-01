//*******************************************************************************
//*********            DEFINICION DE FUNCIONES GENERALES             ************
//*******************************************************************************

//****************************************************
// Reemplaza caracter de un string
//****************************************************
function jreemplazaCaracter(texto, caracterOrigen, caracterDestino){
    var sTmp1 = new String(texto);

	for (var iTmp1 = 0; iTmp1 <= sTmp1.length; iTmp1++){
		if (sTmp1.charAt(iTmp1) == caracterOrigen)
			sTmp1 = sTmp1.replace(caracterOrigen,caracterDestino);
	}

    return sTmp1;
}

//****************************************************
// Formatea Numero Sin Decimales Redondeando
//****************************************************
function jformatearSinDecimal(numero){
	numero = (isNaN(numero))? "0" : Math.round(numero).toString();

	for (var i = 0; i < Math.floor((numero.length-(1+i))/3); i++)
		numero = numero.substring(0, numero.length - (4*i+3)) + oSeparadores.chrMilesMsk + numero.substring(numero.length-(4*i+3));

	return (numero);
}

//****************************************************
// Formatea Numero CON Decimales Redondeando
//****************************************************
function jformatearConDecimal(numero){
	if(isNaN(numero)){
		cents = "0";
		numero = "0";
	} else {
		cents = Math.floor((numero*100+0.5)%100);
		numero = Math.floor(numero).toString();
	}
	if(cents < 10)
		cents = "0" + cents;

	for (var i = 0; i < Math.floor((numero.length-(1+i))/3); i++)
		numero = numero.substring(0, numero.length - (4*i+3)) + oSeparadores.chrMilesMsk + numero.substring(numero.length-(4*i+3));

	return (numero + oSeparadores.chrDecimalesMsk + cents);
}

//****************************************************
// Formtea un Numero Con/Sin decimales
//****************************************************
function jformatNumber(numero, sIndDecimales){
//    if (sIndDecimales == null) sIndDecimales = "N";
    var bIndDecimales = (sIndDecimales == null)? false : ((sIndDecimales == "T")? true : false);
//	if (sIndDecimales == "N")
	if (!bIndDecimales)
		return jformatearSinDecimal(numero);
	else
		return jformatearConDecimal(numero);
}

//****************************************************
// Desformatea un Numero Con/Sin decimales
//****************************************************
function junFormatNumber(numero){
    var sNumEditMsk = jreemplazaCaracter(numero, oSeparadores.chrMilesMsk, "");
	
	return sNumEditMsk;
}

//****************************************************
// Obtiene el valor Numerico Con/Sin decimales
//****************************************************
function jobtieneValorNumerico(numero, sIndDecimales){
    var bIndDecimales = (sIndDecimales == null)? false : ((sIndDecimales == "T")? true : false);
    var sValorNumerico = jreemplazaCaracter(numero, oSeparadores.chrMilesMsk, "");
    sValorNumerico = jreemplazaCaracter(sValorNumerico, oSeparadores.chrDecimalesMsk, ".");
	
	var dValorNumerico = 0;
	if (bIndDecimales){
	    dValorNumerico = (!isNaN(parseFloat(sValorNumerico)))? parseFloat(sValorNumerico) : 0;
	} else {
	    dValorNumerico = (!isNaN(parseInt(sValorNumerico, 10)))? parseInt(sValorNumerico, 10) : 0;
	}

	return dValorNumerico;
}

// ********************************************************************************************
// Formatea un Run
// ********************************************************************************************
function jformatRun(sRun) {
    if (sRun != "") {
        var iLargoRun = sRun.length;
        var sDv = sRun.substring(iLargoRun - 1,iLargoRun);
        iLargoRun--;
        sRun = sRun.substring(0,iLargoRun);
        sRun = $.trim(sRun);
//$("#divtmpdisplay").html("<font color='#ff0000'>("+sRun+")("+sDv+")</font>");

        if (!jcheckDV(sRun, sDv)) {
            alert("El RUN es incorrecto");
            sRun = "";
        } else {
            sRun = jformatNumber(sRun);
            sRun += "-" + sDv;
        }
    }
    return sRun;
}

// ********************************************************************************************
// Chequea el dígito verificador
// ********************************************************************************************
function jcheckDV(sRut, sDigitoVer) {
var dvr = '0';
var suma = 0;
var mul  = 2;
var dvi = 0;

    for (var i = sRut.length - 1; i >= 0; i--) {
        suma += parseInt(sRut.charAt(i), 10) * mul;
        mul = (mul == 7)? 2 : (mul + 1);
    }

    var res = parseInt(suma, 10) % 11;
    if (res == 1)
        dvr = 'k';
    else
        if (res == 0)
            dvr = '0';
        else {
            dvi = 11-res;
            dvr = dvi+"";
        }
    return (dvr != sDigitoVer.toLowerCase())? false : true;
}

// ********************************************************************************************
// valida que el string de fecha enviado sea una fecha valida segun formato de ingreso
// ********************************************************************************************
function jvalidaFecha(sFecha){
	var iDiasXMes = 1;
	for(var iTmp1=0; iTmp1 <= sFecha.length - 1; iTmp1++){
		switch (iTmp1){
			case 2:
			case 5:
				if (!isNaN(parseInt(sFecha.substr(iTmp1,1), 10))) return false;
				break;

			default:
				if (isNaN(parseInt(sFecha.substr(iTmp1,1), 10))) return false;
				break;
		}
	}

	var idia = parseInt(sFecha.substr(0,2), 10);
	var imes = parseInt(sFecha.substr(3,2), 10);
	var iano = parseInt(sFecha.substr(6,4), 10);

	if ((imes < 1) || (imes > 12))
    	return false;

	switch (imes){
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			iDiasXMes = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			iDiasXMes = 30;
			break;
		default:
			iDiasXMes = ((iano % 4 == 0) && ((iano % 100 != 0) || (iano % 400 == 0)))? 29 : 28;
			break;
	}

	if ((idia < 1) || (idia > iDiasXMes))
    	return false;
	
	
	if ((iano < 1900) || (iano > 2100))
    	return false;

	return true;
}

// ********************************************************************************************
// Chequea si el codigo ascci de un caracter corresponde a un caracter imprimible o no
// ********************************************************************************************
function jCaracterImprimible(sAsciiCode) {
	var bImprimible = false;
	switch (sAsciiCode){
		case oAscii.Aacute:
		case oAscii.Eacute:
		case oAscii.Iacute:
		case oAscii.Ntilde:
		case oAscii.Oacute:
		case oAscii.Uacute:
		case oAscii.aacute:
		case oAscii.eacute:
		case oAscii.iacute:
		case oAscii.ntilde:
		case oAscii.oacute:
		case oAscii.uacute:
            //inclusiones
		    bImprimible = true;
			break;
	        //excepciones
		default:
	        //general
			bImprimible = ((sAsciiCode >= 32) && (sAsciiCode <= 126))? true : false;
			break;
	}

    return bImprimible;
}

// ********************************************************************************************
// Chequea si el codigo ascci de un caracter del alfabeto o no
// ********************************************************************************************
function jCaracterAlfabetico(sAsciiCode) {
	var bAlfabetico = false;
	switch (sAsciiCode){
		case oAscii.ESPACIO:
		case oAscii.Aacute:
		case oAscii.Eacute:
		case oAscii.Iacute:
		case oAscii.Ntilde:
		case oAscii.Oacute:
		case oAscii.Uacute:
		case oAscii.aacute:
		case oAscii.eacute:
		case oAscii.iacute:
		case oAscii.ntilde:
		case oAscii.oacute:
		case oAscii.uacute:
            //inclusiones
		    bAlfabetico = true;
			break;
		default:
	        //general
	        var bLetraMayuscula = ((sAsciiCode >= oAscii.A) && (sAsciiCode <= oAscii.Z))? true : false;
	        var bLetraMinuscula = ((sAsciiCode >= oAscii.a) && (sAsciiCode <= oAscii.z))? true : false;
			bAlfabetico = (bLetraMayuscula || bLetraMinuscula)? true : false;
			break;
	}

    return bAlfabetico;
}

// ********************************************************************************************
// Chequea si el codigo ascci de un caracter corresponde a un caracter numerico o no
// ********************************************************************************************
function jCaracterNumerico(sAsciiCode) {
    return ((sAsciiCode > 47) && (sAsciiCode < 58))? true : false;
}

// ********************************************************************************************
// Chequea si el codigo ascci de un caracter es imprimible y valido para aceptarse
// ********************************************************************************************
function jCaracterValido(sAsciiCode) {
	var bValido = true;
	switch (sAsciiCode){
		case oAscii.COMA:
		case oAscii.COMILLAS:
		case oAscii.CREMILLAS:
		case oAscii.DOSPUNTOS:
		case oAscii.PUNTOCOMA:
		case oAscii.INTERROG:
            //exclusiones
		    bValido = false;
			break;
	}

    return bValido;
}

function abre_ventana(_sRuta, _aData, _sNomVentana, _aPropiedades){
var aPropiedades = {
	    toolbar: 'no', 
	    status: 'yes', 
	    menubar: 'no', 
	    resizable: 'no', 
	    scrollbars: 'no', 
	    width: 800, 
	    height: 600, 
	    top: 0,
	    left: 0
    };
    for(propiedad in _aPropiedades){
	    aPropiedades[propiedad] = _aPropiedades[propiedad];
    }
    aPropiedades["top"] = (screen.height - aPropiedades["height"]) / 2;
    aPropiedades["left"] = (screen.width - aPropiedades["width"]) / 2;

    var sData = array2String(_aData, "&");
    var sPropiedades = array2String(aPropiedades, ", ");
    window.open(_sRuta+"?"+sData, _sNomVentana, sPropiedades);
}

array2String = function(_data, _separador){
    var sData = "";
    for(dato in _data) {
    	sData += (($.trim(sData)!="")?_separador:"")+dato+"="+_data[dato];
    }
    return sData;
};

array2Input = function(_data){
    var sData = "";
    for(dato in _data) {
    	sData += "<input type='hidden' name='"+dato+"' value='"+_data[dato]+"'>";
    }
    return sData;	
};

string2Input = function(_data){
    var sData = "";
    var aParametros = _data.split('&');    
    for(var i = 0; i < aParametros.length; i++){
    	var aParametro = aParametros[i].split('=');
    	sData += "<input type='hidden' name='"+aParametro[0]+"' value='"+aParametro[1]+"'>";
    }    
    return sData;
};

cargaContenido = function(_tipo, _destino, _data){
	capaLoading("show");
	var sDataParam = "";
	switch (_tipo) {
	    case 's': //parametros tipo string
	    	sDataParam = string2Input(_data);
	        break;
	    case 'a': //parametros tipo array
	    	sDataParam = array2Input(_data);
	        break;
	}
    $("BODY").append(
            '<form id="formContenido" action="'+_destino+'" target="_self" method="post">'+
            sDataParam+
            '</form>');
    $("#formContenido").submit();
};

cargaContenidoTo = function(_tipo, _destino, _data, _to){
	capaLoading("show");
	var sDataParam = "";
	switch (_tipo) {
	    case 's': //parametros tipo string
	    	sDataParam = string2Input(_data);
	        break;
	    case 'a': //parametros tipo array
	    	sDataParam = array2Input(_data);
	        break;
	}
    $("BODY").append(
            '<form id="formContenido" action="'+_destino+'" target="'+_to+'" method="post">'+
            sDataParam+
            '</form>');
    $("#formContenido").submit();
};

ejecutaCallBack = function(xml){
	//confirma si hay definicion de script en el xml
    if ($(xml).find('scripts').length > 0){
        $(xml).find('scripts').each(function(){
            $(this).find('script').each(function(){
                var sScript = '';
                var nombre = $(this).find('nombre_scr').text();
                sScript = nombre+'(';
    	        if ($(this).find('parametros').length > 0){
                    var iCantParametros = 0;
    	            $(this).find('parametros').each(function(){
    	                $(this).find('parametro').each(function(){
    	                    var tipo = $(this).find('tipo').text();
    	                    switch (tipo) {
    	                        case 'string':
	        	                    var valor = $(this).find('valor').text();
    	                        	sScript += ((iCantParametros > 0)?',':'') + '"' + valor + '"';
    	                            break;
    	                        case 'array':
    	                            var iCantDatos = 0;
    	                        	sScript += ((iCantParametros > 0)?',':'') + '{';
    		        	            $(this).find('datos').each(function(){
    		        	                $(this).find('dato').each(function(){
    		        	                    var nombre = $(this).find('nombre').text();
    		        	                    var valor = $(this).find('valor').text();
	        	                        	sScript += ((iCantDatos > 0)?',':'') + '"' + nombre + '":"' + valor + '"';
    		        	                    iCantDatos++;
    		        	                });
    		        	            });
    	                        	sScript += '}';
    	                            break;
    	                    }
    	                    iCantParametros++;
    	                });
    	            });
    	        }
                sScript += ');';
                eval(sScript);
                sScript = null;
            });
        });
    }
};

getDataFila = function(xml, nomTabla, id){
    var oNombreId = $("#"+nomTabla).getGridParam("xmlReader").id;
	var aData = new Array();

    if ($(xml).find('fila').length > 0){
        $(xml).find('fila').each(function(){
        	var sTmpId = $(this).find(oNombreId).text();
        	if(sTmpId == id){
                $(this).children().each(function(){
	                oTag = $(this);
	                aData[oTag[0].nodeName] = oTag.text();
                });
        	}
        });
    };
    return aData;
};

getXml = function(_urlScript, _data, _callback){
    var oError = null;

    $.ajax({
        async: false,
	    type: "POST",
	    url: _urlScript+"?"+_data,
	    //data: _data,
	    cache: false,
	    dataType: ($.browser.msie) ? "text" : "xml",
	    beforeSend: function(data){ 
            capaLoading("show");
	    },        
	    error: function(requestData, strError, strTipoError){
	    	var sMensajeError = "error carga xml (" + strTipoError + " : " + strError + ")";
	        oError = new Object;
            oError.numero = strError;
            oError.mensaje = sMensajeError;
	    },
	    success: function(dataXml){ 
		    var xml;
		    if (typeof dataXml == "string") {
		        xml = new ActiveXObject("Microsoft.XMLDOM");
		        xml.async = false;
		        xml.loadXML(dataXml);
		    } else {
		        xml = dataXml;
		    }
		   
	        $(xml).find('respuestaXML').each(function(){ 
	            $(this).find('error').each(function(){
			        oError = new Object;
		            oError.numero = (isNaN(parseInt($(this).find('numero').text(), 10)))? "0" : new String(parseInt($(this).find('numero').text(), 10));
		            oError.mensaje = $(this).find('mensaje').text();
	            }); 
	            
				if (!oError){
					ejecutaCallBack(xml);
			    	if(_callback) _callback(xml);
				}
	        }); 
	    },        
        complete: function(xml, resultado){
  	        capaLoading("hide");

			if (oError){
				alert(oError.mensaje);
			}
	    } 
	});
};

getContenido = function(_urlScript, _data, _destino, _callback){
    $.ajax({
        async: false,
	    type: "POST",
	    url: _urlScript+"?"+_data,
	    //data: _data, 
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
  	        $(_destino).html(respuestaHtml);
	    	if(_callback) _callback();
	    } 
	});
}; 

getIFrameDocument = function(_iframe) {
	if (_iframe.contentDocument) {
		// For NS6
		return _iframe.contentDocument; 
	} else if (_iframe.contentWindow) {
		// For IE5.5 and IE6
		return _iframe.contentWindow.document;
	} else if (_iframe.document) {
		// For IE5
		return _iframe.document;
	} else {
		return null;
	}
};

callTransaccion = function(_options){
	var self = this;
	var settings = {
	    form: '',
	    destino: '',
	    data: {},
	    beforeSend: function(){},
		onComplete: function(respuesta) {}
	};

	// Merge the users options with our defaults
	for (var i in _options) {
		settings[i] = _options[i];
	}
	
	var $form = null;
	if ((settings.form != null) && (settings.form != undefined) && ($.trim(settings.form) != "")){
		$form = $("#"+settings.form);
	} else {
		alert("llamada inv&aacute;lida(data)");
		return false;
    }
	if ((settings.destino == null) || (settings.destino == undefined) || ($.trim(settings.destino) == "")){
		alert("llamada inv&aacute;lida(destino)");
		return false;
    }

	var $oTipoRespuesta = $('<input type="hidden" id="tpoRespuesta" name="tpoRespuesta" value="xml"/>');
	$oTipoRespuesta.appendTo($form);

    var $input = null;
	var _data = settings.data;
    for (var dato in _data) {
    	if ( $("#"+dato, $form).length > 0 ) 
    		$("#"+dato, $form).remove();
    	$input = $("<input type='hidden' id='"+dato+"' name='"+dato+"' value='"+_data[dato]+"'>");
    	$input.appendTo($form);
    }
	
	var nomIframe = "ifrmtx";
	var $iframe = $('<iframe src="javascript:false;" id="'+nomIframe+'" name="'+nomIframe+'" />');
	$iframe.appendTo('body');

	settings.beforeSend.call(self);
	$form.attr({
		action: settings.destino,
		target: nomIframe,
		method: 'post'
	});
	$form.submit();
	
	var toDeleteFlag = false;
	$iframe.bind('load', function(e){
		if (// For Safari
			$iframe.attr("src") == "javascript:'%3Chtml%3E%3C/html%3E';" ||
			// For FF, IE
			$iframe.attr("src") == "javascript:'<html></html>';"){						
			
			// First time around, do not delete.
			if( toDeleteFlag ){
			    // Fix busy state in FF3
			    setTimeout( function() {
				    $iframe.remove();
			    }, 0);
			}
			return;
		}				
		
		var doc = getIFrameDocument($iframe[0]); //iframe.contentDocument ? iframe.contentDocument : frames[iframe.id].document;

		// fixing Opera 9.26
		if (doc.readyState && doc.readyState != 'complete'){
			// Opera fires load event multiple times
			// Even when the DOM is not ready yet
			// this fix should not affect other browsers
			return;
		}
		
		// fixing Opera 9.64
		if (doc.body && doc.body.innerHTML == "false"){
			// In Opera 9.64 event was fired second time
			// when body.innerHTML changed from false 
			// to server response approx. after 1 sec
			return;				
		}
		
		var respuesta;
							
		if (doc.XMLDocument){
			// response is a xml document IE property
			respuesta = doc.XMLDocument;
		} else if (doc.body){
			// response is html document or plain text
			respuesta = doc.body.innerHTML;
		} else {
			// response is a xml document
			respuesta = doc;
		}
																	
		settings.onComplete.call(self, respuesta);
		
		// Reload blank page, so that reloading main page
		// does not re-submit the post. Also, remember to
		// delete the frame
		toDeleteFlag = true;
				
		// Fix IE mixed content issue
		$iframe[0].src = "javascript:'<html></html>';";		 								
	});
	$oTipoRespuesta.remove();
    for (var dato in _data) {
        $("#"+dato, $form).remove();
    }
};

getValor = function(sNomObjeto){
	return $(sNomObjeto).attr("valor");
};

//******************************************************************************
//Valida Error Devuelto en un XML para saber si debe redireccionar al usuario
//******************************************************************************
function verificarErrorXML(error){
var numero = $(error).find('numero').text();
var mensaje = $(error).find('mensaje').text();
	
var bEncontroError = false;
    if (parseInt(numero, 10) != 0 && parseInt(numero, 10) != 87101){
        alert("Error: " + mensaje);
        bEncontroError = true;
	} else {
	    if(parseInt(numero, 10) == 87101){
			alert("Error: " + mensaje);
	    }
	}
	return bEncontroError;
}

function ShowHide(nomObjeto){
	var objetos = null;

	if ( typeof nomObjeto === "string" ){
		objetos = {};
		objetos[0] = nomObjeto;
	} else {
		objetos = nomObjeto;
	}

	for ( var objeto in objetos ){
		if ($("#"+objetos[objeto]).is(":hidden")) {
	        $("#"+objetos[objeto]).slideDown('slow');
	        $("#"+objetos[objeto]+" #grupo_datos div.valor_datos div").first().click();
	    } else {
	        $("#"+objetos[objeto]).slideUp('fast');
	    }
	}
}

function ShowOnlyOne(nomObjeto){
/*    $("div.contenido_seccion:visible").slideUp('fast');
	$("#"+nomObjeto).slideDown('slow');*/
	$("div.contenido_seccion:visible").hide();
	$("#"+nomObjeto).show();
}

function GestionaSubsecciones(oLink, sKSI){
    var $this = $(oLink);
    
    if (!$this.is('.activo')){
        var oActivo = $this.parent().parent().find('A.activo');
        var sObjDestino = ($this.attr("secrel") == undefined)? "" : $this.attr("secrel");
        var bRecarga = ($this.attr("recarga") == undefined)? true : (($this.attr("recarga") == "S")? true : false);
        var sSvtDestino = ($this.attr("svt") == undefined)? "" : $this.attr("svt");
        var sAccion = ($this.attr("accion") == undefined)? "" : $this.attr("accion");
        var idActivo = ($this.attr("idActivo") == undefined)? "" : $this.attr("idActivo");
        var descriptor = ($this.attr("descriptor") == undefined)? "" : $this.attr("descriptor");

        if ($.trim(sObjDestino) == ""){
            alert("seccion con información incompleta");
            return;
        }

        var bExisteObjDestino = ($('#'+sObjDestino, $('#contenido')).length > 0)? true : false;
        
        if (bRecarga){
            if ($.trim(sSvtDestino) == ""){
                alert("seccion sin destino definido");
    	        return;
    	    }

            if ($.trim(sAccion) == ""){
                alert("accion no definido");
    		    return;
    		}
            
            if ($.trim(idActivo) == ""){
                alert("id activo no definido");
    		    return;
    		}
            
            if ($.trim(descriptor) == ""){
                alert("descriptor no definido");
    		    return;
    		}
            
            if (!bExisteObjDestino)
                $('#contenido').append('<div id="'+sObjDestino+'" class="contenido_seccion"></div>');

            var sData = 'KSI='+sKSI;
			sData += '&accion='+sAccion;
			sData += '&idActivo='+idActivo;
			sData += '&descriptor='+descriptor;
			getContenido(sSvtDestino, sData, "#"+sObjDestino, function(){
				ShowOnlyOne(sObjDestino);
			});
        } else {
            ShowOnlyOne(sObjDestino);
        }
        
        $(oActivo).removeClass("activo");
        $this.addClass("activo");
    }
}

//******************************************************************************
//Borra Todo el Contenido del Menu Lateral
//******************************************************************************
function  borrarMenuLateral()
{
	$("#opcmenu_0").empty();
	$("#opcmenu_1").empty();
	$("#opcmenu_2").empty();
	$("#opcmenu_3").empty();
	$("#opcmenu_4").empty();
	$("#opcmenu_5").empty();
	$("#opcmenu_6").empty();
	$("#opcmenu_7").empty();
	$("#opcmenu_8").empty();
	$("#opcmenu_9").empty();
}

//******************************************************************************
//Oculta Botones Superiores
//******************************************************************************
function ocultar_BtnSuperiores()
{
	$("#opciones_encabezado div.opcion").each(function(){
		$("#opciones_encabezado div.opcion").css("display","none");
		$("#opciones_encabezado div.opcion").css("cursor","default");
	});
}

//******************************************************************************
//Muestra Botones Superiores
//******************************************************************************
function habilitar_BtnSuperiores(arrayIdBotones, KSI){

	for(var i=0; i<arrayIdBotones.length;i++)
	{
		var img = $(arrayIdBotones[i]).find("img");
	    $(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgMenuOn"));
		$(arrayIdBotones[i]).css("display","block");
		$(arrayIdBotones[i]).css("cursor","pointer");

		$(arrayIdBotones[i] + ' img').tipsy({
			title:'tooltip'
		});
		$(arrayIdBotones[i]).click(function() {
			var img = $(this).find("img");
		    $(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgMenuPres"));
		    $(this).unbind("mouseover");
			$(this).unbind("mouseout");
			var sData = "accion="+$(this).attr("accion");
			sData += "&KSI=" + KSI;
			cargaContenido("s", "SvtFunciones", sData);		
			desabilita_BtnSuperioresPresionados(arrayIdBotones, this);
		});
	}
}

//******************************************************************************
//Desabilita Los Botones Presionados
//******************************************************************************
function desabilita_BtnSuperioresPresionados(arrayIdBotones, BtnSeleccionado){
	//se muestran los botones de la lista superior
	var img;
	for(var i=0; i<arrayIdBotones.length;i++)
	{
		img = $(arrayIdBotones[i]).find("img");
	    $(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgMenuOn"));
	}
	img = $(BtnSeleccionado).find("img");
    $(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgMenuPres"));
}
//******************************************************************************
//Desabilita Los Botones
//******************************************************************************
function desabilita_BtnSuperiores(arrayIdBotones){
	//se muestran los botones de la lista superior
	for(var i=0; i<arrayIdBotones.length;i++)
	{
		$(arrayIdBotones[i]).css("display","block");
		$(arrayIdBotones[i]).css("cursor","default");
		$(arrayIdBotones[i]).unbind("mouseover");
		$(arrayIdBotones[i]).unbind("mouseout");
	}
}
//******************************************************************************
//Habilita Boton Busqueda
//******************************************************************************
function habilitar_BtnBusqueda(KSI, btnSelecionado){
	
	//se muestran los botones de la lista superior		
	$("#btn_Busqueda").css("display","block");
	$("#btn_Busqueda").css("cursor","pointer");
	
	var img = $("#btn_Busqueda").find("img");
    $(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgMenuOn"));
    $("#btn_Busqueda").unbind("mouseover");
	$("#btn_Busqueda").unbind("mouseout");
	$("#btn_Busqueda").click(function() {
		var sData = "accion="+$(this).attr("accion")+btnSelecionado;
		sData += "&KSI=" + KSI;
		
		cargaContenido("s", "SvtFunciones", sData);		    
	});
}
		
//******************************************************************************
//Seleciona Boton
//******************************************************************************
function selecciona_boton(idBoton){
	var img = $(idBoton).find("img");
	$(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgMenuPres"));
	$(idBoton).unbind("mouseover");
	$(idBoton).unbind("mouseout");
}

//******************************************************************************
//muestra menu problema salud 1
//******************************************************************************
function muestraMenuProblemaSalud1(){
	borrarMenuLateral();
	var texto = '<a href="#" class="activo" secrel="opcproblemaSalud" recarga="N"><br>Problema de Salud</a></li>';
	$("#opcmenu_0").append(texto);
}

//******************************************************************************
//muestra menu problema salud 2
//******************************************************************************
function muestraMenuProblemaSalud2(ksi, idActivo, descriptor){
	borrarMenuLateral();
	var texto = '<a href="#" class="activo" secrel="opcproblemaSalud" recarga="N" idActivo="'+idActivo+'" descriptor="'+descriptor+'" ><br>Problema GES</a></li>';
	$("#opcmenu_0").append(texto);
	texto = '<a href="#" secrel="opcpatologias" svt="SvtFunciones" accion="menuCargaPatologias" idActivo="'+idActivo+'" descriptor="'+descriptor+'"><br>Patolog&iacute;as</a></li>';
	$("#opcmenu_1").append(texto);
	texto = '<a href="#" secrel="opcfactoresinclusion" svt="SvtFunciones" accion="menuCargaFactoresInclusion" idActivo="'+idActivo+'" descriptor="'+descriptor+'"><br>Factores de Inclusi&oacute;n</a></li>';
	$("#opcmenu_2").append(texto);
	texto = '<a href="#" secrel="opcetapas" svt="SvtFunciones" accion="menuCargaEtapas" idActivo="'+idActivo+'" descriptor="'+descriptor+'"><br>Etapas</a></li>';
	$("#opcmenu_3").append(texto);

	$('#subsecciones li A').click(function(){
		GestionaSubsecciones(this, ksi);
	});
}

//******************************************************************************
//muestra menu asignacion
//******************************************************************************
function muestraMenuAsignacion1(){
	borrarMenuLateral();
	var texto = '<a href="#" class="activo" secrel="opcAsignaciones" recarga="N"><br>Asignaciones</a></li>';
	$("#opcmenu_0").append(texto);
}

function muestraMenuAsignacion(ksi, idActivo, descriptor){
	borrarMenuLateral();
	var texto = '<a href="#" class="activo" secrel="opcAsignaciones" recarga="N" idActivo="'+idActivo+'" descriptor="'+descriptor+'"><br>Detalle</a></li>';
	$("#opcmenu_0").append(texto);
	texto = '<a href="#" secrel="opcAdjuntos" svt="SvtFunciones" accion="menuCargaAdjuntosAsignacion" idActivo="'+idActivo+'" descriptor="'+descriptor+'"><br>Adjuntos</a></li>';
	$("#opcmenu_1").append(texto);
	texto = '<a href="#" secrel="opcAsignacion" svt="SvtFunciones" accion="menuCargaAsignacion" idActivo="'+idActivo+'" descriptor="'+descriptor+'"><br>Asignaci&oacute;n</a></li>';
	$("#opcmenu_2").append(texto);
	
	$('#subsecciones li A').click(function(){
		GestionaSubsecciones(this, ksi);
	});
}

function muestraMenuDetalleCasosGES1(){
	borrarMenuLateral();
	var texto = '<a href="#" class="activo" secrel="opcConsultas" recarga="N"><br>Consultas</a></li>';
	$("#opcmenu_0").append(texto);
}

function muestraMenuDetalleCasosGES(ksi, idActivo, descriptor){
	borrarMenuLateral();
	var texto = '<a href="#" class="activo" secrel="opcConsultas" recarga="N" idActivo="'+idActivo+'" descriptor="'+descriptor+'"><br>Detalle</a></li>';
	$("#opcmenu_0").append(texto);
	texto = '<a href="#" secrel="opcAdjuntos" svt="SvtFunciones" accion="menuCargaAdjuntosConsultas" idActivo="'+idActivo+'" descriptor="'+descriptor+'"><br>Adjuntos</a></li>';
	$("#opcmenu_1").append(texto);
	
	$('#subsecciones li A').click(function(){
		GestionaSubsecciones(this, ksi);
	});
}

function muestraMenuAdjunto()
{
	borrarMenuLateral();
	var texto = '<a href="#" class="activo" secrel="opcAdjuntos" recarga="N"><br>Adjunto</a></li>';
	$("#opcmenu_0").append(texto);
}

//******************************************************************************
//Obtiene el id activo de la grilla
//******************************************************************************
function obtieneIdActivo(sDataCarga)
{
	var idActivo = sDataCarga.split("&idActivo=");
	return idActivo[1];	
}

function obtienedescriptor(sDataCarga)
{
	var descriptor = sDataCarga.split("&descriptor=");
	return descriptor[1];
}
//******************************************************************************
//Muestra botones en la pagina
//******************************************************************************
function muestraBotones(arrayIdBotones)
{
	for(var i=0; i<arrayIdBotones.length;i++)
	{
		$(arrayIdBotones[i]).css("display","block");
		$(arrayIdBotones[i]).css("cursor","pointer");
	}
}
//******************************************************************************
//Convierte Fecha dd/mm/yyyy a yyyymmddhhmmss
//******************************************************************************
function desformateaFecha(oInput){
	var sDia = "";
	var sMes = "";
	var sAno = "";
    var sFecha = $(oInput).val();
    var sFechaHora = "";
	var fecha = new Date();
	var hora = fecha.getHours();
	var minutos = fecha.getMinutes();
    
	if ($.trim(sFecha) != "")
	{
		if (jvalidaFecha(sFecha))
		{
			sDia = sFecha.substr(0,2);
			sMes = sFecha.substr(3,2);
			sAno = sFecha.substr(6,4);
		}
	}
	else
	{
		return sFecha;
	}
	
	sFecha = sAno+sMes+sDia;

	if(hora < 10)
		hora = "0"+hora;
	
	if(minutos < 10)
		minutos = "0"+minutos;
	
	sFechaHora = sFecha+hora+minutos+"00";
	
	return sFechaHora;
}

function desformateaFechaSinHora(oInput){
	var sDia = "";
	var sMes = "";
	var sAno = "";
	var sFecha = $(oInput).val();
  
	if ($.trim(sFecha) != "")
	{
		if (jvalidaFecha(sFecha))
		{
			sDia = sFecha.substr(0,2);
			sMes = sFecha.substr(3,2);
			sAno = sFecha.substr(6,4);
		}
	}
	else
	{
		return sFecha;
	}
	
	sFecha = sAno+sMes+sDia;

	return sFecha;
}

getDimensions = function(oElement) {
    var x, y, w, h;
    x = y = w = h = 0;
    if (document.getBoxObjectFor) { // Mozilla
      var oBox = document.getBoxObjectFor(oElement);
      x = oBox.x-1;
      w = oBox.width;
      y = oBox.y-1;
      h = oBox.height;
    }
    else if (oElement.getBoundingClientRect) { // IE
      var oRect = oElement.getBoundingClientRect();
      x = oRect.left-2;
      w = oElement.clientWidth;
      y = oRect.top-2;
      h = oElement.clientHeight;
    }
    return {x: x, y: y, w: w, h: h};
  };
//******************************************************************************
//Desabilita Los Botones Inferiores Presionados
//******************************************************************************
function desabilita_BtnInferioresPresionados(arrayIdBotones, BtnSeleccionado){
	//se muestran los botones de la lista superior
	var img;
	for(var i=0; i<arrayIdBotones.length;i++)
	{
		img = $(arrayIdBotones[i]).find("img");
	    $(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgon"));
	}
	img = $(BtnSeleccionado).find("img");
	$(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgoff"));
}

function selecciona_botoninf(idBoton){
	var img = $(idBoton).find("img");
	$(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgon"));
	$(idBoton).unbind("mouseover");
	$(idBoton).unbind("mouseout");
}
//******************************************************************************
//Habilita Botones Inferiores
//******************************************************************************
function habilitar_BtnInferiores(arrayIdBotones){

	muestraBotones();
	deshabilitaBotones();

	for(var i=0; i<arrayIdBotones.length;i++)
	{
		$(arrayIdBotones[i]).attr("habilitado","S");
		var img = $(arrayIdBotones[i]).find("img");
	    $(img).attr("src",sRutaImagenes+"botones/"+$(img).attr("imgon"));
		$(arrayIdBotones[i]).css("cursor","pointer");
		$(img).css("cursor","pointer");
		$(img).tipsy({title:'tooltip',delayIn: 30000000,delayOut:500});
		//$('#example-delay').tipsy({delayIn: 500, delayOut: 1000});
	}
}

function deshabilitaBotones()
{
	$("#opciones_grupo img").each(function(){ 
		$(this).attr("src",sRutaImagenes+"botones/"+$(this).attr("imgOff"));
		$(this).unbind("click");
		$(this).unbind("mouseover");
		$(this).unbind("mouseout");
		$(this).css("cursor","default");
		$(this).unbind("mouseenter");
		$(this).unbind("mouseleave");
		$(".tipsy").remove();
	});
	$("#opciones_grupo .opcion").each(function(){ 
		$(this).attr("habilitado","N");
	});
}

function muestraBotones()
{
	$("#opciones_grupo img").each(function(){ 
		$(this).css("display","block");
	});
}

function ocultaBotones()
{
	$("#opciones_grupo img").each(function(){ 
		$(this).css("display","none");
	});
}

function devuelveHora(n)
{
    var fecha = new Date();
	var hora = fecha.getHours();
	var min = fecha.getMinutes();

	if(hora < 10)
		hora = "0" + hora; 
	if(min < 10)
		min = "0" + min; 
    if(n%2==0)
        return "<div id='horas' style='float:left' >"+ hora +"</div>" + "<div id='dosPuntos' style='float:left'>&nbsp;</div>" + "<div id='minutos' style='float:left'>"+ min +"</div>";
    else 
		return "<div id='horas' style='float:left' >"+ hora +"</div>" + "<div id='dosPuntos' style='float:left'>:</div>" + "<div id='minutos' style='float:left'>"+ min +"</div>";
}

function jrun_desformatea(sValorRun){
	sValorRun = jreemplazaCaracter(sValorRun, oSeparadores.chrMilesMsk, "");
	sValorRun = jreemplazaCaracter(sValorRun, "-", "");
	return sValorRun;
}