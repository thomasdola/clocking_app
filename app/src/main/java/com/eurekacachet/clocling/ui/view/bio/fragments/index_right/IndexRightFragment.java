package com.eurekacachet.clocling.ui.view.bio.fragments.index_right;


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
public class IndexRightFragment extends Fragment implements IndexRightMvpView {

    Button backButton;
    Button nextButton;
    Button captureButton;
    TextView headerView;
    ImageView fingerView;
    String mIndexRight;

    @Inject IndexRightPresenter presenter;
    private BiometricsManager mBiometricsManager;
    private FileStore mFileStore;
    private Bitmap mIndexRightBitmap;

    public IndexRightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_index_right, container, false);
        initView(view);
        initListeners();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((BaseActivity) getActivity()).getActivityComponent().inject(this);
        presenter.attachView(this);
        mBiometricsManager = ((BioActivity) getActivity())
                .getBiometricsManager();
        mFileStore = new FileStore(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.currentIndexRight();
        if(mIndexRight != null){
            File file = new File(getContext().getFilesDir(), Constants.INDEX_RIGHT);
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
                mBiometricsManager
                        .grabFingerprint(Biometrics.ScanType.SINGLE_FINGER, new Biometrics.OnFingerprintGrabbedListener() {
                            @Override
                            public void onFingerprintGrabbed(Biometrics.ResultCode resultCode, Bitmap bitmap, byte[] bytes, String filePath, String status) {
                                if(status != null) headerView.setText(status);
                                if(bitmap != null) {
                                    fingerView.setImageBitmap(bitmap);
                                    mIndexRightBitmap = bitmap;

                                    mBiometricsManager.convertToFmd(mIndexRightBitmap, Biometrics.FmdFormat.ANSI_378_2004, new Biometrics.OnConvertToFmdListener() {
                                        @Override
                                        public void onConvertToFmd(Biometrics.ResultCode resultCode, byte[] fmd) {
                                            presenter.setCurrentIndexRight(
                                                    mFileStore.save(mIndexRightBitmap, Constants.INDEX_RIGHT).getAbsolutePath(),
                                                    mFileStore.save(fmd, Constants.INDEX_RIGHT_FMD).getAbsolutePath()
                                            );
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCloseFingerprintReader(Biometrics.CloseReasonCode closeReasonCode) {
                                Log.d("IndexRightFragment", "finger reader closed");
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
    public void setCurrentIndexRightPath(String path) {
        mIndexRight = path;
    }
}
