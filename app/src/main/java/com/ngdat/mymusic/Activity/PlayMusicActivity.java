package com.ngdat.mymusic.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.ngdat.mymusic.Adapter.ViewPagerPlayMusicAdapter;
import com.ngdat.mymusic.Fragment.FragmentCDMusic;
import com.ngdat.mymusic.Fragment.FragmentPlayDanhSachBaiHat;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Callback;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class PlayMusicActivity extends AppCompatActivity {

    public static ArrayList<BaiHatYeuThich> baiHatList = new ArrayList<>();

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private ImageButton btnPlay, btnBack, btnNext, btnLap, btnRandom;
    private TextView txtTimeSong, txtTotalTime;
    private SeekBar mSeekBar;

    private FragmentCDMusic mFragmentCDMusic;
    private FragmentPlayDanhSachBaiHat mFragmentPlayDanhSachBaiHat;
    public static ViewPagerPlayMusicAdapter mViewPagerPlayMusicAdapter;

    private MediaPlayer mMediaPlayer;
    private Handler mHandler;

    private TextView txtSongName, txtSingerName;
    private ImageView imgSong;
    private int position = 0;
    private boolean repeat = false;
    private boolean checkRandom = false;
    private boolean next = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        // Cho phép gọi các hàm mạng trên luồng chính (tạm thời, không khuyến khích)
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mHandler = new Handler();

        initView();        // Ánh xạ view
        init();            // Thiết lập toolbar, fragments, và play nhạc đầu tiên
        eventClick();      // Gắn các sự kiện click
        GetDataFromItent();

        // Khởi tạo MediaPlayer lần đầu
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(this, Uri.parse(baiHatList.get(0).getLinkBaiHat()));
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            Log.e("MediaPlayer", "Error preparing MediaPlayer: " + e.getMessage());
        }

        getSupportActionBar().setTitle(baiHatList.get(0).getTenBaiHat());
        txtSongName.setText(baiHatList.get(0).getTenBaiHat());        // 🆕
        txtSingerName.setText(baiHatList.get(0).getCaSi());           // 🆕
        TimeSong();
        UpdateTime();
        btnPlay.setImageResource(mMediaPlayer.isPlaying() ? R.drawable.iconpause : R.drawable.iconplay);
    }


    // Ánh xạ các view trong layout
    private void initView() {
        mToolbar = findViewById(R.id.toobarPlayNhac);
        mSeekBar = findViewById(R.id.seekbarSong);
        mViewPager = findViewById(R.id.viewPagerPlayNhac);
        txtTimeSong = findViewById(R.id.tv_timeSong);
        txtTotalTime = findViewById(R.id.tv_totalTimeSong);
        btnBack = findViewById(R.id.btn_back);
        btnLap = findViewById(R.id.btn_lapLai);
        btnPlay = findViewById(R.id.btn_play);
        btnNext = findViewById(R.id.btn_next);
        btnRandom = findViewById(R.id.btn_random);
        // 🆕 Ánh xạ thêm 3 TextView mới
        txtSongName = findViewById(R.id.tv_songName);
        txtSingerName = findViewById(R.id.tv_singerName);
        imgSong = findViewById(R.id.img_song); // 🆕 ánh xạ ImageView
    }

    // Thiết lập toolbar, adapter cho ViewPager và play bài đầu tiên
    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set cho thành phần toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false); //bỏ title trên toolbar
        mToolbar.setNavigationIcon(R.drawable.iconback);
        mToolbar.setNavigationOnClickListener(v -> {
            finish(); // Đóng Activity
            stopAndReleaseMediaPlayer(); // Dừng và giải phóng MediaPlayer
            baiHatList.clear(); // Xóa danh sách bài hát
        });
