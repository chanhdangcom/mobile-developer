package com.ngdat.mymusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ngdat.mymusic.Model.YoutubeMusic;
import com.ngdat.mymusic.R;

import java.util.List;

public class VideoYoutubeAdapter extends BaseAdapter {
    private final Context context;
    private final List<YoutubeMusic> musicList;

    public VideoYoutubeAdapter(Context context, List<YoutubeMusic> musicList) {
        this.context = context;
        this.musicList = musicList;
    }

    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_video_youtube, null);
            holder.txtTitle = convertView.findViewById(R.id.tv_titleVideo);
            holder.imageViewThumbnail = convertView.findViewById(R.id.img_thumbnail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            YoutubeMusic youtubeMusic = musicList.get(position);
            holder.txtTitle.setText(youtubeMusic.getmTitle());
            Picasso.get()
                    .load(youtubeMusic.getmThumbnail())
                    .into(holder.imageViewThumbnail);
        }
        return convertView;
    }

    private class ViewHolder {
        ImageView imageViewThumbnail;
        TextView txtTitle;
    }
}
