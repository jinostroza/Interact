<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoValoresTipoAdmisionData = null;
	var idValoresTipoAdmision = 0; 

	$(function() {
		
		$("#listadoValoresTipoAdmision").jqGrid(
		{
			url : getUrlBuscaValoresTipoAdmision(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', 'Valores Posibles', 'Codigo'],
			colModel : [ 
						{name : 'idValTA', index:'id', hidden : true}, 
			 			{name : 'ValTA', index : 'ValTA', width : 180, resizable : false, sortable : true}, 
						{name : 'codValTA', index:'codValTA', width: 103, resizable: false, sortable: true}
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
			rowNum : 6,
			hoverrows : true,
//			loadtext : '',
//			emptyrecords : '',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
//			pager: '#pieValoresTipoAdmision',
//			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idValoresTipoAdmision = rowId; 
				//console.log("click en fila: "+idValoresTipoAdmision);
			},
			loadComplete : function(data){
				/* var udata = $("#listadoValoresTipoAdmision").getGridParam("userData");

				if ($("#listadoValoresTipoAdmision").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoValoresTipoAdmisionData = null;
					}else {
						listadoValoresTipoAdmisionData = data;
					}
				} */
			}, 
			gridComplete: function(){
				var ids = $("#listadoValoresTipoAdmision").getDataIDs();

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
	
	function getUrlBuscaValoresTipoAdmision(){
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarValoresTipoAdmision';
		return sData;
	} 
	
</script>
 
<div id="listadoBusquedaValoresTipoAdmision" style="height: 216px;width: 373px;margin-left: 8px;margin-top: 18px;">
	<table id="listadoValoresTipoAdmision"></table> 
</div> 