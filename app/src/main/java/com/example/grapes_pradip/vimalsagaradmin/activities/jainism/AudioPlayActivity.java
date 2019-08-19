package com.example.grapes_pradip.vimalsagaradmin.activities.jainism;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.grapes_pradip.vimalsagaradmin.common.CommonURL;
import com.example.jean.jcplayer.JcPlayerView;

public class AudioPlayActivity extends AppCompatActivity {

    JcPlayerView jcplayer_audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play);
        jcplayer_audio = findViewById(R.id.jcplayer_audio);

        String audio = getIntent().getStringExtra("audio");

        try {
            Log.e("audio","--------"+CommonURL.Main_url + "static/jainaismaudio/" + audio);
            jcplayer_audio.playAudio(CommonURL.Main_url + "static/jainaismaudio/" + audio.replaceAll(" ","%20"), "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jcplayer_audio.kill();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
