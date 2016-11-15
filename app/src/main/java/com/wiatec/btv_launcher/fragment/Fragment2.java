package com.wiatec.btv_launcher.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.presenter.Fragment2Presenter;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by PX on 2016-11-12.
 */

public class Fragment2 extends BaseFragment<IFragment2, Fragment2Presenter> implements IFragment2, View.OnFocusChangeListener {
    @BindView(R.id.bt_b1)
    Button bt_B1;
    @BindView(R.id.bt_b2)
    Button bt_B2;
    @BindView(R.id.bt_b3)
    Button bt_B3;
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.iv_bvision)
    ImageView iv_Bvision;
    @BindView(R.id.ibt_ad_r1)
    ImageButton ibt_AdR1;
    @BindView(R.id.ibt_ad_r2)
    ImageButton ibt_AdR2;
    @BindView(R.id.ibt_ad_r3)
    ImageButton ibt_AdR3;
    @BindView(R.id.ibt_bvision)
    ImageButton ibt_Bvision;
    @BindView(R.id.ibt_qishi)
    ImageButton ibt_Qishi;

    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (presenter != null) {
                presenter.loadData();
            }
        } else {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                iv_Bvision.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        setZoom();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            iv_Bvision.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Fragment2Presenter createPresenter() {
        return new Fragment2Presenter(this);
    }

    @Override
    public void loadChannel(final List<ChannelInfo> list) {
        //Logger.d(list.toString());
        bt_B1.setText(list.get(0).getChannelName());
        bt_B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(list, 0);
            }
        });
        bt_B2.setText(list.get(1).getChannelName());
        bt_B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(list, 1);
            }
        });
        bt_B3.setText(list.get(2).getChannelName());
        bt_B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(list, 2);
            }
        });
    }

    @Override
    public void loadImage2(final List<ImageInfo> list) {
//        Logger.d(list.toString());
        Glide.with(getContext()).load(list.get(0).getUrl()).placeholder(R.drawable.anti_virus_icon).into(ibt_AdR1);
        Glide.with(getContext()).load(list.get(1).getUrl()).placeholder(R.drawable.anti_virus_icon).into(ibt_AdR2);
        Glide.with(getContext()).load(list.get(2).getUrl()).placeholder(R.drawable.anti_virus_icon).into(ibt_AdR3);
        ibt_AdR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(0).getLink())));
            }
        });
        ibt_AdR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(1).getLink())));
            }
        });
        ibt_AdR3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(list.get(2).getLink())));
            }
        });
    }

    private void playVideo(List<ChannelInfo> list, int position) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(list.get(position).getChannelUrl());
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    iv_Bvision.setVisibility(View.GONE);
                    mp.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.reset();
                    return false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setZoom() {
        ibt_Bvision.setOnFocusChangeListener(this);
        bt_B1.setOnFocusChangeListener(this);
        bt_B2.setOnFocusChangeListener(this);
        bt_B3.setOnFocusChangeListener(this);
        ibt_Qishi.setOnFocusChangeListener(this);
        ibt_AdR1.setOnFocusChangeListener(this);
        ibt_AdR2.setOnFocusChangeListener(this);
        ibt_AdR3.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            Zoom.zoomIn10_11(v);
        }
    }
}
