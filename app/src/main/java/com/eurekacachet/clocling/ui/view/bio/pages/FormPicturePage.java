package com.eurekacachet.clocling.ui.view.bio.pages;


import com.eurekacachet.clocling.ui.view.bio.fragments.form.FormPictureFragment;

import me.panavtec.wizard.WizardPage;

public class FormPicturePage extends WizardPage<FormPictureFragment> {

    @Override
    public FormPictureFragment createFragment() {
        return new FormPictureFragment();
    }
}
