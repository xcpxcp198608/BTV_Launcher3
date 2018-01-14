package com.px.common.http.listener;

import com.px.common.http.pojo.ResultInfo;
import com.px.common.utils.Logger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public abstract class UploadListener extends BaseListener {

    public abstract void onSuccess(ResultInfo resultInfo) throws Exception;

    public UploadListener() {
        super(null);
    }

    @Override
    protected void handResponse(String jsonString) throws Exception {
        Logger.d(jsonString);
    }
}
