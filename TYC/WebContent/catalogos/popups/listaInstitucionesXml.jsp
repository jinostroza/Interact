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
        <c:forEach items="${requestScope.listaEmpresasInst}" var="empresasInst">
            <fila>
            	<idEmpresaInst>${empresasInst.nIdEmpresa}</idEmpresaInst>
            	<nombre>${empresasInst.sNombreFantasia}</nombre>
            	<rut>${solemfmt:stringToRun(empresasInst.sRut)}</rut>
            </fila> 
        </c:forEach>
    </filas>
</respuestaXML>