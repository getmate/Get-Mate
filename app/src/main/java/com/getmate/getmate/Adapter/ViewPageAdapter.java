package com.getmate.getmate.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.getmate.getmate.AchievementFragment;
import com.getmate.getmate.InterestFragment;
import com.getmate.getmate.RecentActivityFragment;

/**
 * Created by HP on 23-02-2018.
 */

public class ViewPageAdapter extends FragmentPagerAdapter
{
    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag = null;
        if (position == 0)
        {
            frag = new RecentActivityFragment();


        }
        else if (position == 1)
        {
            frag = new InterestFragment();


        }
        else if (position == 2)
        {
            frag = new AchievementFragment();

        }
        return frag;
    }


    @Override
    public int getCount() {
        return 3;
    }
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "Recent Activity";
        }
        else if (position == 1)
        {
            title = "Interest";
        }
        else if (position == 2)
        {
            title = "Achievement";
        }
        return title;
    }
}
