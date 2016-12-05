package com.eurekacachet.clocling.utils.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.eurekacachet.clocling.ClockingApplication;
import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.data.model.ActionResponse;
import com.eurekacachet.clocling.data.model.Fingerprint;
import com.eurekacachet.clocling.utils.AndroidComponentUtil;
import com.eurekacachet.clocling.utils.NetworkUtil;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class SyncService extends Service {

    @Inject
    DataManager mDataManager;
    private Subscription mSubscription;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, SyncService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ClockingApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.i(this.getClass().getSimpleName(), "Starting sync...");

        if (!NetworkUtil.isNetworkConnected(this)) {
            Log.d(getClass().getSimpleName(), "Sync canceled, connection not available");
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        if (mSubscription != null && !mSubscription.isUnsubscribed()) mSubscription.unsubscribe();
        mSubscription = mDataManager.syncFingerprints()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Fingerprint>() {
                    @Override
                    public void onCompleted() {
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(Fingerprint fingerprint) {}
                });

        mSubscription = mDataManager.syncClocks()
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<ActionResponse>() {
                    @Override
                    public void onCompleted() {
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(ActionResponse actionResponse) {}
                });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Timber.i("Connection is now available, triggering sync...");
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context));
            }
        }
    }
}
