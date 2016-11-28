package com.eurekacachet.clocling.injection.component;


import com.eurekacachet.clocling.injection.module.ActivityModule;
import com.eurekacachet.clocling.injection.scope.PerActivity;
import com.eurekacachet.clocling.ui.view.bio.BioActivity;
import com.eurekacachet.clocling.ui.view.bio.fragments.form.FormPictureFragment;
import com.eurekacachet.clocling.ui.view.bio.fragments.index_left.IndexLeftFragment;
import com.eurekacachet.clocling.ui.view.bio.fragments.index_right.IndexRightFragment;
import com.eurekacachet.clocling.ui.view.bio.fragments.portrait.FaceFragment;
import com.eurekacachet.clocling.ui.view.bio.fragments.thumb_left.ThumbLeftFragment;
import com.eurekacachet.clocling.ui.view.bio.fragments.thumb_right.ThumbRightFragment;
import com.eurekacachet.clocling.ui.view.clocking.ClockingActivity;
import com.eurekacachet.clocling.ui.view.login.LoginActivity;
import com.eurekacachet.clocling.ui.view.login.modal.LoginFragment;
import com.eurekacachet.clocling.ui.view.main.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoginActivity loginActivity);

    void inject(LoginFragment loginFragment);

    void inject(MainActivity mainActivity);

    void inject(BioActivity bioActivity);

    void inject(FaceFragment faceFragment);

    void inject(ThumbRightFragment thumbRightFragment);

    void inject(ThumbLeftFragment thumbLeftFragment);

    void inject(IndexRightFragment indexRightFragment);

    void inject(IndexLeftFragment indexLeftFragment);

    void inject(FormPictureFragment formPictureFragment);

    void inject(ClockingActivity clockingActivity);
}
