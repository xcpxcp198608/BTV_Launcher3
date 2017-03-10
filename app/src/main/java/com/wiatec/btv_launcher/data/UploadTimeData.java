package com.wiatec.btv_launcher.data;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by patrick on 2017/2/27.
 */

public class UploadTimeData {

    private  OkHttpClient okHttpClient;

    public UploadTimeData() {
        okHttpClient = new OkHttpClient();
    }

    public void upload(final String exitTime , final String holdTime){
        Request.Builder builder = new Request.Builder();
        builder.url(F.url.upload_data);
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("exitTime",exitTime);
        formBuilder.add("holdTime",holdTime);
        builder.post(formBuilder.build());
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

            }
        });
    }
}
