package com.wiatec.btv_launcher.service_task;

import android.content.Intent;
import android.text.TextUtils;

import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.SQL.WeatherDao;
import com.wiatec.btv_launcher.bean.WeatherInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PX on 2016-11-17.
 */

public class LoadWeather implements Runnable {

    @Override
    public void run() {
        String city = (String) SPUtil.get( "city" ,"");
        if(TextUtils.isEmpty(city)){
            return;
        }
        try {
            loadWeather(city);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadWeather (String city){
        String apiKey = "c0c69463f12ddb77b388fe9fac994407";
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID="+apiKey;
        String forecastUrl = "http://api.openweathermap.org/data/2.5/forecast?q=shenzhen,cn&APPID=c0c69463f12ddb77b388fe9fac994407";
        HttpMaster.get(url)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        try {
                            WeatherInfo weatherInfo = new WeatherInfo();
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
                            weatherInfo.setWeather(weather.getString("main"));
                            weatherInfo.setWeatherDescription(weather.getString("description"));
                            weatherInfo.setIcon(weather.getString("icon"));
                            JSONObject main = jsonObject.getJSONObject("main");
                            weatherInfo.setTemperature(showTemperature(main.getString("temp")));
                            weatherInfo.setMaxTemperature(showTemperature(main.getString("temp_max")));
                            weatherInfo.setMinTemperature(showTemperature(main.getString("temp_min")));
                            weatherInfo.setHumidity(main.getString("humidity")+"%");
                            weatherInfo.setPressure(main.getString("pressure")+"hPa");
                            JSONObject wind = jsonObject.getJSONObject("wind");
                            weatherInfo.setDeg(wind.getString("deg"));
                            weatherInfo.setSpd(wind.getString("speed"));
                            weatherInfo.setCity(jsonObject.getString("name"));
                            JSONObject sys = jsonObject.getJSONObject("sys");
                            weatherInfo.setCountry(sys.getString("country"));
                            weatherInfo.setSunrise(formatTime(sys.getString("sunrise")));
                            weatherInfo.setSunset(formatTime(sys.getString("sunset")));
                            weatherInfo.setDate(formatDate(sys.getString("sunset")));
                            //Logger.d(weatherInfo.toString());
                            if(weatherInfo != null){
                                WeatherDao weatherDao = WeatherDao.getInstance(CommonApplication.context);
                                weatherDao.deleteAll();
                                if(weatherDao.insertWeather(weatherInfo)){
                                    Intent intent = new Intent();
                                    intent.setAction("action.Weather.Change");
                                    intent.putExtra("weatherInfo" , weatherInfo);
                                    CommonApplication.context.sendBroadcast(intent);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }

    private float kelvinToCelsius (float kelvin){
        float t = kelvin - 273.15f;
        float t1 = (float)(Math.round(t*10))/10;
        return t1;
    }

    private int celsiusToFahrenheit (float celsius){
        int f = (int) ((1.8f * celsius)+32);
        return f;
    }

    private String showTemperature (String kTemp){
        float k = Float.parseFloat(kTemp);
        float c = kelvinToCelsius(k);
        int f = celsiusToFahrenheit(c);
        return ""+c+" ℃ / "+f +" F";
    }

    private String formatTime (String time){
        if(time != null) {
            long l = Long.parseLong(time) * 1000;
            Date date = new Date(l);
            String time1 = new SimpleDateFormat("HH:mm:ss").format(date);
            return time1;
        }else {
            return null;
        }
    }

    private String formatDate (String time) {
        if(time != null) {
            long l = Long.parseLong(time) * 1000;
            Date date = new Date(l);
            String time1 = new SimpleDateFormat("yyyy-MM-dd").format(date);
            return time1;
        }else {
            return null;
        }
    }
}
