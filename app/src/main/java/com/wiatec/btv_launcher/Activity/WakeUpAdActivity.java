package com.wiatec.btv_launcher.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SPUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by patrick on 2017/3/3.
 */

public class WakeUpAdActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView vvWake;
    private LinearLayout llDelay;
    private TextView tvTimeDelay ,tvTime;
    private int time ;
    private Button btSkip;
    private Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_up_ad);
        vvWake = (VideoView) findViewById(R.id.vv_wake);
        llDelay = (LinearLayout) findViewById(R.id.ll_delay);
        tvTimeDelay = (TextView) findViewById(R.id.tv_delay_time);
        btSkip = (Button) findViewById(R.id.bt_skip);
        tvTime = (TextView) findViewById(R.id.tv_time);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int index = (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*0.2);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC , index , 0);
        time = (int) SPUtils.get(WakeUpAdActivity.this , "bootAdVideoTime" , 0);
        btSkip.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final int userLevel = (int) SPUtils.get(WakeUpAdActivity.this ,"userLevel" ,1);
        if(userLevel >= 3){
            skipAds();
        }
        vvWake.setVideoPath(F.path.boot_ad_video);
        vvWake.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        vvWake.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                llDelay.setVisibility(View.GONE);
                finish();
                return true;
            }
        });
        vvWake.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
        if(time >0){
            llDelay.setVisibility(View.VISIBLE);
            subscription = Observable.interval(0,1 , TimeUnit.SECONDS).take(time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            int i = (int) (time -1 - aLong);
                            tvTimeDelay.setText(i+" s");
                            if(userLevel == 2){
                                int j = (int) (30 -aLong);
                                if(j <0){
                                    j = 0;
                                }
                                tvTime.setText(" "+j + "s");
                                if(time - i >30){
                                    btSkip.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
        }
    }

    public void skipAds(){
        if(vvWake != null ){
            vvWake.stopPlayback();
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_skip:
                skipAds();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(vvWake != null ){
            vvWake.stopPlayback();
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(vvWake != null ){
            vvWake.stopPlayback();
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(vvWake != null ){
            vvWake.stopPlayback();
        }
        if(subscription != null){
            subscription.unsubscribe();
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
