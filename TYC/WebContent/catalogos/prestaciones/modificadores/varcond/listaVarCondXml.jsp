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
            	<idVarCond>1</idVarCond>
            	<varCond>Tipo Admision</varCond>
            	<codigo>TIPADMI</codigo> 
            </fila>
            <fila>
            	<idVarCond>2</idVarCond>
            	<varCond>Unidad Organizacional</varCond>
            	<codigo>UO</codigo> 
            </fila>
            <fila>
            	<idVarCond>3</idVarCond>
            	<varCond>Dia Prestacion</varCond>
            	<codigo>DIA_PREST</codigo> 
            </fila>
            <fila>
            	<idVarCond>4</idVarCond>
            	<varCond>Hora Prestacion</varCond>
            	<codigo>HORA_PREST</codigo> 
            </fila>     
            <fila>
            	<idVarCond>5</idVarCond>
            	<varCond>Total Prestaciones (Valor)</varCond>
            	<codigo>TOT_VAL_PREST</codigo> 
            </fila>
            <fila>
            	<idVarCond>6</idVarCond>
            	<varCond>Total Prestaciones (Cantidad)</varCond>
            	<codigo>TOT_CANT_PREST</codigo> 
            </fila>                  
        <%-- </c:forEach>--%>
    </filas>
</respuestaXML>