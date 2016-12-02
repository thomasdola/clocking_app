package com.eurekacachet.clocling.ui.view.clocking;


import android.util.Base64;
import android.util.Log;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;
import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.data.model.Fingerprint;
import com.eurekacachet.clocling.ui.base.BasePresenter;
import com.eurekacachet.clocling.utils.Constants;
import com.google.common.io.Files;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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

    public void matchFinger(final byte[] fmd, final BiometricsManager biometricsManager){
        checkViewAttached();
//        getMvpView().showLoading();

        Log.d(getClass().getSimpleName(),
                String.format("string of fmd -> %s", Arrays.toString(fmd)));

        String encodeToString = Base64.encodeToString(fmd, Base64.DEFAULT);
        Log.d(getClass().getSimpleName(),
                String.format("base 64 string of fmd -> %s", encodeToString));

        biometricsManager.compareFmd(fmd, Base64.decode(encodeToString, Base64.DEFAULT),
                Biometrics.FmdFormat.ANSI_378_2004,
                new Biometrics.OnCompareFmdListener() {
                    @Override
                    public void onCompareFmd(Biometrics.ResultCode resultCode, float v) {
                        Log.d(getClass().getSimpleName(),
                                String.format("comparison from same result -> %s", v));
                    }
                });

        String path = mDataManager.getPath(Constants.THUMB_LEFT_FMD);
        if(path == null){
            getMvpView().hideLoading();
            return;
        }

        File file = new File(path);
        if(!file.exists()){
            getMvpView().hideLoading();
            return;
        }

        try{
            final byte[] local = Files.toByteArray(file);
            biometricsManager.compareFmd(fmd, local,
                    Biometrics.FmdFormat.ANSI_378_2004,
                    new Biometrics.OnCompareFmdListener() {
                        @Override
                        public void onCompareFmd(Biometrics.ResultCode resultCode, float v) {
                            Log.d(getClass().getSimpleName(),
                                                    String.format("comparison result -> %s", v));

//                            Log.d(getClass().getSimpleName(),
//                                    String.format("string of fmd -> %s", Arrays.toString(local)));
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
//        biometricsManager.compareFmd();
//        subscription = mDataManager
//                .getFingerprints()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<Fingerprint>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//
//                    @Override
//                    public void onNext(List<Fingerprint> fingerprints) {
//                        for(Fingerprint fingerprint : fingerprints){
//                            biometricsManager.compareFmd(fmd, fingerprint.fingerprint,
//                                    Biometrics.FmdFormat.ANSI_378_2004,
//                                    new Biometrics.OnCompareFmdListener() {
//                                        @Override
//                                        public void onCompareFmd(Biometrics.ResultCode resultCode, float v) {
//                                            Log.d(getClass().getSimpleName(),
//                                                    String.format("comparison result -> %s", v));
//                                        }
//                                    });
//                        }
//                    }
//                });
    }

    public void compareFmd(byte[] fmd, BiometricsManager biometricsManager) {
        checkViewAttached();

    }
}
