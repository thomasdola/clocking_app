package com.eurekacachet.clocling.data.local;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.eurekacachet.clocling.data.model.Clock;
import com.eurekacachet.clocling.data.model.FingerFmd;
import com.eurekacachet.clocling.data.model.Fingerprint;
import com.eurekacachet.clocling.data.model.Fmd;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DatabaseHelper {
    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        SqlBrite.Builder briteBuilder = new SqlBrite.Builder();
        mDb = briteBuilder.build().wrapDatabaseHelper(dbOpenHelper, Schedulers.immediate());
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }



    public Observable<Fingerprint> saveFingerprints(final Collection<Fingerprint> fingerprints){
        Log.d(getClass().getSimpleName(),
                String.format("saveFingerprints called with -> %s", fingerprints));
        return Observable.create(new Observable.OnSubscribe<Fingerprint>() {
            @Override
            public void call(Subscriber<? super Fingerprint> subscriber) {
                if(subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try{
                    mDb.delete(Db.Fingerprints.TABLE_NAME, null);
                    for (Fingerprint fingerprint : fingerprints) {
                        Log.d(getClass().getSimpleName(), String.format("finger -> %s", fingerprint));
                        long result = mDb.insert(Db.Fingerprints.TABLE_NAME,
                                Db.Fingerprints.toContentValues(fingerprint),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) subscriber.onNext(fingerprint);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Clock>> getClocks(){
        return mDb.createQuery(Db.Clocks.TABLE_NAME,
                "SELECT * FROM " + Db.Clocks.TABLE_NAME)
                .mapToList(new Func1<Cursor, Clock>() {
                    @Override
                    public Clock call(Cursor cursor) {
                        return Db.Clocks.parseCursor(cursor);
                    }
                });
    }

    public Observable<List<FingerFmd>> getFingerprints(){
        return mDb.createQuery(Db.Fingerprints.TABLE_NAME,
                "SELECT * FROM " + Db.Fingerprints.TABLE_NAME)
                .mapToList(new Func1<Cursor, FingerFmd>() {
                    @Override
                    public FingerFmd call(Cursor cursor) {
                        return Db.Fingerprints.parseCursor(cursor);
                    }
                });
    }

    public Observable<Fmd> saveFmd(final Fmd fmd){
        return Observable.create(new Observable.OnSubscribe<Fmd>() {
            @Override
            public void call(Subscriber<? super Fmd> subscriber) {
                if(subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try{
                    long result = mDb.insert(Db.Fmds.TABLE_NAME,
                            Db.Fmds.toContentValues(fmd),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    if (result >= 0) subscriber.onNext(fmd);
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                }finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Fmd> getFmd(String fingerType){
        return mDb.createQuery(Db.Fmds.TABLE_NAME, String.format("SELECT * FROM %s", Db.Fmds.TABLE_NAME))
                .map(new Func1<SqlBrite.Query, Fmd>() {
                    @Override
                    public Fmd call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        Fmd fmd = Db.Fmds.parseCursor(cursor);
                        assert cursor != null;
                        cursor.close();
                        return fmd;
                    }
                });
    }

    public Observable<Clock> saveClock(final Clock clock) {
        return Observable.create(new Observable.OnSubscribe<Clock>() {
            @Override
            public void call(Subscriber<? super Clock> subscriber) {
                if(subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try{
                    long result = mDb.insert(Db.Clocks.TABLE_NAME,
                            Db.Clocks.toContentValues(clock),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    if (result >= 0) subscriber.onNext(clock);
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }
}
