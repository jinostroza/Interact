 
/*
     FILE ARCHIVED ON 3:43:49 mar 29, 2007 AND RETRIEVED FROM THE
     INTERNET ARCHIVE ON 14:38:59 jul 13, 2012.
     JAVASCRIPT APPENDED BY WAYBACK MACHINE, COPYRIGHT INTERNET ARCHIVE.

     ALL OTHER CONTENT MAY ALSO BE PROTECTED BY COPYRIGHT (17 U.S.C.
     SECTION 108(a)(3)).
*/

function caret(node) {
	 if(node.selectionStart) return node.selectionStart;
	 else if(!document.selection) return 0;
	 //node.focus();
	 var c		= "\001";
	 var sel	= document.selection.createRange();
	 var txt	= sel.text;
	 var dul	= sel.duplicate();
	 var len	= 0;
	 try{ dul.moveToElementText(node); }catch(e) { return 0; }
	 sel.text	= txt + c;
	 len		= (dul.text.indexOf(c));
	 sel.moveStart('character',-1);
	 sel.text	= "";
	 return len;
}

