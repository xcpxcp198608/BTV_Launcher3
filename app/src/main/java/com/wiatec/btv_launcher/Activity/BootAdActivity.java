package com.wiatec.btv_launcher.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.SPUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by patrick on 2017/2/15.
 */

public class BootAdActivity extends AppCompatActivity {
    private VideoView vvBoot;
    private LinearLayout llDelay;
    private TextView tvTimeDelay;
    private Subscription subscription;
    private int time ;

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
        deleteOldFolder();
//        time = (int) SPUtils.get(BootAdActivity.this , "bootAdVideoTime" , 0);
    }

    private void deleteOldFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/BLauncher/";
        File file = new File(path);
        if(file.exists()){
            if(file.isDirectory()){
                File [] files = file.listFiles();
                for(File file1 : files){
                    file1.delete();
                }
                file.delete();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Application.getBootStatus()){
            Observable.interval(0,1 , TimeUnit.SECONDS).take(5)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            int i = (int) (5 -1 - aLong);
                            if(i == 0){
                                Application.setBootStatus(false);
                                startActivity(new Intent(BootAdActivity.this , MainActivity.class));
                                finish();
                            }
                        }
                    });
        }else{
            Application.setBootStatus(false);
            startActivity(new Intent(BootAdActivity.this , MainActivity.class));
            finish();
        }



//        if(Application.getBootStatus()){
//            File file = new File(F.path.boot_ad_video);
//            if(!file.exists()){
//                Application.setBootStatus(false);
//                startActivity(new Intent(BootAdActivity.this , MainActivity.class));
//                finish();
//                return;
//            }
//            vvBoot.setVideoPath(F.path.boot_ad_video);
//            vvBoot.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mp.start();
//                }
//            });
//            vvBoot.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                @Override
//                public boolean onError(MediaPlayer mp, int what, int extra) {
//                    Application.setBootStatus(false);
//                    startActivity(new Intent(BootAdActivity.this , MainActivity.class));
//                    finish();
//                    return true;
//                }
//            });
//            vvBoot.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    Application.setBootStatus(false);
//                    llDelay.setVisibility(View.GONE);
//                    startActivity(new Intent(BootAdActivity.this , MainActivity.class));
//                    finish();
//                }
//            });
//        }else{
//            Application.setBootStatus(false);
//            startActivity(new Intent(BootAdActivity.this , MainActivity.class));
//            finish();
//        }
//        if(time >0){
//            llDelay.setVisibility(View.VISIBLE);
//            Observable.interval(0,1 , TimeUnit.SECONDS).take(time)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Action1<Long>() {
//                        @Override
//                        public void call(Long aLong) {
//                            int i = (int) (time -1 - aLong);
//                            tvTimeDelay.setText(i+" s");
//                        }
//                    });
//        }
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
