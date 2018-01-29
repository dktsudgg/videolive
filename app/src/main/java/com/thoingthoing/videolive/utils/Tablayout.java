package com.thoingthoing.videolive.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.thoingthoing.videolive.ui.main.Favorite;
import com.thoingthoing.videolive.ui.main.LiveView;
import com.thoingthoing.videolive.ui.main.Ranking;


public class Tablayout extends FragmentStatePagerAdapter {

    private int TabCount;

    public Tablayout(FragmentManager manager, int CountTabs) {

        super(manager);

        this.TabCount = CountTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LiveView tab1 = new LiveView();
                return tab1;

            case 1:
                Ranking tab2 = new Ranking();
                return tab2;

            case 2:
                Favorite tab3 = new Favorite();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}
