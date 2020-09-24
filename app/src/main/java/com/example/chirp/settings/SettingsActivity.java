package com.example.chirp.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.chirp.R;

public class SettingsActivity extends AppCompatActivity {

    private ImageView settings_page_back_button;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);


        settings_page_back_button = findViewById(R.id.settings_page_back_button);
        settings_page_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}