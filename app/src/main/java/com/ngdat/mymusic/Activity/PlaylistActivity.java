package com.ngdat.mymusic.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Adapter.DanhSachAllPlaylistAdapter;
import com.ngdat.mymusic.Model.PlaylistAll;
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
import com.ngdat.mymusic.Model.BaiHat;

public class PlaylistActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    DanhSachAllPlaylistAdapter mAdapter;
    RecyclerView recyclerViewBaiHat;
    BaiHatAdapter baiHatAdapter;
    ArrayList<BaiHat> baiHatArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        initView();
        init();
        Getdata();
        loadBaiHatYeuThich();
    }

    private void Getdata() {
        DataService mDataService = APIService.getService();
        Call<List<PlaylistAll>> callBack = mDataService.getAllPlaylist();
        callBack.enqueue(new Callback<List<PlaylistAll>>() {
            @Override
            public void onResponse(Call<List<PlaylistAll>> call, Response<List<PlaylistAll>> response) {
                ArrayList<PlaylistAll> playlists = (ArrayList<PlaylistAll>) response.body();
                mAdapter = new DanhSachAllPlaylistAdapter(PlaylistActivity.this, playlists);
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new GridLayoutManager(PlaylistActivity.this, 2));
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<PlaylistAll>> call, Throwable t) {

            }
        });
    }

    private void init() {
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
        baiHatArrayList = new ArrayList<>();

        baiHatArrayList.add(new BaiHat(
                "1",
                "Anh chưa một lần nói",
                "https://cdn.chanhdang.com/singer_khanh_vin.jpg",
                "Ê Kê Vin",
                "https://cdn.chanhdang.com/music_rap_with_tran_nha.mp3",
                "765384"
        ));

        baiHatArrayList.add(new BaiHat(
                "2",
                "Tháp drill tự do",
                "https://cdn.chanhdang.com/cover_thap_drill_tu_do.jpg",
                "Ê Kê Vin",
                "https://cdn.chanhdang.com/music_thap_drill_tu_do.mp3",
                "765344"
        ));

        baiHatAdapter = new BaiHatAdapter(this, baiHatArrayList);
        recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBaiHat.setAdapter(baiHatAdapter);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.myRecycleViewPlaylist);
        mToolbar = findViewById(R.id.toobarPlaylist);
        recyclerViewBaiHat = findViewById(R.id.recyclerViewBaiHat);
    }
}
