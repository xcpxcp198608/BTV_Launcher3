package com.wiatec.btv_launcher.data;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.SQL.WeatherDao;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.LocationInfo;
import com.wiatec.btv_launcher.bean.WeatherInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-16.
 */

public class WeatherData implements  IWeatherData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, WeatherInfo>() {
                    @Override
                    public WeatherInfo call(String s) {
                        WeatherInfo w = WeatherDao.getInstance(Application.getContext()).query();
                        return w;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WeatherInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onLoadListener.onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(WeatherInfo weatherInfo) {
                        onLoadListener.onSuccess(weatherInfo);
                    }
                });

    }

}
