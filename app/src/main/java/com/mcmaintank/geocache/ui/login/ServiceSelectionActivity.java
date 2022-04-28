package com.mcmaintank.geocache.ui.login;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mcmaintank.geocache.R;
import com.mcmaintank.geocache.data.model.Geocache;
import com.mcmaintank.geocache.data.model.UserInfoShp;
import com.mcmaintank.geocache.ui.geocache.CreateGeocacheActivity;
import com.mcmaintank.geocache.ui.geocache.GetGeocacheActivity;
import com.mcmaintank.geocache.ui.geocache.ShowGeocacheActivity;
import com.mcmaintank.geocache.ui.geocache.ViewHistoryActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServiceSelectionActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    private String responseString;
    private String username;
    private ArrayList list = new ArrayList();
    private List<Geocache> geocacheList = new ArrayList<Geocache>();
    private LocationManager locationManager;
    private String locationProvider;
    private Double latitudes;
    private Double longitudes;
    private JSONObject jsonObject;
    private Geocache geocache;
    private static UserInfoShp userInfoShp;
    private JSONArray jsonArray;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.d(TAG, "Location changed to: " + getLocationInfo(location));
        }
        public void onProviderDisabled(String provider) {
            Log.d(TAG, provider + " disabled.");
        }

        public void onProviderEnabled(String provider) {
            Log.d(TAG, provider + " enabled.");
        }

        public void onStatusChanged(String provider, int status,
                                    Bundle extras){
            Log.d(TAG, provider + " status changed.");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);//设置toolbar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button button_selection_to_find_geocache = findViewById(R.id.button_selection_to_find_geocache);
        Button button_selection_to_create = findViewById(R.id.button_selection_to_create);
        Button button_selection_to_history = findViewById(R.id.button_selection_to_history);
        Button button_selection_to_show_geocache = findViewById(R.id.button_selection_to_show_geocache);
        if (ActivityCompat.checkSelfPermission(ServiceSelectionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ServiceSelectionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(ServiceSelectionActivity.this, "Sorry, we don't have access to your location.", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        if(providers==null)
            return;
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //If the provider is GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //If the provider is network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(ServiceSelectionActivity.this, "No available location provider, there might be no GPS signal in your location", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG,"Your provider is:"+locationProvider.toString());
        try {
            sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        locationManager.requestLocationUpdates(locationProvider,100,5,locationListener);
        try {
            sleep(350);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(isGpsAble(locationManager)){
            Log.i(TAG,"GPS is available");
        }
        if(location!=null){
            Log.i(TAG,"good");
        }else{
            return;
        }
        Log.i(TAG,location.toString());
        latitudes = location.getLatitude();
        longitudes = location.getLongitude();
        button_selection_to_find_geocache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ServiceSelectionActivity.this, GetGeocacheActivity.class);
                startActivity(intent);
            }
        });
        button_selection_to_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ServiceSelectionActivity.this, CreateGeocacheActivity.class);
                startActivity(intent);
            }
        });
        button_selection_to_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = userInfoShp.getUserName(ServiceSelectionActivity.this);
                new Thread(){
                    @Override
                    public void run(){
                        RequestBody requestBody = RequestBody.create("{"+"\"username\":\""+username+"\"}", MediaType.parse("application/json"));
                        Request request = new Request.Builder().url("http://10.0.2.2:8080/getGeocacheByUserId")
                                .post(requestBody).build();
                        okHttpClient = new OkHttpClient();
                        Call call = okHttpClient.newCall(request);
                        Response response = null;
                        try {
                            response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            responseString = response.body().string();
                            Log.i(TAG,"Requested geocache list: "+responseString );
                            jsonArray = new JSONArray(responseString);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Geocache geocache = new Geocache();
                                geocache.setGeocacheId(parseInt(jsonObject.getString("geocacheId")));
                                geocache.setDescription(jsonObject.getString("geocacheLocationDescription"));
                                geocache.setLatitudes(jsonObject.getDouble("geocacheLatitudes"));
                                geocache.setLongitudes(jsonObject.getDouble("geocacheLongitudes"));
                                geocache.setDeleted(jsonObject.getBoolean("deleted"));
                                geocache.setPid(parseInt(jsonObject.getString("pid")));
                                geocacheList.add(geocache);
                            }
//                            System.out.println(geocacheList.get(0).getGeocacheId().toString());
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
//                            list.add(geocacheList);
                            bundle.putString("jsonString",responseString);
                            Log.i(TAG,"Requested geocache list: "+responseString );
                            intent.setClass(ServiceSelectionActivity.this, ViewHistoryActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();

            }
        });

        button_selection_to_show_geocache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run(){
                        RequestBody requestBody = RequestBody.create("{"+"\"Latitudes\":\""+latitudes.toString()+"\",\"Longitudes\":\""+longitudes.toString()+"\"}", MediaType.parse("application/json"));
                        Request request = new Request.Builder().url("http://10.0.2.2:8080/getNearestGeocache")
                                .post(requestBody).build();
                        okHttpClient = new OkHttpClient();
                        Call call = okHttpClient.newCall(request);
                        Response response = null;
                        try {
                            response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            responseString = response.body().string();
                            Log.i(TAG,"Requested geocache list: "+responseString);
                            if(responseString==null){
                                Toast.makeText(ServiceSelectionActivity.this, "We couldn't see any geocache near you, sorry:(", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            jsonArray = new JSONArray(responseString);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Geocache geocache = new Geocache();
                                geocache.setGeocacheId(parseInt(jsonObject.getString("geocacheId")));
                                geocache.setDescription(jsonObject.getString("geocacheLocationDescription"));
                                geocache.setLatitudes(jsonObject.getDouble("geocacheLatitudes"));
                                geocache.setLongitudes(jsonObject.getDouble("geocacheLongitudes"));
                                geocache.setDeleted(jsonObject.getBoolean("deleted"));
                                geocache.setPid(parseInt(jsonObject.getString("pid")));
                                geocacheList.add(geocache);
                            }
//                            System.out.println(geocacheList.get(0).getGeocacheId().toString());
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
//                            list.add(geocacheList);
                            bundle.putString("jsonString",responseString);
                            Log.i(TAG,"Requested geocache list: "+responseString );
                            intent.setClass(ServiceSelectionActivity.this, ShowGeocacheActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        });
    }
    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
    }


    private String getLocationInfo(Location location) {
        String info = "";
        info += "Longitude:" + location.getLongitude();
        info += ", Latitude:" + location.getLatitude();
        if (location.hasAltitude()) {
            info += ", Altitude:" + location.getAltitude();
        }
        if (location.hasBearing()) {
            info += ", Bearing:" + location.getBearing();
        }
        return info;
    }


}

