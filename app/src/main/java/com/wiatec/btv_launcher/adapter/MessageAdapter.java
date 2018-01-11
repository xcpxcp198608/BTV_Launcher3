package com.wiatec.btv_launcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.px.common.image.ImageMaster;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.MessageInfo;

import java.util.List;

/**
 * Created by PX on 2016-11-15.
 */

public class MessageAdapter extends BaseAdapter {

    private List<MessageInfo> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public MessageAdapter(Context context ,List<MessageInfo> list) {
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
        MessageViewHolder viewHolder = new MessageViewHolder();
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_message ,parent ,false);
            viewHolder.iv_Message_Icon = convertView.findViewById(R.id.iv_message_icon);
            viewHolder.tv_Message_Title = convertView.findViewById(R.id.tv_message_title);
            viewHolder.tv_Message_Content = convertView.findViewById(R.id.tv_message_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (MessageViewHolder) convertView.getTag();
        }
        MessageInfo messageInfo = list.get(position);
        ImageMaster.load(messageInfo.getIcon(), viewHolder.iv_Message_Icon, R.drawable.message_icon);
        viewHolder.tv_Message_Title.setText(messageInfo.getTitle());
        viewHolder.tv_Message_Content.setText(messageInfo.getContent());
        return convertView;
    }

}

