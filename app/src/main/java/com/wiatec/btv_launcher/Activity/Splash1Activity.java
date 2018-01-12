package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.px.common.image.ImageMaster;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.databinding.ActivitySplashBinding;
import com.wiatec.btv_launcher.presenter.SplashPresenter;

import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class Splash1Activity extends Base1Activity<ISplashActivity, SplashPresenter>
        implements ISplashActivity, View.OnClickListener {

    private ActivitySplashBinding binding;
    private String packageName;
    private int delayTime = 11;
    private Disposable disposable;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        packageName = getIntent().getStringExtra("packageName");
        binding.ibtPass.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String l = (String) SPUtil.get(F.sp.level , "1");
        int level = Integer.parseInt(l);
        if(level >= 4){
            launchApp(packageName);
            finish();
            return;
        }
        presenter.loadImage();
        Observable.interval(0,1, TimeUnit.SECONDS).take(delayTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        int i = (int) (delayTime - 1 - aLong);
                        //Logger.d(i + "");
                        binding.llDelay.setVisibility(View.VISIBLE);
                        binding.tvDelayTime.setText(String.valueOf(i) +" s");
                        if(i<=5){
                            binding.ibtPass.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Logger.d("delay onError");
                        launchApp(packageName);
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        launchApp(packageName);
                        finish();
                    }
                });
    }

    @Override
    public void loadImage(final ImageInfo imageInfo) {
        try {
            ImageMaster.load(imageInfo.getUrl(), binding.ivSplash, R.drawable.btv1, R.drawable.btv1);
        }catch (Exception e){
            e.printStackTrace();
            Logger.d(e.getMessage());
        }
        binding.ibtKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(imageInfo.getLink())));
                    disposable.dispose();
                }catch (Exception e){
                    Logger.d(e.getMessage());
                }
            }
        });
    }

    private void launchApp(String packageName) {
        AppUtil.launchApp(Splash1Activity.this, packageName);
    }

    @Override
    public void onClick(View view) {
        launchApp(packageName);
        disposable.dispose();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        disposable.dispose();
    }
}
