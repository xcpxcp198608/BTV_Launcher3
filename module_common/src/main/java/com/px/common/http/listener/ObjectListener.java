package com.px.common.http.listener;


import com.google.gson.Gson;
import com.px.common.UpdateInfo;
import com.px.common.utils.Logger;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public abstract class ObjectListener<T> extends BaseListener {

    public abstract void onSuccess(T t) throws Exception;

    public ObjectListener(Class clasz) {
        super(clasz);
    }

    @Override
    protected void handResponse(String jsonString) throws Exception{
        Observable.just(jsonString)
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String jsonString) throws Exception {
                        return (T) new Gson().fromJson(jsonString, mClass);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<T>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(T t) {
                        try {
                            onSuccess(t);
                        } catch (Exception e) {
                            handFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        handFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
