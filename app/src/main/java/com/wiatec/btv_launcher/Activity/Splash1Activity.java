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

import com.px.common.image.ImageMaster;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.presenter.SplashPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by PX on 2016-11-14.
 */

public class Splash1Activity extends Base1Activity<ISplashActivity, SplashPresenter> implements ISplashActivity {
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
    private int delayTime = 11;
    private Disposable disposable;

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
                        ll_Delay.setVisibility(View.VISIBLE);
                        tv_DelayTime.setText(String.valueOf(i) +" s");
                        if(i<=5){
                            ibt_Pass.setVisibility(View.VISIBLE);
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
            ImageMaster.load(imageInfo.getUrl(),iv_Splash, R.drawable.btv1, R.drawable.btv1);
        }catch (Exception e){
            e.printStackTrace();
            Logger.d(e.getMessage());
        }
        ibt_Know.setOnClickListener(new View.OnClickListener() {
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

    @OnClick(R.id.ibt_pass)
    public void onClick() {
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
