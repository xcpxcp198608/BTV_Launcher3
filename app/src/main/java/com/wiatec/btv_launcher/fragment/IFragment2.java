package com.wiatec.btv_launcher.fragment;

import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.RollImageInfo;

import java.nio.channels.Channel;
import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public interface IFragment2 {
    void loadChannel (List<ChannelInfo> list);
    void loadImage2 (List<ImageInfo> list);

}
