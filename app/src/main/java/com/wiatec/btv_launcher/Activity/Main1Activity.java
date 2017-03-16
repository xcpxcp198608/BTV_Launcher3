package com.wiatec.btv_launcher.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.bean.DeviceInfo;
import com.wiatec.btv_launcher.receiver.OnNetworkStatusListener;
import com.wiatec.btv_launcher.receiver.OnWifiStatusListener;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.FileCheck;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.WifiStatusIconSetting;
import com.wiatec.btv_launcher.adapter.FragmentAdapter;
import com.wiatec.btv_launcher.bean.Message1Info;
import com.wiatec.btv_launcher.bean.UpdateInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.bean.WeatherInfo;
import com.wiatec.btv_launcher.fragment.Fragment1;
import com.wiatec.btv_launcher.fragment.Fragment2;
import com.wiatec.btv_launcher.presenter.MainPresenter;
import com.wiatec.btv_launcher.receiver.NetworkStatusReceiver;
import com.wiatec.btv_launcher.receiver.ScreenWeekUpReceiver;
import com.wiatec.btv_launcher.receiver.WeatherStatusReceiver;
import com.wiatec.btv_launcher.receiver.WifiStatusReceiver;
import com.wiatec.btv_launcher.WeatherIconSetting;
import com.wiatec.btv_launcher.service.LoadCloudService;
import com.wiatec.btv_launcher.service.LoadWeatherService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Main1Activity extends Base1Activity<IMainActivity, MainPresenter> implements IMainActivity, OnNetworkStatusListener, OnWifiStatusListener {

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
    @BindView(R.id.iv_weather)
    ImageView iv_Weather;
    @BindView(R.id.iv_message)
    ImageView iv_Message;
    @BindView(R.id.fl_message)
    FrameLayout fl_Message;
    @BindView(R.id.tv_message_count)
    TextView tv_MessageCount;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private List<Fragment> list;
    private NetworkStatusReceiver networkStatusReceiver;
    private WifiStatusReceiver wifiStatusReceiver;
    private WeatherStatusReceiver weatherStatusReceiver;
    private ScreenWeekUpReceiver screenWeekUpReceiver;
    private boolean isStartLoadNetData = false;
    private boolean isStartAlarmService =false;
    private boolean isStartLoadRss = false;
    private Subscription rssSubscription;
    public DeviceInfo mDeviceInfo;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        initFragment();
        checkDevice();
        showTimeAndData();
        registerBroadcastReceiver();
        if(presenter != null){
            presenter.loadInstalledApp();
        }
        if(SystemConfig.isNetworkConnected(Main1Activity.this) && !isStartAlarmService){
            startAlarmService();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDeviceInfo = deviceInfo;
        if(presenter != null){
            presenter.loadWeatherInfo();
            if (SystemConfig.isNetworkConnected(Main1Activity.this)) {
                isStartLoadNetData = true;
                presenter.loadLocation();
                presenter.loadUpdate();
                presenter.loadMessage();
                presenter.loadVideo();
                presenter.loadMessage1();
                presenter.loadKodiData();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStartLoadRss = false;
        if(rssSubscription != null){
            rssSubscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
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

    @Override
    public void onConnected(boolean isConnected) {
        if (isConnected) {
            if(!isStartAlarmService) {
                startAlarmService();
            }
            if(isStartLoadNetData){
                return;
            }
            presenter.loadLocation();
            presenter.loadVideo();
            presenter.loadUpdate();
            presenter.loadMessage1();
            presenter.loadMessage();
            presenter.loadKodiData();
        }
    }

    @Override
    public void onDisconnect(boolean disConnected) {
    }

    @Override
    public void onWifiLevelChange(int level) {
        WifiStatusIconSetting.setIcon(iv_Net , level);
    }

    @Override
    public void loadUpdate(UpdateInfo updateInfo) {
        String info = updateInfo.getInfo();
        String info2 = info.replaceAll("\\n" ,"\n");
        int localVersion = ApkCheck.getInstalledApkVersionCode(Main1Activity.this, getPackageName());
        if (localVersion < updateInfo.getCode()) {
            showUpdateDialog(updateInfo);
        }
    }

    @Override
    public void loadBootAdVideo(VideoInfo videoInfo) {
        if (videoInfo == null) {
            return;
        }
        if (!FileCheck.isFileExists(F.path.download, "btvbootad.mp4")) {
            //Logger.d("video is not exists");
            presenter.downloadAdVideo("btvbootad.mp4" ,videoInfo.getUrl());
        } else if (!FileCheck.isFileIntact(F.path.download, "btvbootad.mp4", videoInfo.getMd5())) {
            //Logger.d("video is not intact");
            presenter.downloadAdVideo("btvbootad.mp4" ,videoInfo.getUrl());
        } else {
            // Logger.d("video no need update");
        }
    }

    @Override
    public void loadAdVideo(VideoInfo videoInfo) {
        if (!FileCheck.isFileExists(F.path.download, "btvad.mp4")) {
            presenter.downloadAdVideo("btvad.mp4" ,videoInfo.getUrl());
        } else if (!FileCheck.isFileIntact(F.path.download, "btvad.mp4", videoInfo.getMd5())) {
            presenter.downloadAdVideo("btvad.mp4" ,videoInfo.getUrl());
        } else {
            //Logger.d("video no need update");
        }
    }

    @Override
    public void loadMessage1(final List<Message1Info> list) {
        if(isStartLoadRss){

        }else {
            isStartLoadRss = true;
            rssSubscription = Observable.interval(6, TimeUnit.SECONDS)
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
                            if (message1Info == null) {
                                return;
                            }
                            tv_Message.setVisibility(View.VISIBLE);
                            //tv_Message.setTextColor(Color.rgb(message1Info.getColorR(), message1Info.getColorG(), message1Info.getColorB()));
                            tv_Message.setText("  " + message1Info.getContent());
                        }
                    });
        }
    }

    @Override
    public void loadWeatherInfo(WeatherInfo weatherInfo) {
        WeatherIconSetting.setIcon(iv_Weather, weatherInfo.getIcon());
        tv_Temperature.setText(weatherInfo.getTemperature());
    }

    private void showUpdateDialog(final UpdateInfo updateInfo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(Main1Activity.this,R.style.dialog).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if(window == null){
            return;
        }
        window.setContentView(R.layout.dialog_update);
        TextView tvInfo = (TextView) window.findViewById(R.id.tv_info);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        tvInfo.setText(Html.fromHtml(updateInfo.getInfo()));
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main1Activity.this, UpdateActivity.class);
                intent.putExtra("updateInfo", updateInfo);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
    }

    private void checkDevice(){
        String device = Build.MODEL;
        if(!"BTVi3".equals(device) && !"MorphoBT E110".equals(device)&& !"BTV3".equals(device)){
            showWarningDialog();
            return ;
        }
    }

    private void showWarningDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(Main1Activity.this);
        dialog.setTitle(getString(R.string.warning));
        dialog.setMessage(getString(R.string.no_support));
        dialog.setCancelable(false);
        dialog.setNegativeButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    private void initFragment(){
        if (fragment1 == null) {
            fragment1 = new Fragment1();
        }
        if (fragment2 == null) {
            fragment2 = new Fragment2();
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(fragment1);
        list.add(fragment2);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), list));
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
        Application.getThreadPool().execute(new Runnable() {
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
        });
    }

    private void registerBroadcastReceiver() {
        networkStatusReceiver = new NetworkStatusReceiver(iv_Net);
        networkStatusReceiver.setOnNetworkStatusListener(this);
        registerReceiver(networkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        wifiStatusReceiver = new WifiStatusReceiver();
        wifiStatusReceiver.setOnWifiStatusListener(this);
        registerReceiver(wifiStatusReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));

        weatherStatusReceiver = new WeatherStatusReceiver(iv_Weather);
        registerReceiver(weatherStatusReceiver, new IntentFilter("action.Weather.Change"));

        screenWeekUpReceiver = new ScreenWeekUpReceiver();
        registerReceiver(screenWeekUpReceiver , new IntentFilter(Intent.ACTION_SCREEN_ON));
    }

    private void unregister() {
        unregisterReceiver(networkStatusReceiver);
        unregisterReceiver(wifiStatusReceiver);
        unregisterReceiver(weatherStatusReceiver);
        unregisterReceiver(screenWeekUpReceiver);
    }

    private void startAlarmService() {
        isStartAlarmService = true;
        //启动服务加载天气信息，并通过alarm定时每120分钟加载一次
        Intent alarmIntent = new Intent(Main1Activity.this, LoadWeatherService.class);
        alarmIntent.setAction("loadWeather");
        PendingIntent alarmPendingIntent = PendingIntent.getService(Main1Activity.this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long startTime = SystemClock.elapsedRealtime();
        long repeatTime = 120 * 60 * 1000;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, repeatTime, alarmPendingIntent);

        //启动服务加载cloud照片信息列表,定时每3分钟读取一次
        Intent cloudIntent = new Intent(Main1Activity.this, LoadCloudService.class);
        PendingIntent cloudPendingIntent = PendingIntent.getService(Main1Activity.this, 0 ,cloudIntent , 0);
        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long repeatTime1 = 5*60*1000;
        alarmManager1.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP ,startTime , repeatTime1 ,cloudPendingIntent);
    }
}
