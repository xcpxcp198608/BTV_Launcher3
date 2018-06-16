package com.wiatec.btv_launcher.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.px.common.constant.CommonApplication;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.DownloadListener;
import com.px.common.http.pojo.DownloadInfo;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.Activity.AppSelectActivity;
import com.wiatec.btv_launcher.Activity.FMPlayActivity;
import com.wiatec.btv_launcher.Activity.LoginActivity;
import com.wiatec.btv_launcher.Activity.MenuActivity;
import com.wiatec.btv_launcher.Activity.Opportunity1Activity;
import com.wiatec.btv_launcher.Activity.Splash1Activity;
import com.wiatec.btv_launcher.Activity.UserManual1Activity;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.databinding.Fragment1Binding;
import com.wiatec.btv_launcher.sql.InstalledAppDao;
import com.px.common.utils.FileUtil;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.bean.UserLogInfo;
import com.wiatec.btv_launcher.receiver.OnNetworkStatusListener;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;
import com.wiatec.btv_launcher.presenter.Fragment1Presenter;
import com.wiatec.btv_launcher.receiver.NetworkStatusReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class Fragment1 extends BaseFragment<IFragment1, Fragment1Presenter> implements IFragment1, OnNetworkStatusListener, View.OnFocusChangeListener {

    private Fragment1Binding binding;
    private NetworkStatusReceiver networkStatusReceiver;
    private Disposable videoDisposable;
    private VideoInfo currentVideoInfo;
    private boolean isVideoPlaying = false;
    private long entryTime;
    private UserLogInfo userDataInfo;
    private InstalledAppDao installedAppDao;
    private Intent intent;
    private boolean isNetworkReceiveRegister = false;
    private boolean isUserVisible = false;

    @Override
    protected Fragment1Presenter createPresenter() {
        return new Fragment1Presenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment1, container, false);
        binding.setOnEvent(new OnEventListener());
        userDataInfo = new UserLogInfo();
        installedAppDao = InstalledAppDao.getInstance(CommonApplication.context);
        intent = new Intent();
        if (!NetUtil.isConnected()){
            networkStatusReceiver = new NetworkStatusReceiver(null);
            networkStatusReceiver.setOnNetworkStatusListener(this);
            Context context = getContext();
            if(context != null) {
                context.registerReceiver(networkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            }
            isNetworkReceiveRegister = true;
        }
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        setFocusTransmit();
        setZoom();
        showCustomShortCut(binding.ibt1 , "ibt1");
        showCustomShortCut(binding.ibt2 , "ibt2");
        showCustomShortCut(binding.ibt3 , "ibt3");
        showCustomShortCut(binding.ibt4 , "ibt4");
        showCustomShortCut(binding.ibt5 , "ibt5");
        showCustomShortCut(binding.ibt6 , "ibt6");
        showCustomShortCut(binding.ibt7 , "ibt7");
        showCustomShortCut(binding.ibt8 , "ibt8");
        showCustomShortCut(binding.ibt9 , "ibt9");
        showCustomShortCut(binding.ibt10 , "ibt10");
    }

    private void setFocusTransmit() {
        binding.ibt1.setNextFocusDownId(R.id.fl_video);
        binding.ibt2.setNextFocusDownId(R.id.fl_video);
        binding.ibt3.setNextFocusDownId(R.id.fl_video);
        binding.ibt4.setNextFocusDownId(R.id.fl_video);
        binding.ibt5.setNextFocusDownId(R.id.fl_video);
        binding.ibt6.setNextFocusDownId(R.id.fl_video);
        binding.ibt7.setNextFocusDownId(R.id.fl_video);
        binding.ibt8.setNextFocusDownId(R.id.fl_video);
    }

    @Override
    public void onResume() {
        super.onResume();
        isUserVisible = true;
        entryTime = System.currentTimeMillis();
        startLoadData();
    }

    private void startLoadData(){
        if(!NetUtil.isConnected()){
            return;
        }
        if (presenter != null && binding.vvMain != null && !binding.vvMain.isPlaying() && !isVideoPlaying) {
            presenter.loadVideo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(userDataInfo != null){
            long exitTime = System.currentTimeMillis();
            long holdTime = exitTime - entryTime;
            String eTime = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date(exitTime));
            userDataInfo.setExitTime(eTime);
            userDataInfo.setStayTime(holdTime +"");
            userDataInfo.setUserName((String) SPUtil.get(F.sp.username ,""));
            userDataInfo.setMac((String) SPUtil.get(F.sp.ethernet_mac ,""));
            userDataInfo.setCountry((String) SPUtil.get(F.sp.country ,""));
            userDataInfo.setCity((String) SPUtil.get(F.sp.city ,""));
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
        if (videoDisposable != null) {
            videoDisposable.dispose();
        }
        if (binding.vvMain != null) {
            binding.vvMain.stopPlayback();
        }
        if(binding.tivBanner != null){
            binding.tivBanner.stop();
        }
    }

    public class OnEventListener {
        public void onClick(View view) {
            if (getLevel() <= 0) {
                Toast.makeText(CommonApplication.context, CommonApplication.context.getString(R.string.account_error),
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
                    startActivity(new Intent(getContext(), UserManual1Activity.class));
                    break;
                case R.id.ibt_setting:
                    if (AppUtil.isInstalled(F.package_name.setting)) {
                        String device = Build.DEVICE;
                        if("p201".equals(device)) {
                            AppUtil.launchApp(getContext(), F.package_name.setting);
                        }else if ("p281".equals(device)){
                            Intent intent = new Intent();
                            intent.setClassName("com.android.tv.settings", "com.android.tv.settings.MainSettings");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    break;
                case R.id.ibt_apps:
                    startActivity(new Intent(getContext(), MenuActivity.class));
                    break;
                case R.id.ibt_market:
                    launchAppByLogin(getContext(), F.package_name.market);
                    break;
                case R.id.ibt_opportunity:
                    startActivity(new Intent(getContext(), Opportunity1Activity.class));
                    break;
                case R.id.ibt_game:
                    if (AppUtil.isInstalled(F.package_name.happy_chick)) {
                        AppUtil.launchApp(getContext(), F.package_name.happy_chick);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.download_guide) + " HappyChick", Toast.LENGTH_SHORT).show();
                        AppUtil.launchApp(getContext(), F.package_name.market);
                    }
                    break;
                case R.id.fl_video:
                    release();
                    launchAppByLogin(getContext(), F.package_name.ldservice);
                    break;
                case R.id.ibt_eufonic_bvision:
                    if(!AppUtil.isInstalled(F.package_name.ldextension)){
                        return;
                    }
                    Intent intent = new Intent("com.wiatec.ldextension.view.activity.MainActivity");
                    intent.putExtra("router_type", 3);
                    startActivity(intent);
                    break;
            }
        }
    }

    private void launchAppByLogin(Context context , String packageName){
        String userName = (String) SPUtil.get(F.sp.username ,"");
        String token = (String) SPUtil.get(F.sp.token ,"");
        if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(token)){
            getContext().startActivity(new Intent(getContext() , LoginActivity.class));
        }else {
            presenter.check(packageName , context );
        }
    }

    @Override
    public void showLivePlayDownloadDialog() {
        FileUtil.delete(F.path.download, F.file_name.ld_service);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle(getString(R.string.download_add));
        progressDialog.setMessage(getString(R.string.download_wait));
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        HttpMaster.download(getContext()).url(F.url.ld_service).path(F.path.download).name(F.file_name.ld_service)
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
                        if(AppUtil.isApkCanInstall(F.path.download, F.file_name.ld_service )){
                            AppUtil.installApk(F.path.download, F.file_name.ld_service, "");
                        }else{
                            if(FileUtil.isExists(F.path.download, F.file_name.ld_service)){
                                FileUtil.delete(F.path.download, F.file_name.ld_service);
                            }
                            Toast.makeText(CommonApplication.context, getString(R.string.update_error),Toast.LENGTH_LONG).show();
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
        videoDisposable = Observable.just(type)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, InstalledApp>() {
                    @Override
                    public InstalledApp apply(String s) throws Exception {
                        List<InstalledApp> list = installedAppDao.queryDataByType(s);
                        if (list.size() == 1) {
                            return installedAppDao.queryDataByType(s).get(0);
                        } else {
                            return new InstalledApp();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<InstalledApp>() {
                    @Override
                    public void accept(final InstalledApp installedApp) throws Exception {
                        if (!TextUtils.isEmpty(installedApp.getAppPackageName())) {
                            imageButton.setImageDrawable(AppUtil.getIcon(installedApp.getAppPackageName()));
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(getLevel() <= 0 ){
                                        Toast.makeText(CommonApplication.context , CommonApplication.context.getString(R.string.account_error) ,
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
                                        Toast.makeText(CommonApplication.context , CommonApplication.context.getString(R.string.account_error) ,
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
                                        Toast.makeText(CommonApplication.context , CommonApplication.context.getString(R.string.account_error) ,
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
        binding.tivBanner.setImageInfoList(list);
    }

    @Override
    public void loadRollOverImage(final List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        binding.tivEufonic.setImageInfoList(list);
    }

    @Override
    public void loadCloudImage(List<String> list) {
        if(list == null || list.size() <1){
            return;
        }
    }

    @Override
    public void loadVideo(final List<VideoInfo> list) {
        if (presenter != null) {
            presenter.loadRollImageData();
            presenter.loadRollOverImage();
            presenter.loadCloudData();
        }
        if(list == null || list.size() <1){
            return;
        }
        videoDisposable = Observable.interval(0,list.get(0).getPlayInterval(),TimeUnit.MILLISECONDS)
                .take(list.size())
                .subscribeOn(Schedulers.io())
                .repeat()
                .map(new Function<Long, String>() {

                    @Override
                    public String apply(Long aLong) {
                        int i = aLong.intValue();
                        return list.get(i).getUrl();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {

                    @Override
                    public void accept(String s) {
                        if(binding.vvMain != null ){
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
        if(binding.vvMain != null){
            binding.vvMain.stopPlayback();
        }
        if(!isUserVisible) {
            return;
        }
        binding.vvMain.setVideoPath(url);
        binding.vvMain.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isVideoPlaying = true;
                binding.ivBtvLogo.setVisibility(View.GONE);
                if(isUserVisible) {
                    binding.vvMain.start();
                }
            }
        });
        binding.vvMain.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                binding.ivBtvLogo.setVisibility(View.VISIBLE);
                return true;
            }
        });
        binding.vvMain.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    binding.vvMain.setVideoPath(url);
                    binding.vvMain.start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.d(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onConnected(boolean isConnected) {
        if (presenter != null && binding.vvMain != null && !binding.vvMain.isPlaying() && !isVideoPlaying) {
            presenter.loadVideo();
        }
    }

    @Override
    public void onDisconnect(boolean disConnected) {
        if (disConnected) {
            Toast.makeText(CommonApplication.context, getString(R.string.network_disconnect), Toast.LENGTH_LONG).show();
        }
    }

    private void setZoom() {
        binding.ibtEufonico.setOnFocusChangeListener(this);
        binding.ibtUserManual.setOnFocusChangeListener(this);
        binding.ibtSetting.setOnFocusChangeListener(this);
        binding.ibtApps.setOnFocusChangeListener(this);
        binding.ibtMarket.setOnFocusChangeListener(this);
        binding.ibtGame.setOnFocusChangeListener(this);
        binding.ibtOpportunity.setOnFocusChangeListener(this);
        binding.ibtEufonicBvision.setOnFocusChangeListener(this);
        binding.ibt1.setOnFocusChangeListener(this);
        binding.ibt2.setOnFocusChangeListener(this);
        binding.ibt3.setOnFocusChangeListener(this);
        binding.ibt4.setOnFocusChangeListener(this);
        binding.ibt5.setOnFocusChangeListener(this);
        binding.ibt6.setOnFocusChangeListener(this);
        binding.ibt7.setOnFocusChangeListener(this);
        binding.ibt8.setOnFocusChangeListener(this);
        binding.ibt9.setOnFocusChangeListener(this);
        binding.ibt10.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Zoom.zoomIn09_10(v);
        }
    }

    private int getLevel(){
        String l = (String) SPUtil.get(F.sp.level , "1");
        return Integer.parseInt(l);
    }
}
