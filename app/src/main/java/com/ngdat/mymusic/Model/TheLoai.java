package com.ngdat.mymusic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TheLoai implements Serializable {

    @SerializedName("IdTheLoai")
    @Expose
    private String iDTheLoai;
    @SerializedName("IdKeyChuDe")
    @Expose
    private String iDKeyChuDe;
    @SerializedName("TenTheLoai")
    @Expose
    private String tenTheLoai;
    @SerializedName("HinhTheLoai")
    @Expose
    private String hinhTheLoai;

    public String getIDTheLoai() {
        return iDTheLoai;
    }

    public void setIDTheLoai(String iDTheLoai) {
        this.iDTheLoai = iDTheLoai;
    }

    public String getIDKeyChuDe() {
        return iDKeyChuDe;
    }

    public void setIDKeyChuDe(String iDKeyChuDe) {
        this.iDKeyChuDe = iDKeyChuDe;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public String getHinhTheLoai() {
        return hinhTheLoai;
    }

    public void setHinhTheLoai(String hinhTheLoai) {
        this.hinhTheLoai = hinhTheLoai;
    }

}