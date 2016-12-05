package com.eurekacachet.clocling.ui.view.clocking;


import android.util.Log;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;
import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.data.model.Beneficiary;
import com.eurekacachet.clocling.data.model.Clock;
import com.eurekacachet.clocling.data.model.FingerFmd;
import com.eurekacachet.clocling.ui.base.BasePresenter;
import com.eurekacachet.clocling.utils.NetworkUtil;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void matchFinger(final byte[] fmd, final BiometricsManager biometricsManager,
                            final String deviceId){
        checkViewAttached();
        getMvpView().showLoading();

        subscription = mDataManager.getFingerprints()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<FingerFmd>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onNext(List<FingerFmd> fingerFmds) {
                        for (final FingerFmd fingerFmd : fingerFmds){
                            biometricsManager.compareFmd(fmd, fingerFmd.fmd,
                                    Biometrics.FmdFormat.ANSI_378_2004,
                                    new Biometrics.OnCompareFmdListener() {
                                        @Override
                                        public void onCompareFmd(Biometrics.ResultCode resultCode, float score) {
                                            if(score == 0){
                                                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                                final HashMap<String, String> data = new HashMap<>();
                                                data.put("timestamp", timestamp.toString());
                                                data.put("bid", fingerFmd.bid);
                                                data.put("device_id", deviceId);
                                                persistClock(data);
                                                Log.d(getClass().getSimpleName(), String.format("result -> %s", score));
                                                Log.d(getClass().getSimpleName(), String.format("result -> %s", true));
                                            }else {
                                                getMvpView().hideLoading();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void persistClock(final HashMap<String, String> data) {
        if(!NetworkUtil.isNetworkConnected(getMvpView().getApplicationContext())){
            saveLocally(data);
        }else {
            saveOnline(data);
        }
    }

    private void saveOnline(final HashMap<String, String> data) {
        subscription = mDataManager.getBeneficiaryDetails(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Beneficiary>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        saveLocally(data);
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onNext(Beneficiary beneficiary) {
                        getMvpView().displayInfo(beneficiary);
                        getMvpView().hideLoading();
                    }
                });
    }

    private void saveLocally(HashMap<String, String> data) {
        subscription = mDataManager.saveClock(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Clock>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onNext(Clock clock) {
                        getMvpView().displayLittleInfo(clock);
                        getMvpView().hideLoading();
                    }
                });
    }
}
