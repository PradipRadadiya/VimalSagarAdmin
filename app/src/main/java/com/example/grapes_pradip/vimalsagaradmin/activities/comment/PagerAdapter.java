package com.example.grapes_pradip.vimalsagaradmin.activities.comment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class PagerAdapter extends FragmentStatePagerAdapter {
    private final int notabcount;

    public PagerAdapter(FragmentManager supportFragmentManager, int tabCount) {
        super(supportFragmentManager);
        this.notabcount = tabCount;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new InformationCommentFragment();
                return fragment;
            case 1:
                fragment = new EventCommentFragment();
                return fragment;
            case 2:
                fragment = new ThoughtCommentFragment();
                return fragment;
            case 3:
                fragment = new AudioCommentFragment();
                return fragment;
            case 4:
                fragment = new VideoCommentFragment();
                return fragment;
            case 5:
                fragment = new ByPeopleCommentFragment();
                return fragment;

        }
        return null;

    }

    @Override
    public int getCount() {
        return notabcount;
    }
}
