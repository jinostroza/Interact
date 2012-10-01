<%@ include file="../../general/includes/declaraciones.jsp"%>
<%@ include file="../../general/includes/htmlEncabezadoPopUp.jsp"%>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<script type="text/javascript">
	var idNumFila = 0;	
	var listadoCompuestasData = null;

	$(document).ready(function() 
	{
		$("#listadoCompuestas").jqGrid({
			url : getUrlBuscaPrestacionCompuesta(), 
			mtype : 'POST',
			datatype : 'xml',
			colNames : [ '', '${solemfmt:getLabel("label.Cantidad", codIdioma)}','${solemfmt:getLabel("label.Codigoprestacion", codIdioma)}', '${solemfmt:getLabel("label.Prestacion", codIdioma)}',  '${solemfmt:getLabel("label.TipoPrestacion", codIdioma)}'],  
			colModel : [
						{name : 'idPrestacion', index:'idPrestacion', hidden : true}, 
						{name : 'cantidad', index : 'cant', width : 70, resizable : false, sortable : true}, 
						{name : 'codigo',index:'codigo', width : 140, search : true, resizable : false, sortable : true},
			 			{name : 'prestacion',index:'prest', width : 318, search : true, resizable : false, sortable : true}, 
						{name : 'tipo', index:'tipo', width: 240, resizable: false, sortable: true}
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
			width : 814,
			height : 160,
			scrollOffset : 0,
			rowNum : 5,
			hoverrows : true,
			loadtext : '${solemfmt:getLabel("label.BuscandoPrestaciones", codIdioma)}',
			emptyrecords : '${solemfmt:getLabel("label.NoEncontraronPrestaciones", codIdioma)}',
			hidegrid : false,
			sortname : 'idPrestacion',  
			sortorder : 'ASC',
			page : 1,
			pager: '#pieCompuestas',
			pgtext : 'Pág: {0} de {1}',
			viewrecords : true,
			pginput : true,
			shrinkToFit : false,
			onSelectRow : function(rowId, status){
				idNumFila = rowId;
			},
			loadComplete : function(data){
				var udata = $("#listadoCompuestas").getGridParam("userData");

				if ($("#listadoCompuestas").getGridParam('datatype') != 'local') {
					if (parseInt(udata.errornum, 10) != 0) {
						alert("¡¡¡ATENCION 1!!! \n("+ udata.errornum + ")"+ udata.errormsj);
						listadoCompuestasData = null;
					} else {
						listadoCompuestasData = data;
					}
				}
			},
			gridComplete: function(){
				var ids = $("#listadoCompuestas").getDataIDs();
				for ( var i = 0; i < ids.length; i++){
					
					var idFila = ids[i];
					var a = 'cant'+idFila;
					var input = "<input type='text' class='inputGrillaDialog' value='1' id='"+a+"'>";
					$("#listadoCompuestas").setRowData(ids[i], {cantidad : input}); 
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
				pagUsuario = $("#listadoCompuestas").jqGrid('getGridParam','page');
				totalRegistros = $("#listadoCompuestas").jqGrid('getGridParam','records');
				regPorPagina = $("#listadoCompuestas").jqGrid('getGridParam','rowNum');
				totalPaginas = Math.ceil(totalRegistros/regPorPagina);

				if(totalPaginas<pagUsuario)
				{
					$("listadoCompuestas").setGridParam({page:totalPaginas});				
				}
			},
			multiselect : true,
			loadError : function(xhr, status, error){
				alert("¡¡¡ATENCION 2!!! \n(Tipo: " + status+ "; Respuesta: " + xhr.status + " - "+ xhr.statusText + ")");
			}
		});

		$('input#agregarComp').click(function(){
			var id = $("#listadoCompuestas").jqGrid('getGridParam','selarrrow');
			if(id.length>0){
				var value = validarCantidadesCompuestas();
				if (value>0)
					agregarPrestCompuesta(recargarCompuestas);
				else 
					alert("${solemfmt:getLabel('alert.LaCantDebeMayorACero', codIdioma)}");   
			}
			else  
				alert("${solemfmt:getLabel('alert.SeleccioneAlMenosUnaPrest', codIdioma)}");   
		});
	}); 

	function agregarPrestCompuesta(callback)
	{
	    $.ajax({
	        async: false,
		    type: "POST",
		    url: getUrlAgregaCompuesta(),
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

	function recargarCompuestas(){
		window.parent.recargarBusquedaCompuestas();
		window.parent.$('#ifrmCompuestas').dialog('close');
	}

	function cierraDialogCompuestas(){
		window.parent.parent.$('#ifrmCompuestas').css("height","144px");
		window.parent.$('#ifrmCompuestas').dialog('close');
	}
	
	function getUrlBuscaPrestacionCompuesta(){
		var sData = "SvtPrestaciones";
		sData+= "?KSI=${sesion.sesIdSesion}";
		sData += "&accion=buscarCompuestas";
		sData += '&nIdPrestacion=${nIdPrestacion}';
		sData += "&prestacion.sCodPrestacion="+$('#compCodigo').val();			
		sData += "&prestacion.sNombre="+$('#compNombre').val();	
		//console.log(sData);
		return sData;
	}

	function getUrlAgregaCompuesta()
	{
		var sData = "SvtPrestaciones";
		sData+= "?KSI=${sesion.sesIdSesion}";
		sData += '&accion=agregarCompuesta';
		sData += '&nIdPrestacion=${nIdPrestacion}';
		sData += '&aIds=' + getidlistaCompuestas();
		sData += '&cantidad=' + getCantidadlistaPrestacionCompuesta();
		return sData;
	}

	function validarCantidadesCompuestas()
    { 
	    var value = '';
		var data = '';
   		var list = $("#listadoCompuestas").jqGrid('getGridParam','selarrrow');
   			for( var i=0; i<list.length;i++){
   				data = $("#listadoCompuestas").getRowData(list[i]);
   				var idRow = (data.idPrestacion);
   				value=$("#cant"+idRow).val();
   				if(value==0) return value;
   			}
		return value;
    }

	function getCantidadlistaPrestacionCompuesta()
    { 
	    var value = '';
		var data = '';
   		var list = $("#listadoCompuestas").jqGrid('getGridParam','selarrrow');
   			for( var i=0; i<list.length;i++)
   			{
   				data = $("#listadoCompuestas").getRowData(list[i]);
   				if(i == list.length-1){
   					var idRow = (data.idPrestacion);
   					 value+=$("#cant"+idRow).val();
   				}else{
   					var idRow = (data.idPrestacion);
   					value+= $("#cant"+idRow).val()+ ',';
   				}	
   			}
		return value;
    }

	function getidlistaCompuestas()
    {
		var value = '';
		var data = '';
   		var list = $("#listadoCompuestas").jqGrid('getGridParam','selarrrow');

			for( var i=0; i<list.length;i++)
   			{
   				data = $("#listadoCompuestas").getRowData(list[i]);

   				if(i == list.length-1)
   					value+= (data.idPrestacion);
   				else
   					value+= (data.idPrestacion) + ',';
   			}
		return value;
    }

	function recargarBusquedaCompuestas()
	{
		$('#listadoCompuestas').jqGrid('setGridParam',{
			url : getUrlBuscaPrestacionCompuesta(),
			mtype : 'POST',
			datatype : 'xml',
			rowNum : 5,
			page : 1,
		}).trigger("reloadGrid");
	}
</script> 

<div id="detalleCompuesta" style="width: 820px;height: 386px;background:#C4C3A5;"> 
	<form id="formFiltrosCompuesta" method="post">
		<div id="cont_sec_det_01" class="cont_sec_det" style="width: 777px;" >
			<div id="titulo01" class="titulo" style="width: 814px;">
				<div id="contieneFlecha" class="contieneFlecha" style="width:4px;">
				</div>
				<div id="contienetitulo" class="contienetitulo" style="width:700px">
					<span>${solemfmt:getLabel("label.BusquedaPrestaciones", codIdioma)}</span> 
				</div>	 
			</div> 
			<div id="detalleCompuesta" class="contenido_detalle sombra" style="width: 812px;">
				<div class="fila" style="width: 772px;">
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.Codigoprestacion", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="compCodigo" class="caja_texto" onkeyup="recargarBusquedaCompuestas();">
						</div>
					</div>  
					<div class="campo campo_detalle">
						<div class="label_campo detalle_label_campo">
							<span>${solemfmt:getLabel("label.NombrePrestacion", codIdioma)}: </span>
						</div>
						<div class="valor_campo float_left">
							<input type="text" id="compNombre" class="caja_texto" onkeyup="recargarBusquedaCompuestas();">
						</div>
					</div> 
				</div> 
				<div class="espacio5"></div>
			</div>
		</div>

		<div class="espacio5"></div>
			
		<div id="listadoBusquedaCompuestas" class="listado" style="height: 241px;">
			<table id="listadoCompuestas"></table>
			<div id="pieCompuestas"></div>
		</div>
		
		<div style="text-align: center;">
			<div id="btnCargaComp" class="contBtnAceptarPopUp">
				<input type="button" class="darkButton" id="agregarComp" value="${solemfmt:getLabel('boton.Cargar', codIdioma)}" role="button" aria-disabled="false">
			</div>
			<div id="btnCancelar" class="contBtnCancelarPopUp">
				<input type="button" class="darkButton" id="cancelaComp" onclick= "cierraDialogCompuestas();" value="${solemfmt:getLabel('boton.Cancelar', codIdioma)}" role="button" aria-disabled="false">
			</div>			
		</div>
		  
	</form>
</div>