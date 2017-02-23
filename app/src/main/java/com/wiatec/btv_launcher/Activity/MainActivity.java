package com.wiatec.btv_launcher.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.OnLanguageChangeListener;
import com.wiatec.btv_launcher.OnNetworkStatusListener;
import com.wiatec.btv_launcher.OnWifiStatusListener;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.MessageDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.FileCheck;
import com.wiatec.btv_launcher.Utils.FileDownload.DownloadManager;
import com.wiatec.btv_launcher.Utils.FileDownload.OnDownloadListener;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.adapter.FragmentAdapter;
import com.wiatec.btv_launcher.bean.Message1Info;
import com.wiatec.btv_launcher.bean.MessageInfo;
import com.wiatec.btv_launcher.bean.UpdateInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.bean.WeatherInfo;
import com.wiatec.btv_launcher.fragment.Fragment1;
import com.wiatec.btv_launcher.fragment.Fragment4;
import com.wiatec.btv_launcher.presenter.MainPresenter;
import com.wiatec.btv_launcher.receiver.LanguageChangeReceiver;
import com.wiatec.btv_launcher.receiver.NetworkStatusReceiver;
import com.wiatec.btv_launcher.receiver.WeatherStatusReceiver;
import com.wiatec.btv_launcher.receiver.WifiStatusReceiver;
import com.wiatec.btv_launcher.service.DownloadService;
import com.wiatec.btv_launcher.service.LoadCloudService;
import com.wiatec.btv_launcher.service.LoadService;
import com.wiatec.btv_launcher.service_task.WeatherIconSetting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity<IMainActivity, MainPresenter> implements IMainActivity, OnNetworkStatusListener, OnWifiStatusListener, OnLanguageChangeListener {

    @BindView(R.id.tv_time)
    TextView tv_Time;
    @BindView(R.id.tv_temperature)
    TextView tv_Temperature;
    @BindView(R.id.iv_net)
    ImageView iv_Net;
    @BindView(R.id.tv_date)
    TextView tv_Date;
    @BindView(R.id.tv_message)
    TextView tv_Message;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.ibt_weather)
    ImageButton ibt_Weather;
    @BindView(R.id.iv_message)
    ImageView iv_Message;
    @BindView(R.id.fl_message)
    FrameLayout fl_Message;
    @BindView(R.id.tv_message_count)
    TextView tv_MessageCount;


    private Fragment1 fragment1;
    private Fragment4 fragment4;
    private List<Fragment> list;

    private NetworkStatusReceiver networkStatusReceiver;
    private WifiStatusReceiver wifiStatusReceiver;
    private WeatherStatusReceiver weatherStatusReceiver;
    private LanguageChangeReceiver languageChangeReceiver;
    private boolean isVideoDownloading = false;
    private MessageDao messageDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);

        if (fragment1 == null) {
            fragment1 = new Fragment1();
        }
        if (fragment4 == null) {
            fragment4 = new Fragment4();
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(fragment1);
        list.add(fragment4);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), list));
        showTimeAndData();
        networkStatusReceiver = new NetworkStatusReceiver(iv_Net);
        networkStatusReceiver.setOnNetworkStatusListener(this);
        registerReceiver(networkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        wifiStatusReceiver = new WifiStatusReceiver();
        wifiStatusReceiver.setOnWifiStatusListener(this);
        registerReceiver(wifiStatusReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        weatherStatusReceiver = new WeatherStatusReceiver(ibt_Weather);
        registerReceiver(weatherStatusReceiver, new IntentFilter("action.Weather.Change"));
        languageChangeReceiver = new LanguageChangeReceiver();
        registerReceiver(languageChangeReceiver, new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED));
        languageChangeReceiver.setOnLanguageChangeListener(this);

        Intent intent = new Intent(MainActivity.this, LoadService.class);
        intent.setAction("loadInstalledApp");
        startService(intent);

        messageDao = MessageDao.getInstance(MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("language", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String language = SystemConfig.getLanguage(this);
        editor.putString("language", language);
        editor.commit();
        presenter.loadWeatherIcon();
        if (SystemConfig.isNetworkConnected(MainActivity.this)) {
            presenter.loadUpdate();
            Intent intent = new Intent(MainActivity.this, LoadService.class);
            intent.setAction("loadMessage");
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkStatusReceiver);
        unregisterReceiver(wifiStatusReceiver);
        unregisterReceiver(weatherStatusReceiver);
        unregisterReceiver(languageChangeReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_Time.setText((String) msg.obj);
                    break;
                case 2:
                    tv_Date.setText((String) msg.obj);
                    break;
            }
        }
    };

    private void showTimeAndData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        Date d = new Date(System.currentTimeMillis());
                        String time = new SimpleDateFormat("h:mm a").format(d);
                        String date = new SimpleDateFormat("MM-dd-yyyy").format(d);
                        handler.obtainMessage(1, time).sendToTarget();
                        handler.obtainMessage(2, date).sendToTarget();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onConnected(boolean isConnected) {
        if (isConnected) {
            showNetworkStatus();
            presenter.loadVideo();
            presenter.loadUpdate();
            presenter.loadMessage1();

            //启动服务加载天气信息，并通过alarm定时每120分钟加载一次
            Intent alarmIntent = new Intent(MainActivity.this, LoadService.class);
            alarmIntent.setAction("loadWeather");
            PendingIntent alarmPendingIntent = PendingIntent.getService(MainActivity.this, 0, alarmIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            long startTime = SystemClock.elapsedRealtime();
            long repeatTime = 120 * 60 * 1000;
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, repeatTime, alarmPendingIntent);

            //启动服务加载cloud照片信息列表,定时每2分钟读取一次
            Intent cloudIntent = new Intent(MainActivity.this , LoadCloudService.class);
            PendingIntent cloudPendingIntent = PendingIntent.getService(MainActivity.this, 0 ,cloudIntent , 0);
            AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            long repeatTime1 = 3*60*1000;
            alarmManager1.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP ,startTime , repeatTime1 ,cloudPendingIntent);
        }
    }

    @Override
    public void onDisconnect(boolean disConnected) {
        showNetworkStatus();
    }

    @Override
    public void onWifiLevelChange(int level) {
        switch (level) {
            case 4:
                iv_Net.setImageResource(R.drawable.wifi4);
                break;
            case 3:
                iv_Net.setImageResource(R.drawable.wifi3);
                break;
            case 2:
                iv_Net.setImageResource(R.drawable.wifi2);
                break;
            case 1:
                iv_Net.setImageResource(R.drawable.wifi1);
                break;
            case 0:
                iv_Net.setImageResource(R.drawable.wifi0);
                break;
        }
    }

    @Override
    public void onChange(String language) {
        if (SystemConfig.isNetworkConnected(MainActivity.this)) {
            presenter.loadMessage1();
        }
    }

    private void showNetworkStatus() {
        int i = SystemConfig.networkConnectType(MainActivity.this);
        switch (i) {
            case 0:
                iv_Net.setImageResource(R.drawable.disconnect);
                break;
            case 1:
                iv_Net.setImageResource(R.drawable.wifi4);
                break;
            case 3:
                iv_Net.setImageResource(R.drawable.ethernet);
                break;
        }
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    public void loadUpdate(UpdateInfo updateInfo) {
        //Logger.d(updateInfo.toString());
        String info = updateInfo.getInfo();
        String info2 = info.replaceAll("\\n" ,"\n");
        Logger.d(info2);
        int localVersion = ApkCheck.getInstalledApkVersionCode(MainActivity.this, getPackageName());
        if (localVersion < updateInfo.getCode()) {
            showUpdateDialog(updateInfo);
        }
    }

    @Override
    public void loadVideo(List<VideoInfo> list) {
//        Logger.d(videoInfo.toString());
//        if (videoInfo == null) {
//            return;
//        }
//        if (!FileCheck.isFileExists(F.path.download, "btvi3.mp4") && !isVideoDownloading) {
//            //Logger.d("video is not exists");
//            downloadVideo(videoInfo);
//        } else if (!FileCheck.isFileIntact(F.path.download, "btvi3.mp4", videoInfo.getMd5()) && !isVideoDownloading) {
//            //Logger.d("video is not intact");
//            downloadVideo(videoInfo);
//        } else {
//            // Logger.d("video no need update");
//        }
    }

    @Override
    public void loadAdVideo(VideoInfo videoInfo) {
        // Logger.d(videoInfo.toString());
        if (!FileCheck.isFileExists(F.path.download, "btvad.mp4")) {
           // Logger.d("video is not exists");
            Intent intent = new Intent(MainActivity.this, DownloadService.class);
            intent.putExtra("url", videoInfo.getUrl());
            startService(intent);
        } else if (!FileCheck.isFileIntact(F.path.download, "btvad.mp4", videoInfo.getMd5())) {
            Intent intent = new Intent(MainActivity.this, DownloadService.class);
            intent.putExtra("url", videoInfo.getUrl());
            startService(intent);
        } else {
            //Logger.d("video no need update");
        }
    }

    @Override
    public void loadMessage1(final List<Message1Info> list) {
//        Logger.d(list.toString());
        Observable.interval(6, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .take(list.size())
                .repeat()
                .map(new Func1<Long, Message1Info>() {
                    @Override
                    public Message1Info call(Long l) {
                        return list.get(l.intValue());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Message1Info>() {
                    @Override
                    public void call(Message1Info message1Info) {
                        if(message1Info == null){
                            return;
                        }
                        tv_Message.setVisibility(View.VISIBLE);
                        tv_Message.setTextColor(Color.rgb(message1Info.getColorR(), message1Info.getColorG(), message1Info.getColorB()));
                        tv_Message.setText("  " + message1Info.getContent());
                    }
                });
    }

    @Override
    public void loadWeather(WeatherInfo weatherInfo) {
        WeatherIconSetting.setIcon(ibt_Weather, weatherInfo.getIcon());
        tv_Temperature.setText(weatherInfo.getTemperature());
    }

    private void showUpdateDialog(final UpdateInfo updateInfo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if(window == null){
            return;
        }
        window.setContentView(R.layout.dialog_update);
        TextView tvInfo = (TextView) window.findViewById(R.id.tv_info);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        tvInfo.setText(updateInfo.getInfo());
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("updateInfo", updateInfo);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.ibt_weather})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_weather:
                startActivity(new Intent(MainActivity.this, WeatherActivity.class));
                break;
        }
    }
}
