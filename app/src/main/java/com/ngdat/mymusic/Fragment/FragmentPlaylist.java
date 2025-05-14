package com.ngdat.mymusic.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Activity.PlaylistActivity;
import com.ngdat.mymusic.Activity.SongsListActivity;
import com.ngdat.mymusic.Adapter.PlaylistAdapter;
import com.ngdat.mymusic.Model.Playlist;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;
import com.ngdat.mymusic.Model.BaiHatYeuThich;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.recyclerview.widget.GridLayoutManager;

public class FragmentPlaylist extends Fragment {
    View view;
    RecyclerView rvPlaylist;
    TextView txtTiltlePlaylist;
    PlaylistAdapter playlistAdapter;
    List<Playlist> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        intitView();
        GetData();

        return view;
    }

    private void intitView() {
        rvPlaylist = view.findViewById(R.id.rv_playlist);
        txtTiltlePlaylist = view.findViewById(R.id.tv_titlePlaylist);
    }

    private void GetData() {
        DataService mDataService = APIService.getService();
        Call<List<Playlist>> mCall = mDataService.getDataPlaylist();
        mCall.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                mList = response.body();
                playlistAdapter = new PlaylistAdapter(getActivity(), mList, new PlaylistAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Playlist playlist) {
                        Intent intent = new Intent(getActivity(), SongsListActivity.class);
                        ArrayList<BaiHatYeuThich> danhSachBaiHat = (ArrayList<BaiHatYeuThich>) playlist.getDanhSachBaiHat();
                        intent.putParcelableArrayListExtra("allbaihatfromplaylist", danhSachBaiHat);
                        startActivity(intent);
                        Log.d("FragmentPlaylist", "Đã click vào playlist: " + playlist.getTen());
                        if (playlist.getDanhSachBaiHat() != null) {
                            Log.d("FragmentPlaylist", "Số lượng bài hát: " + playlist.getDanhSachBaiHat().size());
                        } else {
                            Log.d("FragmentPlaylist", "Danh sách bài hát là null.");
                        }
                    }
                });
//                rvPlaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvPlaylist.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                rvPlaylist.setAdapter(playlistAdapter);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.e("FragmentPlaylist", "Lỗi khi lấy dữ liệu playlist: " + t.getMessage());
            }
        });
    }
}