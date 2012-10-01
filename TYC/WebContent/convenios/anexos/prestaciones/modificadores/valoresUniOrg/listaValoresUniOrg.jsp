<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoValoresUniOrgData = null;
	var idValoresUniOrg = 0; 

	$(function() {
		
		$("#listadoValoresUniOrg").jqGrid(
		{
			url : getUrlBuscaValoresUniOrg(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', 'Valores UniOrg', 'Codigo'],
			colModel : [ 
						{name : 'idValUO', index:'id', hidden : true}, 
			 			{name : 'valUO', width : 180, resizable : false, sortable : true}, 
						{name : 'codUO', width: 103, resizable: false, sortable: true}
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idValUO',
				userdata : "solemdata"
			},
			width : 310,
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
//			pager: '#pieValoresUniOrg',
//			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idValoresUniOrg = rowId;
				//console.log("click en fila: "+idValoresUniOrg);
			},
			loadComplete : function(data){
				/*var udata = $("#listadoValoresUniOrg").getGridParam("userData");

				if ($("#listadoValoresUniOrg").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoValoresUniOrgData = null;
					}else {
						listadoValoresUniOrgData = data;
					}
				}*/
			},
			gridComplete: function(){
				var ids = $("#listadoValoresUniOrg").getDataIDs();

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
	
	function getUrlBuscaValoresUniOrg(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarValoresUniOrg';
		sData += '&idConvenio=88';
//		sData += '&idConvenio=${idConvenio}';
		return sData;
	} 
	
</script>
 
<div id="listadoBusquedaValoresUniOrg" style="height: 216px;width: 373px;margin-left: 8px;margin-top: 18px;">
	<table id="listadoValoresUniOrg"></table> 
</div> 