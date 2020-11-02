package com.example.starwars;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class QualLugarActivity extends AppCompatActivity {
private Spinner spinnerLocais;
private Button btnSearchLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qual_lugar);
        btnSearchLocation = findViewById(R.id.btnSearchLocation);
        /*spinnerLocais.setAdapter(ArrayAdapter.createFromResource(QualLugarActivity.this, R.array.opcoeslocais, R.layout.support_simple_spinner_dropdown_item));
        spinnerLocais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        btnSearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QualLugarActivity.this,MapsActivity.class));
            }
        });
    }

    public void OpenMoreCuriosites(View view)
    {
        Uri uri = Uri.parse("https://quantocustaviajar.com/blog/lugares-para-fas-de-star-wars/");
        Intent it = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(it);

    }
}

