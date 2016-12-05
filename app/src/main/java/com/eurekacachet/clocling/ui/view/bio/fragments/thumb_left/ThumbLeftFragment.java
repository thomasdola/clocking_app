package com.eurekacachet.clocling.ui.view.bio.fragments.thumb_left;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.File;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThumbLeftFragment extends Fragment implements ThumbLeftMvpView {

    Button backButton;
    Button nextButton;
    Button captureButton;
    TextView headerView;
    ImageView fingerView;
    String mThumbLeft;

    @Inject ThumbLeftPresenter presenter;
    private BiometricsManager mBiometricsManager;
    private FileStore mFileStore;
    private Bitmap mThumbLeftBitmap;

    public ThumbLeftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thumb_left, container, false);
        initView(view);
        initListeners();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.currentThumbLeftPath();
        Log.d(this.getClass().getSimpleName(), "onResume called");
        Log.d(this.getClass().getSimpleName(), String.format("path out -> %s", mThumbLeft));
        if(mThumbLeft != null){
            File file = new File(getActivity().getFilesDir(), Constants.THUMB_LEFT);
            Log.d(this.getClass().getSimpleName(), String.format("path -> %s", mThumbLeft));
            Log.d(this.getClass().getSimpleName(), String.format("file exists? -> %s", file.exists()));
            if(file.exists()){
                fingerView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        Log.d(this.getClass().getSimpleName(), "onDestroy called");
    }

    private void initListeners() {
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerView.setText(R.string.loading_text);
                fingerView.setImageDrawable(null);

                mBiometricsManager = ((BioActivity) getActivity())
                        .getBiometricsManager();
                mBiometricsManager
                        .grabFingerprint(Biometrics.ScanType.SINGLE_FINGER, new Biometrics.OnFingerprintGrabbedListener() {
                            @Override
                            public void onFingerprintGrabbed(Biometrics.ResultCode resultCode, Bitmap bitmap, byte[] bytes, String filePath, String status) {
                                if(status != null) headerView.setText(status);
                                if(bitmap != null) {
                                    fingerView.setImageBitmap(bitmap);
                                    mThumbLeftBitmap = bitmap;
                                    mFileStore = new FileStore(getActivity());
                                    mBiometricsManager.convertToFmd(mThumbLeftBitmap, Biometrics.FmdFormat.ANSI_378_2004, new Biometrics.OnConvertToFmdListener() {
                                        @Override
                                        public void onConvertToFmd(Biometrics.ResultCode resultCode, byte[] fmd) {
                                            presenter.setCurrentThumbLeft(
                                                    mFileStore.save(mThumbLeftBitmap, Constants.THUMB_LEFT).getAbsolutePath(),
                                                    mFileStore.save(fmd, Constants.THUMB_LEFT_FMD).getAbsolutePath()
                                            );
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCloseFingerprintReader(Biometrics.CloseReasonCode closeReasonCode) {
                                Log.d("ThumbLeftFragment", "finger reader closed");
                            }
                        });
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
    }

    private void initView(View view) {
        captureButton = (Button) view.findViewById(R.id.captureButton);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        backButton = (Button) view.findViewById(R.id.backButton);
        headerView = (TextView) view.findViewById(R.id.headerView);
        fingerView = (ImageView) view.findViewById(R.id.fingerView);
    }

    @Override
    public void setCurrentThumbLeftPath(String path) {
        Log.d(this.getClass().getSimpleName(), String.format("setCurrentThumbLeft with -> %s", path));
        mThumbLeft = path;
    }
}
