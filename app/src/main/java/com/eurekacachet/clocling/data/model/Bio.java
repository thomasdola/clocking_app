package com.eurekacachet.clocling.data.model;


import android.support.annotation.Nullable;

import java.util.Arrays;

public class Bio {

    @Nullable
    byte[] fmd;

    @Nullable
    String base64Fmd;

    @Nullable
    byte[] file;

    @Nullable
    String base64File;

    @Nullable
    String type;

    @Nullable
    String bid;

    @Nullable
    public String getBase64Fmd() {
        return base64Fmd;
    }

    public void setBase64Fmd(@Nullable String base64Fmd) {
        this.base64Fmd = base64Fmd;
    }

    @Nullable
    public byte[] getFmd() {
        return fmd;
    }

    public void setFmd(@Nullable byte[] fmd) {
        this.fmd = fmd;
    }

    @Nullable
    public String getBase64File() {
        return base64File;
    }

    public void setBase64File(@Nullable String base64File) {
        this.base64File = base64File;
    }

    @Nullable
    public byte[] getFile() {
        return file;
    }

    public void setFile(@Nullable byte[] file) {
        this.file = file;
    }

    @Nullable
    public String getType() {
        return type;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    @Nullable
    public String getBid() {
        return bid;
    }

    public void setBid(@Nullable String bid) {
        this.bid = bid;
    }

    @Override
    public String toString() {
        return "Bio{" +
                "fmd=" + Arrays.toString(fmd) +
                ", base64Fmd='" + base64Fmd + '\'' +
                ", file=" + Arrays.toString(file) +
                ", base64File='" + base64File + '\'' +
                ", type='" + type + '\'' +
                ", bid='" + bid + '\'' +
                '}';
    }
}
