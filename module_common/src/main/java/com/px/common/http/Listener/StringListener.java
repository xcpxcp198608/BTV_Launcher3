package com.px.common.http.listener;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.px.common.utils.EmojiToast;

import java.io.IOException;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public abstract class StringListener implements Callback {

    public abstract void onSuccess(String s) throws Exception;
    public abstract void onFailure (String e);

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        handFailure(e.getMessage());
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        try {
            if (response.code() == 400) {
                handFailure("request error");
                return;
            }
            if (response.code() == 404) {
                handFailure("resource no found");
                return;
            }
            if (response.code() == 408) {
                handFailure("request timeout");
                return;
            }
            if (response.code() == 500) {
                handFailure("server exception");
                return;
            }
            Observable.just(response)
                    .map(new Function<Response, String>() {
                        @Override
                        public String apply(Response response) throws Exception {
                            return response.body().string();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<String>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(String value) {
                            try {
                                if (value != null) {
                                    onSuccess(value);
                                } else {
                                    handFailure("response data is empty");
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
        }catch (Exception e){
            handFailure(e.getMessage());
        }
    }

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
}
