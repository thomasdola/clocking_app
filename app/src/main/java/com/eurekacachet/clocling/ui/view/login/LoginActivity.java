package com.eurekacachet.clocling.ui.view.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.login.modal.LoginFragment;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity implements LoginMvpView {

    private static final String SIGN_IN_TAG = "sign_in_tag";
    private Button signInButton;
    private String mDeviceId;

    @Inject LoginPresenter presenter;

    public static Intent newIntent(Activity launcher){
        return new Intent(launcher, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_login);
        initView();
        presenter.attachView(this);
        initListeners();
    }

    private void initListeners() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                LoginFragment loginFragment = LoginFragment.newInstance(mDeviceId);
                loginFragment.show(fragmentManager, SIGN_IN_TAG);
            }
        });
    }

    public void initView(){
        signInButton = (Button) findViewById(R.id.login_button);
    }

    @Override
    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }
}
