package com.eurekacachet.clocling.injection.component;


import com.eurekacachet.clocling.injection.module.ActivityModule;
import com.eurekacachet.clocling.injection.scope.PerActivity;
import com.eurekacachet.clocling.ui.view.login.LoginActivity;
import com.eurekacachet.clocling.ui.view.login.modal.LoginFragment;
import com.eurekacachet.clocling.ui.view.main.MainActivity;

import dagger.Component;
import dagger.Subcomponent;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoginActivity loginActivity);

    void inject(LoginFragment loginFragment);

    void inject(MainActivity mainActivity);
}
