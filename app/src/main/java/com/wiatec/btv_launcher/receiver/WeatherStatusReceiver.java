package com.wiatec.btv_launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.WeatherDao;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.WeatherIconSetting;
import com.wiatec.btv_launcher.bean.WeatherInfo;

/**
 * Created by PX on 2016-11-18.
 */

public class WeatherStatusReceiver extends BroadcastReceiver {

    private ImageButton imageButton;

    public WeatherStatusReceiver(ImageButton imageButton) {
        this.imageButton = imageButton;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if("action.Weather.Change".equals(intent.getAction())){
            WeatherInfo weatherInfo = WeatherDao.getInstance(context).query();
            WeatherIconSetting.setIcon(imageButton ,  weatherInfo.getIcon());
        }
    }
}
