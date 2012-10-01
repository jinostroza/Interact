<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoModificadoresData = null;
	var idModificador = 0; 

	$(function() {
		
		$("#listadoModificadores").jqGrid(
		{
			url : getUrlBuscaModificadores(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '${solemfmt:getLabel("label.Tipo",codIdioma)}', '${solemfmt:getLabel("label.Modificador",codIdioma)}', '${solemfmt:getLabel("label.FechaInicio",codIdioma)}','${solemfmt:getLabel("label.FechaFin",codIdioma)}', '' ],
			colModel : [ 
						{name : 'idModificador', index:'id', hidden : true}, 
			 			{name : 'tipo', index : 'tipo', width : 170, resizable : false, sortable : true}, 
						{name : 'nombre', index:'nombre', width: 400, resizable: false, sortable: true},
						{name : 'fechaIni', index:'fecIni', width: 172, resizable: false, sortable: true},
			 			{name:  'fechaFin', index: 'fecFin', width: 170, resizable: false, sortable: true},
			 			{name : 'act',index:'act', index: 'act', width : 20, resizable : false,	sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idModificador',
				userdata : "solemdata"
			},
			caption: '${solemfmt:getLabel("label.ListadoModificadores",codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 378,
			scrollOffset : scrollOffSet,
			rowNum : 0,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoModificadores",codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronModificadores",codIdioma)}',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
			pager: '#pieModificador',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idModificador = rowId;
			},
			loadComplete : function(data){
				var udata = $("#listadoModificadores").getGridParam("userData");

				if ($("#listadoModificadores").getGridParam('datatype') != 'local')
				{
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoModificadoresData = null;
					} else {
						listadoModificadoresData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoModificadores").getDataIDs();

				for ( var i = 0; i < ids.length; i++) {
					var idFila = ids[i];

					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarModificador("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoModificadores").setRowData(ids[i], {act : btnEliminar});
				}
			},ondblClickRow: function(){
				editarModificador(); 
			},
			multiselect : false,
			loadError : function(xhr, status, error) {
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});
		creaBoton("Modificador", "A");
		
		$('#btnAgregaModificador').click(function(){
			$('#formDetallePrestacionAnexo').attr('action', getUrlAgregaModificador());
			$('#formDetallePrestacionAnexo').submit();
		});
	});

	function getUrlBuscaModificadores(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarModificadores';
		sData += '&idPrestacionAnexo='+${prestacionAnexo.nIdConvDetalle};
		return sData;
	}

	function getUrlAgregaModificador(){
    	var sData = 'SvtConvenios';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=agregarModificador';
      	sData += '&idConvenio=${prestacionAnexo.nIdConvenio}';
      	sData += '&idPrestacionAnexo=${prestacionAnexo.nIdConvDetalle}';
      	sData += '&indRuta=S';
    	return sData;
    }

    function getUrlCargaModificador(){
    	var dataFila = getDataFila(listadoModificadoresData, "listadoModificadores", idModificador);        
    	var sData = 'SvtConvenios';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=cargarModificador';
      	sData += '&idModificador=' + dataFila.idModificador;
      	sData += '&indRuta=S';
    	return sData;
    }

    function getUrlEliminaModificador(){
    	var dataFila = getDataFila(listadoModificadoresData, "listadoModificadores", idModificador);        
    	var sData = 'KSI=${sesion.sesIdSesion}';
      	sData += '&accion=eliminarModificador';
      	sData += '&idModificador=' + dataFila.idModificador;
    	return sData;	
    }

	function editarModificador(){
		var idSeleccionado = $('#listadoModificadores').jqGrid('getGridParam','selrow');
		var fila = $("#listadoModificadores").jqGrid('getRowData',idSeleccionado);

		idModificador = fila.idModificador;
		
		$('#formDetallePrestacionAnexo').attr('action', getUrlCargaModificador());
		$('#formDetallePrestacionAnexo').submit();
	}

	function eliminarModificador(idFila){
		idModificador = idFila;
		var titulo = 'Aviso';
		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarModificador",codIdioma)}', titulo, function(r){
			if (r == true){
				getXml("SvtConvenios", getUrlEliminaModificador(), function(xml){
					$("#listadoModificadores").jqGrid('setGridParam', {url: getUrlBuscaModificadores(), mtype: 'POST', datatype: 'xml', rowNum: 0}).trigger("reloadGrid");
					alert('${solemfmt:getLabel("confirm.ModificadorEliminado",codIdioma)}');
			    });
			}
		});
	}
</script>
<div id="listadoBusquedaModificador" class="listado" style="height:465px;">
	<table id="listadoModificadores"></table>
	<div id="pieModificador"></div>
</div> 