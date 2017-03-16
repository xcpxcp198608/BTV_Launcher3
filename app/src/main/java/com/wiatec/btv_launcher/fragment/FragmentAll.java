package com.wiatec.btv_launcher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wiatec.btv_launcher.Activity.MenuActivity;
import com.wiatec.btv_launcher.Activity.Splash1Activity;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.adapter.MenuItemAdapter;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-19.
 */

public class FragmentAll extends Fragment {
    @BindView(R.id.gv_All)
    GridView gv_All;

    private MenuActivity activity;
    private MenuItemAdapter adapter;
    private InstalledAppDao installedAppDao;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        ButterKnife.bind(this, view);
        activity = (MenuActivity) getContext();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        installedAppDao = activity.installedAppDao;
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<InstalledApp>>() {
                    @Override
                    public List<InstalledApp> call(String s) {
                        return installedAppDao.queryData();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InstalledApp>>() {
                    @Override
                    public void call(final List<InstalledApp> installedApps) {
                        adapter = new MenuItemAdapter(activity , installedApps);
                        gv_All.setAdapter(adapter);
                        gv_All.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String packageName = installedApps.get(position).getAppPackageName();
                                if("com.wiatec.update".equals(packageName)){
                                    ApkLaunch.launchApkByPackageName(getContext() ,packageName);
                                }else {
                                    Intent intent = new Intent(activity, Splash1Activity.class);
                                    intent.putExtra("packageName", packageName);
                                    startActivity(intent);
                                }
                            }
                        });
                        gv_All.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Zoom.zoomIn09_10(view);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.notifyDataSetChanged();
        }
    }
}
