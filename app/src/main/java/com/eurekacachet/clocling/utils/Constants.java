package com.eurekacachet.clocling.utils;


public class Constants {

    public static String SOCKET_URL = "http://192.168.1.136:6001";
    public static String SERVER_URL = "http://192.168.1.136/device-api/";

    public static String userChannel(String UUID){
        return String.format("staff_%s_channel", UUID);
    }

    public static String makeEvent(String UUID, String event){
        return String.format("%s:%s", Constants.userChannel(UUID), event);
    }
}
