package com.ugb.controlesbasicos;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class detectarInternet {
    private Context _context;

    public detectarInternet(Context _context) {
        this._context = _context;
    }

    public boolean hayConexionInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        if (networkInfos == null) return false;
        for (int i = 0; i < networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) return true;
        }
        return false;
    }

    public boolean noHayInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return true; // Si no se puede obtener el servicio, asumimos que no hay conexión

        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        if (networkInfos == null)
            return true; // Si no se pueden obtener los NetworkInfo, asumimos que no hay conexión

        for (NetworkInfo networkInfo : networkInfos) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED)
                return false; // Si hay alguna conexión conectada, entonces hay conexión
        }
        return true; // Si no hay ninguna conexión conectada, entonces no hay conexión
    }
}

