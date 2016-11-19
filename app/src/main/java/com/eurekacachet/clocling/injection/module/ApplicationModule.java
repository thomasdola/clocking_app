package com.eurekacachet.clocling.injection.module;


import android.app.Application;
import android.content.Context;

import com.eurekacachet.clocling.data.local.PreferencesHelper;
import com.eurekacachet.clocling.data.remote.ClockingService;
import com.eurekacachet.clocling.injection.context.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application providesApplication(){
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context providesContext(){
        return mApplication;
    }

    @Provides
    @Singleton
    ClockingService providesClockingService(PreferencesHelper preferencesHelper){
        return ClockingService.Creator.newClockingService(preferencesHelper);
    }
}
