package com.eurekacachet.clocling.data.local;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.eurekacachet.clocling.injection.context.ApplicationContext;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "clocking_pref_file";
    public static final String KEY_ACCESS_TOKEN = "token";
    private static final String KEY_IS_LOGIN = "is_login";
    private static final String KEY_USER_UUID = "user_uuid";
    private static final String KEY_DEVICE_ID = "device_id";

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
        return Observable.just(mPreferences.getString(KEY_ACCESS_TOKEN, null));
    }

    public void setLogin(boolean loggedIn) {
        mPreferences.edit().putBoolean(KEY_IS_LOGIN, loggedIn)
                .apply();
    }

    public Observable<Boolean> getLogin(){
        Log.d("MainActivityPresenter", "getLogin");
        return Observable.just(
                mPreferences.getBoolean(KEY_IS_LOGIN, false)
        );
    }

    public void setUserUUID(String userUUID) {
        mPreferences.edit().putString(KEY_USER_UUID, userUUID)
                .apply();
    }

    public Observable<String> getUserUUID(){
        return Observable.just(mPreferences.getString(KEY_USER_UUID, null));
    }

    public void setDeviceId(String deviceId) {
        mPreferences.edit().putString(KEY_DEVICE_ID, deviceId)
                .apply();
    }

    public Observable<String> getDeviceId(){
        return Observable.just(mPreferences.getString(KEY_DEVICE_ID, null));
    }
}
