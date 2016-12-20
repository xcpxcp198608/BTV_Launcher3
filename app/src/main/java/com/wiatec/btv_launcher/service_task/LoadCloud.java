package com.wiatec.btv_launcher.service_task;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.CloudImageDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.FileCheck;
import com.wiatec.btv_launcher.Utils.ImageDownload.DownloadInfo;
import com.wiatec.btv_launcher.Utils.ImageDownload.DownloadManager;
import com.wiatec.btv_launcher.Utils.ImageDownload.OnDownloadListener;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.CloudImageInfo;
import com.wiatec.btv_launcher.bean.TokenInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-12-01.
 */

public class LoadCloud implements Runnable {

    private DownloadManager downloadManager;

    public LoadCloud() {
        downloadManager = DownloadManager.getInstance(Application.getContext());
    }

    @Override
    public void run() {
        loadCloudToken();
    }

    private void loadCloudToken (){
        //Logger.d("start");
        if(!ApkCheck.isApkInstalled(Application.getContext() , F.package_name.cloud)){
            return;
        }
        ContentResolver contentResolver = Application.getContext().getContentResolver();
        Uri uri = Uri.parse("content://com.legacydirect.tvphoto.provider.AuthProvider/token");
        Cursor cursor = null;
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
                return;
            }
            String url = "https://apps.legacydirect.cloud/api/file/get_folder_file_list?path=MemoSync/Uploads&authcode="+ token+"&order_by=date";
            final String url1 = "https://apps.legacydirect.cloud/api/file/get?authcode="+ token+"&path=";
            JsonObjectRequest j = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Logger.d(response.toString());
                    try {
                        JSONObject jsonObject = response.getJSONObject("result");
                        JSONArray jsonArray = jsonObject.getJSONArray("file");
                        if(jsonArray!=null && jsonArray.length() >0){
                            CloudImageDao cloudImageDao = CloudImageDao.getInstance();
                            int length = 0 ;
                            if(jsonArray.length()>10){
                                length = 10;
                            }else {
                                length = jsonArray.length();
                            }
                            for (int i = 0 ; i <length ; i++){
                                final CloudImageInfo imageInfo = new CloudImageInfo();
                                String name = jsonArray.getString(i);
                                String []names = name.split("/");
                                String realName = names[2];
                                String url = url1 +name;
                                imageInfo.setUrl(url);
                                imageInfo.setName(realName);
                                String path = Application.getContext().getExternalFilesDir("images").getAbsolutePath();
                                imageInfo.setPath(path);
                                if(FileCheck.isFileExists(path , imageInfo.getName())){

                                }else {

                                    downloadManager.startDownload(imageInfo.getName() , imageInfo.getUrl() , imageInfo.getPath());
                                    downloadManager.setDownloadListener(imageInfo.getName(), new OnDownloadListener() {
                                        @Override
                                        public void onPending(DownloadInfo downloadInfo) {

                                        }

                                        @Override
                                        public void onStart(DownloadInfo downloadInfo, boolean isStart) {
                                            Logger.d(downloadInfo.getName()+"start");
                                        }

                                        @Override
                                        public void onProgressChange(DownloadInfo downloadInfo, int progress) {
                                            Logger.d(downloadInfo.getName()+progress);
                                        }

                                        @Override
                                        public void onPause(DownloadInfo downloadInfo) {

                                        }

                                        @Override
                                        public void onFinished(DownloadInfo downloadInfo, int progress) {
                                            Logger.d(downloadInfo.getName()+"onFinished");
                                        }

                                        @Override
                                        public void onError(DownloadInfo downloadInfo, String e) {

                                        }

                                        @Override
                                        public void onRemove(DownloadInfo downloadInfo) {

                                        }
                                    });
                                }
                               // Logger.d(imageInfo.toString());
                                if(!cloudImageDao.isExists(imageInfo)){
                                    cloudImageDao.insert(imageInfo);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.d(error.getMessage());
                }
            });
            j.setTag("token");
            Application.getRequestQueue().add(j);
        }catch (Exception e){
            e.printStackTrace();
            Logger.d(e.getMessage());
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
    }
}
