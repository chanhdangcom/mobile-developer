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

import com.squareup.picasso.Picasso;
import com.ngdat.mymusic.Activity.SongsListActivity;
import com.ngdat.mymusic.Model.TheLoai;
import com.ngdat.mymusic.R;

import java.util.List;

public class DanhSachTheLoaiTheoChuDeAdapter extends RecyclerView.Adapter<DanhSachTheLoaiTheoChuDeAdapter.ViewHolder> {
    Context mContext;
    List<TheLoai> mTheLoais;

    public DanhSachTheLoaiTheoChuDeAdapter(Context mContext, List<TheLoai> mTheLoais) {
        this.mContext = mContext;
        this.mTheLoais = mTheLoais;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.item_the_loai_theo_chu_de, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TheLoai theLoai = mTheLoais.get(position);
        Picasso.get().load(theLoai.getHinhTheLoai()).into(holder.imgTheoChuDe);
        holder.txtTheochude.setText(theLoai.getTenTheLoai());
    }

    @Override
    public int getItemCount() {
        return mTheLoais.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgTheoChuDe;
        TextView txtTheochude;

        public ViewHolder(View itemView) {
            super(itemView);
            imgTheoChuDe = itemView.findViewById(R.id.img_theloaitheochude);
            txtTheochude = itemView.findViewById(R.id.tv_theloaitheochude);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SongsListActivity.class);
                    intent.putExtra("idtheloai", mTheLoais.get(getPosition()));
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
