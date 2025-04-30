package com.ngdat.mymusic.Activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.MusicService;

import java.util.ArrayList;

import android.widget.ImageView;

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

    // Binding MusicService to PlayMusicActivity
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            serviceBound = true;

            // Khi đã kết nối, mới bắt đầu phát nhạc
            if (baiHatList.size() > 0) {
                BaiHatYeuThich baiHat = baiHatList.get(position);
                startPlaying(baiHat.getLinkBaiHat());
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
        img_Song = findViewById(R.id.img_song);  // Đảm bảo ID trong XML là img_song
    }

    public ArrayList<BaiHatYeuThich> getBaiHatList() {
        return baiHatList;
    }
    private void init() {
        // Get the first song details
        if (baiHatList.size() > 0) {
            BaiHatYeuThich baiHat = baiHatList.get(position);
            txtSongName.setText(baiHat.getTenBaiHat());
            txtSingerName.setText(baiHat.getCaSi());
            Glide.with(this)
                    .load(baiHat.getHinhBaiHat())
                    .placeholder(R.drawable.no_music)
                    .error(R.drawable.iconfloatingactionbutton)
                    .into(img_Song);  // imgSong là một đối tượng ImageView
//            startPlaying(baiHat.getLinkBaiHat());
        }

        btnPlay.setOnClickListener(v -> {
            if (serviceBound) {
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
        if (position < baiHatList.size() - 1) {
            position++;
        } else {
            position = 0; // Loop to first song
        }
        BaiHatYeuThich baiHat = baiHatList.get(position);
        startPlaying(baiHat.getLinkBaiHat());
        txtSongName.setText(baiHat.getTenBaiHat());
        txtSingerName.setText(baiHat.getCaSi());
    }

    private void playPrevious() {
        if (position > 0) {
            position--;
        } else {
            position = baiHatList.size() - 1; // Loop to last song
        }
        BaiHatYeuThich baiHat = baiHatList.get(position);
        startPlaying(baiHat.getLinkBaiHat());
        txtSongName.setText(baiHat.getTenBaiHat());
        txtSingerName.setText(baiHat.getCaSi());
    }

    private void startPlaying(String link) {
        if (serviceBound) {
            musicService.playSong(link);
            btnPlay.setImageResource(R.drawable.iconpause);
        }
    }

    private void GetDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            BaiHatYeuThich baiHat = intent.getParcelableExtra("cakhuc");
            if (baiHat != null) {
                baiHatList.add(baiHat);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Gửi intent kèm đường link để phát nhạc
        BaiHatYeuThich baiHat = baiHatList.size() > 0 ? baiHatList.get(position) : null;
        if (baiHat != null) {
            Intent startIntent = new Intent(this, MusicService.class);
            startIntent.putExtra("linkBaiHat", baiHat.getLinkBaiHat());
            Log.d("PlayMusicActivity", "Starting service with link: " + baiHat.getLinkBaiHat());
            startService(startIntent); // Đảm bảo phát nhạc ngay khi bắt đầu
        }

        // Rồi mới bind
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