package com.eurekacachet.clocling.data.model;


public class Clock {

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
}
