package com.ugb.controlesbasicos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.FileOutputStream;
import com.itextpdf.layout.Document;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;

public class AgregarAlCarrito extends AppCompatActivity {

    private String nombreProducto;
    private double precioProducto;
    private int cantidad = 1; // Cantidad inicial
    private static final int REQUEST_WRITE_PERMISSION = 786;

    private EditText numeroTarjetaEditText;
    private Spinner metodoPagoSpinner, subMetodoPagoSpinner;
    private String metodoPago, subMetodoPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_al_carrito);

        Intent intent = getIntent();
        nombreProducto = intent.getStringExtra("name");
        String precioProductoStr = intent.getStringExtra("precio");

        // Verificamos si el precio contiene el símbolo "$"
        if (precioProductoStr != null && precioProductoStr.startsWith("$")) {
            // Eliminamos el símbolo "$" antes de convertir a double
            precioProductoStr = precioProductoStr.substring(1);
        }

        precioProducto = Double.parseDouble(precioProductoStr);

        TextView nombreProductoTextView = findViewById(R.id.nombreProductoTextView);
        TextView precioProductoTextView = findViewById(R.id.precioProductoTextView);
        TextView quantityTextView = findViewById(R.id.quantityTextView);
        TextView totalTextView = findViewById(R.id.totalTextView);
        Button generarPDFButton = findViewById(R.id.generarPDFButton);
        ImageButton masButton = findViewById(R.id.masButton);
        ImageButton menosButton = findViewById(R.id.menosButton);
        numeroTarjetaEditText = findViewById(R.id.numeroTarjetaEditText);
        metodoPagoSpinner = findViewById(R.id.metodoPagoSpinner);
        subMetodoPagoSpinner = findViewById(R.id.subMetodoPagoSpinner);

        // Configuramos el Spinner de Métodos de Pago
        ArrayAdapter<CharSequence> metodoPagoAdapter = ArrayAdapter.createFromResource(this,
                R.array.metodos_pago, android.R.layout.simple_spinner_item);
        metodoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metodoPagoSpinner.setAdapter(metodoPagoAdapter);

        metodoPagoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                metodoPago = parent.getItemAtPosition(position).toString();
                ArrayAdapter<CharSequence> subMetodoPagoAdapter = null;

                if (metodoPago.equals("Tarjeta de Crédito")) {
                    numeroTarjetaEditText.setVisibility(View.VISIBLE);
                    subMetodoPagoAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                            R.array.tarjetas_credito, android.R.layout.simple_spinner_item);

                } else {
                    numeroTarjetaEditText.setVisibility(View.GONE);
                    subMetodoPagoSpinner.setVisibility(View.GONE);
                }

                if (subMetodoPagoAdapter != null) {
                    subMetodoPagoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subMetodoPagoSpinner.setAdapter(subMetodoPagoAdapter);
                    subMetodoPagoSpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        subMetodoPagoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subMetodoPago = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        nombreProductoTextView.setText(nombreProducto);
        precioProductoTextView.setText("$" + precioProductoStr);
        updateTotal(totalTextView);

        masButton.setOnClickListener(v -> {
            cantidad++;
            quantityTextView.setText(String.valueOf(cantidad));
            updateTotal(totalTextView);
        });

        menosButton.setOnClickListener(v -> {
            if (cantidad > 1) {
                cantidad--;
                quantityTextView.setText(String.valueOf(cantidad));
                updateTotal(totalTextView);
            }
        });

        generarPDFButton.setOnClickListener(v -> {
            Log.d("AgregarAlCarrito", "Botón Generar PDF presionado");
            String nombreCliente = ((EditText) findViewById(R.id.nombreClienteEditText)).getText().toString();
            String correoCliente = ((EditText) findViewById(R.id.correoClienteEditText)).getText().toString();
            String telefonoCliente = ((EditText) findViewById(R.id.telefonoClienteEditText)).getText().toString();
            String numeroTarjeta = numeroTarjetaEditText.getText().toString();
            double total = cantidad * precioProducto;

            // Verificamos si todos los campos están llenos
            if (nombreCliente.isEmpty() || correoCliente.isEmpty() || telefonoCliente.isEmpty()) {
                Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            } else {
                generarPDF(nombreCliente, correoCliente, telefonoCliente, nombreProducto, precioProducto, cantidad, total, metodoPago, subMetodoPago, numeroTarjeta);
            }
        });

        // ESTE ES EL BOTON QUE HICE PARA RETROCEDER
        ImageButton regresarButton = findViewById(R.id.regresarbtn);
        regresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Esto lo hice para que me cierre la actividad actual y volverá al activity anterior
            }
        });
    }

        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            String nombreCliente = ((EditText) findViewById(R.id.nombreClienteEditText)).getText().toString();
            String correoCliente = ((EditText) findViewById(R.id.correoClienteEditText)).getText().toString();
            String telefonoCliente = ((EditText) findViewById(R.id.telefonoClienteEditText)).getText().toString();
            String numeroTarjeta = numeroTarjetaEditText.getText().toString();
            double total = cantidad * precioProducto;
            generarPDF(nombreCliente, correoCliente, telefonoCliente, nombreProducto, precioProducto, cantidad, total, metodoPago, subMetodoPago, numeroTarjeta);
        }
    }

    private void updateTotal(TextView totalTextView) {
        double total = cantidad * precioProducto;
        totalTextView.setText("Total: $" + String.format("%.2f", total));
    }

    private void generarPDF(String nombreCliente, String correoCliente, String telefonoCliente, String nombreProducto, double precioProducto, int cantidad, double total, String metodoPago, String subMetodoPago, String numeroTarjeta) {
        try {
            String filePath = getExternalFilesDir(null) + "/Factura.pdf";
            Log.d("AgregarAlCarrito", "Ruta del archivo PDF: " + filePath);

            PdfWriter writer = new PdfWriter(new FileOutputStream(filePath));
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("\nINFORMACION DEL CLIENTE:"));
            document.add(new Paragraph("\nNombre: " + nombreCliente));
            document.add(new Paragraph("Correo Electrónico: " + correoCliente));
            document.add(new Paragraph("Teléfono: " + telefonoCliente));
            document.add(new Paragraph("\nINFORMACION DEL PRODUCTO:"));
            document.add(new Paragraph("\nNombre del Producto: " + nombreProducto));
            document.add(new Paragraph("Precio del Producto: $" + precioProducto));
            document.add(new Paragraph("Cantidad: " + cantidad));
            document.add(new Paragraph("Total: $" + String.format("%.2f", total)));
            document.add(new Paragraph("\nMETODO DE PAGO:"));
            document.add(new Paragraph("\nTipo de pago: " + metodoPago));
            document.add(new Paragraph("Tipo de targeta: " + subMetodoPago));

            if (metodoPago.equals("Tarjeta de Crédito")) {
                document.add(new Paragraph("Número de Tarjeta: " + numeroTarjeta));
            }

            Toast.makeText(this, "La factura esta siendo lista para enviar...", Toast.LENGTH_SHORT).show();

            document.close();



            Log.d("AgregarAlCarrito", "PDF generado correctamente");
            enviarPDFPorCorreo(correoCliente, filePath);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AgregarAlCarrito", "Error al generar PDF", e);
        }
    }

    private void enviarPDFPorCorreo(String correo, String filePath) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("application/pdf");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{correo});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Factura de Compra");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Adjunto encontrarás la factura de tu compra.");

            Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", new File(filePath));
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(emailIntent, "Enviar Email"));
            Log.d("AgregarAlCarrito", "Correo enviado correctamente con el PDF adjunto");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AgregarAlCarrito", "Error al enviar PDF por correo", e);
        }
    }
}
