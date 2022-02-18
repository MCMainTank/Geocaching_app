package com.example.geocache;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.geocache.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_1 = findViewById(R.id.button_1);

        
        button_1.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "let's go";
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onTouch: ");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }



//            @Override
//            public void onClick(View view, MotionEvent event) {
//                Log.e(TAG, "onTouch: "+event);
//            }
        });



    }
}