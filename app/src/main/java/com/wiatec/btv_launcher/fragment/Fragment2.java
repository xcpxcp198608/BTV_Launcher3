package com.wiatec.btv_launcher.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.wiatec.btv_launcher.Activity.AppSelectActivity;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.bean.RollImageInfo;
import com.wiatec.btv_launcher.presenter.Fragment2Presenter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-12.
 */

public class Fragment2 extends BaseFragment<IFragment2, Fragment2Presenter> implements IFragment2, View.OnFocusChangeListener {
    @BindView(R.id.bt_b1)
    Button bt_B1;
    @BindView(R.id.bt_b2)
    Button bt_B2;
    @BindView(R.id.bt_b3)
    Button bt_B3;
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.iv_bvision)
    ImageView iv_Bvision;
    @BindView(R.id.ibt_rabbit)
    ImageButton ibt_Rabbit;
    @BindView(R.id.ibt_add1)
    ImageButton ibt_Add1;
    @BindView(R.id.ibt_add2)
    ImageButton ibt_Add2;
    @BindView(R.id.ibt_add3)
    ImageButton ibt_Add3;
    @BindView(R.id.ibt_browser)
    ImageButton ibt_Browser;
    @BindView(R.id.ibt_security)
    ImageButton ibt_Security;
    @BindView(R.id.ibt_file)
    ImageButton ibt_File;


    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private InstalledAppDao installedAppDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_2, container, false);
        ButterKnife.bind(this, view);
        installedAppDao = InstalledAppDao.getInstance(getContext());
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (presenter != null) {
                presenter.loadData();
            }
        } else {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                iv_Bvision.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        setZoom();

        showCustomShortCut(ibt_Add1 ,"add1");
        showCustomShortCut(ibt_Add2 ,"add2");
        showCustomShortCut(ibt_Add3 ,"add3");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            iv_Bvision.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Fragment2Presenter createPresenter() {
        return new Fragment2Presenter(this);
    }

    @Override
    public void loadChannel(final List<ChannelInfo> list) {
        //Logger.d(list.toString());
        bt_B1.setText(list.get(0).getName());
        bt_B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(list, 0);
            }
        });
        bt_B2.setText(list.get(1).getName());
        bt_B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(list, 1);
            }
        });
        bt_B3.setText(list.get(2).getName());
        bt_B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(list, 2);
            }
        });
    }

    @Override
    public void loadImage2(final List<ImageInfo> list) {
//        Logger.d(list.toString());
        Glide.with(getContext()).load(list.get(0).getUrl()).placeholder(R.drawable.browser_icon).into(ibt_Browser);
        Glide.with(getContext()).load(list.get(1).getUrl()).placeholder(R.drawable.security_icon).into(ibt_Security);
        Glide.with(getContext()).load(list.get(2).getUrl()).placeholder(R.drawable.file_icon).into(ibt_File);
    }


    private void playVideo(List<ChannelInfo> list, int position) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(list.get(position).getUrl());
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    iv_Bvision.setVisibility(View.GONE);
                    mp.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.reset();
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setZoom() {
        bt_B1.setOnFocusChangeListener(this);
        bt_B2.setOnFocusChangeListener(this);
        bt_B3.setOnFocusChangeListener(this);
        ibt_Browser.setOnFocusChangeListener(this);
        ibt_Security.setOnFocusChangeListener(this);
        ibt_File.setOnFocusChangeListener(this);
        ibt_Add1.setOnFocusChangeListener(this);
        ibt_Add2.setOnFocusChangeListener(this);
        ibt_Add3.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            Zoom.zoomIn10_11(v);
        }
    }

    @OnClick({R.id.ibt_browser, R.id.ibt_security, R.id.ibt_file ,R.id.ibt_rabbit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_browser:
                if(ApkCheck.isApkInstalled(getContext(), F.package_name.chrome)){
                    ApkLaunch.launchApkByPackageName(getContext(),F.package_name.chrome);
                }
                break;
            case R.id.ibt_security:
                if(ApkCheck.isApkInstalled(getContext(), F.package_name.legacy_antivirus)){
                    ApkLaunch.launchApkByPackageName(getContext(),F.package_name.legacy_antivirus);
                }
                break;
            case R.id.ibt_file:
                if(ApkCheck.isApkInstalled(getContext(), F.package_name.file)){
                    ApkLaunch.launchApkByPackageName(getContext(),F.package_name.file);
                }
                break;
            case R.id.ibt_rabbit:
                startActivity(new Intent(Intent.ACTION_VIEW ,Uri.parse("http://www.rabb.it")));
                break;
        }
    }

    private void showCustomShortCut (final ImageButton imageButton , final String type){
        Observable.just(type)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, InstalledApp>() {
                    @Override
                    public InstalledApp call(String s) {
                        List<InstalledApp> list = installedAppDao.queryDataByType(s);
                        if(list.size() ==1){
                            return installedAppDao.queryDataByType(s).get(0);
                        }else {
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<InstalledApp>() {
                    @Override
                    public void call(final InstalledApp installedApp) {
                        if (installedApp != null) {
                            imageButton.setImageDrawable(ApkCheck.getInstalledApkIcon(getContext() , installedApp.getAppPackageName()));
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ApkLaunch.launchApkByPackageName(getContext() ,installedApp.getAppPackageName());
                                }
                            });
                            imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    Intent intent = new Intent(getContext() , AppSelectActivity.class);
                                    intent.putExtra("type" ,type);
                                    startActivity(intent);
                                    return true;
                                }
                            });
                        }else {
                            imageButton.setImageResource(R.drawable.add1);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext() , AppSelectActivity.class);
                                    intent.putExtra("type" ,type);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }
}
