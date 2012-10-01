<%@ page language="java"%>
<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezado.jsp"%>

<script type="text/javascript">
var idResponsable = 0;
var listadoResponsablesData = null;

	$(function() { 
		
		$("#listadoResponsables").jqGrid({
			url : getUrlBuscaResponsables(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '','${solemfmt:getLabel("label.Nombre", codIdioma)}','${solemfmt:getLabel("label.Empresa", codIdioma)}','${solemfmt:getLabel("label.Cargo", codIdioma)}'],
			colModel : [
						{name : 'idResponsable',index:'id', hidden : true}, 
			 			{name : 'nombre',index:'nombre', width : 300, search : true, resizable : false, sortable : true},
			 			{name : 'empresa',index:'emp',	width : 230, resizable : false,	sortable : true},
			 			{name : 'cargo',index:'cargo',	width : 225, resizable : false,	sortable : true},  
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idResponsable',
				userdata : "solemdata"
			},
			caption : '${solemfmt:getLabel("label.Responsables", codIdioma)}',
			width : 770,
			height : 155,
			scrollOffset : 0,
			rowNum : 0,
			hoverrows : true,  
			loadtext : '${solemfmt:getLabel("label.BuscandoResponsables", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronResponsables", codIdioma)}',
			hidegrid : false,
			sortname : 'run',  
			sortorder : 'asc',
			page : 1,
			pager: '#pieResponsables',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			onSelectRow : function(rowId, status){
				idResponsable = rowId;
			},
			ondblClickRow: function(){
				cargarResponsable();
			},
			loadComplete : function(data){
				var udata = $("#listadoResponsables").getGridParam("userData");
	
				if ($("#listadoResponsables").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoResponsablesData = null;
					} else {
						listadoResponsablesData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoResponsables").getDataIDs();
	
				for (var i = 0; i < ids.length; i++){
					var idFila = ids[i];
	
					var btnEditar="";
					btnEditar+= "<div id='btnEditar' class='icono' onclick='editar("+idFila+")'>";
					btnEditar+= 	"<img src='general/imagenes/iconos/add-32.png'>";
					btnEditar+= "</div>";
	
					var btnEliminar="";
					btnEliminar+= "<div id='btnEliminar' class='icono' onclick='eliminar("+idFila+")'>";
					btnEliminar+= 	"<img src='general/imagenes/iconos/delete-32.png'>";
					btnEliminar+= "</div>";
					$("#listadoResponsables").setRowData(ids[i], {act : btnEditar + btnEliminar});
				}
			},	
			multiselect: false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		}); 

		$('#cargaResp').click(function(){
			cargarResponsable();
		});  
	});


	function cargarResponsable(){
		var idSeleccionado = $('#listadoResponsables').jqGrid('getGridParam','selrow');
		var fila = $("#listadoResponsables").jqGrid('getRowData',idSeleccionado);

		var idResponsable = fila.idResponsable;
		var nomResponsable = fila.nombre;

		setearResponsable(idResponsable, nomResponsable);
		cierraDialog();
	}
	
	function getUrlBuscaResponsables(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarResponsables';
		sData += '&usuario.sNombre=' + $('#nombreUsuResp').val();
		sData += '&usuario.sApePaterno=' + $('#apPatUsuResp').val();
		sData += '&usuario.sCodCargo=00002';
		sData += '&idEmpresa=' + $('#empUsuResp').val();
		return sData;
	}

	function recargarBusqueda(){
		console.log("Recargar Busqueda");
		console.log(getUrlBuscaResponsables());
		
		$('#listadoResponsables').jqGrid('setGridParam', {
			url : getUrlBuscaResponsables(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 10,
			page : 1,
		}).trigger("reloadGrid");
	} 
 
	function cierraDialog(){
		//window.parent.parent.$('#ifrmConvenio').css("height","307px");
		window.parent.$('#ifrmResponsables').dialog('close');
	}

	function setearResponsable(idResponsable, nomResponsable){
		window.parent.$('#conIdResp').val(idResponsable);
		window.parent.$('#conNomResp').val(nomResponsable);
	}
  
</script> 
<div id="listadoResponsable" style="width: 777px;height: 382px;background:#C4C3A5;"> 
	<form id="formAgregaEmpresaInst" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="width: 777px;" >
			<div id="titulo01" class="titulo" style="width: 772px;">
				<div id="contieneFlecha" class="contieneFlecha" style="width: 4px;">
				</div>
				<div id="contienetitulo" class="contienetitulo" style="width:700px">
					<span>${solemfmt:getLabel("label.BusquedaResponsables", codIdioma)}</span>  
				</div>	 
			</div> 
			<div id="detalle01" class="contenido_detalle sombra" style="width: 770px;">
				<div class="fila" style="width: 772px;">
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<input type="text" id="nombreUsuResp" class="caja_texto" onkeyup="recargarBusqueda();">
						</div>
					</div>  
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.ApellidoPaterno", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<input type="text" id="apPatUsuResp" class="caja_texto" onkeyup="recargarBusqueda();">
						</div>
					</div>
					<!-- <div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.ApellidoMaterno", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="apMatEmpResp" class="caja_texto" onkeyup="recargarBusqueda();">
						</div>
					</div>  -->
				
					<div class="campo">
						<div class="label_campo">
							<span>${solemfmt:getLabel("label.Empresa", codIdioma)}: </span>
						</div>
						<div class="valor_campo">
							<select id="empUsuResp" class="caja_texto" onchange="recargarBusqueda();">
								<c:forEach items="${requestScope.listaInstituciones}" var="listaInstituciones">
									<option value="${listaInstituciones.nIdEmpresa}" <c:if test='${idInst == listaInstituciones.nIdEmpresa}'> selected</c:if>>${listaInstituciones.sNombreFantasia}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<!--<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Cargo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<select id="cargoUsuResp" class=" caja_texto" onchange="recargarBusqueda();">
								<c:forEach items="${requestScope.listaCargos}" var="listaCargos">
									<option value="${listaCargos.parCodParametro01}">${listaCargos.parDesLargo01}</option>
								</c:forEach>
							</select>
						</div>
					</div> -->
				</div>
				<div class="espacio5"></div>
			</div>
		</div>

		<div class="espacio5"></div>
			
		<div id="listadoBusquedaResponsablesentes" class="listado" style="height: 237px;">
			<table id="listadoResponsables"></table>
			<div id="pieResponsables"></div>
		</div>
		
		<div style="text-align: center;">
			<div id="btnCargaInst" class="contBtnAceptarPopUp">
				<input type="button" class="darkButton" id="cargaResp" value="${solemfmt:getLabel('boton.Cargar', codIdioma)}" role="button" aria-disabled="false">
			</div>		
			<div id="btnCancelar" class="contBtnCancelarPopUp">
				<input type="button" class="darkButton" id="cancelaResponsable" onclick= "cierraDialog();" value="${solemfmt:getLabel('boton.Cancelar', codIdioma)}" role="button" aria-disabled="false">
			</div>	
		</div>
		  
	</form>
</div>