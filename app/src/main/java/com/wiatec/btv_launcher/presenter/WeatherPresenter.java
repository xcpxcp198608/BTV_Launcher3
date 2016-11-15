package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.activity.IWeatherActivity;

/**
 * Created by PX on 2016-11-15.
 */

public class WeatherPresenter extends BasePresenter<IWeatherActivity> {

    private IWeatherActivity iWeatherActivity;

    public WeatherPresenter(IWeatherActivity iWeatherActivity) {
        this.iWeatherActivity = iWeatherActivity;
    }

    public void loadWeather (){

    }
}
