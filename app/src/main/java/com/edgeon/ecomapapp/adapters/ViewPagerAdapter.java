package com.edgeon.ecomapapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.edgeon.ecomapapp.fragments.Issues;
import com.edgeon.ecomapapp.fragments.MyIssues;
import com.edgeon.ecomapapp.fragments.MySolutions;
import com.edgeon.ecomapapp.fragments.Solutions;

/**
 * Created by SAQ on 10/12/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MyIssues();
        } else if (position == 1){
            return new MySolutions();
        } else if (position == 2){
            return new Issues();
        } else {
            return new Solutions();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "MY ISSUES";
            case 1:
                return "MY SOLUTIONS";
            case 2:
                return "ISSUES";
            case 3:
                return "SOLUTIONS";
            default:
                return null;
        }
    }
}
