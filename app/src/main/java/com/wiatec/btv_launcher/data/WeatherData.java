package com.wiatec.btv_launcher.data;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.LocationInfo;
import com.wiatec.btv_launcher.bean.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by PX on 2016-11-16.
 */

public class WeatherData implements  IWeatherData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        String ip_url = "http://ip-api.com/json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(ip_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        LocationInfo locationInfo = new LocationInfo();
                        locationInfo.setCountry(response.getString("country"));
                        locationInfo.setCity(response.getString("city"));
                        locationInfo.setCountryCode(response.getString("countryCode"));
                        locationInfo.setIp(response.getString("query"));
                        locationInfo.setIsp(response.getString("isp"));
                        locationInfo.setRegionName(response.getString("regionName"));
                        if(locationInfo.getCity()!=null){
                            Logger.d(locationInfo.getCity());
                            loadWeather(locationInfo.getCity() , onLoadListener);
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
        jsonObjectRequest.setTag("WeatherInfo");
        Application.getRequestQueue().add(jsonObjectRequest);

    }

    private void loadWeather (String city , final OnLoadListener onLoadListener) {
        String url = "http://apis.baidu.com/heweather/weather/free";
        String httpUrl = url+"?city="+city;
        final String apiKey = "6d2c261609e14bac4c78081dd57c7ddc";
        StringRequest stringRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response!=null){
                    try {
                        Logger.d(response.toString());
                        WeatherInfo weatherInfo = new WeatherInfo();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("HeWeather data service 3.0");
                        JSONObject data = jsonArray.getJSONObject(0);
                        JSONObject basic = data.getJSONObject("basic");//````
                        weatherInfo.setCity(basic.getString("city"));
                        JSONArray daily_forecast = data.getJSONArray("daily_forecast");
                        JSONObject today = daily_forecast.getJSONObject(0);
                        JSONObject tmp = today.getJSONObject("tmp");
                        weatherInfo.setMaxTemperature(tmp.getString("max"));
                        weatherInfo.setMinTemperature(tmp.getString("min"));
                        JSONObject now = data.getJSONObject("now");
                        JSONObject cond = now.getJSONObject("cond");
                        weatherInfo.setWeather(cond.getString("txt"));
                        weatherInfo.setHumidity(now.getString("hum"));
                        weatherInfo.setTemperature(now.getString("tmp"));
                        JSONObject wind = now.getJSONObject("wind");
                        weatherInfo.setDeg(wind.getString("deg"));
                        weatherInfo.setSpd(wind.getString("spd"));
                        Logger.d(weatherInfo.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(error.getMessage());
                onLoadListener.onFailure(error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String , String> headers = new HashMap<String ,String>();
                headers.put("apikey",apiKey);
                headers.put("Charset", "UTF-8");
                headers.put("Content-Type", "application/x-javascript");
                headers.put("Accept-Encoding", "gzip,deflate");
                return headers;
            }
        };
        stringRequest.setTag("weather");
        Application.getRequestQueue().add(stringRequest);
    }
}
