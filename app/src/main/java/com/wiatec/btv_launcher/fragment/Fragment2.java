package com.wiatec.btv_launcher.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.wiatec.btv_launcher.Activity.AppSelectActivity;
import com.wiatec.btv_launcher.Activity.FMPlayActivity;
import com.wiatec.btv_launcher.Activity.LoginActivity;
import com.wiatec.btv_launcher.Activity.MainActivity;
import com.wiatec.btv_launcher.Activity.PlayActivity;
import com.wiatec.btv_launcher.Activity.Splash1Activity;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.Utils.SystemConfig;
import com.wiatec.btv_launcher.adapter.ChannelGrideAdapter;
import com.wiatec.btv_launcher.adapter.LinkListAdapter;
import com.wiatec.btv_launcher.adapter.RollImageAdapter;
import com.wiatec.btv_launcher.adapter.RollOverViewAdapter1;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ChannelTypeInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.custom_view.RollOverView;
import com.wiatec.btv_launcher.presenter.Fragment2Presenter;

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

public class Fragment2 extends BaseFragment<IFragment2, Fragment2Presenter> implements IFragment2, View.OnFocusChangeListener, View.OnClickListener {
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
    private boolean isShow = true;
    private RollOverViewAdapter1 rollOverViewAdapter1;
    private boolean rollOverStart = false;
    private MainActivity activity;
    private boolean isF2VisibleToUser = false;

