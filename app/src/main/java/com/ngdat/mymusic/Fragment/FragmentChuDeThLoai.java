package com.ngdat.mymusic.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.ngdat.mymusic.Activity.DanhSachAllChuDeActivity;
import com.ngdat.mymusic.Activity.DanhSachTheLoaiTheoChuDeActivity;
import com.ngdat.mymusic.Activity.SongsListActivity;
import com.ngdat.mymusic.Model.ChuDe;
import com.ngdat.mymusic.Model.ChuDeAndTheLoai;
import com.ngdat.mymusic.Model.TheLoai;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChuDeThLoai extends Fragment {
    View view;
    HorizontalScrollView mHorizontalScrollView;
    TextView txtXemThem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chude_theloai, container, false);
        initView();
        GetData();
        return view;
    }

    private void initView() {
        mHorizontalScrollView = view.findViewById(R.id.myScollChudeTheLoai);
        txtXemThem = view.findViewById(R.id.tv_xemthem);
        txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachAllChuDeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetData() {
        DataService mDataService = APIService.getService();
        Call<ChuDeAndTheLoai> mCall = mDataService.getDataChuDeTheLoai();
        mCall.enqueue(new Callback<ChuDeAndTheLoai>() {
            @Override
            public void onResponse(Call<ChuDeAndTheLoai> call, Response<ChuDeAndTheLoai> response) {
                ChuDeAndTheLoai mChuDeAndTheLoai = response.body();
                final ArrayList<ChuDe> chuDeArrayList = new ArrayList<ChuDe>();
                // addAll() là add thêm 1 mảng cùng kiểu dữ liệu vào mảng chủ đề
                chuDeArrayList.addAll(mChuDeAndTheLoai.getChuDe());
                final ArrayList<TheLoai> theLoaiArrayList = new ArrayList<TheLoai>();
                // addAll() là add thêm 1 mảng cùng kiểu dữ liệu vào mảng thể loại
                theLoaiArrayList.addAll(mChuDeAndTheLoai.getTheLoai());

                LinearLayout mLinearLayout = new LinearLayout(getActivity());
                mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                // set lại kích thước cho layout
                LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(580, 250);
                mLayoutParams.setMargins(10, 20, 10, 30);

                for (int i = 0; i < chuDeArrayList.size(); i++) {
                    CardView mCardView = new CardView(getActivity());
                    mCardView.setRadius(10); // bo xung quanh 10dp
                    ImageView mImageView = new ImageView(getActivity());
                    mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (chuDeArrayList.get(i).getHinhChuDe() != null) {
                        Picasso.get().load(chuDeArrayList.get(i).getHinhChuDe()).into(mImageView);
                    }
                    mCardView.setLayoutParams(mLayoutParams);
                    mCardView.addView(mImageView);
                    mLinearLayout.addView(mCardView);
                    final int finalI = i;
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DanhSachTheLoaiTheoChuDeActivity.class);
                            intent.putExtra("chude", chuDeArrayList.get(finalI));
//                            Log.d("BBB", chuDeArrayList.get(0).getIDChuDe());
                            startActivity(intent);
                        }
                    });
                }
                for (int j = 0; j < theLoaiArrayList.size(); j++) {
                    CardView mCardView = new CardView(getActivity());
                    mCardView.setRadius(10); // bo xung quanh 10dp
                    ImageView mImageView = new ImageView(getActivity());
                    mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (theLoaiArrayList.get(j).getHinhTheLoai() != null) {
                        Picasso.get().load(theLoaiArrayList.get(j).getHinhTheLoai()).into(mImageView);
                    }
                    mCardView.setLayoutParams(mLayoutParams);
                    mCardView.addView(mImageView);
                    mLinearLayout.addView(mCardView);

                    final int finalJ = j;
                    mImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), SongsListActivity.class);
                            intent.putExtra("idtheloai", theLoaiArrayList.get(finalJ));
                            startActivity(intent);
                        }
                    });
                }
                mHorizontalScrollView.addView(mLinearLayout);
            }

            @Override
            public void onFailure(Call<ChuDeAndTheLoai> call, Throwable t) {

            }
        });
    }
}
