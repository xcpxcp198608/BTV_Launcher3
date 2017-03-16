package com.wiatec.btv_launcher.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.adapter.RollOverView2Adapter;
import com.wiatec.btv_launcher.custom_view.RollOverView;
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

public class FMPlayActivity extends Base1Activity {

    private MediaPlayer mediaPlayer;
    private String url;
    private ImageView ivLogo;
    private VoiceSpectrumView vsvRadio;
    private ProgressBar progressBar;
    private Subscription subscription;
    private RollOverView rollOverView;
    private RollOverView2Adapter rollOverView2Adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON , WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_fm_play);
        ivLogo  = (ImageView) findViewById(R.id.iv_logo);
        vsvRadio = (VoiceSpectrumView) findViewById(R.id.vsv_radio);
        rollOverView = (RollOverView) findViewById(R.id.roll_over_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        url = getIntent().getStringExtra("url");
        if("http://142.4.216.91:8280/".equals(url)){
            ivLogo.setImageResource(R.drawable.euf_big);
        }else{
            ivLogo.setImageResource(R.drawable.btv);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        rollOverView2Adapter = new RollOverView2Adapter();
        rollOverView.setRollViewAdapter(rollOverView2Adapter);
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
                        progressBar.setVisibility(View.GONE);
                        voiceViewStart();
                        vsvRadio.setVisibility(View.VISIBLE);
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
        rollOverView.stop();
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
