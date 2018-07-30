package com.noza.zplitter.app.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.noza.zplitter.app.android.fragment.LoginFragment;
import com.noza.zplitter.app.android.fragment.RegisterFragment;

/**
 * Created by omiwrench on 2016-01-28.
 */
public class LoginViewFragmentPageAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;
    private static final String[] tabTitles = {"Sign in", "Sign up"};
    private Context context;

    public LoginViewFragmentPageAdapter(FragmentManager manager, Context context){
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
            case 1: return RegisterFragment.newInstance();
            default: throw new IllegalArgumentException("Login view tabs do not have a tab number " + position);
        }
    }
    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }
}
