package com.ngdat.mymusic.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.ngdat.mymusic.Adapter.DanhSachBaiHatAdapter;
import com.ngdat.mymusic.Model.Album;
import com.ngdat.mymusic.Model.BaiHat;
import com.ngdat.mymusic.Model.Playlist;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongsListActivity extends AppCompatActivity {
    CoordinatorLayout mCoordinatorLayout;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    RecyclerView recyclerViewBaiHat;
    Button mButtonNgheTatCa;
    ImageView mImageView;
    ImageView mImageViewToolbarBackground;
    List<BaiHat> listBaiHat;
    DanhSachBaiHatAdapter mAdapterBaiHatPlaylist;
    Playlist mPlaylist;
    Album mAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        DataItent();
        initView();
        initToolbar();

        ArrayList<BaiHat> danhSachBaiHatTuPlaylist = getIntent().getParcelableArrayListExtra("allbaihatfromplaylist");

        if (danhSachBaiHatTuPlaylist != null && !danhSachBaiHatTuPlaylist.isEmpty()) {

            mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, danhSachBaiHatTuPlaylist);
            recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
            Log.d("SongsListActivity", "Đã hiển thị danh sách bài hát từ playlist: " + danhSachBaiHatTuPlaylist.size() + " bài hát.");
        } else {
            Log.w("SongsListActivity", "Không có dữ liệu bài hát từ playlist để hiển thị, tải theo intent ban đầu.");
        }
    }

    private void addTestSong(List<BaiHat> list) {
        BaiHat baiHatMoi = new BaiHat("test_id", "Bài hát thử nghiệm", "test_hinh", "Nghệ sĩ thử nghiệm", "test_link", "100");
        list.add(0, baiHatMoi);
    }

    private void getDataAlbum(String idAlbum) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> call = dataService.getDataBaiHatTheoAlbum(idAlbum);
        call.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listBaiHat = response.body();
                if (listBaiHat != null && !listBaiHat.isEmpty()) {
                    mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                    recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                    recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                    eventClick();
                } else {
                    listBaiHat = new ArrayList<>();
                    addTestSong(listBaiHat);
                    mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                    recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                    recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                    eventClick();
                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Log.e("SongsListActivity", "getDataAlbum Error: " + t.getMessage());
                listBaiHat = new ArrayList<>();
                addTestSong(listBaiHat);
                mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                eventClick();
            }
        });
    }

    private void getDataTheLoai(String idtheloai) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> call = dataService.getDataBaiHatTheoTheLoai(idtheloai);
        call.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listBaiHat = response.body();
                if (listBaiHat != null && !listBaiHat.isEmpty()) {
                    mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                    recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                    recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                    eventClick();
                } else {
                    listBaiHat = new ArrayList<>();
                    addTestSong(listBaiHat);
                    mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                    recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                    recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                    eventClick();
                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Log.e("SongsListActivity", "getDataTheLoai Error: " + t.getMessage());
                listBaiHat = new ArrayList<>();
                addTestSong(listBaiHat);
                mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                eventClick();
            }
        });
    }

    private void getDataPlaylist(String idplaylist) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> call = dataService.getDataBaiHatTheoPlaylist(idplaylist);
        call.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listBaiHat = response.body();
                if (listBaiHat != null && !listBaiHat.isEmpty()) {
                    mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                    recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                    recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                    eventClick();
                } else {
                    listBaiHat = new ArrayList<>();
                    addTestSong(listBaiHat);
                    mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                    recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                    recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                    eventClick();
                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Log.e("SongsListActivity", "getDataPlaylist Error: " + t.getMessage());
                listBaiHat = new ArrayList<>();
                addTestSong(listBaiHat);
                mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                eventClick();
            }
        });
    }

    private void getDataQuangCao(String idquangcao) {
        DataService dataService = APIService.getService();
        Call<List<BaiHat>> call = dataService.getDataBaiHatTheoQuangCao(idquangcao);
        call.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                listBaiHat = response.body();
                if (listBaiHat != null && !listBaiHat.isEmpty()) {
                    mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                    recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                    recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                    eventClick();
                } else {
                    listBaiHat = new ArrayList<>();
                    addTestSong(listBaiHat);
                    mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                    recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                    recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                    eventClick();
                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Log.e("SongsListActivity", "getDataQuangCao Error: " + t.getMessage());
                listBaiHat = new ArrayList<>();
                addTestSong(listBaiHat);
                mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, listBaiHat);
                recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(SongsListActivity.this));
                recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
                eventClick();
            }
        });
    }

    private void setupRecyclerViews() {

        recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
    }

    private void setValuesInView(String name, String image) {
        mCollapsingToolbarLayout.setTitle(name);
        Log.d("SongsListActivity", "Đang tải ảnh: " + image);
        Picasso.get().load(image).into(mImageView);
        Picasso.get().load(image).into(mImageViewToolbarBackground);
    }
    private void eventClick() {
        mButtonNgheTatCa.setEnabled(true);
        mButtonNgheTatCa.setOnClickListener(v -> {
            Intent intent = new Intent(SongsListActivity.this, PlayMusicActivity.class);
            intent.putParcelableArrayListExtra("allbaihat", (ArrayList<? extends Parcelable>) listBaiHat);
            startActivity(intent);
        });
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        mToolbar.setNavigationOnClickListener(v -> finish());
        mButtonNgheTatCa.setEnabled(false);
    }

    private void initView() {
        mCollapsingToolbarLayout = findViewById(R.id.myCollapsingToolLayout);
        mCoordinatorLayout = findViewById(R.id.myCooridinerLayout);
        mToolbar = findViewById(R.id.my_toolbarList);
        mRecyclerView = findViewById(R.id.recycleDanhSachBH);
        recyclerViewBaiHat = findViewById(R.id.recyclerViewBaiHat);
        mButtonNgheTatCa = findViewById(R.id.btn_nghetatca);
        mImageView = findViewById(R.id.img_danhSachbaihat);
        mImageViewToolbarBackground = findViewById(R.id.img_toolbarBackground);
    }

    private void DataItent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("itemPlaylist")) {
                mPlaylist = (Playlist) intent.getSerializableExtra("itemPlaylist");
            }
            if (intent.hasExtra("album")) {
                mAlbum = (Album) intent.getSerializableExtra("album");
            }
        }
    }

}