package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wiatec.btv_launcher.bean.WeatherInfo;
import com.wiatec.btv_launcher.presenter.WeatherPresenter;

/**
 * Created by PX on 2016-11-15.
 */

public class WeatherActivity extends BaseActivity<IWeatherActivity ,WeatherPresenter> implements IWeatherActivity {
    @Override
    protected WeatherPresenter createPresenter() {
        return new WeatherPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.wiatec.btv_launcher.R.layout.activity_weather);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadWeather();

    }

    @Override
    public void loadWeather(WeatherInfo weatherInfo) {

    }
}
