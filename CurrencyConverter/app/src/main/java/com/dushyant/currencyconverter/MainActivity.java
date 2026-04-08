package com.dushyant.currencyconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Spinner spFrom, spTo;
    EditText etAmount;
    TextView tvResult;
    Button btnConvert, btnSwap;

    String[] list = {"USD", "INR", "YUAN", "DIRHAM"};
    HashMap<String, Double> rates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(); // set dark mode if needed
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spFrom = findViewById(R.id.spFrom);
        spTo = findViewById(R.id.spTo);
        etAmount = findViewById(R.id.etAmount);
        tvResult = findViewById(R.id.tvResult);
        btnConvert = findViewById(R.id.btnConvert);
        btnSwap = findViewById(R.id.btnSwap);

        // conversion rates compared to 1 USD
        rates.put("USD", 1.0);
        rates.put("INR", 93.0);
        rates.put("YUAN", 7.0);
        rates.put("DIRHAM", 3.67);

        ArrayAdapter<String> adp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrom.setAdapter(adp);
        spTo.setAdapter(adp);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert();
            }
        });

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // swap the items in spinners
                int p1 = spFrom.getSelectedItemPosition();
                int p2 = spTo.getSelectedItemPosition();
                spFrom.setSelection(p2);
                spTo.setSelection(p1);
            }
        });
    }

    void convert() {
        String s = etAmount.getText().toString();

        if (s.isEmpty()) {
            Toast.makeText(this, "enter amount", Toast.LENGTH_SHORT).show();
            return;
        }

        double val = Double.parseDouble(s);
        String f = spFrom.getSelectedItem().toString();
        String t = spTo.getSelectedItem().toString();

        // convert to USD then to target
        double usd = val / rates.get(f);
        double res = usd * rates.get(t);

        tvResult.setText(String.format("%.2f %s", res, t));
    }

    void setTheme() {
        SharedPreferences sp = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean dark = sp.getBoolean("dark", false);
        if (dark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