    @Override
    protected Fragment2Presenter createPresenter() {
        return new Fragment2Presenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        ButterKnife.bind(this, view);
        if (SystemConfig.isNetworkConnected(activity)) {
            presenter.bind();
            presenter.loadChannelType();
        }
        installedAppDao = InstalledAppDao.getInstance(Application.getContext());
        isShow = (boolean) SPUtils.get(getContext(), "isShow", true);
        lvCountry.setNextFocusRightId(R.id.gv_channel);
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
            isF2VisibleToUser = true;
            isShow = (boolean) SPUtils.get(getContext(), "isShow", true);
            if (isShow) {
                showWarning();
            }
            if (!isLoaded) {
                presenter.bind();
                presenter.loadChannelType();
            }
            if (isLoaded) {
                presenter.showChannel("country", "BVISION", "name");
            }
            if (presenter != null && !rollOverStart) {
                presenter.loadRollImage();
            }
            if(presenter != null){
                presenter.loadChannel();
            }
        }else{
            isF2VisibleToUser = false;
            if(rollOverView != null){
                rollOverView.stop();
            }
        }
    }

    private void showWarning() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext() ,R.style.dialog).create();
        alertDialog.show();
        alertDialog.setCancelable(false);

        Window window = alertDialog.getWindow();
        if (window == null) {
            return;
        }
        window.setContentView(R.layout.dialog_warning_f2);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        Button btCancel = (Button) window.findViewById(R.id.bt_cancel);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.put(getContext(), "isShow", false);
                alertDialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.put(getContext(), "isShow", true);
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        setZoom();
        ibtC1.setOnClickListener(this);
        showCustomShortCut(ibtC2, "c2");
        showCustomShortCut(ibtC3, "c3");
        showCustomShortCut(ibtC4, "c4");
        showCustomShortCut(ibtC5, "c5");
        showCustomShortCut(ibtC6, "c6");
        showCustomShortCut(ibtC7, "c7");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null && isF2VisibleToUser) {
            presenter.loadRollImage();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(rollOverView != null){
            rollOverView.stop();
        }
    }

    @Override
    public void loadImage(final List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        isLoaded = true;
        Glide.with(Application.getContext()).load(list.get(7).getUrl()).placeholder(R.drawable.ld_store_icon).into(ibtLdStore);
        ibtLdStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkByBrowser(list.get(7).getLink());
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
    public void loadRollImage(List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        RollImageAdapter rollImageAdapter = new RollImageAdapter(list);
        rpvMain.setAdapter(rollImageAdapter);
    }

    @Override
    public void loadRollOverImage(final List<ImageInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        rollOverViewAdapter1 = new RollOverViewAdapter1(list);
        rollOverView.setRollViewAdapter(rollOverViewAdapter1);
        rollOverViewAdapter1.setOnItemClickListener(new RollOverViewAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showLinkByBrowser(list.get(position).getLink());
            }
        });
    }

    @Override
    public void loadChannelType(final List<ChannelTypeInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        lvCountry.setBackground(null);
        tvNotice.setVisibility(View.VISIBLE);
        listAdapter = new LinkListAdapter(Application.getContext(), list);
        lvCountry.setAdapter(listAdapter);
        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelTypeInfo channelTypeInfo = list.get(position);
                if (channelTypeInfo.getFlag() == 0) {
                    if ("CHINA".equals(channelTypeInfo.getName()) || "TAIWAN".equals(channelTypeInfo.getName())) {
                        presenter.showChannel("country", channelTypeInfo.getName(), "sequence");
                    } else {
                        presenter.showChannel("country", channelTypeInfo.getName(), "name");
                    }
                } else if (channelTypeInfo.getFlag() == 1) {
                    presenter.showChannel("style", channelTypeInfo.getName(), "name");
                }
            }
        });
        lvCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChannelTypeInfo channelTypeInfo = list.get(position);
                if (channelTypeInfo.getFlag() == 0) {
                    if ("CHINA".equals(channelTypeInfo.getName()) || "TAIWAN".equals(channelTypeInfo.getName())) {
                        presenter.showChannel("country", channelTypeInfo.getName(), "sequence");
                    } else {
                        presenter.showChannel("country", channelTypeInfo.getName(), "name");
                    }
                } else if (channelTypeInfo.getFlag() == 1) {
                    presenter.showChannel("style", channelTypeInfo.getName(), "name");
                }
                Zoom.zoomIn09_10(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void showChannel(final List<ChannelInfo> list) {
        if(list == null || list.size() <1){
            return;
        }
        grideAdapter = new ChannelGrideAdapter(Application.getContext(), list);
        gridView.setAdapter(grideAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelInfo channelInfo = list.get(position);
                isShow = (boolean) SPUtils.get(getContext(), "isShow", true);
                if (isShow) {
                    return;
                }
                if ("link".equals(channelInfo.getType())) {
                    showLinkByBrowser(channelInfo.getUrl());
                } else if ("live".equals(channelInfo.getType())) {
                    Intent intent = new Intent(getContext(), PlayActivity.class);
                    intent.putExtra("url", channelInfo.getUrl());
                    startActivity(intent);
                } else if ("radio".equals(channelInfo.getType())) {
                    Intent intent = new Intent(getContext(), FMPlayActivity.class);
                    intent.putExtra("url", channelInfo.getUrl());
                    startActivity(intent);
                } else if ("app".equals(channelInfo.getType())) {
                    if (ApkCheck.isApkInstalled(getContext(), channelInfo.getUrl())) {
                        ApkLaunch.launchApkByPackageName(getContext(), channelInfo.getUrl());
                    } else {
                        Toast.makeText(getContext(), getString(R.string.download_guide) + " TV2.0+", Toast.LENGTH_SHORT).show();
                        ApkLaunch.launchApkByPackageName(getContext(), F.package_name.market);
                    }
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
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v.getId() == R.id.ibt_c1 || v.getId() == R.id.ibt_ld_store) {
                Zoom.zoomIn10_11(v);
            } else {
                Zoom.zoomIn09_10(v);
            }
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

    @OnClick({R.id.ibt_c1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_c1:
                Intent intent = new Intent(getActivity(), FMPlayActivity.class);
                intent.putExtra("url", "http://142.4.216.91:8280/");
                getContext().startActivity(intent);
                break;
        }
    }
}
