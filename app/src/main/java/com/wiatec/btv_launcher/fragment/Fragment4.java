package com.wiatec.btv_launcher.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.wiatec.btv_launcher.Activity.AppSelectActivity;
import com.wiatec.btv_launcher.Activity.FMPlayActivity;
import com.wiatec.btv_launcher.Activity.LoginActivity;
import com.wiatec.btv_launcher.Activity.PlayActivity;
import com.wiatec.btv_launcher.Activity.SplashActivity;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.adapter.ChannelGrideAdapter;
import com.wiatec.btv_launcher.adapter.LinkListAdapter;
import com.wiatec.btv_launcher.adapter.RollImageAdapter;
import com.wiatec.btv_launcher.adapter.RollOverViewAdapter;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.custom_view.RollOverView;
import com.wiatec.btv_launcher.presenter.Fragment4Presenter;

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
 * Created by patrick on 2016/12/28.
 */

public class Fragment4 extends BaseFragment<IFragment4, Fragment4Presenter> implements IFragment4 ,View.OnFocusChangeListener ,View.OnClickListener{
    @BindView(R.id.lv_country)
    ListView lvCountry;
    @BindView(R.id.gv_channel)
    GridView gridView;
    @BindView(R.id.ibt_c1)
    ImageButton ibtC1;
    @BindView(R.id.ibt_c2)
    ImageButton ibtC2;
    @BindView(R.id.ibt_c3)
    ImageButton ibtC3;
    @BindView(R.id.ibt_c4)
    ImageButton ibtC4;
    @BindView(R.id.ibt_c5)
    ImageButton ibtC5;
    @BindView(R.id.ibt_c6)
    ImageButton ibtC6;
    @BindView(R.id.ibt_c7)
    ImageButton ibtC7;
    @BindView(R.id.rpv_main)
    RollPagerView rpvMain;
    @BindView(R.id.ibt_ld_store)
    ImageButton ibtLdStore;
    @BindView(R.id.roll_over_view)
    RollOverView rollOverView;
    @BindView(R.id.tv_notice)
    TextView tvNotice;

    private LinkListAdapter listAdapter;
    private ChannelGrideAdapter grideAdapter;
    private boolean isLoaded = false;
    private InstalledAppDao installedAppDao;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isShow = true;
    private RollOverViewAdapter rollOverViewAdapter;
    private boolean rollOverStart = false;

