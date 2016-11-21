package com.wiatec.btv_launcher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;
import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-11-19.
 */

public class AppSelectAdapter extends BaseAdapter {

    private Context context;
    private List<InstalledApp> list;
    private LayoutInflater layoutInflater;
    private String type;

    public AppSelectAdapter(Context context, List<InstalledApp> list ,String type) {
        this.context = context;
        this.list = list;
        this.type = type;
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
            convertView = layoutInflater.inflate(R.layout.item_app_select ,parent ,false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_app_select);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_app_select);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_app_select);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        InstalledApp installedApp = list.get(position);
        viewHolder.imageView.setImageDrawable(ApkCheck.getInstalledApkIcon(context,installedApp.getAppPackageName()));
        viewHolder.textView.setText(installedApp.getAppName());
        if(type.equals(installedApp.getType())){
            viewHolder.checkBox.setChecked(true);
        }else {
            viewHolder.checkBox.setChecked(false);
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public CheckBox checkBox;
    }
}
