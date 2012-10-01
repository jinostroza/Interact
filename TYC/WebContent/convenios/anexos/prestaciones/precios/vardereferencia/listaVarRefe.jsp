<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoVarRefeData = null;
	var idVarRefer = 0; 

	$(function() {
		
		$("#listadoVarRefe").jqGrid(
		{
			url : getUrlBuscaVarPrest(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', 'Descripci&oacute;n Variable', 'Variable'],
			colModel : [ 
						{name : 'idDescVarRef', index:'id', hidden : true}, 
			 			{name : 'descVarRef', index : 'descVar', width : 210, resizable : false, sortable : true}, 
						{name : 'varRef', index:'var', width: 130, resizable: false, sortable: true}
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
//			pager: '#pieVarPrest',
//			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idVarRefer = rowId; 
			},
			loadComplete : function(data){
				/*var udata = $("#listadoVarRefe").getGridParam("userData");

				if ($("#listadoVarRefe").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoVarRefeData = null;
					}else {
						listadoVarRefeData = data;
					}
				}*/
			},
			gridComplete: function(){
				var ids = $("#listadoVarRefe").getDataIDs();

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
	
	function getUrlBuscaVarPrest(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarVariablesDeReferencia';
		return sData;
	}   
	  
</script>
 
<div id="listadoBusquedaVarDeReferencia" style="height: 216px;width: 373px;margin-left: 8px;margin-top: 18px;">
	<table id="listadoVarRefe"></table> 
</div> 