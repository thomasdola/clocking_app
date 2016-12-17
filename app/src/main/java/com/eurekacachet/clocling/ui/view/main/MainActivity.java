package com.eurekacachet.clocling.ui.view.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.ui.view.clocking.ClockingActivity;
import com.eurekacachet.clocling.ui.view.login.LoginActivity;
import com.eurekacachet.clocling.utils.services.SocketService;
import com.eurekacachet.clocling.utils.services.SyncService;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements HomeMvpView {

    private static final String EXTRA_TRIGGER_SYNC_FLAG = "EXTRA_TRIGGER_SYNC_FLAG";
    RelativeLayout mClockingButton;
    Button mLogoutButton;
    TextView mDeviceIdTextView;
    String mUserUUID;

    TelephonyManager telephonyManager;
    @Inject
    HomePresenter presenter;
    int mUserRoleId;
    private ProgressDialog mProgressDialog;

    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    public void setRoleId(int userRoleId) {
        Log.d(getClass().getSimpleName(), String.format("role id -> %s", userRoleId));
        mUserRoleId = userRoleId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_home);
        presenter.attachView(this);
        presenter.isLoggedIn();
        presenter.getUserRoleId();

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }

        presenter.getUserUUID();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        initView();
        initListeners();
    }

    @Override
    public void showLoading() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if(mProgressDialog != null){
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
    }

    @Override
    public void closeOut() {
        stopService(new Intent(this, SocketService.class));
        launchLoginActivity();
        finish();
    }

    private void initView() {
        mClockingButton = (RelativeLayout) findViewById(R.id.clocking_button);
        mLogoutButton = (Button) findViewById(R.id.logoutButton);
        mDeviceIdTextView = (TextView) findViewById(R.id.deviceIdView);
        if(mUserRoleId == 1){
            mDeviceIdTextView.setText(telephonyManager.getDeviceId());
            mDeviceIdTextView.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners() {
        mClockingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getSimpleName(), "launch clocking page");
                launchClockingActivity();
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.logout();
            }
        });
    }

    @Override
    public void onError(String reason) {
        Toast.makeText(MainActivity.this, reason, Toast.LENGTH_SHORT).show();
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
