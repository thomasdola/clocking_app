package com.eurekacachet.clocling.data.model;


import java.util.Arrays;

public class FingerFmd {

    public byte[] fmd;

    public String type;

    public String bid;

    @Override
    public String toString() {
        return "FingerFmd{" +
                "fmd=" + Arrays.toString(fmd) +
                ", type='" + type + '\'' +
                ", bid='" + bid + '\'' +
                '}';
    }
}
