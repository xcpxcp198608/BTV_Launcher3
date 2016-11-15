package com.wiatec.btv_launcher.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.wiatec.btv_launcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PX on 2016-11-14.
 */

public class PlayActivity extends AppCompatActivity {
    @BindView(R.id.vv_play)
    VideoView vv_Play;

    private String url;
    private int resId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        resId = getIntent().getIntExtra("resId" ,0);
        url = getIntent().getStringExtra("url");
        if(resId != 0){
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + resId);
            vv_Play.setVideoURI(uri);
        }
        if(url != null){
            vv_Play.setVideoPath(url);
        }
        vv_Play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_Play.start();
            }
        });
        vv_Play.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                vv_Play.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(vv_Play != null && vv_Play.isPlaying()){
            vv_Play.stopPlayback();
        }
    }
}
