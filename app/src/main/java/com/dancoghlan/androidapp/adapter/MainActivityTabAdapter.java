package com.dancoghlan.androidapp.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dancoghlan.androidapp.fragment.HomeFragment;
import com.dancoghlan.androidapp.fragment.StatisticsFragment;
import com.dancoghlan.androidapp.fragment.ViewRunsCardFragment;

public class MainActivityTabAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTabs;

    public MainActivityTabAdapter(Context context, FragmentManager fragmentManager, int totalTabs) {
        super(fragmentManager);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                ViewRunsCardFragment viewRunsFragment = new ViewRunsCardFragment();
                return viewRunsFragment;
            case 2:
                StatisticsFragment statisticsFragment = new StatisticsFragment();
                return statisticsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

}