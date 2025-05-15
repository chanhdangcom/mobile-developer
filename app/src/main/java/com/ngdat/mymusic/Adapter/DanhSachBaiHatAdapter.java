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
import com.ngdat.mymusic.Model.BaiHat;
import com.ngdat.mymusic.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class DanhSachBaiHatAdapter extends RecyclerView.Adapter<DanhSachBaiHatAdapter.ViewHolder> {
    Context mContext;
    List<BaiHat> list;


    public DanhSachBaiHatAdapter(Context mContext, List<BaiHat> list) {
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
        BaiHat baiHat = list.get(position);
        holder.txtTenCS.setText(baiHat.getCaSi());
        holder.txtTenBH.setText(baiHat.getTenBaiHat());
        holder.txtSTT.setText(position + 1 + "");
        Picasso.get().load(baiHat.getHinhBaiHat()).into(holder.imgBaiHat);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgYeuThich;
        TextView txtSTT, txtTenBH, txtTenCS;
        ImageView imgBaiHat;

        public ViewHolder(View itemView) {
            super(itemView);
            imgYeuThich = itemView.findViewById(R.id.img_yeuThich);
            txtSTT = itemView.findViewById(R.id.tv_danhSachIndex);
            txtTenBH = itemView.findViewById(R.id.tv_tenCaKhuc);
            txtTenCS = itemView.findViewById(R.id.tv_TenCaSiBH);
            imgBaiHat = itemView.findViewById(R.id.img_baiHat);
            imgYeuThich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgYeuThich.setImageResource(R.drawable.iconloved);
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