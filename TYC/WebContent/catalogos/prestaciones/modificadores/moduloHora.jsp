<script type="text/javascript">  
/************************************************************************************************/
 
$(function(){  
 
	$('#horaCondicion').timepicker({});
}); 
 
</script> 

<form name="myform" method="POST" action="">
	<div class="fila" style="width: 89px;margin-top: 16px;margin-left: 0px;">
		<div class="campo">
			<div class="label_campo">
				<span>Hora: </span>
			</div>
			<div class="valor_campo">
				<input type="text" id="horaCondicion" maxlength="5">
			</div>
		</div>
	</div>	
</form>