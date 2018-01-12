package com.wiatec.btv_launcher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.Activity.MenuActivity;
import com.wiatec.btv_launcher.Activity.Splash1Activity;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.sql.InstalledAppDao;
import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.adapter.MenuItemAdapter;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.constant.F;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FragmentAll extends Fragment {
    GridView gvAll;

    private MenuActivity activity;
    private MenuItemAdapter adapter;
    private InstalledAppDao installedAppDao;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all, container, false);
        gvAll = view.findViewById(R.id.gv_All);
        activity = (MenuActivity) getContext();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        installedAppDao = activity.installedAppDao;
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Function<String, List<InstalledApp>>() {
                    @Override
                    public List<InstalledApp> apply(String s) {
                        return installedAppDao.queryData();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<InstalledApp>>() {
                    @Override
                    public void accept(final List<InstalledApp> installedApps) {
                        adapter = new MenuItemAdapter(activity , installedApps);
                        gvAll.setAdapter(adapter);
                        gvAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(activity.getLevel() <= 0){
                                    activity.showLimit();
                                    return;
                                }
                                String packageName = installedApps.get(position).getAppPackageName();
                                if("com.wiatec.update".equals(packageName)){
                                    AppUtil.launchApp(getContext() ,packageName);
                                }else {
                                    String l  = (String) SPUtil.get(F.sp.level, "1");
                                    int level = Integer.parseInt(l);
                                    if(level >=3){
                                        AppUtil.launchApp(getContext() ,packageName);
                                    }else {Intent intent = new Intent(activity, Splash1Activity.class);
                                        intent.putExtra("packageName", packageName);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
                        gvAll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
