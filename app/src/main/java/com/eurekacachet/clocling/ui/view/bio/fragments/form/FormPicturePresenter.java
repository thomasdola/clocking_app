package com.eurekacachet.clocling.ui.view.bio.fragments.form;

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
import rx.functions.Func1;
import rx.schedulers.Schedulers;


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
        getMvpView().setCurrentFormPicturePath(mDataManager.getPath(Constants.FORM));
    }

    public void setCurrentFormPicturePath(String path){
        checkViewAttached();
        mDataManager.setPath(Constants.FORM, path);
    }

    public void getBioData(final String bid) {
        checkViewAttached();
        getMvpView().showLoading();
        subscription = mDataManager.getBio()
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
                .subscribe(new Subscriber<List<Bio>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Bio> bios) {
                        getMvpView().convertToFmd(bios);
                    }
                });
    }

    public void readyToReview() {
        checkViewAttached();
        Log.d(getClass().getSimpleName(), "readyToReview called");
        if(mDataManager.readyToReview()){
            Log.d(getClass().getSimpleName(), "ready");
            getMvpView().onReview();
        }else {
            Log.d(getClass().getSimpleName(), "not ready");
            return;
        }
    }

    public void send(List<Bio> bios) {
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

        getMvpView().sendForReview(jsonBios);
    }

    public void editBioData() {
        checkViewAttached();
        getMvpView().hideLoading();
    }
}
