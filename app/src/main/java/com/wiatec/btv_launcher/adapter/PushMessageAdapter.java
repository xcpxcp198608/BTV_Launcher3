package com.wiatec.btv_launcher.adapter;

import android.content.Context;
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
        return mList.size();
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
        PushMessageInfo pushMessageInfo = mList.get(position);
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_message_list , parent ,false);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tv_message);
            viewHolder.ivImg1 = (ImageView) convertView.findViewById(R.id.iv_img1);
            viewHolder.ivImg2 = (ImageView) convertView.findViewById(R.id.iv_img2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(TextUtils.isEmpty(pushMessageInfo.getImg2())){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    420 , LinearLayout.LayoutParams.WRAP_CONTENT);
            viewHolder.ivImg1.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    0 , LinearLayout.LayoutParams.WRAP_CONTENT);
            viewHolder.ivImg2.setLayoutParams(layoutParams1);
        }
        viewHolder.tvUserName.setText(pushMessageInfo.getUserName());
        String time = pushMessageInfo.getTime().substring(0 , pushMessageInfo.getTime().length() -2);
        viewHolder.tvTime.setText(time);
        viewHolder.tvMessage.setText(pushMessageInfo.getMessage());
        if(!TextUtils.isEmpty(pushMessageInfo.getImg1())){
            Glide.with(context).load(pushMessageInfo.getImg1())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .dontAnimate()
                    .into(viewHolder.ivImg1);
        }
        if(!TextUtils.isEmpty(pushMessageInfo.getImg2())){
            Glide.with(context).load(pushMessageInfo.getImg2())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .dontAnimate()
                    .into(viewHolder.ivImg2);
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvUserName;
        public TextView tvTime;
        public TextView tvMessage;
        public ImageView ivImg1;
        public ImageView ivImg2;
    }
}
