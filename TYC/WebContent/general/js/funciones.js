/**************************************************/
/*** REPOSITORIO DE FUNCIONES JAVASCRIPT/JQUERY ***/
/*** AUTOR				:	JIU				   	***/
/*** FECHA CREACIÓN		:	JULIO 2012		   	***/
/*** FECHA ACTUALIZACIÓN: 	JULIO 2012		   	***/
/**************************************************/

//******************************************************************************
//Valida que el campo sea solo numerico
//******************************************************************************
function soloNumeros(evt)
{
    var retorno = true;
    var charCode = (evt.which) ? evt.which : event.keyCode;

    if (charCode > 31 && (charCode < 48 || charCode > 57))
		retorno = false;

    return retorno;
} 
//******************************************************************************
//	Valida que el campo sea solo numerico al perder el foco permitiendo la coma
//******************************************************************************
function soloNumerosCommaOnBlur(oInput)
{
	var retorno = true;
	var valorNumerico = $.trim($('#'+oInput).val()); 
	
	if (!/^([0-9,])*$/.test(valorNumerico)){
		$('#'+oInput).val(''); 
		retorno = false;
	}else if(valorNumerico!=""){
		var letra = ",";
		var texto1 = valorNumerico; 
		var longValor = valorNumerico.length; 
		var numCar = texto1.length;  
		var veces = 0;  
		var posicion = texto1.indexOf(letra);  
		var posfinal = texto1.lastIndexOf(letra);
		posfinal = numCar-posfinal;  
	
		while(numCar>=posfinal) {
		   posicion = texto1.indexOf(letra);  
		   posicion++;
		   texto1 = texto1.substring(posicion); 
		   numCar -= posicion;  
		   veces++;  
		} 
		
		if((veces>1) || (posicion==1 && veces>=1) || posicion==longValor){
			$('#'+oInput).css("background","#C86464");
			$('#'+oInput).parent().children("img.imgNoOk").css("display","block");
			$('#'+oInput).parent().children("img.imgOk").css("display","none");  
 			retorno = false;
		} 
	}	 
	
	return retorno ;
} 
//******************************************************************************
//Valida que el campo sea solo numerico al perder el foco
//******************************************************************************
function soloNumerosOnBlur(oInput)
{
	var valorNumerico = $('#'+oInput).val();
	
	if (!/^([0-9])*$/.test(valorNumerico)){
		$('#'+oInput).val('');
		alert("Este campo acepta solo n&uacute;meros");
		return false;
	}
} 
//******************************************************************************
//	Devuelve estado del check S o N
//******************************************************************************
function devuelveEstadoCheck(idElemento)
{
	var esMarcado = $(idElemento).attr('checked');
	var indicador;
	
	if (esMarcado)
		indicador = "S";
	else
		indicador = "N";	

	return indicador; 
}
//******************************************************************************
//	Limpia los filtros de busqueda
//******************************************************************************
function limpiarFiltros(idFormFiltros)
{
	$('#'+idFormFiltros+ ' input').each(function(){
		$('#'+idFormFiltros+ ' :input').val("");
	});
}
//******************************************************************************
//	Buscar la lista de entidades dependiendo la lista de filtros
//******************************************************************************
function buscarListaEntidades(nomGrilla, urlBusqueda)
{
	$('#'+nomGrilla).jqGrid('setGridParam', {
		url : urlBusqueda,
		page : 1,
		rowNum : numFilasGrilla,
		autoencode : false,
		mtype : 'POST',
		datatype : 'xml',
	}).trigger("reloadGrid");
	
	return false;
}
//******************************************************************************
//	Agregar una entidad
//******************************************************************************
function agregarEntidad(nomForm, urlAgrega)
{		
	$('#'+nomForm).attr('action', urlAgrega);
	$('#'+nomForm).submit();
}
//******************************************************************************
//	Editar entidad del listado
//******************************************************************************
function editarEntidad(idFila)
{	
	var fila = $('#'+nomGrilla).jqGrid('getRowData',idFila);
	var urlEdicion = 'getUrlCarga'+ nomEntidad+'('+'fila'+');';
	$('#'+nomForm).attr('action', eval(urlEdicion));
	$('#'+nomForm).submit();
}
//******************************************************************************
//	Eliminar entidad del listado
//******************************************************************************
function eliminarEntidad(idFila)
{
	var fila = $('#'+nomGrilla).jqGrid('getRowData',idFila);
	var urlBusqueda = 'getUrlBusca'+nomEntidad+'();';
	var urlEliminacion = 'getUrlElimina'+nomEntidad+'('+'fila'+');';

	jConfirm(avisoConfirmacionEliminacion, tituloConfirmacionAlerta, function(r){
		if (r == true){
			getAjaxCall(eval(urlEliminacion), function(xml){
				buscarListaEntidades(nomGrilla, eval(urlBusqueda));
				alert(avisoExitoEliminacion);
	       	});	
		}
	});
}
//******************************************************************************
//	Accion Ajax con callback
//******************************************************************************
getAjaxCall = function(_urlScript, _callback){
    var oError = null;

    $.ajax({
        async: false,
	    type: "POST",
	    url: _urlScript,//+"?"+_data,
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
//******************************************************************************
//	Valida si existen filas seleccionadas en una grilla
//******************************************************************************
function validarCheckGrid(idGrilla)
{
	var retorno = true;
	var listaId = $('#'+idGrilla).jqGrid('getGridParam', 'selarrrow');
	
	if(listaId.length == 0)
		retorno = false;
	
	return retorno;
}
//******************************************************************************
//	Crea un boton en el titulo de la grilla
//	TODO: Pendiente cambiar el label de los botones a FMT .. mandar labelBoton
//******************************************************************************
function creaBoton(nomEntidad, tipoBoton)
{
	var boton = "";
	var idContenedorGrilla = "listadoBusqueda"+nomEntidad;
	
	if(tipoBoton == 'E')
		boton += "<input class='boton' type='button' id='btnElimina"+nomEntidad+"' value='"+tituloBotonEliminar+"' role='button' aria-disabled='false'>";
	
	if(tipoBoton == 'A')
		boton += "<input class='boton' type='button' id='btnAgrega"+nomEntidad+"' value='"+tituloBotonAgregar+"' role='button' aria-disabled='false'>";
	
	if(tipoBoton == 'F')
	{
		boton += "<input class='boton' type='button' id='btnElimina"+nomEntidad+"' value='"+tituloBotonEliminar+"' role='button' aria-disabled='false'>";
		boton += "<input class='boton' type='button' id='btnAgrega"+nomEntidad+"' value='"+tituloBotonAgregar+"' role='button' aria-disabled='false'>";
	}
	$(boton).appendTo('#'+idContenedorGrilla+' .ui-jqgrid-titlebar');
}
//******************************************************************************
//	Crea grilla JQGrid
//******************************************************************************
function creaGrilla()
{
	var idGrilla = colModel[0].name;
	var idPieGrilla = $('.pie').attr('id');
	var labelGrilla = nomGrilla.split("listado")[1];
	
	$("#"+nomGrilla).jqGrid(
	{
		datatype : 'local',
		colNames: colNames,
		colModel: colModel,
		xmlReader : {
			root : 'filas',
			row : 'fila',
			page : 'respuestaXML>paginaactual',
			total : 'respuestaXML>totalpaginas',
			records : 'respuestaXML>totalregistros',
			repeatitems : false,
			id: idGrilla,
			userdata : "solemdata"		
		},
		caption : tituloGrilla,
		width : anchoGrilla,
		height : altoGrilla,
		rowNum : numFilasGrilla,
		hoverrows : true,
		emptyrecords : 'No se encontraron '+labelGrilla,
		hidegrid : false,
		sortname: idGrilla, 
		sortorder : 'ASC',
		page : 1,
		pager: $('#'+idPieGrilla),
		pgtext : 'Pág: {0} de {1}',
		viewrecords : true,
		pginput : true,
		shrinkToFit : false,
		gridview: true,
		onSelectRow : function(rowId, status) 
		{
			idNumFila = rowId;
		},
		loadComplete : function(data) 
		{			
			var udata = $("#"+nomGrilla).getGridParam("userData");

			if ($("#"+nomGrilla).getGridParam('datatype') != 'local') 
			{
				if (parseInt(udata.errornum, 10) != 0) {
					alert("¡¡¡ATENCION 1!!! \n("+ udata.errornum + ")"+ udata.errormsj);
					listadoData = null;
				} else {
					listadoData = data;
				}
			}
		},
		gridComplete: function()
		{
			var ids = $("#"+nomGrilla).getDataIDs();
			
			for (var i = 0; i < ids.length; i++)
			{
				var idFila = ids[i];

				var btnEliminar="";
				btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarEntidad("+idFila+")'>";
				btnEliminar+= 	"<img title='eliminar' src='general/imagenes/iconos/delete-32.png'>";
				btnEliminar+= "</div>";

				$("#"+nomGrilla).setRowData(ids[i], {act : btnEliminar});
			}
		},
		onPaging: function(pgButton){
			pagUsuario = $("#"+nomGrilla).jqGrid('getGridParam','page');
			totalRegistros = $("#"+nomGrilla).jqGrid('getGridParam','records');
			regPorPagina = $("#"+nomGrilla).jqGrid('getGridParam','rowNum');
			totalPaginas = Math.ceil(totalRegistros/regPorPagina);

			if(totalPaginas<pagUsuario)
			{
				$("#"+nomGrilla).setGridParam({page:totalPaginas});				
			}
		},
		ondblClickRow: function()
		{
			editarEntidad(idNumFila);
		},
		multiselect : false,
		loadError : function(xhr, status, error)
		{
			alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
		}
	});
}