package com.eurekacachet.clocling;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.eurekacachet.clocling.data.remote.ClockingService;
import com.eurekacachet.clocling.injection.component.ApplicationComponent;
import com.eurekacachet.clocling.injection.component.DaggerApplicationComponent;
import com.eurekacachet.clocling.injection.module.ApplicationModule;
import com.eurekacachet.clocling.utils.Constants;
import com.eurekacachet.clocling.utils.services.SocketService;

import java.net.URI;

import javax.inject.Inject;

import io.socket.client.Manager;
import io.socket.client.Socket;

public class ClockingApplication extends Application{

    @Inject ClockingService mClockingService;
    ApplicationComponent mApplicationComponent;

    public static ClockingApplication sClockingApplication;

    private final Socket mDefaultSocket;

    private final Socket mEnrolmentSocket;

    {
        Manager mSocketManager = new Manager(URI.create(Constants.SOCKET_URL));
        mDefaultSocket = mSocketManager.socket("/");
        mEnrolmentSocket = mSocketManager.socket("/enrolment");
    }

    public Socket getDefaultSocket(){
        return mDefaultSocket;
    }

    public Socket getEnrolmentSocket(){
        return mEnrolmentSocket;
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

    public static ClockingApplication getApplication(){
        return sClockingApplication;
    }

    @Override
    public void onTerminate() {
        stopService(SocketService.getStartIntent(this));
        if(mDefaultSocket.connected()){
            mDefaultSocket.disconnect();
        }
        super.onTerminate();
        sClockingApplication = null;

    }
}
