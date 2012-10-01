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
        <c:forEach items="${requestScope.listaAnexos}" var="anexo">
            <fila>
            	<idAnexo>${anexo.nIdAnexo}</idAnexo>
            	<nombre>${anexo.sNombreAnexo}</nombre>
            	<fechaIni>${solemfmt:stringToFecha(anexo.sFhoInicio)}</fechaIni>
            	<fechaFin>${solemfmt:stringToFecha(anexo.sFhoTermino)}</fechaFin>
            	<estado>${anexo.sCodEstado}</estado> 
            </fila>
        </c:forEach>
    </filas>
</respuestaXML>