//        mToolbar.setTitleTextColor(Color.WHITE);

        mFragmentCDMusic = new FragmentCDMusic();
        mFragmentPlayDanhSachBaiHat = new FragmentPlayDanhSachBaiHat();

        mViewPagerPlayMusicAdapter = new ViewPagerPlayMusicAdapter(getSupportFragmentManager());
        mViewPagerPlayMusicAdapter.addFragment(mFragmentPlayDanhSachBaiHat);
        mViewPagerPlayMusicAdapter.addFragment(mFragmentCDMusic);
        mViewPager.setAdapter(mViewPagerPlayMusicAdapter);
        mFragmentCDMusic = (FragmentCDMusic) mViewPagerPlayMusicAdapter.getItem(1);

        // Kiểm tra nếu danh sách bài hát không trống
        if (baiHatList.size() > 0) {
            BaiHatYeuThich baiHat = baiHatList.get(0);
            getSupportActionBar().setTitle(baiHat.getTenBaiHat());
            txtSongName.setText(baiHat.getTenBaiHat());       // 🆕
            txtSingerName.setText(baiHat.getCaSi());          // 🆕

            Log.d("DEBUG_IMAGE", "Link ảnh: " + baiHat.getHinhBaiHat());

            // Dùng Picasso để tải ảnh từ URL có trong dữ liệu
            if (baiHat.getHinhBaiHat() != null && !baiHat.getHinhBaiHat().isEmpty()) {
                Picasso.get()
                        .load(baiHat.getHinhBaiHat())
                        .placeholder(R.drawable.no_music)
                        .error(R.drawable.iconfloatingactionbutton)
                        .into(imgSong);
            }

            new PlayMusic().execute(baiHat.getLinkBaiHat()); // Phát nhạc
            btnPlay.setImageResource(R.drawable.iconpause); // Thay đổi icon play
        }
    }

    private void stopAndReleaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release(); // Giải phóng tài nguyên của MediaPlayer
            mMediaPlayer = null; // Đảm bảo mMediaPlayer không còn tham chiếu đến đối tượng đã bị giải phóng
        }
    }


    // Lấy dữ liệu bài hát từ Intent
    private void GetDataFromItent() {
        Intent intent = getIntent();
        baiHatList.clear();
        if (intent != null) {
            if (intent.hasExtra("cakhuc")) {
                BaiHatYeuThich baiHatYeuThich = intent.getParcelableExtra("cakhuc");
                baiHatList.add(baiHatYeuThich);
            }
            if (intent.hasExtra("allbaihat")) {
                ArrayList<BaiHatYeuThich> allbaihatList = intent.getParcelableArrayListExtra("allbaihat");
                baiHatList = allbaihatList;
            }
        }
    }

    // Cập nhật thời lượng tổng bài hát và SeekBar
    public void TimeSong() {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        txtTotalTime.setText(format.format(mMediaPlayer.getDuration()));
        mSeekBar.setMax(mMediaPlayer.getDuration());
    }

    // Cập nhật tiến trình bài hát, xử lý sự kiện khi nhạc kết thúc
    private void UpdateTime() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer != null) {
                    SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                    txtTimeSong.setText(format.format(mMediaPlayer.getCurrentPosition()));
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());

                    mMediaPlayer.setOnCompletionListener(mp -> {
                        next = true;
                    });

                    handler.postDelayed(this, 500);
                }
            }
        }, 100);

        final Handler handlerNext = new Handler();
        handlerNext.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next) {
                    playNext();
                    next = false;
                    handlerNext.removeCallbacks(this);
                } else {
                    handlerNext.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    // Hàm xử lý chuyển bài kế tiếp
    private void playNext() {
        if (position < baiHatList.size()) {
            position++;
            if (repeat) position--;
            if (checkRandom) {
                Random random = new Random();
                int index = random.nextInt(baiHatList.size());
                position = (index == position) ? index - 1 : index;
            }
            if (position > baiHatList.size() - 1) position = 0;

            new PlayMusic().execute(baiHatList.get(position).getLinkBaiHat());
            mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
            getSupportActionBar().setTitle(baiHatList.get(position).getTenBaiHat());

            getSupportActionBar().setTitle(baiHatList.get(position).getTenBaiHat());
            txtSongName.setText(baiHatList.get(position).getTenBaiHat());
            txtSingerName.setText(baiHatList.get(position).getCaSi());

            TimeSong();
            UpdateTime();
        }
    }

    // Xử lý các sự kiện click: play, pause, next, back, repeat, random
    private void eventClick() {

        final Handler handler = new Handler();

        Runnable checkViewPagerReady = new Runnable() {
            @Override
            public void run() {
                if (mViewPagerPlayMusicAdapter.getItem(1) != null && baiHatList.size() > 0) {
                    mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
                } else {
                    handler.postDelayed(this, 300);
                }
            }
        };

        handler.postDelayed(checkViewPagerReady, 500);

        btnPlay.setOnClickListener(v -> {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                btnPlay.setImageResource(R.drawable.iconplay);
            } else {
                mMediaPlayer.start();
                btnPlay.setImageResource(R.drawable.iconpause);
            }
            TimeSong();
            UpdateTime();
        });

        btnLap.setOnClickListener(v -> {
            repeat = !repeat;
            if (repeat) btnLap.setImageResource(R.drawable.iconsyned);
            else btnLap.setImageResource(R.drawable.iconrepeat);
        });

        btnRandom.setOnClickListener(v -> {
            checkRandom = !checkRandom;
            if (checkRandom) btnRandom.setImageResource(R.drawable.iconshuffled);
            else btnRandom.setImageResource(R.drawable.iconsuffle);
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(mSeekBar.getProgress());
            }
        });

        btnNext.setOnClickListener(v -> playNext());

        btnBack.setOnClickListener(v -> {
            if (position > 0) position--;
            else position = baiHatList.size() - 1;
            new PlayMusic().execute(baiHatList.get(position).getLinkBaiHat());
            mFragmentCDMusic.Playnhac(baiHatList.get(position).getHinhBaiHat());
            getSupportActionBar().setTitle(baiHatList.get(position).getTenBaiHat());
            TimeSong();
            UpdateTime();
        });
    }

    // AsyncTask dùng để load và phát nhạc từ URL
    class PlayMusic extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String baihat) {
            try {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(baihat));
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

            getSupportActionBar().setTitle(baiHatList.get(position).getTenBaiHat());
            txtSongName.setText(baiHatList.get(position).getTenBaiHat());
            txtSingerName.setText(baiHatList.get(position).getCaSi());
            Picasso.get()
                    .load(baiHatList.get(position).getHinhBaiHat())
                    .placeholder(R.drawable.no_music)
                    .error(R.drawable.iconfloatingactionbutton)
                    .fit()
                    .centerCrop()
                    .into(imgSong, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d("DEBUG_PICASSO", "Load ảnh thành công");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("DEBUG_PICASSO", "Lỗi load ảnh: " + e.getMessage());
                        }
                    });

            TimeSong();
            UpdateTime();
        }

    }
}