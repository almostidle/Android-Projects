package com.dushyant.galleryapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvStatus;
    File imgFile;
    File folder;

    // open camera and save the photo
    ActivityResultLauncher<Uri> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {
                if (success) {
                    tvStatus.setText("Saved: " + imgFile.getName());
                    Toast.makeText(this, "Photo saved!", Toast.LENGTH_SHORT).show();
                }
            });

    // pick a folder from storage
    ActivityResultLauncher<Uri> folderLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), uri -> {
                if (uri != null) {
                    String path = getPath(uri);
                    if (path != null) {
                        folder = new File(path);
                        tvStatus.setText("Folder: " + folder.getAbsolutePath());
                    }
                }
            });

    // ask for permissions
    ActivityResultLauncher<String[]> permLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean ok = true;
                for (boolean granted : result.values()) {
                    if (!granted) ok = false;
                }
                if (ok) openCamera();
                else Toast.makeText(this, "Need permissions!", Toast.LENGTH_SHORT).show();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);
        Button btnCamera = findViewById(R.id.btnTakePhoto);
        Button btnFolder = findViewById(R.id.btnPickFolder);
        Button btnGallery = findViewById(R.id.btnViewGallery);

        // default is DCIM
        folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        btnCamera.setOnClickListener(v -> checkPerms());
        btnFolder.setOnClickListener(v -> folderLauncher.launch(null));
        btnGallery.setOnClickListener(v -> {
            Intent i = new Intent(this, GalleryActivity.class);
            i.putExtra("path", folder.getAbsolutePath());
            startActivity(i);
        });
    }

    void checkPerms() {
        String[] perms;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            perms = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            perms = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        boolean allOk = true;
        for (String p : perms) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                allOk = false;
                break;
            }
        }

        if (allOk) openCamera();
        else permLauncher.launch(perms);
    }

    void openCamera() {
        try {
            imgFile = makeFile();
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imgFile);
            cameraLauncher.launch(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create a new file with timestamp
    File makeFile() {
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String name = "IMG_" + time + ".jpg";
        if (!folder.exists()) folder.mkdirs();
        return new File(folder, name);
    }

    // helper to get path from uri
    String getPath(Uri uri) {
        try {
            String id = uri.getLastPathSegment();
            if (id != null && id.contains(":")) {
                String[] parts = id.split(":");
                if ("primary".equalsIgnoreCase(parts[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + parts[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}