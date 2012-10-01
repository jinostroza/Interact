<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">

	var listadoData = null;
	var nomEntidad = "Convenio";
	var nomGrilla = "listadoConvenio";
	var nomForm = "formFiltrosConvenio";
	var tituloGrilla = "${solemfmt:getLabel('label.ListadoConvenios', codIdioma)}";
	var anchoGrilla = anchoGrillaGenerico;
	var altoGrilla = 390;
	var numFilasGrilla = 12;
	var avisoConfirmacionEliminacion = "${solemfmt:getLabel('confirm.DeseaEliminarConvenio', codIdioma)}";
	var avisoExitoEliminacion = "${solemfmt:getLabel('confirm.ConvenioEliminado', codIdioma)}";
	var colNames = [ '', '${solemfmt:getLabel("label.Codigo", codIdioma)}', '${solemfmt:getLabel("label.Prestador", codIdioma)}', '${solemfmt:getLabel("label.Nombre", codIdioma)}', '${solemfmt:getLabel("label.Clase", codIdioma)}', '${solemfmt:getLabel("label.SubClase", codIdioma)}','${solemfmt:getLabel("label.Estado", codIdioma)}', '' ];
	var colModel = new Array();
	colModel[0] = {name : 'idConvenio', index:'idConvenio', hidden : true}; 
	colModel[1] = {name : 'codigo', index : 'cod', width : 100, resizable : false, sortable : true,  align:'right'}; 
	colModel[2] = {name : 'inst',index:'inst', width : 289, search : true, resizable : false, sortable : true}; 
	colModel[3] = {name : 'nombre', index:'nombre', width: 300, resizable: false, sortable: true};
	colModel[4] = {name : 'clase',index:'clase',	width : 100, resizable : false,	sortable : true};
	colModel[5] = {name : 'subClase',index:'subClase',	width : 90, resizable : false,	sortable : true};
	colModel[6] = {name : 'estado',index:'est', width : 70, resizable : false,	sortable : true};
	colModel[7] = {name : 'act',index:'act', index: 'act', width : 20, resizable : false,	sortable : true};
	
	$(function() {
		limpiarFiltros('formFiltrosConvenio'); 
		creaGrilla();
		buscarConvenios();
		creaBoton(nomEntidad, "A");
	    
	    $("#tituloFiltros img").click(function(event)
	    {
			var id = $(this).attr("id");
			var div = $(this).parent().parent().parent().attr("id");

			if(id=="flechaUp"){
				$("#"+div+" .contenido_detalle").slideUp();
				$(this).attr("id","flechaDown");	
				$(this).attr("src","${rutaImg}template/"+$(this).attr("imgDown"));
				var t = setTimeout("borderBottomRadius('"+div+"')",400);
				$(this).parent().parent().removeClass("borderBottomRadius");
			}else{
				$(".ui-jqgrid-bdiv").css('height','372px');
				$(this).attr("id","flechaUp");	
				$(this).attr("src","${rutaImg}template/"+$(this).attr("imgUp")); 
				$(this).parent().parent().removeClass("borderBottomRadius");
				$("#"+div+" .contenido_detalle").slideDown();  
			}
		});

		$('#btnFiltrar').click(function()
		{
			limpiarFiltros('formFiltrosConvenio');
			buscarConvenios();
		});

		$('#btnAgregaConvenio').click(function()
		{
			agregarEntidad(nomForm, getUrlAgregaConvenio());
		});

	    $('input#buscaInstFiltroConv').click(function()
	    {
			$('#titlePopup').parent().remove();
			$('iframe').remove();
			var myDiv = document.getElementById('contenidoPrincipal');
			var xDiv = getDimensions(myDiv).x+104;
			var yDiv = getDimensions(myDiv).y+85; 

			var url = "SvtConvenios?KSI=${sesion.sesIdSesion}&accion=cargaEmpresasInst";
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
				title:'${solemfmt:getLabel("label.NoSeEncontraronConvenios", codIdioma)}'  
			}).width(777).height(382);
			$('.ui-dialog-buttonpane').find('button:contains("Cancelar")').removeClass().addClass('darkButton');
			$('.ui-dialog-buttonpane').find('button:contains("Agregar")').removeClass().addClass('darkButton');
			$('.ui-dialog-titlebar').css("display","none"); 
		});
	});

	function buscarConvenios() 
	{
		buscarListaEntidades("listadoConvenio", getUrlBuscaConvenio());
	}
	 
	function getUrlBuscaConvenio() 
	{
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarConvenios';
		sData += '&convenio.sCodConvenio='+$('#conCodFiltro').val();
		sData += '&convenio.nIdPrestador='+$('#conIdPres').val();
		sData += '&convenio.sNomPrestador='+$('#conNomPres').val();
		sData += '&convenio.sNombre='+$('#conNombreFiltro').val();
		sData += '&convenio.sCodClase='+$('#conClaseFiltro').val();
		sData += '&convenio.sCodTipo='+$('#conTipoFiltro').val();
		sData += '&convenio.sCodEstado='+$('#conEstadoFiltro').val();
		return sData;
	}

	function getUrlAgregaConvenio()
	{
		var sData = "SvtConvenios";
		sData += "?KSI=${sesion.sesIdSesion}";
		sData += "&accion=agregarConvenio";
		sData += "&indRuta=S";
		return sData;   
	} 

    function getUrlCargaConvenio(fila) 
    {      
    	var sData = 'SvtConvenios';
        sData += '?KSI=${sesion.sesIdSesion}';
      	sData += '&accion=cargarConvenio';
      	sData += '&idConvenio=' + fila.idConvenio;
      	sData += '&indRuta=S';
    	return sData;
    }

    function getUrlEliminaConvenio(fila)
    {
		var sData = "SvtConvenios"; 
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=eliminarConvenio';
		sData += '&idConvenio=' + fila.idConvenio;
		return sData;
    }
    
