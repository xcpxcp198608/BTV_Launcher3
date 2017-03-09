package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Activity.IMemberChannelActivity;
import com.wiatec.btv_launcher.SQL.ChannelDao;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.data.ChannelData;
import com.wiatec.btv_launcher.data.IChannelData;

import java.util.List;

/**
 * Created by patrick on 2017/3/4.
 */

public class MemberChannelPresenter extends BasePresenter<IMemberChannelActivity> {

    private IMemberChannelActivity iMemberChannelActivity;
    private IChannelData iChannelData;

    public MemberChannelPresenter(IMemberChannelActivity iMemberChannelActivity) {
        this.iMemberChannelActivity = iMemberChannelActivity;
        iChannelData = new ChannelData();
    }

    public void loadChannel(){
        if(iChannelData != null){
            iChannelData.showChannel("country", "MEMBER", "name", new IChannelData.OnLoadListener() {
                @Override
                public void onSuccess(List<ChannelInfo> list) {
                    iMemberChannelActivity.loadChannel(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
