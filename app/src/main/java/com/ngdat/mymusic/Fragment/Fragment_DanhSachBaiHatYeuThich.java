package com.ngdat.mymusic.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Adapter.BaiHatAdapter;
import com.ngdat.mymusic.Model.BaiHat;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;
import com.ngdat.mymusic.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_DanhSachBaiHatYeuThich extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    BaiHatAdapter mAdapter;
    DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bai_hat_yeuthich, container, false);
        mRecyclerView = view.findViewById(R.id.myRecycleBaiHatYeuThich);
        databaseHelper = new DatabaseHelper(getContext());
        GetData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GetData();
    }

    private void GetData() {
        DataService mDataService = APIService.getService();
        Call<List<BaiHat>> mCall = mDataService.getDataBaiHatDuocYeuThich();

        mCall.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                int userId = sharedPreferences.getInt("userId", -1);

                ArrayList<BaiHat> allSongs = (ArrayList<BaiHat>) response.body();
                List<Integer> favoriteSongIds = databaseHelper.getFavoriteSongs(userId);

                ArrayList<BaiHat> favoriteSongs = new ArrayList<>();
                for (BaiHat baiHat : allSongs) {
                    if (favoriteSongIds.contains(Integer.parseInt(baiHat.getIdBaiHat()))) {
                        favoriteSongs.add(baiHat);
                    }
                }

                mAdapter = new BaiHatAdapter(getActivity(), favoriteSongs);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Toast.makeText(getActivity(), "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}