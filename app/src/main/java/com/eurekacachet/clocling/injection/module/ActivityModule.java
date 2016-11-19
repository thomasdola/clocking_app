package com.eurekacachet.clocling.injection.module;


import android.app.Activity;
import android.content.Context;

import com.eurekacachet.clocling.injection.context.ActivityContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity){
        mActivity = activity;
    }

    @Provides
    Activity providesActivity(){
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext(){
        return mActivity;
    }
}
