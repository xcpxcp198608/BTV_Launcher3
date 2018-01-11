package com.wiatec.btv_launcher.data;

import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.wiatec.btv_launcher.constant.F;
import com.wiatec.btv_launcher.bean.UserLogInfo;

import java.io.IOException;
public class UserLogData {

    public void upload(final UserLogInfo userDataInfo){
        HttpMaster.post(F.url.user_log)
                .param("username", userDataInfo.getUserName())
                .param("ip", (String) SPUtil.get("ip", "1"))
                .param("country", userDataInfo.getCountry())
                .param("city", userDataInfo.getCity())
                .param("mac", userDataInfo.getMac())
                .param("exitTime", userDataInfo.getExitTime())
                .param("stayTime", userDataInfo.getStayTime())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws Exception {

                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
