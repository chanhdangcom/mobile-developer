package com.ngdat.mymusic.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Activity.PlayMusicActivity;
import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<BaiHatYeuThich> list;

    public HistoryAdapter(Context mContext, List<BaiHatYeuThich> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_history_songs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        BaiHatYeuThich baiHat = list.get(position);
        holder.txtTenCS.setText(baiHat.getCaSi());
        holder.txtTenBH.setText(baiHat.getTenBaiHat());
        holder.txtSTT.setText(String.valueOf(position + 1));
        Picasso.get().load(baiHat.getHinhBaiHat()).into(holder.imgBaiHat);
    }

    @Override
    public int getItemCount() {
        return list.size();
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
                intent.putExtra("cakhuc", list.get(getAdapterPosition()));
                mContext.startActivity(intent);
            });

            // Chỉ đổi icon khi click trái tim (tùy chỉnh nếu cần thêm xử lý)
            imgYeuThich.setOnClickListener(v -> {
                imgYeuThich.setImageResource(R.drawable.iconloved);
            });
        }
    }
}
