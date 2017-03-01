package com.wiatec.btv_launcher.fragment;

import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by patrick on 2016/12/28.
 */

public interface IFragment4 {
    void loadImage(List<ImageInfo> list);
    void loadImage2(List<ImageInfo> list);
    void loadRollImage(List<ImageInfo> list);
    void loadRollOverImage(List<ImageInfo> list);
    void showChannel(List<ChannelInfo> list);
}
