package com.wiatec.btv_launcher.Utils.OkHttp.Listener;

import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SPUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by patrick on 2016/12/22.
 */

public abstract class StringListener implements Callback {

    public abstract void onSuccess (String  s) throws IOException;
    public abstract void onFailure (String e);

    @Override
    public void onFailure(Call call, IOException e) {
        Observable.just(e.getMessage())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        onFailure(s);
                    }
                });

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        Headers headers = response.headers();
        List<String> cookies = headers.values("Set-Cookie");
        if(cookies != null && cookies.size() > 0 ) {
            String session = cookies.get(0);
            String cookie = session.substring(0, session.indexOf(";"));
            SPUtils.put("cookie", cookie);
            Logger.d(cookie);
        }
        Observable.just(response)
                .map(new Func1<Response, String>() {
                    @Override
                    public String call(Response response) {
                        try {
                            return response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onFailure(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            if(s!=null) {
                                onSuccess(s);
                            }else{
                                onFailure("request result is empty");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
