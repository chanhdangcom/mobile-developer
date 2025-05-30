package com.ngdat.mymusic.Activity;

import static com.ngdat.mymusic.utils.SongLoader.songsList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.ngdat.mymusic.Adapter.CurrentSongHolder;
import com.ngdat.mymusic.Adapter.ViewPagerAdapter;
import com.ngdat.mymusic.Fragment.Fragment_DanhSachBaiHatYeuThich;
import com.ngdat.mymusic.Fragment.Fragment_History;
import com.ngdat.mymusic.Fragment.Fragment_TimKiem;
import com.ngdat.mymusic.Fragment.Fragment_TrangChu;
import com.ngdat.mymusic.Fragment.Fragment_device_music;
import com.ngdat.mymusic.Model.Song;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.utils.MyMediaPlayer;
import com.ngdat.mymusic.utils.PermissionHelper;
import com.ngdat.mymusic.utils.SongLoader;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    Toolbar nowPlayingToolbar;
    TextView tvNowPlaying, tvNowPlayingSinger;
    ImageView imgNowPlaying;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
        setupNowPlayingToolbar();


        PermissionHelper.checkAndRequestPermissions(this);

        boolean hasPermission = PermissionHelper.hasPermissions(this);
        boolean hasNotificationPermission = PermissionHelper.hasNotificationPermission(this);

        // Thông báo quyền audio và thông báo
        Toast.makeText(this, "Audio Permission: " + hasPermission + "\nNotification Permission: " + hasNotificationPermission, Toast.LENGTH_SHORT).show();



        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void initView() {
        mTabLayout = findViewById(R.id.myTablayout);
        mViewPager = findViewById(R.id.myViewPager);
        nowPlayingToolbar = findViewById(R.id.nowPlayingToolbar);
        tvNowPlaying = findViewById(R.id.tvNowPlaying);
        tvNowPlayingSinger = findViewById(R.id.tvNowPlayingSinger);
        imgNowPlaying = findViewById(R.id.imgNowPlaying);
    }

    private void init() {
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new Fragment_TrangChu(), "Trang Chủ");
        mViewPagerAdapter.addFragment(new Fragment_DanhSachBaiHatYeuThich(), "Yêu thích");
        mViewPagerAdapter.addFragment(new Fragment_TimKiem(), "Tìm Kiếm");
        mViewPagerAdapter.addFragment(new Fragment_device_music(), "Device Music");
        mViewPagerAdapter.addFragment(new Fragment_History(), "Gần đây");


        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (mTabLayout.getTabAt(0) != null) mTabLayout.getTabAt(0).setIcon(R.drawable.icontrangchu);
        if (mTabLayout.getTabAt(1) != null) mTabLayout.getTabAt(1).setIcon(R.drawable.heart_fill);
        if (mTabLayout.getTabAt(2) != null) mTabLayout.getTabAt(2).setIcon(R.drawable.icontimkiem1);
        if (mTabLayout.getTabAt(3) != null) mTabLayout.getTabAt(3).setIcon(R.drawable.device_mobile_speaker_fill);
        if (mTabLayout.getTabAt(4) != null) mTabLayout.getTabAt(4).setIcon(R.drawable.clock_counter_clockwise_bold);
    }

    private void setupNowPlayingToolbar() {
        nowPlayingToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MyMediaPlayer.currentIndex >= 0 && MyMediaPlayer.currentIndex < songsList.size()) {
                    Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MainActivity.this.startActivity(intent);
                }
                else if (CurrentSongHolder.currentSong != null) {
                    Intent intent = new Intent(MainActivity.this, PlayMusicActivity.class);
                    intent.putExtra("cakhuc", CurrentSongHolder.currentSong);
                    startActivity(intent);
                }
            }
        });

        updateNowPlayingText();
    }

    private void updateNowPlayingText() {
        if (MyMediaPlayer.currentIndex >= 0 && MyMediaPlayer.currentIndex < songsList.size()) {

            Song song = songsList.get(MyMediaPlayer.currentIndex);
            tvNowPlaying.setText(song.getTitle());
            tvNowPlayingSinger.setText(""); // Không có ca sĩ

            byte[] imageBytes = song.getEmbeddedPicture();
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imgNowPlaying.setImageBitmap(bitmap);
            } else {
                imgNowPlaying.setImageResource(R.drawable.no_music);
            }
        }
        else if (CurrentSongHolder.currentSong != null) {

            tvNowPlaying.setText(CurrentSongHolder.currentSong.getTenBaiHat());
            tvNowPlayingSinger.setText(CurrentSongHolder.currentSong.getCaSi());

            if (CurrentSongHolder.currentSong.getHinhBaiHat() != null) {
                Glide.with(this)
                        .load(CurrentSongHolder.currentSong.getHinhBaiHat())
                        .placeholder(R.drawable.no_music)
                        .into(imgNowPlaying);
            } else {
                imgNowPlaying.setImageResource(R.drawable.no_music);
            }
        } else {

            tvNowPlaying.setText("Bài hát");
            tvNowPlayingSinger.setText("Ca sĩ");
            imgNowPlaying.setImageResource(R.drawable.no_music);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNowPlayingText();
    }
}