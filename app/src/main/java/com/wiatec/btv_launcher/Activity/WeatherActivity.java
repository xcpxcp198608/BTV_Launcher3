package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.WeatherInfo;
import com.wiatec.btv_launcher.presenter.WeatherPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by PX on 2016-11-15.
 */

public class WeatherActivity extends Base1Activity<IWeatherActivity, WeatherPresenter> implements IWeatherActivity {
    @BindView(R.id.iv_icon)
    ImageView iv_Icon;
    @BindView(R.id.tv_city)
    TextView tv_City;
    @BindView(R.id.tv_description)
    TextView tv_Description;
    @BindView(R.id.tv_weather)
    TextView tv_Weather;
    @BindView(R.id.tv_temperature)
    TextView tv_Temperature;
    @BindView(R.id.tv_wind)
    TextView tv_Wind;
    @BindView(R.id.tv_humidity)
    TextView tv_Humidity;
    @BindView(R.id.tv_pressure)
    TextView tv_Pressure;

    @Override
    protected WeatherPresenter createPresenter() {
        return new WeatherPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadWeather();

    }

    @Override
    public void loadWeather(WeatherInfo weatherInfo) {
        Logger.d(weatherInfo.toString());
        tv_City.setText(weatherInfo.getCity());
        tv_Description.setText(weatherInfo.getWeatherDescription());
        tv_Weather.setText(weatherInfo.getWeather());
        tv_Temperature.setText(weatherInfo.getTemperature());
        tv_Wind.setText(weatherInfo.getDeg()+"\r\n"+weatherInfo.getSpd()+getString(R.string.wind_unit));
        tv_Humidity.setText(weatherInfo.getHumidity());
        tv_Pressure.setText(weatherInfo.getPressure());
        String icon = weatherInfo.getIcon();
        if("01d".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w01d_1);
        }else if ("01n".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w01n_1);
        }else if ("02d".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w02d_1);
        }else if ("02n".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w02n_1);
        }else if ("03d".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w03d_1);
        }else if ("03n".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w03n_1);
        }else if ("04d".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w04d_1);
        }else if ("04n".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w04n_1);
        }else if ("09d".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w09d_1);
        }else if ("09n".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w09n_1);
        }else if ("10d".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w10d_1);
        }else if ("10n".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w10n_1);
        }else if ("11d".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w11d_1);
        }else if ("11n".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w11n_1);
        }else if ("13d".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w13d_1);
        }else if ("13n".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w13n_1);
        }else if ("50d".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w50d_1);
        }else if ("50n".equals(icon)){
            iv_Icon.setImageResource(R.drawable.w50n_1);
        }
    }
}