    @Override
    protected Fragment4Presenter createPresenter() {
        return new Fragment4Presenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment6, container, false);
        ButterKnife.bind(this, view);
        presenter.bind();
        installedAppDao = InstalledAppDao.getInstance(Application.getContext());
        sharedPreferences = Application.getContext().getSharedPreferences(F.sp.name , Context.MODE_PRIVATE);
        isShow = sharedPreferences.getBoolean("isShow" , true);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            isShow = sharedPreferences.getBoolean("isShow" , true);
            if(isShow){
                showWarning();
            }
            if(!isLoaded){
                presenter.bind();
            }
            if(isLoaded){
                presenter.showChannel("country" ,"BVISION",null);
            }
            presenter.loadRollImage();
        }else{
            if(rollOverView != null){
                rollOverView.pause();
                rollOverStart = false;
            }
        }
    }

    private void showWarning() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if(window == null){
            return;
        }
        window.setContentView(R.layout.dialog_warning_f4);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        Button btCancel = (Button) window.findViewById(R.id.bt_cancel);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.putBoolean("isShow" ,false);
                editor.commit();
                alertDialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedPreferences.edit();
                editor.putBoolean("isShow" ,true);
                editor.commit();
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setZoom();
        ibtC1.setOnClickListener(this);
        showCustomShortCut(ibtC2 ,"c2");
        showCustomShortCut(ibtC3 ,"c3");
        showCustomShortCut(ibtC4 ,"c4");
        showCustomShortCut(ibtC5 ,"c5");
        showCustomShortCut(ibtC6 ,"c6");
        showCustomShortCut(ibtC7 ,"c7");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter != null){
            presenter.loadRollImage();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(rollOverView != null){
            rollOverView.pause();
            rollOverStart = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(rollOverView != null){
            rollOverView.pause();
            rollOverStart = false;
        }
    }

    @Override
    public void loadImage(final List<ImageInfo> list) {
        isLoaded = true;
        Glide.with(Application.getContext()).load(list.get(7).getUrl()).placeholder(R.drawable.ld_store_icon).into(ibtLdStore);
        ibtLdStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkByBrowser(list.get(7).getLink());
            }
        });
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
    public void loadImage2(final List<ImageInfo> list) {
        if(list == null){
            return;
        }
        lvCountry.setBackground(null);
        tvNotice.setVisibility(View.VISIBLE);
        listAdapter = new LinkListAdapter(Application.getContext() , list);
        lvCountry.setAdapter(listAdapter);
        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageInfo imageInfo = list.get(position);
                if(imageInfo.getQuery_flag() == 0){
                    if("CHINA".equals(imageInfo.getName()) || "TAIWAN".equals(imageInfo.getName())){
                        presenter.showChannel("country" ,imageInfo.getName(),"sequence");
                    }else{
                        presenter.showChannel("country" ,imageInfo.getName(),"name");
                    }
                }else if(imageInfo.getQuery_flag() == 1){
                    presenter.showChannel("style" ,imageInfo.getName(),"name");
                }
            }
        });
        lvCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ImageInfo imageInfo = list.get(position);
                if(imageInfo.getQuery_flag() == 0){
                    if("CHINA".equals(imageInfo.getName()) || "TAIWAN".equals(imageInfo.getName())){
                        presenter.showChannel("country" ,imageInfo.getName(),"sequence");
                    }else{
                        presenter.showChannel("country" ,imageInfo.getName(),"name");
                    }
                }else if(imageInfo.getQuery_flag() == 1){
                    presenter.showChannel("style" ,imageInfo.getName(),"name");
                }
                Zoom.zoomIn09_10(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void loadRollImage(List<ImageInfo> list) {
        RollImageAdapter rollImageAdapter = new RollImageAdapter(list);
        rpvMain.setAdapter(rollImageAdapter);
    }

    @Override
    public void loadRollOverImage(final List<ImageInfo> list) {
        Logger.d(list.toString());
        if(list.size() <=0){
            return;
        }
        if(rollOverStart){
            return;
        }
        rollOverStart = true;
        rollOverViewAdapter = new RollOverViewAdapter(list);
        rollOverView.setRollViewAdapter(rollOverViewAdapter);
        rollOverView.start();
        rollOverViewAdapter.setOnItemClickListener(new RollOverViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showLinkByBrowser(list.get(position).getLink());
            }
        });
        rollOverView.setOffscreenPageLimit(list.size());
        rollOverView.start();
    }

    @Override
    public void showChannel(final List<ChannelInfo> list) {
        grideAdapter = new ChannelGrideAdapter(Application.getContext() ,list);
        gridView.setAdapter(grideAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelInfo channelInfo = list.get(position);
                isShow = sharedPreferences.getBoolean("isShow" , true);
                if(isShow){
                    return;
                }
                if("link".equals(channelInfo.getType())){
                    showLinkByBrowser(channelInfo.getUrl());
                }else if("live".equals(channelInfo.getType())){
                    Intent intent = new Intent(getContext() , PlayActivity.class);
                    intent.putExtra("url",channelInfo.getUrl());
                    startActivity(intent);
                }else if("radio".equals(channelInfo.getType())){
                    Intent intent = new Intent(getContext() , FMPlayActivity.class);
                    intent.putExtra("url",channelInfo.getUrl());
                    startActivity(intent);
                }
            }
        });
        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Zoom.zoomIn09_10(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                                    Intent intent = new Intent(getActivity(), SplashActivity.class);
                                    intent.putExtra("packageName", installedApp.getAppPackageName());
                                    startActivity(intent);
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if(v.getId() == R.id.ibt_c1 || v.getId() == R.id.ibt_ld_store || v.getId() == R.id.ibt_ad_1){
                Zoom.zoomIn10_11(v);
            }else {
                Zoom.zoomIn09_10(v);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibt_c1:
                Intent intent = new Intent(getActivity() , FMPlayActivity.class);
                intent.putExtra("url" , "http://142.4.216.91:8280/");
                getContext().startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void setZoom() {
        ibtLdStore.setOnFocusChangeListener(this);
        ibtC1.setOnFocusChangeListener(this);
        ibtC2.setOnFocusChangeListener(this);
        ibtC3.setOnFocusChangeListener(this);
        ibtC4.setOnFocusChangeListener(this);
        ibtC5.setOnFocusChangeListener(this);
        ibtC6.setOnFocusChangeListener(this);
        ibtC7.setOnFocusChangeListener(this);
    }

}
