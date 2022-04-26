package com.mcmaintank.geocache.ui.geocache;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.amap.api.navi.ParallelRoadListener;
import com.mcmaintank.geocache.R;
import com.mcmaintank.geocache.data.model.GeocacheInfoShp;
import com.mcmaintank.geocache.data.model.UserInfoShp;
import com.mcmaintank.geocache.view.ArrowView;
import com.mcmaintank.geocache.view.DirectionView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewGeocacheActivity extends AppCompatActivity {
    private EditText editTextTextID;
    private EditText editTextLatitudes;
    private EditText editTextLongitudes;
    private EditText editTextTextDescription;
    private static UserInfoShp userInfoShp;
    private OkHttpClient okHttpClient;
    private String responseString;
    private Integer status;
    private SensorManager sensorManager;
    private Sensor mSensorOrientation;
    private DirectionView directionView = null;
    private ArrowView arrowView = null;
    private SensorListener sensorListener = new SensorListener();
    private LinearLayout compassLayout;
    private static GeocacheInfoShp geocacheInfoShp;
    private final double EARTH_RADIUS = 6378137.0;
    private TextView distanceView;
    private float azimuth;
    private Double curLat;
    private Double curLon;
    private LocationManager locationManager;
    private String locationProvider;
    private Double tarLat;
    private Double tarLon;
    private Double distance;
    private float azimuth_old = 0;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.d(TAG, "Location changed to: " + getLocationInfo(location));
            curLat = location.getLatitude();
            curLon = location.getLongitude();
            distance = gps2m(curLat,curLon,tarLat,tarLon);
            distanceView.setText(distance.toString());
            distanceView.append("km away");
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



    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geocache);
        Intent intent = getIntent();
        String description = intent.getStringExtra("geocacheLocationDescription");
        Integer id = intent.getIntExtra("geocacheId", 0);
//        Double latitudes = intent.getDoubleExtra("geocacheLatitudes", 404);
//        Double longitudes = intent.getDoubleExtra("geocacheLongitudes", 404);
        tarLat = intent.getDoubleExtra("geocacheLatitudes", 404);
        tarLon = intent.getDoubleExtra("geocacheLongitudes", 404);
//        geocacheInfoShp.setLatitude(ViewGeocacheActivity.this, latitudes.toString());
//        geocacheInfoShp.setLongitude(ViewGeocacheActivity.this, longitudes.toString());
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorOrientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(sensorListener, mSensorOrientation, SensorManager.SENSOR_DELAY_NORMAL);
        Log.i(TAG, mSensorOrientation.toString());
        editTextTextID = findViewById(R.id.editTextTextID);
        editTextLatitudes = findViewById(R.id.editTextLatitudes);
        editTextLongitudes = findViewById(R.id.editTextLongitudes);
        editTextTextDescription = findViewById(R.id.editTextTextDescription);
        distanceView = findViewById(R.id.distanceView);
        editTextTextID.setEnabled(false);
        compassLayout = findViewById(R.id.compassLayout);
        directionView = new DirectionView(ViewGeocacheActivity.this);
        arrowView = new ArrowView(ViewGeocacheActivity.this);
