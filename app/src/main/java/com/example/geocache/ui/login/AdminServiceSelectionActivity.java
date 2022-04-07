package com.example.geocache.ui.login;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geocache.R;

public class AdminServiceSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service_selection);
        Button geocacheManagement = findViewById(R.id.geocache);
        Button userManagement = findViewById(R.id.user);
    }

}
