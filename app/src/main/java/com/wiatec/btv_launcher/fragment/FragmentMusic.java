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
import com.wiatec.btv_launcher.sql.InstalledAppDao;
import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.adapter.MenuCustomAdapter;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FragmentMusic extends Fragment {

    private static  final String TYPE = "music";
    private MenuCustomAdapter menuCustomAdapter;
    private MenuActivity activity;
    private InstalledAppDao installedAppDao;
    private GridView gv_Music;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music ,container ,false);
        gv_Music = (GridView) view.findViewById(R.id.gv_Music);
        activity = (MenuActivity) getContext();
        installedAppDao = InstalledAppDao.getInstance(activity);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Observable.just(TYPE)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<InstalledApp>>() {
                    @Override
                    public List<InstalledApp> apply(String s) {
                        return installedAppDao.queryDataByType(TYPE);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<InstalledApp>>() {
                    @Override
                    public void accept(final List<InstalledApp> installedApps) {
                        menuCustomAdapter = new MenuCustomAdapter(activity , installedApps);
                        gv_Music.setAdapter(menuCustomAdapter);
                        gv_Music.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(activity.getLevel() <= 0){
                                    activity.showLimit();
                                    return;
                                }
                                if(position <installedApps.size()){
                                    String packageName = installedApps.get(position).getAppPackageName();
                                    if("com.wiatec.update".equals(packageName)){
                                        AppUtil.launchApp(getContext() ,packageName);
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
                        gv_Music.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
