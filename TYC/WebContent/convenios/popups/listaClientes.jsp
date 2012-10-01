<%@ page language="java"%>
<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezado.jsp"%>

<script type="text/javascript">
var idEmpresaCli = 0;
var listadoEmpresasCliData = null;

	$(function() { 
		
		$("#listadoEmpresasCli").jqGrid({
			url : getUrlBuscaEmpresasCli(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '','${solemfmt:getLabel("label.Run", codIdioma)}','${solemfmt:getLabel("label.Nombre", codIdioma)}'],
			colModel : [
						{name : 'idEmpresaCli',index:'id', hidden : true}, 
						{name : 'rut',index:'rut',	width : 230, resizable : false,	sortable : true}, 
			 			{name : 'nombre',index:'nombre', width : 530, search : true, resizable : false, sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idEmpresaCli',
				userdata : "solemdata"
			},
			caption : '${solemfmt:getLabel("label.Clientes", codIdioma)}',
			width : 770,
			height : 155,
			scrollOffset : 0,
			rowNum : 0,
			hoverrows : true,  
			loadtext : '${solemfmt:getLabel("label.BuscandoClientes", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronClientes", codIdioma)}',
			hidegrid : false,
			sortname : 'run',  
			sortorder : 'asc',
			page : 1,
			pager: '#pieEmpresasCli',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			onSelectRow : function(rowId, status){
				idEmpresaCli = rowId;
			},
			ondblClickRow: function(){ 
				cargarCliente();
			},
			loadComplete : function(data){
				var udata = $("#listadoEmpresasCli").getGridParam("userData");
	
				if ($("#listadoEmpresasCli").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoEmpresasCliData = null;
					} else {
						listadoEmpresasCliData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoEmpresasCli").getDataIDs();
	
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
					$("#listadoEmpresasCli").setRowData(ids[i], {act : btnEditar + btnEliminar});
				}
			},	
			multiselect: false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		}); 

		$('#cargaCli').click(function(){
			cargarCliente();
		});  
	});


	function cargarCliente(){
		var idSeleccionado = $('#listadoEmpresasCli').jqGrid('getGridParam','selrow');
		var fila = $("#listadoEmpresasCli").jqGrid('getRowData',idSeleccionado);

		var idEmpresaCli = fila.idEmpresaCli;
		var nomCliente = fila.nombre;

		setearCliente(idEmpresaCli, nomCliente);
		cierraDialog();
	}
	
	function getUrlBuscaEmpresasCli(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarEmpresasCli';
		sData += '&empresa.sNombreFantasia=' + $('#nombreEmpCli').val();
		sData += '&empresa.sRut=' + $('#rutEmpCli').val();
		return sData;
	}

	function recargarBusqueda(){
		console.log("Recargar Busqueda");
		console.log(getUrlBuscaEmpresasCli());
		
		$('#listadoEmpresasCli').jqGrid('setGridParam', {
			url : getUrlBuscaEmpresasCli(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 10,
			page : 1,
		}).trigger("reloadGrid");
	} 
 
	function cierraDialog(){
		window.parent.parent.$('#ifrmConvenio').css("height","307px");
		window.parent.$('#ifrmClientes').dialog('close');
	}

	function setearCliente(idEmpresaCli, nomCliente){
		window.parent.$('#conIdCliente').val(idEmpresaCli);
		window.parent.$('#conNomCliente').val(nomCliente);
	}
  
</script> 

<div id="listadoEmpresaCli" style="width: 777px;height: 382px;background:#C4C3A5;"> 
	<form id="formAgregaEmpresaInst" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="width: 777px;" >
			<div id="titulo01" class="titulo" style="width: 772px;">
				<div id="contieneFlecha" class="contieneFlecha" style="width: 4px;">
				</div>
				<div id="contienetitulo" class="contienetitulo" style="width:700px">
					<span>${solemfmt:getLabel("label.BusquedaClientes", codIdioma)}</span>  
				</div>	 
			</div> 
			<div id="detalle01" class="contenido_detalle sombra" style="width: 770px;">
				<div class="fila" style="width: 772px;">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="nombreEmpCli" class="caja_texto" onkeyup="recargarBusqueda();">
							<input type="hidden" id="idEmpCli" value="${empresa.nIdEmpresa}">
						</div>
					</div>  
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Run", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="rutEmpCli" class="caja_texto" onkeyup="recargarBusqueda();">
						</div>
					</div> 
				</div> 
				<div class="espacio5"></div>
			</div>
		</div>

		<div class="espacio5"></div>
			
		<div id="listadoBusquedaEmpresasClientes" class="listado" style="height: 237px;">
			<table id="listadoEmpresasCli"></table>
			<div id="pieEmpresasCli"></div>
		</div>
		
		<div style="text-align: center;">
			<div id="btnCargaInst" class="contBtnAceptarPopUp">
				<input type="button" class="darkButton" id="cargaCli" value="${solemfmt:getLabel('boton.Cargar', codIdioma)}" role="button" aria-disabled="false">
			</div>		
			<div id="btnCancelar" class="contBtnCancelarPopUp">
				<input type="button" class="darkButton" id="cancelaEmpresaCli" onclick= "cierraDialog();" value="${solemfmt:getLabel('boton.Cancelar', codIdioma)}" role="button" aria-disabled="false">
			</div>	
		</div>
		  
	</form>
</div>