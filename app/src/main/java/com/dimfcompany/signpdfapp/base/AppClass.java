package com.dimfcompany.signpdfapp.base;

import android.app.Application;

import com.dimfcompany.signpdfapp.di.application.ApplicationComponent;
import com.dimfcompany.signpdfapp.di.application.ApplicationModule;
import com.dimfcompany.signpdfapp.di.application.DaggerApplicationComponent;

public class AppClass extends Application
{
    ApplicationComponent applicationComponent;
    private static AppClass app;

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static AppClass getApp()
    {
        return app;
    }

    public ApplicationComponent getApplicationComponent()
    {
        return applicationComponent;
    }

}
