package com.ugb.controlesbasicos;

import java.util.Base64;

public class utilidades {
    static String url_consulta = "http://192.168.43.119:5984/amigos/_design/amigos/_view/amigos";
    static String url_mto = "http://192.168.43.119:5984/amigos";
    static String user="alfred";
    static String passwd="alfredvelas2";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user+":"+passwd).getBytes());
    public String generarIdUnico(){
        return java.util.UUID.randomUUID().toString();
    }
}