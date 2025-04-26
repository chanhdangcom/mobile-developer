package com.ngdat.mymusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ngdat.mymusic.Activity.PlayMusicActivity;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.Service.APIService;
import com.ngdat.mymusic.Service.DataService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiHatAdapter extends RecyclerView.Adapter<BaiHatAdapter.ViewHolder> {
    Context mContext;
    List<BaiHatYeuThich> baiHatYeuThichList;

    public BaiHatAdapter(Context mContext, List<BaiHatYeuThich> baiHatYeuThichList) {
        this.mContext = mContext;
        this.baiHatYeuThichList = baiHatYeuThichList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_bai_hat_yeu_thich, parent, false);
        ViewHolder mViewHolder = new ViewHolder(v);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHatYeuThich baiHatYeuThich = baiHatYeuThichList.get(position);
        holder.txtTenCaSi.setText(baiHatYeuThich.getCaSi());
        holder.txtTenBaiHat.setText(baiHatYeuThich.getTenBaiHat());
        Picasso.get().load(baiHatYeuThich.getHinhBaiHat()).into(holder.imghinhBaihat);

    }

    @Override
    public int getItemCount() {
        return baiHatYeuThichList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLuotThich, imghinhBaihat;
        TextView txtTenBaiHat, txtTenCaSi;

        public ViewHolder(View itemView) {
            super(itemView);
            imghinhBaihat = itemView.findViewById(R.id.img_baihatyeuthich);
            imgLuotThich = itemView.findViewById(R.id.img_luotthich);
            txtTenBaiHat = itemView.findViewById(R.id.tv_tenBaiHat);
            txtTenCaSi = itemView.findViewById(R.id.tv_tenCaSi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PlayMusicActivity.class);
                    intent.putExtra("cakhuc", baiHatYeuThichList.get(getPosition()));
                    mContext.startActivity(intent);
                }
            });
            imgLuotThich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgLuotThich.setImageResource(R.drawable.iconloved);
                    DataService dataService = APIService.getService();
                    Call<String> mCall = dataService.getDataLuotLikeBaiHat("1", baiHatYeuThichList.get(getPosition()).getIdBaiHat());
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

                        }
                    });
                    imgLuotThich.setEnabled(false);
                }
            });

        }
    }
}
