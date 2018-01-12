package com.wiatec.btv_launcher.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.adapter.RollOverView2Adapter;
import com.wiatec.btv_launcher.custom_view.RollOverView;

import java.io.IOException;

public class PlayActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private String url;
    private int resId;
    private RollOverView rollOverView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON , WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play);
        progressBar = findViewById(R.id.progressbar);
        SurfaceView surfaceView = findViewById(R.id.vv_play);
        rollOverView = findViewById(R.id.roll_over_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        resId = getIntent().getIntExtra("resId" ,0);
        url = getIntent().getStringExtra("url");
    }

    @Override
    protected void onStart() {
        super.onStart();
        RollOverView2Adapter rollOverView2Adapter = new RollOverView2Adapter();
        rollOverView.setRollViewAdapter(rollOverView2Adapter);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        try {
            if(resId != 0){
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + resId);
                mediaPlayer.setDataSource(PlayActivity.this ,uri);
            }
            if(url != null) {
                mediaPlayer.setDataSource(url);
            }
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mediaPlayer.reset();
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mediaPlayer != null ){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (rollOverView != null){
            rollOverView.stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer != null ){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (rollOverView != null){
            rollOverView.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null ){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (rollOverView != null){
            rollOverView.stop();
        }
    }
}
