package com.eurekacachet.clocling.ui.view.bio.fragments.form;

import com.eurekacachet.clocling.ui.base.MvpView;

import org.json.JSONObject;


public interface FormPictureMvpView extends MvpView {
    void setCurrentFormPicturePath(String path);

    void showLoading();

    void hideLoading();

    void sendForReview(JSONObject object);

    void onReview();
}
