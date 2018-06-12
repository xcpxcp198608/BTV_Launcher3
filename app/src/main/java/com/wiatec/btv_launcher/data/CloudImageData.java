package com.wiatec.btv_launcher.data;

import com.px.common.constant.CommonApplication;
import com.wiatec.btv_launcher.Application;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PX on 2016-12-01.
 */

public class CloudImageData implements ICloudImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        try{
            File file = CommonApplication.context.getExternalFilesDir("images");
            if(!file.exists()){
                onLoadListener.onFailure("file not exists");
                return;
            }
            File [] files = file.listFiles();
            if(files == null || files.length <=0){
                return;
            }
            List<String> list = new ArrayList<>();
            for(File file1 : files){
                String path = file1.getAbsolutePath();
                list.add(path);
            }
            if(list == null || list.size() <=0){
                return;
            }
            onLoadListener.onSuccess(list);
        }catch (Exception e){
            e.printStackTrace();
            onLoadListener.onFailure(e.getMessage());
        }
    }
}
