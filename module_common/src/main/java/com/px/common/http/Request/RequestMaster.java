package com.px.common.http.request;

import android.util.Log;

import com.px.common.http.pojo.DownloadInfo;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.DownloadCallback;
import com.px.common.http.listener.DownloadListener;
import com.px.common.http.listener.UploadListener;
import com.px.common.http.configuration.Header;
import com.px.common.http.configuration.Parameters;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.NetUtil;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public abstract class RequestMaster {
    private Header header;
    private Parameters parameters;
    private Object mTag;
    private Map<Object ,Call> callMap = new ConcurrentHashMap<>();
    protected DownloadInfo mDownloadInfo;

    public RequestMaster() {
        parameters = new Parameters();
        header = new Header();
    }

    public RequestMaster tag(Object tag){
        this.mTag = tag;
        return this;
    }

    public RequestMaster param(String key , String  value){
        parameters.put(key ,value);
        return this;
    }

    public RequestMaster param(String key , File value){
        parameters.put(key ,value);
        return this;
    }

    public RequestMaster param(String key , Object value){
        parameters.put(key ,value);
        return this;
    }

    public RequestMaster params(Parameters parameters){
        this.parameters = parameters;
        return this;
    }

    public RequestMaster headers(String key ,String value){
        header.put (key ,value);
        return this;
    }

    public RequestMaster headers(Header header){
        this.header = header;
        return this;
    }

    protected abstract Request createRequest(Header header, Parameters parameters ,Object tag);

    /**
     * 同步执行请求
     */
    public String execute(){
        try {
            if(!NetUtil.isConnected()){
                return null;
            }
            Request request = createRequest(header, parameters, mTag);
            Call call = HttpMaster.okHttpClient.newCall(request);
            Response response = call.execute();
            return response.body().string();
        }catch (Exception e){
            Log.d("okhttp",e.getMessage());
        }
        return "";
    }

    //异步执行请求
    public void enqueue (Callback callback){
        try {
            if(!NetUtil.isConnected()){
                return;
            }
            Request request = createRequest(header, parameters, mTag);
            Call call = HttpMaster.okHttpClient.newCall(request);
            call.enqueue(callback);
            if (mTag != null) {
                callMap.put(mTag, call);
            }
        }catch (Exception e){
            Log.d("okhttp",e.getMessage());
        }
    }
    //异步执行下载
    public void startDownload (DownloadListener downloadListener){
        try {
            if(!NetUtil.isConnected()){
                return;
            }
            Request request = createRequest(header , parameters ,mTag);
            Call call = HttpMaster.okHttpClient.newCall(request);
            call.enqueue(new DownloadCallback(mDownloadInfo ,downloadListener));
            if(mTag!=null) {
                callMap.put(mTag, call);
            }
        }catch (Exception e){
            Log.d("okhttp",e.getMessage());
        }
    }
    public void upload(UploadListener uploadListener){
        try {
            if(!NetUtil.isConnected()){
                return;
            }
            Request request = createRequest(header , parameters ,mTag);
            Call call = HttpMaster.okHttpClient.newCall(request);
            call.enqueue(uploadListener);
        }catch (Exception e){
            Log.d("okhttp",e.getMessage());
        }
    }
    //通过标签取消请求
    public void cancel (Object tag){
        Call call = callMap.get(tag);
        if(call != null) {
            call.cancel();
        }
    }
    //取消所有请求
    public void cancelAll(){
        HttpMaster.okHttpClient.dispatcher().cancelAll();
    }
}