</script>

<div class="detalle">
	<div id="filtrosBusquedaConvenios" class="filtros">
		<div id="tituloFiltros" class="titulo"> 
			<div id="contieneFlecha" class="contieneFlecha">
				<img id="flechaUp" class="flecha" alt="" imgUp="flechaUp.png" imgDown="flechaDown.png" src="${rutaImg}template/flechaUp.png">
			</div>
			<div id="contienetitulo" class="contienetitulo">
				<span>${solemfmt:getLabel("label.BusquedaConvenios", codIdioma)}</span>
			</div>		
			
			<input id="btnFiltrar" class="boton botonFiltro" type="button" value="${solemfmt:getLabel('boton.Limpiar', codIdioma)}" role="button" aria-disabled="false">
		</div>
	
		<div id="contenidoFiltros" class="contenido_detalle sombra">
			<form id="formFiltrosConvenio" method="post">
				<div class="fila">
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<input type="text" id="conCodFiltro" onkeyup="buscarConvenios();" maxlength="19">
						</div>
					</div>
		
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Prestador", codIdioma)}: </span>
						</div>
						<div class="valor_campo_small">
							<input type="hidden" id="conIdPres">
							<input type="text" id="conNomPres" onkeyup="buscarConvenios();">
						</div>
						<div class="contieneButtonLupa">
							 <input class="buttonLupa" type="button" id="buscaInstFiltroConv" value="" role="button" aria-disabled="false">
						</div>
					</div>
		
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<input type="text" id="conNombreFiltro" onkeyup="buscarConvenios();">
						</div>
					</div>
					
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Clase", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<select id="conClaseFiltro" onChange="buscarConvenios();">
									<option value="" selected>${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaClases}" var="listaClases">
									<option value="${listaClases.parCodParametro01}">${listaClases.parDesLargo01}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="fila">
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Tipo", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<select id="conTipoFiltro" onChange="buscarConvenios();">
								<option value="" selected>${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaTipos}" var="listaTipos">
									<option value="${listaTipos.parCodParametro01}">${listaTipos.parDesLargo01}</option>
								</c:forEach>
							</select>
						</div>
					</div>
		
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Estado", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<select id="conEstadoFiltro" onChange="buscarConvenios();">
									<option value="" selected>${solemfmt:getLabel("label.Seleccione", codIdioma)}</option>
								<c:forEach items="${requestScope.listaEstadosConvenio}" var="listaEstados">
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
	<div id="listadoBusquedaConvenio" class="listado">
		<table id="listadoConvenio"></table>
		<div id="pieConvenio" class="pie"></div>
	</div>
</div>