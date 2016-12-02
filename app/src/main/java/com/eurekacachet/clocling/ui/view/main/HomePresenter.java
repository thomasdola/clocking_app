package com.eurekacachet.clocling.ui.view.main;

import android.content.Intent;
import android.util.Log;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;
import com.eurekacachet.clocling.utils.services.SocketService;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class HomePresenter extends BasePresenter<MainActivity> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public HomePresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainActivity mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription != null) mSubscription.unsubscribe();
    }

    public void captureBioData(String bid){
        getMvpView().launchBioDataCaptureActivity();
    }

    public void isLoggedIn(){
        checkViewAttached();
//        Log.d("MainActivityPresenter", "isLoggedIn called");
        if(!mDataManager.isLoggedIn()){
            getMvpView().launchLoginActivity();
        }else{
            getMvpView().startService(new Intent(getMvpView(), SocketService.class));
        }
    }

    public void getUserUUID() {
        checkViewAttached();
//        Log.d("MainActivityPresenter", "getUserUUID called");
        mSubscription = mDataManager
                .getUserUUID()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String UUID) {
//                        Log.d("MainActivityPresenter", String.format("user uuid %s", UUID));
                        getMvpView().setUserUUID(UUID);
                    }
                });
    }
}
