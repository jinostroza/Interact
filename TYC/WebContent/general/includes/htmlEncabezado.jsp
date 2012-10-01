<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="Pragma" content="no-cache">

		<!-- ESTILOS CSS -->
        <link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/jquery-ui-1.8.19.custom.css?FHO=${sesion.sesFhoUltimaConsulta}">
        <link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/ui.jqgrid.css?FHO=${sesion.sesFhoUltimaConsulta}">
		<link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/grid.css?FHO=${sesion.sesFhoUltimaConsulta}"/>
		<link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/estilos.css?FHO=${sesion.sesFhoUltimaConsulta}"/> 
		<link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/jq.tipsy.css?FHO=${sesion.sesFhoUltimaConsulta}">
		<link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/ezmark.css?FHO=${sesion.sesFhoUltimaConsulta}">
		<link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/quick-menu-core.css?FHO=${sesion.sesFhoUltimaConsulta}">
		<link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/quick-menu-style.css?FHO=${sesion.sesFhoUltimaConsulta}">  
		<link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/jquery.alerts.css?FHO=${sesion.sesFhoUltimaConsulta}">                  
        <link type="text/css" rel="stylesheet" media="screen" href="${rutaRaiz}general/css/ezmark.css?FHO=${sesion.sesFhoUltimaConsulta}"> 
		
		<!-- JQUERY JS -->
        <script type="text/javascript" src="${rutaRaiz}general/js/jquery-1.7.2.min.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        
		<!-- JQUERY UI JS -->
        <script type="text/javascript" src="${rutaRaiz}general/js/jquery-ui-1.8.19.custom.min.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>

        <!-- JQGRID 4.4.0-->        
        <script type="text/javascript" src="${rutaRaiz}general/js/jquery.jqGrid.min.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>        
        
		<!-- IDIOMAS GRILLA (agregar más de ser necesario)-->
		<script type="text/javascript" src="${rutaRaiz}general/js/_gridLang/grid.locale-en.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
		<script type="text/javascript" src="${rutaRaiz}general/js/_gridLang/grid.locale-es.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        
        <!-- IDIOMAS DATEPICKER (agregar más de ser necesario)-->
        <script type="text/javascript" src="${rutaRaiz}general/js/jq.ui.datepicker-lang.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        
        <!-- GENERALES JS -->
 		<script type="text/javascript" src="${rutaRaiz}general/js/jquery.alerts.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>        
        <script type="text/javascript" src="${rutaRaiz}general/js/jq.tipsy.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        <script type="text/javascript" src="${rutaRaiz}general/js/jq.path.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        <script type="text/javascript" src="${rutaRaiz}general/js/jquery.ezmark.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        <script type="text/javascript" src="${rutaRaiz}general/js/jquery.jqprint-0.3.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        <script type="text/javascript" src="${rutaRaiz}general/js/jquery.placeholder.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        <script type="text/javascript" src="${rutaRaiz}general/js/tabber.js?FHO=${sesion.sesFhoUltimaConsulta}"></script> 
        <script type="text/javascript" src="${rutaRaiz}general/js/jquery.ezmark.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        <script type="text/javascript" src="${rutaRaiz}general/js/js_GetCursorCaretPositioninTextarea.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        <script type="text/javascript" src="${rutaRaiz}general/js/jquery-ui-timepicker-addon.js?FHO=${sesion.sesFhoUltimaConsulta}"></script> 
        
  		<!-- SOLEM JS -->
  		<script type="text/javascript" src="${rutaRaiz}general/js/jq.solem.fn.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
  		<script type="text/javascript" src="${rutaRaiz}general/js/funciones.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        <script type="text/javascript" src="${rutaRaiz}general/js/definiciones.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        <script type="text/javascript" src="${rutaRaiz}general/js/jq.solem.loading.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
        
		<!-- MENU PRINCIPAL -->
		<script type="text/javascript" src="${sRutaRaiz}general/js/menu-js.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>	
		<script type="text/javascript" src="${sRutaRaiz}general/js/menu-quick-menu-basic.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
		<script type="text/javascript" src="${sRutaRaiz}general/js/menu-0-add-on-settings.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
		<script type="text/javascript" src="${sRutaRaiz}general/js/menu-rounded-corners.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
		<script type="text/javascript" src="${sRutaRaiz}general/js/menu-add-on-code-item-bullets-CSS-Imageless.js?FHO=${sesion.sesFhoUltimaConsulta}"></script>
  
        <title>${sTitulo}</title>
        <link rel="shortcut icon" href="${rutaRaiz}general/imagenes/iconos/Icon.png">
    </head>
    <body>