package com.ngdat.mymusic.Model;

import java.io.Serializable;

public class Song implements Serializable {

    private String path;
    private String title;
    private String duration;
    private byte[] embeddedPicture;

    public Song(String path, String title, String duration, byte[] embeddedPicture) {
        this.path = path;
        this.title = title;
        this.duration = duration;
        this.embeddedPicture = embeddedPicture;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public byte[] getEmbeddedPicture() {
        return embeddedPicture;
    }
}
