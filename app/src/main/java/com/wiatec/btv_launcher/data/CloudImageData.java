package com.wiatec.btv_launcher.data;

import com.wiatec.btv_launcher.Application;
import java.io.File;

/**
 * Created by PX on 2016-12-01.
 */

public class CloudImageData implements ICloudImageData {
    @Override
    public void loadData(final OnLoadListener onLoadListener) {

        String path = Application.getContext().getExternalFilesDir("images").getAbsolutePath();
        File file = new File(path);
        if(!file.exists()){
            onLoadListener.onFailure("file not exists");
            return;
        }
        File [] files = file.listFiles();
        onLoadListener.onSuccess(files);
    }
}
