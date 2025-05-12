package com.ngdat.mymusic.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.Model.Playlist;
import com.ngdat.mymusic.Model.PlaylistAll;
import com.ngdat.mymusic.Model.Quangcao;
import com.ngdat.mymusic.Model.TheLoai;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongsListActivity extends AppCompatActivity {
    CoordinatorLayout mCoordinatorLayout;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    Toolbar mToolbar;
    RecyclerView mRecyclerView; // Có thể không dùng đến, xem lại layout
    RecyclerView recyclerViewBaiHat; // RecyclerView để hiển thị bài hát từ playlist
    Button mButtonNgheTatCa;
    ImageView mImageView;
    ImageView mImageViewToolbarBackground; // Thêm ImageView cho background toolbar
    Quangcao mQuangcao;
    List<BaiHatYeuThich> listBaiHat;
    DanhSachBaiHatAdapter mAdapterBaiHatPlaylist; // Adapter cho danh sách bài hát của playlist
    Playlist mPlaylist;
    PlaylistAll mPlaylistAll;
    TheLoai mTheLoai;
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
        logIntentData();
        initView();
        initToolbar();

        ArrayList<BaiHatYeuThich> danhSachBaiHatTuPlaylist = getIntent().getParcelableArrayListExtra("allbaihatfromplaylist");

        if (danhSachBaiHatTuPlaylist != null && !danhSachBaiHatTuPlaylist.isEmpty()) {
            // Sử dụng DanhSachBaiHatAdapter để hiển thị
            mAdapterBaiHatPlaylist = new DanhSachBaiHatAdapter(SongsListActivity.this, danhSachBaiHatTuPlaylist);
            recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
            Log.d("SongsListActivity", "Đã hiển thị danh sách bài hát từ playlist: " + danhSachBaiHatTuPlaylist.size() + " bài hát.");
        } else {
            Log.w("SongsListActivity", "Không có dữ liệu bài hát từ playlist để hiển thị, tải theo intent ban đầu.");
            if (mQuangcao != null && !mQuangcao.getTenbaihat().equals("")) {
                setValuesInView(mQuangcao.getTenbaihat(), mQuangcao.getHinhbaihat());
                getDataQuangCao(mQuangcao.getIdQuangCao());
            }
            if (mPlaylist != null && !mPlaylist.getTen().equals("")) {
                setValuesInView(mPlaylist.getTen(), mPlaylist.getHinhAnhPlaylist());
                getDataPlaylist(mPlaylist.getIdPlaylist());
            }
            if (mPlaylistAll != null && !mPlaylistAll.getTen().equals("")) {
                setValuesInView(mPlaylistAll.getTen(), mPlaylistAll.getHinhNen());
                getDataPlaylist(mPlaylistAll.getIdPlaylist()); // Sử dụng getDataPlaylist cho PlaylistAll
            }
            if (mTheLoai != null && !mTheLoai.getTenTheLoai().equals("")) {
                setValuesInView(mTheLoai.getTenTheLoai(), mTheLoai.getHinhTheLoai());
                getDataTheLoai(mTheLoai.getIDTheLoai());
            }
            if (mAlbum != null && !mAlbum.getTenAlbum().equals("")) {
                setValuesInView(mAlbum.getTenAlbum(), mAlbum.getHinhAlbum());
                getDataAlbum(mAlbum.getIdAlbum());
            }
        }
    }

    private void addTestSong(List<BaiHatYeuThich> list) {
        BaiHatYeuThich baiHatMoi = new BaiHatYeuThich("test_id", "Bài hát thử nghiệm", "test_hinh", "Nghệ sĩ thử nghiệm", "test_link", "100");
        list.add(0, baiHatMoi);
    }

    private void getDataAlbum(String idAlbum) {
        DataService dataService = APIService.getService();
        Call<List<BaiHatYeuThich>> call = dataService.getDataBaiHatTheoAlbum(idAlbum);
        call.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
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
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {
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
        Call<List<BaiHatYeuThich>> call = dataService.getDataBaiHatTheoTheLoai(idtheloai);
        call.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
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
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {
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
        Call<List<BaiHatYeuThich>> call = dataService.getDataBaiHatTheoPlaylist(idplaylist);
        call.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
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
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {
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
        Call<List<BaiHatYeuThich>> call = dataService.getDataBaiHatTheoQuangCao(idquangcao);
        call.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
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
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {
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
        // Có thể không cần dùng đến nếu chỉ hiển thị danh sách bài hát từ playlist
        // mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // mRecyclerView.setAdapter(mAdapter);

        recyclerViewBaiHat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBaiHat.setAdapter(mAdapterBaiHatPlaylist);
    }

    private void setValuesInView(String name, String image) {
        mCollapsingToolbarLayout.setTitle(name);
        Log.d("SongsListActivity", "Đang tải ảnh: " + image); // Log để kiểm tra URL
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
            if (intent.hasExtra("quangcao")) {
                mQuangcao = (Quangcao) intent.getSerializableExtra("quangcao");
            }
            if (intent.hasExtra("itemPlaylist")) {
                mPlaylist = (Playlist) intent.getSerializableExtra("itemPlaylist");
            }
            if (intent.hasExtra("itemPlaylistAll")) {
                mPlaylistAll = (PlaylistAll) intent.getSerializableExtra("itemPlaylistAll");
            }
            if (intent.hasExtra("idtheloai")) {
                mTheLoai = (TheLoai) intent.getSerializableExtra("idtheloai");
            }
            if (intent.hasExtra("album")) {
                mAlbum = (Album) intent.getSerializableExtra("album");
            }
        }
    }

    private void logIntentData() {
        if (mQuangcao != null) {
            Log.d("SongsListActivity", "Nhận mQuangcao: " + mQuangcao.getTenbaihat() + ", ID: " + mQuangcao.getIdQuangCao());
        }
        if (mPlaylist != null) {
            Log.d("SongsListActivity", "Nhận mPlaylist: " + mPlaylist.getTen() + ", ID: " + mPlaylist.getIdPlaylist() + ", Hình ảnh: " + mPlaylist.getHinhAnhPlaylist());
        }
        if (mPlaylistAll != null) {
            Log.d("SongsListActivity", "Nhận mPlaylistAll: " + mPlaylistAll.getTen() + ", ID: " + mPlaylistAll.getIdPlaylist() + ", Hình nền: " + mPlaylistAll.getHinhNen());
        }
        if (mTheLoai != null) {
            Log.d("SongsListActivity", "Nhận mTheLoai: " + mTheLoai.getTenTheLoai() + ", ID: " + mTheLoai.getIDTheLoai() + ", Hình ảnh: " + mTheLoai.getHinhTheLoai());
        }
        if (mAlbum != null) {
            Log.d("SongsListActivity", "Nhận mAlbum: " + mAlbum.getTenAlbum() + ", ID: " + mAlbum.getIdAlbum() + ", Hình ảnh: " + mAlbum.getHinhAlbum());
        }
    }
}