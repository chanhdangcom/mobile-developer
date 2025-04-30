package com.ngdat.mymusic.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ngdat.mymusic.R;

import java.io.IOException;

public class MusicService extends Service {

    private static final String TAG = "MusicService";
    private static final String CHANNEL_ID = "music_channel";

    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "Service bound");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        initMediaPlayer();
        createNotificationChannel();
    }

    private void initMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();  // đảm bảo không bị trùng instance
            }

            mediaPlayer = new MediaPlayer();
            Log.d(TAG, "MediaPlayer initialized");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                );
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

        } catch (Exception e) {
            Log.e(TAG, "initMediaPlayer: Error initializing MediaPlayer", e);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Music Playback",
                    NotificationManager.IMPORTANCE_LOW
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Đang phát nhạc")
                    .setContentText("Ứng dụng MyMusic")
                    .setSmallIcon(R.drawable.iconfloatingactionbutton)
                    .build();
            startForeground(1, notification);
        }

        String link = intent.getStringExtra("LinkBaiHat");
        if (link != null) {
            Log.d(TAG, "Received link: " + link);
            playSong(link);
        } else {
            Log.w(TAG, "No song link received");
        }
        return START_STICKY;
    }

    public void playSong(String url) {
        Log.d(TAG, "Attempting to play song: " + url);
        try {
            if (mediaPlayer == null) {
                Log.d(TAG, "MediaPlayer is null, initializing");
                initMediaPlayer();
            }

            mediaPlayer.reset();
            Log.d(TAG, "MediaPlayer reset");

            mediaPlayer.setDataSource(this, Uri.parse(url));
            Log.d(TAG, "DataSource set");

            mediaPlayer.setOnPreparedListener(mp -> {
                Log.d(TAG, "MediaPlayer prepared, starting playback");
                mp.start();
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error - what: " + what + ", extra: " + extra);
                return true;
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d(TAG, "MediaPlayer completed");
            });

            mediaPlayer.prepareAsync();
            Log.d(TAG, "prepareAsync called");

        } catch (IOException e) {
            Log.e(TAG, "IOException while setting data source", e);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "IllegalArgumentException while setting data source", e);
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException while setting data source", e);
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d(TAG, "MediaPlayer paused");
        }
    }

    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d(TAG, "MediaPlayer resumed");
        }
    }

    public boolean isPlaying() {
        boolean playing = mediaPlayer != null && mediaPlayer.isPlaying();
        Log.d(TAG, "isPlaying: " + playing);
        return playing;
    }

    public void stopAndRelease() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                Log.d(TAG, "MediaPlayer stopped");
            }
            mediaPlayer.release();
            mediaPlayer = null;
            Log.d(TAG, "MediaPlayer released");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        stopAndRelease();
    }
}
