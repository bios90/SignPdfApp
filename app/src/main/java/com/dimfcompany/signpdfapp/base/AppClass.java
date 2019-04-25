package com.dimfcompany.signpdfapp.base;

import android.app.Application;

import com.dimfcompany.signpdfapp.di.application.ApplicationComponent;
import com.dimfcompany.signpdfapp.di.application.ApplicationModule;
import com.dimfcompany.signpdfapp.di.application.DaggerApplicationComponent;

public class AppClass extends Application
{
    ApplicationComponent applicationComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }


    public ApplicationComponent getApplicationComponent()
    {
        return applicationComponent;
    }
}
