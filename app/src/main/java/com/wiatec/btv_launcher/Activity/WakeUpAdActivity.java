package com.wiatec.btv_launcher.Activity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by patrick on 2017/3/3.
 */

public class WakeUpAdActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView vvWake;
    private LinearLayout llDelay;
    private TextView tvTimeDelay ,tvTime;
    private int time ;
    private Button btSkip;
    private Disposable disposable;
    private static final int SKIP_TIME = 15;

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
        time = (int) SPUtil.get(F.sp.ad_boot_video_time , 0);
        btSkip.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String l = (String) SPUtil.get(F.sp.level , "1");
        final int userLevel = Integer.parseInt(l);
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
            disposable = Observable.interval(0,1 , TimeUnit.SECONDS).take(time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) {
                            int i = (int) (time -1 - aLong);
                            tvTimeDelay.setText(i+" s");
                            if(userLevel == 2){
                                int j = (int) (SKIP_TIME -aLong);
                                if(j <0){
                                    j = 0;
                                }
                                tvTime.setText(" "+j + "s");
                                if(time - i >SKIP_TIME){
                                    btSkip.setVisibility(View.VISIBLE);
                                    btSkip.requestFocus();
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
        if(disposable != null){
            disposable.dispose();
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
        if(disposable != null){
            disposable.dispose();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(vvWake != null ){
            vvWake.stopPlayback();
        }
        if(disposable != null){
            disposable.dispose();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(vvWake != null ){
            vvWake.stopPlayback();
        }
        if(disposable != null){
            disposable.dispose();
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
