package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.adapter.OpportunityImageAdapter;
import com.wiatec.btv_launcher.adapter.UserManualImageAdapter;
import com.wiatec.btv_launcher.animator.OpportunityPagerTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-12-06.
 */

public class UserManualActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private List<View> list;
    private UserManualImageAdapter adapter;
    private  String language;
    private Spinner spinner;
    private int [] englishResIds = {R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5,
            R.drawable.m6, R.drawable.m7 , R.drawable.m8 };
    private int [] spanishResIds = {R.drawable.ms1, R.drawable.ms2, R.drawable.ms3, R.drawable.ms4, R.drawable.ms5,
            R.drawable.ms6, R.drawable.ms7 ,};
    private String [] languages ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);
        viewPager = (ViewPager) findViewById(R.id.view_pager_manual);
        spinner = (Spinner) findViewById(R.id.spinner);
        language = getIntent().getStringExtra("language");
        languages = getResources().getStringArray(R.array.language);

    }


    @Override
    protected void onStart() {
        super.onStart();
        selectLanguage(languages);
    }

    private void selectLanguage (String [] languages){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(UserManualActivity.this ,
                android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item , languages);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){

                }else {
                    if (position == 1) {
                        language = "english";
                    } else if (position == 2) {
                        language = "spanish";
                    }
                    spinner.setVisibility(View.GONE);
                    showManual();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void showManual(){
        int[] resIds;
        if("spanish".equals(language)){
            resIds = spanishResIds;
        }else {
            resIds = englishResIds;
        }
        list = new ArrayList<>();
        for (int resId : resIds){
            ImageView imageView = new ImageView(this);
            Glide.with(this).load(resId).into(imageView);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.MATCH_PARENT));
            list.add(imageView);
        }
        adapter = new UserManualImageAdapter(list);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true , new OpportunityPagerTransformer());
        viewPager.setVisibility(View.VISIBLE);
    }

}
