package com.erika.i3sensorreader;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erikalarsen on 1/12/18.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragmentList = new ArrayList<>();
    List<String> mTitleList = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public SectionsPagerAdapter(FragmentManager fm, Fragment list, Fragment map) {
        super(fm);
        mFragmentList.add(list);
        mFragmentList.add(map);
        mTitleList.add("List");
        mTitleList.add("Map");

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
