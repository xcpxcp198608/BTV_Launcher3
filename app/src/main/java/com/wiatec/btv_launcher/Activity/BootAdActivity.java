package com.wiatec.btv_launcher.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

/**
 * Created by patrick on 2017/2/15.
 */

public class BootAdActivity extends AppCompatActivity {
    private VideoView vvBoot;
    private LinearLayout llDelay;
    private TextView tvTimeDelay;
    private Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_boot_ad);
        vvBoot = (VideoView) findViewById(R.id.vv_boot);
        llDelay = (LinearLayout) findViewById(R.id.ll_delay);
        tvTimeDelay = (TextView) findViewById(R.id.tv_delay_time);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int index = (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*0.2);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC , index , 0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Application.getBootStatus()){
            vvBoot.setVideoPath(F.path.boot_ad_video);
            vvBoot.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            vvBoot.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Application.setBootStatus(false);
                    startActivity(new Intent(BootAdActivity.this , MainActivity.class));
                    finish();
                    return true;
                }
            });
            vvBoot.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Application.setBootStatus(false);
                    startActivity(new Intent(BootAdActivity.this , MainActivity.class));
                    finish();
                }
            });
        }else{
            Application.setBootStatus(false);
            startActivity(new Intent(BootAdActivity.this , MainActivity.class));
            finish();
        }
    }

    private void showDelayTime(){

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(vvBoot != null ){
            vvBoot.stopPlayback();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(vvBoot != null ){
            vvBoot.stopPlayback();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(vvBoot != null ){
            vvBoot.stopPlayback();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
