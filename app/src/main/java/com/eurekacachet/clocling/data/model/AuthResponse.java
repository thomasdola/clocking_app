package com.eurekacachet.clocling.data.model;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class AuthResponse implements Parcelable {

    String status;
    String message;
    int code;
    @Nullable
    String token;
    @Nullable
    String userUUID;

    public int code() {
        return code;
    }

    public String status() {
        return status;
    }

    public String message() {
        return message;
    }

    @Nullable
    public String token() {
        return token;
    }

    @Nullable
    public String userUUID() {
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

    @Override
    public String toString() {
        return "AuthResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", token='" + token + '\'' +
                ", userUUID='" + userUUID + '\'' +
                '}';
    }
}
