package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.adapter.OpportunityPagerAdapter;
import com.wiatec.btv_launcher.animator.OpportunityPagerTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-11-30.
 */

public class OpportunityActivity extends AppCompatActivity {

    private List<View> list;
    private ViewPager viewPager;
    private OpportunityPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        list = new ArrayList<>();
        int [] resIds = new int [] {R.drawable.o1 ,R.drawable.o2  ,R.drawable.o3,R.drawable.o4,R.drawable.o5,
                R.drawable.o6,R.drawable.o7,R.drawable.o8  ,R.drawable.o9 , R.drawable.o10};
        for (int i =0 ; i< resIds.length ;i++){
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(resIds [i]).into(imageView);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));
            list.add(imageView);
        }
        adapter = new OpportunityPagerAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true , new OpportunityPagerTransformer());
    }
}
