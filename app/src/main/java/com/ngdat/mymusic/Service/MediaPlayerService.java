package com.ngdat.mymusic.Service;

import static com.ngdat.mymusic.utils.SongLoader.songsList;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ngdat.mymusic.Model.Song;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.utils.MyMediaPlayer;

import java.util.Random;

public class MediaPlayerService extends Service {

    private final MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    private Song currentSong;
    private boolean isShuffle = false;
    private boolean isRepeat = false;
    private static final String CHANNEL_ID = "music_channel";


    @Override
    public void onCreate() {
        super.onCreate();
        setupMediaPlayerCompletionListener();
    }

    private void setupMediaPlayerCompletionListener() {
        mediaPlayer.setOnCompletionListener(mp -> {
            if (isRepeat) {
                playSong(currentSong); // Lặp lại bài hiện tại
            } else {
                Song nextSong = getNextSong(); // Bài kế tiếp
                if (nextSong != null) {
                    playSong(nextSong);
                } else {
                    stopSelf(); // Hết danh sách thì dừng
                }
            }
        });
    }

    private Song getNextSong() {
        int currentIndex = MyMediaPlayer.currentIndex;

        if (isShuffle) {
            Random random = new Random();
            int randomIndex;
            do {
                randomIndex = random.nextInt(songsList.size());
            } while (randomIndex == currentIndex && songsList.size() > 1); // Không lặp lại cùng bài
            MyMediaPlayer.currentIndex = randomIndex;
        } else {
            currentIndex++;
            if (currentIndex >= songsList.size()) {
                return null; // Hết danh sách
            }
            MyMediaPlayer.currentIndex = currentIndex;
        }

        return songsList.get(MyMediaPlayer.currentIndex);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        if (intent != null) {
            String action = intent.getAction();

            if (action != null) {
                switch (action) {
                    case "service_play_song":
                        Song song = (Song) intent.getSerializableExtra("media");
                        if (song != null) {
                            playSong(song);
                        }
                        break;

                    case "service_resume_song":
                        resumeSong();
                        break;

                    case "service_pause_song":
                        pauseSong();
                        break;

                    case "service_next_song":
                        Song nextSong = getNextSong();
                        if (nextSong != null) {
                            playSong(nextSong);
                        }
                        break;

                    case "service_prev_song":
                        Song prevSong = getPreviousSong();
                        if (prevSong != null) {
                            playSong(prevSong);
                        }
                        break;

                    case "service_seekbar_song":
                        int currentPosition = intent.getIntExtra("current position", 0);
                        seekTo(currentPosition);
                        break;

                    case "service_toggle_shuffle":
                        isShuffle = !isShuffle;
                        Log.d("MediaPlayerService", "Shuffle: " + isShuffle);
                        break;

                    case "service_toggle_repeat":
                        isRepeat = !isRepeat;
                        Log.d("MediaPlayerService", "Repeat: " + isRepeat);
                        break;

                }
            }
        }
        return START_NOT_STICKY;
    }

    private Song getPreviousSong() {
        int currentIndex = MyMediaPlayer.currentIndex;

        if (isShuffle) {
            Random random = new Random();
            int randomIndex;
            do {
                randomIndex = random.nextInt(songsList.size());
            } while (randomIndex == currentIndex && songsList.size() > 1);
            MyMediaPlayer.currentIndex = randomIndex;
        } else {
            currentIndex--;
            if (currentIndex < 0) {
                return null; // Đầu danh sách
            }
            MyMediaPlayer.currentIndex = currentIndex;
        }
        return songsList.get(MyMediaPlayer.currentIndex);
    }

    private void playSong(Song song) {
        try {
            mediaPlayer.reset();
            currentSong = song;
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            Log.d("MediaPlayerService", "Playing song: " + song.getTitle());
            Intent intent = new Intent("ACTION_SONG_CHANGED");
            sendBroadcast(intent);
            showNotification(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pauseSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            Log.d("MediaPlayerService", "Song paused");
            showNotification(false);
        }
    }

    private void resumeSong() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            Log.d("MediaPlayerService", "Song resumed");
            showNotification(true);
        }
    }

    private void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    private void showNotification(boolean isPlaying) {
        // Nút Previous
        Intent prevIntent = new Intent(this, MediaPlayerService.class);
        prevIntent.setAction("service_prev_song");
        PendingIntent prevPendingIntent = PendingIntent.getService(
                this, 2, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Nút Play/Pause
        Intent playPauseIntent = new Intent(this, MediaPlayerService.class);
        playPauseIntent.setAction(isPlaying ? "service_pause_song" : "service_resume_song");
        PendingIntent playPausePendingIntent = PendingIntent.getService(
                this, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Nút Next
        Intent nextIntent = new Intent(this, MediaPlayerService.class);
        nextIntent.setAction("service_next_song");
        PendingIntent nextPendingIntent = PendingIntent.getService(
                this, 1, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.no_music)
                .setContentTitle(currentSong.getTitle())
                .addAction(R.drawable.baseline_skip_previous_24, "Previous", prevPendingIntent) // 🔹 Previous
                .addAction(isPlaying ? R.drawable.baseline_pause_45 : R.drawable.baseline_play_arrow_50, isPlaying?"Pause":"PLay", playPausePendingIntent) // 🔹 Play/Pause
                .addAction(R.drawable.baseline_skip_next_24, "Next", nextPendingIntent) // 🔹 Next
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();

        startForeground(1, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, "Music Playback", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("Notification for music playback");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}