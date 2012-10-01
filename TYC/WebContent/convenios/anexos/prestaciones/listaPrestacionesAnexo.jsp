<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">

	var listadoPrestacionesAnexoData = null;
	var idPrestacionAnexo = 0; 

	$(function() {
		
		$("#listadoPrestacionesAnexo").jqGrid(
		{
			url : getUrlBuscaPrestacionesAnexo(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '${solemfmt:getLabel("label.Codigo",codIdioma)}', '${solemfmt:getLabel("label.Prestacion",codIdioma)}', '${solemfmt:getLabel("label.FechaIncorporacion",codIdioma)}', '' ],
			colModel : [
						{name : 'idPrestacionAnexo', index:'id', hidden : true}, 
			 			{name : 'codigo', index : 'cod', width : 223, resizable : false, sortable : true}, 
						{name : 'nombre', index:'nom', width: 531, resizable: false, sortable: true},
			 			{name:  'fechaIni', index: 'fecIni', width: 180, resizable: false, sortable: true},
			 			{name : 'act',index:'act', index: 'act', width : 20, resizable : false,	sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idPrestacionAnexo',
				userdata : "solemdata"
			},
			caption: '${solemfmt:getLabel("label.ListadoPrestaciones",codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 340,
			scrollOffset : scrollOffSet,
			rowNum : 10,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoPrestacionesAnexo",codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronPrestacionesAnexo",codIdioma)}',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
			pager: '#piePrestacionAnexo',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : true,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status) 
			{
				idPrestacionAnexo = rowId;
			},
			loadComplete : function(data) 
			{
				var udata = $("#listadoPrestacionesAnexo").getGridParam("userData");

				if ($("#listadoPrestacionesAnexo").getGridParam('datatype') != 'local')
				{
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoPrestacionesAnexoData = null;
					} else {
						listadoPrestacionesAnexoData = data;
					}
				}
			},
			gridComplete: function()
			{
				var ids = $("#listadoPrestacionesAnexo").getDataIDs();

				for ( var i = 0; i < ids.length; i++) 
				{
					var idFila = ids[i];

					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarPrestacionAnexo("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoPrestacionesAnexo").setRowData(ids[i], {act : btnEliminar});
				}
			},ondblClickRow: function(){
				editarPrestacionAnexo(); 
			},
			multiselect : false,
			loadError : function(xhr, status, error) 
			{
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});
		creaBoton("Prestacion", "A");

		$('#btnAgregaPrestacion').click(function(){
			$('#formDetalleAnexo').attr('action', getUrlAgregaPrestacionAnexo());
			$('#formDetalleAnexo').submit();
		});
	});

	function getUrlBuscaPrestacionesAnexo() 
	{
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarPrestacionesAnexoConvenio';
		sData += '&idAnexo='+${anexo.nIdAnexo};
		return sData;
	}

	function getUrlAgregaPrestacionAnexo()
	{
		var sData = "SvtConvenios";
		sData+= "?KSI=${sesion.sesIdSesion}";
		sData += '&accion=agregarPrestacionAnexo';
		sData += '&idConvenio=${anexo.nIdConvenio}';
		sData += '&idAnexo=${anexo.nIdAnexo}';
		sData += '&indRuta=S';
		return sData;
	}
	
	/*function getUrlAgregaPrestacionAnexo(){
		var url = "";
		url = "SvtConvenios?KSI=${sesion.sesIdSesion}&idConvenio=${anexo.nIdConvenio}&idAnexo=${anexo.nIdAnexo}&accion=agregarPrestacionAnexo";  
		return url;
	}*/

    function getUrlCargaPrestacionAnexo()
    {
    	var dataFila = getDataFila(listadoPrestacionesAnexoData, "listadoPrestacionesAnexo", idPrestacionAnexo);        
    	var sData = 'SvtConvenios';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=cargarPrestacionAnexo';
      	sData += '&idPrestacionAnexo=' + dataFila.idPrestacionAnexo;
      	sData += '&indRuta=S';
    	return sData;
    }

    function getUrlEliminaPrestacionAnexo()
    {
    	var dataFila = getDataFila(listadoPrestacionesAnexoData, "listadoPrestacionesAnexo", idPrestacionAnexo);        
    	var sData = 'KSI=${sesion.sesIdSesion}';
      	sData += '&accion=eliminarPrestacionAnexo';
      	sData += '&idPrestacionAnexo=' + dataFila.idPrestacionAnexo;
    	return sData;	
    }

    function editarPrestacionAnexo()
	{
		var idSeleccionado = $('#listadoPrestacionesAnexo').jqGrid('getGridParam','selrow');
		var fila = $("#listadoPrestacionesAnexo").jqGrid('getRowData',idSeleccionado);

		idPrestacionAnexo = fila.idPrestacionAnexo;

		$('#formDetalleAnexo').attr('action', getUrlCargaPrestacionAnexo());
		$('#formDetalleAnexo').submit();
	}

	function eliminarPrestacionAnexo(idFila)
	{
		idPrestacionAnexo = idFila;
		var titulo = 'Aviso';
		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarPrestacionAnexo",codIdioma)}', titulo, function(r)
		{
			if (r == true)
			{
				getXml("SvtConvenios", getUrlEliminaPrestacionAnexo(), function(xml)
				{
					$("#listadoPrestacionesAnexo").jqGrid('setGridParam', {url: getUrlBuscaPrestacionesAnexo(), mtype: 'POST', datatype: 'xml', rowNum: 0}).trigger("reloadGrid");
					alert('${solemfmt:getLabel("confirm.PrestacionAnexoEliminada",codIdioma)}');
			    });
			}
		});
	}

	function recargarBusqueda(){ 
		$('#listadoPrestacionesAnexo').jqGrid().trigger("reloadGrid"); 
	}
</script>

<div id="listadoBusquedaPrestacion" class="listado" style="height:425px;">
	<table id="listadoPrestacionesAnexo"></table>
	<div id="piePrestacionAnexo"></div>
</div> 