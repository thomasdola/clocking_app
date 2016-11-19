package com.eurekacachet.clocling.data.local;


import android.content.Context;
import android.content.SharedPreferences;

import com.eurekacachet.clocling.injection.context.ApplicationContext;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "clocking_pref_file";
    public static final String KEY_ACCESS_TOKEN = "token";
    private static final String KEY_IS_LOGIN = "is_login";
    private static final String KEY_USER_UUID = "user_uuid";

    private final SharedPreferences mPreferences;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

//    Access Token
    public String apiToken(){
        return mPreferences.getString(KEY_ACCESS_TOKEN, "");
    }
    public void setAccessToken(String accessToken){
        mPreferences.edit().putString(KEY_ACCESS_TOKEN, accessToken)
                .apply();
    }
    public Observable<String> getAccessToken(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                mPreferences.getString(KEY_ACCESS_TOKEN, "");
            }
        });
    }

    public void setLogin(boolean loggedIn) {
        mPreferences.edit().putBoolean(KEY_IS_LOGIN, loggedIn)
                .apply();
    }

    public Observable<Boolean> getLogin(){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                mPreferences.getString(KEY_IS_LOGIN, "");
            }
        });
    }

    public void setUserUUID(String userUUID) {
        mPreferences.edit().putString(KEY_USER_UUID, userUUID)
                .apply();
    }

    public Observable<String> getUserUUID(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                mPreferences.getString(KEY_USER_UUID, "");
            }
        });
    }
}
