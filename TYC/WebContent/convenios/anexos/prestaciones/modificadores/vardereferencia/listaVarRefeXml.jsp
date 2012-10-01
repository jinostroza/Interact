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
        <%--<c:forEach items="${requestScope.listaVarCond}" varRef="modificador"> --%>
            <fila>
            	<idDescVarRef>1</idDescVarRef>
            	<descVarRef>Valor Promedio Normal Prestacion</descVarRef>
            	<varRef>VAL_PROM_NORMAL</varRef> 
            </fila>
            <fila>
            	<idDescVarRef>2</idDescVarRef>
            	<descVarRef>Valor Referencia 01</descVarRef>
            	<varRef>VAL_REF_01</varRef> 
            </fila>
            <fila>
            	<idDescVarRef>3</idDescVarRef>
            	<descVarRef>Valor Referencia 02</descVarRef>
            	<varRef>VAL_REF_02</varRef> 
            </fila> 
                          
        <%-- </c:forEach>--%>
    </filas>
</respuestaXML>