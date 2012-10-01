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
        <c:forEach items="${requestScope.listaPrestaciones}" var="prestacionNac">
            <fila>
            	<idPrestacion><![CDATA[${prestacionNac.nIdPrestacion}]]></idPrestacion>
            	<tabla><![CDATA[${prestacionNac.sIndicador}]]></tabla>
            	<cantidad><![CDATA[${prestacionNac.nCantidad}]]></cantidad>
            	<codigo><![CDATA[${prestacionNac.sCodPrestacion}]]></codigo>
            	<descripcion><![CDATA[${prestacionNac.sDescripcion}]]></descripcion>
            	<nomenclador><![CDATA[${prestacionNac.sDesTipoPrestacion}]]></nomenclador>
            	<fecha><![CDATA[${solemfmt:stringToFecha(prestacionNac.sFecModificacion)}]]></fecha>
            </fila>
        </c:forEach>
    </filas>
</respuestaXML>