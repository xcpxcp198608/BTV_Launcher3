package com.wiatec.btv_launcher.Utils.OkHttp.Listener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by patrick on 2016/12/26.
 */

public abstract class UploadListener implements Callback {

    public abstract void onSuccess(Response response) throws IOException;
    public abstract void onFailure(String e);

    @Override
    public void onFailure(Call call, IOException e) {
        onFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if(response!= null) {
            onSuccess(response);
        }
    }
}
