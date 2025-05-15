package com.ngdat.mymusic.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.utils.DatabaseHelper;
import com.squareup.picasso.Picasso;
import com.ngdat.mymusic.Activity.PlayMusicActivity;
import com.ngdat.mymusic.Model.BaiHat;
import com.ngdat.mymusic.R;

import java.util.List;

public class BaiHatAdapter extends RecyclerView.Adapter<BaiHatAdapter.ViewHolder> {
    Context mContext;
    List<BaiHat> baiHatList;
    DatabaseHelper databaseHelper;
    int userId;

    public BaiHatAdapter(Context mContext, List<BaiHat> baiHatList) {
        this.mContext = mContext;
        this.baiHatList = baiHatList;
        databaseHelper = new DatabaseHelper(mContext);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
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
        BaiHat baiHat = baiHatList.get(position);
        holder.txtTenCaSi.setText(baiHat.getCaSi());
        holder.txtTenBaiHat.setText(baiHat.getTenBaiHat());
        Picasso.get().load(baiHat.getHinhBaiHat()).into(holder.imghinhBaihat);
        final int songId = Integer.parseInt(baiHat.getIdBaiHat());
        boolean initialIsFavorited = databaseHelper.isFavoriteExists(userId, songId);

        if (initialIsFavorited) {
            holder.imgLuotThich.setImageResource(R.drawable.iconloved);
        } else {
            holder.imgLuotThich.setImageResource(R.drawable.iconlove);
        }

        holder.imgLuotThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == -1) {
                    Toast.makeText(mContext, "Please log in to favorite songs.", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean currentIsFavorited = databaseHelper.isFavoriteExists(userId, songId);

                if (currentIsFavorited) {
                    boolean success = databaseHelper.deleteFavorite(userId, songId);
                    if (success) {
                        Toast.makeText(mContext, "Đã xoá yêu thích", Toast.LENGTH_SHORT).show();
                        Log.d("FAVORITES", "Xoá thành công: userId=" + userId + ", songId=" + songId);
                        holder.imgLuotThich.setImageResource(R.drawable.iconlove);
                    } else {
                        Toast.makeText(mContext, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("FAVORITES", "Xoá thất bại: userId=" + userId + ", songId=" + songId);
                    }
                } else {
                    boolean success = databaseHelper.addFavorite(userId, songId);
                    if (success) {
                        Toast.makeText(mContext, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        Log.d("FAVORITES", "Thêm thành công: userId=" + userId + ", songId=" + songId);
                        holder.imgLuotThich.setImageResource(R.drawable.iconloved);
                    } else {
                        Toast.makeText(mContext, "Thêm thất bại (có thể đã tồn tại)", Toast.LENGTH_SHORT).show();
                        Log.e("FAVORITES", "Thêm thất bại: userId=" + userId + ", songId=" + songId);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return baiHatList.size();
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
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(mContext, PlayMusicActivity.class);
                        intent.putExtra("cakhuc", baiHatList.get(position));
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
