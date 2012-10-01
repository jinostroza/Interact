<%@ page language="java"%>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<script type="text/javascript">
 
	$(function() {
		$('a').click(function(){

			var idMenu= $(this).attr('id');
			var url = "";

			url = retornaUrl(idMenu);
			
			$('#menu').attr('action', url);
			$('#menu').submit();
		});
	});

	function retornaUrl(idMenu)
	{
		var url="";

		if(idMenu == 'convenio')
			url = getUrlBuscaConvenios();

		if(idMenu == 'agregarConvenio')
			url = getUrlAgregaConvenio();

		else if(idMenu == 'prestacion')
			url = getUrlBuscaPrestaciones();

		if(idMenu == 'agregarPrestacion')
			url = getUrlAgregaPrestacion();
		
		else if(idMenu == 'catalogo')
			url = getUrlBuscaCatalogos();

		if(idMenu == 'agregarCatalogo')
			url = getUrlAgregaCatalogo();

		return url;
	}

	function borderBottomRadius(div){  
		$("#"+div+" .titulo").addClass("borderBottomRadius");
	}

	function getUrlBuscaConvenios()
	{
		var sData = "SvtConvenios";
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=despacharConvenios';
		sData += '&indRuta=B';
		return sData;		
	}

	function getUrlAgregaConvenio()
	{
		var sData = "SvtConvenios";
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=agregarConvenio';
		sData += '&indRuta=B';
		return sData;		
	}

	function getUrlBuscaPrestaciones()
	{
		var sData = "SvtPrestaciones";
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=despacharPrestaciones';
		sData += '&indRuta=B';
		return sData;		
	}

	function getUrlAgregaPrestacion()
	{
		var sData = "SvtPrestaciones";
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=agregarPrestacion';
		sData += '&indRuta=B';
		return sData;		
	}

	function getUrlBuscaCatalogos()
	{
		var sData = "SvtCatalogos";
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=despacharCatalogos';
		sData += '&indRuta=B';
		return sData;		
	}

	function getUrlAgregaCatalogo()
	{
		var sData = "SvtCatalogos";
		sData += '?KSI=${sesion.sesIdSesion}';
		sData += '&accion=agregarCatalogo';
		sData += '&indRuta=B';
		return sData;		
	}

</script> 

<div id="contenedor_Menu" style="width:auto;">
	<form id="menu" method="post"> 
	
	<div style="float: none; clear: both;"></div>

	<ul id="qm0" class="qmmc">
		<li><a id="prestacion" href="#" class="qmparent">${solemfmt:getLabel("menu.Prestaciones", codIdioma)}</a>
			<ul>
				<li><a id="prestacion" href="#" class="qmparent" >${solemfmt:getLabel("menu.Administrar_Prestaciones", codIdioma)}</a></li>				
				<li>
					<ul>
						<li><a id="prestacion" href="#">${solemfmt:getLabel("menu.Buscar", codIdioma)}</a></li>
						<li><a id="agregarPrestacion" href="#">${solemfmt:getLabel("menu.Agregar", codIdioma)}</a></li>
					</ul>
				</li>
			</ul>
		</li>
		
		<li><a id="catalogo" href="#" class="qmparent" >${solemfmt:getLabel("menu.Catalogos", codIdioma)}</a>
			<ul>
				<li><a id="catalogo" href="#" class="qmparent" href="#">${solemfmt:getLabel("menu.Administrar_Catalogos", codIdioma)}</a></li>
				<li>
					<ul>
						<li><a id="catalogo" href="#">${solemfmt:getLabel("menu.Buscar", codIdioma)}</a></li>
						<li><a id="agregarCatalogo" href="#">${solemfmt:getLabel("menu.Agregar", codIdioma)}</a></li>
					</ul>
				</li>
			</ul>
		</li>

		<li><a id="convenio" href="#" class="qmparent" >${solemfmt:getLabel("menu.Convenios", codIdioma)}</a>
			<ul>
				<li><a id="convenio" href="#" class="qmparent" href="#">${solemfmt:getLabel("menu.Administrar_Convenios", codIdioma)}</a></li>
				<li>
					<ul>
						<li><a id="convenio" href="#">${solemfmt:getLabel("menu.Buscar", codIdioma)}</a></li>
						<li><a id="agregarConvenio" href="#">${solemfmt:getLabel("menu.Agregar", codIdioma)}</a></li>
					</ul>
				</li>
			</ul>
		</li>
		<li class="qmclear">&nbsp;</li>
	</ul>

	<!-- Create Menu Settings: (Menu ID, Is Vertical, Show Timer, Hide Timer, On Click, Right to Left, Horizontal Subs, Flush Left) -->
	<script type="text/javascript">
		qm_create(0,false,0,300,false,false,false,false);
	</script>

	</form>
</div>