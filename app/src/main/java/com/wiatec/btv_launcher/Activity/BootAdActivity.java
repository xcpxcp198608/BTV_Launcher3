package com.wiatec.btv_launcher.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BootAdActivity extends AppCompatActivity {
    private VideoView vvBoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_boot_ad);
        vvBoot = findViewById(R.id.vv_boot);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int index = (int) (audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)*0.2);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC , index , 0);
        deleteOldFolder();
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
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) {
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
