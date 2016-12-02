package com.eurekacachet.clocling.data.model;


import java.util.Arrays;

public class Fmd {

    public byte[] fmd;
    public String fingerType;

    public byte[] getFmd() {
        return fmd;
    }

    public String getFingerType() {
        return fingerType;
    }

    @Override
    public String toString() {
        return "Fmd{" +
                "fmd=" + Arrays.toString(fmd) +
                ", fingerType='" + fingerType + '\'' +
                '}';
    }
}
