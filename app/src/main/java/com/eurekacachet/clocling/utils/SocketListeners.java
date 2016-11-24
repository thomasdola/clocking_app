package com.eurekacachet.clocling.utils;


import android.app.Activity;

import io.socket.emitter.Emitter;

public class SocketListeners {

    public static Emitter.Listener onConnect(final Activity activity){
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

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

                    }
                });
            }
        };
    }
}
