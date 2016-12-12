package com.eurekacachet.clocling.ui.view.bio.fragments.thumb_right;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.utils.Constants;
import com.eurekacachet.clocling.utils.FileStore;

import org.json.JSONObject;

import java.io.File;

import javax.inject.Inject;

import io.socket.client.Ack;
import io.socket.client.Socket;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThumbRightFragment extends Fragment implements ThumbRightMvpView {

    Button nextButton;
    Button cancelButton;
    Button captureButton;
    ImageView fingerView;
    TextView headerView;
    private String mThumbRight;
    @Inject ThumbRightPresenter presenter;
    private Socket mEnrolmentSocket;
    private String mBid;
    private String mUserUUID;
    BiometricsManager mBiometricsManager;
    FileStore mFileStore;
    private byte[] mFmd;
    private Bitmap mFingerImage;
    private boolean mFirstTime;

    public ThumbRightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thumb_right, container, false);
        mEnrolmentSocket = ((BioActivity) getActivity()).getEnrolmentSocket();
        mBid = ((BioActivity) getActivity()).getBid();
        mUserUUID = ((BioActivity) getActivity()).getUserUUID();
        mBiometricsManager = ((BioActivity) getActivity()).getBiometricsManager();
        mFileStore = new FileStore(getActivity());
        initView(view);
        initListeners();
        if(((BioActivity) getActivity()).getFirstTime()){
            showConfirmBid();
        }
        return view;
    }

    private void showConfirmBid() {
        AlertDialog.Builder builder;
        AlertDialog alertDialog;
        builder = new AlertDialog.Builder(getActivity())
                .setMessage(String.format("Capture Bio for %s", mBid))
                .setPositiveButton(R.string.start_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyDesktop();
                        getActivity().finish();
                    }
                });
        alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(this.getClass().getSimpleName(), "onActivityCreated called");
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(this.getClass().getSimpleName(), "onStart called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(this.getClass().getSimpleName(), "onStop called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        doCleanUp();
        notifyDesktop();
        Log.d(this.getClass().getSimpleName(), "onDestroy called");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getCurrentThumbRight();
        Log.d(this.getClass().getSimpleName(), "onResume called");
        Log.d(this.getClass().getSimpleName(), String.format("path out -> %s", mThumbRight));

        if(mThumbRight != null){
            File file = new File(getActivity().getFilesDir(), Constants.THUMB_RIGHT);
            if(file.exists()){
                fingerView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
            Log.d(this.getClass().getSimpleName(), String.format("path -> %s", mThumbRight));
            Log.d(this.getClass().getSimpleName(), String.format("file exists? -> %s", file.exists()));
        }
    }

    private void initView(View view) {
        nextButton = (Button) view.findViewById(R.id.nextButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        captureButton = (Button) view.findViewById(R.id.captureButton);
        fingerView = (ImageView) view.findViewById(R.id.fingerView);
        headerView = (TextView) view.findViewById(R.id.headerView);
    }

    private void initListeners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BioActivity) getActivity()).getWizard().navigateNext();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doCleanUp();
                notifyDesktop();
                getActivity().finish();
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerView.setText(R.string.loading_text);
                fingerView.setImageDrawable(null);
                mBiometricsManager
                        .grabFingerprint(Biometrics.ScanType.SINGLE_FINGER, new Biometrics.OnFingerprintGrabbedListener() {
                            @Override
                            public void onFingerprintGrabbed(Biometrics.ResultCode resultCode, Bitmap bitmap, byte[] bytes, String filePath, String status) {
                                if(status != null) headerView.setText(status);
                                if(bitmap != null){
                                    fingerView.setImageBitmap(bitmap);
                                    mFingerImage = bitmap;

                                    mBiometricsManager.convertToFmd(bitmap, Biometrics.FmdFormat.ANSI_378_2004, new Biometrics.OnConvertToFmdListener() {
                                        @Override
                                        public void onConvertToFmd(Biometrics.ResultCode resultCode, byte[] bytes) {
                                            mFmd = bytes;
                                            presenter.setCurrentThumbRight(
                                                    mFileStore.save(mFingerImage, Constants.THUMB_RIGHT).getAbsolutePath(),
                                                    mFileStore.save(mFmd, Constants.THUMB_RIGHT_FMD).getAbsolutePath()
                                            );
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCloseFingerprintReader(Biometrics.CloseReasonCode closeReasonCode) {
                                Log.d(getClass().getSimpleName(), "fingerprint reader close for tR");
                            }
                        });
            }
        });
    }

    private void doCleanUp() {
        getActivity().deleteFile(Constants.THUMB_RIGHT);
        getActivity().deleteFile(Constants.THUMB_RIGHT_FMD);
        getActivity().deleteFile(Constants.THUMB_LEFT);
        getActivity().deleteFile(Constants.THUMB_LEFT_FMD);
        getActivity().deleteFile(Constants.INDEX_RIGHT);
        getActivity().deleteFile(Constants.INDEX_RIGHT_FMD);
        getActivity().deleteFile(Constants.INDEX_LEFT);
        getActivity().deleteFile(Constants.INDEX_LEFT_FMD);
        getActivity().deleteFile(Constants.PORTRAIT);
        getActivity().deleteFile(Constants.FORM);
    }

    private void notifyDesktop() {
        String cancelCaptureBioDataEvent = Constants.makeEvent(mUserUUID, Constants.CANCEL_CAPTURE);
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("channel", cancelCaptureBioDataEvent);
            mEnrolmentSocket.emit(Constants.ENROLMENT, jsonObject, new Ack() {
                @Override
                public void call(Object... args) {
                    Log.d(getClass().getSimpleName(), "done cancelling capture");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void currentThumbRight(String path) {
        Log.d(this.getClass().getSimpleName(), String.format("currentThumbRight with -> %s", path));
        mThumbRight = path;
    }
}
