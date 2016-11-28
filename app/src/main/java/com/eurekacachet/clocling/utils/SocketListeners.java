package com.eurekacachet.clocling.utils;


import android.app.Activity;
import android.util.Log;

import io.socket.emitter.Emitter;

public class SocketListeners {

    public static Emitter.Listener onConnect(final Activity activity){
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(this.getClass().getSimpleName(), "socket success");
                    }
                });
            }
        };
    }

    public static Emitter.Listener onDisconnect(final Activity activity){
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(this.getClass().getSimpleName(), "socket disconnect");
                    }
                });
            }
        };
    }

    public static Emitter.Listener onError(final Activity activity){
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(this.getClass().getSimpleName(), "socket failed");
            }
        };
    }
}
