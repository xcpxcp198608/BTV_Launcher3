package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.adapter.ChannelGrideAdapter;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.presenter.MemberChannelPresenter;

import java.util.List;

/**
 * Created by patrick on 2017/3/4.
 */

public class MemberChannelActivity extends BaseActivity<IMemberChannelActivity , MemberChannelPresenter> implements IMemberChannelActivity {

    private GridView gridView;
    private ChannelGrideAdapter channelGrideAdapter;

    @Override
    protected MemberChannelPresenter createPresenter() {
        return new MemberChannelPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_channel);
        gridView = (GridView) findViewById(R.id.gv_channel);
    }

    @Override
    public void loadChannel(List<ChannelInfo> channelInfos) {
        channelGrideAdapter = new ChannelGrideAdapter(this , channelInfos);
        gridView.setAdapter(channelGrideAdapter);
    }
}
