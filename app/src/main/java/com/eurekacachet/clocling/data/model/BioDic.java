package com.eurekacachet.clocling.data.model;


public class BioDic {
    String type;
    String path;

    public BioDic(String type, String path) {
        this.type = type;
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "BioDic{" +
                "type='" + type + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
