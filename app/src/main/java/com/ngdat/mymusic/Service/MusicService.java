package com.ngdat.mymusic.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.media.session.MediaButtonReceiver;

import com.ngdat.mymusic.Model.BaiHat;
import com.ngdat.mymusic.R;

import java.io.IOException;

public class MusicService extends Service {

    private static final String TAG = "MusicService";
    private static final String CHANNEL_ID = "music_channel";
    static final int NOTIFICATION_ID = 1;

    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;
    private OnMediaPreparedListener onMediaPreparedListener;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

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
        initMediaSession();
        createNotificationChannel();
    }

    private void initMediaSession() {
        mediaSession = new MediaSessionCompat(getBaseContext(), "MusicServiceTag");
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );

        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_STOP
                );
        mediaSession.setPlaybackState(stateBuilder.build());

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
                    showNotification();
                }
            }

            @Override
            public void onPause() {
                super.onPause();
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    updatePlaybackState(PlaybackStateCompat.STATE_PAUSED);
                    showNotification();
                }
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                // Xử lý chuyển bài tiếp theo (nếu có logic)
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                // Xử lý chuyển bài trước đó (nếu có logic)
            }

            @Override
            public void onStop() {
                super.onStop();
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                updatePlaybackState(PlaybackStateCompat.STATE_STOPPED);
                stopForeground(true);
                stopSelf();
            }
        });

        mediaSession.setMetadata(buildMediaMetadata(CurrentSongHolder.currentSong));
        mediaSession.setActive(true);

//        MediaButtonReceiver.setMediaSession(mediaSession);
    }

    private MediaMetadataCompat buildMediaMetadata(BaiHat song) {
        if (song == null) return null;
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getTenBaiHat())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getCaSi())
                // Thêm thông tin khác như album art nếu có
                .build();
    }

    private void updateMediaMetadata(BaiHat song) {
        mediaSession.setMetadata(buildMediaMetadata(song));
    }

    private void updatePlaybackState(int state) {
        stateBuilder.setState(state, mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0, 1.0f);
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    private void initMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
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
                Log.d(TAG, "Playing: " + (CurrentSongHolder.currentSong != null ? CurrentSongHolder.currentSong.getTenBaiHat() : "Unknown") + ", Duration: " + mp.getDuration());
                if (onMediaPreparedListener != null) {
                    onMediaPreparedListener.onPrepared(mp.getDuration());
                }
                updateMediaMetadata(CurrentSongHolder.currentSong);
                updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
                showNotification();

                Intent intent = new Intent("com.ngdat.mymusic.UPDATE_NOW_PLAYING");
                intent.putExtra("songName", CurrentSongHolder.currentSong != null ? CurrentSongHolder.currentSong.getTenBaiHat() : "");
                sendBroadcast(intent);
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                updatePlaybackState(PlaybackStateCompat.STATE_STOPPED);
                stopForeground(true);
                stopSelf();
                // Xử lý khi bài hát kết thúc
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer Error: what=" + what + ", extra=" + extra);
                return false; // Trả về false để gọi OnCompletionListener
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
            channel.setDescription("Controls for music playback");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showNotification() {
        MediaMetadataCompat mediaMetadata = mediaSession.getController().getMetadata();
        PlaybackStateCompat playbackState = mediaSession.getController().getPlaybackState();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(mediaMetadata != null ? mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE) : "Unknown Title")
                .setContentText(mediaMetadata != null ? mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST) : "Unknown Artist")
                .setSmallIcon(R.drawable.iconfloatingactionbutton)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1, 2) // Hiển thị Play/Pause, Next ở chế độ thu gọn
                );

        int playPauseIcon = (playbackState != null && playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) ?
                android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play;
        String playPauseAction = (playbackState != null && playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) ?
                "Pause" : "Play";

        builder.addAction(new NotificationCompat.Action(playPauseIcon, playPauseAction,
                MediaButtonReceiver.buildMediaButtonPendingIntent(this,
                        (playbackState != null && playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) ?
                                PlaybackStateCompat.ACTION_PAUSE : PlaybackStateCompat.ACTION_PLAY)));
        builder.addAction(new NotificationCompat.Action(R.drawable.iconnext, "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)));
        builder.addAction(new NotificationCompat.Action(R.drawable.iconback, "Previous",
                MediaButtonReceiver.buildMediaButtonPendingIntent(this, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)));

        if (mediaMetadata != null && mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART) != null) {
            builder.setLargeIcon(mediaMetadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART));
        } else {
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        }

        Intent openAppIntent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        if (openAppIntent != null) {
            openAppIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE);
            builder.setContentIntent(pendingIntent);
        }

        startForeground(NOTIFICATION_ID, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand called");
        MediaButtonReceiver.handleIntent(mediaSession, intent);

        BaiHat song = (BaiHat) intent.getSerializableExtra("cakhuc");
        if (song != null) {
            Log.d(TAG, "Received song: " + song.getTenBaiHat());
            CurrentSongHolder.currentSong = song;
            playSong(song);
            updateMediaMetadata(song);
        } else if (CurrentSongHolder.currentSong != null) {
            playSong(CurrentSongHolder.currentSong);
            updateMediaMetadata(CurrentSongHolder.currentSong);
        } else {
            Log.w(TAG, "No song data received");
        }

        return START_STICKY;
    }

    public void playSong(BaiHat song) {
        Log.d(TAG, "Attempting to play song: " + song.getLinkBaiHat());
        try {
            if (mediaPlayer == null) {
                initMediaPlayer();
            } else {
                mediaPlayer.reset();
            }
            CurrentSongHolder.currentSong = song;
            mediaPlayer.setDataSource(this, Uri.parse(song.getLinkBaiHat()));
            mediaPlayer.prepareAsync();
            updateMediaMetadata(song);
        } catch (IOException e) {
            Log.e(TAG, "Error playing song", e);
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            updatePlaybackState(PlaybackStateCompat.STATE_PAUSED);
            showNotification();
            Log.d(TAG, "MediaPlayer paused");
        }
    }

    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING);
            showNotification();
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
        stopForeground(true);
        stopSelf();
    }

    public BaiHat getCurrentPlayingSong() {
        return CurrentSongHolder.currentSong;
    }

    public int getDuration() {
        return mediaPlayer != null ? mediaPlayer.getDuration() : 0;
    }

    public int getCurrentPosition() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : 0;
    }

    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public static class CurrentSongHolder {
        public static BaiHat currentSong;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        mediaSession.release();
        stopAndRelease();
    }
}