package com.eurekacachet.clocling.ui.view.bio.pages;

import com.eurekacachet.clocling.ui.view.bio.fragments.index_right.IndexRightFragment;

import me.panavtec.wizard.WizardPage;


public class IndexRightPage extends WizardPage<IndexRightFragment> {
    @Override
    public IndexRightFragment createFragment() {
        return new IndexRightFragment();
    }
}
