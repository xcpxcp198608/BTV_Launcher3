package com.px.common;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.px.common.databinding.ActivityMainBinding;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.Logger;
import com.px.common.utils.RxBus;

import java.io.IOException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://www.ldlegacy.com:8080/control_panel/update/get";
//    private static final String URL = "http://panel.ldlegacy.com:8080/panel/category/";

    private ActivityMainBinding binding;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        disposable= RxBus.getDefault().subscribe(TestEvent.class).subscribe(new Consumer<TestEvent>() {
            @Override
            public void accept(TestEvent testEvent) throws Exception {
                binding.tvTest.setText(testEvent.getContent());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testHttpMaster();
            }
        });
    }

    private void testHttpMaster(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpMaster.get(URL)
                        .enqueue(new StringListener() {
                            @Override
                            public void onSuccess(String s) throws IOException {
                                RxBus.getDefault().post(new TestEvent(s));
                            }

                            @Override
                            public void onFailure(String e) {
                                Logger.d(e);
                            }
                        });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null){
            disposable.dispose();
        }
    }
}
