package com.wiatec.btv_launcher.presenter;

import com.px.common.utils.Logger;
import com.wiatec.btv_launcher.Activity.IRenterActivity;
import com.wiatec.btv_launcher.bean.AuthRentUserInfo;
import com.wiatec.btv_launcher.bean.ResultInfo;
import com.wiatec.btv_launcher.data.IRenterData;
import com.wiatec.btv_launcher.data.RenterData;

/**
 * Created by patrick on 2016/12/29.
 */

public class RenterPresenter extends BasePresenter<IRenterActivity> {

    private IRenterActivity iRenterActivity;
    private IRenterData iRenterData;

    public RenterPresenter(IRenterActivity iRenterActivity) {
        this.iRenterActivity = iRenterActivity;
        iRenterData = new RenterData();
    }

    public void login(AuthRentUserInfo authRentUserInfo){
        try {
            if(iRenterData != null){
                iRenterData.login(authRentUserInfo, new IRenterData.OnLoginListener() {
                    @Override
                    public void onSuccess(ResultInfo<AuthRentUserInfo> resultInfo) {
                        iRenterActivity.onLogin(resultInfo);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
