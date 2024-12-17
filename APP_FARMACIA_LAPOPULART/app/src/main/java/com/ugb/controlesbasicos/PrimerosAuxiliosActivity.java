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

public class PrimerosAuxiliosActivity extends AppCompatActivity {

    private ListView listView;
    private ListAdapter listAdapter;
    private ArrayList<ListData> dataArrayList = new ArrayList<>();
    protected EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primeros_auxilios);
        editTextSearch = findViewById(R.id.editTextSearch);

        listView = findViewById(R.id.listview);

        int[] imageList = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p8, R.drawable.p9};
        int[] ingredientList = {R.string.p1, R.string.p2, R.string.p3, R.string.p4, R.string.p5, R.string.p6, R.string.p7, R.string.p8};
        int[] descList = {R.string.p1Desc, R.string.p2Desc, R.string.p3Desc, R.string.p4Desc, R.string.p5Desc, R.string.p6Desc, R.string.p7Desc, R.string.p8Desc};
        String[] nameList = {"ALCOHOL 70°", "Algodon", "locide Solucion", "BUROSOL", "OXIGEN", "CureBand", "MIGASA" , "NexCare" };
        String[] precioList = {"$ 3.25", "$ 1.48", "$ 10.50", "$ 0.40", "$ 2.36", "$ 2.18", "$ 3.43" , "$ 5.52"};
        for (int i = 0; i < imageList.length; i++) {
            ListData listData = new ListData(nameList[i], precioList[i], ingredientList[i], descList[i], imageList[i]);
            dataArrayList.add(listData);
        }

        listAdapter = new ListAdapter(PrimerosAuxiliosActivity.this, dataArrayList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PrimerosAuxiliosActivity.this, DetailedActivity.class);
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

        listAdapter = new ListAdapter(PrimerosAuxiliosActivity.this, filteredList);
        listView.setAdapter(listAdapter);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(PrimerosAuxiliosActivity.this, MainActivity2.class);
        startActivity(intent);
    }
}
