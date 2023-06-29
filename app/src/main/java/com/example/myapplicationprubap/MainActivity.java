package com.example.myapplicationprubap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationprubap.Modelo.Idioma;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.nl.translate.TranslateLanguage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
// variables tipo y su identificador
    EditText Et_Idioma_Origen;
    TextView Tv_Idioma_Destino;
    MaterialButton Btn_elegir_idioma, Btn_Idioma_Elegido, Btn_Traducir;

    private ArrayList<Idioma> IdiomasArrayList;

    private static final String REGISTROS = "Mis registros";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarVistas();
        IdiomasDisponibles();

        Btn_elegir_idioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Elegir idioma", Toast.LENGTH_SHORT).show();
            }
        });

        Btn_Idioma_Elegido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Idioma elegido", Toast.LENGTH_SHORT).show();
            }
        });

        Btn_Traducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Traducir", Toast.LENGTH_SHORT).show();
            }
        });







    }

    private void inicializarVistas(){
        Et_Idioma_Origen = findViewById(R.id.Et_Idioma_Origen);
        Tv_Idioma_Destino = findViewById(R.id.Tv_Idioma_Destino);
        Btn_elegir_idioma = findViewById(R.id.Btn_elegir_idioma);
        Btn_Idioma_Elegido = findViewById(R.id.Btn_Idioma_Elegido);
        Btn_Traducir = findViewById(R.id.Btn_Traducir);
    }


    private void IdiomasDisponibles() {
        IdiomasArrayList = new ArrayList<Idioma>();
        List<String> ListaCodigoIdioma = TranslateLanguage.getAllLanguages();

        for(String codigo_lenguaje : ListaCodigoIdioma){
            String titulo_lenguaje = new Locale(codigo_lenguaje).getDisplayLanguage();

            Log.d(REGISTROS, "iIdiomasDisponibles: codigo_lenguaje: " + codigo_lenguaje);
            Log.d(REGISTROS, "iIdiomasDisponibles: titulo_lenguaje: " + titulo_lenguaje);

            Idioma modelIdioma = new Idioma(codigo_lenguaje,titulo_lenguaje);
            IdiomasArrayList.add(modelIdioma);
        }

    }




}