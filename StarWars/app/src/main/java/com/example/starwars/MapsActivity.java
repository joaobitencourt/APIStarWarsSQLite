package com.example.starwars;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Spinner SpinnerMaps;
    private Button btnSearchInMaps;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient fusedLocationProvaiderClient;
    private double currentLat = 0, currentLong = 0;
    String[] arraylocais = {"LE Burger", "Gibi Cultura Geek", "Jetis Burger e Grill", "Iron Studios Concept Store", "Mundo Maker "};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SpinnerMaps = findViewById(R.id.SpinnerMaps);
        btnSearchInMaps = findViewById(R.id.btnSearchInMaps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        //array strings
        SpinnerMaps.setAdapter(new ArrayAdapter<>(MapsActivity.this, android.R.layout.simple_spinner_dropdown_item, arraylocais));
        fusedLocationProvaiderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        /*btnSearchInMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pegar a possição do spinner
                int i = SpinnerMaps.getSelectedItemPosition();
                //Construção da url
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + "?location" + currentLat + "," + currentLong + "&radius=5000" + "&types=" + "hospital" + "&sensor=true" + "&key=" + getResources().getString(R.string.google_maps_key);
                new PlaceTask().execute(url);
                Log.d("downloadUrl", url);
            }
        });*/

        }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            Task<Location> task = fusedLocationProvaiderClient.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                        //Se a locaização for diferente de null
                        currentLat = location.getLatitude();
                        currentLong = location.getLongitude();
                        supportMapFragment.getMapAsync( googleMap -> {
                                mMap = googleMap;
                            currentPositionMarker(mMap);
                                // zoom no mapa animation
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLong),10));
                            });
                    }
                }
            });

        }else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
        }
        btnSearchInMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pegar a possição do spinner
                int i = SpinnerMaps.getSelectedItemPosition();
                //Construção da url
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + "?location="
                        + currentLat + "," + currentLong + "&radius=50000"+ "&keyword="
                        + arraylocais[i].replace(" ","%") + "&sensor=true" +
                         "&key="+ getResources().getString(R.string.google_maps_key);

                new PlaceTask().execute(url);
                Log.d("downloadUrl", url);
            }
        });
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            if(requestCode == 44){
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getCurrentLocation();
                }
            }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
      /*  getCurrentLocation();*/

        /*mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng local = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(local).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(local));*/
    }

    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }
    private void currentPositionMarker(GoogleMap map) {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(currentLat, currentLong)).title(getApplicationContext().getString(R.string.Aqui));
        map.addMarker(marker);

    }
    private String downloadUrl(String string) throws IOException {
        URL url = new URL (string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader (new InputStreamReader(stream));
        // criando  a string do bffer
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine())!= null){
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        return data;

    }

    private class ParserTask extends AsyncTask<String,Integer, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //Ciração da classe json
            JSonParser jsonParser = new JSonParser();
            //Inicializa o hash map list
            List<HashMap<String, String>> mapList = null;
            JSONObject object;

            try {
                //Inicializa o jsonObject
                 object = new JSONObject(strings[0]);
                 mapList = jsonParser.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;

        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            mMap.clear();
            for(int i=0; i<hashMaps.size(); i++){
                HashMap<String, String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");
                LatLng latLng =  new LatLng(lat, lng);
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                mMap.addMarker(options);
                Log.d(name,"foi");
            }
            currentPositionMarker(mMap);
        }
    }
}