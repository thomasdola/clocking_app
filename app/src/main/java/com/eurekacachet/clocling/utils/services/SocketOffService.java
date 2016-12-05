package com.eurekacachet.clocling.utils.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.eurekacachet.clocling.ClockingApplication;

import io.socket.client.Socket;

public class SocketOffService extends Service {
    private Socket mDefaultSocket;

    public SocketOffService() {
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SocketOffService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ClockingApplication.get(this).getComponent().inject(this);
        mDefaultSocket = ((ClockingApplication) getApplication()).getDefaultSocket();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mDefaultSocket.connected()){
            mDefaultSocket.disconnect();
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
