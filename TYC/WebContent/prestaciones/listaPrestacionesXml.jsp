<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.solem.cl" prefix="solemfmt" %>

<respuestaXML>
	<solemdata name="errornum"><![CDATA[${error.numError}]]></solemdata>
    <solemdata name="errormsj"><![CDATA[${error.msjError}]]></solemdata>
    <paginaactual><![CDATA[${requestScope.pagActual}]]></paginaactual>
    <totalpaginas><![CDATA[${requestScope.totalPaginas}]]></totalpaginas>
    <totalregistros><![CDATA[${requestScope.totalRegistros}]]></totalregistros>

    <filas>
        <c:forEach items="${requestScope.listaPrestacion}" var="prestacion">
            <fila>
            	<idPrestacion><![CDATA[${prestacion.nIdPrestacion}]]></idPrestacion>
            	<codPrestacion><![CDATA[${prestacion.sCodPrestacion}]]></codPrestacion>
            	<nombre><![CDATA[${prestacion.sNombre}]]></nombre>
            	<descripcion><![CDATA[${prestacion.sDescripcion}]]></descripcion>
            	<estado><![CDATA[${prestacion.sDesEstado}]]></estado>
            </fila>
        </c:forEach>
    </filas>
</respuestaXML>