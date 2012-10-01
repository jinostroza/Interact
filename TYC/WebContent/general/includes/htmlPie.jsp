
<c:if test="${sScript ne null}">
	<script type="text/javascript">
	$(document).ready(function(){
	    ${sScript};
	});
	</script>
</c:if>
<!--    <div id="out">(SALIDAS)</div> --->
</body>
</html>