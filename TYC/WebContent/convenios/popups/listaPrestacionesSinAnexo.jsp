<%@ page language="java"%>
<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezado.jsp"%>

<script type="text/javascript">
var idPrestacionSinAnexo = 0;
var listadoPrestacionesSinAnexoData = null;

	$(function() {
		
		$("#listadoPrestacionesSinAnexo").jqGrid({
			url : getUrlBuscaPrestacionesSinAnexo(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '','${solemfmt:getLabel("label.Codigo", codIdioma)}','${solemfmt:getLabel("label.Catalogo", codIdioma)}','${solemfmt:getLabel("label.Prestacion", codIdioma)}'],
			colModel : [
						{name : 'idPrestacionSinAnexo',index:'id', hidden : true},
						{name : 'codigo',index:'cod', width : 115, search : true, resizable : false, sortable : true},  
						{name : 'catalogo',index:'cat', width : 320, search : true, resizable : false, sortable : true}, 
			 			{name : 'nombre',index:'nombre', width : 320, resizable : false,	sortable : true}
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idPrestacionSinAnexo',
				userdata : "solemdata"
			},
			caption : '${solemfmt:getLabel("label.ListadoPrestaciones", codIdioma)}',
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
			pager: '#piePrestacionesSinAnexo',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : true,
			shrinkToFit : false,
			onSelectRow : function(rowId, status){
				idPrestacionSinAnexo = rowId;
			},
			ondblClickRow: function(){ 
				cargarPrestacion();
			},
			loadComplete : function(data){	
				var udata = $("#listadoPrestacionesSinAnexo").getGridParam("userData");
	
				if ($("#listadoPrestacionesSinAnexo").getGridParam('datatype') != 'local'){
					if (parseInt(udata.errornum, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.errornum + ")"+ udata.errormsj);
						listadoPrestacionesSinAnexoData = null;
					} else {
						listadoPrestacionesSinAnexoData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoPrestacionesSinAnexo").getDataIDs();
	
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
					$("#listadoPrestacionesSinAnexo").setRowData(ids[i], {act : btnEditar + btnEliminar});
				}
			},	
			multiselect: false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		}); 

		$('input#cargaPres').click(function(){
			window.parent.parent.$('#ifrmPrestacionAnexo').css("height","144px");
			cargarPrestacion();
		}); 
	});


	function cargarPrestacion(){
		var idSeleccionado = $('#listadoPrestacionesSinAnexo').jqGrid('getGridParam','selrow');
		var fila = $("#listadoPrestacionesSinAnexo").jqGrid('getRowData',idSeleccionado);

		var idPrestacionSinAnexo = fila.idPrestacionSinAnexo;
		var nomPrestacion = fila.nombre;

		setearPrestacion(idPrestacionSinAnexo, nomPrestacion);
		cierraDialog();
	}
	
	function getUrlBuscaPrestacionesSinAnexo() {
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarPrestacionesSinAnexo';
		sData += '&catalogoPrest.sCodPrestacion=' + $('#codPresSinAnexo').val();
		sData += '&catalogoPrest.sNomPrestacion=' + $('#nombrePresSinAnexo').val();
		sData += '&idAnexo=' + $('#presSinAnexoIdAnexoAgrega').val();
		sData += '&idConvenio=' + ${idConvenio}; 
		return sData;
	}

	function recargarBusqueda(){
		console.log("Recargar Busqueda");
		console.log(getUrlBuscaPrestacionesSinAnexo());
		
		$('#listadoPrestacionesSinAnexo').jqGrid('setGridParam', {
			url : getUrlBuscaPrestacionesSinAnexo(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 10,
			page : 1,
		}).trigger("reloadGrid");
	} 
 
	function cierraDialog(){
		window.parent.parent.$('#ifrmPrestacionAnexo').css("height","144px");
		window.parent.$('#ifrmPrestaciones').dialog('close');
	}

	function setearPrestacion(idPrestacionSinAnexo, nomPrestacion){
		window.parent.$('#presAnexoIdPres').val(idPrestacionSinAnexo);
		window.parent.$('#presAnexoNomPres').val(nomPrestacion);
	}
  
</script> 

<div id="listadoPrestacionSinAnexo" style="width: 777px;height: 382px;background:#C4C3A5;"> 
	<form id="formAgregaEmpresaInst" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="width: 777px;" >
			<div id="titulo01" class="titulo" style="width: 772px;"> 
				<div id="contieneFlecha" class="contieneFlecha" style="width: 4px;"></div>
				<div id="contienetitulo" class="contienetitulo" style="width:700px">
					<span>${solemfmt:getLabel("label.BusquedaPrestaciones", codIdioma)}</span>  
				</div>	 
			</div> 
			<div id="detalle01" class="contenido_detalle sombra" style="width: 770px;">
				<input type="hidden" id="presSinAnexoIdAnexoAgrega" value="${idAnexo}">
				<div class="fila" style="width: 772px;">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="codPresSinAnexo" class="caja_texto" onkeyup="recargarBusqueda();">
						</div>
					</div>  
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="nombrePresSinAnexo" class="caja_texto" onkeyup="recargarBusqueda();">
							<input type="hidden" id="idPresSinAnexo" value="">
						</div>
					</div> 
				</div> 
				<div class="espacio5"></div>
			</div>
		</div>

		<div class="espacio5"></div>
			
		<div id="listadoBusquedaPrestacionesSinAnexo" class="listado" style="height: 237px;">
			<table id="listadoPrestacionesSinAnexo"></table>
			<div id="piePrestacionesSinAnexo"></div>
		</div>
		
		<div style="text-align: center;">
			<div id="btnCargaInst" class="contBtnAceptarPopUp">
				<input type="button" class="darkButton" id="cargaPres" value="${solemfmt:getLabel('boton.Cargar', codIdioma)}" role="button" aria-disabled="false">
			</div>
			<div id="btnCancelar" class="contBtnCancelarPopUp">
				<input type="button" class="darkButton" id="cancelaPrestacionSinAnexo" onclick= "cierraDialog();" value="${solemfmt:getLabel('boton.Cancelar', codIdioma)}" role="button" aria-disabled="false">
			</div>			
		</div> 
	</form>
</div>