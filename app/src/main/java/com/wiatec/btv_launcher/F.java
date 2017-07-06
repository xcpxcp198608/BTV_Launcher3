package com.wiatec.btv_launcher;

import android.os.Environment;
import android.renderscript.Sampler;

/**
 * constants class
 * Created by PX on 2016-10-10.
 */

public final class F {
    public static final class url {
        public static final String base_url = "http://www.ldlegacy.com:8080/";
//        public static final String base_url = "http://172.27.0.102:8080/";

        public static final String updater = base_url+"control_panel/update/get";

        public static final String video = base_url+"control_panel/video/get";
        public static final String ad_video = base_url+"control_panel/advideo/get";
        public static final String boot_ad_video = base_url+"control_panel/bootadvideo/get";
        public static final String kodi_video = base_url+"control_panel/kodivideo/get";

        public static final String kodi_image = base_url+"control_panel/kodiimage/get";

        public static final String splash_image = base_url+"control_panel/adimage/get";
        public static final String roll_image = base_url+"control_panel/rollimage/get";
        public static final String roll_over_image = base_url+"control_panel/rolloverimage/get";
        public static final String opportunity_image = base_url+"control_panel/opportunityimage/get";
        public static final String manual_image = base_url+"control_panel/manualimage/get";

        public static final String message = base_url+"control_panel/message/get";
        public static final String message1 = base_url+"control_panel/message1/get";
        public static final String push_message = base_url+"LDFAM/get";

        public static final String login = base_url+"control_panel/user1/login";
        public static final String register = base_url+"control_panel/user1/register";
        public static final String login_repeat_check = base_url+"control_panel/user1/checkRepeat";
        public static final String reset_p = base_url+"control_panel/user1/requestUpdateP";
        public static final String upload_data = base_url+"control_panel/userdata/save";

        public static final String eufonico = "http://142.4.216.91:8280/";
        public static final String ld_support="http://www.ldlegacy.com:8081/html/ld_support.html";
        public static final String ld_opportunity="http://www.ldlegacy.com:8081/html/ld_opportunity.html";
        public static final String guide_register="http://www.ldlegacy.com:8081/html/guide_register.html";

        public static final String live_play = "http://www.ldlegacy.com:8083/update/com.wiatec.bplay.apk";
    }

    public static final class url_eu {
        public static final String base_url = "http://appotaeu.gobeyondtv.co:8080/";
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
        public static final String guide_register="http://appotaeu.gobeyondtv.co:8081/html/guide_register.html";
    }

    public static final class path {
        public static final String download= Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/Android/data/com.wiatec.btv_launcher/files/download/";
        public static final String ad_video= download+"btvad.mp4";
        public static final String boot_ad_video= download+"btvbootad.mp4";
        public static final String logcat= Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/Android/data/com.wiatec.btv_launcher/files/logcat/";
        public static final String kodi_image_path = Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/Android/data/org.xbmc.kodi/files/.kodi/addons/skin.beyondtv/backgrounds/";
        public static final String kodi_video_path = Environment.getExternalStorageDirectory().getAbsolutePath()
        +"/Android/data/org.xbmc.kodi/files/.kodi/addons/skin.beyondtv/extras/Intro/";

        public static final String live_play = "http://appota.gobeyondtv.co:8083/update/com.wiatec.bplay.apk";
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
        public static final String live_net = "com.livenet.iptv";
        public static final String show_box = "com.tdo.showbox";
        public static final String tv_house = "com.fanshi.tvvideo";
        public static final String mx_player = "com.mxtech.videoplayer.ad";
        public static final String terrarium_tv = "com.nitroxenon.terrarium";
        public static final String popcom = "pct.droid";
    }

    public static final class file_name {
        public static final String live_play= "com.wiatec.bplay.apk";
    }
}
