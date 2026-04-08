package com.dushyant.galleryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    RecyclerView rv;
    ImageAdapter adapter;
    List<File> list = new ArrayList<>();
    TextView tvFolder;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        tvFolder = findViewById(R.id.tvFolderName);
        rv = findViewById(R.id.recyclerView);

        // 3 column grid
        rv.setLayoutManager(new GridLayoutManager(this, 3));

        path = getIntent().getStringExtra("path");
        if (path == null) {
            finish();
            return;
        }

        tvFolder.setText(path);
        loadImages();

        // click on image to open detail
        adapter = new ImageAdapter(list, file -> {
            Intent i = new Intent(this, ImageDetailActivity.class);
            i.putExtra("path", file.getAbsolutePath());
            startActivity(i);
        });
        rv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadImages();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    void loadImages() {
        list.clear();
        File folder = new File(path);

        if (!folder.exists()) return;

        File[] files = folder.listFiles();
        if (files == null) return;

        // add only images
        for (File f : files) {
            String name = f.getName().toLowerCase();
            if (name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg")) {
                list.add(f);
            }
        }
    }
}