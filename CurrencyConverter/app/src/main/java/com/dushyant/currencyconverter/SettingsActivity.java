package com.dushyant.currencyconverter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat swDark;
    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        swDark = findViewById(R.id.swDark);
        tvStatus = findViewById(R.id.tvStatus);

        SharedPreferences sp = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean dark = sp.getBoolean("dark", false);
        
        swDark.setChecked(dark);
        if (dark) {
            tvStatus.setText("Dark Mode is ON");
        } else {
            tvStatus.setText("Dark Mode is OFF");
        }

        swDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
                // save choice in prefs
                SharedPreferences.Editor ed = getSharedPreferences("prefs", MODE_PRIVATE).edit();
                ed.putBoolean("dark", isChecked);
                ed.apply();

                // update theme and text
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    tvStatus.setText("Dark Mode is ON");
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    tvStatus.setText("Dark Mode is OFF");
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // go back
        return true;
    }
}
