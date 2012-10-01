<?xml version="1.0" encoding="ISO-8859-1"?>
<%@ page contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<respuestaXML>
	<error>
		<numero><![CDATA[${error.numError}]]></numero>
    	<mensaje><![CDATA[${error.msjError}]]></mensaje>
    </error>
</respuestaXML>