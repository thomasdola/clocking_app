package com.eurekacachet.clocling.ui.view.login.modal;


import android.util.Log;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.data.model.AuthResponse;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginFragmentPresenter extends BasePresenter<LoginFragmentMvpView> {

    private DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    LoginFragmentPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LoginFragmentMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        if(mSubscription != null)
            mSubscription.unsubscribe();
    }

    public void signIn(HashMap<String, String> credentials){
        checkViewAttached();
        getMvpView().toggleLoading(true);
        mSubscription = mDataManager.signIn(credentials)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AuthResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        getMvpView().onSignInFailed(e.getMessage());
                        getMvpView().toggleLoading(false);
                    }

                    @Override
                    public void onNext(AuthResponse authResponse) {
                        Log.d("LoginFragmentPresenter", authResponse.toString());
                        if(authResponse.code() == 200){
                            if(authResponse.token() != null){
                                mDataManager.setToken(authResponse.token());
                            }
                            if(authResponse.userUUID() != null){
                                mDataManager.setUserUUID(authResponse.userUUID());
                            }

                            if(authResponse.userRoleId() != null){
                                mDataManager.setUserRoleId(authResponse.userRoleId());
                            }

                            if(authResponse.logUUID() != null){
                                mDataManager.setLogUUID(authResponse.logUUID());
                            }

                            mDataManager.setLogin(true);
                            getMvpView().startSocketService();
                            getMvpView().launchMainActivity();
                            getMvpView().toggleLoading(false);
                        }else{
                            getMvpView().onSignInFailed(authResponse.message());
                            getMvpView().toggleLoading(false);
                        }
                    }
                });
    }
}
