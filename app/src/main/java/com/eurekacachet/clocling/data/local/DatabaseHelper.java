package com.eurekacachet.clocling.data.local;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eurekacachet.clocling.data.model.Fingerprint;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.Collection;
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
        return Observable.create(new Observable.OnSubscribe<Fingerprint>() {
            @Override
            public void call(Subscriber<? super Fingerprint> subscriber) {
                if(subscriber.isUnsubscribed()) return;
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try{
                    mDb.delete(Db.Fingerprints.TABLE_NAME, null);
                    for (Fingerprint fingerprint : fingerprints) {
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

    public Observable<List<Fingerprint>> getFingerprints(){
        return mDb.createQuery(Db.Fingerprints.TABLE_NAME,
                "SELECT * FROM " + Db.Fingerprints.TABLE_NAME)
                .mapToList(new Func1<Cursor, Fingerprint>() {
                    @Override
                    public Fingerprint call(Cursor cursor) {
                        return Db.Fingerprints.parseCursor(cursor);
                    }
                });
    }
}