//        compassLayout.addView(directionView);
        compassLayout.addView(arrowView);
        arrowView.bringToFront();
        editTextTextID.setText(id.toString());
        editTextLatitudes.setText(tarLat.toString());
        editTextLongitudes.setText(tarLon.toString());
        editTextTextDescription.setText(description);
        Button button_update = findViewById(R.id.button_update);
        Button button_report = findViewById(R.id.button_report);




        if (ActivityCompat.checkSelfPermission(ViewGeocacheActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ViewGeocacheActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(ViewGeocacheActivity.this, "Sorry, we don't have access to your location.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ViewGeocacheActivity.this, "No available location provider, there might be no GPS signal in your location", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG,"Your provider is:"+locationProvider.toString());
        try {
            sleep(1000);
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
        curLat = location.getLatitude();
        curLon = location.getLongitude();
        distance = gps2m(curLat,curLon,tarLat,tarLon);
        distanceView.setText(distance.toString());
        distanceView.append("km away");
        azimuth = gps2d(curLat,curLon,tarLat,tarLon);
        arrowView.rotate = azimuth-azimuth_old;
        arrowView.setRotation(arrowView.rotate);
        arrowView.postInvalidate();
        Log.i(TAG,""+azimuth);





        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ViewGeocacheActivity.this).setTitle("Are you sure you want to update the description?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread() {
                                    @Override
                                    public void run() {

                                        RequestBody requestBody = RequestBody.create("{" + "\"username\":\"" + userInfoShp.getUserName(ViewGeocacheActivity.this) + "\",\"password\":\"" + userInfoShp.getUserPassword(ViewGeocacheActivity.this) + "\",\"geocacheId\":\"" + editTextTextID.getText().toString() + "\",\"description\":\"" + editTextTextDescription.getText().toString() + "\"}", MediaType.parse("application/json"));
                                        Request request = new Request.Builder().url("http://10.0.2.2:8080/updateGeocache")
                                                .post(requestBody).build();
                                        okHttpClient = new OkHttpClient();
                                        Call call = okHttpClient.newCall(request);
                                        call.enqueue(new Callback() {
                                            @Override
                                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                Log.i(TAG, "Update failed.");
                                            }

                                            @Override
                                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                responseString = response.body().string();
                                            }
                                        });

                                    }
                                }.start();
                                try {
                                    sleep(700);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(responseString);
                                    status = jsonObject.getInt("kstatus");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (status == 1) {
                                    Intent intent = new Intent(ViewGeocacheActivity.this,ViewGeocacheActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("geocacheId",id);
                                    bundle.putString("geocacheLocationDescription",editTextTextDescription.getText().toString());
                                    bundle.putDouble("geocacheLatitudes",tarLat);
                                    bundle.putDouble("geocacheLongitudes",tarLon);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Failed, please update your own geocache.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                }).show();
            }
        });

        button_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ViewGeocacheActivity.this).setTitle("Are you sure you want to report this geocache?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread() {
                                    @Override
                                    public void run() {

                                        RequestBody requestBody = RequestBody.create("{" + "\"username\":\"" + userInfoShp.getUserName(ViewGeocacheActivity.this) + "\",\"password\":\"" + userInfoShp.getUserPassword(ViewGeocacheActivity.this) + "\",\"geocacheId\":\"" + editTextTextID.getText().toString() + "\"}", MediaType.parse("application/json"));
                                        Request request = new Request.Builder().url("http://10.0.2.2:8080/reportGeocache")
                                                .post(requestBody).build();
                                        okHttpClient = new OkHttpClient();
                                        Call call = okHttpClient.newCall(request);
                                        call.enqueue(new Callback() {
                                            @Override
                                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                Log.i(TAG, "Update failed.");
                                            }

                                            @Override
                                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                responseString = response.body().string();
                                            }
                                        });

                                    }
                                }.start();
                                try {
                                    sleep(700);
                                    JSONObject jsonObject = new JSONObject(responseString);
                                    status = jsonObject.getInt("kstatus");
                                    if(status == 1){
                                        Toast.makeText(getApplicationContext(), "Successfully reported.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else{
                                        Toast.makeText(getApplicationContext(), "User does not exist or not successfully logged in.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


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

    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {

        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        super.onResume();
    }

    private final class SensorListener implements SensorEventListener{

        private float predegree = 0;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float degree = sensorEvent.values[0];
            predegree = -degree;
            directionView.rotate = predegree;
            directionView.setRotation(directionView.rotate);
//            directionView.postInvalidate();
            azimuth = gps2d(curLat,curLon,tarLat,tarLon);
            arrowView.rotate = azimuth-azimuth_old;
            arrowView.setRotation(arrowView.rotate);
//            arrowView.postInvalidate();
            azimuth_old = azimuth;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {

        double radLat1 = (lat_a * Math.PI / 180.0);

        double radLat2 = (lat_b * Math.PI / 180.0);

        double a = radLat1 - radLat2;

        double b = (lng_a - lng_b) * Math.PI / 180.0;

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)

                + Math.cos(radLat1) * Math.cos(radLat2)

                * Math.pow(Math.sin(b / 2), 2)));

        s = s * EARTH_RADIUS;

        s = Math.round(s * 10000) / 10000;

        s = s/1000;

        return s;

    }

    private float gps2d(double lat_a,double lng_a,double lat_b,double lng_b) {

        double d = 0;

        lat_a=lat_a*Math.PI/180;

        lng_a=lng_a*Math.PI/180;

        lat_b=lat_b*Math.PI/180;

        lng_b=lng_b*Math.PI/180;

        d=Math.sin(lat_a)*Math.sin(lat_b)+Math.cos(lat_a)*Math.cos(lat_b)*Math.cos(lng_b-lng_a);

        d=Math.sqrt(1-d*d);

        d=Math.cos(lat_b)*Math.sin(lng_b-lng_a)/d;

        d=Math.asin(d)*180/Math.PI;

//d = Math.round(d*10000);

        return (float)d;
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

    private boolean isGpsAble(LocationManager lm) {
        return lm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ? true : false;
    }





}


