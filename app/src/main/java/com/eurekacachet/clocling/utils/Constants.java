package com.eurekacachet.clocling.utils;


public class Constants {

    public static final String APP_DIR = "Bio";
    public static final String PORTRAIT = "portrait";
    public static final String FORM = "form";
    public static final String THUMB_RIGHT = "thumb_right";
    public static final String THUMB_RIGHT_FMD = "thumb_right_fmd";
    public static final String THUMB_LEFT = "thumb_left";
    public static final String THUMB_LEFT_FMD = "thumb_left_fmd";
    public static final String INDEX_RIGHT = "index_right";
    public static final String INDEX_RIGHT_FMD = "index_right_fmd";
    public static final String INDEX_LEFT = "index_left";
    public static final String INDEX_LEFT_FMD = "index_left_fmd";
    public static final String ENROLMENT = "enrolment";
    public static final String CANCEL_CAPTURE = "CancelCapture";
    public static final String CAPTURE_BIO_DATA = "CaptureBioData";
    public static final String CAPTURE_BIO_DATA_UPDATE = "CaptureBioDataUpdate";
    public static final String REVIEW_BIO_DATA = "ReviewBioData";
    public static final String REVIEW_BIO_DATA_UPDATE = "ReviewBioDataUpdate";
    public static final String EDIT_CAPTURE = "EditBioData";
    public static final String DEVICES = "DEVICES";
    public static final String DEVICE_CONNECTED = "Device:Connected";
    public static final String DEVICE_DISCONNECTED = "Device:Disconnected";
    public static final String CREDENCE = "CREDENCE";
    public static final String FINGERPRINTS_UPDATED = "FINGERPRINTS:UPDATED";
    public static final String BATCH = "batch";
    public static String SOCKET_URL = "http://192.168.1.138:6001";
    public static String SERVER_URL = "http://192.168.1.138/device-api/";

    public static String makeEvent(String UUID, String event){
        return String.format("%s:%s", UUID, event);
    }
}
