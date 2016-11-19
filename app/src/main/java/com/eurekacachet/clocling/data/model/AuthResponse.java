package com.eurekacachet.clocling.data.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class AuthResponse implements Parcelable {

    public String status;
    public String message;
    public int code;
    @Nullable
    public String token;
    @Nullable
    public String userUUID;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Nullable
    public String getToken() {
        return token;
    }

    @Nullable
    public String getUserUUID() {
        return userUUID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.message);
        dest.writeInt(this.code);
        dest.writeString(this.token);
    }

    public AuthResponse() {
    }

    protected AuthResponse(Parcel in) {
        this.status = in.readString();
        this.message = in.readString();
        this.code = in.readInt();
        this.token = in.readString();
    }

    public static final Parcelable.Creator<AuthResponse> CREATOR = new Parcelable.Creator<AuthResponse>() {
        @Override
        public AuthResponse createFromParcel(Parcel source) {
            return new AuthResponse(source);
        }

        @Override
        public AuthResponse[] newArray(int size) {
            return new AuthResponse[size];
        }
    };
}
