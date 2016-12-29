package com.wiatec.btv_launcher.service_task;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.CloudImageDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.FileDownload.DownloadManager;
import com.wiatec.btv_launcher.Utils.FileDownload.OnDownloadListener;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.CloudImageInfo;
import com.wiatec.btv_launcher.bean.CloudInfo;
import com.wiatec.btv_launcher.bean.TokenInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by PX on 2016-12-01.
 */

public class LoadCloud implements Runnable {

    private String path;

    public LoadCloud() {
        path = Application.getContext().getExternalFilesDir("images").getAbsolutePath();
    }

    @Override
    public void run() {
        loadCloudImage(loadCloudToken());
    }

    private CloudInfo loadCloudToken (){
        if(!ApkCheck.isApkInstalled(Application.getContext() , F.package_name.cloud)){
            return null;
        }
        ContentResolver contentResolver = Application.getContext().getContentResolver();
        Uri uri = Uri.parse("content://com.legacydirect.tvphoto.provider.AuthProvider/token");
        Cursor cursor = null;
        CloudInfo cloudInfo = new CloudInfo();
        try {
            cursor = contentResolver.query(uri ,null,null,null,null);
            TokenInfo tokenInfo = new TokenInfo();
            if (cursor!= null){
                cursor.moveToFirst();
                tokenInfo.setResult(cursor.getString(cursor.getColumnIndex("result")));
                tokenInfo.setError(cursor.getString(cursor.getColumnIndex("error")));
                tokenInfo.setToken(cursor.getString(cursor.getColumnIndex("token")));
            }
            String token = tokenInfo.getToken();
            Logger.d(token);
            if(TextUtils.isEmpty(token)){
                return null;
            }
            String url = "https://apps.legacydirect.cloud/api/file/get_folder_file_list?path=MemoSync/Uploads&authcode="+ token+"&order_by=date";
            cloudInfo.setToken(token);
            cloudInfo.setUrl(url);
        }catch (Exception e){
            e.printStackTrace();
            Logger.d(e.getMessage());
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
       // Logger.d(cloudInfo.toString());
        return cloudInfo;
    }

    public void loadCloudImage(CloudInfo cloudInfo){
        if(cloudInfo == null){
            return;
        }
        final String baseUrl = "https://apps.legacydirect.cloud/api/file/get?authcode="+ cloudInfo.getToken()+"&path=";
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = builder.build();
        try {
            Request request = new Request.Builder().get().url(cloudInfo.getUrl()).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Logger.d(e.getMessage());
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if (response != null) {
                        String jsonString = response.body().string();
                        DownloadManager downloadManager = DownloadManager.getInstance(Application.getContext());
                        try {
                            JSONObject result = new JSONObject(jsonString);
                            JSONObject jsonObject = result.getJSONObject("result");
                            JSONArray jsonArray = jsonObject.getJSONArray("file");
                            if (jsonArray != null && jsonArray.length() > 0) {
                                List<String> list = new ArrayList<>();
                                int length = 0;
                                if (jsonArray.length() > 10) {
                                    length = 10;
                                } else {
                                    length = jsonArray.length();
                                }
                                for (int i = 0; i < length; i++) {
                                    final CloudImageInfo imageInfo = new CloudImageInfo();
                                    String url1 = jsonArray.getString(i);
                                    String name = url1.split("/")[2];
                                    String url = baseUrl + url1;
                                    imageInfo.setUrl(url);
                                    imageInfo.setName(name);
                                    imageInfo.setPath(path);
                                    Logger.d(imageInfo.toString());
                                    downloadManager.startDownload(imageInfo.getName(), imageInfo.getUrl(), imageInfo.getPath());
                                    list.add(imageInfo.getName());
                                }
                                isContainsFile(path, list);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void isContainsFile(String path , List<String> list){
        File file = new File(path);
        if(file.exists()){
            File [] files = file.listFiles();
            for (int i = 0 ; i <files.length ; i++){
                File file1 = files [i];
                if(! list.contains(file1.getName())){
                    Logger.d(file1.getName() +" need delete");
                    file1.delete();
                }else{
                    Logger.d(file1.getName() +" keep");
                }
            }

        }
    }
}
