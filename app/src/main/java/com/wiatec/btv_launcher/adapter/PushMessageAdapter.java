package com.wiatec.btv_launcher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.Logger;
import com.wiatec.btv_launcher.bean.PushMessageInfo;

import java.util.List;

/**
 * Created by xuchengpeng on 27/04/2017.
 */

public class PushMessageAdapter extends BaseAdapter {

    private Context context;
    private List<PushMessageInfo> mList;
    private LayoutInflater layoutInflater;

    public PushMessageAdapter(Context context, List<PushMessageInfo> mList) {
        this.context = context;
        this.mList = mList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_message_list , parent ,false);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tv_message);
            viewHolder.ivImg1 = (ImageView) convertView.findViewById(R.id.iv_img1);
            //viewHolder.ivImg2 = (ImageView) convertView.findViewById(R.id.iv_img2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PushMessageInfo pushMessageInfo = mList.get(position % mList.size());
        if("Sponsor".equals(pushMessageInfo.getUserName())){
            viewHolder.tvUserName.setTextColor(Color.rgb(0,0,255));
            viewHolder.tvMessage.setTextColor(Color.rgb(0,0,0));
        }else if("Announcement".equals(pushMessageInfo.getUserName())){
            viewHolder.tvUserName.setTextColor(Color.rgb(255,0,0));
            viewHolder.tvMessage.setTextColor(Color.rgb(255,0,0));
        }else{
            viewHolder.tvUserName.setTextColor(context.getResources().getColor(R.color.colorGray1));
            viewHolder.tvMessage.setTextColor(Color.rgb(0,0,0));
        }
        viewHolder.tvUserName.setText(pushMessageInfo.getUserName());
        String time = pushMessageInfo.getTime().substring(0 , pushMessageInfo.getTime().length() -2);
        viewHolder.tvTime.setText(time);
        viewHolder.tvMessage.setText(pushMessageInfo.getMessage());
        Glide.with(context).load(pushMessageInfo.getImg1())
                    .dontAnimate()
                    .into(viewHolder.ivImg1);
        return convertView;
    }

    private static class ViewHolder {
        private TextView tvUserName;
        private TextView tvTime;
        private TextView tvMessage;
        private ImageView ivImg1;
        private ImageView ivImg2;
    }
}
