<%@ page language="java"%>
<%@ include file="general/includes/declaraciones.jsp" %>
<%@ include file="general/includes/htmlEncabezado.jsp" %>

<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", 0); 
%>

<script type="text/javascript">
	window.location="login.jsp"; 
</script>

</body>
</html>