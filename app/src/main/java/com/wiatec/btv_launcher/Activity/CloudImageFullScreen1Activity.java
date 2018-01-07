package com.wiatec.btv_launcher.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.px.common.image.ImageMaster;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.custom_view.MultiImage;
import com.wiatec.btv_launcher.presenter.CloudImageFullScreenPresenter;

import java.util.List;


/**
 * Created by patrick on 2017/2/22.
 */

public class CloudImageFullScreen1Activity extends Base1Activity<ICloudImageFullScreenActivity , CloudImageFullScreenPresenter> implements ICloudImageFullScreenActivity {

    private MultiImage multiImage;
    private List<String> mList;
    private int cloudImagePosition;

    @Override
    protected CloudImageFullScreenPresenter createPresenter() {
        return new CloudImageFullScreenPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_image_full_screen);
        multiImage = findViewById(R.id.multi_image);
        cloudImagePosition = getIntent().getIntExtra("cloudImagePosition",0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadImages();
    }

    @Override
    public void loadImages(List<String> list) {
        mList = list;
        showImage(cloudImagePosition);
    }

    private void showImage(int position){
        if (mList != null && mList.size() > 0) {
            if(position <0 || position >=mList.size()){
                return;
            }
            String path = mList.get(position);
            ImageMaster.load(path, multiImage, R.drawable.ld_cloud_icon_3);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT){
            cloudImagePosition -- ;
            if(cloudImagePosition <0){
                cloudImagePosition = mList.size()-1;
            }
            showImage(cloudImagePosition);
        }
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT){
            cloudImagePosition ++;
            if(cloudImagePosition >=mList.size()){
                cloudImagePosition = 0;
            }
            showImage(cloudImagePosition);
        }
        return super.onKeyDown(keyCode, event);
    }
}
