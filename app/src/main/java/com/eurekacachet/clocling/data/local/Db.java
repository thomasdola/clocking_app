package com.eurekacachet.clocling.data.local;


import android.content.ContentValues;
import android.database.Cursor;

import com.eurekacachet.clocling.data.model.Clock;
import com.eurekacachet.clocling.data.model.Fingerprint;
import com.eurekacachet.clocling.data.model.Fmd;

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
                        COLUMN_FINGERPRINT + " BLOB NOT NULL," +
                        COLUMN_FINGER_TYPE + " TEXT NOT NULL" +
                        ");";

        public static ContentValues toContentValues(Fingerprint fingerprint){
            ContentValues values = new ContentValues();
            values.put(COLUMN_BID, fingerprint.bid);
            values.put(COLUMN_FINGER_TYPE, fingerprint.finger_type);
            values.put(COLUMN_FINGERPRINT, fingerprint.fingerprint);
            return values;
        }

        public static Fingerprint parseCursor(Cursor cursor){
            Fingerprint fingerprint = new Fingerprint();
            fingerprint.bid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BID));
            fingerprint.finger_type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FINGER_TYPE));
            fingerprint.fingerprint = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FINGERPRINT));
            return fingerprint;
        }
    }

    public abstract static class Fmds{

        public static final String TABLE_NAME = "fmds";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_FMD = "fmd";
        public static final String COLUMN_FINGER_TYPE = "finger_type";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        COLUMN_FINGER_TYPE + " TEXT NOT NULL," +
                        COLUMN_FMD + " BLOB NOT NULL" +
                        ");";

        public static ContentValues toContentValues(Fmd fmd){
            ContentValues values = new ContentValues();
            values.put(COLUMN_FMD, fmd.getFmd());
            values.put(COLUMN_FINGER_TYPE, fmd.getFingerType());
            return values;
        }

        public static Fmd parseCursor(Cursor cursor){
            Fmd fmd = new Fmd();
            fmd.fmd = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_FMD));
            fmd.fingerType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FINGER_TYPE));
            return fmd;
        }
    }

    public abstract static class Clocks{

        public static final String TABLE_NAME = "clocks";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_BID = "bid";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        COLUMN_BID + " TEXT NOT NULL," +
                        COLUMN_TIMESTAMP + " INTEGER NOT NULL," +
                        ");";

        public static ContentValues toContentValues(Clock clock){
            ContentValues values = new ContentValues();
            values.put(COLUMN_BID, clock.bid);
            values.put(COLUMN_TIMESTAMP, clock.timestamp);
            return values;
        }

        public static Clock parseCursor(Cursor cursor){
            Clock clock = new Clock();
            clock.bid = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BID));
            clock.timestamp = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));
            return clock;
        }
    }
}
