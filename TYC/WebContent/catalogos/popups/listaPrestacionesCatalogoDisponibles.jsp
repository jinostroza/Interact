<%@ page language="java"%>
<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezado.jsp"%>

<script type="text/javascript">
var idPrestacion = 0;
var listadoPrestacionData = null;

	$(function() { 
		
		$("#listadoPrestaciones").jqGrid({
			url : getUrlBuscaPrestacionesDisponibles(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '','${solemfmt:getLabel("label.Codigo", codIdioma)}','${solemfmt:getLabel("label.Nombre", codIdioma)}'],
			colModel : [
						{name : 'idPrestacion',index:'idPrestacion', hidden : true}, 
			 			{name : 'codigo',index:'codigo', width : 320, search : true, resizable : false, sortable : true}, 
			 			{name : 'nombre',index:'nombre',	width : 440, resizable : false,	sortable : true}, 
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
			caption : '${solemfmt:getLabel("label.Prestaciones", codIdioma)}',
			width : 770,
			height : 155,
			scrollOffset : 0,
			rowNum : 5,
			hoverrows : true,  
			loadtext : '${solemfmt:getLabel("label.BuscandoPrestaciones", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronPrestaciones", codIdioma)}',
			hidegrid : false,
			sortname : 'run',  
			sortorder : 'asc',
			page : 1,
			pager: '#piePrestaciones',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : true,
			shrinkToFit : false,
			onSelectRow : function(rowId, status){
				idPrestacion = rowId;
			},
			ondblClickRow: function(){
				cargarPrestaciones();
			},
			loadComplete : function(data){
				var udata = $("#listadoPrestaciones").getGridParam("userData");
	
				if ($("#listadoPrestaciones").getGridParam('datatype') != 'local'){
					if (parseInt(udata.errornum, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.errornum + ")"+ udata.errormsj);
						listadoPrestacionData = null;
					} else {
						listadoPrestacionData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoPrestaciones").getDataIDs();
	
				for ( var i = 0; i < ids.length; i++){
					var idFila = ids[i];
	
					var btnEditar="";
					btnEditar+= "<div id='btnEditar' class='icono' onclick='editar("+idFila+")'>";
					btnEditar+= 	"<img src='general/imagenes/iconos/add-32.png'>";
					btnEditar+= "</div>";
	
					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminar("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";
					$("#listadoPrestaciones").setRowData(ids[i], {act : btnEditar + btnEliminar});
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
			},
				
			multiselect: false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		}); 

		$('#cargaPre').click(function(){
			cargarPrestaciones();
		});

	});

	function cargarPrestaciones(){
		var idSeleccionado = $('#listadoPrestaciones').jqGrid('getGridParam','selrow');
		var fila = $("#listadoPrestaciones").jqGrid('getRowData',idSeleccionado);

		var idPrestacion = fila.idPrestacion;
		var nomPrest = fila.nombre;
		var codPrest = fila.codigo;

		setearCliente(idPrestacion, nomPrest,codPrest);
		cierraDialog();
	}
	
	function getUrlBuscaPrestacionesDisponibles() {
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarPrestacionesCatDispon';
		sData += '&catalogoPrest.nIdCatalogo=${idCatalogo}';
		sData += '&catalogoPrest.sCodPrestacion=' + $('#codigoPrestacion').val();
		sData += '&catalogoPrest.sNomPrestacion=' + $('#nombrePrestacion').val();
		return sData;
	}

	function recargarBusqueda(){
		//console.log("Recargar Busqueda");
		//console.log(getUrlBuscaPrestacionesDisponibles());
		
		$('#listadoPrestaciones').jqGrid('setGridParam', {
			url : getUrlBuscaPrestacionesDisponibles(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 5,
			page : 1,
		}).trigger("reloadGrid");
	} 
 
	function cierraDialog(){
		window.parent.$('#ifrmPrestaciones').dialog('close');
	}

	function setearCliente(idPrestacion, nomPrest, codPrest){
		window.parent.$('#catIdPres').val(idPrestacion);
		window.parent.$('#catNomPres').val(nomPrest);
		window.parent.$('#catCodPres').val(codPrest);
	}
  
</script> 

<div id="listadoPrestacionesDisponibles" style="width: 777px;height: 382px;background:#C4C3A5;"> 
	<form id="formlistadoPrestacionesDisponibles" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="width: 777px;" >
			<div id="titulo01" class="titulo" style="width: 772px;">
				<div id="contieneFlecha" class="contieneFlecha" style="width: 4px;"></div>	
				<div id="contienetitulo" class="contienetitulo" style="width:700px">
					<span>${solemfmt:getLabel("label.BusquedaPrestaciones", codIdioma)}</span> 
				</div>	 
			</div> 
			<div id="detalle03" class="contenido_detalle sombra" style="width: 770px;">
		<!-- 	<input type="hidden" id="presSincatalogoAgrega" value="${idPrestacion}"> -->
			<input type="hidden" id="catIdCatalogo" value="${idCatalogo}">
				<div class="fila" style="width: 772px;">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="codigoPrestacion" class="caja_texto" onkeyup="recargarBusqueda();">
							
						</div>
					</div>  
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="nombrePrestacion" class="caja_texto" onkeyup="recargarBusqueda();">
						</div>
					</div> 
					<div class="espacio5"></div>
				</div> 
			</div>
		</div>
		<div class="espacio5"></div>
		<div id="listadoBusquedaEmpresasClientes" class="listado" style="height: 237px;">
			<table id="listadoPrestaciones"></table>
			<div id="piePrestaciones"></div>
		</div>
		<div style="text-align: center;">
			<div id="btnCargaInst" class="contBtnAceptarPopUp">
				<input type="button" class="darkButton" id="cargaPre" value="${solemfmt:getLabel('boton.Cargar', codIdioma)}" role="button" aria-disabled="false">
			</div>	
			<div id="btnCancelar" class="contBtnCancelarPopUp">
				<input type="button" class="darkButton" id="cancelaEmpresaCli" onclick= "cierraDialog();" value="${solemfmt:getLabel('boton.Cancelar', codIdioma)}" role="button" aria-disabled="false">
			</div>	
				
		</div>
	</form>
</div>