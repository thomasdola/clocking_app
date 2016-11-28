package com.eurekacachet.clocling.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.eurekacachet.clocling.injection.context.ApplicationContext;

import javax.inject.Inject;


public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "clocking.db";
    public static final int DATABASE_VERSION = 2;

    @Inject
    public DbOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
        try{
            Log.d(this.getClass().getSimpleName(), "DB Creating Called");
            sqLiteDatabase.execSQL(Db.Fingerprints.CREATE);
            sqLiteDatabase.setTransactionSuccessful();
            Log.d(this.getClass().getSimpleName(), "DB Creating finish");
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}
