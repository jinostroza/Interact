<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezado.jsp"%>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<script type="text/javascript">
	var indicador = "S";
	var idNumFila = 0;	
	var listadoPrestacionNomenData =null;

	$(function() 
	{
		$('#listadoNomenclador').jqGrid({
			url : getUrlBuscaNomeclador(),
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '','${solemfmt:getLabel("label.Cantidad", codIdioma)}', '${solemfmt:getLabel("label.Codigo", codIdioma)}', '${solemfmt:getLabel("label.Prestacion", codIdioma)}'],
			colModel : [
						{name : 'idNomec', index:'idNomec', hidden : true},
						{name : 'tabla', index:'tabla', hidden : true},
						{name : 'cantidad', index : 'cantidad', width : 70, resizable : false, sortable : true},
						{name : 'codNomec',index:'codNomec', width : 75, search : true, resizable : false, sortable : true},
			 			{name : 'prestacion',index:'prest', width : 609, search : true, resizable : false, sortable : true}
			 			],
			xmlReader : {
				root : 'filas',
				row : 'fila',
				page : 'respuestaXML>paginaactual',
				total : 'respuestaXML>totalpaginas',
				records : 'respuestaXML>totalregistros',
				repeatitems : false,
				id : 'idNomec',
				userdata : "solemdata"
			},
			width : 795,
			height : 165,
			scrollOffset : 0,
			rowNum : 5,
			hoverrows : true,  
			loadtext : '${solemfmt:getLabel("label.BuscandoNomencladores", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronNomencladores", codIdioma)}',
			hidegrid : false,
			sortname : 'idNomec',  
			sortorder : 'ASC',
			page : 1,
			pager: '#pieNomenclador',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : true,
			shrinkToFit : false,
			onSelectRow : function(rowId, status){ 
				idNumFila = rowId;
			},
			loadComplete : function(data){
				var udata = $("#listadoNomenclador").getGridParam("userData");
	
				if ($("#listadoNomenclador").getGridParam('datatype') != 'local'){
					if (parseInt(udata.errornum, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.errornum + ")"+ udata.errormsj);
						listadoPrestacionNomenData = null;
					} else {
						listadoPrestacionNomenData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoNomenclador").getDataIDs(); 
				for ( var i = 0; i < ids.length; i++){
					var idFila = ids[i];
					var a = 'cant'+idFila;
					var input = "<input type='text' class='inputGrillaDialog' value='1' id='cant"+idFila+"'>";
					$("#listadoNomenclador").setRowData(ids[i], {cantidad : input});
				}

				$('.inputGrillaDialog').blur(function(e){
					var id = $(this).attr("id");
			    	if(soloNumerosOnBlur(id) == false)
				    	$("#"+id).val("1");
				});

			    $('.inputGrillaDialog').keypress(function(e){
			    	return soloNumeros(e);
			    });	
			},	
			onPaging: function(pgButton){
				pagUsuario = $("#listadoNomenclador").jqGrid('getGridParam','page');
				totalRegistros = $("#listadoNomenclador").jqGrid('getGridParam','records');
				regPorPagina = $("#listadoNomenclador").jqGrid('getGridParam','rowNum');
				totalPaginas = Math.ceil(totalRegistros/regPorPagina);

				if(totalPaginas<pagUsuario)
				{
					$("listadoNomenclador").setGridParam({page:totalPaginas});				
				}
			},
			multiselect: true,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});

		$('input[type="radio"]').ezMark();
		$('#nomNomenclador').parent().addClass('ez-selected');
		 
		$("#contieneRadioButtom .campo .contineRadioButton").click(function(){
			getRadio();
			recargarBusquedaNomencladores();
		}); 

		$('input#cancelaNomec').click(function(){ 
			window.parent.$('#ifrmPrestacionesNacionales').dialog('close');  
		});
		$('input#cargaNomec').click(function(){
			var id = $("#listadoNomenclador").jqGrid('getGridParam','selarrrow');
			if(id.length>0){
				var value = validarCantidadesNomencladores();
				if (value>0)
					agregarPrestNomec(recargarNomencladores);
				else alert("${solemfmt:getLabel('alert.LaCantDebeMayorACero', codIdioma)}");   
			}
			else alert("${solemfmt:getLabel('alert.SeleccioneAlMenosUnNomenc', codIdioma)}");  
		}); 	
	
	});

	function agregarPrestNomec(callback)
	{
	    $.ajax({
	        async: false,
		    type: "POST",
		    url: getUrlAgregaPrestacionNomec(),
		    cache: false,
		    dataType: "html",
		    beforeSend: function(data){ 
	            capaLoading("show");
		    },
		    error: function(requestData, strError, strTipoError){
		    	var sMensajeError = "error carga contenido (" + strTipoError + " : " + strError + ")";
		    	var oError = null;
		        oError = new Object;
	            oError.numero = strError;
	            oError.mensaje = sMensajeError;
	  	        capaLoading("hide");
				if (oError){
					alert("("+oError.numero+")"+oError.mensaje);
				}
		    },
		    success: function(respuestaHtml){
		    	capaLoading("hide");
		    	if(callback) callback();
		    }
		});
	}
	function recargarNomencladores(){
		window.parent.recargarBusquedaNomencladores();
		window.parent.$('#ifrmPrestacionesNacionales').dialog('close');
	}

	function cierraDialogNomencladores(){
		window.parent.parent.$('#ifrmPrestacionesNacionales').css("height","144px");
		window.parent.$('#ifrmPrestacionesNacionales').dialog('close');
	}
	
	function getRadio(){ 
		var idRadio =	$('#contenidoFiltros .ez-selected').children().attr('id');
	
		if(idRadio == "nomNomenclador"){
			indicador = "S";
		}else{
			indicador = "N";
		}
	}
	
	function recargarBusquedaNomencladores(){
		$('#listadoNomenclador').jqGrid('setGridParam', {
			url : getUrlBuscaNomeclador(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 5,
			page : 1,
		}).trigger("reloadGrid");
	}

	function getUrlBuscaNomeclador()
	{
		var sData = "SvtPrestaciones";
		sData+= "?KSI=${sesion.sesIdSesion}";
		sData += "&accion=buscarNomencladores";
		sData += '&nIdPrestacion=${nIdPrestacion}';
		sData += "&prestacion.sCodPrestacion="+$('#nomCodigoNomenclador').val();			
		sData += "&prestacion.sNombre="+$('#nomPrestacionNomeclador').val();
		sData += "&prestacion.sIndicador="+indicador; 
		return sData;
	}

	function getUrlAgregaPrestacionNomec()
	{ 
		var sData = "SvtPrestaciones";
		sData+= "?KSI=${sesion.sesIdSesion}";
		sData += '&accion=agregarPrestNomec';
		sData += '&nIdPrestacion=${nIdPrestacion}';
		sData += '&cantidad=' + getCantidadlistaNomec();
		sData += '&aIds=' + getidlistaNomec();
		sData += '&tabla=' + indicador;
		return sData;
	}

	function validarCantidadesNomencladores()
    { 
	    var value = '';
		var data = '';
   		var list = $("#listadoNomenclador").jqGrid('getGridParam','selarrrow');
   			for( var i=0; i<list.length;i++){
   				data = $("#listadoNomenclador").getRowData(list[i]);
   				var idRow = (data.idNomec);
   				value=$("#cant"+idRow).val();
   				if(value==0) return value;
   			}
		return value;
    }

	function getCantidadlistaNomec()
    { 
	    var value = '';
		var data = '';
   		var list = $("#listadoNomenclador").jqGrid('getGridParam','selarrrow');
   			for( var i=0; i<list.length;i++)
   			{
   				data = $("#listadoNomenclador").getRowData(list[i]);
   				var idRow = (data.idNomec);
   				if(i == list.length-1){
   					value+=$("#cant"+idRow).val();
   				}else{   					
   					value+= $("#cant"+idRow).val()+ ',';
   				}	
   			}
		return value;
    }
	
	function getidlistaNomec()
    { 
	    var value = '';
		var data = '';
   		var list = $("#listadoNomenclador").jqGrid('getGridParam','selarrrow');

			for( var i=0; i<list.length;i++)
   			{
   				data = $("#listadoNomenclador").getRowData(list[i]);

   				if(i == list.length-1)
   					value+= (data.idNomec);
   				else
   					value+= (data.idNomec) + ',';
   			}
		return value;
    }
</script>

<div id="detalleEmpresaInst" style="width: 820px;height: 386px;background:#C4C3A5;">
	<form id="formDetalleNomenclador" method="post">
	 <div id="cont_sec_det_01" class="cont_sec_det" style="width: 777px;" >
		<div id="filtrosBusquedaNomecladores" class="filtros">
			<div id="tituloFiltros" class="titulo"style="width: 795px;" > 
				<div id="contienetitulo" class="contienetitulo">
					<span>${solemfmt:getLabel("label.BusquedaPrestacionNomencladorNacional", codIdioma)}</span>
				</div> 
			</div>
		
		<div id="contenidoFiltros" class="contenido_detalle sombra"style="width: 793px;">
			<div class="fila">
				<div class="campo" style="width: 175px">
					<div class="label_campo">
						<span>${solemfmt:getLabel("label.CodigoNomenclador", codIdioma)}: </span>
					</div>
					<div class="valor_campo_small">
						<input type="text" id="nomCodigoNomenclador" onkeyup="recargarBusquedaNomencladores();">
					</div>
				</div>
		
				<div class="campo" style="width: 365px">
					<div class="label_campo">
						<span>${solemfmt:getLabel("label.PrestacionNomenclador", codIdioma)}: </span>
					</div>
					<div class="valor_campo" style="width: 380px">
						<input type="text" id="nomPrestacionNomeclador" onkeyup="recargarBusquedaNomencladores();">
					</div>
				</div>
		
				<div id="contieneRadioButtom" class="fila alturaFila"> 
					<div class="campo" style="width: 127px;">
						<div class="contineRadioButton" style="float: left;margin-left: 11px;" >  
							<input type="radio" id="nomNomenclador" class="radio" name="nomenclador"/> 
						</div> 
						<div class="label_campo labelCampoHorizontal anchoLarge" style="width: 69px;">
							<span>${solemfmt:getLabel("label.Nomenclador",codIdioma)}</span>
						</div> 
					</div>	
	
					<div class="campo" style="width: 127px;"> 
						<div class="contineRadioButton" style="float: left;margin-left: 11px;" >  
							<input type="radio" id="nomNomencladorLe" class="radio" name="nomenclador"/> 
						</div> 	
						<div class="label_campo labelCampoHorizontal anchoLarge" style="width: 69px;">
							<span>${solemfmt:getLabel("label.NacionalLe",codIdioma)}</span>
						</div>
					</div>
				</div>		
			</div>
				<div class="espacio4"></div>
			
		</div> 
	</div> 
	</div>
		<div class="espacio5"></div>
			<div id="listadoBusquedaNomencladores" class="listado" style="height: 227px;">
				<table id="listadoNomenclador"></table>
				<div id="pieNomenclador" class="pie"></div>
			</div>
		<div style="text-align: center;">
			<div id="btnCargaInst" class="contBtnAceptarPopUp">
				<input type="button" class="darkButton" id="cargaNomec" value="${solemfmt:getLabel('boton.Cargar', codIdioma)}" role="button" aria-disabled="false">
			</div>	
			<div id="btnCancelar" class="contBtnCancelarPopUp">
				<input type="button" class="darkButton" id="cancelaNomec" onclick= "cierraDialogNomencladores();" value="${solemfmt:getLabel('boton.Cancelar', codIdioma)}" role="button" aria-disabled="false">
			</div>	
		</div>
	</form>
</div> 