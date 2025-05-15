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
    int userId; // User ID should be retrieved once and kept as a class member

    public BaiHatAdapter(Context mContext, List<BaiHat> baiHatList) {
        this.mContext = mContext;
        this.baiHatList = baiHatList;
        databaseHelper = new DatabaseHelper(mContext);
        // Retrieve userId once when the adapter is created
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_bai_hat_yeu_thich, parent, false);
        ViewHolder mViewHolder = new ViewHolder(v);
        // userId is already retrieved in the constructor, no need to do it here
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = baiHatList.get(position);
        holder.txtTenCaSi.setText(baiHat.getCaSi());
        holder.txtTenBaiHat.setText(baiHat.getTenBaiHat());
        Picasso.get().load(baiHat.getHinhBaiHat()).into(holder.imghinhBaihat);

        // --- Determine songId and initial favorite status for THIS specific item ---
        // Declare songId as a final local variable within onBindViewHolder
        final int songId = Integer.parseInt(baiHat.getIdBaiHat());
        // Determine initial favorite status
        boolean initialIsFavorited = databaseHelper.isFavoriteExists(userId, songId);

        // Update the heart icon based on the initial favorite status
        if (initialIsFavorited) {
            holder.imgLuotThich.setImageResource(R.drawable.iconloved);
        } else {
            holder.imgLuotThich.setImageResource(R.drawable.iconlove);
        }

        // --- Set up the click listener for the favorite button ---
        holder.imgLuotThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId == -1) {
                    Toast.makeText(mContext, "Please log in to favorite songs.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Re-check the current favorite status when clicked, using the correct final songId
                boolean currentIsFavorited = databaseHelper.isFavoriteExists(userId, songId);

                if (currentIsFavorited) {
                    // Song is currently favorited, so unfavorite it
                    boolean success = databaseHelper.deleteFavorite(userId, songId);
                    if (success) {
                        Toast.makeText(mContext, "Đã xoá yêu thích", Toast.LENGTH_SHORT).show();
                        Log.d("FAVORITES", "Xoá thành công: userId=" + userId + ", songId=" + songId);
                        holder.imgLuotThich.setImageResource(R.drawable.iconlove); // Update icon immediately
                        // Optional: Remove the item from the list and notify the adapter
                        // if this adapter is used to display only favorite songs
                        // baiHatYeuThichList.remove(holder.getAdapterPosition());
                        // notifyItemRemoved(holder.getAdapterPosition());
                    } else {
                        Toast.makeText(mContext, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("FAVORITES", "Xoá thất bại: userId=" + userId + ", songId=" + songId);
                    }
                } else {
                    // Song is not favorited, so favorite it
                    boolean success = databaseHelper.addFavorite(userId, songId);
                    if (success) {
                        Toast.makeText(mContext, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        Log.d("FAVORITES", "Thêm thành công: userId=" + userId + ", songId=" + songId);
                        holder.imgLuotThich.setImageResource(R.drawable.iconloved); // Update icon immediately
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

            // Set up the click listener for the entire item view
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
            // The favorite button click listener is now set in onBindViewHolder
            // to ensure it uses the correct data for each item.
        }
    }
    // Removed unused class members songId and isFavorited
    /*
    int userId, songId;
    boolean isFavorited;
    */
}
