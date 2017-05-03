package com.wiatec.btv_launcher.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.SPUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-25.
 */

public class PlayAdActivity extends BaseActivity {
    private VideoView vv_PlayAd;
    private int time ;
    private LinearLayout llDelay;
    private TextView tvDelayTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play_ad);
        vv_PlayAd = (VideoView) findViewById(R.id.vv_play_ad);
        llDelay = (LinearLayout) findViewById(R.id.ll_delay);
        tvDelayTime = (TextView) findViewById(R.id.tv_delay_time);
        time = (int) SPUtils.get(PlayAdActivity.this , "adVideoTime" , 0);
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
                llDelay.setVisibility(View.GONE);
                if(ApkCheck.isApkInstalled(PlayAdActivity.this ,packageName)) {
                    ApkLaunch.launchApkByPackageName(PlayAdActivity.this, packageName);
                }
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(time >0){
            llDelay.setVisibility(View.VISIBLE);
            Observable.interval(0,1, TimeUnit.SECONDS).take(time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            int i = (int) (time -1 -aLong);
                            tvDelayTime.setText(i +" s");
                        }
                    });
        }
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
