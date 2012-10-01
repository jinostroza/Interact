<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<respuestaXML>
	<c:if test="${!empty error.msjError}">
	    <error>
	        <numero>${error.numError}</numero>
	        <mensaje>${error.msjError}</mensaje>
	    </error>
	</c:if>
    
    <sesion>
        <ksi>${sesion.sesIdSesion}</ksi>
    </sesion>
</respuestaXML>