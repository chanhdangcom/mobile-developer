package com.ngdat.mymusic.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ngdat.mymusic.Adapter.CurrentSongHolder;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.MusicService;
import com.ngdat.mymusic.Service.MusicService.LocalBinder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PlayMusicActivity extends AppCompatActivity implements MusicService.OnMediaPreparedListener {

    private MusicService musicService;
    private boolean serviceBound = false;
    private ArrayList<BaiHatYeuThich> baiHatList = new ArrayList<>();
    private int position = 0;
    private boolean isRandom = false;
    private boolean isLoop = false;

    private Toolbar toobarPlayNhac;
    private TextView tvCustomTitle, tvSongName, tvSingerName, tvTimeSong, tvTotalTimeSong;
    private ImageView imgSong;
    private SeekBar seekbarSong;
    private ImageButton btnRandom, btnBack, btnPlay, btnNext, btnLapLai;
    private ViewPager viewPagerPlayNhac;

    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        initView();
        GetDataFromIntent();
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        GetDataFromIntent();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            musicService = binder.getService();
            serviceBound = true;
            musicService.setOnMediaPreparedListener(PlayMusicActivity.this); // Set callback

            if (baiHatList.size() > 0) {
                BaiHatYeuThich baiHat = baiHatList.get(position);
                startPlaying(baiHat);
            } else if (CurrentSongHolder.currentSong != null) {
                updateUI(CurrentSongHolder.currentSong);
                updateSeekBar();
                if (musicService.isPlaying()) {
                    btnPlay.setImageResource(R.drawable.iconpause);
                    startUpdateTimeHandler();
                } else {
                    btnPlay.setImageResource(R.drawable.iconplay);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
            musicService.setOnMediaPreparedListener(null); // Clear callback
        }
    };

    private void initView() {
        toobarPlayNhac = findViewById(R.id.toobarPlayNhac);
        tvCustomTitle = findViewById(R.id.tv_customTitle);
        tvSongName = findViewById(R.id.tv_songName);
        tvSingerName = findViewById(R.id.tv_singerName);
        tvTimeSong = findViewById(R.id.tv_timeSong);
        tvTotalTimeSong = findViewById(R.id.tv_totalTimeSong);
        imgSong = findViewById(R.id.img_song);
        seekbarSong = findViewById(R.id.seekbarSong);
        btnRandom = findViewById(R.id.btn_random);
        btnBack = findViewById(R.id.btn_back);
        btnPlay = findViewById(R.id.btn_play);
        btnNext = findViewById(R.id.btn_next);
        btnLapLai = findViewById(R.id.btn_lapLai);
        viewPagerPlayNhac = findViewById(R.id.viewPagerPlayNhac);

        setSupportActionBar(toobarPlayNhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Ẩn chữ "MyMusic"
        toobarPlayNhac.setNavigationIcon(R.drawable.iconback);
        toobarPlayNhac.setNavigationOnClickListener(v -> onBackPressed());
    }

    public ArrayList<BaiHatYeuThich> getBaiHatList() {
        return baiHatList;
    }

    private void init() {
        btnPlay.setOnClickListener(v -> {
            if (serviceBound && musicService != null) {
                if (musicService.isPlaying()) {
                    musicService.pause();
                    btnPlay.setImageResource(R.drawable.iconplay);
                    stopUpdateTimeHandler();
                } else {
                    musicService.resume();
                    btnPlay.setImageResource(R.drawable.iconpause);
                    startUpdateTimeHandler();
                }
            }
        });

        btnNext.setOnClickListener(v -> playNext());

        btnBack.setOnClickListener(v -> playPrevious());

        seekbarSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (serviceBound && musicService != null && fromUser) {
                    musicService.seekTo(progress);
                    tvTimeSong.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopUpdateTimeHandler(); // Dừng cập nhật khi người dùng bắt đầu kéo
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startUpdateTimeHandler(); // Tiếp tục cập nhật khi người dùng thả tay
            }
        });

        btnRandom.setOnClickListener(v -> {
            isRandom = !isRandom;
            if (isRandom) {
                btnRandom.setImageResource(R.drawable.iconshuffled);
            } else {
                btnRandom.setImageResource(R.drawable.iconsuffle);
            }
        });

        btnLapLai.setOnClickListener(v -> {
            isLoop = !isLoop;
            if (isLoop) {
                btnLapLai.setImageResource(R.drawable.iconrepeat);
            } else {
                btnLapLai.setImageResource(R.drawable.iconrepeat);
            }
        });
    }

    private void playNext() {
        if (baiHatList.isEmpty() || !serviceBound || musicService == null) return;
        stopUpdateTimeHandler();
        if (isRandom) {
            position = (int) (Math.random() * baiHatList.size());
        } else {
            if (position < baiHatList.size() - 1) {
                position++;
            } else {
                position = 0; // Lặp lại danh sách
            }
        }
        BaiHatYeuThich baiHat = baiHatList.get(position);
        startPlaying(baiHat);
        updateUI(baiHat);
    }

    private void playPrevious() {
        if (baiHatList.isEmpty() || !serviceBound || musicService == null) return;
        stopUpdateTimeHandler();
        if (isRandom) {
            position = (int) (Math.random() * baiHatList.size());
        } else {
            if (position > 0) {
                position--;
            } else {
                position = baiHatList.size() - 1; // Lặp lại danh sách
            }
        }
        BaiHatYeuThich baiHat = baiHatList.get(position);
        startPlaying(baiHat);
        updateUI(baiHat);
    }

    private void startPlaying(BaiHatYeuThich baiHat) {
        if (serviceBound && musicService != null && baiHat != null) {
            musicService.playSong(baiHat);
            btnPlay.setImageResource(R.drawable.iconpause);
            updateUI(baiHat);
            // updateSeekBar(); // Gọi sau khi onPrepared được gọi
            startUpdateTimeHandler();
        }
    }

    private void updateUI(BaiHatYeuThich baiHat) {
        tvSongName.setText(baiHat.getTenBaiHat());
        tvSingerName.setText(baiHat.getCaSi());
        Glide.with(this)
                .load(baiHat.getHinhBaiHat())
                .placeholder(R.drawable.no_music)
                .error(R.drawable.iconfloatingactionbutton)
                .into(imgSong);

    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            BaiHatYeuThich newBaiHat = intent.getParcelableExtra("cakhuc");
            if (newBaiHat != null) {
                Log.d("PlayMusicActivity", "GetDataFromIntent: Received song - " + newBaiHat.getTenBaiHat());

                if (CurrentSongHolder.currentSong != null && newBaiHat.getLinkBaiHat().equals(CurrentSongHolder.currentSong.getLinkBaiHat())) {
                    Log.d("PlayMusicActivity", "GetDataFromIntent: Clicked on the currently playing song.");
                    updateUI(newBaiHat);
                    updateSeekBar();
                    if (serviceBound && musicService != null && musicService.isPlaying()) {
                        btnPlay.setImageResource(R.drawable.iconpause);
                        startUpdateTimeHandler();
                    } else {
                        btnPlay.setImageResource(R.drawable.iconplay);
                        stopUpdateTimeHandler();
                    }
                } else {
                    Log.d("PlayMusicActivity", "GetDataFromIntent: Received a new song.");
                    baiHatList.clear();
                    baiHatList.add(newBaiHat);
                    position = 0;
                    CurrentSongHolder.currentSong = newBaiHat;
                    updateUI(newBaiHat);

                    if (serviceBound && musicService != null) {
                        startPlaying(newBaiHat);
                    } else {
                        Intent startIntent = new Intent(this, MusicService.class);
                        startIntent.putExtra("cakhuc", newBaiHat); // Truyền đối tượng BaiHatYeuThich
                        startService(startIntent);
                    }
                    // updateSeekBar(); // Gọi sau khi onPrepared
                    // startUpdateTimeHandler(); // Gọi sau khi onPrepared
                }
            }
        }
    }

    @Override
    public void onPrepared(int duration) {
        // Callback từ MusicService khi media đã được chuẩn bị
        Log.d("PlayMusicActivity", "onPrepared: Duration = " + duration);
        updateSeekBar(); // Cập nhật SeekBar và tổng thời gian
        if (serviceBound && musicService != null && musicService.isPlaying()) {
            startUpdateTimeHandler(); // Bắt đầu cập nhật thời gian hiện tại
        }
    }

    private void updateSeekBar() {
        if (serviceBound && musicService != null) {
            int duration = musicService.getDuration();
            seekbarSong.setMax(duration);
            tvTotalTimeSong.setText(formatTime(duration));
            int currentPosition = musicService.getCurrentPosition();
            seekbarSong.setProgress(currentPosition);
            tvTimeSong.setText(formatTime(currentPosition));
        } else {
            seekbarSong.setProgress(0);
            tvTimeSong.setText("0:00");
            tvTotalTimeSong.setText("0:00");
        }
    }

    private void startUpdateTimeHandler() {
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                if (serviceBound && musicService != null && musicService.isPlaying()) {
                    int currentPosition = musicService.getCurrentPosition();
                    seekbarSong.setProgress(currentPosition);
                    tvTimeSong.setText(formatTime(currentPosition));
                }
                handler.postDelayed(this, 1000); // Cập nhật sau mỗi giây
            }
        };
        handler.postDelayed(updateTimeRunnable, 1000);
    }

    private void stopUpdateTimeHandler() {
        if (updateTimeRunnable != null) {
            handler.removeCallbacks(updateTimeRunnable);
        }
    }

    private String formatTime(int milliseconds) {
        long totalSeconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent bindIntent = new Intent(this, MusicService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
            stopUpdateTimeHandler(); // Dừng handler khi Activity dừng
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUpdateTimeHandler(); // Đảm bảo handler dừng khi Activity bị destroy
    }
}