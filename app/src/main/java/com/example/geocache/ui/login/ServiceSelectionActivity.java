package com.example.geocache.ui.login;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geocache.R;
import com.example.geocache.data.model.Geocache;
import com.example.geocache.data.model.UserInfoShp;
import com.example.geocache.ui.geocache.CreateGeocacheActivity;
import com.example.geocache.ui.geocache.GetGeocacheActivity;
import com.example.geocache.ui.geocache.ViewHistoryActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
    private Geocache geocache;
    private static UserInfoShp userInfoShp;
    private JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);
        Button button_selection_to_find_geocache = findViewById(R.id.button_selection_to_find_geocache);
        Button button_selection_to_create = findViewById(R.id.button_selection_to_create);
        Button button_selection_to_history = findViewById(R.id.button_selection_to_history);
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
    }

}

