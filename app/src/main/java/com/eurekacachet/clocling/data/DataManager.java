package com.eurekacachet.clocling.data;


import com.eurekacachet.clocling.data.local.DatabaseHelper;
import com.eurekacachet.clocling.data.local.PreferencesHelper;
import com.eurekacachet.clocling.data.model.AuthResponse;
import com.eurekacachet.clocling.data.model.Fingerprint;
import com.eurekacachet.clocling.data.remote.ClockingService;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func0;

public class DataManager {

    private final ClockingService mClockingService;
    private final PreferencesHelper mPreferencesHelper;
    private final DatabaseHelper mDatabaseHelper;

    @Inject
    public DataManager(ClockingService clockingService,
                       PreferencesHelper preferencesHelper, DatabaseHelper databaseHelper){
        mClockingService = clockingService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
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

    public void setPortrait(String path){
        mPreferencesHelper.setPortrait(path);
    }

    public String getPortraitPath(){
        return mPreferencesHelper.getPortraitPath();
    }

    public void setForm(String path){
        mPreferencesHelper.setForm(path);
    }

    public String getFormPath(){
        return mPreferencesHelper.getFormPath();
    }

    public void setThumbRight(String path){
        mPreferencesHelper.setThumbRight(path);
    }

//    public Observable<String> getThumbRightPath(){
//        return mPreferencesHelper.getThumbRightPath();
//    }

    public String getThumbRightPath(){
        return mPreferencesHelper.getThumbRightPath();
    }

    public void setThumbLeft(String path){
        mPreferencesHelper.setThumbLeft(path);
    }

    public String getThumbLeftPath(){
        return mPreferencesHelper.getThumbLeftPath();
    }

    public void setIndexRight(String path){
        mPreferencesHelper.setIndexRight(path);
    }

    public String getIndexRightPath(){
        return mPreferencesHelper.getIndexRightPath();
    }

    public void setIndexLeft(String path){
        mPreferencesHelper.setIndexLeft(path);
    }

    public String getIndexLeftPath(){
        return mPreferencesHelper.getIndexLeftPath();
    }


    //Database

    public Observable<List<Fingerprint>> getFingeprints(){
        return mDatabaseHelper.getFingerprints();
    }

    public Observable<Fingerprint> saveFingerprints(Collection<Fingerprint> fingerprints){
        return mDatabaseHelper.saveFingerprints(fingerprints);
    }

}
