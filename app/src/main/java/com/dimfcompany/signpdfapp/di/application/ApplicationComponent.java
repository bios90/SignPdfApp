package com.dimfcompany.signpdfapp.di.application;

import com.dimfcompany.signpdfapp.di.presenter.PresenterComponent;
import com.dimfcompany.signpdfapp.di.presenter.PresenterModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent
{
    public PresenterComponent newPresenterComponent(PresenterModule presenterModule);
}
