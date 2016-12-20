package com.wiatec.btv_launcher.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;

/**
 * Created by PX on 2016-11-25.
 */

public class PlayAdActivity extends AppCompatActivity {
    private VideoView vv_PlayAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_ad);
        vv_PlayAd = (VideoView) findViewById(R.id.vv_play_ad);

        final String packageName = getIntent().getStringExtra("packageName");

        vv_PlayAd.setVideoPath(F.path.ad_video);
        vv_PlayAd.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_PlayAd.start();
            }
        });
        vv_PlayAd.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if(ApkCheck.isApkInstalled(PlayAdActivity.this ,packageName)) {
                    ApkLaunch.launchApkByPackageName(PlayAdActivity.this, packageName);
                }
                finish();
                return true;
            }
        });

        vv_PlayAd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(ApkCheck.isApkInstalled(PlayAdActivity.this ,packageName)) {
                    ApkLaunch.launchApkByPackageName(PlayAdActivity.this, packageName);
                }
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(vv_PlayAd != null ){
            vv_PlayAd.stopPlayback();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(vv_PlayAd != null ){
            vv_PlayAd.stopPlayback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(vv_PlayAd != null ){
            vv_PlayAd.stopPlayback();
        }
    }
}
