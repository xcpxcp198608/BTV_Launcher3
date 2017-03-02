package com.wiatec.btv_launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.wiatec.btv_launcher.SQL.WeatherDao;
import com.wiatec.btv_launcher.WeatherIconSetting;
import com.wiatec.btv_launcher.bean.WeatherInfo;

/**
 * Created by PX on 2016-11-18.
 */

public class WeatherStatusReceiver extends BroadcastReceiver {

    private ImageView imageView;

    public WeatherStatusReceiver(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if("action.Weather.Change".equals(intent.getAction())){
            WeatherInfo weatherInfo = WeatherDao.getInstance(context).query();
            WeatherIconSetting.setIcon(imageView ,  weatherInfo.getIcon());
        }
    }
}
