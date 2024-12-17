package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


// MainActivity.java
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivityPersona extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PersonaAdapter adapter;
    private List<Persona> personas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_persona);

        // Agregando el botón para retroceder
        ImageButton regresarButton = findViewById(R.id.regresarbtnpersona2);
        regresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Esto cierra la actividad actual y vuelve al activity anterior
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar lista de personas
        personas = new ArrayList<>();
        personas.add(new Persona("Mauricio Alfredo Carranza", "18 años", "alfred@gmail.com", "Ingeniero", "Farmacia La Popular", R.drawable.img_3));
        personas.add(new Persona("Wesly Ariel Umanzor", "20 años", "wesly@gmail.com", "Ingeniero", "Farmacia La Popular", R.drawable.img_8));
        personas.add(new Persona("Keyla Canales", "20 años", "keyla@gmail.com", "Ingeniera", "Farmacia La Popular", R.drawable.img_5));
        personas.add(new Persona("Erick Josue Chavez", "20 años", "erick@gmail.com", "Ingeniero", "Farmacia La Popular", R.drawable.img_6));
        personas.add(new Persona("Daniel Oswaldo Ramirez", "19 años", "daniel@gmail.com", "Ingeniero", "Farmacia La Popular", R.drawable.img_7));
        // Agregar más personas a la lista...

        adapter = new PersonaAdapter(this, personas);
        recyclerView.setAdapter(adapter);
    }
}
