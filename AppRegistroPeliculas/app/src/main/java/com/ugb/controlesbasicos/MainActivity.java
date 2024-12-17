package com.ugb.controlesbasicos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView tempVal;
    Button btn;
    FloatingActionButton btnRegresar;
    String id="", rev="", idPeliculas="", accion="nuevo";
    ImageView img;
    String urlCompletaFoto;
    Intent tomarFotoIntent;
    utilidades utls;
    DB db;

    detectarInternet di;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utls = new utilidades();
        db = new DB(getApplicationContext(), "", null, 1);
        di = new detectarInternet(getApplicationContext());

        btnRegresar = findViewById(R.id.fabListaPeliculas);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresarLista = new Intent(getApplicationContext(), lista_amigos.class);
                startActivity(regresarLista);
            }
        });
        btn = findViewById(R.id.btnGuardarPelicula);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tempVal = findViewById(R.id.txtTitulo);
                    String titulo = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtSinopsis);
                    String sinopsis = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtDuracion);
                    String duracion = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtActor);
                    String actor = tempVal.getText().toString();


                    String respuesta = "";
                    if (di.hayConexionInternet()) {
                        //obtener datos a enviar al servidor
                        JSONObject datosProductos = new JSONObject();
                        if (accion.equals("modificar")) {
                            datosProductos.put("_id", id);
                            datosProductos.put("_rev", rev);
                        }
                        datosProductos.put("idPeliculas", idPeliculas);
                        datosProductos.put("titulo", titulo);
                        datosProductos.put("sinopsis", sinopsis);
                        datosProductos.put("duracion", duracion);
                        datosProductos.put("actor", actor);
                        datosProductos.put("urlCompletaFoto", urlCompletaFoto);
                        //enviamos los datos
                        enviarDatosServidor objGuardarDatosServidor = new enviarDatosServidor(getApplicationContext());
                        respuesta = objGuardarDatosServidor.execute(datosProductos.toString()).get();
                        //comprobacion de la respuesta
                        JSONObject respuestaJSONObject = new JSONObject(respuesta);
                        if (respuestaJSONObject.getBoolean("ok")) {
                            id = respuestaJSONObject.getString("id");
                            rev = respuestaJSONObject.getString("rev");
                        } else {
                            respuesta = "Error al guardar en servidor: " + respuesta;
                        }
                    } else if (di.noHayInternet()) {

                    }
                    String[] datos = new String[]{id, rev, idPeliculas, titulo, sinopsis, duracion, actor, urlCompletaFoto};
                    respuesta = db.administrar_amigos(accion, datos);
                    if (respuesta.equals("ok")) {
                        mostrarMsg("pelicula registrado con exito.");
                        listarPeliculas();
                    } else {
                        mostrarMsg("Error al intentar registrar la pelicula en tu app: " + respuesta);
                    }
                } catch (Exception e){
                    mostrarMsg("Error al guadar datos en el servidor o en SQLite: "+ e.getMessage());
                }
            }
        });
        img = findViewById(R.id.btnImgPelicula);
        img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                tomarFotoPeliculas();
            }
        });
        mostrarDatosPeliculas();
    }
    private void tomarFotoPeliculas(){
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fotoPelicula = null;
        try{
            fotoPelicula = crearImagenamigo();
            if( fotoPelicula!=null ){
                Uri urifotoPelicula = FileProvider.getUriForFile(MainActivity.this,
                        "com.ugb.controlesbasicos.fileprovider", fotoPelicula);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, urifotoPelicula);
                startActivityForResult(tomarFotoIntent, 1);
            }else{
                mostrarMsg("No se pudo tomar la foto");
            }
        }catch (Exception e){
            mostrarMsg("Error al abrir la camara"+ e.getMessage());
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imagenBitmap);
            }else{
                mostrarMsg("Se cancelo la toma de la foto");
            }
        }catch (Exception e){
            mostrarMsg("Error al seleccionar la foto"+ e.getMessage());
        }
    }
    private File crearImagenamigo() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_"+fechaHoraMs+"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdirs();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        urlCompletaFoto = image.getAbsolutePath();
        return image;
    }
    private void mostrarDatosPeliculas(){
        try{
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");

            if(accion.equals("modificar")){
                JSONObject jsonObject = new JSONObject(parametros.getString("peliculas")).getJSONObject("value");
                id = jsonObject.getString("_id");
                rev = jsonObject.getString("_rev");
                idPeliculas = jsonObject.getString("idPeliculas");

                tempVal = findViewById(R.id.txtTitulo);
                tempVal.setText(jsonObject.getString("titulo"));

                tempVal = findViewById(R.id.txtSinopsis);
                tempVal.setText(jsonObject.getString("sinopsis"));

                tempVal = findViewById(R.id.txtDuracion);
                tempVal.setText(jsonObject.getString("duracion"));

                tempVal = findViewById(R.id.txtActor);
                tempVal.setText(jsonObject.getString("actor"));

                urlCompletaFoto = jsonObject.getString("urlCompletaFoto");
                Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imagenBitmap);
            }else{//nuevos registros
                idPeliculas = utls.generarIdUnico();
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos peliculas");
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void listarPeliculas(){
        Intent intent = new Intent(getApplicationContext(), lista_amigos.class);
        startActivity(intent);
    }

}