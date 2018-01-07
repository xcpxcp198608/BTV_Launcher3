package com.px.common.http.Request;

import com.px.common.http.configuration.Header;
import com.px.common.http.configuration.Parameters;

import java.io.File;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UploadRequest extends RequestMaster {

    private String url;
    private File mFile;

    public UploadRequest (String url){
        this.url = url;
    }

    public UploadRequest file (File file){
        this.mFile = file;
        return this;
    }

    @Override
    protected Request createRequest(Header header, Parameters parameters, Object tag) {
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);
        if(parameters != null){
            for(Map.Entry<String ,String > entry:parameters.stringMap.entrySet() ){
                bodyBuilder.addFormDataPart(entry.getKey() ,entry.getValue());
            }
        }
        if(mFile!= null){
            bodyBuilder.addFormDataPart("file" , mFile.getName() , RequestBody.create(null,mFile));
        }
        Request.Builder builder = new Request.Builder();
        if(header!= null){
            Headers headers = Headers.of(header.stringMap);
            builder.headers(headers);
        }
        builder.post(bodyBuilder.build()).url(url);
        builder.tag("upload");
        return builder.build();
    }
}
