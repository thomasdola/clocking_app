package com.eurekacachet.clocling;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.eurekacachet.clocling.data.local.PreferencesHelper;
import com.eurekacachet.clocling.data.remote.ClockingService;
import com.eurekacachet.clocling.injection.component.ApplicationComponent;
import com.eurekacachet.clocling.injection.component.DaggerApplicationComponent;
import com.eurekacachet.clocling.injection.module.ApplicationModule;
import com.eurekacachet.clocling.utils.Constants;

import java.net.URISyntaxException;

import javax.inject.Inject;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ClockingApplication extends Application{

    @Inject ClockingService mClockingService;
    ApplicationComponent mApplicationComponent;

    public static ClockingApplication sClockingApplication;

    private Socket mSocket;
    {
        try{
            mSocket = IO.socket(Constants.SOCKET_URL);
        }catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sClockingApplication = this;
    }

    public static ClockingApplication get(Context context){
        return (ClockingApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent(){
        if(mApplicationComponent == null){
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }

        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent applicationComponent){
        mApplicationComponent = applicationComponent;
    }

    public Socket getSocket(){
        return mSocket;
    }

    public static ClockingApplication getApplication(){
        return sClockingApplication;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        sClockingApplication = null;
    }
}
