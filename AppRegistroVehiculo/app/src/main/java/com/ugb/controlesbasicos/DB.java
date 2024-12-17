package com.ugb.controlesbasicos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private static final String dbname = "vehiculos";
    private static final int v = 1;
    private static final String SQLdb = "CREATE TABLE vehiculos (id text, rev text, idVehiculos text, marca text, " +
            "motor text, chasis text, Vin text, combustion text, foto text)";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbname, factory, v);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQLdb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //actualizar la estrucutra de la BD.
    }

    public String administrar_amigos(String accion, String[] datos) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "";
            if (accion.equals("nuevo")) {
                sql = "INSERT INTO vehiculos(id,rev,idVehiculos,marca,motor,chasis,vin,combustion,foto) VALUES('" + datos[0] + "','" + datos[1] + "', '" + datos[2] +
                        "','" + datos[3] + "','" + datos[4] + "','" + datos[5] + "','" + datos[6] + "', '" + datos[7] + "', '" + datos[8] + "')";
            } else if (accion.equals("modificar")) {
                sql = "UPDATE vehiculos SET id='" + datos[0] + "', rev='" + datos[1] + "', marca='" + datos[3] + "',motor='" + datos[4] + "',chasis='" +
                        datos[5] + "',vin='" + datos[6] + "',combustion='" + datos[7] + "', foto='" + datos[8] + "' WHERE idVehiculos='" + datos[2] + "'";
            } else if (accion.equals("eliminar")) {
                sql = "DELETE FROM vehiculos WHERE idVehiculos='" + datos[2] + "'";
            }
            db.execSQL(sql);
            return "ok";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Cursor consultar_amigos() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM vehiculos ORDER BY marca", null);
        return cursor;
    }

}
