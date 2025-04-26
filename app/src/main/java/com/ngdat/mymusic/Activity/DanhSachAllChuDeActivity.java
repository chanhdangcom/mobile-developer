package com.ngdat.mymusic.Activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Adapter.DanhSachAllChuDeAdapter;
import com.ngdat.mymusic.Model.ChuDe;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachAllChuDeActivity extends AppCompatActivity {
    RecyclerView recyclerViewAllChuDe;
    Toolbar mToolbarAllChuDe;
    DanhSachAllChuDeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_all_chu_de);
        initView();
        init();
        GetDataChuDe();
    }

    private void GetDataChuDe() {
        DataService mDataService = APIService.getService();
        Call<List<ChuDe>> mCall = mDataService.getAllChuDe();
        mCall.enqueue(new Callback<List<ChuDe>>() {
            @Override
            public void onResponse(Call<List<ChuDe>> call, Response<List<ChuDe>> response) {
                List<ChuDe> chuDeList = response.body();
                //  Log.d("bb",chuDeList.get(0).getIDChuDe());
                mAdapter = new DanhSachAllChuDeAdapter(DanhSachAllChuDeActivity.this, chuDeList);
                recyclerViewAllChuDe.setLayoutManager(new GridLayoutManager(DanhSachAllChuDeActivity.this, 1));
                recyclerViewAllChuDe.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<ChuDe>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(mToolbarAllChuDe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất Cả Chủ Đề Bài Hát");
        mToolbarAllChuDe.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        recyclerViewAllChuDe = findViewById(R.id.recycleViewAllChuDe);
        mToolbarAllChuDe = findViewById(R.id.toobarAllChuDe);
    }
}
