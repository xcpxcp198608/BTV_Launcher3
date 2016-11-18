package com.wiatec.btv_launcher.service_task;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.Utils.Logger;

import org.json.JSONObject;

/**
 * Created by PX on 2016-11-17.
 */

public class LoadWeather implements Runnable {

    @Override
    public void run() {
        loadLocation();
    }

    private void loadLocation (){
        String url = "";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){

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
        String url = "";
        String apiKey = "";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setTag("weather");
        Application.getRequestQueue().add(jsonObjectRequest);
    }
}
