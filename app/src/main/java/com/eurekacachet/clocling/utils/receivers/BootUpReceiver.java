package com.eurekacachet.clocling.utils.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.eurekacachet.clocling.utils.services.SocketService;

public class BootUpReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startSocketServiceIntent = new Intent(context, SocketService.class);
//        startWakefulService(context, startSocketServiceIntent);
    }
}
