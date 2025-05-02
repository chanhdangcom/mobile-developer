package com.ngdat.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.ngdat.mymusic.Adapter.CurrentSongHolder;
import com.ngdat.mymusic.Adapter.ViewPagerAdapter;
import com.ngdat.mymusic.Fragment.Fragment_TimKiem;
import com.ngdat.mymusic.Fragment.Fragment_TrangChu;
import com.ngdat.mymusic.Fragment.baocao.FragmentBaoCao;
import com.ngdat.mymusic.R;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    Toolbar nowPlayingToolbar;
    TextView tvNowPlaying, tvNowPlayingSinger;
    ImageView imgNowPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        init();
        setupNowPlayingToolbar();
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
        mViewPagerAdapter.addFragment(new Fragment_TimKiem(), "Tìm Kiếm");
        mViewPagerAdapter.addFragment(new FragmentBaoCao(), "Báo Cáo");

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (mTabLayout.getTabAt(0) != null) mTabLayout.getTabAt(0).setIcon(R.drawable.icontrangchu);
        if (mTabLayout.getTabAt(1) != null) mTabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
        if (mTabLayout.getTabAt(2) != null) mTabLayout.getTabAt(2).setIcon(R.drawable.ic_search);
    }

    private void setupNowPlayingToolbar() {
        nowPlayingToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentSongHolder.currentSong != null) {
                    Intent intent = new Intent(MainActivity.this, PlayMusicActivity.class);
                    intent.putExtra("cakhuc", CurrentSongHolder.currentSong);
                    startActivity(intent);
                }
            }
        });

        updateNowPlayingText();
    }

    private void updateNowPlayingText() {
        if (CurrentSongHolder.currentSong != null) {
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