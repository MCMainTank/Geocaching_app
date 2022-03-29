package com.example.geocache.ui.geocache;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geocache.R;

import okhttp3.OkHttpClient;

public class CreateGeocacheActivity extends AppCompatActivity {

    private EditText editText;
    private OkHttpClient okHttpClient;
    private String responseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_geocache);
        Button button_1 = findViewById(R.id.button3);
        editText = findViewById(R.id.location_description);
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geocacheDescription = editText.getText().toString();

                new Thread() {
                    @Override
                    public void run() {

                    }
                }.start();
            }
        });
    }

}
