<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<respuestaXML>
    <solemdata name="numError">${error.numError}</solemdata>
    <solemdata name="msjError">${error.msjError}</solemdata>
    <paginaactual>${requestScope.pagina}</paginaactual>
    <totalpaginas>${requestScope.totalPaginas}</totalpaginas>
    <totalregistros>${requestScope.numTotalReg}</totalregistros>

    <filas>                          
        <c:forEach items="${requestScope.listaPrecios}" var="precio">
             <fila>
            	<idPrecioPrestacion>${precio.nIdCatalogoPrecio}</idPrecioPrestacion>
            	<idCatPrestacion>${precio.nIdCatalogoPrestacion}</idCatPrestacion>
            	<c:choose>
	                <c:when test="${precio.nPrecio == 0}">
	                     <precio></precio>
	                </c:when>
	                <c:otherwise>
	                   <precio>${precio.nPrecio}</precio>
	                </c:otherwise>
	            </c:choose>
            	<formula>${precio.sFormula}</formula>
            	<fechaIni>${solemfmt:stringToFecha(precio.sFhoInicio)}</fechaIni>
            	<fechaFin>${solemfmt:stringToFecha(precio.sFhoFin)}</fechaFin>
            </fila>
        </c:forEach>
    </filas>
</respuestaXML>