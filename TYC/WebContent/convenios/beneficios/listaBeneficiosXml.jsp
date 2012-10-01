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
        <c:forEach items="${requestScope.listaBeneficios}" var="beneficio">
            <fila>
            	<idConvBeneficio>${beneficio.nIdConvBeneficio}</idConvBeneficio>
            	<codigo>${beneficio.sCodPlan}</codigo>
            	<nombre>${beneficio.sNombrePlan}</nombre>
            	<estado>${beneficio.sCodEstado}</estado> 
            </fila>
        </c:forEach>
    </filas>
</respuestaXML>