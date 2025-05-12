package com.ngdat.mymusic.Fragment;

import static com.ngdat.mymusic.utils.SongLoader.songsList;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Activity.MainActivity;
import com.ngdat.mymusic.Activity.MusicPlayerActivity;
import com.ngdat.mymusic.Adapter.SongListAdapter;
import com.ngdat.mymusic.Model.Song;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.MediaPlayerService;
import com.ngdat.mymusic.utils.AudioPlayerUtils;
import com.ngdat.mymusic.utils.MyMediaPlayer;
import com.ngdat.mymusic.utils.SongLoader;

import java.io.IOException;

public class Fragment_device_music extends Fragment implements SongListAdapter.OnSongClickListener{
    View view;
    private RecyclerView recyclerView;
    private TextView noMusicAvailable;
    private SongListAdapter songListAdapter;

    private final MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device_music, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
        noMusicAvailable = view.findViewById(R.id.no_music_available);

        if (SongLoader.loadSongs(requireContext()).isEmpty()) {
            noMusicAvailable.setVisibility(View.VISIBLE);
        } else {
            noMusicAvailable.setVisibility(View.GONE);
        }

        songListAdapter = new SongListAdapter(requireContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(songListAdapter);
    }

    @Override
    public void onSongClick(Song song, int position) {
        MyMediaPlayer.currentIndex = position;
        AudioPlayerUtils.playAudio(requireContext());

        Intent intent = new Intent(requireContext(), MusicPlayerActivity.class);
        intent.putExtra("selected_song", song); // nhớ song phải implements Serializable hoặc Parcelable
        startActivity(intent);
    }
}
