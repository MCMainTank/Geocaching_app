package com.example.geocache;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

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
            }


//            @Override
//            public void onClick(View view, MotionEvent event) {
//                Log.e(TAG, "onTouch: "+event);
//            }
        });

    }
}