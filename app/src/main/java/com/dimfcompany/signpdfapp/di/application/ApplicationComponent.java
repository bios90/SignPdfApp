package com.dimfcompany.signpdfapp.di.application;

import com.dimfcompany.signpdfapp.base.activity.BaseJobService;
import com.dimfcompany.signpdfapp.di.presenter.PresenterComponent;
import com.dimfcompany.signpdfapp.di.presenter.PresenterModule;
import com.dimfcompany.signpdfapp.networking.fbmessaging.FbMessagingService;
import com.dimfcompany.signpdfapp.sync.SyncJobService;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent
{
    public PresenterComponent newPresenterComponent(PresenterModule presenterModule);
    void inject(FbMessagingService fbMessagingService);
    void inject(BaseJobService baseJobService);
    void inject(SyncJobService syncJobService);
}
