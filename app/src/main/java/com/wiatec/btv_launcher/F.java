package com.wiatec.btv_launcher;

import android.os.Environment;
import android.renderscript.Sampler;

/**
 * constants class
 * Created by PX on 2016-10-10.
 */

public final class F {
    public static final class url {
        public static final String updater="http://appota.gobeyondtv.co:8080/launcher/update/get";
        public static final String channel_type="http://appota.gobeyondtv.co:8080/launcher/channeltype/get";
        public static final String channel="http://appota.gobeyondtv.co:8080/launcher/channel/get";

        public static final String video="http://appota.gobeyondtv.co:8080/launcher/video/get";
        public static final String ad_video="http://appota.gobeyondtv.co:8080/launcher/advideo/get";
        public static final String boot_ad_video="http://appota.gobeyondtv.co:8080/launcher/bootadvideo/get";
        public static final String kodi_video="http://appota.gobeyondtv.co:8080/launcher/kodivideo/get";

        public static final String kodi_image="http://appota.gobeyondtv.co:8080/launcher/kodiimage/get";
        public static final String image="http://appota.gobeyondtv.co:8080/launcher/image/get";
        public static final String splash_image="http://appota.gobeyondtv.co:8080/launcher/adimage/get";
        public static final String roll_image="http://appota.gobeyondtv.co:8080/launcher/rollimage/get";
        public static final String roll_over_image="http://appota.gobeyondtv.co:8080/launcher/rolloverimage/get";
        public static final String opportunity_image="http://appota.gobeyondtv.co:8080/launcher/opportunityimage/get";
        public static final String manual_image = "http://appota.gobeyondtv.co:8080/launcher/manualimage/get";

        public static final String message="http://appota.gobeyondtv.co:8080/launcher/message/get";
        public static final String message1="http://appota.gobeyondtv.co:8080/launcher/message1/get";

        public static final String login="http://appota.gobeyondtv.co:8080/launcher/user/login";
        public static final String register="http://appota.gobeyondtv.co:8080/launcher/user/register";
        public static final String login_repeat_check="http://appota.gobeyondtv.co:8080/launcher/user/check";
        public static final String level_check="http://appota.gobeyondtv.co:8080/launcher/user/check_level";
        public static final String upload_data="http://appota.gobeyondtv.co:8080/launcher/userdata/upload";

        public static final String eufonico = "http://142.4.216.91:8280/";
        public static final String ld_support="http://appota.gobeyondtv.co:8081/html/ld_support.html";
        public static final String ld_opportunity="http://appota.gobeyondtv.co:8081/html/ld_opportunity.html";
    }

    public static final class url_eu {
        public static final String updater="http://appotaeu.gobeyondtv.co:8080/launcher/update/get";
        public static final String channel_type="http://appotaeu.gobeyondtv.co:8080/launcher/channeltype/get";
        public static final String channel="http://appotaeu.gobeyondtv.co:8080/launcher/channel/get";

        public static final String video="http://appotaeu.gobeyondtv.co:8080/launcher/video/get";
        public static final String ad_video="http://appotaeu.gobeyondtv.co:8080/launcher/advideo/get";
        public static final String boot_ad_video="http://appotaeu.gobeyondtv.co:8080/launcher/bootadvideo/get";
        public static final String kodi_video="http://appotaeu.gobeyondtv.co:8080/launcher/kodivideo/get";

        public static final String kodi_image="http://appotaeu.gobeyondtv.co:8080/launcher/kodiimage/get";
        public static final String image="http://appotaeu.gobeyondtv.co:8080/launcher/image/get";
        public static final String splash_image="http://appotaeu.gobeyondtv.co:8080/launcher/adimage/get";
        public static final String roll_image="http://appotaeu.gobeyondtv.co:8080/launcher/rollimage/get";
        public static final String roll_over_image="http://appotaeu.gobeyondtv.co:8080/launcher/rolloverimage/get";
        public static final String opportunity_image="http://appotaeu.gobeyondtv.co:8080/launcher/opportunityimage/get";
        public static final String manual_image = "http://appotaeu.gobeyondtv.co:8080/launcher/manualimage/get";

        public static final String message="http://appotaeu.gobeyondtv.co:8080/launcher/message/get";
        public static final String message1="http://appotaeu.gobeyondtv.co:8080/launcher/message1/get";

        public static final String login="http://appotaeu.gobeyondtv.co:8080/launcher/user/login";
        public static final String register="http://appotaeu.gobeyondtv.co:8080/launcher/user/register";
        public static final String login_repeat_check="http://appotaeu.gobeyondtv.co:8080/launcher/user/check";
        public static final String level_check="http://appotaeu.gobeyondtv.co:8080/launcher/user/check_level";
        public static final String upload_data="http://appotaeu.gobeyondtv.co:8080/launcher/userdata/upload";

        public static final String eufonico = "http://142.4.216.91:8280/";
        public static final String ld_support="http://appotaeu.gobeyondtv.co:8081/html/ld_support.html";
        public static final String ld_opportunity="http://appotaeu.gobeyondtv.co:8081/html/ld_opportunity.html";
    }

    public static final class path {
        public static final String download= Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/Android/data/com.wiatec.btv_launcher/files/download/";
        public static final String video= download+"btvi3.mp4";
        public static final String ad_video= download+"btvad.mp4";
        public static final String boot_ad_video= download+"btvbootad.mp4";
        public static final String logcat= Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/Android/data/com.wiatec.btv_launcher/files/logcat/";
        public static final String kodi_image_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/Android/data/org.xbmc.kodi/files/.kodi/addons/skin.beyondtv/backgrounds/";
        public static final String kodi_video_path = Environment.getExternalStorageDirectory().getAbsolutePath()
        +"/Android/data/org.xbmc.kodi/files/.kodi/addons/skin.beyondtv/extras/Intro/";
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
        public static final String bplay = "com.wiatec.bplay";
    }
}
