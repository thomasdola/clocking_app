package com.eurekacachet.clocling.ui.view.bio;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;


public class BioPresenter extends BasePresenter<BioActivity> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public BioPresenter(DataManager dataManager){
        mDataManager = dataManager;
    }

    @Override
    public void attachView(BioActivity mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if(mSubscription != null) mSubscription.unsubscribe();
    }


}
