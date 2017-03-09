package com.wiatec.btv_launcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.ChannelInfo;

import java.util.List;

/**
 * Created by patrick on 2016/12/27.
 */

public class ChannelGrideAdapter extends BaseAdapter {
    private Context context;
    private List<ChannelInfo> channelInfos;
    private LayoutInflater layoutInflater;

    public ChannelGrideAdapter(Context context, List<ChannelInfo> channelInfos) {
        this.context = context;
        this.channelInfos = channelInfos;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return channelInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return channelInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView ==null) {
            convertView = layoutInflater.inflate(R.layout.item_channel ,parent ,false);
            viewHolder.iv_Icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_Name = (TextView) convertView.findViewById(R.id.tv_channel);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ChannelInfo channelInfo = channelInfos.get(position);
        Glide.with(context).load(channelInfo.getIcon()).dontAnimate().into(viewHolder.iv_Icon);
        viewHolder.tv_Name.setText(channelInfo.getName());
        return convertView;
    }

    class ViewHolder {
        private ImageView iv_Icon;
        private TextView tv_Name;
    }

}
