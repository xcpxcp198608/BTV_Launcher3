package com.wiatec.btv_launcher.data;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.bean.ImageInfo;

import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

/**
 * Created by patrick on 2017/3/6.
 */

public class ManualData implements IManualData {
    @Override
    public void loadData(final OnLoadListener onLoadListener, final String product, final String language) {
        OkMaster.get(F.url.manual_image)
                .parames("product",product)
                .parames("language",language)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        if(s == null){
                            return;
                        }
                        List<ImageInfo> list = new Gson().fromJson(s , new TypeToken<List<ImageInfo>>(){}.getType());
                        if(list == null || list.size() <= 0){
                            return;
                        }
                        onLoadListener.onSuccess(list);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onFailure(e);
                        OkMaster.get(F.url_eu.manual_image)
                                .parames("product",product)
                                .parames("language",language)
                                .enqueue(new StringListener() {
                                    @Override
                                    public void onSuccess(String s) throws IOException {
                                        if(s == null){
                                            return;
                                        }
                                        List<ImageInfo> list = new Gson().fromJson(s , new TypeToken<List<ImageInfo>>(){}.getType());
                                        if(list == null || list.size() <= 0){
                                            return;
                                        }
                                        onLoadListener.onSuccess(list);
                                    }

                                    @Override
                                    public void onFailure(String e) {
                                        onLoadListener.onFailure(e);
                                    }
                                });
                    }
                });
    }
}
