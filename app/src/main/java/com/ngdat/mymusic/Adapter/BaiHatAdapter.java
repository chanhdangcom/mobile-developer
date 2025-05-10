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
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;

import java.util.List;

public class BaiHatAdapter extends RecyclerView.Adapter<BaiHatAdapter.ViewHolder> {
    Context mContext;
    List<BaiHatYeuThich> baiHatYeuThichList;
    DatabaseHelper databaseHelper;

    public BaiHatAdapter(Context mContext, List<BaiHatYeuThich> baiHatYeuThichList) {
        this.mContext = mContext;
        this.baiHatYeuThichList = baiHatYeuThichList;
        databaseHelper = new DatabaseHelper(mContext);
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
                    int userId;
                    SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    userId = sharedPreferences.getInt("userId", -1);
                    int songId = Integer.parseInt(baiHatYeuThichList.get(getPosition()).getIdBaiHat());


                    boolean success = databaseHelper.addFavorite(userId, songId);
                    if (success) {
                        Toast.makeText(mContext, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        Log.d("FAVORITES", "Thêm thành công: userId=" + userId + ", songId=" + songId);
                        Log.d("DEBUG", "userId: " + userId);

                    } else {
                        Toast.makeText(mContext, "Thêm thất bại (có thể đã tồn tại)", Toast.LENGTH_SHORT).show();
                        Log.e("FAVORITES", "Thêm thất bại: userId=" + userId + ", songId=" + songId);
                    }
                    imgLuotThich.setEnabled(false);
                }
            });

        }
    }
}
