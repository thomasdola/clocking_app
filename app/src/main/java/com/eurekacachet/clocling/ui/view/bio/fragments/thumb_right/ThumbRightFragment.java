package com.eurekacachet.clocling.ui.view.bio.fragments.thumb_right;


import android.content.Intent;
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
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.ui.view.main.MainActivity;
import com.eurekacachet.clocling.utils.Constants;
import com.eurekacachet.clocling.utils.FileStore;

import java.io.File;

import javax.inject.Inject;

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

    public ThumbRightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thumb_right, container, false);
        initView(view);
        initListeners();
        Log.d(this.getClass().getSimpleName(), "onCreateView called");
        return view;
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
        Log.d(this.getClass().getSimpleName(), "onDestroy called");
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getCurrentThumbRight();
        Log.d(this.getClass().getSimpleName(), "onResume called");
        Log.d(this.getClass().getSimpleName(), String.format("path out -> %s", mThumbRight));

        if(mThumbRight != null){
            File file = new File(getContext().getFilesDir(), Constants.THUMB_RIGHT);
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
                getContext().deleteFile(Constants.THUMB_RIGHT);
                getContext().deleteFile(Constants.THUMB_LEFT);
                getContext().deleteFile(Constants.INDEX_RIGHT);
                getContext().deleteFile(Constants.INDEX_LEFT);
                getContext().deleteFile(Constants.PORTRAIT);
                getContext().deleteFile(Constants.FORM);

                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                headerView.setText(R.string.loading_text);
                fingerView.setImageDrawable(null);

                ((BioActivity) getActivity())
                        .getBiometricsManager()
                        .grabFingerprint(Biometrics.ScanType.SINGLE_FINGER, new Biometrics.OnFingerprintGrabbedListener() {
                            @Override
                            public void onFingerprintGrabbed(Biometrics.ResultCode resultCode, Bitmap bitmap, byte[] bytes, String filePath, String status) {
                                if(status != null) headerView.setText(status);
                                if(bitmap != null){
                                    fingerView.setImageBitmap(bitmap);
                                    presenter.setCurrentThumb((new FileStore(getContext()))
                                            .save(bitmap, Constants.THUMB_RIGHT).getAbsolutePath());
                                }

                            }

                            @Override
                            public void onCloseFingerprintReader(Biometrics.CloseReasonCode closeReasonCode) {

                            }
                        });
            }
        });
    }

    @Override
    public void currentThumbRight(String path) {
        Log.d(this.getClass().getSimpleName(), String.format("currentThumbRight with -> %s", path));
        mThumbRight = path;
    }
}
