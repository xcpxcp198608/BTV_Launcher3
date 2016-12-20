package com.wiatec.btv_launcher.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.adapter.OpportunityPagerAdapter;
import com.wiatec.btv_launcher.animator.OpportunityPagerTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-12-06.
 */

public class UserManualActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<View> list;
    private OpportunityPagerAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);
        viewPager = (ViewPager) findViewById(R.id.view_pager_manual);
        int [] resIds = {R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5,
                R.drawable.m6, R.drawable.m7 , R.drawable.m8 };
        list = new ArrayList<>();
        for (int i = 0 ; i < resIds.length ; i ++){
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(resIds[i]).into(imageView);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));
            list.add(imageView);
        }
        adapter = new OpportunityPagerAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true , new OpportunityPagerTransformer());
    }
}
