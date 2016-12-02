package com.eurekacachet.clocling.ui.view.bio.fragments.form;

import android.graphics.Bitmap;

import com.eurekacachet.clocling.data.model.Bio;
import com.eurekacachet.clocling.ui.base.MvpView;

import org.json.JSONObject;

import java.util.List;


public interface FormPictureMvpView extends MvpView {
    void setCurrentFormPicturePath(String path);

    void showLoading();

    void hideLoading();

    void sendForReview(JSONObject object);

    void convertToFmd(List<Bio> bios);

    void onReview();

    void enableReview();

    void disableReview();
}
