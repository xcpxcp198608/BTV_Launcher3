package com.wiatec.btv_launcher.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.wiatec.btv_launcher.Activity.AppSelectActivity;
import com.wiatec.btv_launcher.Activity.CloudImageFullScreen1Activity;
import com.wiatec.btv_launcher.Activity.FMPlayActivity;
import com.wiatec.btv_launcher.Activity.LDSupportActivity;
import com.wiatec.btv_launcher.Activity.LoginActivity;
import com.wiatec.btv_launcher.Activity.LoginSplashActivity;
import com.wiatec.btv_launcher.Activity.MainActivity;
import com.wiatec.btv_launcher.Activity.MenuActivity;
import com.wiatec.btv_launcher.Activity.Message1Activity;
import com.wiatec.btv_launcher.Activity.Opportunity1Activity;
import com.wiatec.btv_launcher.Activity.PlayActivity;
import com.wiatec.btv_launcher.Activity.PlayAdActivity;
import com.wiatec.btv_launcher.Activity.Splash1Activity;
import com.wiatec.btv_launcher.Activity.UserManual1Activity;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.bean.UserDataInfo;
import com.wiatec.btv_launcher.custom_view.MultiImage;
import com.wiatec.btv_launcher.custom_view.RollOverView;
import com.wiatec.btv_launcher.receiver.OnNetworkStatusListener;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.MessageDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.adapter.RollImageAdapter;
import com.wiatec.btv_launcher.adapter.RollOverViewAdapter;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.MessageInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.presenter.Fragment1Presenter;
import com.wiatec.btv_launcher.receiver.NetworkStatusReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    @BindView(R.id.ibt_btv)
    ImageButton ibt_Btv;
    @BindView(R.id.ibt_user_guide)
    ImageButton ibt_UserGuide;
    @BindView(R.id.ibt_setting)
    ImageButton ibt_Setting;
    @BindView(R.id.ibt_apps)
    ImageButton ibt_Apps;
    @BindView(R.id.ibt_market)
    ImageButton ibt_Market;
    @BindView(R.id.ibt_anti_virus)
    ImageButton ibt_AntiVirus;
    @BindView(R.id.ibt_privacy)
    ImageButton ibt_Privacy;
    @BindView(R.id.rpv_main)
    RollPagerView rpv_Main;
    @BindView(R.id.ibt_ld_store)
    ImageButton ibt_LdStore;
    @BindView(R.id.ibt_full_screen)
    ImageButton ibt_FullScreen;
    @BindView(R.id.ibt_7)
    FrameLayout ibt7;
    @BindView(R.id.ibt_8)
    ImageButton ibt8;
    @BindView(R.id.ibt_9)
    ImageButton ibt9;
    @BindView(R.id.ibt_10)
    ImageButton ibt10;
    @BindView(R.id.ibt_eufonico)
    ImageButton ibtEufonico;
    @BindView(R.id.ibt_ldsupport)
    ImageButton ibtLDSupport;
    @BindView(R.id.vv_main)
    VideoView vv_Main;
    @BindView(R.id.ibt_ld_cloud)
    MultiImage ibt_LdCloud;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    @BindView(R.id.tv_message_count)
    TextView tvMessageCount;
    @BindView(R.id.roll_over_view)
    RollOverView rollOverView;

    private boolean isF1Visible = false;
    private NetworkStatusReceiver networkStatusReceiver;
    private Subscription videoSubscription;
    private Subscription messageSubscription;
    private VideoInfo currentVideoInfo;
    private boolean isVideoPlaying = false;
    private MessageDao messageDao;
    private boolean isCloudImagePlaying = false;

    private long entryTime;
    private long exitTime;
    private long holdTime;
    private UserDataInfo userDataInfo;
    private MainActivity activity;
    private InstalledAppDao installedAppDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, container, false);
        ButterKnife.bind(this, view);
        networkStatusReceiver = new NetworkStatusReceiver(null);
        networkStatusReceiver.setOnNetworkStatusListener(this);
        getContext().registerReceiver(networkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        messageDao = MessageDao.getInstance(getContext());
        userDataInfo = new UserDataInfo();
        installedAppDao = InstalledAppDao.getInstance(Application.getContext());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isF1Visible = true;
            if(!SystemConfig.isNetworkConnected(Application.getContext())){
                return;
            }
            if (vv_Main != null && !vv_Main.isPlaying() && isF1Visible && !isVideoPlaying) {
                presenter.loadVideo();
            }
            if(presenter != null){
                presenter.loadImageData();
                presenter.loadCloudData();
            }
            entryTime = System.currentTimeMillis();
        } else {
            isF1Visible = false;
            if (vv_Main != null) {
                vv_Main.stopPlayback();
            }
            if (videoSubscription != null) {
                isVideoPlaying = false;
                videoSubscription.unsubscribe();
            }
            if (messageSubscription != null) {
                messageSubscription.unsubscribe();
            }
            if(userDataInfo != null && SystemConfig.isNetworkConnected(Application.getContext())){
                exitTime = System.currentTimeMillis();
                holdTime = exitTime - entryTime;
                String eTime = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date(exitTime));
                userDataInfo.setExitTime(eTime);
                userDataInfo.setStayTime(holdTime+"");
                userDataInfo.setUserName((String) SPUtils.get(Application.getContext(),"userName" ,""));
                userDataInfo.setMac((String) SPUtils.get(Application.getContext(),"mac" ,""));
                userDataInfo.setCountry((String) SPUtils.get(Application.getContext(),"country" ,""));
                userDataInfo.setCity((String) SPUtils.get(Application.getContext(),"city" ,""));
                if(presenter != null && entryTime >0) {
                    presenter.uploadHoldTime(userDataInfo);
                }
            }
            if(rollOverView != null){
                rollOverView.stop();
            }
            if(ibt_LdCloud!= null){
                ibt_LdCloud.stop();
            }
            isCloudImagePlaying = false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setZoom();
        ibt_LdCloud.setNextFocusDownId(R.id.ibt_full_screen);
        showCustomShortCut(ibt8 , "ibt8");
        showCustomShortCut(ibt9 , "ibt9");
        showCustomShortCut(ibt10 , "ibt10");
    }

    @Override
    protected Fragment1Presenter createPresenter() {
        return new Fragment1Presenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SystemConfig.isNetworkConnected(getContext())){
            return;
        }
        if (vv_Main != null && !vv_Main.isPlaying() && isF1Visible && !isVideoPlaying) {
            presenter.loadVideo();
        }
        if (presenter != null && isF1Visible) {
            presenter.loadImageData();
            presenter.loadCloudData();
        }
        checkMessageCount();
        entryTime = System.currentTimeMillis();
    }

    @Override
    public void onPause() {
        super.onPause();
        isVideoPlaying = false;
        if (vv_Main != null && vv_Main.isPlaying()) {
            vv_Main.stopPlayback();
        }
        if (videoSubscription != null) {
            videoSubscription.unsubscribe();
        }
        if (messageSubscription != null) {
            messageSubscription.unsubscribe();
        }
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
        if(rollOverView != null){
            rollOverView.stop();
        }
        if(ibt_LdCloud!= null){
            ibt_LdCloud.stop();
        }
        isCloudImagePlaying = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().unregisterReceiver(networkStatusReceiver);
        if (videoSubscription != null) {
            videoSubscription.unsubscribe();
        }
        if (messageSubscription != null) {
            messageSubscription.unsubscribe();
        }
    }

    @OnClick({R.id.ibt_btv, R.id.ibt_user_guide, R.id.ibt_setting, R.id.ibt_apps, R.id.ibt_market,
            R.id.ibt_anti_virus, R.id.ibt_privacy, R.id.ibt_ld_cloud ,R.id.fl_video ,R.id.ibt_full_screen,
            R.id.ibt_7 , R.id.ibt_eufonico , R.id.ibt_ldsupport})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_btv:
                Intent intent = new Intent(getContext() , LoginSplashActivity.class);
                intent.putExtra("packageName" , F.package_name.btv);
                startActivity(intent);
                break;
            case R.id.ibt_user_guide:
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
                presenter.launchApp(F.package_name.market);
                break;
            case R.id.ibt_anti_virus:
                startActivity(new Intent(getContext(), Opportunity1Activity.class));
                break;
            case R.id.ibt_privacy:
                startActivity(new Intent(getContext(), Message1Activity.class));
                break;
            case R.id.ibt_ld_cloud:
                if (ApkCheck.isApkInstalled(getContext(), F.package_name.cloud)) {
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.cloud);
                }
                break;
            case R.id.fl_video:
