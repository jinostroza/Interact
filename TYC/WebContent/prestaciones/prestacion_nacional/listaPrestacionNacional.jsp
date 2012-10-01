<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	var idPrestacionNac;
	var listadoPrestacionNacData = null;
	var idNumFila = 0;

	$(function() {
		var nomEntidad = "PrestacionNacional";
		
		$('#listadoPrestacionNacional').jqGrid(
		{
			url : getUrlBuscaPrestacionNacional(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '','', '${solemfmt:getLabel("label.Cantidad", codIdioma)}', '${solemfmt:getLabel("label.Codigo", codIdioma)}', '${solemfmt:getLabel("label.Prestacion", codIdioma)}','${solemfmt:getLabel("label.Nomenclador", codIdioma)}','${solemfmt:getLabel("label.FechaIncorporacion", codIdioma)}','' ],
			colModel : [
						{name : 'idPrestacion', index:'idPrestacion', hidden : true},
						{name : 'tabla', index:'tabla', hidden : true},
						{name : 'cantidad', index : 'cantidad', width : 60, resizable : false, sortable : true, align:'right'},
						{name : 'codigo',index:'codigo', width : 82, search : true, resizable : false, sortable : true, align:'right'},
						{name : 'descripcion',index:'descripcion', width : 490, search : true, resizable : false, sortable : true}, 
						{name : 'nomenclador', index:'nomenclador', width: 127, resizable: false, sortable: true},
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
			caption: '${solemfmt:getLabel("label.ListadoPrestacionesNacionales", codIdioma)}',
			width : anchoGrillaGenericoTab,
			height : 310,
			scrollOffset : 0,
			rowNum : 9,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoNomenclador", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronPrestaciones", codIdioma)}',
			hidegrid : false,
			sortname : 'idPrestacion',  
			sortorder : 'ASC',
			page : 1,
			pager: '#piePrestacionNacional',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : true,
			shrinkToFit : false,
			gridview: true,
			onSelectRow : function(rowId, status) 
			{
				idNumFila = rowId;
			},
			loadComplete : function(data) 
			{
				var udata = $("#listadoPrestacionNacional").getGridParam("userData");

				if ($("#listadoPrestacionNacional").getGridParam('datatype') != 'local') {
					if (parseInt(udata.errornum, 10) != 0){
						alert("¡¡¡ATENCION 1!!! \n("+ udata.errornum + ")"+ udata.errormsj);
						listadoPrestacionNacData = null;
					}else{
						listadoPrestacionNacData = data;
					}
				}
			},
			gridComplete: function()
			{
				var ids = $("#listadoPrestacionNacional").getDataIDs();

				for ( var i = 0; i < ids.length; i++)
				{
					var idFila = ids[i];

					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminarPrestacionNacional("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";

					$("#listadoPrestacionNacional").setRowData(ids[i], {act : btnEliminar});
				}
			},
			onPaging: function(pgButton){
				pagUsuario = $("#listadoPrestacionNacional").jqGrid('getGridParam','page');
				totalRegistros = $("#listadoPrestacionNacional").jqGrid('getGridParam','records');
				regPorPagina = $("#listadoPrestacionNacional").jqGrid('getGridParam','rowNum');
				totalPaginas = Math.ceil(totalRegistros/regPorPagina);

				if(totalPaginas<pagUsuario)
				{
					$("listadoPrestacionNacional").setGridParam({page:totalPaginas});				
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

		$('#btnAgregaPrestacionNacional').click(function()
		{
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y+85; 

			var url = 'SvtPrestaciones?KSI=${sesion.sesIdSesion}&accion=cargarPrestacionNacional&nIdPrestacion='+$("#idPrestacion").val();
			var iframe =  $('<iframe id="ifrmPrestacionesNacionales" src="'+url+'"/>');
			
			iframe.dialog({
				autoOpen: true,
				modal: true,
				height: 386,
				width: 795,
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title: 'Lista Nomencladores', //'${solemfmt:getLabel("label.NoEncontraronNomencladores", codIdioma)}'  
			}).width(1005).height(386);

			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
	    });
	    
		$('#btnEliminaPrestacionNacional').click(function()
		{
		    if(validarCheckGrid("listadoPrestacionNacional"))
			{
	    		var titulo = 'Aviso';
	    		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarPrestaciones", codIdioma)}', titulo, function(r){
	    			if (r == true){
	    				getXml("SvtPrestaciones", getUrlEliminaPrestacionesNacionales(), function(xml){
								$("#listadoPrestacionNacional").jqGrid('setGridParam', {url: getUrlBuscaPrestacionNacional(), mtype: 'POST', datatype: 'xml', rowNum: 9, page: 1,}).trigger("reloadGrid");
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

	function recargarBusqueda(){ 
		$('#listadoPrestacionNacional').jqGrid('setGridParam', {
			url : getUrlBuscaPrestacionNacional(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 9,
			page : 1,
		}).trigger("reloadGrid"); 
	}
	
	function getUrlBuscaPrestacionNacional(){
		var sData = 'SvtPrestaciones';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarPrestacionesNomencladorNacional';
		sData += '&nIdPrestacion=${prestacion.nIdPrestacion}';
		return sData;
	}

	function eliminarPrestacionNacional(idFila){
		idNumFila = idFila;
		var titulo = 'Aviso';

		jConfirm('${solemfmt:getLabel("confirm.DeseaEliminarPrestacion", codIdioma)}', titulo, function(r){
			if (r == true){
				getXml("SvtPrestaciones", getUrlEliminaPrestacionNacional(idNumFila), function(xml){
					$("#listadoPrestacionNacional").jqGrid('setGridParam', {url: getUrlBuscaPrestacionNacional(), mtype: 'POST', datatype: 'xml', rowNum: 9, page: 1,}).trigger("reloadGrid");
					alert('${solemfmt:getLabel("alert.PrestacionEliminada", codIdioma)}');
		       	});	
			}
		});
		
	}

	function getUrlEliminaPrestacionNacional(idNumFila){
		var dataFila = $("#listadoPrestacionNacional").getRowData(idNumFila);
		var sData = 'KSI=${sesion.sesIdSesion}';
		sData += '&accion=eliminarPrestacionNacional';
		sData += '&nIdPrestacion=${prestacion.nIdPrestacion}';
		sData += '&aIds=' + dataFila.idPrestacion;
		sData += '&tablas=' + dataFila.tabla;
		return sData;
    }

	function getUrlEliminaPrestacionesNacionales(){
		var sData = 'KSI=${sesion.sesIdSesion}';
		sData += '&accion=eliminarPrestacionNacional';
		sData += '&nIdPrestacion=${prestacion.nIdPrestacion}';
		sData += '&aIds=' + getidPrestacionNacional();
		sData += '&tablas=' + getIdentificadorTablasNomenc();
		return sData;
    }

	function getidPrestacionNacional(){
		var value = '';
		var data = '';
   		var list = $("#listadoPrestacionNacional").jqGrid('getGridParam','selarrrow');
   			for( var i=0; i<list.length;i++)
   			{
   				data = $("#listadoPrestacionNacional").getRowData(list[i]);
   				if(i == list.length-1)
   					value+= (data.idPrestacion);
   				else
   					value+= (data.idPrestacion) + ',';
   			}
		return value;
    }     

	function getIdentificadorTablasNomenc(){
		var value = '';
		var data = '';
   		var list = $("#listadoPrestacionNacional").jqGrid('getGridParam','selarrrow');
   			for( var i=0; i<list.length;i++)
   			{
   				data = $("#listadoPrestacionNacional").getRowData(list[i]);
   				if(i == list.length-1)
   					value+= (data.tabla);
   				else
   					value+= (data.tabla) + ',';
   			}
		return value;
    } 
	
</script>

<div id="listadoBusquedaPrestacionNacional" class="listado" style="height:auto">
	<table id="listadoPrestacionNacional"></table>
	<div id="piePrestacionNacional"></div>
</div>