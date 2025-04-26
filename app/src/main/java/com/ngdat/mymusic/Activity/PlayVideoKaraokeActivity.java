package com.ngdat.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.ngdat.mymusic.R;

public class PlayVideoKaraokeActivity extends YouTubeBaseActivity {
    YouTubePlayerView mYouTubePlayerView;
    String id = "";
    int REQUEST_VIDEO = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_karaoke);
        mYouTubePlayerView = findViewById(R.id.myYoutubePlayKaraoke);

        Intent intent = getIntent();
        id = intent.getStringExtra("KaraokeVideo");
        mYouTubePlayerView.initialize(KaraokeActivity.API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(id);
                youTubePlayer.setFullscreen(true);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                if (youTubeInitializationResult.isUserRecoverableError()) {
                    youTubeInitializationResult.getErrorDialog(PlayVideoKaraokeActivity.this, REQUEST_VIDEO);
                } else {
                    Toast.makeText(PlayVideoKaraokeActivity.this, "Erross !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO) {
            mYouTubePlayerView.initialize(KaraokeActivity.API_KEY, (YouTubePlayer.OnInitializedListener) PlayVideoKaraokeActivity.this);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
