package com.eurekacachet.clocling.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eurekacachet.clocling.ClockingApplication;
import com.eurekacachet.clocling.injection.component.ActivityComponent;
import com.eurekacachet.clocling.injection.component.DaggerActivityComponent;
import com.eurekacachet.clocling.injection.module.ActivityModule;


public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(ClockingApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }
}
