package com.px.common.http.listener;


import com.google.gson.Gson;
import com.px.common.http.pojo.ResultInfo;
import com.px.common.utils.Logger;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public abstract class ResultListener<T> extends BaseListener {

    public abstract void onSuccess(ResultInfo<T> resultInfo) throws Exception;


    public ResultListener(Class clasz) {
        super(clasz);
    }

    @Override
    protected void handResponse(String jsonString) throws Exception {
        Observable.just(jsonString)
                .map(new Function<String, ResultInfo<T>>() {
                    @Override
                    public ResultInfo<T> apply(String jsonString) throws Exception {
                        Logger.d(jsonString);
                        ParameterizedType parameterizedType = getType(ResultInfo.class, mClass);
                        return new Gson().fromJson(jsonString, parameterizedType);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultInfo<T>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResultInfo<T> resultInfo) {
                        try {
                            onSuccess(resultInfo);
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
