package com.eurekacachet.clocling.ui.view.bio.fragments.portrait;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.credenceid.biometrics.BiometricsManager;
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.utils.Constants;
import com.eurekacachet.clocling.utils.FileStore;
import com.ragnarok.rxcamera.RxCamera;
import com.ragnarok.rxcamera.RxCameraData;
import com.ragnarok.rxcamera.config.RxCameraConfig;
import com.ragnarok.rxcamera.request.Func;

import org.json.JSONObject;

import java.io.File;

import javax.inject.Inject;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class FaceFragment extends Fragment implements FaceFragmentMvpView {

    Button captureButton;
    Button nextButton;
    Button reviewButton;
    Button backButton;
    Button changeButton;
    Button flashOnButton;
    Button flashOffButton;
    boolean flashOn;
    SurfaceView pictureView;
    ImageView pictureTakenView;
    RxCameraConfig mCameraConfig;
    RxCamera mCamera;
    String mPortrait;

    FileStore mFileStore;
    @Inject FaceFragmentPresenter presenter;
    boolean mIsUpdating;
    BiometricsManager mBiometricManager;
    Socket mEnrolmentSocket;
    String mUserUUID;
    String mBid;
    ProgressDialog mProgressDialog;

    public FaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_face, container, false);
        flashOn = false;
        mCameraConfig = new RxCameraConfig.Builder()
                .useBackCamera()
                .setAutoFocus(true)
                .setPreferPreviewFrameRate(15, 30)
                .setPreferPreviewSize(new Point(640, 480), false)
                .setHandleSurfaceEvent(true)
                .build();
        initView(view);
        initListeners();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        mFileStore = new FileStore(getActivity());
        mBiometricManager = ((BioActivity) getActivity()).getBiometricsManager();
        mEnrolmentSocket = ((BioActivity) getActivity()).getEnrolmentSocket();
        mUserUUID = ((BioActivity) getActivity()).getUserUUID();
        mBid = ((BioActivity)getActivity()).getBid();
        mEnrolmentSocket.on(Constants.makeEvent(mUserUUID, Constants.EDIT_CAPTURE), onEditBioData);
        presenter.attachView(this);
        mIsUpdating = ((BioActivity) getActivity()).isBio_updating();
        Log.d(getClass().getSimpleName(), String.format("is updating -> %s", mIsUpdating));
        if(mIsUpdating){
            reviewButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void startCamera() {
        RxCamera.open(getActivity(), mCameraConfig)
                .flatMap(new Func1<RxCamera, Observable<RxCamera>>() {
                    @Override
                    public Observable<RxCamera> call(RxCamera rxCamera) {
                        return rxCamera.bindSurface(pictureView);
                    }
                }).flatMap(new Func1<RxCamera, Observable<RxCamera>>() {
                    @Override
                    public Observable<RxCamera> call(RxCamera rxCamera) {
                        return rxCamera.startPreview();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<RxCamera>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(RxCamera rxCamera) {
                        mCamera = rxCamera;
                    }
                });
    }

    private void stopCamera(){
        if(mCamera != null){
            mCamera.closeCamera();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadCurrentPortraitPath();
        File file = new File(getActivity().getFilesDir(), Constants.PORTRAIT);
        if(mPortrait != null && file.exists()){
            pictureTakenView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            pictureView.setVisibility(View.INVISIBLE);
            captureButton.setVisibility(View.INVISIBLE);
            changeButton.setVisibility(View.VISIBLE);
            pictureTakenView.setVisibility(View.VISIBLE);
        }else {
            startCamera();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopCamera();
    }

    private void initListeners() {

        flashOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offFlash();
            }
        });

        flashOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFlash();
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BioActivity) getActivity()).getWizard().navigateNext();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BioActivity) getActivity()).getWizard().navigatePrevious();
            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopCamera();
                pictureTakenView.setVisibility(View.INVISIBLE);
                pictureView.setVisibility(View.VISIBLE);
                captureButton.setVisibility(View.VISIBLE);
                changeButton.setVisibility(View.INVISIBLE);
                startCamera();
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.readyToReview();
            }
        });
    }

    private void onFlash() {
        if(!checkCamera()){
            return;
        }
        mCamera.action().flashAction(true)
                .subscribe(new Action1<RxCamera>() {
                    @Override
                    public void call(RxCamera rxCamera) {
                        flashOn = true;
                        flashOffButton.setVisibility(View.VISIBLE);
                        flashOnButton.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void offFlash() {
        if(!checkCamera()){
            return;
        }
        mCamera.action().flashAction(false)
                .subscribe(new Action1<RxCamera>() {
                    @Override
                    public void call(RxCamera rxCamera) {
                        flashOn = false;
                        flashOnButton.setVisibility(View.VISIBLE);
                        flashOffButton.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void initView(View view) {
        captureButton = (Button) view.findViewById(R.id.captureButton);
        backButton = (Button) view.findViewById(R.id.backButton);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        reviewButton = (Button) view.findViewById(R.id.reviewButton);
        Log.d(getClass().getSimpleName(), String.format("is updating -> %s", mIsUpdating));
        flashOnButton = (Button) view.findViewById(R.id.flashOnButton);
        flashOffButton = (Button) view.findViewById(R.id.flashOffButton);
        flashOffButton.setVisibility(View.INVISIBLE);
        pictureView = (SurfaceView) view.findViewById(R.id.pictureView);
        pictureTakenView = (ImageView) view.findViewById(R.id.pictureTakenView);
        pictureTakenView.setVisibility(View.INVISIBLE);
        changeButton = (Button) view.findViewById(R.id.changePictureButton);
        changeButton.setVisibility(View.INVISIBLE);
    }

    private boolean checkCamera() {
        return !(mCamera == null || !mCamera.isOpenCamera());
    }

    private void takePicture(){
        if(!checkCamera()) {
            return;
        }

        mCamera.request().takePictureRequest(true, new Func() {
            @Override
            public void call() {
                Log.d("FaceFragment", "captured");
                pictureView.setVisibility(View.INVISIBLE);

                captureButton.setVisibility(View.INVISIBLE);
                changeButton.setVisibility(View.VISIBLE);

                flashOnButton.setVisibility(View.VISIBLE);
                flashOffButton.setVisibility(View.INVISIBLE);
            }
        }, 320, 320, ImageFormat.JPEG, flashOn)
                .subscribe(new Action1<RxCameraData>() {
                    @Override
                    public void call(RxCameraData rxCameraData) {
                        Bitmap bitmap = BitmapFactory
                                .decodeByteArray(rxCameraData.cameraData, 0, rxCameraData.cameraData.length);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                                rxCameraData.rotateMatrix, false);
                        pictureTakenView.setImageBitmap(bitmap);
                        pictureTakenView.setVisibility(View.VISIBLE);
                        presenter.setCurrentPortraitPath(
                                (new FileStore(getActivity()).save(bitmap, Constants.PORTRAIT)).getAbsolutePath()
                        );
                    }
                });
    }

    @Override
    public void setCurrentPortraitPath(String path) {
        mPortrait = path;
    }

    @Override
    public void showLoading() {
        showReviewing();
    }

    private void showReviewing() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.reviewing_text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        hideReviewing();
    }

    private void hideReviewing() {
        try{
            if(mProgressDialog != null){
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onReview() {
        presenter.getBioData(((BioActivity)getActivity()).getBid());
    }

    @Override
    public void sendForReview(JSONObject jsonBios) {
        JSONObject payload = new JSONObject();
        try{
            payload.put("channel",
                    Constants.makeEvent(
                            mUserUUID,
                            Constants.REVIEW_BIO_DATA
                    ));
            payload.put("data", jsonBios);

            mEnrolmentSocket.emit(Constants.ENROLMENT, payload, new Ack() {
                @Override
                public void call(Object... args) {
                    Log.d(getClass().getSimpleName(), "done sending bio data");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Emitter.Listener onEditBioData = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            presenter.editBioData();
        }
    };
}
