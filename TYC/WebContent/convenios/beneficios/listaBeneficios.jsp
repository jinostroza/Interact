<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var listadoBeneficiosData = null;
	var idBeneficio = 0; 

	$(function() {
		
		$("#listadoBeneficiosConvenio").jqGrid(
		{
			url : getUrlBuscaBeneficios(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '${solemfmt:getLabel("label.Codigo", codIdioma)}', '${solemfmt:getLabel("label.Nombre", codIdioma)}', '${solemfmt:getLabel("label.Estado", codIdioma)}', '' ],
			colModel : [
						{name : 'idConvBeneficio', index:'id', hidden : true}, 
			 			{name : 'codigo', index : 'cod', width : 277, resizable : false, sortable : true}, 
						{name : 'nombre', index:'nombre', width: 450, resizable: false, sortable: true},
			 			{name:  'estado', index: 'est', width: 207, resizable: false, sortable: true},
			 			{name : 'act',index:'act', index: 'act', width : 20, resizable : false,	sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idConvBeneficio',
				userdata : "solemdata"
			},
			caption: '${solemfmt:getLabel("label.ListadoBeneficios", codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 220,
			scrollOffset : scrollOffSet,
			rowNum : 0,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoBeneficios", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronBeneficios", codIdioma)}',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
			pager: '#pieBeneficio',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status) 
			{
				idConvBeneficio = rowId;
			},
			loadComplete : function(data) 
			{
				var udata = $("#listadoBeneficiosConvenio").getGridParam("userData");

				if ($("#listadoBeneficiosConvenio").getGridParam('datatype') != 'local')
				{
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoBeneficiosData = null;
					} else {
						listadoBeneficiosData = data;
					}
				}
			},
			gridComplete: function()
			{
				var ids = $("#listadoBeneficiosConvenio").getDataIDs();

				for ( var i = 0; i < ids.length; i++) 
				{
					var idFila = ids[i];

					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarBeneficio("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoBeneficiosConvenio").setRowData(ids[i], {act : btnEliminar});
				}
			},
			multiselect : false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});
		creaBoton("Beneficio", "A");
		$('#btnAgregaBeneficio').click(function(){
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var iframe =  $('<iframe id="ifrmBeneficio" src="'+getUrlBeneficio()+'"/>');
			window.parent.$('#ifrmBeneficio').css("height","560px");
			 
			var myDiv = document.getElementById('contenidoPrincipal');
 			var xDiv = getDimensions(myDiv).x+54;
			var yDiv = getDimensions(myDiv).y+55;  

			iframe.dialog({
				autoOpen: true, 
				modal: true,
				height: 142, 
				width: 911,
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title:'${solemfmt:getLabel("label.Beneficiarios", codIdioma)}' 
			}).width(911).height(142);
			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
		});  

	});

	function getUrlBeneficio(){
		var url = "";
		url = "SvtConvenios?KSI=${sesion.sesIdSesion}&accion=agregarBeneficio&idConvenio=${convenio.nIdConvenio}&idEmpresa=${convenio.nIdPrestador}";  
		return url;
	}

	function getUrlBuscaBeneficios(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarBeneficiosConvenio';
		sData += '&idConvenio=${convenio.nIdConvenio}';
		return sData;
	}

	function editarBeneficio(idFila)
	{
		idConvBeneficio = idFila;
		
		$('#titlePopup').parent().remove();
		$('iframe').remove();
		var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+54;
		var yDiv = getDimensions(myDiv).y+55; 
		var iframe =  $('<iframe id="ifrmBeneficio" src="'+getUrlCargaBeneficio()+'"/>'); 

		iframe.dialog({
			autoOpen: true,
			modal: true,
			height: 500,
			width: 916,
			draggable: true,
			resizable: false,
			autoResize: false,
			position: [xDiv,yDiv],
			title:'${solemfmt:getLabel("label.Beneficiarios", codIdioma)}' 
		}).width(916).height(500);
		$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
		$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
		$('.ui-dialog-titlebar').css("display","none"); 

		

		//$('#formDetalleConvenio').attr('action', getUrlCargaBeneficio());
		//$('#formDetalleConvenio').submit();
	}

	function eliminarBeneficio(idFila){
		idConvBeneficio = idFila;
		var titulo = 'Aviso';
		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarBeneficio", codIdioma)}', titulo, function(r)
		{
			if (r == true)
			{
				getXml("SvtConvenios", getUrlEliminaBeneficio(), function(xml)
				{
					$("#listadoBeneficiosConvenio").jqGrid('setGridParam', {url: getUrlBuscaBeneficios(), mtype: 'POST', datatype: 'xml', rowNum: 0}).trigger("reloadGrid");
					alert('${solemfmt:getLabel("confirm.BeneficioEliminado", codIdioma)}');
			    });
			}
		});
	}

	function recargarBusqueda(){
		$('#listadoBeneficiosConvenio').jqGrid().trigger("reloadGrid"); 
	}

    function getUrlCargaBeneficio(){
    	var dataFila = getDataFila(listadoBeneficiosData, "listadoBeneficiosConvenio", idConvBeneficio);        
    	var sData = 'SvtConvenios';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=cargarBeneficio';
      	sData += '&idBeneficio=' + dataFila.idConvBeneficio;
    	return sData;
    }

    function getUrlEliminaBeneficio(){
    	var dataFila = getDataFila(listadoBeneficiosData, "listadoBeneficiosConvenio", idConvBeneficio);        
    	var sData = 'KSI=${sesion.sesIdSesion}';
      	sData += '&accion=eliminarBeneficio';
      	sData += '&idBeneficio=' + dataFila.idConvBeneficio;
      	//console.log(sData);
    	return sData;	
    }
    
</script>
<div id="listadoBusquedaBeneficio" class="listado" style="height: 310px;">
	<table id="listadoBeneficiosConvenio"></table>
	<div id="pieBeneficio"></div>
</div> 