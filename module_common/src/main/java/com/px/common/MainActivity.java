package com.px.common;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.px.common.databinding.CActivityMainBinding;
import com.px.common.http.listener.DownloadListener;
import com.px.common.http.listener.ListListener;
import com.px.common.http.listener.ObjectListener;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.http.pojo.DownloadInfo;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.px.common.utils.RxBus;

import java.io.IOException;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

//    private static final String URL = "http://www.ldlegacy.com:8080/control_panel/update/get";
//    private static final String URL = "http://panel.ldlegacy.com:8080/panel/user/login";
    private static final String URL = "http://panel.ldlegacy.com:8080/panel/category/";

    private CActivityMainBinding binding;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.c_activity_main);
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
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpMaster.get(URL)
//                        .enqueue(new ListListener<CommissionCategoryInfo>(CommissionCategoryInfo.class) {
//                            @Override
//                            public void onSuccess(List<CommissionCategoryInfo> list) throws IOException {
//                                Logger.d(list.toString());
////                                RxBus.getDefault().post(new TestEvent(s));
//                            }
//
//                            @Override
//                            public void onFailure(String e) {
//                                EmojiToast.showLong(e, EmojiToast.EMOJI_SAD);
//                            }
//                        });
//            }
//        }).start();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpMaster.get(URL)
//                        .enqueue(new ObjectListener<UpdateInfo>(UpdateInfo.class) {
//
//                            @Override
//                            public void onSuccess(UpdateInfo updateInfo) throws IOException {
//                                Logger.d(updateInfo.toString());
//                                HttpMaster.download(MainActivity.this)
//                                        .url(updateInfo.getUrl())
//                                        .startDownload(new DownloadListener() {
//                                            @Override
//                                            public void onPending(DownloadInfo downloadInfo) {
//
//                                            }
//
//                                            @Override
//                                            public void onStart(DownloadInfo downloadInfo) {
//                                                Logger.d(downloadInfo.getPath());
//                                                Logger.d(downloadInfo.getName());
//                                            }
//
//                                            @Override
//                                            public void onPause(DownloadInfo downloadInfo) {
//
//                                            }
//
//                                            @Override
//                                            public void onProgress(DownloadInfo downloadInfo) {
//                                                Logger.d(downloadInfo.getProgress()+"");
//                                            }
//
//                                            @Override
//                                            public void onFinished(DownloadInfo downloadInfo) {
//                                                Logger.d(downloadInfo.getProgress()+"");
//                                                Logger.d(downloadInfo.getPath());
//                                                Logger.d(downloadInfo.getName());
//                                            }
//
//                                            @Override
//                                            public void onCancel(DownloadInfo downloadInfo) {
//
//                                            }
//
//                                            @Override
//                                            public void onError(DownloadInfo downloadInfo) {
//                                                Logger.d(downloadInfo.getMessage());
//                                            }
//                                        });
//                                RxBus.getDefault().post(new TestEvent(updateInfo.toString()));
//                            }
//
//                            @Override
//                            public void onFailure(String e) {
//                                EmojiToast.showLong(e, EmojiToast.EMOJI_SAD);
//                            }
//                        });
//            }
//        }).start();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                HttpMaster.post(URL)
//                        .param("username", "ww")
//                        .param("password", "ww")
//                        .param("mac", "5C:41:E7:00:8F:AC")
//                        .enqueue(new ResultListener<AuthRegisterUserInfo>(AuthRegisterUserInfo.class) {
//                            @Override
//                            public void onSuccess(ResultInfo<AuthRegisterUserInfo> resultInfo) throws IOException {
//                                if(resultInfo.getCode() == 200) {
//                                    AuthRegisterUserInfo authRegisterUserInfo = resultInfo.getData();
//                                    RxBus.getDefault().post(new TestEvent(authRegisterUserInfo.toString()));
//                                }else{
//                                    Logger.d(resultInfo.getMessage());
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(String e) {
//                                EmojiToast.showLong(e, EmojiToast.EMOJI_SAD);
//                            }
//                        });
//            }
//        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpMaster.get(URL)
                        .enqueue(new StringListener() {
                            @Override
                            public void onSuccess(String s) throws IOException {
                                Logger.d(s);
                                RxBus.getDefault().post(new TestEvent(s));
                            }

                            @Override
                            public void onFailure(String e) {
                                EmojiToast.showLong(e, EmojiToast.EMOJI_SAD);
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
