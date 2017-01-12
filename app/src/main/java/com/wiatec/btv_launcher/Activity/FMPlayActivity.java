package com.wiatec.btv_launcher.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.custom_view.VoiceSpectrumView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by patrick on 2017/1/9.
 */

public class FMPlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private String url;
    private ImageView ivLogo;
    private VoiceSpectrumView vsvRadio;
    private Subscription subscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON , WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_fm_play);
        ivLogo  = (ImageView) findViewById(R.id.iv_logo);
        vsvRadio = (VoiceSpectrumView) findViewById(R.id.vsv_radio);
        url = getIntent().getStringExtra("url");
        if("http://142.4.216.91:8280/".equals(url)){
            ivLogo.setImageResource(R.drawable.eu);
        }else{
            ivLogo.setImageResource(R.drawable.btv);
        }
        voiceViewStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        if(url != null) {
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mediaPlayer.reset();
                        return false;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void voiceViewStart(){
        subscription = Observable.interval(0,200 , TimeUnit.MILLISECONDS)
                .repeat()
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {
                        vsvRadio.start();
                        return null;
                    }
                })
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {

                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayer!= null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!= null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
    }
}
