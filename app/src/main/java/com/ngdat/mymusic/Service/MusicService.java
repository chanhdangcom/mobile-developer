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

import com.ngdat.mymusic.Model.BaiHatYeuThich;

public class MusicService extends Service {

    private static final String TAG = "MusicService";
    private static final String CHANNEL_ID = "music_channel";

    private final IBinder binder = new LocalBinder();
    static MediaPlayer mediaPlayer;
    private OnMediaPreparedListener onMediaPreparedListener;

    public interface OnMediaPreparedListener {
        void onPrepared(int duration);
    }

    public void setOnMediaPreparedListener(OnMediaPreparedListener listener) {
        this.onMediaPreparedListener = listener;
    }

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

            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                Log.d(TAG, "Playing: " + CurrentSongHolder.currentSong.getTenBaiHat() + ", Duration: " + mp.getDuration());

                if (onMediaPreparedListener != null) {
                    onMediaPreparedListener.onPrepared(mp.getDuration());
                }

                // Cập nhật bài hát hiện tại
                CurrentSongHolder.currentSong = getCurrentPlayingSong(); // Đảm bảo currentSong được cập nhật

                // Gửi Broadcast cho MainActivity (nếu bạn vẫn muốn giữ)
                Intent intent = new Intent("com.ngdat.mymusic.UPDATE_NOW_PLAYING");
                intent.putExtra("songName", CurrentSongHolder.currentSong.getTenBaiHat());
                sendBroadcast(intent);
            });

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

        BaiHatYeuThich song = (BaiHatYeuThich) intent.getSerializableExtra("cakhuc");
        if (song != null) {
            Log.d(TAG, "Received song: " + song.getTenBaiHat());
            CurrentSongHolder.currentSong = song; // Cập nhật ngay khi nhận bài hát
            playSong(song);
        } else if (CurrentSongHolder.currentSong != null) {
            // Nếu service khởi động lại và có bài hát đang phát
            playSong(CurrentSongHolder.currentSong);
        } else {
            Log.w(TAG, "No song data received");
        }

        return START_STICKY;
    }

    public void playSong(BaiHatYeuThich song) {
        Log.d(TAG, "Attempting to play song: " + song.getLinkBaiHat());
        try {
            if (mediaPlayer == null) {
                initMediaPlayer();
            } else {
                mediaPlayer.reset();
            }
            CurrentSongHolder.currentSong = song; // Cập nhật trước khi setDataSource
            mediaPlayer.setDataSource(this, Uri.parse(song.getLinkBaiHat()));
            mediaPlayer.prepareAsync();

        } catch (Exception e) {
            Log.e(TAG, "Error playing song", e);
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

    public BaiHatYeuThich getCurrentPlayingSong() {
        return CurrentSongHolder.currentSong;
    }

    // Phương thức để lấy tổng thời gian của bài hát
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    // Phương thức để lấy vị trí hiện tại của bài hát
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public static class CurrentSongHolder {
        public static BaiHatYeuThich currentSong;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        stopAndRelease();
    }
}