package com.ngdat.mymusic.Fragment.baocao;

public class BaoCaoPresenter implements IBaoCaoContract.IPresenter {
    private final BaoCaoModel baoCaoModel;
    private final IBaoCaoContract.IView iView;

    public BaoCaoPresenter(IBaoCaoContract.IView iView) {
        this.iView = iView;
        baoCaoModel = new BaoCaoModel();
    }

    @Override
    public void getSongs() {
        iView.showSongs(
                baoCaoModel.getSongs1(),
                baoCaoModel.getSongs2(),
                baoCaoModel.getSongs3(),
                baoCaoModel.getSongs4(),
                baoCaoModel.getSongs5()
        );
    }
}
