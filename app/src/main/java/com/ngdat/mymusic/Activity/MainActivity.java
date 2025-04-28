package com.ngdat.mymusic.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.ngdat.mymusic.Adapter.ViewPagerAdapter;
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
        mViewPagerAdapter.addFragment(new Fragment_TimKiem(), "Tìm Kiếm");
        mViewPagerAdapter.addFragment(new FragmentBaoCao(), "Báo Cáo");

        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        // Set icon an toàn, kiểm tra null trước
        if (mTabLayout.getTabAt(0) != null) {
            mTabLayout.getTabAt(0).setIcon(R.drawable.icontrangchu);
        }
        if (mTabLayout.getTabAt(1) != null) {
            mTabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
        }
        if (mTabLayout.getTabAt(2) != null) {
            mTabLayout.getTabAt(2).setIcon(R.drawable.ic_search);
        }
        // KHÔNG setTabAt(3) vì chỉ có 3 tab!
    }

    private void initView() {
        mTabLayout = findViewById(R.id.myTablayout);
        mViewPager = findViewById(R.id.myViewPager);
    }
}