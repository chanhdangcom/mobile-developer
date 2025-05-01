package com.ngdat.mymusic.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ngdat.mymusic.Adapter.CurrentSongHolder;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.MusicService;

import java.util.ArrayList;

public class PlayMusicActivity extends AppCompatActivity {

    private MusicService musicService;
    private boolean serviceBound = false;
    private ArrayList<BaiHatYeuThich> baiHatList = new ArrayList<>();
    private int position = 0;

    private ImageButton btnPlay, btnNext, btnBack;
    private TextView txtSongName, txtSingerName;
    private ImageView img_Song;

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
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            serviceBound = true;

            // Khi đã kết nối, mới bắt đầu phát nhạc hoặc cập nhật nếu có bài hát mới
            if (baiHatList.size() > 0) {
                BaiHatYeuThich baiHat = baiHatList.get(position);
                startPlaying(baiHat);
            } else if (CurrentSongHolder.currentSong != null && !musicService.isPlaying()) {
                // Nếu không có bài hát mới trong Intent và có bài hát hiện tại đang tạm dừng
                updateUI(CurrentSongHolder.currentSong);
                btnPlay.setImageResource(R.drawable.iconplay);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    private void initView() {
        btnPlay = findViewById(R.id.btn_play);
        btnNext = findViewById(R.id.btn_next);
        btnBack = findViewById(R.id.btn_back);
        txtSongName = findViewById(R.id.tv_songName);
        txtSingerName = findViewById(R.id.tv_singerName);
        img_Song = findViewById(R.id.img_song);
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
                } else {
                    musicService.resume();
                    btnPlay.setImageResource(R.drawable.iconpause);
                }
            }
        });

        btnNext.setOnClickListener(v -> playNext());

        btnBack.setOnClickListener(v -> playPrevious());
    }

    private void playNext() {
        if (baiHatList.isEmpty() || !serviceBound || musicService == null) return;
        if (position < baiHatList.size() - 1) {
            position++;
        } else {
            position = 0; // Loop to first song
        }
        BaiHatYeuThich baiHat = baiHatList.get(position);
        startPlaying(baiHat);
        updateUI(baiHat);
    }

    private void playPrevious() {
        if (baiHatList.isEmpty() || !serviceBound || musicService == null) return;
        if (position > 0) {
            position--;
        } else {
            position = baiHatList.size() - 1; // Loop to last song
        }
        BaiHatYeuThich baiHat = baiHatList.get(position);
        startPlaying(baiHat);
        updateUI(baiHat);
    }

    private void startPlaying(BaiHatYeuThich baiHat) {
        if (serviceBound && musicService != null && baiHat != null) {
            musicService.playSong(baiHat);
            btnPlay.setImageResource(R.drawable.iconpause);
            CurrentSongHolder.currentSong = baiHat;
        }
    }

    private void updateUI(BaiHatYeuThich baiHat) {
        txtSongName.setText(baiHat.getTenBaiHat());
        txtSingerName.setText(baiHat.getCaSi());
        Glide.with(this)
                .load(baiHat.getHinhBaiHat())
                .placeholder(R.drawable.no_music)
                .error(R.drawable.iconfloatingactionbutton)
                .into(img_Song);
    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            BaiHatYeuThich newBaiHat = intent.getParcelableExtra("cakhuc");
            if (newBaiHat != null) {
                Log.d("PlayMusicActivity", "GetDataFromIntent: Received song - " + newBaiHat.getTenBaiHat());

                // Kiểm tra xem bài hát mới có phải là bài hát hiện đang phát hay không
                if (CurrentSongHolder.currentSong != null && newBaiHat.getLinkBaiHat().equals(CurrentSongHolder.currentSong.getLinkBaiHat())) {
                    Log.d("PlayMusicActivity", "GetDataFromIntent: Clicked on the currently playing song.");
                    // Không phát lại từ đầu, chỉ cần cập nhật giao diện nút Play dựa trên trạng thái dịch vụ
                    if (serviceBound && musicService != null) {
                        if (musicService.isPlaying()) {
                            btnPlay.setImageResource(R.drawable.iconpause);
                        } else {
                            btnPlay.setImageResource(R.drawable.iconplay);
                        }
                        updateUI(newBaiHat); // Đảm bảo UI được cập nhật đúng
                    }
                    // Không cần startService hoặc cập nhật baiHatList/position
                } else {
                    // Đây là một bài hát mới
                    Log.d("PlayMusicActivity", "GetDataFromIntent: Received a new song.");
                    baiHatList.clear(); // Xóa danh sách cũ để chỉ phát bài hát mới
                    baiHatList.add(newBaiHat);
                    position = 0;
                    CurrentSongHolder.currentSong = newBaiHat;
                    updateUI(newBaiHat);

                    // Nếu service đã bind, phát bài hát mới ngay lập tức
                    if (serviceBound && musicService != null) {
                        startPlaying(newBaiHat);
                        btnPlay.setImageResource(R.drawable.iconpause);
                    } else {
                        // Nếu service chưa bind, service sẽ bắt đầu phát khi onServiceConnected được gọi
                        Intent startIntent = new Intent(this, MusicService.class);
                        startIntent.putExtra("linkBaiHat", newBaiHat.getLinkBaiHat());
                        Log.d("PlayMusicActivity", "Starting service with link: " + newBaiHat.getLinkBaiHat());
                        startService(startIntent);
                    }
                }
            }
        }
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
        }
    }
}