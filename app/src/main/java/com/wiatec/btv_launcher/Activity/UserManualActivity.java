package com.wiatec.btv_launcher.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.adapter.UserManualImageAdapter;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.presenter.UserManualPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-12-06.
 */

public class UserManualActivity extends BaseActivity<IUserManualActivity ,UserManualPresenter> implements IUserManualActivity {
    private RollPagerView rpvManual;
    private UserManualImageAdapter adapter;
    private String language;
    private String product;
    private int [] englishResIds = {R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5,
            R.drawable.m6, R.drawable.m7 , R.drawable.m8 };
    private int [] spanishResIds = {R.drawable.ms1, R.drawable.ms2, R.drawable.ms3, R.drawable.ms4, R.drawable.ms5,
            R.drawable.ms6, R.drawable.ms7 ,};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);
        rpvManual = (RollPagerView) findViewById(R.id.rpv_manual);
        showSelectProduct();
    }

    @Override
    protected UserManualPresenter createPresenter() {
        return new UserManualPresenter(this);
    }

    private void showSelectProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserManualActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Select Product");
        builder.setSingleChoiceItems(getResources().getStringArray(R.array.products), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        product = "btvi3";
                        break;
                    case 1:
                        product = "bksound";
                        break;
                    case 2:
                        product = "bkeymo";
                        break;
                }
                if(dialog != null){
                    dialog.dismiss();
                }
                showSelectLanguage();
            }
        });
        builder.show();
    }

    private void showSelectLanguage (){
        AlertDialog.Builder builder = new AlertDialog.Builder(UserManualActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Select Language");
        builder.setSingleChoiceItems(getResources().getStringArray(R.array.languages), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        language = "en";
                        break;
                    case 1:
                        language = "es_us";
                        break;
                }
                if(dialog != null){
                    dialog.dismiss();
                }
                if("btvi3".equals(product)){
                    showBTVi3Manual(language);
                }else{
                    presenter.loadImage(product , language);
                }
            }
        });
        builder.show();
    }

    private void showBTVi3Manual(String language) {
        int[] resIds;
        if("es_us".equals(language)){
            resIds = spanishResIds;
        }else {
            resIds = englishResIds;
        }
        List<ImageInfo> list = new ArrayList<>();
        for (int resId :resIds) {
            String url = "android.resource://" + getPackageName() + "/" +resId;
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setUrl(url);
            list.add(imageInfo);
        }
        adapter = new UserManualImageAdapter(list);
        rpvManual.setAdapter(adapter);
        rpvManual.setHintView(null);
        rpvManual.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadImage(List<ImageInfo> list) {
        adapter = new UserManualImageAdapter(list);
        rpvManual.setAdapter(adapter);
        rpvManual.setHintView(null);
        rpvManual.setVisibility(View.VISIBLE);
    }
}
