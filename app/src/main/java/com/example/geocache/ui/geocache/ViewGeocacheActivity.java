package com.example.geocache.ui.geocache;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geocache.R;
import com.example.geocache.data.model.UserInfoShp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geocache);
        editTextTextID = findViewById(R.id.editTextTextID);
        editTextLatitudes = findViewById(R.id.editTextLatitudes);
        editTextLongitudes = findViewById(R.id.editTextLongitudes);
        editTextTextDescription = findViewById(R.id.editTextTextDescription);
        editTextTextID.setEnabled(false);
        Intent intent = getIntent();
        String description = intent.getStringExtra("geocacheLocationDescription");
        Integer id = intent.getIntExtra("geocacheId",0);
        Double latitudes = intent.getDoubleExtra("geocacheLatitudes",404);
        Double longitudes = intent.getDoubleExtra("geocacheLongitudes",404);
        editTextTextID.setText(id.toString());
        editTextLatitudes.setText(latitudes.toString());
        editTextLongitudes.setText(longitudes.toString());
        editTextTextDescription.setText(description);
        Button button_update = findViewById(R.id.button_update);
        Button button_report = findViewById(R.id.button_report);
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
                                    bundle.putDouble("geocacheLatitudes",latitudes);
                                    bundle.putDouble("geocacheLongitudes",longitudes);
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
}
