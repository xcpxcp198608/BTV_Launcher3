package com.wiatec.btv_launcher.data;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Activity.ILoginActivity;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by patrick on 2016/12/29.
 */

public class LoginData implements ILoginData {
    @Override
    public void login(final String userName, final String password, final OnLoginListener onLoginListener) {
        String url = F.url.login+"?username="+userName+"&password="+password;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    Result result = new Gson().fromJson(response , new TypeToken<Result>(){}.getType());
                    onLoginListener.onSuccess(result);
                }else{
                    onLoginListener.onFailure("no response");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onLoginListener.onFailure(error.getMessage());
            }
        });
        stringRequest.setTag("login");
        com.wiatec.btv_launcher.Application.getRequestQueue().add(stringRequest);

    }
}
