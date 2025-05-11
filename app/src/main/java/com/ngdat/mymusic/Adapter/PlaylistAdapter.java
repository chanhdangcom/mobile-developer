package com.ngdat.mymusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Model.Playlist;
import com.ngdat.mymusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    Context context;
    List<Playlist> mangPlaylist;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Playlist playlist);
    }

    public PlaylistAdapter(Context context, List<Playlist> mangPlaylist, OnItemClickListener listener) {
        this.context = context;
        this.mangPlaylist = mangPlaylist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = mangPlaylist.get(position);
        holder.txtNamePlaylist.setText(playlist.getTen());
        Picasso.get().load(playlist.getHinhAnhPlaylist()).into(holder.imgBackgroud);
        Picasso.get().load(playlist.getIcon()).into(holder.imgPlaylist);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(playlist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangPlaylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNamePlaylist;
        ImageView imgBackgroud, imgPlaylist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNamePlaylist = itemView.findViewById(R.id.tv_NamePlaylist);
            imgBackgroud = itemView.findViewById(R.id.img_backgroundPlaylist);
            imgPlaylist = itemView.findViewById(R.id.img_Playlist);
        }
    }
}