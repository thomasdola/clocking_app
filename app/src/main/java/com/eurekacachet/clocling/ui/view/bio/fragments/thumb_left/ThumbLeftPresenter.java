package com.eurekacachet.clocling.ui.view.bio.fragments.thumb_left;

import android.util.Log;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class ThumbLeftPresenter extends BasePresenter<ThumbLeftFragment> {

    Subscription mSubscriptions;
    DataManager mDataManager;

    @Inject
    public ThumbLeftPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ThumbLeftFragment mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void currentThumbLeftPath(){
        checkViewAttached();
        String path = mDataManager.getThumbLeftPath();
        Log.d(this.getClass().getSimpleName(), String.format("currentThumbLeftPath with -> %s", path));
        getMvpView().setCurrentThumbLeftPath(path);
    }

    public void setCurrentThumbLeftPath(String path){
        checkViewAttached();
        mDataManager.setThumbLeft(path);
    }
}