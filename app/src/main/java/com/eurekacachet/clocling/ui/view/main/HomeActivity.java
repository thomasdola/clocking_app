package com.eurekacachet.clocling.ui.view.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.eurekacachet.clocling.R;

public class HomeActivity extends AppCompatActivity implements HomeMvpView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public void launchLoginActivity() {

    }

    @Override
    public void launchRegistrationActivity() {

    }
}
