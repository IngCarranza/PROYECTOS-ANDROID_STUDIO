package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// DetallePersonaActivity.java
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetallePersonaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_persona);

        ImageView imgFoto = findViewById(R.id.imgFoto);
        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtEdad = findViewById(R.id.txtEdad);   // Verificar que ID coincide con XML
        TextView txtCorreo = findViewById(R.id.txtCorreo); // Verificar que ID coincide con XML
        TextView txtProfesion = findViewById(R.id.txtProfesion);
        TextView txtTrabajoProfesional = findViewById(R.id.txtTrabajoProfesional);

        // Obtener los datos del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imgFoto.setImageResource(extras.getInt("foto"));
            txtNombre.setText(extras.getString("nombre"));
            txtEdad.setText(extras.getString("edad"));
            txtCorreo.setText(extras.getString("correo"));
            txtProfesion.setText(extras.getString("profesion"));
            txtTrabajoProfesional.setText(extras.getString("trabajoProfesional"));
        }

        // ESTE ES EL BOTON QUE HICE PARA RETROCEDER
        ImageButton regresarButton = findViewById(R.id.regresarbtnpersona);
        regresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Esto lo hice para que me cierre la actividad actual y volverá al activity anterior
            }
        });
    }
}
