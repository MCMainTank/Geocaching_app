package com.mcmaintank.geocache;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.mcmaintank.geocache.ui.login.LoginActivity;
import com.mcmaintank.geocache.ui.login.ServiceSelectionActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "LoginStatus";
    private static final Integer DEFAULT_STATUS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_1 = findViewById(R.id.button_1);

        SharedPreferences loggedInStatus = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//        Integer status = loggedInStatus.getInt("status", 0);
        Integer status = DEFAULT_STATUS;
        button_1.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "let's go";
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onTouch: ");
                Log.e(TAG, "status: "+status);
                if(status==1){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, ServiceSelectionActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }

//            @Override
//            public void onClick(View view, MotionEvent event) {
//                Log.e(TAG, "onTouch: "+event);
//            }
        });



    }


}