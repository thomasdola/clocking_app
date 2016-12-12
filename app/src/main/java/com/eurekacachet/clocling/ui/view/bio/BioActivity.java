package com.eurekacachet.clocling.ui.view.bio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;
import com.eurekacachet.clocling.ClockingApplication;
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.bio.pages.FacePage;
import com.eurekacachet.clocling.ui.view.bio.pages.FormPicturePage;
import com.eurekacachet.clocling.ui.view.bio.pages.IndexLeftPage;
import com.eurekacachet.clocling.ui.view.bio.pages.IndexRightPage;
import com.eurekacachet.clocling.ui.view.bio.pages.ThumbLeftPage;
import com.eurekacachet.clocling.ui.view.bio.pages.ThumbRightPage;
import com.eurekacachet.clocling.utils.Constants;

import javax.inject.Inject;

import io.socket.client.Socket;
import me.panavtec.wizard.Wizard;
import me.panavtec.wizard.WizardListener;
import me.panavtec.wizard.WizardPage;
import me.panavtec.wizard.WizardPageListener;

public class BioActivity extends BaseActivity implements WizardPageListener, WizardListener, BioMvpView {

    private static final String BID_EXTRA = "bid";
    private static final String USER_UUID_EXTRA = "user_uuid";
    private static final String BIO_DATA_UPDATE_EXTRA = "bio_data_update";
    @Inject BioPresenter presenter;
    private Wizard wizard;
    BiometricsManager mBiometricsManager;
    String mBID;
    String mUserUUID;
    Boolean mFirstTime;
    private boolean bio_updating;
    private Socket mEnrolmentSocket;

    public static Intent startNewIntent(Context context, String userUUID, String bid,
                                        boolean updating){
        Log.d("BioActivity", String.valueOf(updating));
        Intent intent = new Intent(context, BioActivity.class);
        intent.putExtra(BID_EXTRA, bid);
        intent.putExtra(USER_UUID_EXTRA, userUUID);
        intent.putExtra(BIO_DATA_UPDATE_EXTRA, updating);
        return intent;
    }

    private BroadcastReceiver cancelCaptureBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getSimpleName(), "bio on create called");
        getActivityComponent().inject(this);
        mFirstTime = true;
        bio_updating = getIntent().getBooleanExtra(BIO_DATA_UPDATE_EXTRA, false);
        mBID = getIntent().getStringExtra(BID_EXTRA).replace("\"", "");
        mUserUUID = getIntent().getStringExtra(USER_UUID_EXTRA);
        Log.d(getClass().getSimpleName(), String.format("form bio -> %s, %s, %s", bio_updating, mBID, mUserUUID));
        setContentView(R.layout.activity_bio);
        mEnrolmentSocket = ((ClockingApplication) getApplication())
                .getEnrolmentSocket().connect();
        mBiometricsManager = new BiometricsManager(this);
        initView();
        presenter.attachView(this);
        initWizard();
        registerReceiver(cancelCaptureBroadcastReceiver, new IntentFilter(Constants.CANCEL_CAPTURE));
    }

    public boolean isBio_updating(){
        return bio_updating;
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
    protected void onStop() {
        super.onStop();
        mBiometricsManager.cancelCapture();
    }

    @Override
    protected void onDestroy() {
        mBiometricsManager.finalizeBiometrics(false);
        super.onDestroy();
        unregisterReceiver(cancelCaptureBroadcastReceiver);
        presenter.detachView();
    }

    public Boolean getFirstTime(){
        return mFirstTime;
    }

    public void setFirstTime(Boolean v){
        mFirstTime = true;
    }

    private void initWizard() {
        wizard = new Wizard.Builder(
                this,
                new ThumbRightPage(),
                new ThumbLeftPage(),
                new IndexRightPage(),
                new IndexLeftPage(),
                new FacePage(),
                new FormPicturePage()
        ).containerId(android.R.id.content)
                .enterAnimation(R.anim.card_slide_right_in)
                .exitAnimation(R.anim.card_slide_left_out)
                .popEnterAnimation(R.anim.card_slide_left_in)
                .popExitAnimation(R.anim.card_slide_right_out)
                .pageListener(this)
                .wizardListener(this)
                .build();
        wizard.init();
    }

    public Wizard getWizard() {
        return wizard;
    }

    public Socket getEnrolmentSocket(){
        return mEnrolmentSocket;
    }

    public BiometricsManager getBiometricsManager(){
        return mBiometricsManager;
    }

    private void initView() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mBiometricsManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onWizardFinished() {
        Toast.makeText(this, "Wizard finished", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageChanged(int currentPageIndex, WizardPage page) {
        mFirstTime = false;
    }

    @Override
    public void onBackPressed() {

    }

    public String getBid() {
        return mBID;
    }

    public String getUserUUID() {
        return mUserUUID;
    }
}
