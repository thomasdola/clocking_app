package com.eurekacachet.clocling.data;


import com.eurekacachet.clocling.data.local.DatabaseHelper;
import com.eurekacachet.clocling.data.local.PreferencesHelper;
import com.eurekacachet.clocling.data.model.AuthResponse;
import com.eurekacachet.clocling.data.model.BioDic;
import com.eurekacachet.clocling.data.model.Fingerprint;
import com.eurekacachet.clocling.data.remote.ClockingService;
import com.eurekacachet.clocling.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;
import rx.functions.Func1;

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

    public boolean isLoggedIn(){
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


    public void setPath(String key, String path){
        mPreferencesHelper.setPath(key, path);
    }

    public String getPath(String key){
        return mPreferencesHelper.getPath(key);
    }

    //Database

    public Observable<Fingerprint> saveFingerprints(Collection<Fingerprint> fingerprints){
        return mDatabaseHelper.saveFingerprints(fingerprints);
    }

    public Observable<Fingerprint> syncFingerprints() {
        return mClockingService.getFingerprints()
                .concatMap(new Func1<List<Fingerprint>, Observable<? extends Fingerprint>>() {
                    @Override
                    public Observable<? extends Fingerprint> call(List<Fingerprint> fingerprints) {
                        return mDatabaseHelper.saveFingerprints(fingerprints);
                    }
                });
    }

    public Observable<List<Fingerprint>> getFingerprints(){
        return mDatabaseHelper.getFingerprints().distinct();
    }

    public boolean thumbRightExists(){
        String path = getPath(Constants.THUMB_RIGHT);
        return path != null && (new File(path)).exists();
    }

    public boolean thumbRightFmdExists(){
        String path = getPath(Constants.THUMB_RIGHT_FMD);
        return path != null && (new File(path)).exists();
    }

    public boolean thumbLeftExists(){
        String path = getPath(Constants.THUMB_LEFT);
        return path != null && (new File(path)).exists();
    }

    public boolean thumbLeftFmdExists(){
        String path = getPath(Constants.THUMB_LEFT_FMD);
        return path != null && (new File(path)).exists();
    }

    public boolean indexRightExists(){
        String path = getPath(Constants.INDEX_RIGHT);
        return path != null && (new File(path)).exists();
    }

    public boolean indexRightFmdExists(){
        String path = getPath(Constants.INDEX_RIGHT_FMD);
        return path != null && (new File(path)).exists();
    }

    public boolean indexLeftExists(){
        String path = getPath(Constants.INDEX_LEFT);
        return path != null && (new File(path)).exists();
    }

    public boolean indexLeftFmdExists() {
        String path = getPath(Constants.INDEX_LEFT_FMD);
        return path != null && (new File(path)).exists();
    }

    public boolean formExists(){
        String path = getPath(Constants.FORM);
        return path != null && (new File(path)).exists();
    }

    public boolean portraitExists(){
        String path = getPath(Constants.PORTRAIT);
        return path != null && (new File(path)).exists();
    }

    public boolean readyToReview(){
        return portraitExists()
                && formExists()
                && indexLeftExists()
                && indexLeftFmdExists()
                && indexRightExists()
                && indexRightFmdExists()
                && thumbLeftExists()
                && thumbLeftFmdExists()
                && thumbRightExists()
                && thumbRightFmdExists();
    }

    public Observable<BioDic> getBio() {
        final HashMap<String, String> data = new HashMap<>();
        data.put(Constants.THUMB_RIGHT, getPath(Constants.THUMB_RIGHT));
        data.put(Constants.THUMB_LEFT, getPath(Constants.THUMB_LEFT));
        data.put(Constants.INDEX_RIGHT, getPath(Constants.INDEX_RIGHT));
        data.put(Constants.INDEX_LEFT, getPath(Constants.INDEX_LEFT));
        data.put(Constants.FORM, getPath(Constants.FORM));
        data.put(Constants.PORTRAIT, getPath(Constants.PORTRAIT));
        return Observable.create(new Observable.OnSubscribe<BioDic>() {
            @Override
            public void call(Subscriber<? super BioDic> subscriber) {
                if(subscriber.isUnsubscribed()) return;
                try{
                    for(Map.Entry<String, String> entry : data.entrySet()){
                        BioDic bio = new BioDic(entry.getKey(), entry.getValue());
                        subscriber.onNext(bio);
                    }
                }catch (Exception e){
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });
    }

    public Observable<List<byte[]>> getLocalFMDs() {
        return getFingerprints()
                .map(new Func1<List<Fingerprint>, List<byte[]>>() {
                    @Override
                    public List<byte[]> call(List<Fingerprint> fingerprints) {
                        List<byte[]> fmds = new ArrayList<>();
                        for(Fingerprint fingerprint : fingerprints){
                            fmds.add(fingerprint.fingerprint);
                        }
                        return fmds;
                    }
                });
    }
}
