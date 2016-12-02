package com.eurekacachet.clocling.data.local;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.eurekacachet.clocling.injection.context.ApplicationContext;
import com.eurekacachet.clocling.utils.Constants;

import javax.inject.Inject;

import retrofit.http.PUT;
import rx.Observable;
import rx.Subscriber;

public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "clocking_pref_file";
    public static final String KEY_ACCESS_TOKEN = "token";
    private static final String KEY_IS_LOGIN = "is_login";
    private static final String KEY_USER_UUID = "user_uuid";
    private static final String KEY_THUMB_RIGHT_PATH = Constants.THUMB_RIGHT;
    private static final String KEY_THUMB_LEFT_PATH = Constants.THUMB_LEFT;
    private static final String KEY_INDEX_RIGHT_PATH = Constants.INDEX_RIGHT;
    private static final String KEY_INDEX_LEFT_PATH = Constants.INDEX_LEFT;
    private static final String KEY_PORTRAIT_PATH = Constants.PORTRAIT;
    private static final String KEY_FORM_PATH = Constants.FORM;
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

    public boolean getLogin(){
        Log.d("MainActivityPresenter", "getLogin");
        return mPreferences.getBoolean(KEY_IS_LOGIN, false);
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

    public void setPath(String KEY_PATH, String path){
        mPreferences.edit().putString(KEY_PATH, path)
                .apply();
    }

    public String getPath(String KEY_PATH){
        return mPreferences.getString(KEY_PATH, null);
    }
}
