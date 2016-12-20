package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.bean.WeatherInfo;

/**
 * Created by PX on 2016-11-16.
 */

public interface IWeatherData {
    void loadData (OnLoadListener onLoadListener);
    interface OnLoadListener {
        void onSuccess (WeatherInfo weatherInfo);
        void onFailure (String e);
    }
}
