<%@ page language="java"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoVarCondData = null;
	var idVarCond = 0; 

	$(function() {
		
		$("#listadoVarCond").jqGrid(
		{
			url : getUrlBuscaVarCond(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '${solemfmt:getLabel("label.VariablesCondicionales",codIdioma)}', 'Codigo'],
			colModel : [ 
						{name : 'idVarCond', index:'id', hidden : true}, 
			 			{name : 'varCond', index : 'varCond', width : 210, resizable : false, sortable : true}, 
						{name : 'codigo', index:'codigo', width: 130, resizable: false, sortable: true}
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
//			pager: '#pieVarCond',
//			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idVarCond = rowId; 
				opcVistaGrilla(idVarCond);
			},
			loadComplete : function(data){
				/*var udata = $("#listadoVarCond").getGridParam("userData");

				if ($("#listadoVarCond").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoVarCondData = null;
					}else {
						listadoVarCondData = data;
					}
				}*/
			},
			gridComplete: function(){
				var ids = $("#listadoVarCond").getDataIDs();

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
	
	function getUrlBuscaVarCond(){
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarVariablesCondicionales';
		return sData;
	}  

	function opcVistaGrilla(idVarCond){
		if(idVarCond==1 || idVarCond==2){
			$("#gridValoresTipoAdmision").show();
			$("#botonesOpcionales").show(); 
			$("#gridValoresUniOrg").hide();
			$("#gridValoresPosiblesFecha").hide(); 
			$("#opcValorHora").hide(); 
			$("#opcValorInput").hide();
		}	 
		if(idVarCond==2){
			$("#gridValoresTipoAdmision").hide(); 
			$("#gridValoresUniOrg").show();
			$("#botonesOpcionales").show(); 
			$("#gridValoresPosiblesFecha").hide(); 
			$("#opcValorHora").hide(); 
			$("#opcValorInput").hide();
		}else if(idVarCond==3){
			$("#gridValoresTipoAdmision").hide(); 
			$("#gridValoresUniOrg").hide();
			$("#botonesVerOpc").hide(); 
			$("#opcValorHora").hide();
			$("#gridValoresPosiblesFecha").show(); 
			$("#opcValorInput").hide();  
		}else if(idVarCond==4){
			$("#gridValoresTipoAdmision").hide();
			$("#gridValoresUniOrg").hide();
			$("#botonesVerOpc").show();
			$("#opcValorHora").show();
			$("#opcValorInput").hide(); 
			$("#gridValoresPosiblesFecha").hide();  
		}else if(idVarCond==5 || idVarCond==6){
			$("#gridValoresTipoAdmision").hide();
			$("#gridValoresUniOrg").hide();
			$("#botonesVerOpc").show();
			$("#opcValorHora").hide();
			$("#opcValorInput").show();  
			$("#gridValoresPosiblesFecha").hide();  
		}
	}
	  
</script>
 
<div id="listadoBusquedaVarCond" style="height: 216px;width: 373px;margin-left: 8px;margin-top: 18px;">
	<table id="listadoVarCond"></table> 
</div> 