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
    FloatingActionButton btnAgregarPeliculas;
    ListView lts;
    Cursor cPeliculas;
    amigos misPeliculas;
    DB db;
    final ArrayList<amigos> alPeliculas = new ArrayList<amigos>();
    final ArrayList<amigos> alPeliculasCopy = new ArrayList<amigos>();

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
        btnAgregarPeliculas = findViewById(R.id.fabAgregarPeliculas);
        btnAgregarPeliculas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parametros.putString("accion", "nuevo");
                abrirActividad(parametros);
            }
        });
        try {
            di = new detectarInternet(getApplicationContext());
            if (di.hayConexionInternet()) { sincronizar();
                obtenerDatosPeliculasServidor();
            } else {//offline
                obtenerDatosPeliculas();
            }
        }catch (Exception e){
            mostrarMsg("Error al cargar lista de Peliculas: "+ e.getMessage());
        }
        buscarPeliculas();
    }
    private void sincronizar() {
        try {
            di = new detectarInternet(getApplicationContext());

            // Verifica si no hay conexión a Internet
            if (!di.noHayInternet()) {
                // Si no hay conexión, obtén los datos localmente y detén la sincronización
                obtenerDatosPeliculas();
                return;
            }

            // Si hay conexión a Internet, intenta sincronizar con el servidor
            if (di.hayConexionInternet()) {
                obtenerDatosPeliculasServidor();
            }
        } catch (Exception e) {
            mostrarMsg("Error al cargar lista de peliculas: " + e.getMessage());
        }
    }



    private void obtenerDatosPeliculasServidor(){//offline
        try {
            datosServidor = new obtenerDatosServidor();
            String data = datosServidor.execute().get();
            jsonObject = new JSONObject(data);
            datosJSON = jsonObject.getJSONArray("rows");
            mostrarDatosPeliculas();
        }catch (Exception e){
            mostrarMsg("jaja : "+e.getMessage());
        }
    }
    private void mostrarDatosPeliculas() {
        try {
            if (datosJSON.length() > 0) {
                lts = findViewById(R.id.ltsPeliculas);
                alPeliculas.clear();
                alPeliculasCopy.clear();
                JSONObject misDatosJSONObject;
                for (int i = 0; i < datosJSON.length(); i++) {
                    misDatosJSONObject = datosJSON.getJSONObject(i).getJSONObject("value");
                    misPeliculas = new amigos(
                            misDatosJSONObject.getString("_id"),
                            misDatosJSONObject.getString("_rev"),
                            misDatosJSONObject.getString("idPeliculas"),
                            misDatosJSONObject.getString("titulo"),
                            misDatosJSONObject.getString("sinopsis"),
                            misDatosJSONObject.getString("duracion"),
                            misDatosJSONObject.getString("actor"),
                            misDatosJSONObject.getString("urlCompletaFoto")
                    );
                    alPeliculas.add(misPeliculas);
                }
                alPeliculasCopy.addAll(alPeliculas);
                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_amigos.this, alPeliculas);
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
            menu.setHeaderTitle(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("titulo"));
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
                parametros.putString("peliculas", datosJSON.getJSONObject(posicion).toString());
                abrirActividad(parametros);

            } else if (item.getItemId() == R.id.mnxEliminar) {
                eliminarProductos();

            }
            return true;
        }catch (Exception e){
            mostrarMsg("Error al seleccionar una opcion del mennu: "+ e.getMessage());
            return super.onContextItemSelected(item);
        }

    }
    private void eliminarProductos(){
        try{
            AlertDialog.Builder confirmar = new AlertDialog.Builder(lista_amigos.this);
            confirmar.setTitle("Estas seguro de eliminar a: ");
            confirmar.setMessage(datosJSON.getJSONObject(posicion).getJSONObject("value").getString("titulo")); //1 es el nombre
            confirmar.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        String respuesta = db.administrar_amigos("eliminar",
                                new String[]{"", "", datosJSON.getJSONObject(posicion).getJSONObject("value").getString("idPeliculas")});
                        if (respuesta.equals("ok")) {
                            mostrarMsg("Pelicula eliminada con exito");
                            obtenerDatosPeliculas();
                        } else {
                            mostrarMsg("Error al eliminar la pelicula: " + respuesta);
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
            mostrarMsg("Error al eliminar pelicula: "+ e.getMessage());
        }
    }
    private void abrirActividad(Bundle parametros){
        Intent abrirActividad = new Intent(getApplicationContext(), MainActivity.class);
        abrirActividad.putExtras(parametros);
        startActivity(abrirActividad);
    }
    private void obtenerDatosPeliculas(){//offline
        try {
            alPeliculas.clear();
            alPeliculasCopy.clear();

            cPeliculas = db.consultar_amigos();

            if( cPeliculas.moveToFirst() ){
                lts = findViewById(R.id.ltsPeliculas);//eliminar despues
                datosJSON = new JSONArray();
                do{
                    jsonObject =new JSONObject();
                    JSONObject jsonObjectValue = new JSONObject();
                    jsonObject.put("_id", cPeliculas.getString(0));
                    jsonObject.put("_rev", cPeliculas.getString(1));
                    jsonObject.put("idPeliculas", cPeliculas.getString(2));
                    jsonObject.put("titulo", cPeliculas.getString(3));
                    jsonObject.put("sinopsis", cPeliculas.getString(4));
                    jsonObject.put("duracion", cPeliculas.getString(5));
                    jsonObject.put("actor", cPeliculas.getString(6));
                    jsonObject.put("urlCompletaFoto", cPeliculas.getString(7));


                    jsonObjectValue.put("value", jsonObject);
                    datosJSON.put(jsonObjectValue);

                    alPeliculas.add(misPeliculas);//eliminar despues

                }while(cPeliculas.moveToNext());
                mostrarDatosPeliculas();
                alPeliculasCopy.addAll(alPeliculas);//eliminar despues

                adaptadorImagenes adImagenes = new adaptadorImagenes(lista_amigos.this, alPeliculas);
                lts.setAdapter(adImagenes);//eliminar despues

                registerForContextMenu(lts);//eliminar despues
            }else{
                mostrarMsg("No hay Datos de peliculas que mostrar.");
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar datos: "+ e.getMessage());
        }
    }
    private void buscarPeliculas(){
        TextView tempVal;
        tempVal = findViewById(R.id.txtBuscarPeliculas);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    alPeliculas.clear();
                    String valor = tempVal.getText().toString().trim().toLowerCase();
                    if( valor.length()<=0 ){
                        alPeliculas.addAll(alPeliculasCopy);
                    }else{
                        for (amigos peliculas : alPeliculasCopy){
                            String titulo = peliculas.getTitulo();
                            String sinopsis = peliculas.getSinopsis();
                            String duracion = peliculas.getDuracion();
                            String actor = peliculas.getActor();
                            if(titulo.toLowerCase().trim().contains(valor) ||
                                    sinopsis.toLowerCase().trim().contains(valor) ||
                                    duracion.trim().contains(valor) ||
                                    actor.trim().toLowerCase().contains(valor)){
                                alPeliculas.add(peliculas);
                            }
                        }
                        adaptadorImagenes adImagenes = new adaptadorImagenes(getApplicationContext(), alPeliculas);
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