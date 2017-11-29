package com.wiatec.btv_launcher.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.Activity.AppSelectActivity;
import com.wiatec.btv_launcher.Activity.FMPlayActivity;
import com.wiatec.btv_launcher.Activity.InstituteActivity;
import com.wiatec.btv_launcher.Activity.LoginActivity;
import com.wiatec.btv_launcher.Activity.MainActivity;
import com.wiatec.btv_launcher.Activity.MenuActivity;
import com.wiatec.btv_launcher.Activity.Message1Activity;
import com.wiatec.btv_launcher.Activity.Opportunity1Activity;
import com.wiatec.btv_launcher.Activity.Splash1Activity;
import com.wiatec.btv_launcher.Activity.UserManual1Activity;
import com.wiatec.btv_launcher.Activity.WebViewActivity;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.Utils.ApkInstall;
import com.wiatec.btv_launcher.Utils.FileCheck;
import com.wiatec.btv_launcher.Utils.OkHttp.Bean.DownloadInfo;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.DownloadListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.adapter.AutoScrollAdapter;
import com.wiatec.btv_launcher.adapter.PushMessageAdapter;
import com.wiatec.btv_launcher.adapter.TranslationAdapter;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.bean.LiveChannelInfo;
import com.wiatec.btv_launcher.bean.PushMessageInfo;
import com.wiatec.btv_launcher.bean.RollImageInfo;
import com.wiatec.btv_launcher.bean.UserDataInfo;
import com.wiatec.btv_launcher.custom_view.AutoScrollRecycleView;
import com.wiatec.btv_launcher.custom_view.MessageListView;
import com.wiatec.btv_launcher.custom_view.MultiImage;
import com.wiatec.btv_launcher.custom_view.TranslationImageView;
import com.wiatec.btv_launcher.receiver.OnNetworkStatusListener;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.MessageDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.MessageInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.presenter.Fragment1Presenter;
import com.wiatec.btv_launcher.receiver.NetworkStatusReceiver;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-12.
 */

public class Fragment1 extends BaseFragment<IFragment1, Fragment1Presenter> implements IFragment1, OnNetworkStatusListener, View.OnFocusChangeListener {
    @BindView(R.id.ibt_eufonico)
    ImageButton ibtEufonico;
    @BindView(R.id.ibt_user_manual)
    ImageButton ibtUserManual;
    @BindView(R.id.ibt_setting)
    ImageButton ibtSetting;
    @BindView(R.id.ibt_apps)
    ImageButton ibtApps;
    @BindView(R.id.ibt_eufonic_bvision)
    ImageButton ibtEufonicBvision;
    @BindView(R.id.ibt_market)
    ImageButton ibtMarket;
    @BindView(R.id.ibt_game)
    ImageButton ibtGame;
    @BindView(R.id.ibt_opportunity)
    ImageButton ibtOpportunity;
    @BindView(R.id.ibt_1)
    ImageButton ibt1;
    @BindView(R.id.ibt_2)
    ImageButton ibt2;
    @BindView(R.id.ibt_3)
    ImageButton ibt3;
    @BindView(R.id.ibt_4)
    ImageButton ibt4;
    @BindView(R.id.ibt_5)
    ImageButton ibt5;
    @BindView(R.id.ibt_6)
    ImageButton ibt6;
    @BindView(R.id.ibt_7)
    ImageButton ibt7;
    @BindView(R.id.ibt_8)
    ImageButton ibt8;
    @BindView(R.id.vv_main)
    VideoView vv_Main;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    @BindView(R.id.tiv_banner)
    MultiImage tivBanner;
    @BindView(R.id.tiv_eufonic)
    MultiImage tivEufonic;
    @BindView(R.id.iv_btv_logo)
    ImageView ivBTVLogo;

    private NetworkStatusReceiver networkStatusReceiver;
    private Subscription videoSubscription;
    private VideoInfo currentVideoInfo;
    private boolean isVideoPlaying = false;
    private long entryTime;
    private long exitTime;
    private long holdTime;
    private UserDataInfo userDataInfo;
    private InstalledAppDao installedAppDao;
    private Intent intent;

