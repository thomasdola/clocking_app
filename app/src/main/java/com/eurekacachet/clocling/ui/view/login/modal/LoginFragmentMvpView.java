package com.eurekacachet.clocling.ui.view.login.modal;


import com.eurekacachet.clocling.ui.base.MvpView;

public interface LoginFragmentMvpView extends MvpView {

//    void toggleInputField(Boolean enabled);

    void toggleLoading(Boolean visible);

    void launchMainActivity();

    void onSignInFailed(String reason);
}
