<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
	
	var listadoData = null;
	var nomEntidad = "Prestacion";
	var nomGrilla = "listadoPrestacion";
	var nomForm = "formFiltrosPrestacion";
	var tituloGrilla = "${solemfmt:getLabel('label.ListadoPrestaciones', codIdioma)}";
	var labelBoton = "${solemfmt:getLabel('boton.Agregar', codIdioma)}";
	var anchoGrilla = anchoGrillaGenerico;
	var altoGrilla = altoGrillaGenerico;
	var numFilasGrilla = 14;
	var avisoConfirmacionEliminacion = "${solemfmt:getLabel('confirm.DeseaEliminarPrestacion', codIdioma)}";
	var avisoExitoEliminacion = "${solemfmt:getLabel('confirm.PrestacionEliminada', codIdioma)}";
	var colNames = ['', '${solemfmt:getLabel("label.Codigo", codIdioma)}', '${solemfmt:getLabel("label.Nombre", codIdioma)}', '${solemfmt:getLabel("label.Descripcion", codIdioma)}','${solemfmt:getLabel("label.Estado", codIdioma)}', ''];
	var colModel = new Array();
	colModel[0] = {name : 'idPrestacion', index:'idPrestacion', hidden : true}; 
	colModel[1] = {name : 'codPrestacion', index : 'codPrestacion', width : 100, resizable : false, sortable : true}; 
	colModel[2] = {name : 'nombre', index:'nombre', width : 290, search : true, resizable : false, sortable : true}; 
	colModel[3] = {name : 'descripcion', index:'descripcion', width: 484, resizable: false, sortable: true};
	colModel[4] = {name : 'estado', index:'estado', width : 80, resizable : false,	sortable : true};
	colModel[5] = {name : 'act', index:'act', index: 'act', width : 25, resizable : false,	sortable : true};
	
	$(function() {
		limpiarFiltros('formFiltrosPrestacion');
		creaGrilla();
		buscarPrestaciones();
		creaBoton(nomEntidad, "A");
			    
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

		$('#btnFiltrar').click(function()
		{
			limpiarFiltros('formFiltrosPrestacion');
			buscarPrestaciones();
		});

		$('#btnAgregaPrestacion').click(function()
		{
			agregarEntidad(nomForm, getUrlAgregaPrestacion());
		}); 
	});

	function buscarPrestaciones() 
	{
		buscarListaEntidades("listadoPrestacion", getUrlBuscaPrestacion());
	}

	function getUrlBuscaPrestacion()
	{
		var sData = "SvtPrestaciones";
		sData+= "?KSI=${sesion.sesIdSesion}";
		sData += "&accion=buscarPrestaciones";
		sData += "&prestacion.sCodPrestacion="+$('#presCodigo').val();			
		sData += "&prestacion.sNombre="+$('#presPrestacion').val();			
		sData += "&prestacion.sCodTipoPrestacion="+$('#presTipo').val();				
		sData += "&prestacion.sCodEstado="+$('#presEstado').val();
		return sData;
	}

	function getUrlAgregaPrestacion()
	{
		var sData = "SvtPrestaciones";
		sData+= "?KSI=${sesion.sesIdSesion}";
		sData += '&accion=agregarPrestacion';
		sData += '&indRuta=S';
		return sData;
	}

	function getUrlCargaPrestacion(fila)
	{
	   	var sData = 'SvtPrestaciones';
	   	sData+= "?KSI=${sesion.sesIdSesion}";
	  	sData += '&accion=cargarPrestacion';
	  	sData += '&nIdPrestacion=' + fila.idPrestacion;
		sData += '&indRuta=S';
		return sData;
	}

	function getUrlEliminaPrestacion(fila)
	{
		var sData = "SvtPrestaciones";
		sData+= "?KSI=${sesion.sesIdSesion}";
		sData += '&accion=eliminarPrestacion';
		sData += '&nIdPrestacion=' + fila.idPrestacion;
		sData += '&codPrestacion=' + fila.codPrestacion;
		return sData;
	}

	function borderBottomRadius(div)
	{
		$("#"+div+" .titulo").addClass("borderBottomRadius");
		$(".ui-jqgrid-bdiv").animate({height: "482px"}, 400 ).trigger("resize");// Animate Grilla
	}
</script>

<div class="detalle">
	<div id="filtrosBusquedaPrestaciones" class="filtros">
		<div id="tituloFiltros" class="titulo"> 
			<div id="contieneFlecha" class="contieneFlecha">
				<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">
			</div>
			<div id="contienetitulo" class="contienetitulo">
				<span>${solemfmt:getLabel("label.BusquedaPrestaciones", codIdioma)}</span>
			</div>
			
			<input id="btnFiltrar" class="boton botonFiltro" type="button" value="${solemfmt:getLabel('boton.Limpiar', codIdioma)}" role="button" aria-disabled="false">
		</div>
		
		<div id="contenidoFiltros" class="contenido_detalle sombra">
			<form id="formFiltrosPrestacion" method="post">
				<div class="fila">
					<div class="campo" style="width: 175px">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo_small">
							<input type="text" id="presCodigo" onkeyup="buscarPrestaciones();">
						</div>
					</div>
		
					<div class="campo" style="width: 365px">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo" style="width: 380px">
							<input type="text" id="presPrestacion" onkeyup="buscarPrestaciones();">
						</div>
					</div>
		
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Tipo", codIdioma)}: </span>
						</div>
						<div class="valor_campo"> 
							<select id="presTipo" onChange="buscarPrestaciones();">
								<option value="" selected>${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaTipoPrestacion}" var="listaTipos">
									<option value="${listaTipos.parCodParametro01}" >${listaTipos.parDesLargo01}</option>
								</c:forEach>
							</select> 
						</div>
					</div>
					
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<select id="presEstado" onChange="buscarPrestaciones();">
									<option value="" selected>${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaEstadoPrestacion}" var="listaEstados">
									<option value="${listaEstados.parCodParametro01}">${listaEstados.parDesLargo01}</option>
								</c:forEach>
							</select>
						</div>
					</div>						
				</div>
				<div class="espacio5"></div>
			</form>
		</div>
	</div> 
	<div class="espacio5"></div>
	<div id="listadoBusquedaPrestacion" class="listado">
		<table id="listadoPrestacion"></table>
		<div id="piePrestacion" class="pie"></div>
	</div>
</div> 