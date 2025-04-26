package com.ngdat.mymusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Activity.PlayMusicActivity;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachBaiHatAdapter extends RecyclerView.Adapter<DanhSachBaiHatAdapter.ViewHolder> {
    Context mContext;
    List<BaiHatYeuThich> list;

    public DanhSachBaiHatAdapter(Context mContext, List<BaiHatYeuThich> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_danhsachbaihat, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHatYeuThich baiHatYeuThich = list.get(position);
        holder.txtTenCS.setText(baiHatYeuThich.getCaSi());
        holder.txtTenBH.setText(baiHatYeuThich.getTenBaiHat());
        holder.txtSTT.setText(position + 1 + "");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgYeuThich;
        TextView txtSTT, txtTenBH, txtTenCS;

        public ViewHolder(View itemView) {
            super(itemView);
            imgYeuThich = itemView.findViewById(R.id.img_yeuThich);
            txtSTT = itemView.findViewById(R.id.tv_danhSachIndex);
            txtTenBH = itemView.findViewById(R.id.tv_tenCaKhuc);
            txtTenCS = itemView.findViewById(R.id.tv_TenCaSiBH);
            imgYeuThich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgYeuThich.setImageResource(R.drawable.iconloved);
                    DataService dataService = APIService.getService();
                    Call<String> mCall = dataService.getDataLuotLikeBaiHat("1", list.get(getPosition()).getIdBaiHat());
                    mCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String resuilt = response.body();
                            if (resuilt.equals("OK")) {
                                Toast.makeText(mContext, "Đã Thích Cám Ơn", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Please Check Again !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("TAG", t.toString());
                        }
                    });
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PlayMusicActivity.class);
                    intent.putExtra("cakhuc", list.get(getPosition()));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
