<%@ page language="java"%>
<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezado.jsp"%>

<script type="text/javascript">
var idEmpresaInst = 0;
var listadoEmpresasInstData = null;

	$(function() { 
		
		$("#listadoEmpresasInst").jqGrid({
			url : getUrlBuscaEmpresasInst(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '','${solemfmt:getLabel("label.Run", codIdioma)}','${solemfmt:getLabel("label.Nombre", codIdioma)}'],
			colModel : [
						{name : 'idEmpresaInst',index:'id', hidden : true},
						{name : 'rut',index:'rut',	width : 217, resizable : false,	sortable : true}, 
			 			{name : 'nombre',index:'nombre', width : 543, search : true, resizable : false, sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idEmpresaInst',
				userdata : "solemdata"
			},
			caption : '${solemfmt:getLabel("label.Prestadores", codIdioma)}',
			width : 770,
			height : 155,
			scrollOffset : scrollOffSet,
			rowNum : 0,
			hoverrows : true,  
			loadtext : '${solemfmt:getLabel("label.BuscandoInstituciones", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronInstituciones", codIdioma)}',
			hidegrid : false,
			sortname : 'run',  
			sortorder : 'asc',
			page : 1,
			pager: '#pieEmpresaDisponibles',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			onSelectRow : function(rowId, status){
				idEmpresaInst = rowId;
			},
			ondblClickRow: function(){
				cargarInstitucion();
			},
			loadComplete : function(data){
				var udata = $("#listadoEmpresasInst").getGridParam("userData");
	
				if ($("#listadoEmpresasInst").getGridParam('datatype') != 'local'){
					if (parseInt(udata.errornum, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.errornum + ")"+ udata.errormsj);
						listadoEmpresasInstData = null;
					} else {
						listadoEmpresasInstData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoEmpresasInst").getDataIDs();
	
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
					$("#listadoEmpresasInst").setRowData(ids[i], {act : btnEditar + btnEliminar});
				}
			},	
			multiselect: false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		}); 

		$('#cargaInst').click(function(){
			cargarInstitucion();
		});
	});


	function cargarInstitucion(){
		var idSeleccionado = $('#listadoEmpresasInst').jqGrid('getGridParam','selrow');
		var fila = $("#listadoEmpresasInst").jqGrid('getRowData',idSeleccionado);

		var idEmpresaInst = fila.idEmpresaInst;
		var nomInstitucion = fila.nombre;

		setearInstitucion(idEmpresaInst, nomInstitucion);
		cierraDialog();
	}
	
	function getUrlBuscaEmpresasInst() {
		var sData = 'SvtCatalogos';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarEmpresasInst';
		sData += '&empresa.sNombreFantasia=' + $('#nombreEmpInst').val();
		sData += '&empresa.sRut=' + $('#rutEmpInst').val();
		return sData;
	}

	function recargarBusqueda(){
		//console.log("Recargar Busqueda");
		//console.log(getUrlBuscaEmpresasInst());
		
		$('#listadoEmpresasInst').jqGrid('setGridParam', {
			url : getUrlBuscaEmpresasInst(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 10,
			page : 1,
		}).trigger("reloadGrid");
	} 
 
	function cierraDialog(){
		window.parent.buscarCatalogos();
		window.parent.parent.$('#ifrmCatalogo').css("height","209px");	 
		window.parent.$('#ifrmInstituciones').dialog('close'); 
	}

	function setearInstitucion(idEmpresaInst, nomInstitucion)
	{
		window.parent.$('#catIdPrestador').val(idEmpresaInst);
		window.parent.$('#catNombrePrestador').val(nomInstitucion);
	}
  
</script> 

<div id="detalleEmpresaInst" style="width: 777px;height: 382px;background:#C4C3A5;"> 
	<form id="formAgregaEmpresaInst" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="width: 777px;" >
			<div id="titulo01" class="titulo" style="width: 772px;">
				<div id="contieneFlecha" class="contieneFlecha" style="width: 4px;"></div>
				<div id="contienetitulo" class="contienetitulo" style="width:700px">
					<span>${solemfmt:getLabel("label.BusquedaPrestadores", codIdioma)}</span>
				</div>	 
			</div> 
			<div id="detalle01" class="contenido_detalle sombra" style="width: 770px;">
				<div class="fila" style="width: 772px;">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Run", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="rutEmpInst" class="caja_texto" onkeyup="recargarBusqueda();">
						</div>
					</div> 
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="nombreEmpInst" class="caja_texto" onkeyup="recargarBusqueda();">
							<input type="hidden" id="idEmpInst" value="${empresa.nIdEmpresa}">
						</div>
					</div>  
					<div class="espacio5"></div>
				</div> 
			</div>
		</div>
		
		<div class="espacio5"></div>
			
		<div id="listadoBusquedaEmpresasDisponibles" class="listado" style="height: 237px;">
			<table id="listadoEmpresasInst"></table>
			<div id="pieEmpresaDisponibles"></div>
		</div>
		
		<div style="align: center;">
			<div id="btnCargaInst" class="contBtnAceptarPopUp">
				<input type="button" class="darkButton" id="cargaInst" value="${solemfmt:getLabel('boton.Cargar', codIdioma)}" role="button" aria-disabled="false">
			</div>
			<div id="btnCancelar" class="contBtnCancelarPopUp">
				<input type="button" class="darkButton" id="cancelaEmpresaInst" onclick= "cierraDialog();" value="${solemfmt:getLabel('boton.Cancelar', codIdioma)}" role="button" aria-disabled="false">
			</div>	
		</div>
		  
	</form>
</div>