package com.ngdat.mymusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Model.BaiHatYeuThich;
import com.ngdat.mymusic.R;

import java.util.List;

public class PlayMusicAdapter extends RecyclerView.Adapter<PlayMusicAdapter.ViewHolder> {
    Context mContext;
    List<BaiHatYeuThich> listBH;

    public PlayMusicAdapter(Context mContext, List<BaiHatYeuThich> listBH) {
        this.mContext = mContext;
        this.listBH = listBH;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_play_music, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHatYeuThich baihat = listBH.get(position);
        holder.txtTenCaSi.setText(baihat.getCaSi());
        holder.txtTenBaiHat.setText(baihat.getTenBaiHat());
        holder.txtIndex.setText(position + 1 + "");
    }

    @Override
    public int getItemCount() {
        return listBH.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenCaSi, txtTenBaiHat, txtIndex;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTenBaiHat = itemView.findViewById(R.id.tv_playnhacTenbaihat);
            txtIndex = itemView.findViewById(R.id.tv_playnhacindex);
            txtTenCaSi = itemView.findViewById(R.id.tv_tenCaSiPlayMusic);
        }
    }
}
