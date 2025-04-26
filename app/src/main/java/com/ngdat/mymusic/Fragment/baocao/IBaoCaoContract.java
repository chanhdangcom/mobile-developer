package com.ngdat.mymusic.Fragment.baocao;

import java.util.List;

public interface IBaoCaoContract {
    interface IView {
        void showSongs(List<Song> songs1,
                       List<Song> songs2,
                       List<Song> songs3,
                       List<Song> songs4,
                       List<Song> songs5
        );
    }

    interface IPresenter {
        void getSongs();
    }
}
