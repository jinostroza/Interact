<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>


<script type="text/javascript">
	var idCatPrestacion = 0;
	var listadoPrestacionesData = null;
		
	$(function() 
	{
		
		var nomEntidad = "Prestacion";
		$("#listadoPrestaciones").jqGrid(
		{
			url : getUrlBuscaPrestacionesCatalogo(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '','','${solemfmt:getLabel("label.Codigo", codIdioma)}', '${solemfmt:getLabel("label.Prestacion", codIdioma)}',  '${solemfmt:getLabel("label.FechaIncorporacion", codIdioma)}', '' ],
			colModel : [
						{name : 'idCatPrestacion', index:'idCatPrestacion', hidden : true}, 
						{name : 'idPrestacion', index:'idPrestacion', hidden : true}, 
			 			{name : 'codigoprestacion',index:'codigoprestacion', width : 130, search : true, resizable : false, sortable : true}, 
			 			{name : 'nombreprestacion', index : 'nombreprestacion', width : 629, resizable : false, sortable : true}, 
						{name : 'fechaincorporacion', index:'fechaincorporacion', width: 150, resizable: false, sortable: true},
			 			{name : 'act',index:'act', index: 'act', width : 20, resizable : false,	sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idCatPrestacion',
				userdata : "solemdata"
			},
			caption : '${solemfmt:getLabel("label.ListadoPrestaciones", codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 280,
			scrollOffset : scrollOffSet,
			rowNum : 9,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoCatalogos", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronCatalogos", codIdioma)}',
			hidegrid : false,
			sortname : 'id',  
			sortorder : 'ASC',
			page : 1,
			pager: '#piePrestacion',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : true,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status) 
			{
				idCatPrestacion = rowId;
			},
			loadComplete : function(data) 
			{
				var udata = $("#listadoPrestaciones").getGridParam("userData");

				if ($("#listadoPrestaciones").getGridParam('datatype') != 'local') 
				{
					if (parseInt(udata.errornum, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.errornum + ")"+ udata.errormsj);
						listadoPrestacionesData = null;
					} else {
						listadoPrestacionesData = data;
					}
				}
	
			},
			gridComplete: function()
			{
				var ids = $("#listadoPrestaciones").getDataIDs();

				for ( var i = 0; i < ids.length; i++) 
				{
					var idFila = ids[i];
					
					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarFilaPrestacion("+idFila+")'>";
					btnEliminar+= 	"<img title='eliminar' src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoPrestaciones").setRowData(ids[i], {act : btnEliminar});
				}
			},
			onPaging: function(pgButton){
				pagUsuario = $("#listadoPrestaciones").jqGrid('getGridParam','page');
				totalRegistros = $("#listadoPrestaciones").jqGrid('getGridParam','records');
				regPorPagina = $("#listadoPrestaciones").jqGrid('getGridParam','rowNum');
				totalPaginas = Math.ceil(totalRegistros/regPorPagina);

				if(totalPaginas<pagUsuario)
				{
					$("listadoPrestaciones").setGridParam({page:totalPaginas});				
				}
			},ondblClickRow: function(){
				editarPrestacionCatalogo(idCatPrestacion); 
			},
			multiselect : true,
			loadError : function(xhr, status, error) 
			{
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});

		creaBoton(nomEntidad, "F");

	    $("#tituloFiltros img").click(function(event){
			var id = $(this).attr("id");
			var div = $(this).parent().parent().parent().attr("id");

			if(id=="flechaUp"){
				$("#"+div+" .contenido_detalle").slideUp();
				$(this).attr("id","flechaDown");	
				$(this).attr("src","${rutaImg}template/"+$(this).attr("imgDown"));
				//cambiar a reloadGrid cuando sea con paginacion
				var t = setTimeout("borderBottomRadius('"+div+"')",400);
				$(this).parent().parent().removeClass("borderBottomRadius");
				
			}else{
			  //$(".ui-jqgrid-bdiv").animate({height: '303px'}, 50);
				$(".ui-jqgrid-bdiv").css('height','420px');
				$(this).attr("id","flechaUp");	
				$(this).attr("src","${rutaImg}template/"+$(this).attr("imgUp")); 
				$(this).parent().parent().removeClass("borderBottomRadius");
				$("#"+div+" .contenido_detalle").slideDown();  
			}
		}); 

	    $('#btnAgregaPrestacion').click(function(){
			$('#formDetalleCatalogo').attr('action', getUrlPrestacion());
			$('#formDetalleCatalogo').submit();
		});

	    $('#btnEliminaPrestacion').click(function(){
	    	if(validacheckgrilla())
			{
	    		var titulo = 'Aviso';
	    		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarPrestaciones", codIdioma)}', titulo, function(r)
	    		{
	    			if (r == true)
	    			{
	    		getXml("SvtCatalogos", getUrlEliminaPrestacion(), function(xml)
 					{
 						$("#listadoPrestaciones").jqGrid('setGridParam', {url: getUrlBuscaPrestacionesCatalogo(), mtype: 'POST', datatype: 'xml', rowNum: 0}).trigger("reloadGrid");
 						alert('${solemfmt:getLabel("confirm.PrestacionEliminada", codIdioma)}');
 			       	});
	    			}
	    		});
			}
	    	else
			{
				alert('${solemfmt:getLabel("alert.DebeSeleccionarAlMenosUnaPrestacion", codIdioma)}');
			}
		});

	});

	function validacheckgrilla() { 
	var value = getid();
		if(value.length>0)
			return true;
		else 
			return false;	
	}

	
	function recargarBusqueda() 
		{
			console.log("Recargar Busqueda");
			console.log(getUrlBuscaPrestacionesCatalogo());
			
			$('#listadoPrestaciones').jqGrid('setGridParam', {
				url : getUrlBuscaPrestacionesCatalogo(),
				mtype : 'POST',
				datatype : 'xml',
				rowNum : 10,
				page : 1,
			}).trigger("reloadGrid");
		}
	
	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius");
		$(".ui-jqgrid-bdiv").animate({height: "482px"}, 400 ).trigger("resize");// Animate Grilla
	}
	
	function getUrlBuscaPrestacionesCatalogo() 
	{
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarPrestacionesCatalogo';
		sData += '&idCatalogo=${catalogo.nIdCatalogo}';
		return sData;
	}
	
	function editarPrestacionCatalogo(idFila)
	{

		var fila = $('#listadoPrestaciones').jqGrid('getRowData',idFila);
		$('#formDetalleCatalogo').attr('action', getUrlCargaPrestacionCatalogo(fila));
		$('#formDetalleCatalogo').submit();
	}

    function getUrlCargaPrestacionCatalogo(fila)
    {
    	var sData = 'SvtCatalogos';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=cargarPrestacionesCatalogo';
      	sData += '&idCatalogoPrestacion=' + fila.idCatPrestacion;
      	sData += '&indRuta=S';
    	return sData;
    }

    function getUrlPrestacion(){
		var sData = "";
		sData = "SvtCatalogos?KSI=${sesion.sesIdSesion}&idCatalogo=${catalogo.nIdCatalogo}&accion=agregarPrestacion&indRuta=S";  
		return sData;
	}

    function getid()
    {
		var value = '';
		var data = '';
   		var list = $("#listadoPrestaciones").jqGrid('getGridParam','selarrrow');
   			for( var i=0; i<list.length;i++)
   			{
   				data = $("#listadoPrestaciones").getRowData(list[i]);

   				if(i == list.length-1)
   					value+= (data.idCatPrestacion);
   				else
   					value+= (data.idCatPrestacion) + ',';
   			}

		return value;
    }
	
    function getUrlEliminaPrestacion()
    {
		var sData = 'KSI=${sesion.sesIdSesion}';
		sData += '&accion=eliminarPrestacionesCatalogo';
		sData += '&idCatalogo=${catalogo.nIdCatalogo}';
		sData += '&aIds=' + getid();
		return sData;
    }

   
	function eliminarFilaPrestacion(idFila) {
		idCatalogo = idFila;
		var titulo = 'Aviso';
		jConfirm('¿Desea eliminar una Prestacion?', titulo, function(r)
		{
			if (r == true)
			{
				getXml("SvtCatalogos", getUrlEliminaPrestacion(), function(xml)
				{
					$("#listadoPrestaciones").jqGrid('setGridParam', {url: getUrlBuscaPrestacionesCatalogo(), mtype: 'POST', datatype: 'xml', rowNum: 0}).trigger("reloadGrid");
					alert("Prestacion eliminado exitosamente");
		       	});	
			}
		});
	}
   
</script> 
	<div id="listadoBusquedaPrestacion" class="listado" style="height:367px;">
		<table id="listadoPrestaciones"></table>
		<div id="piePrestacion"></div>
	</div>  