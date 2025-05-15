package com.ngdat.mymusic.Service;


import com.ngdat.mymusic.Model.Album;
import com.ngdat.mymusic.Model.BaiHat;
import com.ngdat.mymusic.Model.Playlist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface DataService {



        // nhận dữ liệu

    @GET("PlaylistSong.php")
    Call<List<Playlist>> getDataPlaylist();

    @GET("albumSong.php")
    Call<List<Album>> getDataAlbum();

    @GET("BaiHatDuocYeuThich.php")
    Call<List<BaiHat>> getDataBaiHatDuocYeuThich();

    @FormUrlEncoded
    @POST("DanhSachBaiHat.php")
    Call<List<BaiHat>> getDataBaiHatTheoQuangCao(@Field("idquangcao") String idquangcao);

    @FormUrlEncoded
    @POST("DanhSachBaiHatPlaylist.php")
    Call<List<BaiHat>> getDataBaiHatTheoPlaylist(@Field("idplaylist") String idplaylist);

    @FormUrlEncoded
    @POST("DanhSachBaiHatPlaylist.php")
    Call<List<BaiHat>> getDataBaiHatTheoTheLoai(@Field("idtheloai") String idtheloai);

    @FormUrlEncoded
    @POST("TheLoaiTheoChuDe.php")

    @GET("AlbumAll.php")
    Call<List<Album>> getAllAlbum();

    @FormUrlEncoded
    @POST("DanhSachBaiHatPlaylist.php")
    Call<List<BaiHat>> getDataBaiHatTheoAlbum(@Field("idalbum") String idalbum);

    @FormUrlEncoded
    @POST("UpdateLuotLike.php")
    Call<String> getDataLuotLikeBaiHat(@Field("luotthich") String luotlike, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("SearchBH.php")
    Call<List<BaiHat>> getSearchBaiHat(@Field("keyword") String keyword);
}
