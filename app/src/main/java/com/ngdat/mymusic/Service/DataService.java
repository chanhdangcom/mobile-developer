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

// khi tương tác phía server,n~ yêu cầu thì phải gửi lên đúng cấu trúc thì server mới thực hiện
// dùng để gửi lên những phương thức để chúng ta tương tác phía server và sau khi server kết nối đc rồi
// nó sẽ trả dữ liệu về cho thằng này ==> thằng này dùng để gửi phướng thức và dữ liệu từ phía server về.
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
        // tương tác và gửi data lên và nhận về
    Call<List<BaiHat>> getDataBaiHatTheoQuangCao(@Field("idquangcao") String idquangcao);

    @FormUrlEncoded
    @POST("DanhSachBaiHatPlaylist.php")
        // tương tác và gửi data lên và nhận về
    Call<List<BaiHat>> getDataBaiHatTheoPlaylist(@Field("idplaylist") String idplaylist);

    @FormUrlEncoded
    @POST("DanhSachBaiHatPlaylist.php")
        // tương tác và gửi data lên và nhận về
    Call<List<BaiHat>> getDataBaiHatTheoTheLoai(@Field("idtheloai") String idtheloai);


    @FormUrlEncoded
    @POST("TheLoaiTheoChuDe.php")
        // tương tác và gửi data lên và nhận về

    @GET("AlbumAll.php")
    Call<List<Album>> getAllAlbum();

    @FormUrlEncoded
    @POST("DanhSachBaiHatPlaylist.php")
        // tương tác và gửi data lên và nhận về
    Call<List<BaiHat>> getDataBaiHatTheoAlbum(@Field("idalbum") String idalbum);

    @FormUrlEncoded
    @POST("UpdateLuotLike.php")
        // tương tác và gửi data lên và nhận về
    Call<String> getDataLuotLikeBaiHat(@Field("luotthich") String luotlike, @Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("SearchBH.php")
        // tương tác và gửi data lên và nhận về
    Call<List<BaiHat>> getSearchBaiHat(@Field("keyword") String keyword);
}
