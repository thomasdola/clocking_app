package com.eurekacachet.clocling.ui.view.bio.pages;


import com.eurekacachet.clocling.ui.view.bio.fragments.ThumbLeftFragment;

import me.panavtec.wizard.WizardPage;

public class ThumbLeftPage extends WizardPage<ThumbLeftFragment> {

    @Override
    public ThumbLeftFragment createFragment() {
        return new ThumbLeftFragment();
    }
}
