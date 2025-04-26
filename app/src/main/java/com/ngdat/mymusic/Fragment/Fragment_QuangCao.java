package com.ngdat.mymusic.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ngdat.mymusic.Adapter.QuangCaoAdapter;
import com.ngdat.mymusic.Model.Quangcao;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_QuangCao extends Fragment {
    View view;
    ViewPager mViewPager;
    QuangCaoAdapter mAdapter;
    CircleIndicator mCircleIndicator;
    Runnable mRunnable;
    Handler mHandler;
    int item;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_quangcao, container, false);
        initView();
        GetBanner();
        return view;
    }

    public void initView() {
        mViewPager = view.findViewById(R.id.viewPager);
        mCircleIndicator = view.findViewById(R.id.myIndicator);

    }

    public void GetBanner() {
        DataService mDataService = APIService.getService(); // khởi tạo retrofit để đẩy lên
        Call<List<Quangcao>> listCall = mDataService.getDataBanner();
        // enqueue() sự kiện lắng nghe
        listCall.enqueue(new Callback<List<Quangcao>>() {
            @Override
            public void onResponse(Call<List<Quangcao>> call, Response<List<Quangcao>> response) {
                // lấy dữ liệu ra
                final ArrayList<Quangcao> arrayListBanner = (ArrayList<Quangcao>) response.body();
                mAdapter = new QuangCaoAdapter(getActivity(), arrayListBanner);
                mViewPager.setAdapter(mAdapter);
                // hiện ra số lượng indicator tùy theo số lượng pager
                mCircleIndicator.setViewPager(mViewPager);
                mHandler = new Handler();
                // thực hiện hành động khi handler gọi
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // item hiện tại đang đứng ở đâu
                        item = mViewPager.getCurrentItem();
                        item++;
//                        // nếu  vượt quá kích thức page thì trở lại pager đầu
                        assert arrayListBanner != null;
                        if (item >= arrayListBanner.size()) {
                            item = 0;
                        }
                        // chạy xong set dữ liệu lên
                        mViewPager.setCurrentItem(item, true);
                        //mHandler.postDelayed(mRunnable, 4000);
                    }

                };
                mHandler.postDelayed(mRunnable, 4000);
            }

            @Override
            public void onFailure(Call<List<Quangcao>> call, Throwable t) {

            }
        });
    }
}
