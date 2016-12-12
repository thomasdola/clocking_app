package com.eurekacachet.clocling.ui.view.clocking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.data.model.Beneficiary;
import com.eurekacachet.clocling.data.model.Clock;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.clocking.modals.ErrorModal;
import com.eurekacachet.clocling.ui.view.clocking.modals.ResultModal;

import javax.inject.Inject;

public class ClockingActivity extends BaseActivity implements ClockingMvpView {

    private static final String DISPLAY_CLOCK_INFO_TAG = "display_clock_info";
    private static final String DISPLAY_MATCH_ERROR_TAG = "display_match_error";
    TextView headerView;
    ImageView fingerView;
    Button scanButton;

    @Inject ClockingPresenter presenter;
    BiometricsManager mBiometricsManager;
    ProgressDialog mProgressDialog;
    private TelephonyManager mTelephonyManager;

    public static Intent startNewIntent(Context context){
        return new Intent(context, ClockingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_clocking);
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
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
                            presenter.matchFinger(fmd, mBiometricsManager, mTelephonyManager.getDeviceId());
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
        this.hideLoading();
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
        try{
            if(mProgressDialog != null){
                if(mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
                mProgressDialog = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void startCompare(byte[] fmd) {
//        presenter.compareFmd(fmd, mBiometricsManager);
        Log.d(this.getClass().getSimpleName(), String.format("size of fmd -> %s", fmd.length));
    }

    @Override
    public void onError() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ErrorModal modal = ErrorModal.newInstance();
        modal.show(fragmentManager, DISPLAY_MATCH_ERROR_TAG);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void displayInfo(Beneficiary beneficiary) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ResultModal modal = ResultModal.newInstance(beneficiary);
        modal.show(fragmentManager, DISPLAY_CLOCK_INFO_TAG);
    }

    @Override
    public void displayLittleInfo(Clock clock) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ResultModal modal = ResultModal.newInstance(clock);
        modal.show(fragmentManager, DISPLAY_CLOCK_INFO_TAG);
    }


}
