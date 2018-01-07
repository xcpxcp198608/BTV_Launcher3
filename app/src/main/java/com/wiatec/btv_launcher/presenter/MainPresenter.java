package com.wiatec.btv_launcher.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.MessageDao;
import com.wiatec.btv_launcher.Activity.IMainActivity;
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

    public void loadAdVideo(){
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
                        iMainActivity.loadUpdate(null);
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
            final MessageDao messageDao = MessageDao.getInstance(CommonApplication.context);
            HttpMaster.get(F.url.message)
                    .param("deviceInfo.countryCode", SPUtil.get("countryCode" , ""))
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
        HttpMaster.get(url)
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
                            SPUtil.put("countryCode",countryCode);
                            SPUtil.put("country",country);
                            SPUtil.put("regionName",regionName);
                            SPUtil.put("timeZone",timeZone);
                            SPUtil.put("city",city);
                            SPUtil.put("ip",ip);
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
        if(!NetUtil.isConnected()){
            return;
        }
        HttpMaster.download(CommonApplication.context)
                .path(F.path.download)
                .name(name)
                .url(url)
                .startDownload(new com.px.common.http.Listener.DownloadListener() {
                    @Override
                    public void onPending(com.px.common.http.Bean.DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onStart(com.px.common.http.Bean.DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onPause(com.px.common.http.Bean.DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(com.px.common.http.Bean.DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onFinished(com.px.common.http.Bean.DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onCancel(com.px.common.http.Bean.DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onError(com.px.common.http.Bean.DownloadInfo downloadInfo) {

                    }
                });
    }
}
