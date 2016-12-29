package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Activity.IPinentryActivity;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.data.ChannelData;
import com.wiatec.btv_launcher.data.IChannelData;

import java.util.List;

/**
 * Created by patrick on 2016/12/29.
 */

public class PinentryPresenter extends BasePresenter<IPinentryActivity> {

    private IPinentryActivity iPinentryActivity ;
    private IChannelData iChannelData;

    public PinentryPresenter(IPinentryActivity iPinentryActivity) {
        this.iPinentryActivity = iPinentryActivity;
        iChannelData = new ChannelData();
    }

    public void loadChannel(String country){
        if(iChannelData != null){
            iChannelData.showData(country, new IChannelData.OnLoadListener() {
                @Override
                public void onSuccess(List<ChannelInfo> list) {
                    iPinentryActivity.loadChannel(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
