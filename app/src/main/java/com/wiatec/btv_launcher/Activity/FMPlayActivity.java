package com.wiatec.btv_launcher.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class FMPlayActivity extends BaseActivity {

    private MediaPlayer mediaPlayer;
    private String url;
    private ImageView ivLogo;
    private ImageView ivEufonicoList;
    private VoiceSpectrumView vsvRadio;
    private ProgressBar progressBar;
    private Disposable disposable;
    private RollOverView rollOverView;
    private RollOverView2Adapter rollOverView2Adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON , WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_fm_play);
        ivLogo  = (ImageView) findViewById(R.id.iv_logo);
        ivEufonicoList  = (ImageView) findViewById(R.id.iv_eufonico_list);
        vsvRadio = (VoiceSpectrumView) findViewById(R.id.vsv_radio);
        rollOverView = (RollOverView) findViewById(R.id.roll_over_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        url = getIntent().getStringExtra("url");
        if("http://142.4.216.91:8280/".equals(url)){
            ivLogo.setImageResource(R.drawable.eufonic_big_logo);
            ivEufonicoList.setVisibility(View.VISIBLE);
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
        disposable = Observable.interval(0,200 , TimeUnit.MILLISECONDS)
                .repeat()
                .map(new Function<Long, Object>() {
                    @Override
                    public Object apply(Long aLong) {
                        vsvRadio.start();
                        return "";
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) {

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
        if(disposable != null){
            disposable.dispose();
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
        if(disposable != null){
            disposable.dispose();
        }
    }
}
