package com.wiatec.btv_launcher;

import android.os.Environment;
import android.renderscript.Sampler;

/**
 * Created by PX on 2016-10-10.
 */

public final class F {
    public static final class url {
        public static final String updater="http://appota.gobeyondtv.co:8080/Launcher_EN/GetUpdate";
        public static final String ad_video="http://appota.gobeyondtv.co:8081/json/AdVideoInfo.json";
        public static final String boot_ad_video="http://appota.gobeyondtv.co:8081/json/BoootAdVideoInfo.json";
        public static final String video="http://appota.gobeyondtv.co:8080/Launcher_EN/GetVideo";

        public static final String image="http://appota.gobeyondtv.co:8080/Launcher_EN/GetImage";
        public static final String image2="http://appota.gobeyondtv.co:8080/Launcher_EN/GetImage2";
        public static final String splash_image="http://appota.gobeyondtv.co:8080/Launcher_EN/GetAdImage";
        public static final String roll_image="http://appota.gobeyondtv.co:8080/Launcher_EN/GetRollImage";
        public static final String roll_image2="http://appota.gobeyondtv.co:8080/Launcher_EN/GetRollImage2";
        public static final String roll_over_image="http://appota.gobeyondtv.co:8080/Launcher_EN/GetRollOverImage";
        public static final String channel="http://appota.gobeyondtv.co:8080/Launcher_EN/GetChannel";
        public static final String message1="http://appota.gobeyondtv.co:8080/Launcher_EN/GetMessage1";
        public static final String message="http://appota.gobeyondtv.co:8080/Launcher_EN/GetMessage";


        public static final String message1_zh_CN="http://appota.gobeyondtv.co:8081/json/Message1_zh_CN.json";
        public static final String message1_zh_TW="http://appota.gobeyondtv.co:8081/json/Message1_zh_TW.json";
        public static final String message1_es_ES="http://appota.gobeyondtv.co:8081/son/Message1_es_ES.json";
        public static final String message1_es_US="http://appota.gobeyondtv.co:8081/json/Message1_es_US.json";
        public static final String message1_it_IT="http://appota.gobeyondtv.co:8081/json/Message1_it_IT.json";

        public static final String login="http://appota.gobeyondtv.co:8080/Launcher_EN/PinentryLogin";
        public static final String pinentry_login="http://appota.gobeyondtv.co:8080/Launcher_EN/PinentryLogin";

        public static final String upload_hold_time="http://appota.gobeyondtv.co:8080/Launcher_EN/UploadHoldTime";

    }

    public static final class path {
        public static final String download= Environment.getExternalStorageDirectory().getAbsolutePath()+"/BLauncher/";
        public static final String video= Environment.getExternalStorageDirectory().getAbsolutePath()+"/BLauncher/btvi3.mp4";
        public static final String ad_video= Environment.getExternalStorageDirectory().getAbsolutePath()+"/BLauncher/btvad.mp4";
        public static final String boot_ad_video= Environment.getExternalStorageDirectory().getAbsolutePath()+"/BLauncher/btvbootad.mp4";
    }

    public static final class sp {
        public static final String name = "launcher";
        public static final String key_video = "video";

    }

    public static final class package_name {
        public static final String btv= "org.xbmc.kodi";
        public static final String setting = "com.android.tv.settings";
        public static final String market = "com.px.bmarket";
        public static final String cloud = "com.legacydirect.tvphoto";
        public static final String legacy_antivirus =  "com.legacydirect.security.suite";
        public static final String legacy_privacy =  "com.legacydirect.privacyadvisor";
        public static final String file = "com.droidlogic.FileBrower";
        public static final String tvplus = "com.elinkway.tvlive2";
        public static final String joinme = "com.logmein.joinme";
        public static final String spotify = "com.spotify.tv.android";
        public static final String happy_chick = "com.xiaoji.tvbox";
    }
}
