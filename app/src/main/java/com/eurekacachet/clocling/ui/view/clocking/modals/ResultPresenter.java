package com.eurekacachet.clocling.ui.view.clocking.modals;


import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;

public class ResultPresenter extends BasePresenter<ResultModal> {

    Subscription subscription;
    DataManager mDataManager;

    @Inject
    public ResultPresenter (DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(ResultModal mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(subscription != null)
            subscription.unsubscribe();
    }
}
