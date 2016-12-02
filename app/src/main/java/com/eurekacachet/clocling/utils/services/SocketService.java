package com.eurekacachet.clocling.utils.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.eurekacachet.clocling.ClockingApplication;
import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.ui.view.main.MainActivity;
import com.eurekacachet.clocling.utils.Constants;

import java.net.URI;
import java.util.Arrays;

import javax.inject.Inject;

import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.functions.Action1;

public class SocketService extends Service {

    @Inject
    DataManager mDataManager;
    private String mUserUUID;
    private Socket mDefaultSocket;
    private Socket mEnrolmentSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(getClass().getSimpleName(), "Start socket service onCreate called");
        ClockingApplication.get(this).getComponent().inject(this);
        mDefaultSocket = ((ClockingApplication) getApplication()).getDefaultSocket().connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getSimpleName(), "Start socket service onDestroy called");
        mDefaultSocket.off(Socket.EVENT_CONNECT, onConnect);
        mDefaultSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mDefaultSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mDefaultSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mDefaultSocket.off(Socket.EVENT_RECONNECT, onConnect);
        mDefaultSocket.off(Constants.makeEvent(mUserUUID, Constants.CAPTURE_BIO_DATA), onReceiveCaptureBio);
        mDefaultSocket.off(Constants.makeEvent(mUserUUID, Constants.CANCEL_CAPTURE), onReceiveCancelCapture);
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
                        String cancelCaptureBioDataEvent = Constants.makeEvent(uuid, Constants.CANCEL_CAPTURE);
                        Log.d(getClass().getSimpleName(), String.format("user uuid -> %s on %s", uuid, captureBioDataEvent));
                        mDefaultSocket.on(captureBioDataEvent, onReceiveCaptureBio);
                        mDefaultSocket.on(cancelCaptureBioDataEvent, onReceiveCancelCapture);
                    }
                });
    }

    private void launchBioActivity(String bid){
        Intent intent = BioActivity.startNewIntent(this, mUserUUID, bid);
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
        public void call(Object... args) {
            Log.d(getClass().getSimpleName(), "socket service disconnected ...");
        }
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
                launchBioActivity(bid);
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
}
