
capaLoading = function(accion) {
	switch( accion ) {
	    case 'show':
//	        capaLoading('hide');
	
            getPageSize('local');
            getPageScroll('local');
	        if ($.browser.msie && $.browser.version <= 6 ) {
	            $('body').append('<div id="capaLoading">'+
	                '<iframe id="ifrm_capaLoading" frameborder="0" tabindex="-1" style="display:block;position:absolute;top:0px;left:0px;width:100%;height:100%;z-index:99998;"/></div>');
	
	            frames["ifrm_capaLoading"].document.write('<html><head><\/head><body bgcolor=\"'+oLoading.overlayColor+'\"><\/body><\/html>');
	        } else {
	            $('body').append('<div id="capaLoading"></div>');
	        }
            $('body').append(
                    '<div id="loading_container">'+
                        '<div id="loading_img"></div>'+
                    '</div>');

            // IE8 Fix
            var pos = ($.browser.msie && parseInt($.browser.version) <= 8 ) ? 'absolute' : 'fixed'; 

            $('#capaLoading').css({
	            position: pos,
	            zIndex: 99997,
	            top: oWindows.arrayPageScroll[1]+'px',
	            left: oWindows.arrayPageScroll[0]+'px',
	            width: oWindows.arrayPageSize[2]+'px',
	            height: oWindows.arrayPageSize[3]+'px',
	            backgroundColor: oLoading.capaColor,
	            opacity: oLoading.capaOpacity/10,
	            filter: 'alpha(opacity=' + oLoading.capaOpacity*10 + ')'
	        });

            $('#loading_container').css({
                position: pos,
                zIndex: 99999
            });
	
	        // Si la ventana es redimencionada, calcula las nuevas dimensiones del fondo
	        $(window).resize(function() {
	            getPageSize('local');
	            getPageScroll('local');
	            $('#capaLoading').css({
		            top: oWindows.arrayPageScroll[1]+'px',
		            left: oWindows.arrayPageScroll[0]+'px',
		            width: oWindows.arrayPageSize[2]+'px',
		            height: oWindows.arrayPageSize[3]+'px'
	            });
		        repositionLoading();
	        });
	        // Si la ventana es scroleada, calcula las nuevas posiciones del fondo
	        $(window).scroll(function() {
	            getPageSize('local');
	            getPageScroll('local');
	            $('#capaLoading').css({
		            top: oWindows.arrayPageScroll[1]+'px',
		            left: oWindows.arrayPageScroll[0]+'px',
		            width: oWindows.arrayPageSize[2]+'px',
		            height: oWindows.arrayPageSize[3]+'px'
	            });
		        repositionLoading();
	        });
	        repositionLoading();
	        break;
	        
	    case 'hide':
	        $('#capaLoading').remove();
	        $('#loading_container').remove();
	        break;
	}
};

repositionLoading = function() {
    var top = ((oWindows.arrayPageSize[3] / 2) - ($('#loading_container').outerHeight() / 2)) + oWindows.arrayPageScroll[1] + oLoading.verticalOffset;
    var left = ((oWindows.arrayPageSize[2] / 2) - ($('#loading_container').outerWidth() / 2)) + oWindows.arrayPageScroll[0] + oLoading.horizontalOffset;
    if( top < 0 ) top = 0;
    if( left < 0 ) left = 0;

    $('#loading_container').css({
        top: top + 'px',
        left: left + 'px'
    });
};

//retorna un Arreglo con el ancho, alto de la pagina y el ancho y alto de la ventana
getPageSize = function(tipoVentana) {
    var xScroll, yScroll;

    var oVentana = (tipoVentana = 'top') ? window.top : window;

    if (oVentana.innerHeight && oVentana.scrollMaxY) {
        xScroll = oVentana.innerWidth + oVentana.scrollMaxX;
        yScroll = oVentana.innerHeight + oVentana.scrollMaxY;
    } else if (oVentana.document.body.scrollHeight > oVentana.document.body.offsetHeight){ // todos menos Explorer Mac
        xScroll = oVentana.document.body.scrollWidth;
        yScroll = oVentana.document.body.scrollHeight;
    } else { // Explorador Mac...puede funcionar solo con Explorer 6, Mozilla y Safari
        xScroll = oVentana.document.body.offsetWidth;
        yScroll = oVentana.document.body.offsetHeight;
    }

    var windowWidth, windowHeight, pageHeight, pageWidth;
    if (oVentana.innerHeight) {	// todos excepto Explorer
        windowWidth = (oVentana.document.documentElement.clientWidth) ? oVentana.document.documentElement.clientWidth : oVentana.innerWidth;
        windowHeight = oVentana.innerHeight;
    } else if (oVentana.document.documentElement && oVentana.document.documentElement.clientHeight) { // Explorer 6 
        windowWidth = oVentana.document.documentElement.clientWidth;
        windowHeight = oVentana.document.documentElement.clientHeight;
    } else if (oVentana.document.body) { // otros Exploradores
        windowWidth = oVentana.document.body.clientWidth;
        windowHeight = oVentana.document.body.clientHeight;
    }

    // for small pages with total height less then height of the viewport
    pageHeight = (yScroll < windowHeight) ? windowHeight : yScroll;

    // for small pages with total width less then width of the viewport
    pageWidth = (xScroll < windowWidth) ? xScroll : windowWidth;

    oWindows.arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight);
};

//retorna un Arreglo con el x,y del scroll de la pagina
getPageScroll = function(tipoVentana) {
    var xScroll, yScroll;

    var oVentana = (tipoVentana = 'top') ? window.top : window;

    if (oVentana.pageYOffset) {
        yScroll = oVentana.pageYOffset;
        xScroll = oVentana.pageXOffset;
    } else if (oVentana.document.documentElement && oVentana.document.documentElement.scrollTop) {	 // Explorer 6
        yScroll = oVentana.document.documentElement.scrollTop;
        xScroll = oVentana.document.documentElement.scrollLeft;
    } else if (oVentana.document.body) {// todos los otros Exploradores
        yScroll = oVentana.document.body.scrollTop;
        xScroll = oVentana.document.body.scrollLeft;
    }

    oWindows.arrayPageScroll = new Array(xScroll,yScroll);
};
