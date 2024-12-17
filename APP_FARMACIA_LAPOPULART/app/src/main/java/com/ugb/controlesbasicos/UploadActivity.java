package com.ugb.controlesbasicos;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class UploadActivity extends AppCompatActivity {

    EditText uploadName, uploadEmail;
    ImageView uploadImage;
    Button saveButton;
    ImageButton regresarButton;
    private Uri uri;
    private Bitmap bitmapImage;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadEmail = findViewById(R.id.uploadEmail);
        uploadImage = findViewById(R.id.uploadImage);
        uploadName = findViewById(R.id.uploadName);
        saveButton = findViewById(R.id.saveButton);
        regresarButton = findViewById(R.id.regresar2); // Nuevo botón regresar
        dbHelper = new DBHelper(this);

        // Configuración del launcher para seleccionar imágenes
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            assert data != null;
                            uri = data.getData();
                            try {
                                bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            } catch (IOException e) {
                                Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            uploadImage.setImageBitmap(bitmapImage);
                        } else {
                            Toast.makeText(UploadActivity.this, "Ninguna imagen seleccionada", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // Configuración del click listener para seleccionar una imagen
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    activityResultLauncher.launch(intent);
                } catch (Exception e) {
                    Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configuración del click listener para guardar los datos
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeImage();
            }
        });

        // Configuración del click listener para el botón regresar
        regresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    // Método para almacenar la imagen y los datos
    private void storeImage() {
        if (!uploadName.getText().toString().isEmpty() && !uploadEmail.getText().toString().isEmpty()
                && uploadImage.getDrawable() != null && bitmapImage != null) {

            dbHelper.storeData(new ModelClass(uploadName.getText().toString(), uploadEmail.getText().toString(), bitmapImage));
            Intent intent = new Intent(UploadActivity.this, MainActivity2.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }
    }
}
