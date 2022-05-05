package com.mcmaintank.geocache.ui.geocache;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mcmaintank.geocache.R;
import com.mcmaintank.geocache.data.model.Geocache;
import com.mcmaintank.geocache.data.model.UserInfoShp;

import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateGeocacheActivity extends AppCompatActivity {

    private EditText editText;
    private OkHttpClient okHttpClient;
    private String responseString;
    private LocationManager locationManager;
    private String locationProvider;
    private static UserInfoShp userInfoShp;
    private Geocache geocache = new Geocache();
    private Integer generatedKey = new Integer(0);
    private Double latitudes;
    private Double longitudes;
    private JSONObject jsonObject;
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
        setContentView(R.layout.activity_create_geocache);
        Button button_1 = findViewById(R.id.button3);
        editText = findViewById(R.id.location_description);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(CreateGeocacheActivity.this).setTitle("Are you sure you want to submit? You can still make amends to your description after you submit.")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (ActivityCompat.checkSelfPermission(CreateGeocacheActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateGeocacheActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    Toast.makeText(CreateGeocacheActivity.this, "Sorry, we don't have access to your location.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(CreateGeocacheActivity.this, "No available location provider, there might be no GPS signal in your location", Toast.LENGTH_SHORT).show();
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
                                String geocacheDescription = editText.getText().toString();
                                String username = userInfoShp.getUserName(CreateGeocacheActivity.this);
                                String creating = "Creating new geocache...";
                                Toast.makeText(getApplicationContext(),creating, Toast.LENGTH_LONG).show();
//                                new Thread() {
//                                    @Override
//                                    public void run() {
                                if(location!=null){
                                    Log.i(TAG,"good");
                                }else{
                                    return;
                                }
                                Log.i(TAG,location.toString());
                                latitudes = location.getLatitude();
                                longitudes = location.getLongitude();
                                Log.i(TAG,latitudes.toString()+longitudes.toString());
                                geocache.setLongitudes(longitudes);
                                geocache.setLatitudes(latitudes);
                                geocache.setDescription(geocacheDescription);
                                new Thread(){
                                    @Override
                                    public void run(){

                                        RequestBody requestBody = RequestBody.create("{"+"\"username\":\""+username+"\",\"Latitudes\":\""+latitudes.toString()+"\",\"Longitudes\":\""+longitudes.toString()+"\",\"Description\":\""+geocacheDescription+"\"}", MediaType.parse("application/json"));
                                        Request request = new Request.Builder().url("http://39.105.14.129:8080/createGeocache")
                                                .post(requestBody).build();
                                        okHttpClient = new OkHttpClient();
                                        Call call = okHttpClient.newCall(request);
                                        Response response = null;
                                        try {
                                            response= call.execute();
                                            responseString = response.body().string();
                                            jsonObject = new JSONObject(responseString);
                                            setGeneratedKey(parseInt(jsonObject.getString("generatedKey")));
                                            generatedKey = getGeneratedKey();
                                            geocache.setGeocacheId(getGeneratedKey());
                                            Log.i(TAG,generatedKey.toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
//                                        Log.i(TAG,"Beacon"+responseString);
//                                        String success = "You have successfully connected to the server, jumping...";
//                                        Toast.makeText(getApplicationContext(),success, Toast.LENGTH_LONG).show();
//                                        if(generatedKey!=0){
//                                            Looper.prepare();
//                                            String success_1 = "You have successfully created a geocache!";
//                                            Toast.makeText(getApplicationContext(),success_1, Toast.LENGTH_LONG).show();
//                                            Looper.loop();
//                                        }else{
//                                            Looper.prepare();
//                                            String failed = "Something went wrong, you may want to check your description.";
//                                            Toast.makeText(getApplicationContext(),failed, Toast.LENGTH_LONG).show();
//                                            Looper.loop();
//
//                                        }
                                        generatedKey = getGeneratedKey();
                                        Log.i(TAG,generatedKey.toString());
                                        if(getGeneratedKey()!=0){
//                                            String success_1 = "You have successfully created a geocache!";
//                                            Toast.makeText(getApplicationContext(),success_1, Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(CreateGeocacheActivity.this,ViewGeocacheActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("geocacheId",getGeneratedKey());
                                            bundle.putString("geocacheLocationDescription",geocache.getDescription());
                                            bundle.putDouble("geocacheLatitudes",geocache.getLatitudes());
                                            bundle.putDouble("geocacheLongitudes",geocache.getLongitudes());
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }else{
//                                            String failed = "Something went wrong, you may want to check your description.";
//                                            Toast.makeText(getApplicationContext(),failed, Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                    }
                                }.start();


//                                //TODO:Enqueue the call and get results
//                                call.enqueue(new Callback() {
//                                    @Override
//                                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                                        Log.i(TAG,"Failed to communicate with server.");
//                                    }
//
//                                    @Override
//                                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

//                                    }
//                                });
//                                    }
//                                }.start();


                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).show();

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

    private void setGeneratedKey(Integer generatedKey){
        this.generatedKey = generatedKey;
    }

    private int getGeneratedKey(){
        return this.generatedKey;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
