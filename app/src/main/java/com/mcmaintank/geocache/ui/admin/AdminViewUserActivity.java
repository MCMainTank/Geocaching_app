package com.mcmaintank.geocache.ui.admin;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.mcmaintank.geocache.R;
import com.mcmaintank.geocache.data.model.UserInfoShp;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AdminViewUserActivity extends AppCompatActivity {
    private EditText editTextID;
    private EditText editTextName;
    private TextView textViewReportCount;
    private static UserInfoShp userInfoShp;
    private OkHttpClient okHttpClient;
    private String responseString;
    private Integer status;

    @Override
    protected void onCreate(Bundle savedInstanState) {
        super.onCreate(savedInstanState);
        setContentView(R.layout.activity_admin_view_user);
        editTextID = findViewById(R.id.editTextID);
        editTextName = findViewById(R.id.editTextName);
        textViewReportCount = findViewById(R.id.textViewReported);
        Intent intent = getIntent();
        Integer Id = intent.getIntExtra("userId",0);
        String username = intent.getStringExtra("username");
        Integer reported = intent.getIntExtra("reported",-1);
        editTextID.setText(Id.toString());
        editTextName.setText(username);
        textViewReportCount.setText(reported.toString());
        Button button_delete = findViewById(R.id.button20);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AdminViewUserActivity.this).setTitle("Are you sure you want to delete this user?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new Thread() {
                                    @Override
                                    public void run() {

                                        RequestBody requestBody = RequestBody.create("{" + "\"username\":\"" + userInfoShp.getUserName(AdminViewUserActivity.this) + "\",\"password\":\"" + userInfoShp.getUserPassword(AdminViewUserActivity.this) + "\",\"userId\":\"" + editTextID.getText().toString() + "\"}", MediaType.parse("application/json"));
                                        Request request = new Request.Builder().url("http://39.105.14.129:8080/deleteUser")
                                                .post(requestBody).build();
                                        okHttpClient = new OkHttpClient();
                                        Call call = okHttpClient.newCall(request);
                                        call.enqueue(new Callback() {
                                            @Override
                                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                Log.i(TAG, "Delete failed.");
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
                                        Toast.makeText(getApplicationContext(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else{
                                        Toast.makeText(getApplicationContext(), "User does not exist or not successfully logged in.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
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
    }

}
