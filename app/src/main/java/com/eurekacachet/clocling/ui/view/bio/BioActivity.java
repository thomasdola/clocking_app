package com.eurekacachet.clocling.ui.view.bio;

import android.os.Bundle;
import android.widget.Toast;

import com.eurekacachet.clocling.R;
import com.eurekacachet.clocling.ui.base.BaseActivity;
import com.eurekacachet.clocling.ui.view.bio.pages.ThumbLeftPage;
import com.eurekacachet.clocling.ui.view.bio.pages.ThumbRightPage;

import javax.inject.Inject;

import me.panavtec.wizard.Wizard;
import me.panavtec.wizard.WizardListener;
import me.panavtec.wizard.WizardPage;
import me.panavtec.wizard.WizardPageListener;

public class BioActivity extends BaseActivity implements BioMvpView, WizardPageListener, WizardListener {

    @Inject BioPresenter presenter;
    private Wizard wizard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_bio);
        initView();
        initWizard();
        presenter.attachView(this);
    }

    private void initWizard() {
        wizard = new Wizard.Builder(
                this,
                new ThumbRightPage(),
                new ThumbLeftPage()
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

    private void initView() {

    }

    @Override
    public void onWizardFinished() {
        Toast.makeText(this, "Wizard finished", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageChanged(int currentPageIndex, WizardPage page) {
        Toast.makeText(this, "Page selected: " + currentPageIndex, Toast.LENGTH_SHORT).show();
    }
}
