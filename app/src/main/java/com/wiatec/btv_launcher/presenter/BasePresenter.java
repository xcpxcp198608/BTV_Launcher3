package com.wiatec.btv_launcher.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by PX on 2016-11-14.
 */

public abstract class BasePresenter<V> {
    private WeakReference<V> weakReference;

    public void attachView (V view){
        weakReference = new WeakReference<V>(view);
    }

    public void detachView (){
        if(weakReference !=null){
            weakReference.clear();
            weakReference =null;
        }
    }

    protected  V getView (){
        return weakReference.get();
    }
}
