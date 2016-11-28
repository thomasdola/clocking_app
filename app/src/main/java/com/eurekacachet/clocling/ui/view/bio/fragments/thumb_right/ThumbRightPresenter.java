package com.eurekacachet.clocling.ui.view.bio.fragments.thumb_right;

import android.util.Log;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class ThumbRightPresenter extends BasePresenter<ThumbRightFragment> {

    Subscription mSubscriptions;
    DataManager mDataManager;

    @Inject
    public ThumbRightPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ThumbRightFragment mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscriptions != null) mSubscriptions.unsubscribe();
    }

    public void getCurrentThumbRight(){
        checkViewAttached();
        String path = mDataManager.getThumbRightPath();
        Log.d(this.getClass().getSimpleName(), String.format("getCurrentThumbRight with -> %s", path));
        getMvpView().currentThumbRight(path);
    }

    public void setCurrentThumb(String path){
        checkViewAttached();
        mDataManager.setThumbRight(path);
    }
}
