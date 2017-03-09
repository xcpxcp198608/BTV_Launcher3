package com.wiatec.btv_launcher.presenter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.MessageDao;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Activity.IMainActivity;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.Message1Info;
import com.wiatec.btv_launcher.bean.MessageInfo;
import com.wiatec.btv_launcher.bean.UpdateInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.bean.WeatherInfo;
import com.wiatec.btv_launcher.data.AdVideoData;
import com.wiatec.btv_launcher.data.BootAdVideoData;
import com.wiatec.btv_launcher.data.IAdVideoData;
import com.wiatec.btv_launcher.data.IBootAdVideoData;
import com.wiatec.btv_launcher.data.IMessage1Data;
import com.wiatec.btv_launcher.data.IUpdateData;
import com.wiatec.btv_launcher.data.IWeatherData;
import com.wiatec.btv_launcher.data.Message1Data;
import com.wiatec.btv_launcher.data.UpdateData;
import com.wiatec.btv_launcher.data.WeatherData;
import com.wiatec.btv_launcher.service_task.LoadInstalledApp;
import com.wiatec.btv_launcher.service_task.LoadKodiData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public class MainPresenter extends BasePresenter<IMainActivity> {

    private IMainActivity iMainActivity;
    private IMessage1Data iMessage1Data;
    private IUpdateData iUpdateData;
    private IBootAdVideoData iBootAdVideoData;
    private IAdVideoData iAdVideoData;
    private IWeatherData iWeatherData;

    public MainPresenter(IMainActivity iMainActivity) {
        this.iMainActivity = iMainActivity;
        iMessage1Data = new Message1Data();
        iUpdateData = new UpdateData();
        iBootAdVideoData = new BootAdVideoData();
        iWeatherData = new WeatherData();
        iAdVideoData = new AdVideoData();
    }

    public void loadWeatherInfo (){
        if(iWeatherData !=null){
            iWeatherData.loadData(new IWeatherData.OnLoadListener() {
                @Override
                public void onSuccess(WeatherInfo weatherInfo) {
                    iMainActivity.loadWeatherInfo(weatherInfo);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }

    public void loadVideo (){
        if(iBootAdVideoData != null){
            iBootAdVideoData.loadData(new IBootAdVideoData.OnLoadListener() {
                @Override
                public void onSuccess(VideoInfo videoInfo) {
                    iMainActivity.loadBootAdVideo(videoInfo);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }

        if(iAdVideoData != null){
            iAdVideoData.loadData(new IAdVideoData.OnLoadListener() {
                @Override
                public void onSuccess(VideoInfo videoInfo) {
                    iMainActivity.loadAdVideo(videoInfo);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }

    public void loadUpdate(){
        if(iUpdateData != null){
            iUpdateData.loadData(new IUpdateData.OnLoadListener() {
                @Override
                public void onSuccess(UpdateInfo updateInfo) {
                    iMainActivity.loadUpdate(updateInfo);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }

    public void loadMessage1 (){
        if(iMessage1Data != null){
            iMessage1Data.loadData(new IMessage1Data.OnLoadListener() {
                @Override
                public void onSuccess(List<Message1Info> list) {
                    iMainActivity.loadMessage1(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }

    public void loadInstalledApp(){
        Application.getThreadPool().execute(new LoadInstalledApp());
    }

    public void loadKodiData(){
        File kodiDir = new File(F.path.kodi_image_path);
        if(kodiDir.exists()){
            Application.getThreadPool().execute(new LoadKodiData());
        }
    }

    public void loadMessage(){
        final MessageDao messageDao = MessageDao.getInstance(Application.getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(F.url.message, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(response != null) {
                    List<MessageInfo> list = new Gson().fromJson(String.valueOf(response), new TypeToken<List<MessageInfo>>() {
                    }.getType());
                    for (MessageInfo messageInfo:list){
                        messageDao.insertMessage(messageInfo);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d(error.getMessage());
            }
        });
        jsonArrayRequest.setTag("MessageInfo");
        Application.getRequestQueue().add(jsonArrayRequest);
    }

    public void loadLocation(){
        String url = "http://ip-api.com/json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null){
                    try {
                        String city = response.getString("city");
                        String country = response.getString("country");
                        String countryCode = response.getString("countryCode");
                        Logger.d(country +"---"+ countryCode +"---"+ city);
                        SPUtils.put(Application.getContext() , "countryCode",countryCode);
                        SPUtils.put(Application.getContext() , "country",country);
                        SPUtils.put(Application.getContext() , "city",city);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.d("location load error "+error.getMessage());
            }
        });
        jsonObjectRequest.setTag("location");
        Application.getRequestQueue().add(jsonObjectRequest);
    }
}
