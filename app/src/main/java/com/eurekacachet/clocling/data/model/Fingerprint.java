package com.eurekacachet.clocling.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Fingerprint implements Parcelable {

    public String bid;
    public String finger_type;
    public byte[] fingerprint;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bid);
        dest.writeString(this.finger_type);
        dest.writeByteArray(this.fingerprint);
    }

    public Fingerprint() {
    }

    protected Fingerprint(Parcel in) {
        this.bid = in.readString();
        this.finger_type = in.readString();
        this.fingerprint = in.createByteArray();
    }

    public static final Parcelable.Creator<Fingerprint> CREATOR = new Parcelable.Creator<Fingerprint>() {
        @Override
        public Fingerprint createFromParcel(Parcel source) {
            return new Fingerprint(source);
        }

        @Override
        public Fingerprint[] newArray(int size) {
            return new Fingerprint[size];
        }
    };

    @Override
    public String toString() {
        return "Fingerprint{" +
                "bid='" + bid + '\'' +
                ", finger_type='" + finger_type + '\'' +
                ", fingerprint=" + Arrays.toString(fingerprint) +
                '}';
    }
}
