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
        <c:forEach items="${requestScope.listaResponsables}" var="responsable">
            <fila>
            	<idResponsable>${responsable.nIdUsuario}</idResponsable>
            	<nombre>${responsable.sNombre} ${responsable.sApePaterno} ${responsable.sApeMaterno}</nombre>
            	<empresa>${responsable.sNombreEmpresa}</empresa>
            	<cargo>${responsable.sDesCargo}</cargo>
            </fila>
        </c:forEach>
    </filas>
</respuestaXML>