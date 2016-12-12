package com.eurekacachet.clocling.ui.view.bio.fragments.portrait;


import com.eurekacachet.clocling.ui.base.MvpView;

import org.json.JSONObject;

public interface FaceFragmentMvpView extends MvpView {

    void setCurrentPortraitPath(String path);

    void showLoading();

    void hideLoading();

    void onReview();

    void sendForReview(JSONObject jsonBios);
}
