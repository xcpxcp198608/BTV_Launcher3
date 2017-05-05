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
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
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
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.DeviceInfo;
import com.wiatec.btv_launcher.custom_view.RollTextView;
import com.wiatec.btv_launcher.receiver.OnNetworkStatusListener;
import com.wiatec.btv_launcher.receiver.OnWifiStatusListener;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.FileCheck;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.WifiStatusIconSetting;
import com.wiatec.btv_launcher.bean.Message1Info;
import com.wiatec.btv_launcher.bean.UpdateInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.bean.WeatherInfo;
import com.wiatec.btv_launcher.fragment.Fragment1;
import com.wiatec.btv_launcher.presenter.MainPresenter;
import com.wiatec.btv_launcher.receiver.NetworkStatusReceiver;
import com.wiatec.btv_launcher.receiver.ScreenWeekUpReceiver;
import com.wiatec.btv_launcher.receiver.WeatherStatusReceiver;
import com.wiatec.btv_launcher.receiver.WifiStatusReceiver;
import com.wiatec.btv_launcher.WeatherIconSetting;
import com.wiatec.btv_launcher.service.LoadCloudService;
import com.wiatec.btv_launcher.service.LoadWeatherService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Base1Activity<IMainActivity, MainPresenter> implements IMainActivity, OnNetworkStatusListener, OnWifiStatusListener {

    @BindView(R.id.tv_time)
    TextView tv_Time;
    @BindView(R.id.tv_temperature)
    TextView tv_Temperature;
    @BindView(R.id.iv_net)
    ImageView iv_Net;
    @BindView(R.id.tv_date)
    TextView tv_Date;
    @BindView(R.id.tv_message)
    RollTextView tv_Message;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.iv_weather)
    ImageView iv_Weather;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_welcome)
    TextView tvWelcome;

    private Fragment1 fragment1;
    private NetworkStatusReceiver networkStatusReceiver;
    private WifiStatusReceiver wifiStatusReceiver;
    private WeatherStatusReceiver weatherStatusReceiver;
    private ScreenWeekUpReceiver screenWeekUpReceiver;
    private boolean isStartLoadNetData = false;
    private boolean isStartAlarmService =false;
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
        showVersion();
        initFragment();
        checkDevice();
        showTimeAndData();
        registerBroadcastReceiver();
        if(presenter != null){
            presenter.loadInstalledApp();
        }
        if(SystemConfig.isNetworkConnected(MainActivity.this) && !isStartAlarmService){
            startAlarmService();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String userName = (String) SPUtils.get(MainActivity.this , "userName" , "");
        if(!TextUtils.isEmpty(userName)){
            tvWelcome.setText(getString(R.string.welcome) + " " + userName);
        }
        mDeviceInfo = deviceInfo;
        if(presenter != null){
            presenter.loadWeatherInfo();
            if (SystemConfig.isNetworkConnected(MainActivity.this)) {
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
        if(tv_Message != null){
            tv_Message.stop();
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
        if(updateInfo ==null){
            return;
        }
        String info = updateInfo.getInfo();
        String info2 = info.replaceAll("\\n" ,"\n");
        int localVersion = ApkCheck.getInstalledApkVersionCode(MainActivity.this, getPackageName());
        if (localVersion < updateInfo.getCode()) {
            showUpdateDialog(updateInfo);
        }
    }

    @Override
    public void loadBootAdVideo(VideoInfo videoInfo) {
        if (videoInfo == null) {
            return;
        }
        SPUtils.put(MainActivity.this , "bootAdVideoTime" ,videoInfo.getPlayInterval());
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
        if(videoInfo == null){
            return;
        }
        SPUtils.put(MainActivity.this , "adVideoTime" ,videoInfo.getPlayInterval());
        if (!FileCheck.isFileExists(F.path.download, "btvad.mp4")) {
            presenter.downloadAdVideo("btvad.mp4" ,videoInfo.getUrl());
//            Logger.d("video not exists start download");
        } else if (!FileCheck.isFileIntact(F.path.download, "btvad.mp4", videoInfo.getMd5())) {
//            Logger.d("video not intact start download");
            presenter.downloadAdVideo("btvad.mp4" ,videoInfo.getUrl());
        } else {
            //Logger.d("video no need update");
        }
    }

    @Override
    public void loadMessage1(final List<Message1Info> list) {
    }

    @Override
    public void loadWeatherInfo(WeatherInfo weatherInfo) {
        if(weatherInfo ==null){
            return;
        }
        WeatherIconSetting.setIcon(iv_Weather, weatherInfo.getIcon());
        tv_Temperature.setText(weatherInfo.getTemperature());
    }

    private void showUpdateDialog(final UpdateInfo updateInfo) {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,R.style.dialog).create();
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
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout , fragment1);
        fragmentTransaction.commit();
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
        Intent alarmIntent = new Intent(MainActivity.this, LoadWeatherService.class);
        alarmIntent.setAction("loadWeather");
        PendingIntent alarmPendingIntent = PendingIntent.getService(MainActivity.this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long startTime = SystemClock.elapsedRealtime();
        long repeatTime = 120 * 60 * 1000;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, repeatTime, alarmPendingIntent);

        //启动服务加载cloud照片信息列表,定时每3分钟读取一次
        Intent cloudIntent = new Intent(MainActivity.this, LoadCloudService.class);
        PendingIntent cloudPendingIntent = PendingIntent.getService(MainActivity.this, 0 ,cloudIntent , 0);
        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long repeatTime1 = 5*60*1000;
        alarmManager1.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP ,startTime , repeatTime1 ,cloudPendingIntent);
    }

    private void showVersion() {
        String s = ApkCheck.getInstalledApkVersionName(this , getPackageName());
        s = s.substring(1 , s.length());
        tvVersion.setText(getString(R.string.ui)+" "+s);
    }
}
