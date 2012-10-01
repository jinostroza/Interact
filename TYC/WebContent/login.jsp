<%@ page language="java"%>
<%@ include file="general/includes/declaraciones.jsp" %>
<%@ include file="general/includes/htmlEncabezado.jsp" %>

<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader ("Expires", 0); 
%>

<script type="text/javascript">
$(function(){
	$('input').placeholder();
	$(".imgNoOk").hide();
	$(".imgOk").hide();	 
	
	$("#btnOk").click(function(){ 
		$(".imgNoOk").hide();
		$(".imgOk").hide(); 
		submitear();
   	});
   	
	$("#btnCancelar").click(function(){ 
		$(".imgNoOk").hide();
		$(".imgOk").hide();  
		limpia();
	});

	limpia = function(){
		 $('.dato_login input').val(""); 
	};

	submitear = function()
	{
		var sUser = $("#txtUsuario").val();
		var sPass = $("#txtPassword").val();
		var sHost = $("#hidHost").val();  
		var sIdioma =  $("#idioma").val();  
		
		if (($.trim(sUser) == "" || $.trim(sPass) == "") || (sPass=="Palabla clave  " || sUser=="Usuario  ")){  
			if(sUser== "" || sUser=="Usuario  "){
				$("#imgUserCheck .imgOk").hide();
				$("#imgUserCheck .imgNoOk").show();
			}else{
				$("#imgUserCheck .imgNoOk").hide();				 
				$("#imgUserCheck .imgOk").show();
			}
			
			if(sPass=="" || sPass=="Palabla clave  "){  
				$("#imgPassCheck .imgOk").hide();	 	 
				$("#imgPassCheck .imgNoOk").show();				
			}else{ 
				$("#imgPassCheck .imgNoOk").hide();	
				$("#imgPassCheck .imgOk").show(); 
			}

	    	$("#txtUsuario").focus(); 
			return;
		} 
		   
		var sData = "accion=inicio";
		sData += "&login.username=" + sUser;
		sData += "&login.password=" + sPass;
		sData += "&login.host=" + sHost;
		sData += "&login.idioma="+ sIdioma;

		cargaContenido("s", "SvtSession", sData); 
	};
});

function submit(e) 
{
	if(e.keyCode == 13)
		submitear();
}
</script>
  
<div id="cuerpoPrincipal" class="cuerpoPrincipal" style="width: 1024px; height: 767px; margin: auto; position: relative;">
	<div style="clear:both; height:263px;" ></div>
	<div id="contenido" style="height:360px;">
	    
		<div id="logoAlert"></div>
	    <div id="data-acceso" style="border: 1px solid; background: #EBEBC9;-webkit-border-radius: 8px;border-radius: 8px; border: 1px solid #AAA; -webkit-box-shadow: 0 0 6px #999; box-shadow: 0 0 6px #999;">
		    <div class="iniSesion">Iniciar sesion<br/><strong>TYC</strong></div>
		    <div>
		    	<table>
			        <tr> 
			            <td>
			                <div id="user">
				                <div class="dato_login"><input type="text" id="txtUsuario" maxLength="20" class="caja_texto login tpo_texto" tabindex=1 placeholder="Usuario  "></div>
			               		<label id="imgUserCheck" style="position: absolute; color: red; font-style: bold; margin-top: 10px;">
			               			<img class="imgNoOk" style='height: 18px' alt='' src='${rutaImg}iconos/badCheck.png'>
									<img class="imgOk" style='height: 18px' alt='' src='${rutaImg}iconos/okCheck.png'>
			               		</label> 
			                </div>
			                <div id="pass"> 
				                <div class="dato_login"><input type="password" id="txtPassword" maxLength="20" class="caja_texto login tpo_texto" tabindex=2 onkeypress="submit(event);" placeholder="Palabla clave  "></div>
			               		<label id="imgPassCheck" style="position: absolute; color: red; font-style: bold; margin-top: 10px;">
			               			<img class="imgNoOk" style='height: 18px' alt='' src='${rutaImg}iconos/badCheck.png'>
									<img class="imgOk" style='height: 18px' alt='' src='${rutaImg}iconos/okCheck.png'>
			               		</label>
			               		<input class="inputTabla" id="hidHost" type="hidden" name="login.host" value="<% out.print( request.getRemoteAddr() );%>" /> 
				            </div>
			                
			                <div id="idioma_Login" style="margin-left:10px">
				                <select id="idioma">
				                	<option value="spa">Espa&ntilde;ol</option>
				                	<option value="eng">Ingl&eacute;s</option>
				                </select>
			                </div> 
			            </td> 
			        </tr>     
		        </table>
             	
	        	<div style="text-align: center;">   
	        		<img id="btnCancelar" src="${rutaImg}login/btnBorrar.png" class="clickeable">
	        		<img id="btnOk" src="${rutaImg}login/btnEntrar.png" class="clickeable" tabindex = 3 >
	       		</div>
		    </div>
	    </div>
	</div>
</div> 

<%@ include file="general/includes/htmlPie.jsp" %> 