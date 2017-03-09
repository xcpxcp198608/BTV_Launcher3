package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Activity.IUserManualActivity;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.data.IManualData;
import com.wiatec.btv_launcher.data.ManualData;

import java.util.List;

/**
 * Created by patrick on 2017/3/6.
 */

public class UserManualPresenter extends BasePresenter<IUserManualActivity> {

    private IUserManualActivity iUserManualActivity;
    private IManualData iManualData;

    public UserManualPresenter(IUserManualActivity iUserManualActivity) {
        this.iUserManualActivity = iUserManualActivity;
        iManualData = new ManualData();
    }

    public void loadImage(String product ,String language){
        if(iManualData != null){
            iManualData.loadData(new IManualData.OnLoadListener() {
                @Override
                public void onSuccess(List<ImageInfo> list) {
                    iUserManualActivity.loadImage(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            } ,product, language );
        }
    }
}