    private boolean isNetworkReceiveRegister = false;
    private boolean isUserVisible = false;

    private MainActivity activity;

    @Override
    protected Fragment1Presenter createPresenter() {
        return new Fragment1Presenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_1, container, false);
        ButterKnife.bind(this, view);
        activity = (MainActivity) getContext();
        userDataInfo = new UserDataInfo();
        installedAppDao = InstalledAppDao.getInstance(Application.getContext());
        intent = new Intent();
        if (!SystemConfig.isNetworkConnected(getContext())){
            networkStatusReceiver = new NetworkStatusReceiver(null);
            networkStatusReceiver.setOnNetworkStatusListener(this);
            getContext().registerReceiver(networkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            isNetworkReceiveRegister = true;
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setFocusTransmit();
        setZoom();
        showCustomShortCut(ibt1 , "ibt1");
        showCustomShortCut(ibt2 , "ibt2");
        showCustomShortCut(ibt3 , "ibt3");
        showCustomShortCut(ibt4 , "ibt4");
        showCustomShortCut(ibt5 , "ibt5");
        showCustomShortCut(ibt6 , "ibt6");
        showCustomShortCut(ibt7 , "ibt7");
        showCustomShortCut(ibt8 , "ibt8");
    }

    private void setFocusTransmit() {
        ibt1.setNextFocusDownId(R.id.fl_video);
        ibt2.setNextFocusDownId(R.id.fl_video);
        ibt3.setNextFocusDownId(R.id.fl_video);
        ibt4.setNextFocusDownId(R.id.fl_video);
        ibt5.setNextFocusDownId(R.id.fl_video);
        ibt6.setNextFocusDownId(R.id.fl_video);
        ibt7.setNextFocusDownId(R.id.fl_video);
        ibt8.setNextFocusDownId(R.id.fl_video);
    }

    @Override
    public void onResume() {
        super.onResume();
        isUserVisible = true;
        entryTime = System.currentTimeMillis();
        startLoadData();
    }

    private void startLoadData(){
        if(!SystemConfig.isNetworkConnected(getContext())){
            return;
        }
        if (presenter != null && vv_Main != null && !vv_Main.isPlaying() && !isVideoPlaying) {
            presenter.loadVideo();
        }
        if (presenter != null) {
            presenter.loadRollImageData();
            presenter.loadRollOverImage();
            presenter.loadCloudData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(userDataInfo != null){
            exitTime = System.currentTimeMillis();
            holdTime = exitTime - entryTime;
            String eTime = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date(exitTime));
            userDataInfo.setExitTime(eTime);
            userDataInfo.setStayTime(holdTime+"");
            userDataInfo.setUserName((String) SPUtils.get(Application.getContext(),"userName" ,""));
            userDataInfo.setMac((String) SPUtils.get(Application.getContext(),"mac" ,""));
            userDataInfo.setCountry((String) SPUtils.get(Application.getContext(),"country" ,""));
            userDataInfo.setCity((String) SPUtils.get(Application.getContext(),"city" ,""));
            if(presenter != null && entryTime>0) {
                presenter.uploadHoldTime(userDataInfo);
            }
        }
        release();
    }

    @Override
    public void onStop() {
        super.onStop();
        release();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(networkStatusReceiver != null && isNetworkReceiveRegister){
            getContext().unregisterReceiver(networkStatusReceiver);
        }
        release();
    }

    private void release(){
        isUserVisible = false;
        isVideoPlaying = false;
        if (videoSubscription != null) {
            videoSubscription.unsubscribe();
        }
        if (vv_Main != null) {
            vv_Main.stopPlayback();
        }
        if(tivBanner != null){
            tivBanner.stop();
        }
    }

    @OnClick({R.id.ibt_eufonico, R.id.ibt_user_manual, R.id.ibt_setting, R.id.ibt_apps, R.id.ibt_market,
            R.id.ibt_game, R.id.fl_video, R.id.ibt_opportunity, R.id.ibt_eufonic_bvision})
    public void onClick(View view) {
        if(getLevel() <= 0 ){
            Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.account_error) ,
                    Toast.LENGTH_LONG).show();
            return;
        }
        switch (view.getId()) {
            case R.id.ibt_eufonico:
                intent.setClass(getActivity(), FMPlayActivity.class);
                intent.putExtra("url", F.url.eufonico);
                getContext().startActivity(intent);
                break;
            case R.id.ibt_user_manual:
                startActivity(new Intent(getContext() , UserManual1Activity.class));
                break;
            case R.id.ibt_setting:
                if (ApkCheck.isApkInstalled(getContext(), F.package_name.setting)) {
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.setting);
                }
                break;
            case R.id.ibt_apps:
                startActivity(new Intent(getContext(), MenuActivity.class));
                break;
            case R.id.ibt_market:
                launchAppByLogin(getContext() , F.package_name.market);
                break;
            case R.id.ibt_opportunity:
                startActivity(new Intent(getContext(), Opportunity1Activity.class));
                break;
            case R.id.ibt_game:
                if (ApkCheck.isApkInstalled(getContext(), F.package_name.happy_chick)) {
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.happy_chick);
                }else{
                    Toast.makeText(getContext() , getString(R.string.download_guide)+" HappyChick",Toast.LENGTH_SHORT).show();
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.market);
                }
                break;
            case R.id.fl_video:
                release();
                launchAppByLogin(getContext() , F.package_name.bplay);
                break;
            case R.id.ibt_eufonic_bvision:
                Intent intent = new Intent("com.wiatec.bplay.view.activity.PlayLiveActivity");
                intent.putExtra("id", 20+"");
                intent.putExtra("userId", 26+"");
                intent.putExtra("title", "LDE");
                intent.putExtra("message", "");
                intent.putExtra("playUrl", "http://ldlive.protv.company:8080/hls/OGUyMDIwMW.m3u8");
                startActivity(intent);
                break;
        }
    }

    private void launchAppByLogin(Context context , String packageName){
        String userName = (String) SPUtils.get(Application.getContext() , "userName" ,"");
        String token = (String) SPUtils.get(Application.getContext() , "token" ,"");
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(token)){
            getContext().startActivity(new Intent(getContext() , LoginActivity.class));
        }else {
            Logger.d(""+packageName);
            presenter.check(packageName , context );
        }
    }

    @Override
    public void showLivePlayDownloadDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.download_add));
        progressDialog.setMessage(getString(R.string.download_wait));
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        OkMaster.download(getContext()).url(F.url.live_play).path(F.path.download).name(F.file_name.live_play)
                .startDownload(new DownloadListener() {
                    @Override
                    public void onPending(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onStart(DownloadInfo downloadInfo) {
                        progressDialog.setProgress(downloadInfo.getProgress());
                    }

                    @Override
                    public void onPause(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {
                        progressDialog.setProgress(downloadInfo.getProgress());
                    }

                    @Override
                    public void onFinished(DownloadInfo downloadInfo) {
                        progressDialog.setProgress(100);
                        progressDialog.dismiss();
                        if(ApkCheck.isApkCanInstalled(getContext(), F.path.download, F.file_name.live_play )){
                            ApkInstall.installApk(getContext(), F.path.download, F.file_name.live_play);
                        }else{
                            if(FileCheck.isFileExists(F.path.download, F.file_name.live_play)){
                                FileCheck.delete(F.path.download, F.file_name.live_play);
                            }
                            Toast.makeText(Application.getContext(),getString(R.string.update_error),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancel(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onError(DownloadInfo downloadInfo) {

                    }
                });
    }

    private void showCustomShortCut(final ImageButton imageButton, final String type) {
        Observable.just(type)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, InstalledApp>() {
                    @Override
                    public InstalledApp call(String s) {
                        List<InstalledApp> list = installedAppDao.queryDataByType(s);
                        if (list.size() == 1) {
                            return installedAppDao.queryDataByType(s).get(0);
                        } else {
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<InstalledApp>() {
                    @Override
                    public void call(final InstalledApp installedApp) {
                        if (installedApp != null) {
                            imageButton.setImageDrawable(ApkCheck.getInstalledApkIcon(getContext(), installedApp.getAppPackageName()));
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(getLevel() <= 0 ){
                                        Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.account_error) ,
                                                Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Intent intent = new Intent(getActivity(), Splash1Activity.class);
                                    intent.putExtra("packageName", installedApp.getAppPackageName());
                                    startActivity(intent);
                                }
                            });
                            imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    if(getLevel() <= 0 ){
                                        Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.account_error) ,
                                                Toast.LENGTH_LONG).show();
                                        return true;
                                    }
                                    Intent intent = new Intent(getContext(), AppSelectActivity.class);
                                    intent.putExtra("type", type);
                                    startActivity(intent);
                                    return true;
                                }
                            });
                        } else {
                            imageButton.setImageResource(R.drawable.add1);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(getLevel() <= 0 ){
                                        Toast.makeText(Application.getContext() , Application.getContext().getString(R.string.account_error) ,
                                                Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    Intent intent = new Intent(getContext(), AppSelectActivity.class);
                                    intent.putExtra("type", type);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void loadRollImage(List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        tivBanner.setImageInfoList(list);
    }

    @Override
    public void loadRollOverImage(final List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        tivEufonic.setImageInfoList(list);
    }

    @Override
    public void loadCloudImage(List<String> list) {
        if(list == null || list.size() <1){
            return;
        }
    }

    @Override
    public void loadVideo(final List<VideoInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        videoSubscription = Observable.interval(0,list.get(0).getPlayInterval(),TimeUnit.MILLISECONDS)
                .take(list.size())
                .subscribeOn(Schedulers.io())
                .repeat()
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        int i = aLong.intValue();
                        return list.get(i).getUrl();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if(vv_Main != null ){
                            if(currentVideoInfo == null){
                                currentVideoInfo = new VideoInfo();
                            }
                            currentVideoInfo.setUrl(s);
                            playVideo(s);
                        }
                    }
                });
    }

    private void playVideo(final String url) {
        if(vv_Main != null){
            vv_Main.stopPlayback();
        }
//        Logger.d(isUserVisible+"");
        if(!isUserVisible) {
            return;
        }
        vv_Main.setVideoPath(url);
        vv_Main.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isVideoPlaying = true;
                ivBTVLogo.setVisibility(View.GONE);
                if(isUserVisible) {
                    vv_Main.start();
                }
            }
        });
        vv_Main.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ivBTVLogo.setVisibility(View.VISIBLE);
                return true;
            }
        });
        vv_Main.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    vv_Main.setVideoPath(url);
                    vv_Main.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.d(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onConnected(boolean isConnected) {
        Logger.d("connect");
        if (presenter != null && vv_Main != null && !vv_Main.isPlaying() && !isVideoPlaying) {
            presenter.loadVideo();
        }
        if (presenter != null) {
            presenter.loadRollImageData();
            presenter.loadRollOverImage();
            presenter.loadCloudData();
        }
    }

    @Override
    public void onDisconnect(boolean disConnected) {
        if (disConnected) {
            Toast.makeText(Application.getContext(), getString(R.string.network_disconnect), Toast.LENGTH_LONG).show();
        }
    }

    private void setZoom() {
        ibtEufonico.setOnFocusChangeListener(this);
        ibtUserManual.setOnFocusChangeListener(this);
        ibtSetting.setOnFocusChangeListener(this);
        ibtApps.setOnFocusChangeListener(this);
        ibtMarket.setOnFocusChangeListener(this);
        ibtGame.setOnFocusChangeListener(this);
        ibtOpportunity.setOnFocusChangeListener(this);
        ibtEufonicBvision.setOnFocusChangeListener(this);
        ibt1.setOnFocusChangeListener(this);
        ibt2.setOnFocusChangeListener(this);
        ibt3.setOnFocusChangeListener(this);
        ibt4.setOnFocusChangeListener(this);
        ibt5.setOnFocusChangeListener(this);
        ibt6.setOnFocusChangeListener(this);
        ibt7.setOnFocusChangeListener(this);
        ibt8.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Zoom.zoomIn09_10(v);
        }
    }

    private int getLevel(){
        String l = (String) SPUtils.get(Application.getContext() , "userLevel" , "1");
        return Integer.parseInt(l);
    }
}
