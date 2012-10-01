<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoRestriccionesData = null;
	var idRestriccion = 0; 

	$(function() {
		
		$("#listadoRestriccionesConvenio").jqGrid(
		{
			url : getUrlBuscaRestricciones(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '${solemfmt:getLabel("label.Nombre", codIdioma)}', '${solemfmt:getLabel("label.Tipo", codIdioma)}', '${solemfmt:getLabel("label.Valor", codIdioma)}', '${solemfmt:getLabel("label.Rango1", codIdioma)}', '${solemfmt:getLabel("label.Rango2", codIdioma)}', '' ],
			colModel : [
						{name : 'idRestriccion', index:'id', hidden : true}, 
			 			{name : 'nombre', index : 'nom', width : 270, resizable : false, sortable : true}, 
						{name : 'tipo', index:'fecIni', width: 165, resizable: false, sortable: true},
			 			{name:  'valor', index: 'fecFin', width: 157, resizable: false, sortable: true},
						{name : 'rango1',index:'est',width : 166, resizable : false,	sortable : true},
						{name : 'rango2',index:'est',width : 166, resizable : false,	sortable : true},
			 			{name : 'act',index:'act', index: 'act', width : 20, resizable : false,	sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idRestriccion',
				userdata : "solemdata"
			},
			caption: '${solemfmt:getLabel("label.ListadoRestricciones", codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 220,
			scrollOffset :scrollOffSet,
			rowNum : 0,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoRestricciones", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronRestricciones", codIdioma)}',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
			pager: '#pieRestriccion',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status) 
			{
				idRestriccion = rowId;
			},
			loadComplete : function(data) 
			{
				var udata = $("#listadoRestriccionesConvenio").getGridParam("userData");

				if ($("#listadoRestriccionesConvenio").getGridParam('datatype') != 'local')
				{
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoRestriccionesData = null;
					} else {
						listadoRestriccionesData = data;
					}
				}
			},
			gridComplete: function()
			{
				var ids = $("#listadoRestriccionesConvenio").getDataIDs();

				for ( var i = 0; i < ids.length; i++) 
				{
					var idFila = ids[i];

					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarRestriccion("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoRestriccionesConvenio").setRowData(ids[i], {act : btnEliminar});
				}
			},ondblClickRow: function(){
				editarRestriccion(); 
			},
			multiselect : false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});
		
		creaBoton("Restriccion", "A");

		$('#btnAgregaRestriccion').click(function(){
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
 			var xDiv = getDimensions(myDiv).x+54;
			var yDiv = getDimensions(myDiv).y+55; 
			var iframe =  $('<iframe id="ifrmRestriccion" src="'+getUrlRestriccion()+'"/>');  

			iframe.dialog({
				autoOpen: true, 
				modal: true,
				height: 208, 
				width: 916,
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title:'${solemfmt:getLabel("label.Restricciones", codIdioma)}' 
			}).width(916).height(208);
			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
		});

	});

	function getUrlRestriccion(){
		var url = "";
		url = "SvtConvenios?KSI=${sesion.sesIdSesion}&idConvenio=${convenio.nIdConvenio}&accion=agregarRestriccion";  
		return url;
	}

	function getUrlBuscaRestricciones(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarRestriccionesConvenio';
		sData += '&idConvenio=${convenio.nIdConvenio}';
		return sData;
	}

	function editarRestriccion()
	{
		var idSeleccionado = $('#listadoRestriccionesConvenio').jqGrid('getGridParam','selrow');
		var fila = $("#listadoRestriccionesConvenio").jqGrid('getRowData',idSeleccionado);

		idRestriccion = fila.idRestriccion;

		//$('#formDetalleConvenio').attr('action', getUrlCargaRestriccion());
		//$('#formDetalleConvenio').submit();

		$('#titlePopup').parent().remove();
		$('iframe').remove();
		var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+54;
		var yDiv = getDimensions(myDiv).y+55; 
		var iframe =  $('<iframe id="ifrmRestriccion" src="'+getUrlCargaRestriccion()+'"/>'); 

		iframe.dialog({
			autoOpen: true, 
			modal: true,
			height: 261, 
			width: 916,
			draggable: true,
			resizable: false,
			autoResize: false,
			position: [xDiv,yDiv],
			title:'${solemfmt:getLabel("label.Restricciones", codIdioma)}' 
		}).width(916).height(261);
		$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
		$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
		$('.ui-dialog-titlebar').css("display","none"); 
	
	}

	function eliminarRestriccion(idFila){
		idRestriccion = idFila;
		var titulo = 'Aviso';
		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarRestriccion", codIdioma)}', titulo, function(r)
		{
			if (r == true)
			{
				getXml("SvtConvenios", getUrlEliminaRestriccion(), function(xml)
				{
					$("#listadoRestriccionesConvenio").jqGrid('setGridParam', {url: getUrlBuscaRestricciones(), mtype: 'POST', datatype: 'xml', rowNum: 0}).trigger("reloadGrid");
					alert('${solemfmt:getLabel("confirm.RestriccionEliminada", codIdioma)}');
			    });
			}
		});
	}

	function recargarBusquedaRestricciones(){
		$('#listadoRestriccionesConvenio').jqGrid().trigger("reloadGrid"); 
	}

    function getUrlCargaRestriccion(){
    	var dataFila = getDataFila(listadoRestriccionesData, "listadoRestriccionesConvenio", idRestriccion);        
    	var sData = 'SvtConvenios';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=cargarRestriccion';
      	sData += '&idRestriccion=' + dataFila.idRestriccion;
    	return sData;
    }

    function getUrlEliminaRestriccion(){
    	var dataFila = getDataFila(listadoRestriccionesData, "listadoRestriccionesConvenio", idRestriccion);        
    	var sData = 'KSI=${sesion.sesIdSesion}';
      	sData += '&accion=eliminarRestriccion';
      	sData += '&idRestriccion=' + dataFila.idRestriccion;
    	return sData;
    }
    
</script>
<div id="listadoBusquedaRestriccion" class="listado" style="height:310px;">
	<table id="listadoRestriccionesConvenio"></table>
	<div id="pieRestriccion"></div>
</div> 