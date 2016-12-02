package com.eurekacachet.clocling.ui.view.clocking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;

import javax.inject.Inject;

public class ClockingActivity extends BaseActivity implements ClockingMvpView {

    TextView headerView;
    ImageView fingerView;
    Button scanButton;

    @Inject ClockingPresenter presenter;
    BiometricsManager mBiometricsManager;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_clocking);
        mBiometricsManager = new BiometricsManager(this);
        presenter.attachView(this);
        initView();
        initListeners();
    }

    private void initView() {
        fingerView = (ImageView) findViewById(R.id.fingerView);
        headerView = (TextView) findViewById(R.id.headerView);
        scanButton = (Button) findViewById(R.id.scanButton);
    }

    private void initListeners() {
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanFinger();
            }
        });
    }

    private void scanFinger() {
        headerView.setText(R.string.loading_text);
        fingerView.setImageDrawable(null);
        mBiometricsManager.grabFingerprint(Biometrics.ScanType.SINGLE_FINGER,
                new Biometrics.OnFingerprintGrabbedListener() {
            @Override
            public void onFingerprintGrabbed(Biometrics.ResultCode resultCode, Bitmap bitmap,
                                             byte[] bytes, String filePath, String status) {
                if(status != null) headerView.setText(status);
                if(bitmap != null){
                    fingerView.setImageBitmap(bitmap);
                    mBiometricsManager.convertToFmd(bitmap, Biometrics.FmdFormat.ANSI_378_2004,
                            new Biometrics.OnConvertToFmdListener() {
                        @Override
                        public void onConvertToFmd(Biometrics.ResultCode resultCode, byte[] fmd) {
                            presenter.matchFinger(fmd, mBiometricsManager);
                        }
                    });
                }
            }

            @Override
            public void onCloseFingerprintReader(Biometrics.CloseReasonCode closeReasonCode) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBiometricsManager.initializeBiometrics(new Biometrics.OnInitializedListener() {
            @Override
            public void onInitialized(Biometrics.ResultCode resultCode, String sdkVersion, String requiredVersion) {
                Log.d("BioActivity", "onBiometricsInitialized");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mBiometricsManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBiometricsManager.cancelCapture();
    }

    @Override
    protected void onDestroy() {
        mBiometricsManager.finalizeBiometrics(false);
        super.onDestroy();
        mBiometricsManager.cancelCapture();
        presenter.detachView();
    }

    @Override
    public void showLoading() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading_text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        if(mProgressDialog != null){
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
            mProgressDialog = null;
        }
    }

    @Override
    public void startCompare(byte[] fmd) {
//        presenter.compareFmd(fmd, mBiometricsManager);
        Log.d(this.getClass().getSimpleName(), String.format("size of fmd -> %s", fmd.length));
    }

    @Override
    public void onError(String reason) {

    }

    @Override
    public void onSuccess() {

    }
}
