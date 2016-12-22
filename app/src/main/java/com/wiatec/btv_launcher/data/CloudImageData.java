package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.Application;
import java.io.File;

/**
 * Created by PX on 2016-12-01.
 */

public class CloudImageData implements ICloudImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {
        try{
            File file = Application.getContext().getExternalFilesDir("images");
            if(!file.exists()){
                onLoadListener.onFailure("file not exists");
                return;
            }
            File [] files = file.listFiles();
            onLoadListener.onSuccess(files);
        }catch (Exception e){
            e.printStackTrace();
            onLoadListener.onFailure(e.getMessage());
        }
    }
}
