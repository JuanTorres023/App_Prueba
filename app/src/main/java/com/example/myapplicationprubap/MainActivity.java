package com.example.myapplicationprubap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationprubap.Modelo.Idioma;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.TranslatorOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
// variables tipo y su identificador
    EditText Et_Idioma_Origen;
    TextView Tv_Idioma_Destino;
    MaterialButton Btn_elegir_idioma, Btn_Idioma_Elegido, Btn_Traducir;

    private ProgressDialog progressDialog;
    private ArrayList<Idioma> IdiomasArrayList;

    private String codigo_idioma_origen = "es";
    private String titulo_idioma_origen ="Español";

    private String codigo_idioma_destino = "en";
    private String titulo_idioma_destino ="English";
    private TranslatorOptions translatorOptions;
    private Translator translator;
    private String Texto_idioma_Origen = "";

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
                //Toast.makeText(MainActivity.this, "Elegir idioma", Toast.LENGTH_SHORT).show();
                Elegiridiomaorigen();
            }
        });

        Btn_Idioma_Elegido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Idioma elegido", Toast.LENGTH_SHORT).show();
                Elegiridiomadestino();
            }
        });

        Btn_Traducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "Traducir", Toast.LENGTH_SHORT).show();
                ValidarDatos();
            }
        });







    }

    private void inicializarVistas(){
        Et_Idioma_Origen = findViewById(R.id.Et_Idioma_Origen);
        Tv_Idioma_Destino = findViewById(R.id.Tv_Idioma_Destino);
        Btn_elegir_idioma = findViewById(R.id.Btn_elegir_idioma);
        Btn_Idioma_Elegido = findViewById(R.id.Btn_Idioma_Elegido);
        Btn_Traducir = findViewById(R.id.Btn_Traducir);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Cargando...");
        progressDialog.setCanceledOnTouchOutside(false);
    }


    private void IdiomasDisponibles() {
        IdiomasArrayList = new ArrayList<Idioma>();
        List<String> ListaCodigoIdioma = TranslateLanguage.getAllLanguages();

        for(String codigo_lenguaje : ListaCodigoIdioma){
            String titulo_lenguaje = new Locale(codigo_lenguaje).getDisplayLanguage();

            // Log.d(REGISTROS, "iIdiomasDisponibles: codigo_lenguaje: " + codigo_lenguaje);
            // Log.d(REGISTROS, "iIdiomasDisponibles: titulo_lenguaje: " + titulo_lenguaje);

            Idioma modelIdioma = new Idioma(codigo_lenguaje,titulo_lenguaje);
            IdiomasArrayList.add(modelIdioma);
        }

    }

    private void Elegiridiomaorigen(){
        PopupMenu popupMenu = new PopupMenu(this, Btn_elegir_idioma);
        for(int i=0; i<IdiomasArrayList.size(); i++){
            popupMenu.getMenu().add(Menu.NONE, i, i, IdiomasArrayList.get(i).getTitulo_idioma());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int position = item.getItemId();

                codigo_idioma_origen = IdiomasArrayList.get(position).getCodigo_idioma();
                titulo_idioma_origen = IdiomasArrayList.get(position).getTitulo_idioma();

                Btn_elegir_idioma.setText(titulo_idioma_origen);
                Et_Idioma_Origen.setHint("Ingrese texto en: "+titulo_idioma_origen);


                Log.d(REGISTROS, "onMenuItemClick: codigo_idioma_origen: " + codigo_idioma_origen);
                Log.d(REGISTROS, "onMenuItemClick: titulo_idioma_origen: " + titulo_idioma_origen);

                return false;
            }
        });
    }


    private void Elegiridiomadestino() {
        PopupMenu popupMenu = new PopupMenu(this, Btn_Idioma_Elegido);
        for (int i = 0; i < IdiomasArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, IdiomasArrayList.get(i).getTitulo_idioma());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int position = item.getItemId();

                codigo_idioma_destino = IdiomasArrayList.get(position).getCodigo_idioma();
                titulo_idioma_destino = IdiomasArrayList.get(position).getTitulo_idioma();

                Btn_Idioma_Elegido.setText(titulo_idioma_destino);
                Et_Idioma_Origen.setHint("Ingrese texto en: " + titulo_idioma_destino);


                Log.d(REGISTROS, "onMenuItemClick: codigo_idioma_origen: " + codigo_idioma_destino);
                Log.d(REGISTROS, "onMenuItemClick: titulo_idioma_origen: " + titulo_idioma_destino);

                return false;
            }
        });
    }

    private void ValidarDatos(){
        Texto_idioma_Origen = Et_Idioma_Origen.getText().toString().trim();
        Log.d(REGISTROS, "ValidarDatos: Texto_idioma_origen "+ Texto_idioma_Origen);

        if(Texto_idioma_Origen.isEmpty()){
            Toast.makeText(this, "Ingrese texto en: ", Toast.LENGTH_SHORT).show();
        }else {
            TraducirTexto();
        }
    }

    private void TraducirTexto() {
       /* progressDialog.setMessage("Procesando..."  );
        progressDialog.show();

        translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(codigo_idioma_origen)
                .setTargetLanguage(codigo_idioma_destino)
                .build();


        translator = Translator.getClient(translatorOptions);

        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        //Toast.makeText(MainActivity.this, "Texto traducido", Toast.LENGTH_SHORT).show();
                     Log.d(REGISTROS, "Eñ áqiete dre ");
                    }
                }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(REGISTROS, "onFailure"+e);
                        Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })

    }*/

}