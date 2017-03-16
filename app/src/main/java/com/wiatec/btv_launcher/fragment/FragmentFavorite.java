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

import com.wiatec.btv_launcher.Activity.AppSelectActivity;
import com.wiatec.btv_launcher.Activity.MenuActivity;
import com.wiatec.btv_launcher.Activity.Splash1Activity;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.Utils.ApkLaunch;
import com.wiatec.btv_launcher.adapter.MenuCustomAdapter;
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

public class FragmentFavorite extends Fragment {

    @BindView(R.id.gv_Favorite)
    GridView gv_Favorite;

    private static  final String TYPE = "favorite";
    private MenuCustomAdapter menuCustomAdapter;
    private MenuActivity activity;
    private InstalledAppDao installedAppDao;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);
        activity = (MenuActivity) getContext();
        installedAppDao = activity.installedAppDao;
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Observable.just(TYPE)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<InstalledApp>>() {
                    @Override
                    public List<InstalledApp> call(String s) {
                        return installedAppDao.queryDataByType(TYPE);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InstalledApp>>() {
                    @Override
                    public void call(final List<InstalledApp> installedApps) {
                        menuCustomAdapter = new MenuCustomAdapter(activity , installedApps);
                        gv_Favorite.setAdapter(menuCustomAdapter);
                        gv_Favorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(position <installedApps.size()){
                                    String packageName = installedApps.get(position).getAppPackageName();
                                    if("com.wiatec.update".equals(packageName)){
                                        ApkLaunch.launchApkByPackageName(getContext() ,packageName);
                                    }else {
                                        Intent intent = new Intent(activity, Splash1Activity.class);
                                        intent.putExtra("packageName", packageName);
                                        startActivity(intent);
                                    }
                                }else {
                                    Intent intent = new Intent(activity , AppSelectActivity.class);
                                    intent.putExtra("type" ,TYPE);
                                    startActivity(intent);
                                }
                            }
                        });
                        gv_Favorite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        if(menuCustomAdapter != null){
            menuCustomAdapter.notifyDataSetChanged();
        }
    }
}
