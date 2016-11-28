package com.eurekacachet.clocling.data.local;


import android.content.ContentValues;
import android.database.Cursor;

import com.eurekacachet.clocling.data.model.Fingerprint;

public class Db {

    public Db(){}

    public abstract static class Fingerprints{

        public static final String TABLE_NAME = "fingerprints";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_BID = "bid";
        public static final String COLUMN_FINGERPRINT = "fingerprint";
        public static final String COLUMN_FINGER_TYPE = "finger_type";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        COLUMN_BID + " TEXT NOT NULL," +
                        COLUMN_FINGERPRINT + " TEXT NOT NULL," +
                        COLUMN_FINGER_TYPE + " BLOB NOT NULL," +
                        ");";

        public static ContentValues toContentValues(Fingerprint fingerprint){
            ContentValues values = new ContentValues();
            values.put(COLUMN_BID, fingerprint.bid);
            values.put(COLUMN_FINGER_TYPE, fingerprint.fingerType);
            values.put(COLUMN_FINGERPRINT, fingerprint.fingerprint);
            return values;
        }

        public static Fingerprint parseCursor(Cursor cursor){
            Fingerprint fingerprint = new Fingerprint();
            fingerprint.bid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BID));
            fingerprint.fingerType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FINGER_TYPE));
            fingerprint.fingerprint = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FINGERPRINT));
            return fingerprint;
        }
    }
}
