package com.wiatec.btv_launcher.Utils.OkHttp.Request;

import android.text.TextUtils;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SPUtils;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;

/**
 * Created by patrick on 2016/12/20.
 */

public class GetRequest extends RequestMaster {

    private String url;
    public GetRequest(String url) {
        this.url = url;
    }

    @Override
    protected Request createRequest(Header header, Parameters parameters ,Object tag) {
        Request.Builder builder = new Request.Builder();
        String cookie = (String) SPUtils.get("cookie", "");
        if(!TextUtils.isEmpty(cookie)){
            headers("Cookie", cookie);
        }
        if(header !=null){
            Headers headers = Headers.of(header.stringMap);
            builder.headers(headers);
        }
        if(parameters != null){
            StringBuilder stringBuilder = new StringBuilder(url);
            stringBuilder.append("?");
            for(Map.Entry<String,String> entry : parameters.stringMap.entrySet()){
                stringBuilder.append(entry.getKey()+"=").append(entry.getValue()+"&");
            }
            url = stringBuilder.toString().substring(0,stringBuilder.toString().length()-1);
        }
        if(tag != null){
            builder.tag(tag);
        }
        //Logger.d(url);
        builder.get().url(url);
        return builder.build();
    }
}
