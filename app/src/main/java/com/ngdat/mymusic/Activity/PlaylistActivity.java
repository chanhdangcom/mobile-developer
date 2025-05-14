package com.ngdat.mymusic.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.ngdat.mymusic.Adapter.BaiHatAdapter;
import com.ngdat.mymusic.Model.BaiHatYeuThich;


public class PlaylistActivity extends AppCompatActivity {
    Toolbar mToolbar;
    RecyclerView recyclerViewBaiHat;
    BaiHatAdapter baiHatAdapter;
    ArrayList<BaiHatYeuThich> baiHatYeuThichArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        initView();
        initToolbar();
        loadBaiHatYeuThich();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Playlist");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.BLACK));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadBaiHatYeuThich() {
        baiHatYeuThichArrayList = new ArrayList<>();

        baiHatYeuThichArrayList.add(new BaiHatYeuThich(
                "1",
                "Anh chưa một lần nói",
                "https://cdn.chanhdang.com/singer_khanh_vin.jpg",
                "Ê Kê Vin",
                "https://cdn.chanhdang.com/music_rap_with_tran_nha.mp3",
                "765384"
        ));

        baiHatYeuThichArrayList.add(new BaiHatYeuThich(
                "2",
                "Tháp drill tự do",
                "https://cdn.chanhdang.com/cover_thap_drill_tu_do.jpg",
                "Ê Kê Vin",
                "https://cdn.chanhdang.com/music_thap_drill_tu_do.mp3",
                "765344"
        ));

        baiHatAdapter = new BaiHatAdapter(this, baiHatYeuThichArrayList);
        recyclerViewBaiHat.setHasFixedSize(true);
        recyclerViewBaiHat.setLayoutManager(new GridLayoutManager(this, 2)); // 2 cột
        recyclerViewBaiHat.setAdapter(baiHatAdapter);

    }

    private void initView() {
//        mToolbar = findViewById(R.id.toobarPlaylist); // không được comment
        recyclerViewBaiHat = findViewById(R.id.rv_playlist); // hoặc R.id.recyclerViewBaiHat nếu bạn dùng layout cũ
    }
}