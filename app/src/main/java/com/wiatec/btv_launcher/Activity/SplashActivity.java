package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.presenter.SplashPresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by PX on 2016-11-14.
 */

public class SplashActivity extends BaseActivity<ISplashActivity, SplashPresenter> implements ISplashActivity {
    @BindView(R.id.iv_splash)
    ImageView iv_Splash;
    @BindView(R.id.ibt_know)
    Button ibt_Know;
    @BindView(R.id.ibt_pass)
    Button ibt_Pass;
    @BindView(R.id.tv_delay_time)
    TextView tv_DelayTime;
    @BindView(R.id.ll_delay)
    LinearLayout ll_Delay;


    private String packageName;
    private int delayTime = 6;
    private Subscription subscription;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        packageName = getIntent().getStringExtra("packageName");
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadImage();
        subscription = Observable.interval(0,1, TimeUnit.SECONDS).take(delayTime)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                       // Logger.d("delay onCompleted");
                        launchApp(packageName);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                       // Logger.d("delay onError");
                        launchApp(packageName);
                        finish();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        int i = (int) (delayTime - 1 - aLong);
                        //Logger.d(i + "");
                        ll_Delay.setVisibility(View.VISIBLE);
                        tv_DelayTime.setText(String.valueOf(i) +" s");
                    }
                });
    }

    @Override
    public void loadImage(final List<ImageInfo> list) {
        //Logger.d(list.toString());
        final ImageInfo imageInfo = list.get(0);
        try {
            Glide.with(SplashActivity.this).load(imageInfo.getUrl()).placeholder(R.drawable.btv1).into(iv_Splash);
        }catch (Exception e){
            e.printStackTrace();
            Logger.d(e.getMessage());
        }
        ibt_Know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(imageInfo.getLink())));
                subscription.unsubscribe();
            }
        });
    }

    private void launchApp(String packageName) {
        ApkLaunch.launchApkByPackageName(SplashActivity.this, packageName);
    }

    @OnClick(R.id.ibt_pass)
    public void onClick() {
        launchApp(packageName);
        subscription.unsubscribe();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        subscription.unsubscribe();
    }
}
