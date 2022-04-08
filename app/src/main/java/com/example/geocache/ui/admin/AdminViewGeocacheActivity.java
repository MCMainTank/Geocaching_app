package com.example.geocache.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geocache.R;
import com.example.geocache.data.model.UserInfoShp;

import okhttp3.OkHttpClient;

public class AdminViewGeocacheActivity extends AppCompatActivity {
    private EditText editTextTextID;
    private EditText editTextLatitudes;
    private EditText editTextLongitudes;
    private EditText editTextTextDescription;
    private TextView textViewReported;
    private static UserInfoShp userInfoShp;
    private OkHttpClient okHttpClient;
    private String responseString;
    private Integer status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_geocache);
        editTextTextID = findViewById(R.id.editTextTextID);
        editTextLatitudes = findViewById(R.id.editTextLatitudes);
        editTextLongitudes = findViewById(R.id.editTextLongitudes);
        editTextTextDescription = findViewById(R.id.editTextTextDescription);
        textViewReported = findViewById(R.id.textViewReported);
        editTextTextID.setEnabled(false);
        Intent intent = getIntent();
        String description = intent.getStringExtra("geocacheLocationDescription");
        Integer id = intent.getIntExtra("geocacheId", 0);
        Double latitudes = intent.getDoubleExtra("geocacheLatitudes", 404);
        Double longitudes = intent.getDoubleExtra("geocacheLongitudes", 404);
        Integer reported = intent.getIntExtra("reported",0);
        editTextTextID.setText(id.toString());
        editTextLatitudes.setText(latitudes.toString());
        editTextLongitudes.setText(longitudes.toString());
        editTextTextDescription.setText(description);
        textViewReported.setText(reported);
        Button button_delete = findViewById(R.id.button);

    }
}
