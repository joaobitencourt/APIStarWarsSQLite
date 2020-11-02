package com.example.starwars;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecebeActivity extends AppCompatActivity {
    private TextView tvclima, tvnomepla,tvterritorio;
    Button btnRed, btnGreen, Historico;
    Toolbar toolbar;
    View View;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recebe);
        tvclima = findViewById(R.id.txtvClima);
        tvnomepla = findViewById(R.id.txtvNomePla);
        tvterritorio = findViewById(R.id.txtvterritorio);
        btnRed = findViewById(R.id.OptionColor);
        btnGreen = findViewById(R.id.OptionChangeBackground);
        Historico = findViewById(R.id.Historico);
        View = findViewById(R.id.View);
        Bundle bundle = getIntent().getExtras(); // resgatar o que vai passar para outra view
        String resultadoString = bundle.getString("resultado");
        String resultadoString2 = bundle.getString("resultado2");
        String resultadoString3 = bundle.getString("resultado3");
        tvnomepla.setText(resultadoString);
        tvclima.setText(resultadoString2);
        tvterritorio.setText(resultadoString3);
        toolbar = findViewById(R.id.toolbarSettings);
        setSupportActionBar(toolbar);
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (getcolor() !=  getResources().getColor(R.color.colorPrimary )) {
            View.setBackgroundColor(getcolor());
            getWindow().setStatusBarColor(getcolor());
        }

    }

    public void storeColor(int color){
        SharedPreferences msharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = msharedPreferences.edit();
        mEditor.putInt("color",color);
        mEditor.apply();
    }

    public int  getcolor(){
        SharedPreferences msharedPreferences = getSharedPreferences("ToolbarColor", MODE_PRIVATE);
        int selectedColor = msharedPreferences.getInt("color", getResources().getColor(R.color.colorPrimary));
        return selectedColor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_settinigs,menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            int num = item.getItemId();
            switch (num) {
                case R.id.OptionColor:
                    getWindow().setStatusBarColor(getResources().getColor(R.color.windonwcolorRed));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.toolbarcolorRed));
                    View.setBackgroundColor(getResources().getColor(R.color.viewcolorRed));
                    storeColor(getResources().getColor(R.color.windonwcolorRed));
                    storeColor(getResources().getColor(R.color.toolbarcolorRed));
                    storeColor(getResources().getColor(R.color.viewcolorRed));
                    Toast.makeText(getApplicationContext(), R.string.dark_side, Toast.LENGTH_LONG).show();
                    break;
                case R.id.OptionChangeBackground:
                    getWindow().setStatusBarColor(getResources().getColor(R.color.windonwcolorBlue));
                    View.setBackgroundColor(getResources().getColor(R.color.viewcolorBlue));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.toolbarcolorBlue));
                    storeColor(getResources().getColor(R.color.windonwcolorBlue));
                    storeColor(getResources().getColor(R.color.toolbarcolorBlue));
                    storeColor(getResources().getColor(R.color.viewcolorBlue));
                    Toast.makeText(getApplicationContext(), R.string.jedi_side, Toast.LENGTH_LONG).show();
                    break;
                case R.id.Historico:
                    startActivity(new Intent(this,StorageActivity.class));
                    break;
                /*default:
                    getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                    View.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    storeColor(getResources().getColor(R.color.colorAccent));
                    break;*/
            }
        return true;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();

            assert children != null;
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile())
            return dir.delete();
        else
            return false;
    }

    public void ScreenShot(View view){
        View view1 = getWindow().getDecorView().getRootView();
        view1.setDrawingCacheEnabled(true);

        Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());
        view1.setDrawingCacheEnabled(false);

        String filePath = Environment.getExternalStorageDirectory()+"/Download/"+ Calendar.getInstance().getTime().toString()+".jpg";
        Log.d("url",filePath);
        File fileScreenShot = new File((filePath));

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileScreenShot);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File dir = getCacheDir();
            deleteDir(dir);
        } catch (Exception ignored) {
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(fileScreenShot);
        intent.setDataAndType(uri,"image/jpeg");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    public void abrirSite(View view)
    {
        Uri uri = Uri.parse("https://m.folha.uol.com.br/asmais/2015/10/1697728-dez-lugares-que-todo-fa-de-star-wars-adoraria-conhecer.shtml");
        Intent it = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(it);

    }


}