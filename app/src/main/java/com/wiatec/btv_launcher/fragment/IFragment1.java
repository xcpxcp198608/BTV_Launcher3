package com.wiatec.btv_launcher.fragment;

import com.wiatec.btv_launcher.bean.CloudImageInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.bean.RollImageInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;

import java.io.File;
import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public interface IFragment1 {
    void loadImage(List<ImageInfo> list);
    void loadRollImage(List<ImageInfo> list);
    void loadCloudImage(File[] files);
    void loadVideo(List<VideoInfo> list);
}
