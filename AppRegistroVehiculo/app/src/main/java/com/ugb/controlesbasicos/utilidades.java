package com.ugb.controlesbasicos;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Base64;

@RequiresApi(api = Build.VERSION_CODES.O)
public class utilidades {
    static String url_consulta = "http://192.168.43.119:5984/vehiculos/_design/vehiculos/_view/vehiculos";
    static String url_mto = "http://192.168.43.119:5984/vehiculos";
    static String user="alfred";
    static String passwd="alfredvelas2";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user+":"+passwd).getBytes());
    public String generarIdUnico(){
        return java.util.UUID.randomUUID().toString();
    }
}