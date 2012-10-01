<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<respuestaXML>
    <solemdata name="numError"><![CDATA[${error.numError}]]></solemdata>
    <solemdata name="msjError"><![CDATA[${error.msjError}]]></solemdata>
    <paginaactual><![CDATA[${requestScope.pagActual}]]></paginaactual>
    <totalpaginas><![CDATA[${requestScope.totalPaginas}]]></totalpaginas>
    <totalregistros><![CDATA[${requestScope.totalRegistros}]]></totalregistros>

    <filas>
        <c:forEach items="${requestScope.listaPrestacionesAnexo}" var="convenioAnexoDetalle">
            <fila>
            	<idPrestacionAnexo>${convenioAnexoDetalle.nIdConvDetalle}</idPrestacionAnexo>
            	<codigo>${convenioAnexoDetalle.sCodPrestacion}</codigo>
            	<nombre>${convenioAnexoDetalle.sNombrePrestacion}</nombre>
            	<fechaIni>${solemfmt:stringToFecha(convenioAnexoDetalle.sFecCreacion)}</fechaIni>
            </fila>
        </c:forEach>
    </filas>
</respuestaXML>