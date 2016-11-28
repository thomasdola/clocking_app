package com.eurekacachet.clocling.ui.view.clocking;


import android.graphics.Bitmap;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;
import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

public class ClockingPresenter extends BasePresenter<ClockingActivity> {

    Subscription subscription;
    DataManager mDataManager;

    @Inject
    public ClockingPresenter(DataManager dataManager){ mDataManager = dataManager;}

    @Override
    public void attachView(ClockingActivity mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    public void matchFinger(Bitmap bitmap, BiometricsManager biometricsManager){
        checkViewAttached();
        getMvpView().showLoading();
        biometricsManager.convertToFmd(bitmap, Biometrics.FmdFormat.ANSI_378_2004,
                new Biometrics.OnConvertToFmdListener() {
            @Override
            public void onConvertToFmd(Biometrics.ResultCode resultCode, byte[] fmd) {
                getMvpView().startCompare(fmd);
                getMvpView().hideLoading();
            }
        });
    }

    public void compareFmd(byte[] fmd, BiometricsManager biometricsManager) {
        checkViewAttached();

    }
}
