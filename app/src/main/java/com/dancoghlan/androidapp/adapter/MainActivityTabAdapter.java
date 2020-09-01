package com.dancoghlan.androidapp.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dancoghlan.androidapp.fragment.HomeFragment;
import com.dancoghlan.androidapp.fragment.StatisticsFragment;
import com.dancoghlan.androidapp.fragment.ViewRunsCardFragment;
import com.dancoghlan.androidapp.model.RunContext;
import com.dancoghlan.androidapp.util.RunContextObjectMapper;

import java.util.List;

import static com.dancoghlan.androidapp.util.ProjectConstants.RUN_CONTEXTS_KEY;

public class MainActivityTabAdapter extends FragmentPagerAdapter {
    private Context context;
    private int totalTabs;
    private List<RunContext> runContexts;

    public MainActivityTabAdapter(Context context, FragmentManager fragmentManager, int totalTabs, List<RunContext> runContexts) {
        super(fragmentManager);
        this.context = context;
        this.totalTabs = totalTabs;
        this.runContexts = runContexts;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new ViewRunsCardFragment();
                break;
            case 2:
                fragment = new StatisticsFragment();
                break;
            default:
                return null;
        }

        setBundle(fragment);
        return fragment;
    }

    private void setBundle(Fragment fragment) {
        String json = new RunContextObjectMapper().writeValueAsString(runContexts);
        Bundle bundle = new Bundle();
        bundle.putString(RUN_CONTEXTS_KEY, json);
        fragment.setArguments(bundle);
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

}