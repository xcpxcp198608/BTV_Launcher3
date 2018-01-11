package com.px.common.http.listener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public abstract class UploadListener extends BaseListener {

    public abstract void onSuccess(Response response) throws Exception;

    public UploadListener(Class mClass) {
        super(mClass);
    }

    @Override
    protected void handResponse(String jsonString) throws Exception {

    }
}
