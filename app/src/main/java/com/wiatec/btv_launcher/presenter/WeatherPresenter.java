package com.wiatec.btv_launcher.presenter;

import com.px.common.utils.Logger;
import com.wiatec.btv_launcher.Activity.IWeatherActivity;
import com.wiatec.btv_launcher.bean.WeatherInfo;
import com.wiatec.btv_launcher.data.IWeatherData;
import com.wiatec.btv_launcher.data.WeatherData;

/**
 * Created by PX on 2016-11-15.
 */

public class WeatherPresenter extends BasePresenter<IWeatherActivity> {

    private IWeatherActivity iWeatherActivity;
    private IWeatherData iWeatherData;

    public WeatherPresenter(IWeatherActivity iWeatherActivity) {
        this.iWeatherActivity = iWeatherActivity;
        iWeatherData = new WeatherData();
    }

    public void loadWeather (){
        if(iWeatherData != null){
            iWeatherData.loadData(new IWeatherData.OnLoadListener() {
                @Override
                public void onSuccess(WeatherInfo weatherInfo) {
                    iWeatherActivity.loadWeather(weatherInfo);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
