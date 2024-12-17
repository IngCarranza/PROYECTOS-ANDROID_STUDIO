package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class VitaminasActivity extends AppCompatActivity {

    private ListView listView;
    private ListAdapter listAdapter;
    private ArrayList<ListData> dataArrayList = new ArrayList<>();
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vitaminas);

        listView = findViewById(R.id.listview);
        editTextSearch = findViewById(R.id.editTextSearch);

        int[] imageList = {R.drawable.b1, R.drawable.b2, R.drawable.b3, R.drawable.b4, R.drawable.b5, R.drawable.b6, R.drawable.b7, R.drawable.b8};
        int[] ingredientList = {R.string.b1, R.string.b2, R.string.b3, R.string.b4, R.string.b5, R.string.b6, R.string.b7, R.string.b8};
        int[] descList = {R.string.b1Desc, R.string.b2Desc, R.string.b3Desc, R.string.b4Desc, R.string.b5Desc, R.string.b6Desc, R.string.b7Desc, R.string.b8Desc};
        String[] nameList = {"3-A Ofteno", "acticlep-DB", "arcalion 200", "CEREBROFOS", "ELETIN", "FOSFO B-12", "FOSFO neuromax", "FOSKROL"};
        String[] precioList = {"$ 12", "$ 16", "$ 10.50", "$ 12", "$ 18", "$ 20", "$ 15", "$ 12.50"};
        for (int i = 0; i < imageList.length; i++) {
            ListData listData = new ListData(nameList[i], precioList[i], ingredientList[i], descList[i], imageList[i]);
            dataArrayList.add(listData);
        }

        listAdapter = new ListAdapter(VitaminasActivity.this, dataArrayList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(VitaminasActivity.this, DetailedActivity.class);
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

        listAdapter = new ListAdapter(VitaminasActivity.this, filteredList);
        listView.setAdapter(listAdapter);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(VitaminasActivity.this, MainActivity2.class);
        startActivity(intent);
    }
}
