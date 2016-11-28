package com.eurekacachet.clocling.ui.view.bio.fragments.index_left;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;


public class IndexLeftPresenter extends BasePresenter<IndexLeftFragment> {

    Subscription subscription;
    DataManager mDataManager;

    @Inject
    public IndexLeftPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(IndexLeftFragment mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(subscription != null) subscription.unsubscribe();
    }

    public void currentIndexLeft(){
        checkViewAttached();
        getMvpView().setCurrentIndexLeftPath(mDataManager.getIndexLeftPath());
    }

    public void setCurrentIndexLeftPath(String path){
        checkViewAttached();
        mDataManager.setIndexLeft(path);
    }
}
