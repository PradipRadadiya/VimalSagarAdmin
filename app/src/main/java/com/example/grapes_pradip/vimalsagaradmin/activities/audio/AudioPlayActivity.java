package com.example.grapes_pradip.vimalsagaradmin.activities.audio;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grapes_pradip.vimalsagaradmin.R;
import com.example.jean.jcplayer.JcPlayerView;



public class AudioPlayActivity extends AppCompatActivity {
    private JcPlayerView jcplayer_audio;
    private ImageView img_back;
    private TextView txt_header;
    private String audio;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_play);
        img_back= (ImageView) findViewById(R.id.img_back);
        txt_header= (TextView) findViewById(R.id.txt_header);
        txt_header.setText("Play Audio");
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jcplayer_audio.kill();
                onPause();
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        Intent intent=getIntent();
        audio=intent.getStringExtra("audiopath");
        jcplayer_audio = (JcPlayerView) findViewById(R.id.jcplayer_audio);
        jcplayer_audio.playAudio(audio, "music");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jcplayer_audio.kill();
        onPause();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}
