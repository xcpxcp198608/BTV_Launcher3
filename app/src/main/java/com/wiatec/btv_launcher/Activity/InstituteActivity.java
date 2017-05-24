package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.Utils.ApkLaunch;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by patrick on 23/05/2017.
 * create time : 2:22 PM
 */

public class InstituteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Observable.timer(3000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (ApkCheck.isApkInstalled(InstituteActivity.this, F.package_name.joinme)) {
                            ApkLaunch.launchApkByPackageName(InstituteActivity.this, F.package_name.joinme);
                        }else{
                            Toast.makeText(InstituteActivity.this , getString(R.string.download_guide)+" Joinme",Toast.LENGTH_SHORT).show();
                            ApkLaunch.launchApkByPackageName(InstituteActivity.this, F.package_name.market);
                        }
                        finish();
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
