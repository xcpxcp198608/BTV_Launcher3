package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.sql.InstalledAppDao;
import com.wiatec.btv_launcher.adapter.AppSelectAdapter;
import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by PX on 2016-11-19.
 */

public class AppSelectActivity extends BaseActivity {

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
