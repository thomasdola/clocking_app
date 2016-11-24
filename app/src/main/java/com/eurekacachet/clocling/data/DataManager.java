package com.eurekacachet.clocling.data;


import android.util.Log;

import com.eurekacachet.clocling.data.local.PreferencesHelper;
import com.eurekacachet.clocling.data.model.AuthResponse;
import com.eurekacachet.clocling.data.remote.ClockingService;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;

public class DataManager {

    private final ClockingService mClockingService;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(ClockingService clockingService, PreferencesHelper preferencesHelper){
        mClockingService = clockingService;
        mPreferencesHelper = preferencesHelper;
    }

    public Observable<AuthResponse> signIn(final HashMap<String, String> credentials){
        return Observable.defer(new Func0<Observable<AuthResponse>>() {
            @Override
            public Observable<AuthResponse> call() {
                return mClockingService.login(credentials);
            }
        });
    }

    public void setToken(String token) {
        mPreferencesHelper.setAccessToken(token);
    }

    public Observable<String> getToken(){
        return mPreferencesHelper.getAccessToken();
    }

    public void setLogin(boolean loggedIn) {
        mPreferencesHelper.setLogin(loggedIn);
    }

    public Observable<Boolean> isLoggedIn(){
        return mPreferencesHelper.getLogin();
    }

    public void setUserUUID(String userUUID) {
        mPreferencesHelper.setUserUUID(userUUID);
    }

    public Observable<String> getUserUUID(){
        return mPreferencesHelper.getUserUUID();
    }

    public Observable<String> getDeviceId() {
        return mPreferencesHelper.getDeviceId();
    }
}
