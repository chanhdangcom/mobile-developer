package com.ngdat.mymusic.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ngdat.mymusic.Adapter.ViewPagerAdapter;
import com.ngdat.mymusic.Fragment.FragmentMV;
import com.ngdat.mymusic.Fragment.Fragment_TimKiem;
import com.ngdat.mymusic.Fragment.Fragment_TrangChu;
import com.ngdat.mymusic.Fragment.baocao.FragmentBaoCao;
import com.ngdat.mymusic.R;

public class MainActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void init() {
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPagerAdapter.addFragment(new Fragment_TrangChu(), "Trang Chủ");
        mViewPagerAdapter.addFragment(new FragmentMV(), "MV");
        mViewPagerAdapter.addFragment(new Fragment_TimKiem(), "Tìm Kiếm");
        mViewPagerAdapter.addFragment(new FragmentBaoCao(), "Báo cáo ");

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setIcon(R.drawable.icontrangchu);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_video);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_search);
        mTabLayout.getTabAt(3).setIcon(R.drawable.ic_search);

    }

    private void initView() {
        mTabLayout = findViewById(R.id.myTablayout);
        mViewPager = findViewById(R.id.myViewPager);
    }
}
