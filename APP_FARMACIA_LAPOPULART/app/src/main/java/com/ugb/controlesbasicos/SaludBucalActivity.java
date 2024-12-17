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

public class SaludBucalActivity extends AppCompatActivity {

    private ListView listView;
    private ListAdapter listAdapter;
    private ArrayList<ListData> dataArrayList = new ArrayList<>();
    private EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salud_bucal);
        editTextSearch = findViewById(R.id.editTextSearch);

        listView = findViewById(R.id.listview);

        int[] imageList = {R.drawable.sb1, R.drawable.sb2, R.drawable.sb3, R.drawable.sb4, R.drawable.sb5, R.drawable.sb6, R.drawable.sb7, R.drawable.sb8};
        int[] ingredientList = {R.string.sb1, R.string.sb2, R.string.sb3, R.string.sb4, R.string.sb5, R.string.sb6, R.string.sb7, R.string.sb8};
        int[] descList = {R.string.sb1Desc, R.string.sb2Desc, R.string.sb3Desc, R.string.sb4Desc, R.string.sb5Desc, R.string.sb6Desc, R.string.sb7Desc, R.string.sb8Desc};
        String[] nameList = {"Cariax Gingival", "Cepillo KIN", "KIN Interdentales", "Cera KIN", "Clorhexidina Lacer", "COREGA SUPER", "FLUOR-KIN" , "ODDENT Forte" };
        String[] precioList = {"$ 18.27", "$ 3.95", "$ 6.16", "$ 4.89", "$ 8.16", "$ 10.68", "$ 10.42" , "$ 26.88"};
        for (int i = 0; i < imageList.length; i++) {
            ListData listData = new ListData(nameList[i], precioList[i], ingredientList[i], descList[i], imageList[i]);
            dataArrayList.add(listData);
        }

        listAdapter = new ListAdapter(SaludBucalActivity.this, dataArrayList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SaludBucalActivity.this, DetailedActivity.class);
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

        listAdapter = new ListAdapter(SaludBucalActivity.this, filteredList);
        listView.setAdapter(listAdapter);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SaludBucalActivity.this, MainActivity2.class);
        startActivity(intent);
    }
}
