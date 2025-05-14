package com.ngdat.mymusic.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.ngdat.mymusic.Adapter.BaiHatAdapter;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.utils.DatabaseHelper;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.ngdat.mymusic.Adapter.HistoryAdapter;

public class Fragment_History extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    HistoryAdapter mAdapter;
    DatabaseHelper databaseHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history_songs, container, false);
        mRecyclerView = view.findViewById(R.id.myRecycleHistory);
        databaseHelper = new DatabaseHelper(getContext());
        GetData();
        return view;
    }
    public void onResume() {
        super.onResume();
        GetData(); // gọi lại API hoặc load lại dữ liệu mỗi lần vào Fragment
    }
    private void GetData() {
        DataService mDataService = APIService.getService();
        Call<List<BaiHatYeuThich>> mCall = mDataService.getDataBaiHatDuocYeuThich();
        mCall.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
                SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                int userId = prefs.getInt("userId", -1);

                ArrayList<BaiHatYeuThich> allSongs = (ArrayList<BaiHatYeuThich>) response.body();

                List<Integer> historyIds = databaseHelper.getHistorySongIds(userId);
                // lọc lichj sử nghe
                ArrayList<BaiHatYeuThich> historySongs = new ArrayList<>();
                for (BaiHatYeuThich baiHat : allSongs)
                {
                    if (historyIds.contains(Integer.parseInt(baiHat.getIdBaiHat()))) {
                        historySongs.add(baiHat);
                    }
                }
                mAdapter = new HistoryAdapter(getActivity(), historySongs);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed to load history", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
