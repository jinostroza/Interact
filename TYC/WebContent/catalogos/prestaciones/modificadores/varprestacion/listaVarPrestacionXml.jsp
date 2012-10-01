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
            	<idVarPrest>1</idVarPrest>
            	<desVarPres>Valor Prestacion Hija 1</desVarPres>
            	<varPres>VAL|CODPREST01</varPres> 
            </fila>
            <fila>
            	<idVarPrest>2</idVarPrest>
            	<desVarPres>Valor Prestacion Hija 2</desVarPres>
            	<varPres>VAL|CODPREST02</varPres> 
            </fila>
            <fila>
            	<idVarPrest>3</idVarPrest>
            	<desVarPres>Valor Prestacion Hija 3</desVarPres>
            	<varPres>VAL|CODPREST03</varPres> 
            </fila>  
            <fila>
            	<idVarPrest>4</idVarPrest>
            	<desVarPres>Cantidad Prestaciones Hija 01</desVarPres>
            	<varPres>CANT|CODPREST01</varPres> 
            </fila>
            <fila>
            	<idVarPrest>5</idVarPrest>
            	<desVarPres>Cantidad Prestaciones Hija 02</desVarPres>
            	<varPres>CANT|CODPREST02</varPres> 
            </fila>                                   
        <%-- </c:forEach>--%>
    </filas>
</respuestaXML>