package com.example.grapes_pradip.vimalsagaradmin.activities.comment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.grapes_pradip.vimalsagaradmin.R;

public class TablayoutCommentActivity extends Fragment {
    private TabLayout tab_layout;
    private ViewPager pager;
    private View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.comment_tab_activity, container, false);
        findViewid();
        init();
        return rootview;
    }

    private void init() {
        tab_layout.addTab(tab_layout.newTab().setText("Information"));
        tab_layout.addTab(tab_layout.newTab().setText("Event"));
        tab_layout.addTab(tab_layout.newTab().setText("Thought"));
        tab_layout.addTab(tab_layout.newTab().setText("Audio"));
        tab_layout.addTab(tab_layout.newTab().setText("Video"));
        tab_layout.addTab(tab_layout.newTab().setText("ByPeople"));

        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_layout.setTabTextColors(ContextCompat.getColorStateList(getActivity(), R.color.white));
        tab_layout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.indicator));
        final PagerAdapter adapter = new PagerAdapter
                (getFragmentManager(), tab_layout.getTabCount());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        //noinspection deprecation
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void findViewid() {
        tab_layout = rootview.findViewById(R.id.tab_layout);
        pager = rootview.findViewById(R.id.pager);
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TablayoutCommentActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }*/
}
