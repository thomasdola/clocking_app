package com.eurekacachet.clocling.ui.view.bio.fragments.form;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;


public class FormPicturePresenter extends BasePresenter<FormPictureFragment> {

    Subscription subscription;
    DataManager mDataManager;

    @Inject
    public FormPicturePresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(FormPictureFragment mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(subscription != null) subscription.unsubscribe();
    }

    public void currentFormPicture(){
        checkViewAttached();
        getMvpView().setCurrentFormPicturePath(mDataManager.getFormPath());
    }

    public void setCurrentFormPicturePath(String path){
        checkViewAttached();
        mDataManager.setForm(path);
    }
}
