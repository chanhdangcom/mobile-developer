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

import com.ngdat.mymusic.Activity.KaraokeActivity;
import com.ngdat.mymusic.Activity.PlayVideoActivity;
import com.ngdat.mymusic.Adapter.VideoAdapter;
import com.ngdat.mymusic.Model.MyVideo;
import com.ngdat.mymusic.R;

import java.util.Vector;

public class FragmentMV extends Fragment {
    View view;
    TextView txtMV, txtKara;
    RecyclerView mRecyclerViewVideoMV, mRecyclerViewKaraoke;
    Vector<MyVideo> myVideos = new Vector<MyVideo>();
    Vector<MyVideo> myVideosKaraoke = new Vector<MyVideo>();
    VideoAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mv, container, false);
        txtMV = view.findViewById(R.id.tv_MV);
        txtKara = view.findViewById(R.id.tv_karaoke);
        mRecyclerViewVideoMV = view.findViewById(R.id.recycleViewVideo);
        mRecyclerViewKaraoke = view.findViewById(R.id.recycleKaraoke);
        EventMV();
        EventKaraoke();
        txtMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayVideoActivity.class);
                startActivity(intent);
            }
        });
        txtKara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KaraokeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void EventKaraoke() {
        mRecyclerViewKaraoke.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewKaraoke.setLayoutManager(linearLayoutManager);
        myVideosKaraoke.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/BexaYai23WU\" frameborder=\"0\" allowfullscreen></iframe>"));
        myVideosKaraoke.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/lcCqNbq5Iig\" frameborder=\"0\" allowfullscreen></iframe>"));
        myVideosKaraoke.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/o5T65Dafhj8\" frameborder=\"0\" allowfullscreen></iframe>"));
        myVideosKaraoke.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/ECceNjyKKtw\" frameborder=\"0\" allowfullscreen></iframe>"));
        myVideosKaraoke.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/2uI2NqTD1jo\" frameborder=\"0\" allowfullscreen></iframe>"));
        mAdapter = new VideoAdapter(myVideosKaraoke);
        mRecyclerViewKaraoke.setAdapter(mAdapter);
    }

    private void EventMV() {
        mRecyclerViewVideoMV.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewVideoMV.setLayoutManager(linearLayoutManager);
        myVideos.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/5vr0ySfmMj4\" frameborder=\"0\" allowfullscreen></iframe>"));
        myVideos.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/BwN3NiZt-PU\" frameborder=\"0\" allowfullscreen></iframe>"));
        myVideos.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/IpniN1Wq68Y\" frameborder=\"0\" allowfullscreen></iframe>"));
        myVideos.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/-64hVQZyp_Y\" frameborder=\"0\" allowfullscreen></iframe>"));
        myVideos.add(new MyVideo("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/os5z_aKqWTw\" frameborder=\"0\" allowfullscreen></iframe>"));
        mAdapter = new VideoAdapter(myVideos);
        mRecyclerViewVideoMV.setAdapter(mAdapter);
    }
}
