package com.wiatec.btv_launcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.ImageInfo;

import java.util.List;

/**
 * Created by patrick on 2016/12/27.
 */

public class LinkListAdapter extends BaseAdapter {
    private Context context;
    private List<ImageInfo> list;
    private LayoutInflater layoutInflater;

    public LinkListAdapter(Context context, List<ImageInfo> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_channel_type,parent , false);
            viewHolder.tv_Link = (TextView) convertView.findViewById(R.id.tv_link);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_Link.setText(list.get(position).getName());
        return convertView;
    }

    class ViewHolder {
        public TextView tv_Link;
    }
}
