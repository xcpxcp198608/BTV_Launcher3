package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.px.common.utils.SPUtil;
import com.px.common.utils.SysUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.presenter.BasePresenter;

/**
 * Created by PX on 2016-11-14.
 */

public abstract class Base2Activity <V ,T extends BasePresenter> extends AppCompatActivity {

    protected T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        presenter.attachView(this);

        String mac = SysUtil.getWifiMac();
        String ethernetMac = SysUtil.getEthernetMac();
        SPUtil.put(F.sp.mac, mac);
        SPUtil.put(F.sp.ethernet_mac, ethernetMac);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();

    }

    protected abstract T createPresenter();


}
