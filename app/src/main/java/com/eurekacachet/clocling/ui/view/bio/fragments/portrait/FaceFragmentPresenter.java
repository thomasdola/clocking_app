package com.eurekacachet.clocling.ui.view.bio.fragments.portrait;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class FaceFragmentPresenter extends BasePresenter<FaceFragment> {

    Subscription mSubscription;
    DataManager mDataManager;

    @Inject
    public FaceFragmentPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(FaceFragment mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadCurrentPortraitPath(){
        checkViewAttached();
        getMvpView().setCurrentPortraitPath(mDataManager.getPortraitPath());
    }

    public void setCurrentPortraitPath(String path){
        mDataManager.setPortrait(path);
    }
}
