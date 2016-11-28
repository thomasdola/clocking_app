package com.eurekacachet.clocling.ui.view.bio.fragments.index_left;


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
import com.eurekacachet.clocling.utils.Constants;
import com.eurekacachet.clocling.utils.FileStore;

import java.io.File;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexLeftFragment extends Fragment implements IndexLeftMvpView {

    Button backButton;
    Button nextButton;
    Button captureButton;
    TextView headerView;
    ImageView fingerView;
    String mIndexLeft;

    @Inject IndexLeftPresenter presenter;

    public IndexLeftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_index_left, container, false);
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
        presenter.currentIndexLeft();
        if(mIndexLeft != null){
            File file = new File(getContext().getFilesDir(), Constants.INDEX_LEFT);
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

                ((BioActivity) getActivity())
                        .getBiometricsManager()
                        .grabFingerprint(Biometrics.ScanType.SINGLE_FINGER, new Biometrics.OnFingerprintGrabbedListener() {
                            @Override
                            public void onFingerprintGrabbed(Biometrics.ResultCode resultCode, Bitmap bitmap, byte[] bytes, String filePath, String status) {
                                if(status != null) headerView.setText(status);
                                if(bitmap != null) {
                                    fingerView.setImageBitmap(bitmap);
                                    presenter.setCurrentIndexLeftPath((new FileStore(getContext()))
                                            .save(bitmap, Constants.INDEX_LEFT).getAbsolutePath());
                                }
                            }

                            @Override
                            public void onCloseFingerprintReader(Biometrics.CloseReasonCode closeReasonCode) {
                                Log.d("IndexLeftFragment", "finger reader closed");
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
        fingerView = (ImageView) view.findViewById(R.id.fingerView);
        headerView = (TextView) view.findViewById(R.id.headerView);
    }

    @Override
    public void setCurrentIndexLeftPath(String path) {
        mIndexLeft = path;
    }
}
