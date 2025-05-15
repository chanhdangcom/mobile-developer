package com.ngdat.mymusic.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class History_songs implements Parcelable {
    public static final Creator<History_songs> CREATOR = new Creator<History_songs>() {
        @Override
        public History_songs createFromParcel(Parcel in) {
            return new History_songs(in);
        }

        @Override
        public History_songs[] newArray(int size) {
            return new History_songs[size];
        }
    };

    @SerializedName("IdBaiHat")
    @Expose
    private String idBaiHat;

    @SerializedName("TenBaiHat")
    @Expose
    private String tenBaiHat;

    @SerializedName("HinhBaiHat")
    @Expose
    private String hinhBaiHat;

    @SerializedName("CaSi")
    @Expose
    private String caSi;

    @SerializedName("LinkBaiHat")
    @Expose
    private String linkBaiHat;

    @SerializedName("ThoiGianNghe")
    @Expose
    private String thoiGianNghe;

    // Constructor
    public History_songs(String idBaiHat, String tenBaiHat, String hinhBaiHat, String caSi, String linkBaiHat, String thoiGianNghe) {
        this.idBaiHat = idBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.hinhBaiHat = hinhBaiHat;
        this.caSi = caSi;
        this.linkBaiHat = linkBaiHat;
        this.thoiGianNghe = thoiGianNghe;
    }

    // Parcelable constructor
    protected History_songs(Parcel in) {
        idBaiHat = in.readString();
        tenBaiHat = in.readString();
        hinhBaiHat = in.readString();
        caSi = in.readString();
        linkBaiHat = in.readString();
        thoiGianNghe = in.readString();
    }


    // Getter vaf Setter
    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getHinhBaiHat() {
        return hinhBaiHat;
    }

    public void setHinhBaiHat(String hinhBaiHat) {
        this.hinhBaiHat = hinhBaiHat;
    }

    public String getCaSi() {
        return caSi;
    }

    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }

    public String getLinkBaiHat() {
        return linkBaiHat;
    }

    public void setLinkBaiHat(String linkBaiHat) {
        this.linkBaiHat = linkBaiHat;
    }

    public String getThoiGianNghe() {
        return thoiGianNghe;
    }

    public void setThoiGianNghe(String thoiGianNghe) {
        this.thoiGianNghe = thoiGianNghe;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idBaiHat);
        dest.writeString(tenBaiHat);
        dest.writeString(hinhBaiHat);
        dest.writeString(caSi);
        dest.writeString(linkBaiHat);
        dest.writeString(thoiGianNghe);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
