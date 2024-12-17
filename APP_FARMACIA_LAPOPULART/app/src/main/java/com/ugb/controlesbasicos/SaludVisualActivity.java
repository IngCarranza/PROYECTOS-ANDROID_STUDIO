package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class SaludVisualActivity extends AppCompatActivity {

    private ListView listView;
    private ListAdapter listAdapter;
    private ArrayList<ListData> dataArrayList = new ArrayList<>();
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salud_visual);
        editTextSearch = findViewById(R.id.editTextSearch);

        listView = findViewById(R.id.listview);

        int[] imageList = {R.drawable.v1, R.drawable.v2, R.drawable.v3, R.drawable.v4, R.drawable.v5, R.drawable.v6, R.drawable.v7, R.drawable.v8};
        int[] ingredientList = {R.string.g1, R.string.g2, R.string.g3, R.string.g4, R.string.g5, R.string.g6, R.string.g7, R.string.g8};
        int[] descList = {R.string.v1Desc, R.string.v2Desc, R.string.v3Desc, R.string.v4Desc, R.string.v5Desc, R.string.v6Desc, R.string.v7Desc, R.string.v8Desc};
        String[] nameList = {"ACULAR LS", "AFLREX", "AGGLAD", "Alin Oftalmico", "Brizolam", "Ciprowell", "CLORA MICOL", "3-A Ofteno"};
        String[] precioList = {"$ 11.37", "$ 9", "$ 13.26", "$ 7.18", "$ 35.54", "$ 10", "$ 5.90", "$ 4.85"};
        for (int i = 0; i < imageList.length; i++) {
            ListData listData = new ListData(nameList[i], precioList[i], ingredientList[i], descList[i], imageList[i]);
            dataArrayList.add(listData);
        }

        listAdapter = new ListAdapter(SaludVisualActivity.this, dataArrayList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SaludVisualActivity.this, DetailedActivity.class);
                intent.putExtra("name", nameList[i]);
                intent.putExtra("precio", precioList[i]);
                intent.putExtra("ingredients", getString(ingredientList[i]));
                intent.putExtra("desc", getString(descList[i]));
                intent.putExtra("image", imageList[i]);
                startActivity(intent);
            }
        });

        ImageButton regresarButton = findViewById(R.id.regresar);
        regresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filter(String text) {
        ArrayList<ListData> filteredList = new ArrayList<>();

        for (ListData item : dataArrayList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        listAdapter = new ListAdapter(SaludVisualActivity.this, filteredList);
        listView.setAdapter(listAdapter);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SaludVisualActivity.this, MainActivity2.class);
        startActivity(intent);
    }
}