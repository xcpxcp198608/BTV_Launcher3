package com.wiatec.btv_launcher.service_task;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.OnWeatherChangeListener;
import com.wiatec.btv_launcher.SQL.WeatherDao;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.WeatherInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PX on 2016-11-17.
 */

public class LoadWeather implements Runnable {

    @Override
    public void run() {
       // Logger.d("start load weather");
        loadLocation();
    }

    private void loadLocation (){
        String url = "http://ip-api.com/json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        String city = response.getString("city");
                        Logger.d(city);
                        loadWeather(city);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(error.getMessage());
            }
        });
        jsonObjectRequest.setTag("location");
        Application.getRequestQueue().add(jsonObjectRequest);
    }

    private void loadWeather (String city){
        String apiKey = "c0c69463f12ddb77b388fe9fac994407";
        String langUrl = "http://api.openweathermap.org/data/2.5/weather?q=shenzhen&lang=zh_cn&APPID=c0c69463f12ddb77b388fe9fac994407";
        String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID="+apiKey;
        String forecastUrl = "http://api.openweathermap.org/data/2.5/forecast?q=shenzhen,cn&APPID=c0c69463f12ddb77b388fe9fac994407";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response !=null){
                    try {
                        WeatherInfo weatherInfo = new WeatherInfo();
                        JSONObject jsonObject = new JSONObject(response);
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
                            WeatherDao weatherDao = WeatherDao.getInstance(Application.getContext());
                            weatherDao.deleteAll();
                            if(weatherDao.insertWeather(weatherInfo)){
                                Intent intent = new Intent();
                                intent.setAction("action.Weather.Change");
                                intent.putExtra("weatherInfo" , weatherInfo);
                                Application.getContext().sendBroadcast(intent);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(error.getMessage());
            }
        });
        stringRequest.setTag("weather");
        Application.getRequestQueue().add(stringRequest);
    }

    private float kelvinToCelsius (float kelvin){
        float t = kelvin - 273.15f;
        float t1 = (float)(Math.round(t*10))/10;
        return t1;
    }

    private int celsiusToFahrenheit (float celsius){
        int f = (int) ((1.8f+celsius)+32);
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
