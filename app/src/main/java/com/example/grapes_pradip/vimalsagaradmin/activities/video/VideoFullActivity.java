package com.example.grapes_pradip.vimalsagaradmin.activities.video;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.example.grapes_pradip.vimalsagaradmin.R;

import static com.example.grapes_pradip.vimalsagaradmin.activities.video.VideoDetailActivity.video_play_url;



public class VideoFullActivity extends AppCompatActivity {

    private VideoView videoView;
    // Declare variables
    private ProgressBar progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplay);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        String url = intent.getStringExtra("videourl");
        Log.e("Uri", "-----------" + url);
        videoView = (VideoView) findViewById(R.id.videoView);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    VideoFullActivity.this);
            mediacontroller.setAnchorView(videoView);
          
            // Get the URL from String VideoURL
            Uri video = Uri.parse(video_play_url);
            videoView.setMediaController(mediacontroller);
            videoView.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoView.requestFocus();
        progressbar.setVisibility(View.VISIBLE);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                progressbar.setVisibility(View.GONE);
                videoView.start();

                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                            progressbar.setVisibility(View.VISIBLE);
                        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                            progressbar.setVisibility(View.GONE);
                        return false;
                    }
                });


//                mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//                    @Override
//                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
//
//                        if (percent!=0){
//                            progressbar.setVisibility(View.VISIBLE);
//
//                        }else {
//                            progressbar.setVisibility(View.GONE);
//                        }
//                        Log.e("percent","--------------"+percent);
//                    }
//                });
//
//                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//                    @Override
//                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                        progressbar.setVisibility(View.VISIBLE);
//                    }
//                });
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                finish();
            }
        });


        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                progressbar.setVisibility(View.GONE);
                return false;
            }
        });

    }
}
