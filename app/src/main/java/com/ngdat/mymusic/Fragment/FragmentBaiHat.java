package com.ngdat.mymusic.Fragment;

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
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBaiHat extends Fragment {
    View view;
    RecyclerView mRecyclerView;
    BaiHatAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bai_hat_yeuthich, container, false);
        mRecyclerView = view.findViewById(R.id.myRecycleBaiHatYeuThich);
        GetData();
        return view;
    }

    private void GetData() {
        DataService mDataService = APIService.getService();
        Call<List<BaiHatYeuThich>> mCall = mDataService.getDataBaiHatDuocYeuThich();
        mCall.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
                ArrayList<BaiHatYeuThich> baiHatYeuThichArrayList = (ArrayList<BaiHatYeuThich>) response.body();
                mAdapter = new BaiHatAdapter(getActivity(), baiHatYeuThichArrayList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {
                Toast.makeText(getActivity(), " Please Check Your Internet Again !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