//                if(currentVideoInfo != null){
//                    Intent intent2 = new Intent(getContext() , PlayActivity.class);
//                    intent2.putExtra("url" , currentVideoInfo.getUrl());
//                    startActivity(intent2);
//                }
                presenter.launchApp(F.package_name.bplay);
                break;
            case R.id.ibt_full_screen:
                if(isCloudImagePlaying){
                    Intent intent3 = new Intent(getContext() , CloudImageFullScreen1Activity.class);
                    intent3.putExtra("cloudImagePosition",ibt_LdCloud.getCurrentPosition());
                    startActivity(intent3);
                }
                break;
            case R.id.ibt_7:
                if (ApkCheck.isApkInstalled(getContext(), F.package_name.joinme)) {
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.joinme);
                }else{
                    Toast.makeText(getContext() , getString(R.string.download_guide)+" Joinme",Toast.LENGTH_SHORT).show();
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.market);
                }
                break;
            case R.id.ibt_eufonico:
                Intent intent4 = new Intent(getActivity(), FMPlayActivity.class);
                intent4.putExtra("url", "http://142.4.216.91:8280/");
                getContext().startActivity(intent4);
                break;
            case R.id.ibt_ldsupport:
                startActivity(new Intent(getContext() , LDSupportActivity.class));
                break;
            default:
                break;
        }
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
                                    Intent intent = new Intent(getActivity(), Splash1Activity.class);
                                    intent.putExtra("packageName", installedApp.getAppPackageName());
                                    startActivity(intent);
                                }
                            });
                            imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
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
    public void loadImage(final List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        Glide.with(Application.getContext()).load(list.get(0).getUrl()).placeholder(R.drawable.btv_icon_1).dontAnimate().into(ibt_Btv);
        Glide.with(Application.getContext()).load(list.get(1).getUrl()).placeholder(R.drawable.user_guide_icon_4).dontAnimate().into(ibt_UserGuide);
        Glide.with(Application.getContext()).load(list.get(2).getUrl()).placeholder(R.drawable.setting_icon).dontAnimate().into(ibt_Setting);
        Glide.with(Application.getContext()).load(list.get(3).getUrl()).placeholder(R.drawable.apps_icon_3).dontAnimate().into(ibt_Apps);
        Glide.with(Application.getContext()).load(list.get(4).getUrl()).placeholder(R.drawable.market_icon).dontAnimate().into(ibt_Market);
        Glide.with(Application.getContext()).load(list.get(5).getUrl()).placeholder(R.drawable.ld_opportunity_12).dontAnimate().into(ibt_AntiVirus);
        Glide.with(Application.getContext()).load(list.get(6).getUrl()).placeholder(R.drawable.message_icon).dontAnimate().into(ibt_Privacy);
        Glide.with(Application.getContext()).load(list.get(7).getUrl()).placeholder(R.drawable.ld_store_icon).dontAnimate().into(ibt_LdStore);
        Glide.with(Application.getContext()).load(list.get(9).getUrl()).placeholder(R.drawable.ld_cloud_icon_3).dontAnimate().into(ibt_LdCloud);
        ibt_LdStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkByBrowser(list.get(7).getLink());
            }
        });
    }

    @Override
    public void loadRollImage(List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        RollImageAdapter rollImageAdapter = new RollImageAdapter(list);
        rpv_Main.setAdapter(rollImageAdapter);
    }

    @Override
    public void loadRollOverImage(final List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        RollOverViewAdapter rollOverViewAdapter = new RollOverViewAdapter(list);
        rollOverView.setRollViewAdapter(rollOverViewAdapter);
        rollOverViewAdapter.setOnItemClickListener(new RollOverViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showLinkByBrowser(list.get(position).getLink());
            }
        });
    }

    @Override
    public void loadCloudImage(List<String> list) {
        if(list == null || list.size() <1){
            return;
        }
        isCloudImagePlaying = true;
        ibt_LdCloud.setImages(list);
    }

    private void checkMessageCount(){
        messageSubscription = Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<MessageInfo>>() {
                    @Override
                    public List<MessageInfo> call(String s) {
                        return messageDao.queryUnreadMessage();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<MessageInfo>>() {
                    @Override
                    public void call(List<MessageInfo> list) {
                        if (list.size() > 0) {
                            int count = list.size();
                            tvMessageCount.setText(count+"");
                            tvMessageCount.setVisibility(View.VISIBLE);
                        } else {
                            tvMessageCount.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void loadVideo(final List<VideoInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        isVideoPlaying = true;
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
        if(vv_Main != null && vv_Main.isPlaying()){
            vv_Main.stopPlayback();
        }
        vv_Main.setVideoPath(url);
        vv_Main.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (isF1Visible) {
                    vv_Main.start();
                }
            }
        });
        vv_Main.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
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

    public void showLinkByBrowser(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d(e.getMessage());
        }
    }

    @Override
    public void onConnected(boolean isConnected) {
//        if (isConnected) {
//            presenter.loadImageData();
//            if(!isVideoPlaying) {
//                presenter.loadVideo();
//            }
//        }
    }

    @Override
    public void onDisconnect(boolean disConnected) {
        if (disConnected) {
            Toast.makeText(Application.getContext(), getString(R.string.network_disconnect), Toast.LENGTH_LONG).show();
        }
    }

    private void setZoom() {
        ibt_Btv.setOnFocusChangeListener(this);
        ibt_UserGuide.setOnFocusChangeListener(this);
        ibt_Setting.setOnFocusChangeListener(this);
        ibt_Apps.setOnFocusChangeListener(this);
        ibt_Market.setOnFocusChangeListener(this);
        ibt_AntiVirus.setOnFocusChangeListener(this);
        ibt_Privacy.setOnFocusChangeListener(this);
        ibt_LdStore.setOnFocusChangeListener(this);
        ibt_LdCloud.setOnFocusChangeListener(this);
        ibt_FullScreen.setOnFocusChangeListener(this);
        ibt7.setOnFocusChangeListener(this);
        ibt8.setOnFocusChangeListener(this);
        ibt9.setOnFocusChangeListener(this);
        ibt10.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if(v.getId() == R.id.ibt_btv || v.getId() == R.id.ibt_ld_store ) {
                Zoom.zoomIn10_11(v);
            }else {
                Zoom.zoomIn09_10(v);
            }
        }
    }
}
