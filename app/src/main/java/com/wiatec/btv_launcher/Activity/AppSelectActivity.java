package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.adapter.AppSelectAdapter;
import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-19.
 */

public class AppSelectActivity extends BaseActivity {

    private List<InstalledApp> list;
    private AppSelectAdapter appSelectAdapter;
    private ListView lv_AppSelect;
    private  String type;
    private InstalledAppDao installedAppDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_select);
        lv_AppSelect = (ListView) findViewById(R.id.lv_app_select);
        type  = getIntent().getStringExtra("type");
        installedAppDao = InstalledAppDao.getInstance(AppSelectActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Observable.just(type)
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
                        appSelectAdapter = new AppSelectAdapter(AppSelectActivity.this ,installedApps ,type);
                        lv_AppSelect.setAdapter(appSelectAdapter);
                        lv_AppSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                InstalledApp installedApp = installedApps.get(position);
                                CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_app_select);
                                if(checkBox.isChecked()){
                                    checkBox.setChecked(false);
                                    installedAppDao.updateData(installedApp ,"");
                                }else {
                                    checkBox.setChecked(true);
                                    installedAppDao.updateData(installedApp ,type);
                                    finish();
                                }

                            }
                        });
                    }
                });
    }
}
