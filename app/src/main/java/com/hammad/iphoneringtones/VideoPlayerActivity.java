package com.hammad.iphoneringtones;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.potyvideo.library.AndExoPlayerView;

import java.util.HashMap;

public class VideoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        AndExoPlayerView andExoPlayerView = findViewById(R.id.andExoPlayerView);
        HashMap<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("foo", "bar");
        andExoPlayerView.setSource("http://rentole.pk/RentoleProductImages/7806/VID_20211229_165729__PL71B0RYS4C7.mp4", extraHeaders);
    }
}