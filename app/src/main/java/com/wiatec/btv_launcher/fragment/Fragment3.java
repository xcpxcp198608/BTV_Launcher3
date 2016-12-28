package com.wiatec.btv_launcher.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.VideoView;

import com.wiatec.btv_launcher.Activity.BaseActivity;
import com.wiatec.btv_launcher.Activity.MenuActivity;
import com.wiatec.btv_launcher.Activity.PlayActivity;
import com.wiatec.btv_launcher.Application;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.adapter.ChannelGrideAdapter;
import com.wiatec.btv_launcher.adapter.LinkListAdapter;
import com.wiatec.btv_launcher.animator.Zoom;
import com.wiatec.btv_launcher.bean.ChannelInfo;
import com.wiatec.btv_launcher.bean.ImageInfo;
import com.wiatec.btv_launcher.presenter.Fragment3Presenter;

import java.util.List;

/**
 * Created by patrick on 2016/12/27.
 */

public class Fragment3 extends BaseFragment<IFragment3 ,Fragment3Presenter> implements IFragment3{
    private GridView gridView;
    private ListView listView;
    private LinkListAdapter linkListAdapter;
    private ChannelGrideAdapter adapter;
    private boolean isShow = false;

    @Override
    protected Fragment3Presenter createPresenter() {
        return new Fragment3Presenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3 ,container ,false);
        gridView = (GridView) view.findViewById(R.id.gv_channel);
        listView = (ListView) view.findViewById(R.id.lv_link);
        presenter.bind();
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(!isShow){
                presenter.bind();
            }
            if(linkListAdapter != null){
                linkListAdapter.notifyDataSetChanged();
            }
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void loadImage2(final List<ImageInfo> list) {
        isShow = true;
        linkListAdapter = new LinkListAdapter(Application.getContext() , list);
        listView.setAdapter(linkListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openLink(list.get(position).getLink());
            }
        });
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Zoom.zoomIn10_11(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void loadChannel(final List<ChannelInfo> list) {
        adapter = new ChannelGrideAdapter(Application.getContext() , list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext() , PlayActivity.class);
                intent.putExtra("url",list.get(position).getUrl());
                startActivity(intent);
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

    private void openLink(String link){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW ,Uri.parse(link)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
