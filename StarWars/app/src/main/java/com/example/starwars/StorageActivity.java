package com.example.starwars;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StorageActivity extends AppCompatActivity {
public RecyclerView planetsStorage;
    Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        planetsStorage = findViewById(R.id.planetsStorage);
        DataBase dataBase = new DataBase(this);
        planetsStorage.setLayoutManager(new LinearLayoutManager(this));
        planetsStorage.setNestedScrollingEnabled(true);
        planetsStorage.addItemDecoration(new DividerItemDecoration(planetsStorage.getContext(),LinearLayout.HORIZONTAL));
        planetsStorage.setAdapter(new Adapter(dataBase.getAllContacts()));
        toolbar = findViewById(R.id.toolbardelete);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_delete,menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int num = item.getItemId();
        switch (num) {
            case R.id.deletAll:
               DataBase dataBase = new DataBase(this);
               dataBase.deleteAll();
                planetsStorage.setAdapter(new Adapter(dataBase.getAllContacts()));
                planetsStorage.getAdapter().notifyDataSetChanged();
                break;

        }
        return true;
    }
}