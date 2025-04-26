package com.ngdat.mymusic.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ngdat.mymusic.Activity.PlaylistActivity;
import com.ngdat.mymusic.Activity.SongsListActivity;
import com.ngdat.mymusic.Adapter.PlaylistAdapter;
import com.ngdat.mymusic.Model.Playlist;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentPlaylist extends Fragment {
    View view;
    ListView mListViewPlaylist;
    TextView txtTiltlePlaylist, txtPlaylistGanDay;
    PlaylistAdapter mPlaylistAdapter;
    List<Playlist> mList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        intitView();
        GetData();
        ActionView();
        return view;
    }

    private void ActionView() {
        txtPlaylistGanDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlaylistActivity.class);
                startActivity(intent);
            }
        });
    }

    private void intitView() {
        mListViewPlaylist = view.findViewById(R.id.lv_playlist);
        txtTiltlePlaylist = view.findViewById(R.id.tv_titlePlaylist);
        txtPlaylistGanDay = view.findViewById(R.id.tv_morePlaylist);
    }

    private void GetData() {
        DataService mDataService = APIService.getService();
        Call<List<Playlist>> mCall = mDataService.getDataPlaylist();
        mCall.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                mList = response.body();
                mPlaylistAdapter = new PlaylistAdapter(getActivity(), android.R.layout.simple_list_item_1, mList);
                mListViewPlaylist.setAdapter(mPlaylistAdapter);
                setListViewHeightBasedOnChildren(mListViewPlaylist);
                mListViewPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), SongsListActivity.class);
                        intent.putExtra("itemPlaylist", mList.get(position));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });


    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
