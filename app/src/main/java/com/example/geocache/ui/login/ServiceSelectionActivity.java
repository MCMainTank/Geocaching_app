package com.example.geocache.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geocache.R;
import com.example.geocache.ui.geocache.CreateGeocacheActivity;
import com.example.geocache.ui.geocache.GetGeocacheActivity;
import com.example.geocache.ui.geocache.ViewHistoryActivity;

public class ServiceSelectionActivity extends AppCompatActivity {
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
                Intent intent = new Intent();
                intent.setClass(ServiceSelectionActivity.this, ViewHistoryActivity.class);
                startActivity(intent);
            }
        });
    }

}

