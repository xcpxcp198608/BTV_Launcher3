package com.wiatec.btv_launcher;

import android.os.Environment;
import android.renderscript.Sampler;

/**
 * Created by PX on 2016-10-10.
 */

public final class F {
    public static final class url {
        public static final String updater="http://158.69.229.104:8091/json/ApkUpdateInfo.json";
        public static final String roll_image="http://158.69.229.104:8091/json/RollViewInfo.json";
        public static final String roll_image2="http://158.69.229.104:8091/json/RollViewInfo2.json";
        public static final String video="http://158.69.229.104:8091/json/VideoInfo.json";
        public static final String image="http://158.69.229.104:8091/json/ImageInfo.json";
        public static final String image2="http://158.69.229.104:8091/json/Image2Info.json";
        public static final String splash_image="http://158.69.229.104:8091/json/SplashImageInfo.json";
        public static final String marquee="http://158.69.229.104:8091/json/WordInfo.json";
        public static final String channel="http://158.69.229.104:8091/json/ChannelInfo.json";
        public static final String message1="http://158.69.229.104:8091/json/Message1.json";
        public static final String message="http://158.69.229.104:8091/json/Message.json";
    }

    public static final class path {
        public static final String download= Environment.getExternalStorageDirectory().getAbsolutePath()+"/BLauncher/";
        public static final String video= Environment.getExternalStorageDirectory().getAbsolutePath()+"/BLauncher/btvi3.mp4";
    }

    public static final class sp {
        public static final String name = "launcher";
        public static final String key_video = "video";
    }

    public static final class package_name {
        public static final String btv= "org.xbmc.kodi";
        public static final String setting = "com.android.tv.settings";
        public static final String browser = "com.android.browser";
        public static final String market = "com.px.bmarket";
        public static final String cloud = "com.legacydirect.tvphoto";
        public static final String legacy_antivirus =  "com.legacydirect.security.suite";
        public static final String legacy_privacy =  "com.legacydirect.privacyadvisor";
        public static final String file = "";
    }
}
