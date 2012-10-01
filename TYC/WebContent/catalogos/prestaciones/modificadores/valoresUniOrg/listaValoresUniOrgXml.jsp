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
        <c:forEach items="${requestScope.listaUniOrg}" var="unidadOrg"> 
            <fila>
				<idValUO>${unidadOrg.nHijoUorIdUnidadOrg}</idValUO>
            	<valUO>${unidadOrg.sHijoUorDescripcion}</valUO>
            	<codUO>${unidadOrg.sHijoUorCodUnidadOrg}</codUO> 
            </fila> 
		</c:forEach> 
    </filas>
</respuestaXML> 