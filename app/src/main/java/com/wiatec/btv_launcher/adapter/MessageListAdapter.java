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
import com.wiatec.btv_launcher.bean.MessageListInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuchengpeng on 24/04/2017.
 */

public class MessageListAdapter extends BaseAdapter {

    private Context context;
    private List<MessageListInfo> mList;
    private LayoutInflater layoutInflater;

    public MessageListAdapter(Context context, List<MessageListInfo> list) {
        this.context = context;
        this.mList = list;
        layoutInflater = LayoutInflater.from(context);

        mList = new ArrayList<>();
        MessageListInfo messageListInfo = new MessageListInfo();
        messageListInfo.setContent("Which is your favorite movies of all time? Watch them all on the BTVi3. #LoveTVagain http://www.baidu.com");
        messageListInfo.setImg1("http://appota.gobeyondtv.co:8081/image/www1.jpeg");
        mList.add(messageListInfo);
        MessageListInfo messageListInfo1 = new MessageListInfo();
        messageListInfo1.setContent("All-New Legacy Trade-In Trade-Up Program Legacy Direct has launched a Trade-In Trade-Up  program that allows BTV owners to trade in their previous-generation BTV2.0 or Trade-Up ANY streaming device for the All-New BTVi3! +#LegacyTradeInTradeUp #LoveTVagain");
        messageListInfo1.setImg1("http://appota.gobeyondtv.co:8081/image/www2.jpeg");
        messageListInfo1.setImg2("http://appota.gobeyondtv.co:8081/image/www5.jpeg");
        mList.add(messageListInfo1);
        MessageListInfo messageListInfo2 = new MessageListInfo();
        messageListInfo2.setContent("There’s no better way to understand the Legacy vision than to experience it. And there’s no better way to experience Legacy than to attend our biggest event of the year... IMPACT. Register today at www.legacyevents.direct +#LegacyDirect #ShareBtvi3 #Winning #Impact2017");
        messageListInfo2.setImg1("");
        mList.add(messageListInfo2);
        MessageListInfo messageListInfo3 = new MessageListInfo();
        messageListInfo3.setContent("");
        messageListInfo3.setImg1("http://appota.gobeyondtv.co:8081/image/www4.jpeg");
        mList.add(messageListInfo3);
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
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_message_list , parent ,false);
            viewHolder.imageView1 = (ImageView) convertView.findViewById(R.id.iv_message_list1);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_message_list);
            viewHolder.imageView2 = (ImageView) convertView.findViewById(R.id.iv_message_list2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MessageListInfo messageListInfo = mList.get(position);
        viewHolder.textView.setText(messageListInfo.getContent());
        Glide.with(context).load(messageListInfo.getImg1())
                .dontAnimate()
                .into(viewHolder.imageView1);
        Glide.with(context).load(messageListInfo.getImg2())
                .dontAnimate()
                .into(viewHolder.imageView2);
        return convertView;
    }

    class ViewHolder {
        private ImageView imageView1;
        private TextView textView;
        private ImageView imageView2;
    }
}
