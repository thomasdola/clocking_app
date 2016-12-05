package com.eurekacachet.clocling.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Clock implements Parcelable {

    public String bid;

    public int timestamp;

    public String getBid() {
        return bid;
    }

    public int getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return "Clock{" +
                "bid='" + bid + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bid);
        dest.writeInt(this.timestamp);
    }

    public Clock() {
    }

    protected Clock(Parcel in) {
        this.bid = in.readString();
        this.timestamp = in.readInt();
    }

    public static final Parcelable.Creator<Clock> CREATOR = new Parcelable.Creator<Clock>() {
        @Override
        public Clock createFromParcel(Parcel source) {
            return new Clock(source);
        }

        @Override
        public Clock[] newArray(int size) {
            return new Clock[size];
        }
    };
}
