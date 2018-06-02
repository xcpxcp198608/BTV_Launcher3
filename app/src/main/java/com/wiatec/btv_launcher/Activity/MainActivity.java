package com.wiatec.btv_launcher.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
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
import android.widget.TextView;

import com.px.common.utils.AppUtil;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.databinding.ActivityMainBinding;
import com.wiatec.btv_launcher.receiver.OnNetworkStatusListener;
import com.wiatec.btv_launcher.receiver.OnWifiStatusListener;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.FileUtil;
import com.wiatec.btv_launcher.config.WifiStatusIconSetting;
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
import com.wiatec.btv_launcher.config.WeatherIconSetting;
import com.wiatec.btv_launcher.service.LoadCloudService;
import com.wiatec.btv_launcher.service.LoadWeatherService;
import com.wiatec.btv_launcher.service_task.CheckLoginMaster;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Base1Activity<IMainActivity, MainPresenter> implements
        IMainActivity, OnNetworkStatusListener, OnWifiStatusListener {

    private static final String RECEIVER_HOME_PAGE = "com.wiatec.ldservice.receiver.HomePageReceiver";
    private static final String RECEIVER_HOME_PAGE_CATEGORY = "HomePageReceiver";

    private ActivityMainBinding binding;
    private Fragment1 fragment1;
    private NetworkStatusReceiver networkStatusReceiver;
    private WifiStatusReceiver wifiStatusReceiver;
    private WeatherStatusReceiver weatherStatusReceiver;
    private ScreenWeekUpReceiver screenWeekUpReceiver;
    private boolean isStartLoadNetData = false;
    private boolean isStartAlarmService =false;
    private CheckLoginMaster master = new CheckLoginMaster();

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showVersion();
        initFragment();
        checkDevice();
        showTimeAndData();
        registerBroadcastReceiver();
        if(presenter != null){
            presenter.loadInstalledApp();
        }
        if(NetUtil.isConnected() && !isStartAlarmService){
            startAlarmService();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkAgreement();
        sendHomePageBroadCast();
        if(presenter != null){
            presenter.loadWeatherInfo();
            if (NetUtil.isConnected()) {
                isStartLoadNetData = true;
                presenter.loadLocation();
                presenter.loadUpdate();
                //check user level
                boolean isRenter = (boolean) SPUtil.get(F.sp.is_renter, false);
                if(isRenter){
                    master.rentValidate();
                }else {
                    master.check();
                }
            }
        }
        try {
            String level = (String) SPUtil.get(F.sp.level, "1");
            int l = Integer.parseInt(level);
            if (l <= 0) {
                showLoginAgainDialog();
            }
        }catch (Exception e){
            Logger.d(e.getLocalizedMessage());
        }
        showLastName();
        boolean isRenter = (boolean) SPUtil.get(F.sp.is_renter, false);
        if(isRenter) {
            String rentalCategory = (String) SPUtil.get(F.sp.rental_category, "");
            if (!TextUtils.isEmpty(rentalCategory)) {
                binding.tvRental.setText(rentalCategory);
            } else {
                binding.tvRental.setText("");
            }
            long leftMillsSeconds = (long) SPUtil.get(F.sp.left_mills_second, 0L);
            if(leftMillsSeconds < 259200000 && leftMillsSeconds > 0){
                showRentRenewNoticeDialog();
            }
        }else{
            binding.tvRental.setText("");
        }
    }

    private void checkAgreement(){
        boolean showAgree = (boolean) SPUtil.get("agree", true);
        boolean showConsent = (boolean) SPUtil.get("consent_agree", true);
        boolean showManualNotice = (boolean) SPUtil.get("manual_notice", true);
        if(showManualNotice) {
            showManualNotice();
        }else if(showAgree){
            showAgreement();
        }else if(showConsent){
            showConsentDialog();
        }
    }

    private void showManualNotice(){
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if(window == null) return;
        window.setContentView(R.layout.dialog_update);
        Button btConfirm = window.findViewById(R.id.bt_confirm);
        TextView tvTitle = window.findViewById(R.id.tvTitle);
        TextView textView = window.findViewById(R.id.tv_info);
        btConfirm.setText(getString(R.string.ok));
        tvTitle.setText(getString(R.string.welcome1));
        textView.setTextSize(16);
        textView.setText(getString(R.string.notice_manual));
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.put("manual_notice", false);
                alertDialog.dismiss();
                boolean showAgree = (boolean) SPUtil.get("agree", true);
                boolean showConsent = (boolean) SPUtil.get("consent_agree", true);
                if(showAgree) {
                    showAgreement();
                }else if(showConsent){
                    showConsentDialog();
                }
            }
        });
    }

    private void showAgreement() {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if(window == null) return;
        window.setContentView(R.layout.dialog_update);
        Button btConfirm = window.findViewById(R.id.bt_confirm);
        TextView tvTitle = window.findViewById(R.id.tvTitle);
        TextView textView = window.findViewById(R.id.tv_info);
        btConfirm.setText(getString(R.string.agree));
        tvTitle.setText(getString(R.string.agreement));
        textView.setTextSize(15);
        textView.setText(getString(R.string.agreement_content));
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.put("agree", false);
                alertDialog.dismiss();
                boolean showConsent = (boolean) SPUtil.get("consent_agree", true);
                if(showConsent){
                    showConsentDialog();
                }

            }
        });
    }

    private void showConsentDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.dialog).create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if(window == null) return;
        window.setContentView(R.layout.dialog_update);
        Button btConfirm = window.findViewById(R.id.bt_confirm);
        TextView tvTitle = window.findViewById(R.id.tvTitle);
        TextView textView = window.findViewById(R.id.tv_info);
        btConfirm.setText(getString(R.string.ok));
        tvTitle.setText(getString(R.string.consent_title));
        textView.setTextSize(16);
        textView.setText(getString(R.string.consent_content));
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.put("consent_agree", false);
                alertDialog.dismiss();
            }
        });
    }

    private void sendHomePageBroadCast(){
        Intent intent = new Intent(RECEIVER_HOME_PAGE);
        intent.addCategory(RECEIVER_HOME_PAGE_CATEGORY);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent, RECEIVER_HOME_PAGE);
    }

    private void showLastName() {
        String lastName = (String) SPUtil.get(F.sp.last_name , "");
        if(!TextUtils.isEmpty(lastName) && !"null".equals(lastName)){
            binding.tvWelcome.setText(getString(R.string.welcome) + " " + lastName + " " + getString(R.string.family));
        }else{
            binding.tvWelcome.setText("");
        }
    }

    private void showRentRenewNoticeDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this , R.style.dialog).create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        if(window == null){
            return;
        }
        window.setContentView(R.layout.dialog_login_repeat);
        TextView txMessage = window.findViewById(R.id.tx_message);
        Button btnConfirm = window.findViewById(R.id.bt_confirm);
        txMessage.setText(getString(R.string.rent_out_expires));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || super.onKeyDown(keyCode, event);
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
            presenter.loadUpdate();
        }
    }

    @Override
    public void onDisconnect(boolean disConnected) {
    }

    @Override
    public void onWifiLevelChange(int level) {
        WifiStatusIconSetting.setIcon(binding.ivNet , level);
    }

    @Override
    public void loadUpdate(UpdateInfo updateInfo) {
        if(presenter != null) {
            presenter.loadAdVideo();
            presenter.loadMessage1();
            presenter.loadMessage();
            presenter.loadKodiData();
        }
        if(updateInfo == null){
            return;
        }
//        Logger.d(updateInfo.toString());
        int localVersion = AppUtil.getVersionCode(getPackageName());
        if (localVersion < updateInfo.getCode()) {
            showUpdateDialog(updateInfo);
        }
    }

    @Override
    public void loadBootAdVideo(VideoInfo videoInfo) {
        if (videoInfo == null) {
            return;
        }
        SPUtil.put("bootAdVideoTime" ,videoInfo.getPlayInterval());
        if (!FileUtil.isExists(F.path.download, "btvbootad.mp4")) {
            //Logger.d("video is not exists");
            presenter.downloadAdVideo("btvbootad.mp4" ,videoInfo.getUrl());
        } else if (!FileUtil.isIntact(F.path.download, "btvbootad.mp4", videoInfo.getMd5())) {
            //Logger.d("video is not intact");
            presenter.downloadAdVideo("btvbootad.mp4" ,videoInfo.getUrl());
        }
    }

    @Override
    public void loadAdVideo(VideoInfo videoInfo) {
        if(videoInfo == null){
            return;
        }
        SPUtil.put(F.sp.ad_time ,videoInfo.getPlayInterval()+"");
        if (!FileUtil.isExists(F.path.download, "btvad.mp4")) {
            presenter.downloadAdVideo("btvad.mp4" ,videoInfo.getUrl());
//            Logger.d("video not exists start download");
        } else if (!FileUtil.isIntact(F.path.download, "btvad.mp4", videoInfo.getMd5())) {
//            Logger.d("video not intact start download");
            presenter.downloadAdVideo("btvad.mp4" ,videoInfo.getUrl());
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
        WeatherIconSetting.setIcon(binding.ivWeather, weatherInfo.getIcon());
        binding.tvTemperature.setText(weatherInfo.getTemperature());
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
        TextView tvInfo = window.findViewById(R.id.tv_info);
        Button btConfirm = window.findViewById(R.id.bt_confirm);
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
        if(!device.contains("BTV")){
            showWarningDialog();
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
                    binding.tvTime.setText((String) msg.obj);
                    break;
                case 2:
                    binding.tvDate.setText((String) msg.obj);
                    break;
                case 3:
                    boolean isRenter = (boolean) SPUtil.get(F.sp.is_renter, false);
                    if(isRenter){
                        binding.tvLeftTime.setText((String) msg.obj);
                    }else{
                        binding.tvLeftTime.setText("");
                    }
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
                        String time = new SimpleDateFormat("h:mm a", Locale.US).format(d);
                        String date = new SimpleDateFormat("MM-dd-yyyy", Locale.US).format(d);
                        String leftTime = (String) SPUtil.get(F.sp.left_time, "");
                        handler.obtainMessage(1, time).sendToTarget();
                        handler.obtainMessage(2, date).sendToTarget();
                        handler.obtainMessage(3, leftTime).sendToTarget();
                    }
                } catch (Exception e) {
                    Logger.e(e.getMessage());
                }
            }
        });
    }

    private void registerBroadcastReceiver() {
        networkStatusReceiver = new NetworkStatusReceiver(binding.ivNet);
        networkStatusReceiver.setOnNetworkStatusListener(this);
        registerReceiver(networkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        wifiStatusReceiver = new WifiStatusReceiver();
        wifiStatusReceiver.setOnWifiStatusListener(this);
        registerReceiver(wifiStatusReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));

        weatherStatusReceiver = new WeatherStatusReceiver(binding.ivWeather);
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
        if(alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, repeatTime, alarmPendingIntent);
        }

        //启动服务加载cloud照片信息列表,定时每3分钟读取一次
//        Intent cloudIntent = new Intent(MainActivity.this, LoadCloudService.class);
//        PendingIntent cloudPendingIntent = PendingIntent.getService(MainActivity.this, 0 ,cloudIntent , 0);
//        AlarmManager alarmManager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        long repeatTime1 = 5*60*1000;
//        if(alarmManager1 != null) {
//            alarmManager1.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, repeatTime1, cloudPendingIntent);
//        }
    }

    private void showVersion() {
        String s = AppUtil.getVersionName(getPackageName());
        s = s.substring(1 , s.length());
        binding.tvVersion.setText(getString(R.string.ui) + " " + s);
    }
}
