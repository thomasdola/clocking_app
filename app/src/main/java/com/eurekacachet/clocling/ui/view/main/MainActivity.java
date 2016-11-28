package com.eurekacachet.clocling.ui.view.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eurekacachet.clocling.ClockingApplication;
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.ui.view.clocking.ClockingActivity;
import com.eurekacachet.clocling.ui.view.login.LoginActivity;
import com.eurekacachet.clocling.utils.Constants;
import com.eurekacachet.clocling.utils.SocketListeners;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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
        ClockingApplication application = (ClockingApplication) getApplication();
        mSocket = application.getSocket();
        setContentView(R.layout.activity_home);
        initView();
        presenter.attachView(this);
        presenter.isLoggedIn();
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
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, SocketListeners.onDisconnect(this));
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onError);
        mSocket.on(Constants.makeEvent(mUserUUID, "CaptureBioData"), onCaptureBioData);
        mSocket.on("*", testListener);

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
                presenter.captureBioData("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, SocketListeners.onDisconnect(this));
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onError);
        mSocket.off("*", testListener);
        mSocket.off(Constants.makeEvent(mUserUUID, "CaptureBioData"), onCaptureBioData);
    }

    private void launchClockingActivity() {
        startActivity(new Intent(this, ClockingActivity.class));
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
                    Log.d("MainActivity", String.format("socket:bid -> %s", bid));
//                    presenter.captureBioData(bid);
                }
            });
        }
    };

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(this.getClass().getSimpleName(), "connect...");
        }
    };

    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(this.getClass().getSimpleName(), "error...");
        }
    };

    private Emitter.Listener testListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(this.getClass().getSimpleName(), "receiving...");
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
        startActivity(new Intent(this, BioActivity.class));
        finish();
    }

    @Override
    public void setUserUUID(String UUID) {
        mUserUUID = UUID;
    }
}
