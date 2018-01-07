package com.wiatec.btv_launcher.data;

import com.px.common.utils.CommonApplication;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.SQL.WeatherDao;
import com.wiatec.btv_launcher.bean.WeatherInfo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WeatherData implements  IWeatherData {

    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Function<String, WeatherInfo>() {
                    @Override
                    public WeatherInfo apply(String s) {
                        WeatherInfo w = WeatherDao.getInstance(CommonApplication.context).query();
                        return w;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WeatherInfo weatherInfo) {
                        onLoadListener.onSuccess(weatherInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onLoadListener.onFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
