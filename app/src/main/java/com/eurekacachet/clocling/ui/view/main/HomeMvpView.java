package com.eurekacachet.clocling.ui.view.main;

import com.eurekacachet.clocling.ui.base.MvpView;


public interface HomeMvpView extends MvpView {

    void launchLoginActivity();

    void launchBioDataCaptureActivity();

    void setUserUUID(String UUID);
}
