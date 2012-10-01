<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoVarGlobalesData = null;
	var idVarGlobales = 0; 

	$(function() {
		
		$("#listadoVarGlobales").jqGrid(
		{
			url : getUrlBuscaVarGlobales(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', 'Descripcion Variables', 'Variables'],
			colModel : [ 
						{name : 'idVarGlobales', index:'id', hidden : true}, 
			 			{name : 'desvarGlob', index : 'desvarGlob', width : 210, resizable : false, sortable : true}, 
						{name : 'varGlob', index:'varGlob', width: 130, resizable: false, sortable: true}
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idvarGlobales',
				userdata : "solemdata"
			},
			width : 370,
			height : 186,
			scrollOffset : 0,
			rowNum : 6,
			hoverrows : true,
//			loadtext : '',
//			emptyrecords : '',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
//			pager: '#pieVarGlobales',
//			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idVarGlobales = rowId;  
			},
			loadComplete : function(data){
				/*var udata = $("#listadoVarGlobales").getGridParam("userData");

				if ($("#listadoVarGlobales").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoVarGlobalesData = null;
					}else {
						listadoVarGlobalesData = data;
					}
				}*/
			},
			gridComplete: function(){
				var ids = $("#listadoVarGlobales").getDataIDs();

				for ( var i = 0; i < ids.length; i++){
					var idFila = ids[i]; 
				}
			},
			multiselect : false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});  
	});
	
	function getUrlBuscaVarGlobales(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarVariablesGlobales';
		return sData;
	}   
	  
</script>
 
<div id="listadoBusquedaVarGlobales" style="height: 216px;width: 373px;margin-left: 8px;margin-top: 18px;">
	<table id="listadoVarGlobales"></table> 
</div> 