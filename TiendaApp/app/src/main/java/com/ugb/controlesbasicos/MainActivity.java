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
    String id="", rev="", idProducto="", accion="nuevo";
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

        btnRegresar = findViewById(R.id.fabListaProductos);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regresarLista = new Intent(getApplicationContext(), lista_amigos.class);
                startActivity(regresarLista);
            }
        });
        btn = findViewById(R.id.btnGuardarProducto);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tempVal = findViewById(R.id.txtcodigo);
                    String codigo = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtdescripcion);
                    String descripcion = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtMarca);
                    String marca = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtPresentacion);
                    String presentacion = tempVal.getText().toString();

                    tempVal = findViewById(R.id.txtPrecio);
                    String precio = tempVal.getText().toString();

                    String respuesta = "", actualizado="no";
                    if( di.hayConexionInternet() ) {
                        //obtener datos a enviar al servidor
                        JSONObject datosProductos = new JSONObject();
                        if (accion.equals("modificar")) {
                            datosProductos.put("_id", id);
                            datosProductos.put("_rev", rev);
                        }
                        datosProductos.put("idProducto", idProducto);
                        datosProductos.put("codigo", codigo);
                        datosProductos.put("descripcion", descripcion);
                        datosProductos.put("marca", marca);
                        datosProductos.put("presentacion", presentacion);
                        datosProductos.put("precio", precio);
                        datosProductos.put("urlCompletaFoto", urlCompletaFoto);
                        //enviamos los datos
                        enviarDatosServidor objGuardarDatosServidor = new enviarDatosServidor(getApplicationContext());
                        respuesta = objGuardarDatosServidor.execute(datosProductos.toString()).get();
                        //comprobacion de la respuesta
                        JSONObject respuestaJSONObject = new JSONObject(respuesta);
                        if (respuestaJSONObject.getBoolean("ok")) {
                            id = respuestaJSONObject.getString("id");
                            rev = respuestaJSONObject.getString("rev");
                            actualizado="si";
                        } else {
                            respuesta = "Error al guardar en servidor: " + respuesta;
                        }
                    }
                    String[] datos = new String[]{id, rev, idProducto, codigo, descripcion, marca, presentacion, precio, urlCompletaFoto,actualizado};
                    respuesta = db.administrar_amigos(accion, datos);
                    if (respuesta.equals("ok")) {
                        mostrarMsg("producto registrado con exito.");
                        listarAmigos();
                    } else {
                        mostrarMsg("Error al intentar registrar el producto: " + respuesta);
                    }
                }catch (Exception e){
                    mostrarMsg("Error al guadar datos en el servidor o en SQLite: "+ e.getMessage());
                }
            }
        });
        img = findViewById(R.id.btnImgProducto);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFotoProducto();
            }
        });
        mostrarDatosProducto();
    }
    private void tomarFotoProducto(){
        tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fotoAmigo = null;
        try{
            fotoAmigo = crearImagenamigo();
            if( fotoAmigo!=null ){
                Uri urifotoAmigo = FileProvider.getUriForFile(MainActivity.this,
                        "com.ugb.controlesbasicos.fileprovider", fotoAmigo);
                tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, urifotoAmigo);
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
    private void mostrarDatosProducto(){
        try{
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");

            if(accion.equals("modificar")){
                JSONObject jsonObject = new JSONObject(parametros.getString("productos")).getJSONObject("value");
                id = jsonObject.getString("_id");
                rev = jsonObject.getString("_rev");
                idProducto = jsonObject.getString("idProducto");

                tempVal = findViewById(R.id.txtcodigo);
                tempVal.setText(jsonObject.getString("codigo"));

                tempVal = findViewById(R.id.txtdescripcion);
                tempVal.setText(jsonObject.getString("descripcion"));

                tempVal = findViewById(R.id.txtMarca);
                tempVal.setText(jsonObject.getString("marca"));

                tempVal = findViewById(R.id.txtPresentacion);
                tempVal.setText(jsonObject.getString("presentacion"));

                tempVal = findViewById(R.id.txtPrecio);
                tempVal.setText(jsonObject.getString("precio"));

                urlCompletaFoto = jsonObject.getString("urlCompletaFoto");
                Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                img.setImageBitmap(imagenBitmap);
            }else{//nuevos registros
                idProducto = utls.generarIdUnico();
            }
        }catch (Exception e){
            mostrarMsg("Error al mostrar los datos productos");
        }
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void listarAmigos(){
        Intent intent = new Intent(getApplicationContext(), lista_amigos.class);
        startActivity(intent);
    }
}