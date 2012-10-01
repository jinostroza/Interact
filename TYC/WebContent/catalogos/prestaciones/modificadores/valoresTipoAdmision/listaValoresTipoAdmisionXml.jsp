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
        <c:forEach items="${requestScope.listaValTipAdm}" var="valTipAdm"> 
            <fila>
            	<idValTA>${valTipAdm.parNumParametro}</idValTA>
            	<ValTA>${valTipAdm.parDesLargo01}</ValTA>
            	<codValTA>${valTipAdm.parCodParametro01}</codValTA>  
             </fila>	
        </c:forEach> 
    </filas>
</respuestaXML> 