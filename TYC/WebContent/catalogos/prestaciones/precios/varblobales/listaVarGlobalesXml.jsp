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
        <%--<c:forEach items="${requestScope.listaVarCond}" var="modificador"> --%>
            <fila>
            	<idVarGlobales>1</idVarGlobales>
            	<desvarGlob>Dolar Americano</desvarGlob>
            	<varGlob>USD</varGlob> 
            </fila>
            <fila>
            	<idVarGlobales>2</idVarGlobales>
            	<desvarGlob>EURO</desvarGlob>
            	<varGlob>EURO</varGlob> 
            </fila>
            <fila>
            	<idVarGlobales>3</idVarGlobales>
            	<desvarGlob>Unidad de Fomento</desvarGlob>
            	<varGlob>UF</varGlob> 
            </fila>
            <fila>
            	<idVarGlobales>3</idVarGlobales>
            	<desvarGlob>Hora Prestacion</desvarGlob>
            	<varGlob>HORA_PREST</varGlob> 
            </fila>     
            <fila>
            	<idVarGlobales>4</idVarGlobales>
            	<desvarGlob>Unidad Tributaria mensual</desvarGlob>
            	<varGlob>UTM</varGlob> 
            </fila>
                  
        <%-- </c:forEach>--%>
    </filas>
</respuestaXML>