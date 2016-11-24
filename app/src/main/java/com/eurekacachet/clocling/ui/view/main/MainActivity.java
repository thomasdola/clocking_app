package com.eurekacachet.clocling.ui.view.main;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eurekacachet.clocling.ClockingApplication;
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.login.LoginActivity;
import com.eurekacachet.clocling.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements HomeMvpView {

    Button mClockingButton;
    Button mCaptureBioButton;
    Socket mSocket;
    String mUserUUID;
    TelephonyManager telephonyManager;

    @Inject
    HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_home);
        initView();
        presenter.attachView(this);
        presenter.isLoggedIn();
        presenter.getUserUUID();
        ClockingApplication application = (ClockingApplication) getApplication();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mSocket = application.getSocket();
        initListeners();
    }

    private void initView() {
        mCaptureBioButton = (Button) findViewById(R.id.bio_button);
//        mCaptureBioButton.setActivated(false);
        mClockingButton = (Button) findViewById(R.id.clocking_button);
    }

    private void initListeners() {
        mSocket.on(Constants.makeEvent(mUserUUID, "CaptureBioData"), onCaptureBioData);
        mClockingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch clocking page
                Log.d("MainActivityPresenter", "launch clocking page");
            }
        });
    }

    private Emitter.Listener onCaptureBioData = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String bid;
                    try{
                        bid = data.getString("bid");
                    } catch (JSONException e){
                        return;
                    }
                    presenter.captureBioData(bid);
                }
            });
        }
    };

    @Override
    public void launchLoginActivity() {
        Log.d("MainActivity", String.format("device id -> %s", telephonyManager.getDeviceId()));
        startActivity(LoginActivity.newIntent(this));
        finish();
    }

    @Override
    public void launchBioDataCaptureActivity() {

    }

    @Override
    public void setUserUUID(String UUID) {
        mUserUUID = UUID;
    }
}
