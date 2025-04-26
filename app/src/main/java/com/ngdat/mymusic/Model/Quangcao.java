package com.ngdat.mymusic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Quangcao implements Serializable {

    @SerializedName("IdQuangCao")
    @Expose
    private String idQuangCao;
    @SerializedName("HinhAnh")
    @Expose
    private String hinhanh;
    @SerializedName("NoiDung")
    @Expose
    private String noidung;
    @SerializedName("IdBaiHat")
    @Expose
    private String idbaihat;
    @SerializedName("TenBaiHat")
    @Expose
    private String tenbaihat;
    @SerializedName("HinhBaiHat")
    @Expose
    private String hinhbaihat;

    public String getIdQuangCao() {
        return idQuangCao;
    }

    public void setIdQuangCao(String idQuangCao) {
        this.idQuangCao = idQuangCao;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getIdbaihat() {
        return idbaihat;
    }

    public void setIdbaihat(String idbaihat) {
        this.idbaihat = idbaihat;
    }

    public String getTenbaihat() {
        return tenbaihat;
    }

    public void setTenbaihat(String tenbaihat) {
        this.tenbaihat = tenbaihat;
    }

    public String getHinhbaihat() {
        return hinhbaihat;
    }

    public void setHinhbaihat(String hinhbaihat) {
        this.hinhbaihat = hinhbaihat;
    }

}