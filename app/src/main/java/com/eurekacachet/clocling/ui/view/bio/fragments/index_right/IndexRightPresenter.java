package com.eurekacachet.clocling.ui.view.bio.fragments.index_right;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;


public class IndexRightPresenter extends BasePresenter<IndexRightFragment> {

    Subscription mSubscription;
    DataManager mDataManager;

    @Inject
    public IndexRightPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(IndexRightFragment mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription != null) mSubscription.unsubscribe();
    }

    public void currentIndexRight(){
        checkViewAttached();
        getMvpView().setCurrentIndexRightPath(mDataManager.getIndexRightPath());
    }

    public void setCurrentIndexRightPath(String path){
        checkViewAttached();
        mDataManager.setIndexRight(path);
    }
}
