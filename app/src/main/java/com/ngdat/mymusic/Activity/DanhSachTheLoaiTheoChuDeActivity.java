package com.ngdat.mymusic.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Adapter.DanhSachTheLoaiTheoChuDeAdapter;
import com.ngdat.mymusic.Model.ChuDe;
import com.ngdat.mymusic.Model.TheLoai;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachTheLoaiTheoChuDeActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    ChuDe mChuDe;
    DanhSachTheLoaiTheoChuDeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_the_loai_theo_chu_de);
        mChuDe = new ChuDe();
        GetIntent();
        initView();
        init();

        GetData(mChuDe.getIDChuDe());


    }

    private void GetData(String idchude) {
        DataService mDataService = APIService.getService();
        Call<List<TheLoai>> mCall = mDataService.getTheLoaiTheoChuDe(idchude);
        mCall.enqueue(new Callback<List<TheLoai>>() {
            @Override
            public void onResponse(Call<List<TheLoai>> call, Response<List<TheLoai>> response) {
                ArrayList<TheLoai> theLoaiList = (ArrayList<TheLoai>) response.body();
                mAdapter = new DanhSachTheLoaiTheoChuDeAdapter(DanhSachTheLoaiTheoChuDeActivity.this, theLoaiList);
                mRecyclerView.setLayoutManager(new GridLayoutManager(DanhSachTheLoaiTheoChuDeActivity.this, 2));
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<TheLoai>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(mChuDe.getTenChuDe());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mToolbar = findViewById(R.id.my_toolbarTheoChuDe);
        mRecyclerView = findViewById(R.id.myRecycleTheoChuDe);
    }

    private void GetIntent() {
        Intent itent = getIntent();


        if (itent != null) {
            if (itent.hasExtra("chude")) {

                mChuDe = (ChuDe) itent.getSerializableExtra("chude");
            }
        }
    }
}
