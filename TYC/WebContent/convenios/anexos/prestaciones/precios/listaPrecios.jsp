<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoPreciosData = null;
	var idPrecio = 0; 

	$(function() {
		
		$("#listadoPrecios").jqGrid(
		{
			url : getUrlBuscaPrecios(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '${solemfmt:getLabel("label.Precio",codIdioma)}', '${solemfmt:getLabel("label.Formula",codIdioma)}', '${solemfmt:getLabel("label.FechaInicio",codIdioma)}','${solemfmt:getLabel("label.FechaFin",codIdioma)}', '' ],
			colModel : [
						{name : 'idPrecio', index:'id', hidden : true}, 
			 			{name : 'precio', index : 'precio', width : 170, resizable : false, sortable : true, align: 'right'}, 
						{name : 'formula', index:'formula', width: 400, resizable: false, sortable: true},
						{name : 'fechaInicio', index:'fecIni', width: 172, resizable: false, sortable: true},
			 			{name:  'fechaTermino', index: 'fecFin', width: 170, resizable: false, sortable: true},
			 			{name : 'act',index:'act', index: 'act', width : 20, resizable : false,	sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idPrecio',
				userdata : "solemdata"
			},
			caption: '${solemfmt:getLabel("label.ListadoPrecios",codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 378, 
			scrollOffset : scrollOffSet,
			rowNum : 0,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoPrecios",codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronPrecios",codIdioma)}',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
			pager: '#piePrecio',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idPrecio = rowId;
			},
			loadComplete : function(data)
			{
				var udata = $("#listadoPrecios").getGridParam("userData");

				if ($("#listadoPrecios").getGridParam('datatype') != 'local')
				{
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoPreciosData = null;
					} else {
						listadoPreciosData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoPrecios").getDataIDs();

				for ( var i = 0; i < ids.length; i++){
					var idFila = ids[i];

					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarPrecio("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoPrecios").setRowData(ids[i], {act : btnEliminar});
				}
			},ondblClickRow: function(){
				editarPrecio(); 
			},
			multiselect : false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});
		creaBoton("Precio", "A");

		$('#btnAgregaPrecio').click(function(){
			var url = "";
			url = "SvtConvenios";
			url +="?KSI=${sesion.sesIdSesion}";
			url +="&accion=agregarPrecio";
			url +="&idPrestacionAnexo=${prestacionAnexo.nIdConvDetalle}";
			url +="&nombrePrestacionAnexo=${prestacionAnexo.sNombrePrestacion}";
			url +="&indRuta=S";

			$('#formDetallePrestacionAnexo').attr('action', url);
			$('#formDetallePrestacionAnexo').submit();
		});

	});

	function getUrlBuscaPrecios(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarPrecios';
		sData += '&idPrestacionAnexo='+${prestacionAnexo.nIdConvDetalle};
		return sData;
	}

	function editarPrecio(){
		var idSeleccionado = $('#listadoPrecios').jqGrid('getGridParam','selrow');
		var fila = $("#listadoPrecios").jqGrid('getRowData',idSeleccionado);

		idPrecio = fila.idPrecio;

		$('#formDetallePrestacionAnexo').attr('action', getUrlCargaPrecio());
		$('#formDetallePrestacionAnexo').submit();
	}

	function eliminarPrecio(idFila){
		idPrecio = idFila;
		var titulo = 'Aviso';
		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarPrecio",codIdioma)}', titulo, function(r){
			if (r == true){
				getXml("SvtConvenios", getUrlEliminaPrecio(), function(xml){
					$("#listadoPrecios").jqGrid('setGridParam', {url: getUrlBuscaPrecios(), mtype: 'POST', datatype: 'xml', rowNum: 0}).trigger("reloadGrid");
					alert('${solemfmt:getLabel("confirm.PrecioEliminado",codIdioma)}');
			    });
			}
		});
	}

    function getUrlCargaPrecio(){
    	var dataFila = getDataFila(listadoPreciosData, "listadoPrecios", idPrecio);        
    	var sData = 'SvtConvenios';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=cargarPrecio';
      	sData += '&idPrecio=' + dataFila.idPrecio;
      	sData += '&indRuta=S';
    	return sData;
    }

    function getUrlEliminaPrecio(){
    	var dataFila = getDataFila(listadoPreciosData, "listadoPrecios", idPrecio);        
    	var sData = 'KSI=${sesion.sesIdSesion}';
      	sData += '&accion=eliminarPrecio';
      	sData += '&idPrecio=' + dataFila.idPrecio;
    	return sData;	
    }
    
</script>

<div id="listadoBusquedaPrecio" class="listado" style="height: 465px;">
	<table id="listadoPrecios"></table>
	<div id="piePrecio"></div>
</div> 