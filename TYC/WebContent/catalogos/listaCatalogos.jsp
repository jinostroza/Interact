<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">

	var listadoData = null;
	var nomEntidad = "Catalogo";
	var nomGrilla = "listadoCatalogo";
	var nomForm = "formFiltrosCatalogo";
	var tituloGrilla = '${solemfmt:getLabel("label.ListadoCatalogos", codIdioma)}';
	var anchoGrilla = anchoGrillaGenerico;
	var altoGrilla = 390;
	var numFilasGrilla = 14;
	var avisoConfirmacionEliminacion = "${solemfmt:getLabel('confirm.DeseaEliminarCatalogo', codIdioma)}";
	var avisoExitoEliminacion = "${solemfmt:getLabel('confirm.CatalogoEliminado', codIdioma)}";
	var colNames = ['', '${solemfmt:getLabel("label.Codigo", codIdioma)}', '${solemfmt:getLabel("label.Nombre", codIdioma)}','${solemfmt:getLabel("label.Prestador", codIdioma)}', '${solemfmt:getLabel("label.Estado", codIdioma)}', ''];
	var colModel = new Array();
	colModel[0] = {name : 'idCatalogo', index:'idCatalogo', hidden : true};
	colModel[1] = {name : 'codigo', index: 'codigo', width : 100, resizable : false, sortable : true};
	colModel[2] = {name : 'nombre', index:'nombre', width: 360, resizable: false, sortable: true};
	colModel[3] = {name : 'institucion',index:'institucion', width : 395, search : true, resizable : false, sortable : true};  
	colModel[4] = {name : 'estado', index:'estado', width : 99, resizable : false,	sortable : true};
	colModel[5] = {name : 'act', index:'act', index: 'act', width : 25, resizable : false,	sortable : true};
	
	$(function() {
		limpiarFiltros('formFiltrosCatalogo');
		creaGrilla();
		buscarCatalogos();
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
			  //$(".ui-jqgrid-bdiv").animate({height: '303px'}, 50);
				$(".ui-jqgrid-bdiv").css('height','372px');
				$(this).attr("id","flechaUp");	
				$(this).attr("src","${rutaImg}template/"+$(this).attr("imgUp")); 
				$(this).parent().parent().removeClass("borderBottomRadius");
				$("#"+div+" .contenido_detalle").slideDown();  
			}
		}); 

		$('#btnFiltrar').click(function()
				{
					limpiarFiltros('formFiltrosCatalogo');
					buscarCatalogos();
				});

		$('#btnAgregaCatalogo').click(function(){
			agregarEntidad(nomForm, getUrlAgregaCatalogo());
		});
		
		$('input#catbuscaFiltInstitucion').click(function()
		{
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y+85; 
		
			var url = "SvtCatalogos?KSI=${sesion.sesIdSesion}&accion=cargaEmpresasInst";
			var iframe =  $('<iframe id="ifrmInstituciones" src="'+url+'"/>'); 
			
			iframe.dialog({
				autoOpen: true,
				modal: true,
				height: 382,
				width: 777,
				draggable: true,
				resizable: false,
				autoResize: false,
				position: [xDiv,yDiv],
				title:"Empresas Disponibles"  
			}).width(777).height(382);
			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
		});
		
	});

	function buscarCatalogos() 
	{
		buscarListaEntidades("listadoCatalogo", getUrlBuscaCatalogo());
	}
/*
	function borderBottomRadius(div)
	{
		$("#"+div+" .titulo").addClass("borderBottomRadius");
		$(".ui-jqgrid-bdiv").animate({height: "482px"}, 400 ).trigger("resize");// Animate Grilla
	}
*/
	function getUrlBuscaCatalogo() 
	{
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarCatalogos';
		sData += '&catalogo.sCodCatalogo='+$('#catCodigo').val();
		sData += '&catalogo.nIdPrestador=' +$('#catIdPrestador').val();
		sData += '&catalogo.sNombreFantasia=' +$('#catNombrePrestador').val();
		sData += '&catalogo.sNombre='+$('#catNombre').val();
		sData += '&catalogo.sCodEstado='+$('#catEstado').val();
		return sData;
	}

	function getUrlAgregaCatalogo()
	{
		var sData = "SvtCatalogos";
		sData += "?KSI=${sesion.sesIdSesion}";
		sData += '&accion=agregarCatalogo';
		sData += '&indRuta=S';
		return sData;
	}

	function getUrlCargaCatalogo(fila)
	{
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=cargarCatalogo';
		sData += '&idCatalogo=' + fila.idCatalogo;
		sData += '&indRuta=S';
		return sData;
	}

    function getUrlEliminaCatalogo(fila)
    {
    	var sData = 'SvtCatalogos';
		sData += "?KSI=${sesion.sesIdSesion}";
		sData += '&accion=eliminarCatalogo';
		sData += '&idCatalogo=' + fila.idCatalogo;
		return sData;
    }
</script>

<div class="detalle">
	<div id="filtrosBusquedacatalogos" class="filtros">
		<div id="tituloFiltros" class="titulo"> 
			<div id="contieneFlecha" class="contieneFlecha">
				<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">
			</div>
			<div id="contienetitulo" class="contienetitulo">
				<span>${solemfmt:getLabel("label.BusquedaCatalogos", codIdioma)}</span>
			</div> 
			
			<input id="btnFiltrar" class="boton botonFiltro" type="button" value="${solemfmt:getLabel('boton.Limpiar', codIdioma)}" role="button" aria-disabled="false">
		</div>
		<div id="contenidoFiltros" class="contenido_detalle sombra">
			<form id="formFiltrosCatalogo" method="post">
				<div class="fila">
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<input type="text" id="catCodigo" onkeyup="buscarCatalogos();"maxlength="19">
						</div>
					</div>
		
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Prestador", codIdioma)}: </span>
						</div>
						<div class="valor_campo_small">
							<input type="hidden" id="catIdPrestador">
							<input type="text" id="catNombrePrestador" onkeyup="buscarCatalogos();">
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="catbuscaFiltInstitucion" value="" title="" role="button" aria-disabled="false">
						</div>
					</div>

					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<input type="text" id="catNombre" onkeyup="buscarCatalogos();">
						</div>
					</div>
					
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<select id="catEstado" class="inactivo" onChange="buscarCatalogos();"> 
								<option value="" selected>${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listacatalogo}" var="tipocatalogo">
									<option value="${tipocatalogo.parCodParametro01}">${tipocatalogo.parDesLargo01}</option>
								</c:forEach>
							</select>
						</div>
					</div>	
					<div class="espacio5"></div>					
				</div>
			</form>
		</div>
	</div> 
	<div class="espacio5"></div>
	<div id="listadoBusquedaCatalogo" class="listado">
		<table id="listadoCatalogo"></table>
		<div id="pieCatalogo" class="pie"></div>
	</div>
</div>