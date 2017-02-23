package com.wiatec.btv_launcher.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.databinding.ActivityCloudImageFullScreenBinding;
import com.wiatec.btv_launcher.presenter.CloudImageFullScreenPresenter;
import java.io.File;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by patrick on 2017/2/22.
 */

public class CloudImageFullScreenActivity extends BaseActivity<ICloudImageFullScreenActivity , CloudImageFullScreenPresenter> implements ICloudImageFullScreenActivity {

    private ActivityCloudImageFullScreenBinding binding;
    private File[] mFiles;
    private int cloudImagePosition;

    @Override
    protected CloudImageFullScreenPresenter createPresenter() {
        return new CloudImageFullScreenPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cloud_image_full_screen);
        cloudImagePosition = getIntent().getIntExtra("cloudImagePosition",0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadImages();
    }

    @Override
    public void loadImages(final File[] files) {
        mFiles = files;
        showImage(cloudImagePosition);
    }

    private void showImage(int position){
        if (mFiles != null && mFiles.length > 0) {
            if(position <0 || position >=mFiles.length){
                return;
            }
            String path = mFiles [position].getAbsolutePath();
            Glide.with(CloudImageFullScreenActivity.this)
                    .load(path)
                    .placeholder(R.drawable.ld_cloud_icon_3)
                    .error(R.drawable.ld_cloud_icon_3)
                    .dontAnimate()
                    .into(binding.ivFull);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT){
            cloudImagePosition -- ;
            if(cloudImagePosition <0){
                cloudImagePosition = mFiles.length-1;
            }
            showImage(cloudImagePosition);
        }
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT){
            cloudImagePosition ++;
            if(cloudImagePosition >=mFiles.length){
                cloudImagePosition = 0;
            }
            showImage(cloudImagePosition);
        }
        return super.onKeyDown(keyCode, event);
    }
}
