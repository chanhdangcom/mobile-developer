package com.ngdat.mymusic.Model;

public class YoutubeMusic {
    private String mTitle;
    private String mIdVideo;
    private String mThumbnail;
    private String mChanalTitle;

    public YoutubeMusic(String mTitle, String mIdVideo, String mThumbnail, String mChanalTitle) {
        this.mTitle = mTitle;
        this.mIdVideo = mIdVideo;
        this.mThumbnail = mThumbnail;
        this.mChanalTitle = mChanalTitle;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmIdVideo() {
        return mIdVideo;
    }

    public void setmIdVideo(String mIdVideo) {
        this.mIdVideo = mIdVideo;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public void setmThumbnail(String mThumbnail) {
        this.mThumbnail = mThumbnail;
    }

    public String getmChanalTitle() {
        return mChanalTitle;
    }

    public void setmChanalTitle(String mChanalTitle) {
        this.mChanalTitle = mChanalTitle;
    }
}
