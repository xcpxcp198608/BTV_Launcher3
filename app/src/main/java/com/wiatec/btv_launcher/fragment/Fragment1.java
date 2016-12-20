package com.wiatec.btv_launcher.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.rollviewpager.RollPagerView;
import com.wiatec.btv_launcher.Activity.OpportunityActivity;
import com.wiatec.btv_launcher.Activity.PlayAdActivity;
import com.wiatec.btv_launcher.Activity.UserManualActivity;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.OnNetworkStatusListener;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Activity.MenuActivity;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Activity.MessageActivity;
import com.wiatec.btv_launcher.Activity.UserGuideActivity;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.adapter.RollImageAdapter;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.CloudImageInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.RollImageInfo;
import com.wiatec.btv_launcher.presenter.Fragment1Presenter;
import com.wiatec.btv_launcher.receiver.NetworkStatusReceiver;
import com.wiatec.btv_launcher.service.LoadCloudService;

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

public class Fragment1 extends BaseFragment<IFragment1 ,Fragment1Presenter> implements IFragment1 ,OnNetworkStatusListener,View.OnFocusChangeListener {
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
    @BindView(R.id.ibt_ad_1)
    ImageButton ibt_Ad1;
    @BindView(R.id.vv_main)
    VideoView vv_Main;
    @BindView(R.id.ibt_ld_cloud)
    ImageButton ibt_LdCloud;

