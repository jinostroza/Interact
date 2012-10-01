<%@ page language="java"%>
<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezado.jsp"%>

<script type="text/javascript">
var idPlanBeneficio = 0;
var listadoPlanesBeneficioData = null;

	$(function() { 
		
		$("#listadoPlanesBeneficio").jqGrid({
			url : getUrlBuscaPlanesBeneficio(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '','${solemfmt:getLabel("label.Codigo", codIdioma)}','${solemfmt:getLabel("label.Nombre", codIdioma)}'],
			colModel : [
						{name : 'idPlanBeneficio',index:'id', hidden : true}, 
						{name : 'codigo',index:'cod',	width : 230, resizable : false,	sortable : true}, 
			 			{name : 'nombre',index:'nombre', width : 530, search : true, resizable : false, sortable : true}, 
						],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idPlanBeneficio',
				userdata : "solemdata"
			},
			caption : '${solemfmt:getLabel("label.PlanesBeneficio", codIdioma)}',
			width : 770,
			height : 155,
			scrollOffset : 0,
			rowNum : 0,
			hoverrows : true,  
			loadtext : '${solemfmt:getLabel("label.BuscandoPlanes", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronPlanes", codIdioma)}',
			hidegrid : false,
			sortname : 'run',  
			sortorder : 'asc',
			page : 1,
			pager: '#piePlanesBeneficio',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : false,
			shrinkToFit : false,
			onSelectRow : function(rowId, status){
				idPlanBeneficio = rowId;
			},
			ondblClickRow: function(){ 
				cargarPlan();
			},
			loadComplete : function(data){
				var udata = $("#listadoPlanesBeneficio").getGridParam("userData");
	
				if ($("#listadoPlanesBeneficio").getGridParam('datatype') != 'local'){
					if (parseInt(udata.numError, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.numError + ")"+ udata.msjError);
						listadoPlanesBeneficioData = null;
					} else {
						listadoPlanesBeneficioData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoPlanesBeneficio").getDataIDs();
	
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
					$("#listadoPlanesBeneficio").setRowData(ids[i], {act : btnEditar + btnEliminar});
				}
			},	
			multiselect: false,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		}); 

		$('#cargaPlan').click(function(){
			//window.parent.parent.$('#ifrmPrestacionAnexo').css("height","144px");
			cargarPlan();
		});  
	});


	function cargarPlan(){
		var idSeleccionado = $('#listadoPlanesBeneficio').jqGrid('getGridParam','selrow');
		var fila = $("#listadoPlanesBeneficio").jqGrid('getRowData',idSeleccionado);

		var idPlanBeneficio = fila.idPlanBeneficio;
		var nomPlan = fila.nombre;
		var codPlan = fila.codigo;

		setearPlan(idPlanBeneficio, nomPlan, codPlan);
		cierraDialog();
	}
	
	function getUrlBuscaPlanesBeneficio(){
		var sData = 'SvtConvenios';
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=buscarPlanesBeneficio';
		sData += '&idConvenio='+ ${idConvenio};
		sData += '&planBeneficio.nIdEmpresa=' + ${idEmpresa};
		sData += '&planBeneficio.sCodPlan=' + $('#codPlan').val();
		sData += '&planBeneficio.sNombrePlan=' + $('#nombrePlan').val();
		return sData;
	}

	function recargarBusqueda(){
		console.log("Recargar Busqueda");
		console.log(getUrlBuscaPlanesBeneficio());
		
		$('#listadoPlanesBeneficio').jqGrid('setGridParam', {
			url : getUrlBuscaPlanesBeneficio(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 10,
			page : 1,
		}).trigger("reloadGrid");
	} 
 
	function cierraDialog(){
		window.parent.parent.$('#ifrmBeneficio').css("height","142px");
		window.parent.$('#ifrmPlanes').dialog('close'); 
	}

	function setearPlan(idPlanBeneficio, nomPlan, codPlan){
		window.parent.$('#conIdPlan').val(idPlanBeneficio);
		window.parent.$('#conNomPlan').val(nomPlan);
		window.parent.$('#conCodPlan').val(codPlan);
	}
  
</script> 

<div id="listadoPlanBeneficio" style="width: 777px;height: 382px;background:#C4C3A5;"> 
	<form id="formAgregaEmpresaInst" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="width: 777px;" >
			<div id="titulo01" class="titulo" style="width: 772px;">
				<div id="contieneFlecha" class="contieneFlecha" style="width: 4px;">
				</div>
				<div id="contienetitulo" class="contienetitulo" style="width:700px">
					<span>${solemfmt:getLabel("label.BusquedaPlanesBeneficio", codIdioma)}</span>  
				</div>	 
			</div> 
			<div id="detalle01" class="contenido_detalle sombra" style="width: 770px;">
				<div class="fila" style="width: 772px;">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigo", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="codPlan" class="caja_texto" onkeyup="recargarBusqueda();">
						</div>
					</div>  
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Nombre", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="nombrePlan" class="caja_texto" onkeyup="recargarBusqueda();">
						</div>
					</div> 
				</div> 
				<div class="espacio5"></div>
			</div>
		</div>

		<div class="espacio5"></div>
			
		<div id="listadoBusquedaPlanesBeneficio" class="listado" style="height: 237px;">
			<table id="listadoPlanesBeneficio"></table>
			<div id="piePlanesBeneficio"></div>
		</div>
		
		<div style="text-align: center;">
			<div id="btnCargaPlan" class="contBtnAceptarPopUp">
				<input type="button" class="darkButton" id="cargaPlan" value="${solemfmt:getLabel('boton.Cargar', codIdioma)}" role="button" aria-disabled="false">
			</div>
			<div id="btnCancelar" class="contBtnCancelarPopUp">
				<input type="button" class="darkButton" id="cancelaBeneficioPlan" onclick= "cierraDialog();" value="${solemfmt:getLabel('boton.Cancelar', codIdioma)}" role="button" aria-disabled="false">
			</div>			
		</div>
		  
	</form>
</div>