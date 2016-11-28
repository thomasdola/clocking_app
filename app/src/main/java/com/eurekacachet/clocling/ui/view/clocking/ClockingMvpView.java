package com.eurekacachet.clocling.ui.view.clocking;

import com.eurekacachet.clocling.ui.base.MvpView;


public interface ClockingMvpView extends MvpView {
    void showLoading();

    void hideLoading();

    void startCompare(byte[] fmd);

    void onError(String reason);

    void onSuccess();
}
