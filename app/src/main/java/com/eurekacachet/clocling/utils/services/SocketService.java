package com.eurekacachet.clocling.utils.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.eurekacachet.clocling.ClockingApplication;
import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.ui.view.clocking.ClockingActivity;
import com.eurekacachet.clocling.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.functions.Action1;

public class SocketService extends Service {

    @Inject
    DataManager mDataManager;
    private String mUserUUID;
    private Socket mDefaultSocket;
    private Socket mEnrolmentSocket;
    private String mConnectionId;
    private TelephonyManager mTelephonyManager;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SocketService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getClass().getSimpleName(), "Start socket service onCreate called");
        ClockingApplication.get(this).getComponent().inject(this);
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mDefaultSocket = ((ClockingApplication) getApplication()).getDefaultSocket().connect();
        mConnectionId = mDataManager.getConnectionId();
    }

    @Override
    public void onDestroy() {
        mDefaultSocket.disconnect();
        super.onDestroy();
        Log.d(getClass().getSimpleName(), "Start socket service onDestroy called");
        mDefaultSocket.off(Socket.EVENT_CONNECT, onConnect);
        mDefaultSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mDefaultSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mDefaultSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mDefaultSocket.off(Socket.EVENT_RECONNECT, onConnect);
        mDefaultSocket.off(Constants.makeEvent(mUserUUID, Constants.CAPTURE_BIO_DATA), onReceiveCaptureBio);
        mDefaultSocket.off(Constants.makeEvent(mUserUUID, Constants.CAPTURE_BIO_DATA), onReceiveCaptureBioUpdate);
        mDefaultSocket.off(Constants.makeEvent(mUserUUID, Constants.CANCEL_CAPTURE), onReceiveCancelCapture);
        mDefaultSocket.off(Constants.DEVICES, onReceiveConnectionId);
        mDefaultSocket.off(Constants.FINGERPRINTS_UPDATED, onFingerprintsUpdated);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(getClass().getSimpleName(), "Start socket service command");
        mDefaultSocket.on(Socket.EVENT_CONNECT, onConnect);
        mDefaultSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mDefaultSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mDefaultSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mDefaultSocket.on(Socket.EVENT_RECONNECT, onConnect);
        mDefaultSocket.on(Constants.DEVICES, onReceiveConnectionId);
        mDefaultSocket.on(Constants.FINGERPRINTS_UPDATED, onFingerprintsUpdated);
        listenForBio();
        return START_STICKY;
    }

    private void listenForBio() {
        mDataManager.getUserUUID()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String uuid) {
                        mUserUUID = uuid;
                        String captureBioDataEvent = Constants.makeEvent(uuid, Constants.CAPTURE_BIO_DATA);
                        String captureBioDataUpdateEvent = Constants.makeEvent(uuid, Constants.CAPTURE_BIO_DATA_UPDATE);
                        String cancelCaptureBioDataEvent = Constants.makeEvent(uuid, Constants.CANCEL_CAPTURE);
                        Log.d(getClass().getSimpleName(), String.format("user uuid -> %s on %s", uuid, captureBioDataEvent));
                        mDefaultSocket.on(captureBioDataEvent, onReceiveCaptureBio);
                        mDefaultSocket.on(captureBioDataUpdateEvent, onReceiveCaptureBio);
                        mDefaultSocket.on(cancelCaptureBioDataEvent, onReceiveCancelCapture);
                    }
                });
    }

    private void launchBioActivity(String bid, boolean updating){
        Intent intent = BioActivity.startNewIntent(this, mUserUUID, bid, updating);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void closeBioActivity(){
        Intent bioIntent = new Intent(Constants.CANCEL_CAPTURE);
        sendBroadcast(bioIntent);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(getClass().getSimpleName(), "socket service connected ...");
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {}
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(getClass().getSimpleName(), String.format("socket service error => %s", Arrays.toString(args)));
        }
    };

    private Emitter.Listener onReceiveCaptureBio = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(getClass().getSimpleName(), String.format("socket service data => %s", args[0]));
            try{
                String bid = (String) args[0];
                launchBioActivity(bid, false);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onReceiveCaptureBioUpdate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(getClass().getSimpleName(), String.format("socket service data => %s", args[0]));
            try{
                String bid = (String) args[0];
                launchBioActivity(bid, true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onReceiveCancelCapture = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            closeBioActivity();
        }
    };

    private Emitter.Listener onReceiveConnectionId = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONObject object = (JSONObject) args[0];
                String connectionId = object.getString("connId");
                mDataManager.setConnectionId(connectionId);
                JSONObject data = new JSONObject();
                data.put("deviceId", mTelephonyManager.getDeviceId());
                data.put("connId", connectionId);
                data.put("type", Constants.CREDENCE);
                mDefaultSocket.emit(Constants.DEVICES, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Emitter.Listener onFingerprintsUpdated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
//            Intent intent = ClockingActivity.startNewIntent(getApplicationContext());
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            JSONArray object = (JSONArray) args[0];

        }
    };
}
