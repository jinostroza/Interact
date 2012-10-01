package cl.solem.tyc.funciones;

public class Str {
    public static String left(String texto, int largo){
        return (texto.length() < largo) ? texto : texto.substring(0, largo);
    }
    public static String right(String texto, int largo){
//        return (text.length() < largo) ? text : text.substring(text.length() - largo, largo);
        return (texto.length() < largo) ? texto : texto.substring(texto.length() - largo);
    }
    public static String mid(String texto, int inicio, int fin){
	    //return (text.length() < end + 1) ? text.substring(start, text.length()-1) : text.substring(start, end);
	    return (texto.length() < fin + 1) ? texto.substring(inicio, texto.length()) : texto.substring(inicio, fin);
	}
    public static String mid(String texto, int inicio){
        return (texto.length() < inicio + 1) ? "" : texto.substring(inicio, texto.length() - inicio);
    }
    public static String midL(String texto, int inicio, int largo){
	    return mid(texto, inicio, inicio + largo);
	}
    public static String repeat(String str, int iCantVeces){
        String tst = "";
        for (int j = 0; j < iCantVeces; j++){
            tst += str;
        }
        return tst;
    }
}
