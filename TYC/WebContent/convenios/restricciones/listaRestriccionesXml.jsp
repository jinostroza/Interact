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
        <c:forEach items="${requestScope.listaRestricciones}" var="restriccion">
            <fila>
            	<idRestriccion>${restriccion.nIdRestriccion}</idRestriccion>
            	<nombre>${restriccion.sNombre}</nombre>
            	<tipo>${restriccion.sDesTipo}</tipo>
            	<valor>${restriccion.sValor}</valor>
            	<rango1>${restriccion.sRango1}</rango1>
            	<rango2>${restriccion.sRango2}</rango2>
            </fila>
        </c:forEach>
    </filas>
</respuestaXML>