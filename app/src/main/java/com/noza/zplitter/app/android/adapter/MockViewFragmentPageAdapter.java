package com.noza.zplitter.app.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.noza.zplitter.app.android.fragment.LoginFragment;

/**
 * Created by omiwrench on 2016-01-28.
 */
public class MockViewFragmentPageAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 3;
    private static final String[] tabTitles = {"You", "History", "Friends"};
    private Context context;

    public MockViewFragmentPageAdapter(FragmentManager manager, Context context){
        super(manager);
        this.context = context;
    }

    @Override
    public int getCount(){
        return PAGE_COUNT;
    }
    @Override
    public Fragment getItem(int position){
        switch(position){
            case 0: return LoginFragment.newInstance();
            case 1: return LoginFragment.newInstance();
            case 2: return LoginFragment.newInstance();
            default: throw new IllegalArgumentException("Login view tabs do not have a tab number " + position);
        }
    }
    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }
}
