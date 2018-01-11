package com.px.common.http.listener;


import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public abstract class ListListener<T> extends BaseListener {

    public abstract void onSuccess(List<T> list) throws Exception;

    public ListListener(Class clasz) {
        super(clasz);
    }

    @Override
    protected void handResponse(String jsonString) throws Exception{
        Observable.just(jsonString)
                .map(new Function<String, List<T>>() {
                    @Override
                    public List<T> apply(String jsonString) throws Exception {
                        ParameterizedType parameterizedType = getType(List.class, mClass);
                        return new Gson().fromJson(jsonString, parameterizedType);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<T>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<T> list) {
                        try {
                            if(list != null && list.size() > 0){
                                onSuccess(list);
                            }else{
                                handFailure("result list in null");
                            }
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
