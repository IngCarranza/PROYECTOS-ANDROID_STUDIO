package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ugb.controlesbasicos.databinding.ActivityDetailedBinding;

public class DetallesPersonas extends AppCompatActivity {

    ActivityDetailedBinding binding;

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
            int image = intent.getIntExtra("image", R.drawable.img_3);

            // Mostrar los datos en los elementos de la vista
            binding.detailName.setText(name);
            binding.detailTime.setText("$ " + price);
            binding.detailIngredients.setText(ingredients);
            binding.detailDesc.setText(desc);
            binding.detailImage.setImageResource(image);
        }

        // Botón de regresar
        binding.regresar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(DetallesPersonas.this, MainActivity2.class);
        startActivity(intent);
    }
}