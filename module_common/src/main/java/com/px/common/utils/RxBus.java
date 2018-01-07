package com.px.common.utils;

import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * 基于rx java的事件传递处理
 */

public class RxBus {

    private HashMap<String, CompositeDisposable> mSubscriptionMap;
    private static volatile RxBus mRxBus;
    private final io.reactivex.subjects.Subject<Object> mSubject;

    // RxBus单例 ，全局只有一个实例， 双重校验锁保证多线程调用
    private RxBus (){
        mSubject = io.reactivex.subjects.PublishSubject.create().toSerialized();
    }
    private static volatile RxBus instance;
    public static RxBus getDefault(){
        if(instance == null){
            synchronized (RxBus.class){
                if(instance == null){
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    //发送事件（事件由调用者定义）
    public void post(Object object){
        mSubject.onNext(object);
    }

    //根据事件类型获得对应的Flowable
    private <T>Flowable<T> getObservable(Class<T> type){
        return mSubject.toFlowable(BackpressureStrategy.BUFFER)
                .ofType(type);
    }

    /**
     * 订阅 Flowable
     * @param type event type
     * @param <T> fanxing
     * @return flowable<T> object
     */
    public <T> Flowable<T> subscribe(Class<T> type){
        return getObservable(type)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
