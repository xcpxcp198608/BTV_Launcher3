package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.adapter.ChannelGrideAdapter;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.presenter.BasePresenter;
import com.wiatec.btv_launcher.presenter.PinentryPresenter;

import java.util.List;

/**
 * Created by patrick on 2016/12/29.
 */

public class PinentryActivity extends BaseActivity<IPinentryActivity ,PinentryPresenter> implements IPinentryActivity{

    private GridView gridView;
    private ChannelGrideAdapter channelGrideAdapter;

    @Override
    protected PinentryPresenter createPresenter() {
        return new PinentryPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinentry);
        gridView = (GridView) findViewById(R.id.grid_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadChannel("country" , "Pinentry" , "name");
    }

    @Override
    public void loadChannel(final List<ChannelInfo> list) {
        channelGrideAdapter = new ChannelGrideAdapter(Application.getContext() ,list);
        gridView.setAdapter(channelGrideAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChannelInfo channelInfo = list.get(position);
                //Logger.d(channelInfo.toString());
                if("live".equals(channelInfo.getType())){
                    Intent intent = new Intent(PinentryActivity.this , PlayActivity.class);
                    intent.putExtra("url",channelInfo.getUrl());
                    startActivity(intent);
                }else{

                }
            }
        });
        gridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Zoom.zoomIn10_11(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
