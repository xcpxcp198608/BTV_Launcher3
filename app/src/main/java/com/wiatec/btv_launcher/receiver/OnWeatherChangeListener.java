package com.wiatec.btv_launcher.receiver;

import com.wiatec.btv_launcher.bean.WeatherInfo;

/**
 * Created by PX on 2016-11-18.
 */

public interface OnWeatherChangeListener {
    void onChange (WeatherInfo weatherInfo);
}
