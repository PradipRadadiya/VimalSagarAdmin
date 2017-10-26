package com.example.grapes_pradip.vimalsagaradmin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.grapes_pradip.vimalsagaradmin.R;

/**
 * Created by Grapes-Pradip on 2/15/2017.
 */

@SuppressWarnings("ALL")
public class DesktopFragment extends Fragment implements View.OnClickListener {
    private View rootview;
    private LinearLayout lin_info;
    private LinearLayout lin_event;
    private LinearLayout lin_audio;
    private LinearLayout lin_video;
    private LinearLayout lin_thought;
    private LinearLayout lin_gallery;
    private LinearLayout lin_qa;
    private LinearLayout lin_comp;
    private LinearLayout lin_op;
    private LinearLayout lin_bypeople;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.desktop_fragment, container, false);
        findID();
        idClick();
        return rootview;
    }

    private void findID() {
        lin_info = (LinearLayout) rootview.findViewById(R.id.lin_info);
        lin_event = (LinearLayout) rootview.findViewById(R.id.lin_event);
        lin_audio = (LinearLayout) rootview.findViewById(R.id.lin_audio);
        lin_video = (LinearLayout) rootview.findViewById(R.id.lin_video);
        lin_thought = (LinearLayout) rootview.findViewById(R.id.lin_thought);
        lin_gallery = (LinearLayout) rootview.findViewById(R.id.lin_gallery);
        lin_qa = (LinearLayout) rootview.findViewById(R.id.lin_qa);
        lin_comp = (LinearLayout) rootview.findViewById(R.id.lin_comp);
        lin_op = (LinearLayout) rootview.findViewById(R.id.lin_op);
        lin_bypeople = (LinearLayout) rootview.findViewById(R.id.lin_bypeople);
    }

    private void idClick() {
        lin_info.setOnClickListener(this);
        lin_event.setOnClickListener(this);
        lin_audio.setOnClickListener(this);
        lin_video.setOnClickListener(this);
        lin_thought.setOnClickListener(this);
        lin_gallery.setOnClickListener(this);
        lin_qa.setOnClickListener(this);
        lin_comp.setOnClickListener(this);
        lin_op.setOnClickListener(this);
        lin_bypeople.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_info:
                Log.e("lin_info", "------------------" + "click");
                openInformation();
                break;
            case R.id.lin_event:
                Log.e("information", "------------------" + "click");
                openEvent();
                break;
            case R.id.lin_audio:
                Log.e("lin_event", "------------------" + "click");
                openAudio();
                break;
            case R.id.lin_video:
                Log.e("lin_video", "------------------" + "click");
                openVideo();
                break;
            case R.id.lin_thought:
                Log.e("lin_thought", "------------------" + "click");
                openThought();
                break;
            case R.id.lin_gallery:
                Log.e("lin_gallery", "------------------" + "click");
                openGallery();
                break;
            case R.id.lin_qa:
                Log.e("lin_qa", "------------------" + "click");
                openQA();
                break;
            case R.id.lin_comp:
                Log.e("lin_comp", "------------------" + "click");
                openCompition();
                break;
            case R.id.lin_op:
                Log.e("lin_op", "------------------" + "click");
                openOP();
                break;
            case R.id.lin_bypeople:
                Log.e("lin_bypeople", "------------------" + "click");
                openByPeople();
                break;
        }
    }

    private void openInformation() {
        Fragment fr = null;
        fr = new InformationFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openEvent() {
        Fragment fr = null;
        fr = new EventFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openAudio() {
        Fragment fr = null;
        fr = new AudioFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openVideo() {
        Fragment fr = null;
        fr = new VideoFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openThought() {
        Fragment fr = null;
        fr = new ThoughtFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openGallery() {
        Fragment fr = null;
        fr = new GalleryFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openQA() {
        Fragment fr = null;
        fr = new QuestionAnswerFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openCompition() {
        Fragment fr = null;
        fr = new CompetitionFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openOP() {
        Fragment fr = null;
        fr = new OpinionPollFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openByPeople() {
        Fragment fr = null;
        fr = new ByPeopleFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
