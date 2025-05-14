package com.ngdat.mymusic.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.utils.DatabaseHelper;
import com.squareup.picasso.Picasso;
import com.ngdat.mymusic.Activity.PlayMusicActivity;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;

import java.util.List;
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<BaiHatYeuThich> baiHatYeuThichList;
    DatabaseHelper databaseHelper;
    int userId; // User ID should be retrieved once and kept as a class member

    public HistoryAdapter(Context mContext, List<BaiHatYeuThich> baiHatYeuThichList) {
        this.mContext = mContext;
        this.baiHatYeuThichList = baiHatYeuThichList;
        databaseHelper = new DatabaseHelper(mContext);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_song_history,parent, false);
        ViewHolder mViewHolder = new ViewHolder(v);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHatYeuThich baiHatYeuThich = baiHatYeuThichList.get(position);
        holder.txtTenCS.setText(baiHatYeuThich.getCaSi());
        holder.txtTenBH.setText(baiHatYeuThich.getTenBaiHat());
        holder.txtSTT.setText(String.valueOf(position + 1));
        Picasso.get().load(baiHatYeuThich.getHinhBaiHat()).into(holder.imgBaiHat);
    }

    @Override
    public int getItemCount() {
        return baiHatYeuThichList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSTT, txtTenBH, txtTenCS;
        ImageView imgBaiHat, imgYeuThich;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSTT = itemView.findViewById(R.id.tv_danhSachIndex);
            txtTenBH = itemView.findViewById(R.id.tv_tenCaKhuc);
            txtTenCS = itemView.findViewById(R.id.tv_TenCaSiBH);
            imgBaiHat = itemView.findViewById(R.id.img_baiHat);
            imgYeuThich = itemView.findViewById(R.id.img_yeuThich);

            // Mở bài hát khi click
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, PlayMusicActivity.class);
                intent.putExtra("cakhuc", baiHatYeuThichList.get(getAdapterPosition()));
                mContext.startActivity(intent);
            });

            // Chỉ đổi icon khi click trái tim (tùy chỉnh nếu cần thêm xử lý)
            imgYeuThich.setOnClickListener(v -> {
                imgYeuThich.setImageResource(R.drawable.iconloved);
            });
        }
    }
}
