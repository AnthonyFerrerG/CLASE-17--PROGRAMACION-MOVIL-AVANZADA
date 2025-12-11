package com.example.notasapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {

    EditText editNota;
    Button btnGuardar, btnBorrar, btnGuardarExterno;

    private final String FILE_NAME = "nota.txt";
    private final int REQUEST_EXTERNAL_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNota = findViewById(R.id.editNota);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnGuardarExterno = findViewById(R.id.btnGuardarExterno);

        // Al iniciar, intentar cargar archivo interno
        cargarNotaInterna();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarInterno();
            }
        });

        btnBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                borrarArchivo();
            }
        });

        btnGuardarExterno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarExterno();
            }
        });
    }

    // ==============================
    //       ARCHIVO INTERNO
    // ==============================
    private void cargarNotaInterna() {
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            editNota.setText(new String(bytes));
        } catch (Exception e) {
            // No existe archivo, no hacer nada
        }
    }

    private void guardarInterno() {
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(editNota.getText().toString().getBytes());
            fos.close();
            Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }

    private void borrarArchivo() {
        File file = new File(getFilesDir(), FILE_NAME);
        if (file.exists()) {
            file.delete();
            editNota.setText("");
            Toast.makeText(this, "Archivo borrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No existe archivo", Toast.LENGTH_SHORT).show();
        }
    }

    // ==============================
    //   ARCHIVO EXTERNO
    // ==============================
    private void guardarExterno() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_EXTERNAL_PERMISSION
            );

            return;
        }

        try {
            File dir = Environment.getExternalStorageDirectory();
            File archivo = new File(dir, FILE_NAME);

            FileOutputStream fos = new FileOutputStream(archivo);
            fos.write(editNota.getText().toString().getBytes());
            fos.close();

            Toast.makeText(this, "Guardado en almacenamiento externo", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error en almacenamiento externo", Toast.LENGTH_SHORT).show();
        }
    }
}
