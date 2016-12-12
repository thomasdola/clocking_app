package com.eurekacachet.clocling.ui.view.bio.fragments.portrait;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.eurekacachet.clocling.data.DataManager;
import com.eurekacachet.clocling.data.model.Bio;
import com.eurekacachet.clocling.data.model.BioDic;
import com.eurekacachet.clocling.ui.base.BasePresenter;
import com.eurekacachet.clocling.utils.Constants;
import com.google.common.io.Files;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
        getMvpView().setCurrentPortraitPath(mDataManager.getPath(Constants.PORTRAIT));
    }

    public void setCurrentPortraitPath(String path){
        mDataManager.setPath(Constants.PORTRAIT, path);
    }

    public void getBioData(final String bid) {
        checkViewAttached();
        getMvpView().showLoading();
        mSubscription = mDataManager.getBio()
                .subscribeOn(Schedulers.io())
                .map(new Func1<BioDic, Bio>() {
                    @Override
                    public Bio call(BioDic bioDic) {
                        File file = new File(bioDic.getPath());
                        final Bio bio = new Bio();
                        bio.setBid(bid);
                        bio.setType(bioDic.getType());
                        if(file.exists()){
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            byte[] bytes = getMvpView().mFileStore.bitmapToByteArray(bitmap);
                            bio.setFile(bytes);
                            bio.setBase64File(Base64.encodeToString(bytes, Base64.DEFAULT));
                        }
                        return bio;
                    }
                })
                .map(new Func1<Bio, Bio>() {
                    @Override
                    public Bio call(Bio bio) {
                        String path = mDataManager.getPath(String.format("%s_fmd", bio.getType()));

                        if(path == null) return bio;

                        File file = new File(path);
                        if(file.exists()){
                            try{
                                byte[] fmd = Files.toByteArray(file);
                                bio.setFmd(fmd);
                                bio.setBase64Fmd(Base64.encodeToString(fmd, Base64.DEFAULT));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        return bio;
                    }
                })
                .toList()
                .map(new Func1<List<Bio>, JSONObject>() {
                    @Override
                    public JSONObject call(List<Bio> bios) {
                        JSONObject jsonBios = new JSONObject();
                        for (Bio bio : bios) {
                            try{
                                JSONObject map = new JSONObject();
                                map.put("bid", bio.getBid());
                                map.put("encoded", bio.getBase64File());
                                map.put("fmd", bio.getBase64Fmd());
                                map.put("type", bio.getType());
                                jsonBios.put(bio.getType(), map);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        return jsonBios;
                    }
                })
                .subscribe(new Subscriber<JSONObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(JSONObject jsonBios) {
                        getMvpView().sendForReview(jsonBios);
                    }
                });
    }

    public void editBioData() {
        checkViewAttached();
        getMvpView().hideLoading();
    }

    public void readyToReview() {
        checkViewAttached();
        Log.d(getClass().getSimpleName(), "readyToReview called");
        if(mDataManager.readyToReview(true)){
            Log.d(getClass().getSimpleName(), "ready");
            getMvpView().onReview();
        }else {
            Log.d(getClass().getSimpleName(), "not ready");
            return;
        }
    }
}
