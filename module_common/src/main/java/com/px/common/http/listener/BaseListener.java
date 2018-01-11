package com.px.common.http.listener;

import android.text.TextUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by patrick on 11/01/2018.
 * create time : 1:55 PM
 */

public abstract class BaseListener<T> implements Callback {

    public abstract void onFailure (String e);

    protected Class<T> mClass;

    public BaseListener(Class<T> mClass) {
        this.mClass = mClass;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            if(response.code() == 400){
                handFailure("request error");
                return;
            }
            if(response.code() == 404){
                handFailure("resource no found");
                return;
            }
            if(response.code() == 408){
                handFailure("request timeout");
                return;
            }
            if(response.code() == 500){
                handFailure("server exception");
                return;
            }
            ResponseBody responseBody = response.body();
            if(responseBody == null){
                handFailure("response body is null");
                return;
            }
            handResponse(responseBody.string());
        } catch (Exception e) {
            handFailure(e.getMessage());
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        handFailure(e.getMessage());
    }

    protected abstract void handResponse(String jsonString) throws Exception;

    protected void handFailure(String errorMessage){
        if(TextUtils.isEmpty(errorMessage)){
            errorMessage = "unknown error";
        }
        Flowable.just(errorMessage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        onFailure(s);
                    }
                });
    }

    protected ParameterizedType getType(final Class c, final Type... args){
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return args;
            }

            @Override
            public Type getRawType() {
                return c;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
