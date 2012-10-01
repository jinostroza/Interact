<script type="text/javascript">/*  
	function addChar(oInput){ 
		var inputReadOnly = $('#precioFormulaAgrega').attr("readonly"); 
		var accion = "";  
		
		if(inputReadOnly!='readonly'){ 
			accion = 'precioFormulaAgrega';  
		}else  
			accion = 'precioPrecioAgrega';
		  
		var digito = $(oInput).val();  
		var indicadorValue =$('#puntero').attr("numChar"); 
		var strDividir = $("#"+accion).val();   
		var strA = strDividir.substring(0,indicadorValue);
		var strB = strDividir.substring(indicadorValue);
		var charAgregar = strA+digito+strB;  

		$("#"+accion).val(charAgregar); 

		var strModif = strA+digito;
		var numChar = strModif.length;
		$("#puntero").attr("numChar",numChar);
	} */ 

	function addChar(oInput){   
		var accion = "";  
		var indicadorValue = ""; 
		var puntero  = ""; 
		var seccion = "";
		var digito = $(oInput).val();  

		if($("#detalleDetallePrecio").is(":visible")){
			var inputReadOnly = $('#precioFormulaDetalle').attr("readonly");   
			seccion = "Detalle";
		}else{
			var inputReadOnly = $('#precioFormulaAgrega').attr("readonly");  
			seccion = "Agrega";
		}
		
		if(inputReadOnly!='readonly'){ 
			accion = 'precioFormula'+seccion;  
			puntero  = "punteroFormula"; 
			indicadorValue =$('#'+puntero).attr("numChar"); 
		}else {
			if(digito==","){
				var retorno = true;	
				//var charCode = 44; 
	 			var valorNumerico = $("#precioPrecio"+seccion).val();  
				var numCar = valorNumerico.length;  
				var veces = 0;   
				var posicion = valorNumerico.indexOf(digito);  
				var posfinal = valorNumerico.lastIndexOf(digito);
				posfinal = numCar-posfinal;  
			
				while(numCar>=posfinal) {
				   posicion = valorNumerico.indexOf(letra);  
				   posicion++;
				   valorNumerico = valorNumerico.substring(posicion); 
				   numCar -= posicion;  
				   veces++;  
				} 
				
				if(posicion>=0 && veces>0) 
					retorno = false;  
				if(retorno==true){
					accion = 'precioPrecio'+seccion;
					puntero  = "punteroPrecio"; 
					indicadorValue =$('#'+puntero).attr("numChar"); 
				}
				
			}else{ 
				accion = 'precioPrecio'+seccion;
				puntero  = "punteroPrecio"; 
				indicadorValue =$('#'+puntero).attr("numChar"); 
			}
		}  
		
		var strDividir = $("#"+accion).val();   
		var strA = strDividir.substring(0,indicadorValue);
		var strB = strDividir.substring(indicadorValue);
		var charAgregar = strA+digito+strB;  

		$("#"+accion).val(charAgregar); 

		var strModif = strA+digito;
		var numChar = strModif.length;
		$("#"+puntero).attr("numChar",numChar);
	}  
	
</script>	
	
<div class="filaBtn">
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="7" role="button" onclick="addChar(this)" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="8" role="button" onclick="addChar(this)" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="9" role="button" onclick="addChar(this)" aria-disabled="false">
	</div>
	<div class="opcionalBtns">			
		<div class="contieneButtonCalc btnCalcMedio">
			<input class="button btnAnchoNormal" type="button" value="/" role="button" onclick="addChar(this)" aria-disabled="false">
		</div>	
	
	
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="button btnAnchoNormal" type="button" value="(" role="button" onclick="addChar(this)" aria-disabled="false">
		</div>		
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="button btnAnchoNormal" type="button" value=")" role="button" onclick="addChar(this)" aria-disabled="false">
		</div>		
	</div>						  							
</div>
<div class="filaBtn">
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="4" role="button" onclick="addChar(this)" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="5" role="button" onclick="addChar(this)" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="6" role="button" onclick="addChar(this)" aria-disabled="false">
	</div>	
	<div class="opcionalBtns">	
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="button btnAnchoNormal" type="button" value="*" role="button" onclick="addChar(this)" aria-disabled="false">
		</div>	
	</div>																			
</div>
<div class="filaBtn">
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="1" role="button" onclick="addChar(this)" aria-disabled="false">
	</div>				
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="2" role="button" onclick="addChar(this)" aria-disabled="false">
	</div>	
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="3" role="button" onclick="addChar(this)" aria-disabled="false">
	</div>	 
	<div class="opcionalBtns">	
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="button btnAnchoNormal" type="button" value="-" role="button" onclick="addChar(this)" aria-disabled="false">
		</div>	
	</div>																		
</div>
<div class="filaBtn">
	<div class="contieneButtonCalc btnCalcAncho">
		<input class="button btnAnchoLargo" type="button" value="0" role="button" onclick="addChar(this)" aria-disabled="false" >
	</div>
	<div class="contieneButtonCalc btnCalcNormal">
		<input class="button btnAnchoNormal" type="button" value="," role="button" onclick="addChar(this)" aria-disabled="false">
	</div>		
	<div class="opcionalBtns">							
		<div class="contieneButtonCalc btnCalcNormal">
			<input class="button btnAnchoNormal" type="button" value="+" role="button" onclick="addChar(this)" aria-disabled="false">
		</div>
	</div>																	
</div>	