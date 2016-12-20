package com.wiatec.btv_launcher.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by PX on 2016-12-01.
 */

public class CloudImageData implements ICloudImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {

        String path = Application.getContext().getExternalFilesDir("images").getAbsolutePath();
        File file = new File(path);
        if(!file.exists()){
            onLoadListener.onFailure("file not exists");
            return;
        }
        File [] files = file.listFiles();
        onLoadListener.onSuccess(files);
    }
}
