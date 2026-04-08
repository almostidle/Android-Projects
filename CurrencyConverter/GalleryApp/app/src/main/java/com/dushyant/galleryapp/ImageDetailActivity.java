package com.dushyant.galleryapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ImageView iv = findViewById(R.id.imgDetail);
        TextView tvName = findViewById(R.id.tvName);
        TextView tvPath = findViewById(R.id.tvPath);
        TextView tvSize = findViewById(R.id.tvSize);
        TextView tvDate = findViewById(R.id.tvDate);
        Button btnDel = findViewById(R.id.btnDelete);

        String path = getIntent().getStringExtra("path");
        File f = new File(path);

        // load image
        Glide.with(this).load(f).into(iv);

        tvName.setText("Name: " + f.getName());
        tvPath.setText("Path: " + f.getAbsolutePath());

        // bytes to KB
        long kb = f.length() / 1024;
        tvSize.setText("Size: " + kb + " KB");

        // format date
        String d = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date(f.lastModified()));
        tvDate.setText("Date: " + d);

        btnDel.setOnClickListener(v -> delete(f));
    }

    void delete(File f) {
        new AlertDialog.Builder(this)
                .setTitle("Delete?")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (f.delete()) {
                        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}