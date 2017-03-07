package com.wiatec.btv_launcher.Utils.OkHttp.Request;

import android.preference.PreferenceActivity;
import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import okhttp3.Headers;

/**
 * Created by patrick on 2016/12/20.
 */

public class Header {
    public Map<String ,String> stringMap = new ConcurrentHashMap<>();

    private String key;
    private String value;

    public Header (){

    }

    public Header(String key, String value) {
        this.key = key;
        this.value = value;
        stringMap.put(key ,value);
    }

    public void put (String key ,String value){
        if(TextUtils.isEmpty(key) || TextUtils.isEmpty(value)){
            return;
        }
        stringMap.put(key ,value);
    }

    public String get (String key){
        return stringMap.get(key);
    }
}
