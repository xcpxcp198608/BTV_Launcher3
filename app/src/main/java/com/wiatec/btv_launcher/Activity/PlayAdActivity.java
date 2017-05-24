package com.wiatec.btv_launcher.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.SPUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-25.
 */

public class PlayAdActivity extends BaseActivity implements View.OnClickListener{
    private VideoView vv_PlayAd;
    private int time ;
    private LinearLayout llDelay;
    private TextView tvDelayTime ,tvTime;
    private Button btSkip;
    private Subscription subscription;
    private String packageName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play_ad);
        vv_PlayAd = (VideoView) findViewById(R.id.vv_play_ad);
        llDelay = (LinearLayout) findViewById(R.id.ll_delay);
        tvDelayTime = (TextView) findViewById(R.id.tv_delay_time);
        btSkip = (Button) findViewById(R.id.bt_skip);
        tvTime = (TextView) findViewById(R.id.tv_time);
        time = (int) SPUtils.get(PlayAdActivity.this , "adVideoTime" , 0);
        packageName = getIntent().getStringExtra("packageName");
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
                llDelay.setVisibility(View.GONE);
                if(ApkCheck.isApkInstalled(PlayAdActivity.this ,packageName)) {
                    ApkLaunch.launchApkByPackageName(PlayAdActivity.this, packageName);
                }else{
                    Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.download_guide),
                            Toast.LENGTH_LONG).show();
                    ApkLaunch.launchApkByPackageName(PlayAdActivity.this, F.package_name.market);
                }
                finish();
            }
        });
        btSkip.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final int userLevel = (int) SPUtils.get(PlayAdActivity.this ,"userLevel" ,1);
        if(userLevel >= 3){
            skipAds();
        }
        if(time >0){
            llDelay.setVisibility(View.VISIBLE);
            subscription = Observable.interval(0,1, TimeUnit.SECONDS).take(time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            int i = (int) (time -1 -aLong);
                            tvDelayTime.setText(i +" s");
                            if(userLevel == 2){
                                int j = (int) (30 -aLong);
                                if(j <0){
                                    j = 0;
                                }
                                tvTime.setText(" "+j + "s");
                                if(time - i >30){
                                    btSkip.setVisibility(View.VISIBLE);
                                    btSkip.requestFocus();
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_skip:
                skipAds();
                break;
        }
    }

    public void skipAds(){
        if(vv_PlayAd != null ){
            vv_PlayAd.stopPlayback();
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
        if(ApkCheck.isApkInstalled(PlayAdActivity.this ,packageName)) {
            ApkLaunch.launchApkByPackageName(PlayAdActivity.this, packageName);
        }else{
            Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.download_guide),
                    Toast.LENGTH_LONG).show();
            ApkLaunch.launchApkByPackageName(PlayAdActivity.this, F.package_name.market);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(vv_PlayAd != null ){
            vv_PlayAd.stopPlayback();
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(vv_PlayAd != null ){
            vv_PlayAd.stopPlayback();
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(vv_PlayAd != null ){
            vv_PlayAd.stopPlayback();
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
    }
}
