package com.wiatec.btv_launcher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.px.common.utils.AppUtil;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.List;

/**
 * Created by PX on 2016-11-19.
 */

public class MenuCustomAdapter extends BaseAdapter {

    private Context context;
    private List<InstalledApp> list;
    private LayoutInflater layoutInflater;

    public MenuCustomAdapter(Context context, List<InstalledApp> list) {
        this.context = context;
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size()+1;
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
            convertView = layoutInflater.inflate(R.layout.item_menu , null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_Menu);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_Menu);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(list != null && position <list.size()){
            InstalledApp installedApp = list.get(position);
            viewHolder.imageView.setImageDrawable(AppUtil.getIcon(installedApp.getAppPackageName()));
            viewHolder.textView.setText(installedApp.getAppName());

        }else {
            viewHolder.imageView.setImageResource(R.drawable.add);
            viewHolder.textView.setText("Add");
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}
