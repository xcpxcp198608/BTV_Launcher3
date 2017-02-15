package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.VideoView;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;

/**
 * Created by patrick on 2017/2/15.
 */

public class BootAdActivity extends AppCompatActivity {
    private VideoView vvBoot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_boot_ad);
        vvBoot = (VideoView) findViewById(R.id.vv_boot);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(Application.getBootStatus()+"");
        if(Application.getBootStatus()){
            vvBoot.setVideoPath(F.path.ad_video);
            vvBoot.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    vvBoot.start();
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
