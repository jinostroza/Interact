/**************************************************/
/*** DECLARACION DE VARIABLES GENERALES JS      ***/
/*** AUTOR				:	JIU				   	***/
/*** FECHA CREACIÓN		:	JULIO 2012		   	***/
/*** FECHA ACTUALIZACIÓN: 	JULIO 2012		   	***/
/**************************************************/

var Glb_SeparaMilMsk = ".";
var Glb_SeparaDecimalMsk = ",";
var sRutaRaiz = "";
var sRutaImagenes = sRutaRaiz + 'general/imagenes/';
var tituloConfirmacionAlerta = "Aviso";
var sFechaActualDB = "";

//******************************************************************************
//	Variables grilla JQGrid
//******************************************************************************
var anchoGrillaGenerico = 1004;
var anchoGrillaGenericoTab = 974;
var altoGrillaGenerico = 435;
var numFilasGrillaGenerico = 13;
var scrollOffSet = 12;

if (!oLoading) var oLoading = {
    verticalOffset: 0,         //-75 vertical offset of the dialog from center screen, in pixels
    horizontalOffset: 0,       // horizontal offset of the dialog from center screen, in pixels/
    capaColor     : 'transparent', // color base de la capa loading
    capaOpacity   : 5          // transparencia de la capa loading
};

if (!oWindows) var oWindows = {
    arrayPageScroll : null,
    arrayPageSize : null
};

if (!oAscii) var oAscii = {
    TAB      : 9,
    ENTER    : 13,
    ESCAPE   : 27,
    COMILLAS : 34, //"
    CREMILLAS: 39, //'
    PAREN_INI: 40, //(
    PAREN_FIN: 41, //)
    COMA     : 44, //,
    GUION    : 45, //-
    PUNTO    : 46, //.
    SLASH    : 47, ///
    A        : 65,
    K        : 75,
    Z        : 90,
    a        : 97,
    k        : 107,
    z        : 122,
    Aacute   : 193, //Á
    Eacute   : 201, //É
    Iacute   : 205, //Í
    Ntilde   : 209, //Ñ
    Oacute   : 211, //Ó
    Uacute   : 218, //Ú
    aacute   : 225, //á
    eacute   : 233, //é
    iacute   : 237, //í
    ntilde   : 241, //ñ
    oacute   : 243, //ó
    uacute   : 250  //ú
};

//********************************************************************
//Define Separadores de Numeros y Fechas y los setea para el sitio
//********************************************************************
if (!oSeparadores) var oSeparadores = {
  ascMilesMsk     : oAscii.PUNTO,
  chrMilesMsk     : ".",
  ascDecimalesMsk : oAscii.COMA,
  chrDecimalesMsk : ",",
  ascFechasMsk    : oAscii.SLASH,
  chrFechasMsk    : "/"
};

//********************************************************************
//Define Default Value para objetos Solem segun tipo
//********************************************************************
if (!oDefaultValue) var oDefaultValue = {
    idnumero      : "",
    numero        : "",
    valor         : ""
};

//*******************************************************************************
//*********      REFERENCIA TABLA ASCII CARACTERES IMPORTANTES        ***********
//*******************************************************************************
//32 	espacio
//33 	! 	
//34 	" 	
//35 	# 	
//36 	$ 	
//37 	% 	
//38 	& 	
//39 	' 	
//40 	( 	
//41 	) 	
//42 	* 	
//43 	+ 	
//44 	, 	
//45 	- 	
//46 	. 	
//47 	/ 	
//48 	0 	
//49 	1 	
//50 	2 	
//51 	3 	
//52 	4 	
//53 	5 	
//54 	6 	
//55 	7 	
//56 	8 	
//57 	9 	
//58 	: 	
//59 	; 	
//60 	< 	
//61 	= 	
//62 	> 	
//63 	? 	
//64 	@ 	
//65 	A 	
//66 	B 	
//67 	C 	
//68 	D 	
//69 	E 	
//70 	F 	
//71 	G 	
//72 	H 	
//73 	I 	
//74 	J 	
//75 	K 	
//76 	L 	
//77 	M 	
//78 	N 	
//79 	O 	
//80 	P
//81 	Q
//82 	R
//83 	S
//84 	T
//85 	U
//86 	V
//87 	w
//88 	X
//89 	Y
//90 	Z
//91 	[
//92 	\
//93 	]
//94 	^
//95 	_
//96 	`
//97 	a
//98 	b
//99 	c
//100 	d
//101 	e
//102 	f
//103 	g
//104 	h
//105 	i
//106 	j
//107 	k
//108 	l
//109 	m
//110 	n
//111 	o
//112 	p
//113 	q
//114 	r
//115 	s
//116 	t
//117 	u
//118 	v
//119 	w
//120 	x
//121 	y
//122 	z
//123 	{
//124 	|
//125 	}
//126 	~