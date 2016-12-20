package com.wiatec.btv_launcher.adapter;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiatec.btv_launcher.bean.InstalledApp;
import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.Utils.ApkCheck;

import java.util.List;

/**
 * Created by PX on 2016/9/1.
 */
public class MenuItemAdapter extends BaseAdapter {

    private Context context;
    private List<InstalledApp> list;
    private LayoutInflater layoutInflater;

    public MenuItemAdapter(Context context, List<InstalledApp> list) {
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
            convertView = layoutInflater.inflate(R.layout.item_menu , null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_Menu);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_Menu);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        InstalledApp installedApp = list.get(position);
        viewHolder.imageView.setImageDrawable(ApkCheck.getInstalledApkIcon(context,installedApp.getAppPackageName()));
        viewHolder.textView.setText(installedApp.getAppName());
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}
