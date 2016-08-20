package com.app.youwei.myapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by YouWei on 2016/7/21.
 */
public class CollectionPagerAdapter extends FragmentPagerAdapter {

    private String[] tab_title = {"循环事务", "阶段事务"};
    private int[] tab_icon = {R.drawable.ic_rotate_3d_grey600_24dp, R.drawable.ic_shuffle_disabled_grey600_24dp};
    private int[] tab_icon_on = {R.drawable.ic_rotate_3d_white_24dp, R.drawable.ic_shuffle_disabled_white_24dp};

    private ArrayList<Fragment> fragments = new ArrayList<>();

    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new NowFragment());
        fragments.add(new TodayFragment());

    }

    @Override
    public Fragment getItem(int position) {
//        Fragment fragment = null;
//        switch (position) {
//            case 0:
//                fragment = new NowFragment();
//                break;
//            case 1:
//                fragment = new TodayFragment();
//                break;
//            case 2:
//                fragment = new WeekFragment();
//                break;
//        }
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return tab_title.length;
    }

    public int getIcon(int position) {
        return tab_icon[position];
    }

    public int getIcon_on(int position) {
        return tab_icon_on[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_title[position];
    }
}
