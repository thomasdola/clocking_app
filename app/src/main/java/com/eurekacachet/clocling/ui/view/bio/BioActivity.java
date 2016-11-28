package com.eurekacachet.clocling.ui.view.bio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.credenceid.biometrics.Biometrics;
import com.credenceid.biometrics.BiometricsManager;
import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.bio.pages.FacePage;
import com.eurekacachet.clocling.ui.view.bio.pages.FormPicturePage;
import com.eurekacachet.clocling.ui.view.bio.pages.IndexLeftPage;
import com.eurekacachet.clocling.ui.view.bio.pages.IndexRightPage;
import com.eurekacachet.clocling.ui.view.bio.pages.ThumbLeftPage;
import com.eurekacachet.clocling.ui.view.bio.pages.ThumbRightPage;

import javax.inject.Inject;

import me.panavtec.wizard.Wizard;
import me.panavtec.wizard.WizardListener;
import me.panavtec.wizard.WizardPage;
import me.panavtec.wizard.WizardPageListener;

public class BioActivity extends BaseActivity implements WizardPageListener, WizardListener, BioMvpView {

    @Inject BioPresenter presenter;
    private Wizard wizard;
    BiometricsManager mBiometricsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_bio);
        mBiometricsManager = new BiometricsManager(this);
        initView();
        initWizard();
        presenter.attachView(this);
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
        presenter.detachView();
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
//        Toast.makeText(this, "Page selected: " + currentPageIndex, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

    }
}
