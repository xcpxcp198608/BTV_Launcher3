package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.wiatec.btv_launcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PX on 2016-11-14.
 */

public class UserGuideActivity extends AppCompatActivity {
    @BindView(R.id.ibt_wifi)
    ImageButton ibt_Wifi;
    @BindView(R.id.ibt_resolution)
    ImageButton ibt_Resolution;
    @BindView(R.id.ibt_time_zone)
    ImageButton ibt_TimeZone;
    @BindView(R.id.ibt_btv_setup)
    ImageButton ibt_BtvSetup;
    @BindView(R.id.ibt_app_market)
    ImageButton ibt_AppMarket;
    @BindView(R.id.ibt_faq)
    ImageButton ibt_Faq;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ibt_wifi, R.id.ibt_resolution, R.id.ibt_time_zone, R.id.ibt_btv_setup, R.id.ibt_app_market, R.id.ibt_faq})
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ibt_wifi:
//                int resId = R.raw.wifi;
//                Intent intent = new Intent(UserGuideActivity.this, PlayActivity.class);
//                intent.putExtra("resId", resId);
//                startActivity(intent);
//                break;
//            case R.id.ibt_resolution:
//                int resId1 = R.raw.resolution;
//                Intent intent1 = new Intent(UserGuideActivity.this, PlayActivity.class);
//                intent1.putExtra("resId", resId1);
//                startActivity(intent1);
//                break;
//            case R.id.ibt_time_zone:
//                int resId2 = R.raw.timezone;
//                Intent intent2 = new Intent(UserGuideActivity.this, PlayActivity.class);
//                intent2.putExtra("resId", resId2);
//                startActivity(intent2);
//                break;
//            case R.id.ibt_btv_setup:
//                int resId3 = R.raw.btvsetup;
//                Intent intent3 = new Intent(UserGuideActivity.this, PlayActivity.class);
//                intent3.putExtra("resId", resId3);
//                startActivity(intent3);
//                break;
//            case R.id.ibt_app_market:
//                int resId4 = R.raw.appmarket;
//                Intent intent4 = new Intent(UserGuideActivity.this, PlayActivity.class);
//                intent4.putExtra("resId", resId4);
//                startActivity(intent4);
//                break;
//            case R.id.ibt_faq:
//                int resId5 = R.raw.faq;
//                Intent intent5 = new Intent(UserGuideActivity.this, PlayActivity.class);
//                intent5.putExtra("resId", resId5);
//                startActivity(intent5);
//                break;
//            default:
//                break;
//        }
    }
}
