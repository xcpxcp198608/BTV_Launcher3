package com.wiatec.btv_launcher.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.MessageDao;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Activity.IMainActivity;
import com.wiatec.btv_launcher.Utils.OkHttp.Bean.DownloadInfo;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.DownloadListener;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.Utils.SystemConfig;
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
import java.io.IOException;
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
        try {
            if(iWeatherData !=null){
                iWeatherData.loadData(new IWeatherData.OnLoadListener() {
                    @Override
                    public void onSuccess(WeatherInfo weatherInfo) {
                        iMainActivity.loadWeatherInfo(weatherInfo);
                    }

                    @Override
                    public void onFailure(String e) {
                       // Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadVideo (){
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadUpdate(){
        try {
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadMessage1 (){
        try {
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
        }catch (Exception e){
            e.printStackTrace();
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
        try {
            final MessageDao messageDao = MessageDao.getInstance(Application.getContext());
            OkMaster.get(F.url.message)
                    .parames("deviceInfo.countryCode", SPUtils.get(Application.getContext() , "countryCode" , ""))
                    .enqueue(new StringListener() {
                        @Override
                        public void onSuccess(String s) throws IOException {
                            if(s != null) {
                                List<MessageInfo> list = new Gson().fromJson(s, new TypeToken<List<MessageInfo>>() {
                                }.getType());
                                if(list == null){
                                    return;
                                }
                                for (MessageInfo messageInfo:list){
                                    messageDao.insertMessage(messageInfo);
                                }
                            }
                        }

                        @Override
                        public void onFailure(String e) {

                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadLocation(){
        String url = "http://ip-api.com/json";
        OkMaster.get(url)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        try {
                            JSONObject response = new JSONObject(s);
                            String city = response.getString("city");
                            String country = response.getString("country");
                            String countryCode = response.getString("countryCode");
                            String regionName = response.getString("regionName");
                            String timeZone = response.getString("timezone");
                            String ip = response.getString("query");
                            // Logger.d(country +"---"+ countryCode +"---"+ city);
                            SPUtils.put("countryCode",countryCode);
                            SPUtils.put("country",country);
                            SPUtils.put("regionName",regionName);
                            SPUtils.put("timeZone",timeZone);
                            SPUtils.put("city",city);
                            SPUtils.put("ip",ip);
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

    public void downloadAdVideo (String name , String url){
        if(!SystemConfig.isNetworkConnected(Application.getContext())){
            return;
        }
        OkMaster.download(Application.getContext())
                .path(F.path.download)
                .name(name)
                .url(url)
                .startDownload(new DownloadListener() {
                    @Override
                    public void onPending(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onStart(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onPause(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onFinished(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onCancel(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onError(DownloadInfo downloadInfo) {

                    }
                });
    }
}
