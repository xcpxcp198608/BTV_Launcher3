package com.wiatec.btv_launcher.fragment;

import com.wiatec.btv_launcher.bean.InstalledApp;

import java.util.List;

/**
 * Created by PX on 2016-11-19.
 */

public interface IFragmentFavorite {
    void loadFavorite(List<InstalledApp> list);
}
