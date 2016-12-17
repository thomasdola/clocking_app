package com.eurekacachet.clocling.ui.view.clocking;

import com.eurekacachet.clocling.data.model.Beneficiary;
import com.eurekacachet.clocling.data.model.Clock;
import com.eurekacachet.clocling.ui.base.MvpView;


public interface ClockingMvpView extends MvpView {
    void showLoading();

    void hideLoading();

    void startCompare(byte[] fmd);

    void onError(String reason);

    void onMatchFailed();

    void onSuccess();

    void displayInfo(Beneficiary beneficiary);

    void displayLittleInfo(Clock clock);
}
