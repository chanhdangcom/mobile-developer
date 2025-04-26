package com.ngdat.mymusic.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Adapter.SearchBaiHatAdapter;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_TimKiem extends Fragment {
    View view;
    Toolbar toolbar;
    RecyclerView mRecyclerView;
    TextView mTextViewKoCoData;
    SearchBaiHatAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tim_kiem, container, false);
        toolbar = view.findViewById(R.id.toolbartimkiembaihat);
        mTextViewKoCoData = view.findViewById(R.id.tv_DataNull);
        mRecyclerView = view.findViewById(R.id.recycleviewTimKiem);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_view, menu);
        MenuItem menuItem = menu.findItem(R.id.mySearchBH);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchBaiHat(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void SearchBaiHat(String keyword) {
        DataService dataService = APIService.getService();
        Call<List<BaiHatYeuThich>> call = dataService.getSearchBaiHat(keyword);
        call.enqueue(new Callback<List<BaiHatYeuThich>>() {
            @Override
            public void onResponse(Call<List<BaiHatYeuThich>> call, Response<List<BaiHatYeuThich>> response) {
                List<BaiHatYeuThich> listBH = response.body();
                if (listBH.size() > 0) {
                    mAdapter = new SearchBaiHatAdapter(getActivity(), listBH);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerView.setAdapter(mAdapter);
                    mTextViewKoCoData.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    mTextViewKoCoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<BaiHatYeuThich>> call, Throwable t) {

            }
        });
    }
}
