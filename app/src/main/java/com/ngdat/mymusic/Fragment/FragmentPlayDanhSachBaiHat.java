package com.ngdat.mymusic.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Activity.PlayMusicActivity;
import com.ngdat.mymusic.Adapter.PlayMusicAdapter;
import com.ngdat.mymusic.Model.BaiHat;
import com.ngdat.mymusic.R;

import java.util.ArrayList;

public class FragmentPlayDanhSachBaiHat extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    PlayMusicAdapter musicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_danh_sach_bai_hat, container, false);
        mRecyclerView = view.findViewById(R.id.recyclePlayDanhSachBH);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (getActivity() instanceof PlayMusicActivity) {
            PlayMusicActivity playMusicActivity = (PlayMusicActivity) getActivity();
            ArrayList<BaiHat> baiHatList = playMusicActivity.getBaiHatList();
            if (baiHatList != null && baiHatList.size() > 0) {
                musicAdapter = new PlayMusicAdapter(getActivity(), baiHatList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRecyclerView.setAdapter(musicAdapter);
            }
        }

        return view;
    }

}