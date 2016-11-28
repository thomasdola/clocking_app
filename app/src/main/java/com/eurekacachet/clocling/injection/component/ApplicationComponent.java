package com.eurekacachet.clocling.injection.component;

import android.app.Application;
import android.content.Context;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.data.local.DatabaseHelper;
import com.eurekacachet.clocling.data.local.PreferencesHelper;
import com.eurekacachet.clocling.data.remote.ClockingService;
import com.eurekacachet.clocling.injection.context.ApplicationContext;
import com.eurekacachet.clocling.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext Context context();

    Application application();

    ClockingService clockingService();

    PreferencesHelper preferencesHelper();

    DatabaseHelper databaseHelper();

    DataManager dataManager();
}
