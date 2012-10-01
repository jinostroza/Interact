<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoValPosFecData = null;
	var idValPosFec = 0; 

	$(function() {
		
		$("#listadoValPosFec").jqGrid(
		{
			url : getUrlBuscaValPosFec(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', 'Valores Posibles', 'Codigo'],
			colModel : [ 
						{name : 'idValPos', index:'id', hidden : true}, 
			 			{name : 'valPos', index : 'valPos', width : 180, resizable : false, sortable : true}, 
						{name : 'codigo', index:'codigo', width: 103, resizable: false, sortable: true}
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idValPos',
				userdata : "solemdata"
			},
			width : 310,
			height : 186,
			scrollOffset : 0,
			rowNum : 8,
			hoverrows : true,
//			loadtext : '',
//			emptyrecords : '',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
//			pager: '#pieValPosFec',
//			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idValPosFec = rowId;
				//console.log("click en fila: "+idValPosFec);
			},
			loadComplete : function(data){
				/*var udata = $("#listadoValPosFec").getGridParam("userData");

				if ($("#listadoValPosFec").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoValPosFecData = null;
					}else {
						listadoValPosFecData = data;
					}
				}*/
			},
			gridComplete: function(){
				var ids = $("#listadoValPosFec").getDataIDs();

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
	
	function getUrlBuscaValPosFec(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarValPosFec';
		return sData;
	} 
	
</script>
 
<div id="listadoBusquedaValPosFec" style="height: 216px;width: 373px;margin-left: 8px;margin-top: 18px;">
	<table id="listadoValPosFec"></table> 
</div> 