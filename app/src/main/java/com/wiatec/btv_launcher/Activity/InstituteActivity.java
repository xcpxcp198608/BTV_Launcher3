package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.R;
import com.px.common.utils.AppUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

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
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        if (AppUtil.isInstalled(F.package_name.joinme)) {
                            AppUtil.launchApp(InstituteActivity.this, F.package_name.joinme);
                        }else{
                            Toast.makeText(InstituteActivity.this , getString(R.string.download_guide)+" Joinme",Toast.LENGTH_SHORT).show();
                            AppUtil.launchApp(InstituteActivity.this, F.package_name.market);
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
