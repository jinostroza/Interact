<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">

	var listadoAnexosData = null;
	var idAnexo = 0; 

	$(function() {

		$("#listadoAnexosConvenio").jqGrid(
		{
			url : getUrlBuscaAnexos(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '${solemfmt:getLabel("label.Nombre", codIdioma)}', '${solemfmt:getLabel("label.FechaInicio", codIdioma)}', '${solemfmt:getLabel("label.FechaFin", codIdioma)}', '${solemfmt:getLabel("label.Estado", codIdioma)}', '' ],
			colModel : [
						{name : 'idAnexo', index:'id', hidden : true}, 
			 			{name : 'nombre', index : 'nom', width : 330, resizable : false, sortable : true}, 
						{name : 'fechaIni', index:'fecIni', width: 185, resizable: false, sortable: true},
			 			{name:  'fechaFin', index: 'fecFin', width: 185, resizable: false, sortable: true},
						{name : 'estado',index:'est',width : 229, resizable : false,	sortable : true},
			 			{name : 'act',index:'act', index: 'act', width : 20, resizable : false,	sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idAnexo',
				userdata : "solemdata"
			},
			caption: '${solemfmt:getLabel("label.ListadoAnexos", codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 220,
			scrollOffset : scrollOffSet,
			rowNum : 0,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoAnexos", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronAnexos", codIdioma)}',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
			pager: '#pieAnexo',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status) 
			{
				idAnexo = rowId;
			},
			loadComplete : function(data) 
			{
				var udata = $("#listadoAnexosConvenio").getGridParam("userData");

				if ($("#listadoAnexosConvenio").getGridParam('datatype') != 'local')
				{
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoAnexosData = null;
					} else {
						listadoAnexosData = data;
					}
				}
			},
			gridComplete: function()
			{
				var ids = $("#listadoAnexosConvenio").getDataIDs();

				for ( var i = 0; i < ids.length; i++) 
				{
					var idFila = ids[i];

					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarAnexo("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoAnexosConvenio").setRowData(ids[i], {act : btnEliminar});
				}
			},ondblClickRow: function(){
				editarAnexo(); 
			},
			multiselect : false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});
		creaBoton("Anexo", "A");
		
		$('#btnAgregaAnexo').click(function(){
			$('#formDetalleConvenio').attr('action', getUrlAgregaAnexo());
			$('#formDetalleConvenio').submit();
		});
		
	});

	function getUrlBuscaAnexos()
	{
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarAnexosConvenio';
		sData += '&idConvenio=${convenio.nIdConvenio}';
		return sData;
	}

	function getUrlAgregaAnexo()
	{
		var sData = "SvtConvenios";
		sData+= "?KSI=${sesion.sesIdSesion}";
		sData += '&accion=agregarAnexo';
		sData += '&idConvenio=${convenio.nIdConvenio}';
		sData += '&indRuta=S';
		return sData;
	}

	function getUrlCargaAnexo()
	{
		var dataFila = getDataFila(listadoAnexosData, "listadoAnexosConvenio", idAnexo);        
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=cargarAnexo';
		sData += '&idAnexo=' + dataFila.idAnexo;
		sData += '&indRuta=S';
		return sData;
	}

    function getUrlEliminaAnexo()
    {
    	var dataFila = getDataFila(listadoAnexosData, "listadoAnexosConvenio", idAnexo);        
    	var sData = 'KSI=${sesion.sesIdSesion}';
      	sData += '&accion=eliminarAnexo';
      	sData += '&idAnexo=' + dataFila.idAnexo;
    	return sData;	
    }

	function editarAnexo()
	{
		var idSeleccionado = $('#listadoAnexosConvenio').jqGrid('getGridParam','selrow');
		var fila = $("#listadoAnexosConvenio").jqGrid('getRowData',idSeleccionado);

		idAnexo = fila.idAnexo;

		$('#formDetalleConvenio').attr('action', getUrlCargaAnexo());
		$('#formDetalleConvenio').submit();
	}

	function eliminarAnexo(idFila){
		idAnexo = idFila;
		var titulo = 'Aviso';
		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarAnexo", codIdioma)}', titulo, function(r)
		{
			if (r == true)
			{
				getXml("SvtConvenios", getUrlEliminaAnexo(), function(xml)
				{
					$("#listadoAnexosConvenio").jqGrid('setGridParam', {url: getUrlBuscaAnexos(), mtype: 'POST', datatype: 'xml', rowNum: 0}).trigger("reloadGrid");
					alert('${solemfmt:getLabel("confirm.AnexoEliminado", codIdioma)}');
			    });
			}
		});
	}

	function recargarBusqueda(){
		$('#listadoAnexosConvenio').jqGrid().trigger("reloadGrid");
	}

</script>
<div id="listadoBusquedaAnexo" class="listado" style="height:310px;">
	<table id="listadoAnexosConvenio"></table>
	<div id="pieAnexo"></div>
</div> 