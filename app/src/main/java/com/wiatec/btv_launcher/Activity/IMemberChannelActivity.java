package com.wiatec.btv_launcher.Activity;

import com.wiatec.btv_launcher.bean.ChannelInfo;

import java.util.List;

/**
 * Created by patrick on 2017/3/4.
 */

public interface IMemberChannelActivity {
    void loadChannel(List<ChannelInfo> channelInfos);
}
