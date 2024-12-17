package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ugb.controlesbasicos.databinding.ActivityDetailedBinding;

public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;
    double total = 0.0; // Total inicial
    int quantity = 1; // Cantidad inicial

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        if (intent != null) {
            // Obtener datos de la receta
            String name = intent.getStringExtra("name");
            double price = Double.parseDouble(intent.getStringExtra("precio").replace("$ ", ""));
            String ingredients = intent.getStringExtra("ingredients");
            String desc = intent.getStringExtra("desc");
            int image = intent.getIntExtra("image", R.drawable.cuatrol);

            // Mostrar los datos en la interfaz
            binding.detailName.setText(name);
            binding.detailTime.setText("$ " + String.format("%.2f", price));
            binding.detailDesc.setText(desc);
            binding.detailIngredients.setText(ingredients);
            binding.detailImage.setImageResource(image);
        }

             // ESTE ES EL BOTON QUE HICE PARA RETROCEDER
            ImageButton regresarButton = findViewById(R.id.regresar2);
            regresarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Esto cerrará la actividad actual y volverá al activity anterior
                }
            });


            // Lógica para agregar al carrito (sin cantidad)
            binding.addToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddToCartClicked(v);
                }
            });
        }

    public void onAddToCartClicked(View view) {
        // Obtener los datos necesarios para enviar a AgregarAlCarro
        String name = binding.detailName.getText().toString();
        double price = Double.parseDouble(binding.detailTime.getText().toString().replace("$ ", ""));
        String ingredients = binding.detailIngredients.getText().toString();
        String desc = binding.detailDesc.getText().toString();
        int image = binding.detailImage.getDrawable().getConstantState().hashCode();

        // Crear un Intent para navegar a AgregarAlCarro
        Intent intent = new Intent(DetailedActivity.this, AgregarAlCarrito.class);
        intent.putExtra("name", name);
        intent.putExtra("precio", String.valueOf(price));
        intent.putExtra("ingredients", ingredients);
        intent.putExtra("desc", desc);
        intent.putExtra("image", image);

        startActivity(intent);
    }
}