package com.ugb.controlesbasicos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;


public class lista_amigos extends AppCompatActivity {
    Bundle parametros = new Bundle();
    FloatingActionButton btnAgregarVehiculos;
    ListView lts;
    Cursor cVehiculos;
    amigos misVehiculos;
    DB db;
    final ArrayList<amigos> alVehiculos = new ArrayList<amigos>();
    final ArrayList<amigos> alVehiculosCopy = new ArrayList<amigos>();

    JSONArray datosJSON;
    JSONObject jsonObject;
    obtenerDatosServidor datosServidor;
    detectarInternet di;
    int posicion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_amigos);

        db = new DB(lista_amigos.this, "", null, 1);
        btnAgregarVehiculos = findViewById(R.id.fabAgregarVehiculos);
        btnAgregarVehiculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        });
        try {
            di = new detectarInternet(getApplicationContext());
            if (di.hayConexionInternet()) { sincronizar();
                obtenerDatosVehiculosServidor();
            } else {//offline
                obtenerDatosVehiculos();
            }
        }catch (Exception e){
            mostrarMsg("Error al cargar lista Producto: "+ e.getMessage());
        }
        buscarVehiculos();
    }
    private void sincronizar() {
        try {
            di = new detectarInternet(getApplicationContext());

            // Verifica si no hay conexión a Internet
            if (!di.noHayInternet()) {
                // Si no hay conexión, obtén los datos localmente y detén la sincronización
                obtenerDatosVehiculos();
                return;
            }

            // Si hay conexión a Internet, intenta sincronizar con el servidor
            if (di.hayConexionInternet()) {
                obtenerDatosVehiculosServidor();
            }
        } catch (Exception e) {
            mostrarMsg("Error al cargar lista Producto: " + e.getMessage());
        }
    }



    private void obtenerDatosVehiculosServidor(){//offline
        try {
            datosServidor = new obtenerDatosServidor();
            String data = datosServidor.execute().get();
            jsonObject = new JSONObject(data);
            datosJSON = jsonObject.getJSONArray("rows");
            mostrarDatosVehiculos();
        }catch (Exception e){
            mostrarMsg("jaja : "+e.getMessage());
        }
    }
    private void mostrarDatosVehiculos() {
        try {
            if (datosJSON.length() > 0) {
                lts = findViewById(R.id.ltsVehiculos);
                alVehiculos.clear();
                alVehiculosCopy.clear();
                JSONObject misDatosJSONObject;
                for (int i = 0; i < datosJSON.length(); i++) {
                    misDatosJSONObject = datosJSON.getJSONObject(i).getJSONObject("value");
                    misVehiculos = new amigos(
                            misDatosJSONObject.getString("_id"),
                            misDatosJSONObject.getString("_rev"),
                            misDatosJSONObject.getString("idVehiculos"),
                            misDatosJSONObject.getString("marca"),
                            misDatosJSONObject.getString("motor"),
                            misDatosJSONObject.getString("chasis"),
                            misDatosJSONObject.getString("vin"),
                            misDatosJSONObject.getString("combustion"),
                            misDatosJSONObject.getString("urlCompletaFoto")
                    );
                    alVehiculos.add(misVehiculos);
                }
                alVehiculosCopy.addAll(alVehiculos);
                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_amigos.this, alVehiculos);
                lts.setAdapter(adImagenes);
                registerForContextMenu(lts);
            } else {
                mostrarMsg("No hay datos que mostrar.");
            }
        } catch (Exception e) {
            mostrarMsg("Error al mostrar los datos: " + e.getMessage());
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mimenu, menu);

        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            menu.setHeaderTitle(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("marca"));
        }catch (Exception e){
            mostrarMsg("Error al mostrar el menu: "+ e.getMessage());
        }
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        try{
            if (item.getItemId() == R.id.mnxAgregar) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            } else if (item.getItemId()== R.id.mnxModificar) {
                parametros.putString("accion", "modificar");
                parametros.putString("vehiculos", datosJSON.getJSONObject(posicion).toString());
                abrirActividad(parametros);

            } else if (item.getItemId() == R.id.mnxEliminar) {
                eliminarVehiculos();

            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error al seleccionar una opcion del mennu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarVehiculos(){
        try{
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_amigos.this);
            confirmar.setTitle("Estas seguro de eliminar a: ");
            confirmar.setMessage(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("marca")); //1 es el nombre
            confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        String respuesta = db.administrar_amigos("eliminar",
                                new String[]{"", "", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("idVehiculos")});
                        if (respuesta.equals("ok")) {
                            mostrarMsg("Vehiculo eliminado con exito");
                            obtenerDatosVehiculos();
                        } else {
                            mostrarMsg("Error al eliminar el Vehiculo: " + respuesta);
                        }
                    }catch (Exception e){
                        mostrarMsg("Error al intentar elimianr: "+ e.getMessage());
                    }
                }
            });
            confirmar.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            confirmar.create().show();
        }catch (Exception e){
            mostrarMsg("Error al eliminar vehiculo: "+ e.getMessage());
        }
    }
    private void abrirActividad(Bundle parametros){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }
    private void obtenerDatosVehiculos(){//offline
        try {
            alVehiculos.clear();
            alVehiculosCopy.clear();

            cVehiculos = db.consultar_amigos();

            if( cVehiculos.moveToFirst() ){
                lts = findViewById(R.id.ltsVehiculos);//eliminar despues
                datosJSON = new JSONArray();
                do{
                    jsonObject =new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();
                    jsonObject.put("_id", cVehiculos.getString(0));
                    jsonObject.put("_rev", cVehiculos.getString(1));
                    jsonObject.put("idVehiculos", cVehiculos.getString(2));
                    jsonObject.put("marca", cVehiculos.getString(3));
                    jsonObject.put("motor", cVehiculos.getString(4));
                    jsonObject.put("chasis", cVehiculos.getString(5));
                    jsonObject.put("vin", cVehiculos.getString(6));
                    jsonObject.put("combustion", cVehiculos.getString(7));
                    jsonObject.put("urlCompletaFoto", cVehiculos.getString(8));


                    jsonObjectValue.put("value", jsonObject);
                    datosJSON.put(jsonObjectValue);

                    alVehiculos.add(misVehiculos);//eliminar despues

                }while(cVehiculos.moveToNext());
                mostrarDatosVehiculos();
                alVehiculosCopy.addAll(alVehiculos);//eliminar despues

                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_amigos.this, alVehiculos);
                lts.setAdapter(adImagenes);//eliminar despues

                registerForContextMenu(lts);//eliminar despues
            }else{
                mostrarMsg("No hay Datos de vehiculos que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos: "+ e.getMessage());
        }
    }
    private void buscarVehiculos(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarVehiculo);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    alVehiculos.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alVehiculos.addAll(alVehiculosCopy);
                    }else{
                        for (amigos vehiculos : alVehiculosCopy){
                            String marca = vehiculos.getMarca();
                            String motor = vehiculos.getMotor();
                            String chasis = vehiculos.getChasis();
                            String vin = vehiculos.getVin();
                            String combustion = vehiculos.getCombustion();
                            if(marca.toLowerCase().trim().contains(valor) ||
                                    motor.toLowerCase().trim().contains(valor) ||
                                    chasis.trim().contains(valor) ||
                                    vin.trim().toLowerCase().contains(valor) ||
                                    combustion.trim().contains(valor)){
                                alVehiculos.add(vehiculos);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alVehiculos);
                        lts.setAdapter(adImagenes);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al buscar: "+ e.getMessage());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

}