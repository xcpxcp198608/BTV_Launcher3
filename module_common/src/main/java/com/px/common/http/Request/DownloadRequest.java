package com.px.common.http.request;

import android.content.Context;
import android.text.TextUtils;

import com.px.common.http.pojo.DownloadInfo;
import com.px.common.http.configuration.Header;
import com.px.common.http.configuration.Parameters;

import java.util.Map;
import okhttp3.Headers;
import okhttp3.Request;


public class DownloadRequest extends RequestMaster {

    private Context mContext;
    private String mName;
    private String mUrl;
    private String mPath;

    public DownloadRequest(Context context){
        this.mContext = context;
    }

    public DownloadRequest name(String name){
        this.mName = name;
        return this;
    }

    public DownloadRequest url (String url){
        this.mUrl = url;
        return this;
    }

    public DownloadRequest path (String path){
        this.mPath = path;
        return this;
    }

    public DownloadInfo createDownlaodInfo (){
        DownloadInfo downloadInfo = new DownloadInfo();
        if(TextUtils.isEmpty(mName)){
            mName = mUrl.split("/")[mUrl.split("/").length -1];
        }
        if(TextUtils.isEmpty(mPath)){
            try {
                mPath = mContext.getExternalFilesDir("download").getAbsolutePath();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        downloadInfo.setName(mName);
        downloadInfo.setUrl(mUrl);
        downloadInfo.setPath(mPath);
        return downloadInfo;
    }

    @Override
    protected Request createRequest(Header header, Parameters parameters, Object tag) {
        mDownloadInfo = createDownlaodInfo();
        Request.Builder builder = new Request.Builder();
        if(header !=null){
            Headers headers = Headers.of(header.stringMap);
            builder.headers(headers);
        }
        if(parameters != null){
            StringBuilder stringBuilder = new StringBuilder(mUrl);
            stringBuilder.append("?");
            for(Map.Entry<String,String> entry : parameters.stringMap.entrySet()){
                stringBuilder.append(entry.getKey()+"=").append(entry.getValue()+"&");
            }
            mUrl = stringBuilder.toString().substring(0,stringBuilder.toString().length()-1);
        }
        if(tag != null){
            builder.tag(tag);
        }
        builder.get().url(mUrl);
        builder.tag("download");
        return builder.build();
    }
}
