package com.example.geocache.ui.geocache;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.geocache.R;
import com.example.geocache.data.model.Geocache;
import com.example.geocache.data.model.UserInfoShp;

import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CreateGeocacheActivity extends AppCompatActivity {

    private EditText editText;
    private OkHttpClient okHttpClient;
    private String responseString;
    private LocationManager locationManager;
    private String locationProvider;
    private static UserInfoShp userInfoShp;
    private Geocache geocache;

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
                                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                List<String> providers = locationManager.getProviders(true);
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
                                if (ActivityCompat.checkSelfPermission(CreateGeocacheActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CreateGeocacheActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                Location location = locationManager.getLastKnownLocation(locationProvider);
                                String geocacheDescription = editText.getText().toString();
                                String username = userInfoShp.getUserName(CreateGeocacheActivity.this);
                                new Thread() {
                                    @Override
                                    public void run() {
                                        RequestBody requestBody = RequestBody.create("{"+"\"username\":\""+username+"\",\"Latitudes\":\""+location.getLatitude()+"\"Longitudes\":\""+location.getLongitude()+"\",\"Description\":\""+geocacheDescription+"\"}", MediaType.parse("application/json"));
                                        Request request = new Request.Builder().url("http://10.0.2.2:8080/createGeocache")
                                                .post(requestBody).build();
                                        okHttpClient = new OkHttpClient();
                                        Call call = okHttpClient.newCall(request);
                                        //TODO:Enqueue the call and get results


                                    }
                                }.start();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();

            }
        });
    }

}
