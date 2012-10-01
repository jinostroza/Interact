<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jstl/core" 	prefix="c" %>
<%@ taglib uri="http://www.solem.cl" 			prefix="solemfmt" %>

<c:set var="sTitulo" 			value="TYC 2012"/>
<c:set var="rutaRaiz" 			value=""/>
<c:set var="rutaImg" 			value="${rutaRaiz}general/imagenes/"/>
<c:set var="rutaImgMenu" 		value="${rutaRaiz}general/imagenes/menu"/>
<c:set var="sFechaHoraActual" 	value="${solemfmt:devuelveFechaHoraActual(sesion.sesFhoUltimaConsulta)}"/>
<c:set var="sFechaActual" 		value="${solemfmt:devuelveSoloFechaActual(sesion.sesFhoUltimaConsulta)}"/>
<c:set var="sRangoIni" 			value="15000101"/>
<c:set var="sRangoFin" 			value="22001231"/>
<c:set var="sRangoIniH" 		value="150001010000"/>
<c:set var="sRangoFinH" 		value="220012312359"/>
<c:set var="codIdioma"			value="${sesion.sesCodIdioma}"/>