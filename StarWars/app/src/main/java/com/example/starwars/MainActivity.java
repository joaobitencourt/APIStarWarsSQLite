package com.example.starwars;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private TextView tvclima, tvnomepla, tvLoading, tvterritorio;
    private EditText EtNome;
    private TextInputLayout textInputPersonalName;
    private static final String fileName = "lastsPlanetsSearched.txt";

    // private Button btnprocurar;
    String nomeplaneta;
    String climaplaneta, territorio;
    Button buttonOpenViewLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvclima = findViewById(R.id.txtvClima);
        buttonOpenViewLocation = findViewById(R.id.btnViewSearch);
        tvnomepla = findViewById(R.id.txtvNomePla);
        tvterritorio = findViewById(R.id.txtvterritorio);
        tvLoading = findViewById(R.id.txtvLoading);
        EtNome = findViewById(R.id.txtDigiteoNome);
        //textInputPersonalName = findViewById(R.id.textInoputPersonName);
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

        buttonOpenViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this,QualLugarActivity.class));
            }
        });
    }

   /* public boolean inputPlanetValidation(){
        String txtDplanet = textInputPersonalName.getEditText().getText().toString().trim();

        if(txtDplanet == null){
            textInputPersonalName.setError("Esse campo não pode ser vasio");
            return  false;
        }else{
            textInputPersonalName.setError(null);
            return true;
        }
    }*/

    /*public void ConfirmaInout(View v){
        if(!inputPlanetValidation()){
            return;
        }
        String input = R.string.DPlaneta + textInputPersonalName.getEditText().getText().toString();
        input += "\n";

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }*/


    public void Receba (View view ){
       /* Intent receba = new Intent(MainActivity.this, RecebeActivity.class);
        //receba.putExtra("resultado", txtResu);
        startActivity(receba);*/
    }

    public void procuraPlanetas(View view) {
        String text = EtNome.getText().toString();
        FileOutputStream fileOutputStream = null;
        // Recupera a string de busca.
        String queryString = EtNome.getText().toString();
        // esconde o teclado qdo o botão é clicado
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        // Verifica o status da conexão de rede
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        /* Se a rede estiver disponivel e o campo de busca não estiver vazio
         iniciar o Loader CarregaLivros */
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            Bundle queryBundle = new Bundle();
            Log.d("uraa", queryString);
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            Toast.makeText(getApplicationContext(), R.string.loading, Toast.LENGTH_LONG).show();
            /*tvLoading.setText(R.string.loading);*/
            try {
                fileOutputStream = openFileOutput(fileName,MODE_PRIVATE);
                fileOutputStream.write(text.getBytes());
                if(EtNome.getText() == null){
                    tvclima.setText("");
                    tvnomepla.setText("");
                    tvterritorio.setText("");
                }
                EtNome.getText().clear();
                Toast.makeText(this,"The File Path is:" + getFilesDir() + "/" + fileName, Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(fileOutputStream != null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }/*else if(nomeplaneta != null){

            tvLoading.setText("");
        }*/
        // atualiza a textview para informar que não há conexão ou termo de busca
        else {
            if (queryString.length() == 0) {
                Toast.makeText(getApplicationContext(), R.string.no_search_term, Toast.LENGTH_LONG).show();
                /*tvLoading.setText(R.string.no_search_term);*/
            } else {
                Toast.makeText(getApplicationContext(), R.string.no_network, Toast.LENGTH_LONG).show();
                /*tvLoading.setText(R.string.no_network);*/
            }
        }
            //InternalStorage code - save on file


    }
    //Load internalStorage - Read the file
    public void load(View view){
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String text;
            while((text = bufferedReader.readLine()) != null){
                stringBuilder.append(text).append("\n");
            }

            EtNome.setText(stringBuilder.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fileInputStream != null){
                try {

                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {

        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }
        Log.d("uraa2", queryString);
        return new BuscaPlanetas(this, queryString);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            // Converte a resposta em Json
            JSONObject jsonObject = new JSONObject(data);
            Log.d("uraa3", jsonObject.toString());
            // Obtem o JSONArray dos itens dos Planetas
            JSONArray itemsArray = jsonObject.getJSONArray("results");
            Log.d("Arrayfunfo", itemsArray.toString());
            JSONObject balinha = new JSONObject(itemsArray.get(0).toString());
            Log.d("balinha", balinha.toString());
             nomeplaneta = balinha.getString("name");
             climaplaneta = balinha.getString("climate");
             territorio = balinha.getString("terrain");
            Log.d("balinhaname", nomeplaneta.toString());
            Log.d("balinhaclimate", climaplaneta.toString());
            Intent receba = new Intent(MainActivity.this, RecebeActivity.class);
            receba.putExtra("resultado", nomeplaneta);
            receba.putExtra("resultado2", climaplaneta);
            receba.putExtra("resultado3", territorio);
            DataBase dataBase = new DataBase(this);
            Planetas p = new Planetas();
            p.setPlanete(nomeplaneta);
            p.setGround(territorio);
            p.setClimate(climaplaneta);
            dataBase.insertContact(p);
            startActivity(receba);
        } catch (Exception e) {
            // Se não receber um JSOn valido, informa ao usuário
            Toast.makeText(getApplicationContext(), R.string.no_results, Toast.LENGTH_LONG).show();
           /* tvLoading.setText(R.string.no_results);*/
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }


}


