package com.ngdat.mymusic.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Activity.DanhSachAllAlbumActivity;
import com.ngdat.mymusic.Adapter.AlbumAdapter;
import com.ngdat.mymusic.Model.Album;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAlbum extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    TextView txtXemThemAlbum;
    AlbumAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        initview();
        GetDataAlbum();
        return view;
    }

    private void initview() {
        mRecyclerView = view.findViewById(R.id.myRecycleAlbum);
        txtXemThemAlbum = view.findViewById(R.id.tv_xemthemAlbum);
        txtXemThemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachAllAlbumActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetDataAlbum() {
        DataService mDataService = APIService.getService();
        Call<List<Album>> mCall = mDataService.getDataAlbum();
        mCall.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                List<Album> albumList = response.body();
                mAdapter = new AlbumAdapter(getActivity(), albumList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }
}
