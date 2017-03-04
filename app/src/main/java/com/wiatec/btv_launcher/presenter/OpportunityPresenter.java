package com.wiatec.btv_launcher.presenter;

import com.wiatec.btv_launcher.Activity.IOpportunityActivity;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.data.IOpportunityData;
import com.wiatec.btv_launcher.data.OpportunityData;

import java.util.List;

/**
 * Created by patrick on 2017/3/4.
 */

public class OpportunityPresenter extends BasePresenter<IOpportunityActivity> {

    private IOpportunityActivity iOpportunityActivity;
    private IOpportunityData iOpportunityData;

    public OpportunityPresenter(IOpportunityActivity iOpportunityActivity) {
        this.iOpportunityActivity = iOpportunityActivity;
        iOpportunityData = new OpportunityData();
    }

    public void loadImage(){
        if(iOpportunityData != null){
            iOpportunityData.loadData(new IOpportunityData.OnLoadListener() {
                @Override
                public void onSuccess(List<ImageInfo> list) {
                    iOpportunityActivity.loadImage(list);
                }

                @Override
                public void onFailure(String e) {
                    Logger.d(e);
                }
            });
        }
    }
}
