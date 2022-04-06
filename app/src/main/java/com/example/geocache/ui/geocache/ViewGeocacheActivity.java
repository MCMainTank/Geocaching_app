package com.example.geocache.ui.geocache;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geocache.R;

public class ViewGeocacheActivity extends AppCompatActivity {
    private EditText editTextTextID;
    private EditText editTextLatitudes;
    private EditText editTextLongitudes;
    private EditText editTextTextDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_geocache);
        editTextTextID = findViewById(R.id.editTextTextID);
        editTextLatitudes = findViewById(R.id.editTextLatitudes);
        editTextLongitudes = findViewById(R.id.editTextLongitudes);
        editTextTextDescription = findViewById(R.id.editTextTextDescription);
        Intent intent = getIntent();
        String description = intent.getStringExtra("geocacheLocationDescription");
        Integer id = intent.getIntExtra("geocacheId",0);
        Double latitudes = intent.getDoubleExtra("geocacheLatitudes",404);
        Double longitudes = intent.getDoubleExtra("geocacheLongitudes",404);
        editTextTextID.setText(id);
        editTextLatitudes.setText(latitudes.toString());
        editTextLongitudes.setText(longitudes.toString());
        editTextTextDescription.setText(description);
    }
}
