package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.SQL.InstalledAppDao;
import com.wiatec.btv_launcher.adapter.MenuItemAdapter;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by PX on 2016-11-12.
 */

public class MenuActivity extends AppCompatActivity {

    private MenuItemAdapter menuItemAdapter;
    private InstalledAppDao sqliteDao;
    private GridView gv_Menu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        sqliteDao = InstalledAppDao.getInstance(MenuActivity.this);
        gv_Menu = (GridView) findViewById(R.id.gv_Menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<InstalledApp>>() {
                    @Override
                    public List<InstalledApp> call(String s) {
                        return sqliteDao.queryData();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<InstalledApp>>() {
                    @Override
                    public void call(final List<InstalledApp> installedApps) {
                        menuItemAdapter = new MenuItemAdapter(MenuActivity.this , installedApps);
                        gv_Menu.setAdapter(menuItemAdapter);
                        gv_Menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String packageName = installedApps.get(position).getAppPackageName();
                                Intent intent = new Intent(MenuActivity.this , SplashActivity.class);
                                intent.putExtra("packageName" ,packageName);
                                startActivity(intent);
                            }
                        });
                        gv_Menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Zoom.zoomIn10_11(view);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(menuItemAdapter!=null){
            menuItemAdapter.notifyDataSetChanged();
        }
    }

}
