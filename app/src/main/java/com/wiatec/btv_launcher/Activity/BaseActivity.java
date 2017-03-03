package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.presenter.BasePresenter;

/**
 * Created by PX on 2016-11-14.
 */

public abstract class BaseActivity <V ,T extends BasePresenter> extends AppCompatActivity {
    protected T presenter;
    protected String countryCode ;
    protected String userName;
    protected String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView(this);
        countryCode = (String) SPUtils.get(this ,countryCode ,"");
        userName = (String) SPUtils.get(this ,userName ,"");
        token = (String) SPUtils.get(this ,token ,"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    protected abstract T createPresenter();
}
