package com.eurekacachet.clocling;

import android.app.Application;
import android.content.Context;

import com.eurekacachet.clocling.data.remote.ClockingService;
import com.eurekacachet.clocling.injection.component.ApplicationComponent;
import com.eurekacachet.clocling.injection.component.DaggerApplicationComponent;
import com.eurekacachet.clocling.injection.module.ApplicationModule;

import javax.inject.Inject;

public class ClockingApplication extends Application{

    @Inject
    ClockingService mClockingService;
    ApplicationComponent mApplicationComponent;

    public static ClockingApplication sClockingApplication;


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
        super.onTerminate();
        sClockingApplication = null;
    }
}
