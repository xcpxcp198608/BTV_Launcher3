package com.wiatec.btv_launcher.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.Activity.AppSelectActivity;
import com.wiatec.btv_launcher.Activity.FMPlayActivity;
import com.wiatec.btv_launcher.Activity.LoginActivity;
import com.wiatec.btv_launcher.Activity.MenuActivity;
import com.wiatec.btv_launcher.Activity.Message1Activity;
import com.wiatec.btv_launcher.Activity.Opportunity1Activity;
import com.wiatec.btv_launcher.Activity.Splash1Activity;
import com.wiatec.btv_launcher.Activity.UserManual1Activity;
import com.wiatec.btv_launcher.Activity.WebViewActivity;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.adapter.PushMessageAdapter;
import com.wiatec.btv_launcher.adapter.TranslationAdapter;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.bean.PushMessageInfo;
import com.wiatec.btv_launcher.bean.RollImageInfo;
import com.wiatec.btv_launcher.bean.UserDataInfo;
import com.wiatec.btv_launcher.custom_view.MessageListView;
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
    @BindView(R.id.ibt_customer_service)
    ImageButton ibtCustomerService;
    @BindView(R.id.ibt_market)
    ImageButton ibtMarket;
    @BindView(R.id.ibt_opportunity)
    ImageButton ibtOpportunity;
    @BindView(R.id.ibt_message)
    ImageButton ibtMessage;
    @BindView(R.id.ibt_institute)
    ImageButton ibtInstitute;
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
    @BindView(R.id.vv_main)
    VideoView vv_Main;
    @BindView(R.id.fl_video)
    FrameLayout flVideo;
    @BindView(R.id.tv_message_count)
    TextView tvMessageCount;
    @BindView(R.id.lv_message)
    MessageListView messageListView;
    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.tiv_banner)
    TranslationImageView tivBanner;
    @BindView(R.id.ll_push_message)
    LinearLayout llPushMessage;
    @BindView(R.id.pb_push_message)
    ProgressBar pbPushMessage;

    private PushMessageAdapter pushMessageAdapter;
    private TranslationAdapter translationAdapter;
    private NetworkStatusReceiver networkStatusReceiver;
    private Subscription videoSubscription;
    private Subscription messageSubscription;
    private VideoInfo currentVideoInfo;
    private boolean isVideoPlaying = false;
    private MessageDao messageDao;
    private long entryTime;
    private long exitTime;
    private long holdTime;
    private UserDataInfo userDataInfo;
    private InstalledAppDao installedAppDao;
    private Intent intent;

    private List<PushMessageInfo> pushMessageInfoList  = new ArrayList<>();
    private List<ImageInfo> rollImageInfoList = new ArrayList<>();

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
        intent = new Intent();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ibtMessage.setNextFocusRightId(R.id.ll_push_message);
        ibt6.setNextFocusRightId(R.id.ll_push_message);
        setZoom();
        showCustomShortCut(ibt1 , "ibt1");
        showCustomShortCut(ibt2 , "ibt2");
        showCustomShortCut(ibt3 , "ibt3");
        showCustomShortCut(ibt4 , "ibt4");
        showCustomShortCut(ibt5 , "ibt5");
        //showCustomShortCut(ibt6 , "ibt6");
    }

    @Override
    protected Fragment1Presenter createPresenter() {
        return new Fragment1Presenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        entryTime = System.currentTimeMillis();
        checkMessageCount();
        if (!SystemConfig.isNetworkConnected(getContext())){
            return;
        }
        if (presenter != null && vv_Main != null && !vv_Main.isPlaying() && !isVideoPlaying) {
            presenter.loadVideo();
        }
        if (presenter != null) {
            presenter.loadRollImageData();
            presenter.loadRollOverImage();
            presenter.loadCloudData();
            presenter.loadPushMessage();
        }
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
        if(messageListView != null){
            messageListView.stop();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messageListView != null){
            messageListView.stop();
        }
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
        if(messageListView != null){
            messageListView.stop();
        }
    }

    @OnClick({R.id.ibt_eufonico, R.id.ibt_user_manual, R.id.ibt_setting, R.id.ibt_apps, R.id.ibt_market,
            R.id.ibt_opportunity, R.id.ibt_message, R.id.fl_video , R.id.ibt_institute ,
            R.id.ibt_games ,R.id.ibt_customer_service ,R.id.ll_push_message , R.id.ibt_6})
    public void onClick(View view) {
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
            case R.id.ibt_games:
                if (ApkCheck.isApkInstalled(getContext(), F.package_name.happy_chick)) {
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.happy_chick);
                }else{
                    Toast.makeText(getContext() , getString(R.string.download_guide)+" HappyChick",Toast.LENGTH_SHORT).show();
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.market);
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
            case R.id.ibt_message:
                startActivity(new Intent(getContext(), Message1Activity.class));
                break;
            case R.id.fl_video:
                launchAppByLogin(getContext() , F.package_name.btv);
                break;
            case R.id.ibt_institute:
                if (ApkCheck.isApkInstalled(getContext(), F.package_name.joinme)) {
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.joinme);
                }else{
                    Toast.makeText(getContext() , getString(R.string.download_guide)+" Joinme",Toast.LENGTH_SHORT).show();
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.market);
                }
                break;
            case R.id.ibt_customer_service:
                intent.setClass(getActivity(), WebViewActivity.class);
                intent.putExtra("url", F.url.ld_support);
                getContext().startActivity(intent);
                break;
            case  R.id.ll_push_message:
                pbPushMessage.setVisibility(View.VISIBLE);
                presenter.loadPushMessage();
                presenter.loadRollOverImage();
                break;
            case R.id.ibt_6:
                startActivity(new Intent(getContext() , LoginActivity.class));
                break;
            default:
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
            presenter.check(userName , packageName , context );
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
    public void loadRollImage(List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        rollImageInfoList.clear();
        rollImageInfoList.addAll(list);
        if(translationAdapter ==null){
            translationAdapter = new TranslationAdapter(rollImageInfoList);
        }
        translationAdapter.notifyDataSetChanged();
        tivBanner.setRollViewAdapter(translationAdapter);
    }

    @Override
    public void loadRollOverImage(final List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        Random random = new Random();
        int i = random.nextInt(list.size());
        ImageInfo imageInfo = list.get(i);
        Glide.with(getContext()).load(imageInfo.getUrl()).dontAnimate().into(ivAd);
    }

    @Override
    public void loadCloudImage(List<String> list) {
        if(list == null || list.size() <1){
            return;
        }
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
                vv_Main.start();
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

    @Override
    public void loadPushMessage(List<PushMessageInfo> list) {
        if(list != null && list.size() >0){
            pushMessageInfoList.clear();
            pushMessageInfoList.addAll(list);
            pbPushMessage.setVisibility(View.GONE);
            if(pushMessageAdapter == null){
                pushMessageAdapter = new PushMessageAdapter(getContext() , pushMessageInfoList);
            }
            messageListView.setAdapter(pushMessageAdapter);
            pushMessageAdapter.notifyDataSetChanged();
            messageListView.start();
        }
    }

    @Override
    public void onConnected(boolean isConnected) {
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
        ibtOpportunity.setOnFocusChangeListener(this);
        ibtMessage.setOnFocusChangeListener(this);
        ibtInstitute.setOnFocusChangeListener(this);
        ibtCustomerService.setOnFocusChangeListener(this);
        ibt1.setOnFocusChangeListener(this);
        ibt2.setOnFocusChangeListener(this);
        ibt3.setOnFocusChangeListener(this);
        ibt4.setOnFocusChangeListener(this);
        ibt5.setOnFocusChangeListener(this);
        ibt6.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Zoom.zoomIn09_10(v);
        }
    }

    public void showChoiceDialog (){
//        AlertDialog dialog = new AlertDialog.Builder(getContext())
//            .setTitle("SELECT")
//            .setSingleChoiceItems(new String[]{"LDCLOUD", "EUFONICO"}, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which){
//                    case 0:
//                        ibtEufonico.setVisibility(View.GONE);
//                        ibt_LdCloud.setVisibility(View.VISIBLE);
//                        ibt_FullScreen.setVisibility(View.VISIBLE);
//                        ivLdCloudSmall.setVisibility(View.VISIBLE);
//                        dialog.dismiss();
//                        break;
//                    case 1:
//                        ibtEufonico.setVisibility(View.VISIBLE);
//                        ibt_LdCloud.setVisibility(View.GONE);
//                        ibt_FullScreen.setVisibility(View.GONE);
//                        ivLdCloudSmall.setVisibility(View.GONE);
//                        dialog.dismiss();
//                        break;
//                }
//            }
//        }).show();
    }
}
