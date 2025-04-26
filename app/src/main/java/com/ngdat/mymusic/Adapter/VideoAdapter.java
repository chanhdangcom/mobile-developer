package com.ngdat.mymusic.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Model.MyVideo;
import com.ngdat.mymusic.R;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    List<MyVideo> youtubeVideoList;

    public VideoAdapter(List<MyVideo> youtubeVideoList) {
        this.youtubeVideoList = youtubeVideoList;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_video, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.videoWeb.loadData(youtubeVideoList.get(position).getmVideoUrl(), "text/html", "utf-8");
    }

    @Override
    public int getItemCount() {
        return youtubeVideoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        WebView videoWeb;

        public VideoViewHolder(View itemView) {
            super(itemView);
            videoWeb = itemView.findViewById(R.id.myWebViewVideoView);

            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient() {


            });
        }
    }
}
