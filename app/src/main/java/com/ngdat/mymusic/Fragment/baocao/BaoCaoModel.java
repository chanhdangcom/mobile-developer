package com.ngdat.mymusic.Fragment.baocao;

import android.graphics.Color;

import com.ngdat.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class BaoCaoModel {
    private final List<Song> songs1;
    private final List<Song> songs2;
    private final List<Song> songs3;
    private final List<Song> songs4;
    private final List<Song> songs5;

    public BaoCaoModel() {
        songs1 = new ArrayList<>();
        songs1.add(new Song(1, Color.BLACK, 2.1f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(2.8f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(3.2f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(4.1f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(4.3f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(5.0f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(6.9f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(10.0f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(9.5f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(8.0f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(7.5f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));
        songs1.add(new Song(7.1f, R.drawable.quantronglathanthai, "Quan trọng là thần thái", "OnlyC, Karik"));

        songs2 = new ArrayList<>();
        songs2.add(new Song(2, Color.RED, 2.2f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(2.4f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(3.8f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(3.9f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(4.6f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(5.4f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(6.1f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(3.0f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(3.5f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(4.2f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(7.0f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));
        songs2.add(new Song(7.5f, R.drawable.nguoiamphu, "Người âm phủ", "OSAD, VRT"));

        songs3 = new ArrayList<>();
        songs3.add(new Song(3, Color.GREEN, 1.2f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(1.4f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(2.2f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(3.1f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(4.6f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(4.9f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(5.9f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(8.0f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(9.0f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(7.0f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(6.5f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));
        songs3.add(new Song(6.1f, R.drawable.rungchuongvang, "Rung Chuông Vàng", "Bức Tường"));

        songs4 = new ArrayList<>();
        songs4.add(new Song(4, Color.BLUE, 6.1f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(4.8f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(5.2f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(4.8f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(4.0f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(4.0f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(5.9f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(7.0f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(8.5f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(8.3f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(5.5f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));
        songs4.add(new Song(5.1f, R.drawable.votinh, "Vô tình", "Xesi, Hoaprox"));

        songs5 = new ArrayList<>();
        songs5.add(new Song(5, Color.YELLOW, 7.1f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(6.8f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(6.5f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(7.1f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(5.3f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(5.8f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(6.9f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(7.0f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(8.5f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(8.2f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(8.8f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
        songs5.add(new Song(9.1f, R.drawable.meyeuoi, "Mẹ yêu ơi", "Bé Gia Khiêm"));
    }

    public List<Song> getSongs1() {
        return songs1;
    }

    public List<Song> getSongs2() {
        return songs2;
    }

    public List<Song> getSongs3() {
        return songs3;
    }

    public List<Song> getSongs4() {
        return songs4;
    }

    public List<Song> getSongs5() {
        return songs5;
    }

}


