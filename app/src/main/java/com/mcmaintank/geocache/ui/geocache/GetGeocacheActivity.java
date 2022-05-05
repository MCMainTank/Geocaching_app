package com.mcmaintank.geocache.ui.geocache;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mcmaintank.geocache.R;
import com.mcmaintank.geocache.data.model.Geocache;
import com.mcmaintank.geocache.ui.login.ServiceSelectionActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetGeocacheActivity extends AppCompatActivity {

    private EditText editText;
    private OkHttpClient okHttpClient;
    private String responseString;
    private Geocache geocache = new Geocache();
    private JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_geocache_by_id);
        Button button_1 = findViewById(R.id.button_1);
        editText = findViewById(R.id.geocache_id);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geocacheId = editText.getText().toString();
                new Thread(){
                    @Override
                    public void run(){
                        RequestBody requestBody = RequestBody.create("{"+"\"geocacheId\":\""+geocacheId+"\"}", MediaType.parse("application/json"));
                        Request request = new Request.Builder().url("http://39.105.14.129:8080/getGeocache")
                                .post(requestBody).build();
                        okHttpClient = new OkHttpClient();
                        Call call = okHttpClient.newCall(request);
                        Response response = null;
                        try {
                            response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        call.enqueue(new Callback() {
//                            @Override
//                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                                Log.i(TAG,"Failed to communicate with server.");
//                                return;
//                            }
//
//                            @Override
//                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        try {
                            responseString = response.body().string();
                            Log.i(TAG,"Requested geocache: "+responseString );
                            jsonObject = new JSONObject(responseString);
                            Log.i(TAG,jsonObject.toString());
                            if(geocache!=null){
                                Log.i(TAG,"Fine");
                            }
                            geocache.setGeocacheId(parseInt(jsonObject.getString("geocacheId")));
                            geocache.setDescription(jsonObject.getString("geocacheLocationDescription"));
                            geocache.setLatitudes(jsonObject.getDouble("geocacheLatitudes"));
                            geocache.setLongitudes(jsonObject.getDouble("geocacheLongitudes"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }.start();


//                            }
//                        });
                try {
                    sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(GetGeocacheActivity.this,ViewGeocacheActivity.class);
                Bundle bundle = new Bundle();
                if(geocache.getGeocacheId()!=null){
                    Log.i(TAG,"Fine");
                }else{
                    Toast.makeText(getApplicationContext(), "Geocache not found or deleted.", Toast.LENGTH_SHORT).show();
                    return;
                }
                bundle.putInt("geocacheId",geocache.getGeocacheId());
                bundle.putString("geocacheLocationDescription",geocache.getDescription());
                bundle.putDouble("geocacheLatitudes",geocache.getLatitudes());
                bundle.putDouble("geocacheLongitudes",geocache.getLongitudes());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        startActivity(new Intent(GetGeocacheActivity.this, ServiceSelectionActivity.class));
    }
}
