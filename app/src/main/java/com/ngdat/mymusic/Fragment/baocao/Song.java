package com.ngdat.mymusic.Fragment.baocao;

public class Song {
    private final float rank;
    private final int avatar;
    private final String song;
    private final String songer;
    private int no;
    private int color;

    public Song(int no, int color, float rank, int avatar, String song, String songer) {
        this.no = no;
        this.color = color;
        this.rank = rank;
        this.avatar = avatar;
        this.song = song;
        this.songer = songer;
    }

    public Song(float rank, int avatar, String song, String songer) {
        this.rank = rank;
        this.avatar = avatar;
        this.song = song;
        this.songer = songer;
    }

    public int getNo() {
        return no;
    }

    public int getColor() {
        return color;
    }

    public float getRank() {
        return rank;
    }

    public int getAvatar() {
        return avatar;
    }

    public String getSong() {
        return song;
    }

    public String getSonger() {
        return songer;
    }
}
