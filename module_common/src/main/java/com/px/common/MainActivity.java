package com.px.common;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.px.common.constant.Constant;
import com.px.common.databinding.CActivityMainBinding;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.http.listener.UploadListener;
import com.px.common.http.pojo.ResultInfo;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.px.common.utils.RxBus;

import java.io.File;
import java.io.IOException;

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
        binding.setOnEvent(new OnEventListener());
        disposable= RxBus.getDefault().subscribe(TestEvent.class).subscribe(new Consumer<TestEvent>() {
            @Override
            public void accept(TestEvent testEvent) throws Exception {
                binding.tvTest.setText(testEvent.getContent());
            }
        });
    }

    public class OnEventListener{
        public void onClick(View view) {
//            switch (view.getId()){
//                case R.id.btStart:
//                    testHttpMaster();
//                    break;
//                case R.id.btUpload:
//                    verifyStoragePermissions(MainActivity.this);
//                    break;
//                case R.id.btReport:
//                    testLogCrash();
//                    break;
//            }
        }
    }

    private void testLogCrash() {
        HttpMaster.post(Constant.url.log_crash)
                .param("model", "sfsdf")
                .param("fwVersion", "sfsdf")
                .param("mac", "sfsdf")
                .param("packageName", "sfsdf")
                .param("versionName", "sfsdf")
                .param("versionCode", "11")
                .param("crashTime", "sfsdf")
                .param("content", "sfsdf111")
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws Exception {
                        Logger.d(s);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return ;
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }else{
            Logger.d("have permission");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean writeAccepted = false;
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length == 1){
                    writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                }
                break;
            default:
                break;
        }
        if (writeAccepted){
            Logger.d("access");
        }
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
