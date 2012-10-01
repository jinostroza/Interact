<script type="text/javascript">  
	function addChar(oInput){    
		var digito = $(oInput).val(); 
		var puntero = $(oInput).attr("puntero"); 
		var accion = $("#"+puntero).attr('accion'); 
		var readOnly = $("#"+accion).attr('readonly');  
		
		if(readOnly!="readonly"){
			var indicador =$("#btnUsar").attr('indicador');  
			var indicadorValue =$("#"+puntero).attr("numChar"); 
			var strDividir = $("#"+accion).val();  
			
			if(indicadorValue=="")
				indicadorValue = $("#"+indicador).val(); 
	
			var strA = strDividir.substring(0,indicadorValue);
			var strB = strDividir.substring(indicadorValue);
			var charAgregar = strA+digito+strB;  
	
			$("#"+accion).val(charAgregar); 
	
			var strModif = strA+digito;
			var numChar = strModif.length;
			
			$("#"+puntero).attr("numChar",numChar);   
		}	
	}  
</script>	
	
<div class="filaBtn" style="margin-top: 35px">
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="7" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="8" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="9" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>		
	<div class="contieneButtonCalc btnCalcMedio">
		<input class="char button btnAnchoNormal" type="button" value="/" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	
	
	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="(" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>		
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value=")" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>						  							
</div>
<div class="filaBtn">
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="4" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="5" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="6" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcMedio">
		<input class="char button btnAnchoNormal" type="button" value="*" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>							
								
	<div class="botonesOpcionales">	
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="char button btnAnchoNormal" type="button" value="AND" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
		</div>
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="char button btnAnchoNormal" type="button" value="OR" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
		</div>	
	</div>																				
</div>
<div class="filaBtn">
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="1" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>				
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="2" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="3" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	 
	<div class="contieneButtonCalc btnCalcMedio">
		<input class="char button btnAnchoNormal" type="button" value="-" role="button" onclick="addChar(this)" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	
	
	<div class="botonesOpcionales">	
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="char button btnAnchoNormal" type="button" value="=" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
		</div>
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="char button btnAnchoNormal" type="button" value="<>" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
		</div>
	</div>																			
</div>
<div class="filaBtn">
	<div class="contieneButtonCalc btnCalcAncho">
		<input class="char button btnAnchoLargo" type="button" value="0" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false" >
	</div>
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="char button btnAnchoNormal" type="button" value="," role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>								
	<div class="contieneButtonCalc btnCalcMedio">
		<input class="char button btnAnchoNormal" type="button" value="+" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
	</div>	
	
	<div class="botonesOpcionales">		
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="char button btnAnchoNormal" type="button" value="<=" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
		</div>
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="char button btnAnchoNormal" type="button" value=">=" role="button" onclick="addChar(this)" puntero="punteroCondicion" indicador="indicadorCondicion" accion="condicion" aria-disabled="false">
		</div>	
	</div>																		
</div>	