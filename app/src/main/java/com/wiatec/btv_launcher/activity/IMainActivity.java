package com.wiatec.btv_launcher.Activity;

import com.wiatec.btv_launcher.bean.Message1Info;
import com.wiatec.btv_launcher.bean.UpdateInfo;
import com.wiatec.btv_launcher.bean.VideoInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-14.
 */

public interface IMainActivity {
    void loadUpdate (UpdateInfo updateInfo);
    void loadVideo (VideoInfo videoInfo);
    void loadMessage1 (List<Message1Info> list);
}
