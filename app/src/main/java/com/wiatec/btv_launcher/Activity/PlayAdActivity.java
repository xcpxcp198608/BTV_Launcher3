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

import com.px.common.utils.CommonApplication;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.AppUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PlayAdActivity extends BaseActivity implements View.OnClickListener{
    private VideoView vv_PlayAd;
    private int time ;
    private LinearLayout llDelay;
    private TextView tvDelayTime ,tvTime;
    private Button btSkip;
    private Disposable disposable;
    private String packageName;
    private static final int SKIP_TIME = 15;

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
        time = (int) SPUtil.get(F.sp.ad_video_time , 0);
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
                if(AppUtil.isInstalled(packageName)) {
                    AppUtil.launchApp(PlayAdActivity.this, packageName);
                }
                finish();
                return true;
            }
        });
        vv_PlayAd.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                llDelay.setVisibility(View.GONE);
                release();
                if(AppUtil.isInstalled(packageName)) {
                    AppUtil.launchApp(PlayAdActivity.this, packageName);
                }else{
                    Toast.makeText(CommonApplication.context , CommonApplication.context.getString(R.string.download_guide),
                            Toast.LENGTH_LONG).show();
                    AppUtil.launchApp(PlayAdActivity.this, F.package_name.market);
                }
                finish();
            }
        });
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
        if(time >0){
            llDelay.setVisibility(View.VISIBLE);
            disposable = Observable.interval(0,1, TimeUnit.SECONDS).take(time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) {
                            int i = (int) (time -1 -aLong);
                            tvDelayTime.setText(i +" s");
                            if(userLevel == 2){
                                int j = (int) (SKIP_TIME -aLong);
                                if(j <0){
                                    j = 0;
                                }
                                tvTime.setText(" "+j + "s");
                                if(time - i > SKIP_TIME){
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
        release();
        if(AppUtil.isInstalled(packageName)) {
            AppUtil.launchApp(PlayAdActivity.this, packageName);
        }else{
            Toast.makeText(CommonApplication.context , CommonApplication.context.getString(R.string.download_guide),
                    Toast.LENGTH_LONG).show();
            AppUtil.launchApp(PlayAdActivity.this, F.package_name.market);
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
        release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    private void release(){
        if(vv_PlayAd != null ){
            vv_PlayAd.stopPlayback();
        }
        if(disposable != null){
            disposable.dispose();
        }
    }
}
