package com.dushyant.mediaplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    VideoView vv;
    TextView tvStatus;
    Button btnFile, btnUrl, btnPlay, btnPause, btnStop, btnRestart;

    MediaPlayer mp;
    boolean isAudio = false;
    Uri audioUri = null;

    // file picker for audio
    ActivityResultLauncher<String> picker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    audioUri = uri;
                    playAudio(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vv = findViewById(R.id.vv);
        tvStatus = findViewById(R.id.tvStatus);
        btnFile = findViewById(R.id.btnFile);
        btnUrl = findViewById(R.id.btnUrl);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        btnRestart = findViewById(R.id.btnRestart);

        // video controls
        MediaController mc = new MediaController(this);
        mc.setAnchorView(vv);
        vv.setMediaController(mc);

        btnFile.setOnClickListener(v -> {
            stopAll();
            isAudio = true;
            picker.launch("audio/*");
        });

        btnUrl.setOnClickListener(v -> {
            stopAll();
            isAudio = false;
            String url = "https://cdn.pixabay.com/video/2023/07/17/171907-846103580_large.mp4";
            
            vv.setVisibility(View.VISIBLE);
            vv.setVideoURI(Uri.parse(url));
            vv.setOnPreparedListener(p -> {
                tvStatus.setText("streaming video");
                vv.start();
            });
            vv.setOnErrorListener((p, what, extra) -> {
                tvStatus.setText("error loading video");
                return true;
            });
        });

        btnPlay.setOnClickListener(v -> {
            if (isAudio) {
                if (mp != null && !mp.isPlaying()) {
                    mp.start();
                    tvStatus.setText("playing audio");
                }
            } else {
                if (!vv.isPlaying()) {
                    vv.start();
                    tvStatus.setText("playing video");
                }
            }
        });

        btnPause.setOnClickListener(v -> {
            if (isAudio) {
                if (mp != null && mp.isPlaying()) {
                    mp.pause();
                    tvStatus.setText("audio paused");
                }
            } else {
                if (vv.isPlaying()) {
                    vv.pause();
                    tvStatus.setText("video paused");
                }
            }
        });

        btnStop.setOnClickListener(v -> {
            if (isAudio) {
                if (mp != null) {
                    mp.stop();
                    try {
                        mp.prepare(); // reset for next play
                    } catch (Exception e) {}
                    mp.seekTo(0);
                    tvStatus.setText("audio stopped");
                }
            } else {
                vv.stopPlayback();
                tvStatus.setText("video stopped");
            }
        });

        btnRestart.setOnClickListener(v -> {
            if (isAudio) {
                if (audioUri != null) playAudio(audioUri);
            } else {
                vv.seekTo(0);
                vv.start();
                tvStatus.setText("restarted");
            }
        });
    }

    void playAudio(Uri uri) {
        if (mp != null) mp.release();
        vv.setVisibility(View.GONE);

        mp = new MediaPlayer();
        try {
            mp.setDataSource(this, uri);
            mp.prepare();
            mp.start();
            tvStatus.setText("playing audio");
            mp.setOnCompletionListener(p -> tvStatus.setText("finished"));
        } catch (Exception e) {
            Toast.makeText(this, "error playing", Toast.LENGTH_SHORT).show();
        }
    }

    void stopAll() {
        if (mp != null && mp.isPlaying()) mp.stop();
        if (vv.isPlaying()) vv.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isAudio && mp != null && mp.isPlaying()) mp.pause();
        else if (!isAudio && vv.isPlaying()) vv.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) mp.release();
    }
}