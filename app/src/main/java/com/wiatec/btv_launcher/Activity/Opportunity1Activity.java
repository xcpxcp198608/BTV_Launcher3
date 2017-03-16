package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jude.rollviewpager.RollPagerView;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.adapter.OpportunityImageAdapter;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.presenter.OpportunityPresenter;

import java.util.List;

/**
 * Created by PX on 2016-11-30.
 */

public class Opportunity1Activity extends Base1Activity<IOpportunityActivity , OpportunityPresenter> implements IOpportunityActivity {

    private RollPagerView rpvOpportunity;
    private OpportunityImageAdapter opportunityImageAdapter;

    @Override
    protected OpportunityPresenter createPresenter() {
        return new OpportunityPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity);
        rpvOpportunity = (RollPagerView) findViewById(R.id.rpv_opportunity);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(presenter != null){
            presenter.loadImage();
        }
    }

    @Override
    public void loadImage(List<ImageInfo> list) {
        Logger.d(list.toString());
        opportunityImageAdapter = new OpportunityImageAdapter(list);
        rpvOpportunity.setAdapter(opportunityImageAdapter);
        rpvOpportunity.setHintView(null);
    }
}
