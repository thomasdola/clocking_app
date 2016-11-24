package com.eurekacachet.clocling.ui.view.bio.pages;

import com.eurekacachet.clocling.ui.view.bio.fragments.FaceFragment;

import me.panavtec.wizard.WizardPage;


public class FacePage extends WizardPage<FaceFragment> {
    @Override
    public FaceFragment createFragment() {
        return new FaceFragment();
    }
}
