package com.wiatec.btv_launcher.Activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wiatec.btv_launcher.R;
import com.wiatec.btv_launcher.databinding.ActivityMenuBinding;
import com.wiatec.btv_launcher.sql.InstalledAppDao;
import com.wiatec.btv_launcher.adapter.FragmentAdapter;
import com.wiatec.btv_launcher.custom_view.ViewPagerIndicator;
import com.wiatec.btv_launcher.fragment.FragmentAll;
import com.wiatec.btv_launcher.fragment.FragmentFavorite;
import com.wiatec.btv_launcher.fragment.FragmentGame;
import com.wiatec.btv_launcher.fragment.FragmentMusic;
import com.wiatec.btv_launcher.fragment.FragmentVideo;

import java.util.ArrayList;

public class MenuActivity extends BaseActivity {

    private FragmentFavorite fragmentFavorite;
    private FragmentAll fragmentAll;
    private FragmentVideo fragmentVideo;
    private FragmentGame fragmentGame;
    private FragmentMusic fragmentMusic;
    private ArrayList<Fragment> fragmentList;
    public InstalledAppDao installedAppDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMenuBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        installedAppDao = InstalledAppDao.getInstance(MenuActivity.this);
        if(fragmentFavorite == null){
            fragmentFavorite = new FragmentFavorite();
        }
        if(fragmentAll == null){
            fragmentAll = new FragmentAll();
        }
        if(fragmentVideo == null){
            fragmentVideo = new FragmentVideo();
        }
        if(fragmentGame == null){
            fragmentGame = new FragmentGame();
        }
        if(fragmentMusic == null){
            fragmentMusic = new FragmentMusic();
        }
        if(fragmentList ==null){
            fragmentList = new ArrayList<>();
        }
        fragmentList.add(fragmentFavorite);
        fragmentList.add(fragmentAll);
        fragmentList.add(fragmentVideo);
        fragmentList.add(fragmentGame);
        fragmentList.add(fragmentMusic);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
        binding.viewPager.setAdapter(fragmentAdapter);
        String [] titles = {getString(R.string.favorite) ,getString(R.string.all) ,getString(R.string.video)
                ,getString(R.string.game) ,getString(R.string.music)};
        binding.viewpagerIndicator.setItem(7,0f ,0f);
        binding.viewpagerIndicator.setTextTitle(titles ,35 ,0xffa3a2a2 ,0xffffffff ,R.drawable.icon_bg_selected);
        binding.viewpagerIndicator.setPaint("#ffffff" ,2);
        binding.viewpagerIndicator.setShape(ViewPagerIndicator.SHAPE_TRIANGLE);
        binding.viewpagerIndicator.attachViewPager(binding.viewPager ,1);
    }

}
