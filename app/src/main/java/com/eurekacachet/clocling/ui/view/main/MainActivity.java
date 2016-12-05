package com.eurekacachet.clocling.ui.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eurekacachet.clocling.ClockingApplication;
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.ui.view.clocking.ClockingActivity;
import com.eurekacachet.clocling.ui.view.login.LoginActivity;
import com.eurekacachet.clocling.utils.Constants;
import com.eurekacachet.clocling.utils.services.SyncService;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends BaseActivity implements HomeMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG = "EXTRA_TRIGGER_SYNC_FLAG";
    Button mClockingButton;
    Button mCaptureBioButton;
    String mUserUUID;

    TelephonyManager telephonyManager;
    @Inject
    HomePresenter presenter;

    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_home);
        initView();
        presenter.attachView(this);
        presenter.isLoggedIn();

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }

        presenter.getUserUUID();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        initListeners();
    }

    private void initView() {
        mCaptureBioButton = (Button) findViewById(R.id.bio_button);
//        mCaptureBioButton.setEnabled(false);
        mClockingButton = (Button) findViewById(R.id.clocking_button);
    }

    private void initListeners() {
        mClockingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getSimpleName(), "launch clocking page");
                launchClockingActivity();
            }
        });

        mCaptureBioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void launchClockingActivity() {
        startActivity(ClockingActivity.startNewIntent(this));
    }

    @Override
    public void launchLoginActivity() {
        startActivity(LoginActivity.newIntent(this));
        finish();
    }

    @Override
    public void launchBioDataCaptureActivity() {
        startActivity(new Intent(this, BioActivity.class));
        finish();
    }

    @Override
    public void setUserUUID(String UUID) {
        mUserUUID = UUID;
    }
}
