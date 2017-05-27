package com.wiatec.btv_launcher.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.VideoView;

import com.wiatec.btv_launcher.R;

/**
 * Created by patrick on 26/05/2017.
 * create time : 3:53 PM
 */

public class GuideRegisterActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_register);
        videoView = (VideoView) findViewById(R.id.vv_play);
    }

    @Override
    protected void onStart() {
        super.onStart();
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.btvi3_register));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                finish();
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(videoView != null){
            videoView.stopPlayback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView != null){
            videoView.stopPlayback();
        }
    }
}
