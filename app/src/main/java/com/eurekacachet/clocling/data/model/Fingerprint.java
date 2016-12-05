package com.eurekacachet.clocling.data.model;


public class Fingerprint {

    public String bid;
    public String finger_type;
    public String fingerprint;

    @Override
    public String toString() {
        return "Fingerprint{" +
                "bid='" + bid + '\'' +
                ", finger_type='" + finger_type + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                '}';
    }
}
