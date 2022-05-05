package com.mcmaintank.geocache.ui.login;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mcmaintank.geocache.R;
import com.mcmaintank.geocache.data.model.Geocache;
import com.mcmaintank.geocache.data.model.User;
import com.mcmaintank.geocache.data.model.UserInfoShp;
import com.mcmaintank.geocache.ui.admin.AdminGetGeocacheActivity;
import com.mcmaintank.geocache.ui.admin.ManageGeocacheActivity;
import com.mcmaintank.geocache.ui.admin.ManageUserActivity;

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

public class AdminServiceSelectionActivity extends AppCompatActivity {

    private String responseString;
    private OkHttpClient okHttpClient;
    private static UserInfoShp userInfoShp;
    private JSONArray jsonArray;
    private ArrayList list = new ArrayList();
    private List<Geocache> geocacheList = new ArrayList<Geocache>();
    private Geocache geocache;
    private List<User> userList = new ArrayList<User>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service_selection);
        Button geocacheManagement = findViewById(R.id.geocache);
        Button userManagement = findViewById(R.id.user);
        Button findGeocache = findViewById(R.id.find_geocache);
        findGeocache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(AdminServiceSelectionActivity.this, AdminGetGeocacheActivity.class);
                startActivity(intent);
            }
        });
        userManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run(){
                        RequestBody requestBody = RequestBody.create("{"+"\"username\":\""+userInfoShp.getUserName(AdminServiceSelectionActivity.this)+"\",\"password\":\""+userInfoShp.getUserPassword(AdminServiceSelectionActivity.this)+"\"}", MediaType.parse("application/json"));
                        Request request = new Request.Builder().url("http://39.105.14.129:8080/getTopTenReportedUsers")
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
                            Log.i(TAG,"Requested user list: "+responseString );
                            jsonArray = new JSONArray(responseString);
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                User user = new User();
                                user.setUserId(Long.parseLong(jsonObject.getString("userId")));
                                user.setUserName(jsonObject.getString("userName"));
                                user.setReported(parseInt(jsonObject.getString("reported")));
                                userList.add(user);
                            }
//                            System.out.println(geocacheList.get(0).getGeocacheId().toString());
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
//                            list.add(geocacheList);
                            bundle.putString("jsonString",responseString);
                            Log.i(TAG,"Requested geocache list: "+responseString );
                            intent.setClass(AdminServiceSelectionActivity.this, ManageUserActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
            }
        });
        geocacheManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run(){
                        RequestBody requestBody = RequestBody.create("{"+"\"username\":\""+userInfoShp.getUserName(AdminServiceSelectionActivity.this)+"\",\"password\":\""+userInfoShp.getUserPassword(AdminServiceSelectionActivity.this)+"\"}", MediaType.parse("application/json"));
                        Request request = new Request.Builder().url("http://39.105.14.129:8080/getTopTenReportedGeocaches")
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
                                geocache.setReported(parseInt(jsonObject.getString("reported")));
                                geocacheList.add(geocache);
                            }
//                            System.out.println(geocacheList.get(0).getGeocacheId().toString());
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
//                            list.add(geocacheList);
                            bundle.putString("jsonString",responseString);
                            Log.i(TAG,"Requested geocache list: "+responseString );
                            intent.setClass(AdminServiceSelectionActivity.this, ManageGeocacheActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();
//                Intent intent = new Intent();
//                intent.setClass(AdminServiceSelectionActivity.this, ManageGeocacheActivity.class);
//                startActivity(intent);
            }
        });

    }

}
