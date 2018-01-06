package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.F;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.Utils.OkHttp.Listener.StringListener;
import com.wiatec.btv_launcher.Utils.OkHttp.OkMaster;
import com.wiatec.btv_launcher.Utils.SPUtils;
import com.wiatec.btv_launcher.bean.UserDataInfo;

import java.io.IOException;

/**
 * Created by patrick on 2017/2/27.
 */

public class UserLogData {

    public void upload(final UserDataInfo userDataInfo){
        OkMaster.post(F.url.user_log)
                .parames("username", userDataInfo.getUserName())
                .parames("ip", (String) SPUtils.get("ip", "1"))
                .parames("country", userDataInfo.getCountry())
                .parames("city", userDataInfo.getCity())
                .parames("mac", userDataInfo.getMac())
                .parames("exitTime", userDataInfo.getExitTime())
                .parames("stayTime", userDataInfo.getStayTime())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {

                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
