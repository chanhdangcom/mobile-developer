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

public class SearchBaiHatAdapter extends RecyclerView.Adapter<SearchBaiHatAdapter.ViewHolder> {
    Context mContext;
    List<BaiHatYeuThich> list;

    public SearchBaiHatAdapter(Context mContext, List<BaiHatYeuThich> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_timkiem_baihat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHatYeuThich baiHatYeuThich = list.get(position);
        holder.txtTenBaiHat.setText(baiHatYeuThich.getTenBaiHat());
        holder.txtCasi.setText(baiHatYeuThich.getCaSi());
        Picasso.get().load(baiHatYeuThich.getHinhBaiHat()).into(holder.imgBaiHat);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgBaiHat, imgLike;
        TextView txtTenBaiHat, txtCasi;

        public ViewHolder(View itemView) {
            super(itemView);
            imgBaiHat = itemView.findViewById(R.id.img_timkiem);
            imgLike = itemView.findViewById(R.id.img_SearchLuotThich);
            txtTenBaiHat = itemView.findViewById(R.id.tv_SearchBaiHat);
            txtCasi = itemView.findViewById(R.id.tv_SearchTenCaSi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PlayMusicActivity.class);
                    intent.putExtra("cakhuc", list.get(getPosition()));
                    mContext.startActivity(intent);
                }
            });
            imgLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgLike.setImageResource(R.drawable.iconloved);
                    DataService dataService = APIService.getService();
                    Call<String> call = dataService.getDataLuotLikeBaiHat("1", list.get(getPosition()).getIdBaiHat());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String str = response.body();
                            if (str.equals("OK")) {
                                Toast.makeText(mContext, "Bạn Đã Thích", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "Please check again !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                    imgLike.setEnabled(false);
                }
            });
        }
    }

}
