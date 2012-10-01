<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoPreciosPrestacionData = null;
	var idPrecioPrestacion = 0; 

	$(function() {
		
		$("#listadoPreciosPrestacion").jqGrid(
		{
			url : getUrlBuscaPreciosPrestacion(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '','', '${solemfmt:getLabel("label.Precio", codIdioma)}', '${solemfmt:getLabel("label.Formula", codIdioma)}', '${solemfmt:getLabel("label.FechaInicio", codIdioma)}','${solemfmt:getLabel("label.FechaFin", codIdioma)}', '' ],
			colModel : [
						{name : 'idPrecioPrestacion', index:'id', hidden : true}, 
						{name : 'idCatPrestacion', index:'id', hidden : true},
			 			{name : 'precio', index : 'precio', width : 170, resizable : false, sortable : true}, 
						{name : 'formula', index:'formula', width: 402, resizable: false, sortable: true},
						{name : 'fechaIni', index:'fecIni', width: 170, resizable: false, sortable: true},
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
				id : 'idPrecioPrestacion',
				userdata : "solemdata"
			},
			caption: '${solemfmt:getLabel("label.ListadoPrecios",codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 378,
			scrollOffset : scrollOffSet,
			rowNum : 0,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoPrecios", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronPrecios", codIdioma)}',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
			pager: '#piePrecioPrestacion',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status) 
			{
				idPrecioPrestacion = rowId;
			},
			loadComplete : function(data) 
			{
				var udata = $("#listadoPreciosPrestacion").getGridParam("userData");

				if ($("#listadoPreciosPrestacion").getGridParam('datatype') != 'local')
				{
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoPreciosPrestacionData = null;
					} else {
						listadoPreciosPrestacionData = data;
					}
				}
			},
			gridComplete: function()
			{
				var ids = $("#listadoPreciosPrestacion").getDataIDs();

				for ( var i = 0; i < ids.length; i++) 
				{
					var idFila = ids[i];

					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarPrecio("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoPreciosPrestacion").setRowData(ids[i], {act : btnEliminar});
				}
			},ondblClickRow: function(){
				editarPrecioPrestacion(idPrecioPrestacion); 
			},
			multiselect : false,
			loadError : function(xhr, status, error) 
			{
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});
		creaBoton("Precio", "A");

		$('#btnAgregaPrecio').click(function(){
			var url = "";
			url = "SvtCatalogos?KSI=${sesion.sesIdSesion}&accion=agregarPrecio&idCatalogoPrestacion=${prestacioncatalogo.nIdCatalogoPrestacion}&idCatalogo=${prestacioncatalogo.nIdCatalogo}&nombreCatalogo=${prestacioncatalogo.sNombreCatalogo}&nombrePrestacionCatalogo=${prestacioncatalogo.sNomPrestacion}&indRuta=S";
			$('#formDetallePrestacionCatalogo').attr('action', url);
			$('#formDetallePrestacionCatalogo').submit();
		});

	});

	function getUrlBuscaPreciosPrestacion() 
	{
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarPrecios';
		sData += '&idCatalogoPrestacion=${prestacioncatalogo.nIdCatalogoPrestacion}';
		return sData;
	}
	
	/*
	
	function editarPrecioPrestacion(idFila)
	{
		idPrecioPrestacion = idFila;

		$('#formDetallePrestacionCatalogo').attr('action', getUrlCargaPrecioCatalogo());
		$('#formDetallePrestacionCatalogo').submit();
	}
	
	*/
	
	function editarPrecioPrestacion(){
		var idSeleccionado = $('#listadoPreciosPrestacion').jqGrid('getGridParam','selrow');
		var fila = $("#listadoPreciosPrestacion").jqGrid('getRowData',idSeleccionado);

		idPrecioPrestacion = fila.idPrecioPrestacion;

		$('#formDetallePrestacionCatalogo').attr('action', getUrlCargaPrecioCatalogo());
		$('#formDetallePrestacionCatalogo').submit();
	}

	function eliminarPrecio(idFila)
	{
		idPrecioPrestacion = idFila;
		var titulo = 'Aviso';
		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarPrecio", codIdioma)}', titulo, function(r)
		{
			if (r == true)
			{
				getXml("SvtCatalogos", getUrlEliminaPrecio(), function(xml)
				{
					$("#listadoPreciosPrestacion").jqGrid('setGridParam', {url: getUrlBuscaPreciosPrestacion(), mtype: 'POST', datatype: 'xml', rowNum: 0}).trigger("reloadGrid");
					alert('${solemfmt:getLabel("confirm.PrecioEliminado", codIdioma)}');
			    });
			}
		});
	}

    function getUrlCargaPrecioCatalogo()
    {
    	var dataFila = getDataFila(listadoPreciosPrestacionData, "listadoPreciosPrestacion", idPrecioPrestacion);        
    	var sData = 'SvtCatalogos';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=cargarPrecioCatalogo';
      	sData += '&idCatalogoPrecio=' + dataFila.idPrecioPrestacion;
      	sData += '&indRuta=S';
    	return sData;
    }

    function getUrlEliminaPrecio()
    {
    	var dataFila = getDataFila(listadoPreciosPrestacionData, "listadoPreciosPrestacion", idPrecioPrestacion);        
    	var sData = 'KSI=${sesion.sesIdSesion}';
      	sData += '&accion=eliminarPreciosCatalogo';
      	sData += '&precioCatalogo.nIdCatalogoPrecio=' + dataFila.idPrecioPrestacion;
      	sData += '&precioCatalogo.nIdUsuarioModificacion=${sesion.sesIdUsuario}';
    	return sData;	
    }
    
</script>
<div id="listadoBusquedaPrecio" class="listado" style="height:auto;">
	<table id="listadoPreciosPrestacion"></table>
	<div id="piePrecioPrestacion"></div>
</div> 