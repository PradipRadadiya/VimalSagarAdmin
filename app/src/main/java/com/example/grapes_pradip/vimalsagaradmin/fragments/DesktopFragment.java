package com.example.grapes_pradip.vimalsagaradmin.fragments;

import android.content.Intent;
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
import com.example.grapes_pradip.vimalsagaradmin.activities.MainActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.comment.TablayoutCommentActivity;
import com.example.grapes_pradip.vimalsagaradmin.activities.vichar.SplashContent;


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

    private LinearLayout lin_alerts;
    private LinearLayout lin_comments;
    private LinearLayout lin_banner;
    private LinearLayout lin_vichar;
    private LinearLayout lin_user;

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

        lin_alerts = (LinearLayout) rootview.findViewById(R.id.lin_alerts);
        lin_comments = (LinearLayout) rootview.findViewById(R.id.lin_comments);
        lin_banner = (LinearLayout) rootview.findViewById(R.id.lin_banner);
        lin_vichar = (LinearLayout) rootview.findViewById(R.id.lin_vichar);
        lin_user = (LinearLayout) rootview.findViewById(R.id.lin_user);
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
        lin_alerts.setOnClickListener(this);
        lin_comments.setOnClickListener(this);
        lin_banner.setOnClickListener(this);
        lin_vichar.setOnClickListener(this);
        lin_user.setOnClickListener(this);
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
            case R.id.lin_alerts:
                openAlert();
                break;
            case R.id.lin_comments:
                openComment();
                break;
            case R.id.lin_banner:
                openSlide();
                break;
            case R.id.lin_vichar:
                Intent intent2 = new Intent(getActivity(), SplashContent.class);
                startActivity(intent2);
                break;
            case R.id.lin_user:
                openUser();
                break;
        }
    }

    private void openAlert() {
        Fragment fr = null;
        fr = new Notes();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openUser() {
        Fragment fr = null;
        fr = new UserFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private void openComment() {
        Fragment fr = null;
        fr = new TablayoutCommentActivity();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openSlide() {
        Fragment fr = null;
        fr = new SlideImageFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_content, fr);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
        fr = new EventCategoryFragment();
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
        fr = new QuestionAnswerTabbingFragment();
//        fr = new QuestionAnswerFragment();
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
