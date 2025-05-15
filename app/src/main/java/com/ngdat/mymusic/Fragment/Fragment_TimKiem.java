package com.ngdat.mymusic.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ngdat.mymusic.Adapter.SearchBaiHatAdapter;
import com.ngdat.mymusic.Model.BaiHat;
import com.ngdat.mymusic.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Fragment_TimKiem extends Fragment {

    Toolbar toolbarTimKiemBaiHat;
    RecyclerView recyclerViewTimKiem;
    TextView tvDataNull;
    SearchBaiHatAdapter searchBaiHatAdapter;
    List<BaiHat> tatCaBaiHat; // Danh sách tất cả bài hát từ JSON
    List<BaiHat> mangBaiHatTimKiem; // Danh sách kết quả tìm kiếm

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tim_kiem, container, false);
        toolbarTimKiemBaiHat = view.findViewById(R.id.toolbartimkiembaihat);
        recyclerViewTimKiem = view.findViewById(R.id.recycleviewTimKiem);
        tvDataNull = view.findViewById(R.id.tv_DataNull);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSupportActionBar();
        initRecyclerView();
        loadBaiHatFromJSON(); // Đọc dữ liệu từ JSON khi Fragment được tạo
    }

    private void setSupportActionBar() {
        // Thêm EditText vào Toolbar программно
        EditText edtSearch = new EditText(getContext());
        edtSearch.setHint("Nhập tên bài hát");
        edtSearch.setTextColor(getResources().getColor(android.R.color.white));
        edtSearch.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        edtSearch.setLayoutParams(params);
        toolbarTimKiemBaiHat.addView(edtSearch);

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) { // Bắt đầu tìm kiếm khi người dùng nhập ít nhất 2 ký tự
                    timKiemBaiHat(s.toString());
                } else {
                    mangBaiHatTimKiem.clear();
                    searchBaiHatAdapter.notifyDataSetChanged();
                    tvDataNull.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecyclerView() {
        mangBaiHatTimKiem = new ArrayList<>();
        recyclerViewTimKiem.setLayoutManager(new LinearLayoutManager(getContext()));
        searchBaiHatAdapter = new SearchBaiHatAdapter(getActivity(), mangBaiHatTimKiem);
        recyclerViewTimKiem.setAdapter(searchBaiHatAdapter);
    }

    private void loadBaiHatFromJSON() {
        try {
            InputStream inputStream = getActivity().getAssets().open("BaiHatDuocYeuThich.php.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            Type listType = new TypeToken<List<BaiHat>>() {}.getType();
            tatCaBaiHat = gson.fromJson(json, listType);

            if (tatCaBaiHat == null || tatCaBaiHat.isEmpty()) {
                tvDataNull.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            Log.e("JSON Loading Error", "Error loading JSON from assets", e);
            tvDataNull.setVisibility(View.VISIBLE);
            tvDataNull.setText("Lỗi khi tải dữ liệu bài hát");
        }
    }

    private void timKiemBaiHat(String keyword) {
        mangBaiHatTimKiem.clear();
        if (tatCaBaiHat != null && !tatCaBaiHat.isEmpty()) {
            keyword = keyword.toLowerCase(Locale.getDefault());
            for (BaiHat baiHat : tatCaBaiHat) {
                if (baiHat.getTenBaiHat().toLowerCase(Locale.getDefault()).contains(keyword)) {
                    mangBaiHatTimKiem.add(baiHat);
                }
            }
        }

        if (mangBaiHatTimKiem.isEmpty()) {
            tvDataNull.setVisibility(View.VISIBLE);
        } else {
            tvDataNull.setVisibility(View.GONE);
        }
        searchBaiHatAdapter.notifyDataSetChanged();
    }
}