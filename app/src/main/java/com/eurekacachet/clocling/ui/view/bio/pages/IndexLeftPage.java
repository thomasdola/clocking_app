package com.eurekacachet.clocling.ui.view.bio.pages;

import com.eurekacachet.clocling.ui.view.bio.fragments.IndexLeftFragment;

import me.panavtec.wizard.WizardPage;


public class IndexLeftPage extends WizardPage<IndexLeftFragment> {
    @Override
    public IndexLeftFragment createFragment() {
        return new IndexLeftFragment();
    }
}
