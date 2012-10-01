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
        <%--<c:forEach items="${requestScope.listaVarCond}" var="modificador"> --%>
            <fila>
            	<idValPos>1</idValPos>
            	<valPos>Lunes</valPos>
            	<codigo>LUN</codigo> 
            </fila>
            <fila>
            	<idValPos>2</idValPos>
            	<valPos>Martes</valPos>
            	<codigo>MAR</codigo> 
            </fila>
            <fila>
            	<idValPos>3</idValPos>
            	<valPos>Miercoles</valPos>
            	<codigo>MIE</codigo> 
            </fila> 
            <fila>
            	<idValPos>4</idValPos>
            	<valPos>Jueves</valPos>
            	<codigo>JUE</codigo> 
            </fila>  
            <fila>
            	<idValPos>5</idValPos>
            	<valPos>Viernes</valPos>
            	<codigo>VIE</codigo> 
            </fila>  
            <fila>
            	<idValPos>6</idValPos>
            	<valPos>Sabado</valPos>
            	<codigo>SAB</codigo> 
            </fila>    
             <fila>
            	<idValPos>7</idValPos>
            	<valPos>Domingo</valPos>
            	<codigo>DOM</codigo> 
            </fila>
            <fila>
            	<idValPos>8</idValPos>
            	<valPos>Feriado</valPos>
            	<codigo>FER</codigo> 
            </fila>                                                               
        <%-- </c:forEach>--%>
    </filas>
</respuestaXML>