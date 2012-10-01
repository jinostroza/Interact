<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var idPrestacion;
	var listadoPrestacionesCompuestasData = null;
	var idNumFila = 0;
	
	$(function() {
		var nomEntidad = "PrestacionCompuesta";
		
		$("#listadoPrestacionCompuesta").jqGrid(
		{
			url : getUrlBuscaPrestacionesCompuestas(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '${solemfmt:getLabel("label.Cantidad", codIdioma)}','${solemfmt:getLabel("label.Codigo", codIdioma)}', '${solemfmt:getLabel("label.Prestacion", codIdioma)}',  '${solemfmt:getLabel("label.FechaIncorporacion", codIdioma)}', '' ],  
			colModel : [
						{name : 'idPrestacion', index:'idPrestacion', hidden : true}, 
						{name : 'cantidad', index : 'cant', width : 60, resizable : false, sortable : true,  align:'right'},
						{name : 'codigo',index:'codigo', width : 82, search : true, resizable : false, sortable : true, align:'right'},
			 			{name : 'prestacion',index:'prest', width : 622, search : true, resizable : false, sortable : true}, 
						{name : 'fecha', index:'fecha', width: 140, resizable: false, sortable: true},
			 			{name : 'act',index:'act', index: 'act', width : 20, resizable : false,	sortable : true} 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idPrestacion',
				userdata : "solemdata"						
			},
			caption : '${solemfmt:getLabel("label.ListadoPrestacionesCompuestas", codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 310,
			scrollOffset : 0,
			rowNum : 9,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoPrestaciones", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronPrestaciones", codIdioma)}',
			hidegrid : false,
			sortname : 'idPrestacion',  
			sortorder : 'ASC',
			page : 1,
			pager: '#piePrestacionCompuesta',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : true,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status){
				idNumFila = rowId;
			},
			loadComplete : function(data){
				var udata = $("#listadoPrestacionCompuesta").getGridParam("userData");

				if ($("#listadoPrestacionCompuesta").getGridParam('datatype') != 'local') {
					if (parseInt(udata.errornum, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.errornum + ")"+ udata.errormsj);
						listadoPrestacionesCompuestasData = null;
					} else {
						listadoPrestacionesCompuestasData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoPrestacionCompuesta").getDataIDs();

				for ( var i = 0; i < ids.length; i++){
					var idFila = ids[i];

					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarPrestacionCompuesta("+idFila+")'>";
					btnEliminar+= 	"<img title='eliminar' src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoPrestacionCompuesta").setRowData(ids[i], {act : btnEliminar});
				}
			},
			onPaging: function(pgButton){
				pagUsuario = $("#listadoPrestacionCompuesta").jqGrid('getGridParam','page');
				totalRegistros = $("#listadoPrestacionCompuesta").jqGrid('getGridParam','records');
				regPorPagina = $("#listadoPrestacionCompuesta").jqGrid('getGridParam','rowNum');
				totalPaginas = Math.ceil(totalRegistros/regPorPagina);

				if(totalPaginas<pagUsuario)
				{
					$("listadoPrestacionCompuesta").setGridParam({page:totalPaginas});				
				}
			},
			multiselect : true,
			loadError : function(xhr, status, error){
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
				$(".ui-jqgrid-bdiv").css('height','435px');
				$(this).attr("id","flechaUp");	
				$(this).attr("src","${rutaImg}template/"+$(this).attr("imgUp")); 
				$(this).parent().parent().removeClass("borderBottomRadius");
				$("#"+div+" .contenido_detalle").slideDown();  
			}
		});

		$('#btnAgregaPrestacionCompuesta').click(function()
		{
	    	$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y+85; 
			var url = "SvtPrestaciones?KSI=${sesion.sesIdSesion}&accion=cargarPopUpCompuesta&nIdPrestacion="+$("#idPrestacion").val();
			var iframe =  $('<iframe id="ifrmCompuestas" src="'+url+'"/>');  
			
			iframe.dialog({
				autoOpen: true,
				modal: true,
				height: 386,
				width: 820,
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title: 'Lista Compuestas', //'${solemfmt:getLabel("label.BusquedaPrestaciones", codIdioma)}'  
			}).width(820).height(386);

			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 			
	    });
		
		$('#btnEliminaPrestacionCompuesta').click(function()
		{
		    if(validarCheckGrid("listadoPrestacionCompuesta"))
			{
	    		var titulo = 'Aviso';
	    		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarPrestaciones", codIdioma)}', titulo, function(r){
	    			if (r == true){
	    				getXml("SvtPrestaciones", getUrlEliminaPrestacionesCompuestas(), function(xml){
 							$("#listadoPrestacionCompuesta").jqGrid('setGridParam', {url: getUrlBuscaPrestacionesCompuestas(), mtype: 'POST', datatype: 'xml', rowNum: 9, page: 1,}).trigger("reloadGrid");
 							alert('${solemfmt:getLabel("confirm.PrestacionEliminada", codIdioma)}');
 			       		});
	    			}
	    		});
			}
	    	else{
				alert('${solemfmt:getLabel("alert.DebeSeleccionarAlMenosUnaPrestacion", codIdioma)}');
			}
	    });
	});

	function borderBottomRadius(div){
		$("#"+div+" .titulo").addClass("borderBottomRadius");
		$(".ui-jqgrid-bdiv").animate({height: "482px"}, 400 ).trigger("resize");// Animate Grilla
	}

	function recargarBusquedaRestricciones(){
		$('#listadoPrestacionCompuesta').jqGrid('setGridParam', {
			url : getUrlBuscaPrestacionesCompuestas(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 9,
			page : 1,
		}).trigger("reloadGrid"); 
	}

	function getUrlBuscaPrestacionesCompuestas(){
		var sData = 'SvtPrestaciones';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarPrestacionesCompuestas';
		sData += '&nIdPrestacion=${prestacion.nIdPrestacion}';
		return sData;
	}	

	function eliminarPrestacionCompuesta(idFila){
		idNumFila = idFila;
		var titulo = 'Aviso';

		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarPrestacion", codIdioma)}', titulo, function(r){
			if (r == true){
				getXml("SvtPrestaciones", getUrlEliminaPrestacionCompuesta(idNumFila), function(xml){
					$("#listadoPrestacionCompuesta").jqGrid('setGridParam', {url: getUrlBuscaPrestacionesCompuestas(), mtype: 'POST', datatype: 'xml', rowNum: 9, page: 1,}).trigger("reloadGrid");
					alert('${solemfmt:getLabel("alert.PrestacionEliminada", codIdioma)}');
		       	});	
			}
		});
	}

	function getUrlEliminaPrestacionCompuesta(idNumFila){
		var dataFila = $("#listadoPrestacionCompuesta").getRowData(idNumFila);
		var sData = 'KSI=${sesion.sesIdSesion}';
		sData += '&accion=eliminarPrestacionCompuesta';
		sData += '&nIdPrestacion=${prestacion.nIdPrestacion}';
		sData += '&aIds=' + dataFila.idPrestacion;
		return sData;
    }

	function getUrlEliminaPrestacionesCompuestas(){
		var sData = 'KSI=${sesion.sesIdSesion}';
		sData += '&accion=eliminarPrestacionCompuesta';
		sData += '&nIdPrestacion=${prestacion.nIdPrestacion}';
		sData += '&aIds=' + getidCompuestas();
		return sData;
    }

	function getidCompuestas() {
		var value = '';
		var data = '';
   		var list = $("#listadoPrestacionCompuesta").jqGrid('getGridParam','selarrrow');
   			for( var i=0; i<list.length;i++)
   			{
   				data = $("#listadoPrestacionCompuesta").getRowData(list[i]);
   				if(i == list.length-1)
   					value+= (data.idPrestacion);
   				else
   					value+= (data.idPrestacion) + ',';
   			}
		return value;
    }
    
</script> 

<div id="listadoBusquedaPrestacionCompuesta" class="listado" style="height:auto">
	<table id="listadoPrestacionCompuesta"></table>
	<div id="piePrestacionCompuesta"></div>
</div>