    private boolean isF1Visible = false;
    private int playPosition=0;
    private NetworkStatusReceiver networkStatusReceiver;
    private Subscription subscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Logger.d("f1 -onCreateView ");
        View view = inflater.inflate(R.layout.fragment1, container, false);
        ButterKnife.bind(this, view);
        networkStatusReceiver = new NetworkStatusReceiver(null);
        networkStatusReceiver.setOnNetworkStatusListener(this);
        getContext().registerReceiver(networkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            isF1Visible = true;
            //Logger.d("f1 -isVisibleToUser " + isVisibleToUser);
            if(vv_Main!=null && !vv_Main.isPlaying() && isF1Visible){
               // Logger.d("f1 -isVisibleToUser " +"play");
                playVideo();
            }
        }else {
            //Logger.d("f1 -isVisibleToUser " + isVisibleToUser);
            isF1Visible =false;
            if(vv_Main!=null && vv_Main.isPlaying()){
                playPosition = vv_Main.getCurrentPosition();
                vv_Main.stopPlayback();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Logger.d("f1 -onStart ");
        setZoom();
    }

    @Override
    protected Fragment1Presenter createPresenter() {
        return new Fragment1Presenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("f1 -onResume ");
        if(vv_Main!=null && !vv_Main.isPlaying()){
            if(isF1Visible) {
                //Logger.d("f1 -onResume " +"play");
               playVideo();
            }
        }
        if(SystemConfig.isNetworkConnected(getContext())){
            presenter.loadData();
            presenter.loadCloudData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("f1 -onPause ");
        if(vv_Main!=null && vv_Main.isPlaying()){
            playPosition = vv_Main.getCurrentPosition();
            vv_Main.stopPlayback();
        }
        subscription.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d("f1 -onDestroyView ");
        getContext().unregisterReceiver(networkStatusReceiver);
    }

    @OnClick({R.id.ibt_btv, R.id.ibt_user_guide, R.id.ibt_setting, R.id.ibt_apps, R.id.ibt_market, R.id.ibt_anti_virus, R.id.ibt_privacy,  R.id.ibt_ld_cloud})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_btv:
                Intent intent = new Intent(getContext() , PlayAdActivity.class);
                intent.putExtra("packageName" , F.package_name.btv);
                startActivity(intent);
                break;
            case R.id.ibt_user_guide:
                startActivity(new Intent(getContext(), UserManualActivity.class));
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
                if (ApkCheck.isApkInstalled(getContext(), F.package_name.market)) {
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.market);
                }
                break;
            case R.id.ibt_anti_virus:
                startActivity(new Intent(getContext() , OpportunityActivity.class));
                break;
            case R.id.ibt_privacy:
                startActivity(new Intent(getContext() , MessageActivity.class));
                break;
            case R.id.ibt_ld_cloud:
                if (ApkCheck.isApkInstalled(getContext(), F.package_name.cloud)) {
                    ApkLaunch.launchApkByPackageName(getContext(), F.package_name.cloud);
                }
                break;
        }
    }

    private void playVideo (){
        vv_Main.setVideoPath(F.path.video);
        vv_Main.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_Main.seekTo(playPosition);
                if(isF1Visible) {
                    vv_Main.start();
                }
            }
        });
        vv_Main.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                vv_Main.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.btvi3));
                vv_Main.start();
                return true;
            }
        });
        vv_Main.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                vv_Main.setVideoPath(F.path.video);
                vv_Main.start();
            }
        });
    }

    @Override
    public void loadImage(final List<ImageInfo> list) {
        //Logger.d(list.toString());
        Glide.with(Application.getContext()).load(list.get(0).getUrl()).placeholder(R.drawable.btv_icon_1).into(ibt_Btv);
        Glide.with(Application.getContext()).load(list.get(1).getUrl()).placeholder(R.drawable.user_guide_icon_4).into(ibt_UserGuide);
        Glide.with(Application.getContext()).load(list.get(2).getUrl()).placeholder(R.drawable.setting_icon).into(ibt_Setting);
        Glide.with(Application.getContext()).load(list.get(3).getUrl()).placeholder(R.drawable.apps_icon_3).into(ibt_Apps);
        Glide.with(Application.getContext()).load(list.get(4).getUrl()).placeholder(R.drawable.market_icon).into(ibt_Market);
        Glide.with(Application.getContext()).load(list.get(5).getUrl()).placeholder(R.drawable.ld_opportunity).into(ibt_AntiVirus);
        Glide.with(Application.getContext()).load(list.get(6).getUrl()).placeholder(R.drawable.message_icon).into(ibt_Privacy);
        Glide.with(Application.getContext()).load(list.get(7).getUrl()).placeholder(R.drawable.ld_store_icon).into(ibt_LdStore);
        Glide.with(Application.getContext()).load(list.get(8).getUrl()).placeholder(R.drawable.bksound_icon_3).into(ibt_Ad1);
        Glide.with(Application.getContext()).load(list.get(9).getUrl()).placeholder(R.drawable.ld_cloud_icon_3).into(ibt_LdCloud);
        ibt_LdStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkByBrowser(list.get(7).getLink());
            }
        });
        ibt_Ad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkByBrowser(list.get(8).getLink());
            }
        });
    }

    @Override
    public void loadRollImage(List<ImageInfo> list) {
        //Logger.d(list.toString());
        RollImageAdapter rollImageAdapter = new RollImageAdapter(list);
        rpv_Main.setAdapter(rollImageAdapter);
    }

    @Override
    public void loadCloudImage(final List<String> list) {
        if(list != null && list.size()>0){
            Logger.d(list.toString());
            subscription = Observable.interval(6, TimeUnit.SECONDS).take(list.size())
                    .repeat()
                    .map(new Func1<Long, String>() {
                        @Override
                        public String call(Long aLong) {
                            return list.get(Integer.parseInt(aLong+""));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            Logger.d("f1--->" +s);
                            Glide.with(getContext()).load(s)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.ld_cloud_icon_3)
                                    .error(R.drawable.ld_cloud_icon_3)
                                    .into(ibt_LdCloud);
                        }
                    });
        }
    }

    public void showLinkByBrowser(String url){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW , Uri.parse(url)));
        }catch (Exception e){
            e.printStackTrace();
            Logger.d(e.getMessage());
        }
    }

    @Override
    public void onConnected(boolean isConnected) {
        if(isConnected){
            presenter.loadData();
        }
    }

    @Override
    public void onDisconnect(boolean disConnected) {
        if(disConnected){
            Toast.makeText(getContext() ,getString(R.string.network_disconnect) ,Toast.LENGTH_LONG).show();
        }
    }

    private void setZoom (){
        ibt_Btv.setOnFocusChangeListener(this);
        ibt_UserGuide.setOnFocusChangeListener(this);
        ibt_Setting.setOnFocusChangeListener(this);
        ibt_Apps.setOnFocusChangeListener(this);
        ibt_Market.setOnFocusChangeListener(this);
        ibt_AntiVirus.setOnFocusChangeListener(this);
        ibt_Privacy.setOnFocusChangeListener(this);
        ibt_LdStore.setOnFocusChangeListener(this);
        ibt_Ad1.setOnFocusChangeListener(this);
        ibt_LdCloud.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            Zoom.zoomIn09_10(v);
        }
    }
}
