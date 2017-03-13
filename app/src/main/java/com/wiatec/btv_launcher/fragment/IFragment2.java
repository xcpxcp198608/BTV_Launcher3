package com.wiatec.btv_launcher.fragment;

import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ChannelTypeInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by patrick on 2016/12/28.
 */

public interface IFragment2 {
    void loadImage(List<ImageInfo> list);
    void loadRollImage(List<ImageInfo> list);
    void loadRollOverImage(List<ImageInfo> list);
    void showChannel(List<ChannelInfo> list);
    void loadChannelType(List<ChannelTypeInfo> list);
}
