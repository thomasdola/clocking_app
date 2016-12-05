package com.eurekacachet.clocling.data.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Beneficiary implements Parcelable {

    public String picture;

    public String name;

    public String bid;


    @Override
    public String toString() {
        return "Beneficiary{" +
                "picture='" + picture + '\'' +
                ", name='" + name + '\'' +
                ", bid='" + bid + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picture);
        dest.writeString(this.name);
        dest.writeString(this.bid);
    }

    public Beneficiary() {
    }

    protected Beneficiary(Parcel in) {
        this.picture = in.readString();
        this.name = in.readString();
        this.bid = in.readString();
    }

    public static final Parcelable.Creator<Beneficiary> CREATOR = new Parcelable.Creator<Beneficiary>() {
        @Override
        public Beneficiary createFromParcel(Parcel source) {
            return new Beneficiary(source);
        }

        @Override
        public Beneficiary[] newArray(int size) {
            return new Beneficiary[size];
        }
    };
}
