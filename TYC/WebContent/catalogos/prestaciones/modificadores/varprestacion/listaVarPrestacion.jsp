<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoVarPrestacionData = null;
	var idVarPrestacion = 0; 

	$(function() {
		
		$("#listadoVarPrestacion").jqGrid(
		{
			url : getUrlBuscaVarPrestacion(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', 'Descripci&oacute;n Variables', 'Variables'],
			colModel : [ 
						{name : 'idVarPrest', index:'id', hidden : true}, 
			 			{name : 'desVarPres', index : 'desVarPres', width : 210, resizable : false, sortable : true}, 
						{name : 'varPres', index:'varPres', width: 130, resizable: false, sortable: true}
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idvarCond',
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
//			pager: '#pieVarPrestacion',
//			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idVarPrestacion = rowId;  
			},
			loadComplete : function(data){
				/*var udata = $("#listadoVarPrestacion").getGridParam("userData");

				if ($("#listadoVarPrestacion").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoVarPrestacionData = null;
					}else {
						listadoVarPrestacionData = data;
					}
				}*/
			},
			gridComplete: function(){
				var ids = $("#listadoVarPrestacion").getDataIDs();

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
	
	function getUrlBuscaVarPrestacion(){ 
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarVariablesPrestacion';
		return sData;
	}   
	  
</script>
 
<div id="listadoBusquedaVarPrestacion" style="height: 216px;width: 373px;margin-left: 8px;margin-top: 18px;">
	<table id="listadoVarPrestacion"></table> 
</div> 