package com.dimfcompany.signpdfapp.base.activity;

import android.app.job.JobService;

import com.dimfcompany.signpdfapp.base.AppClass;
import com.dimfcompany.signpdfapp.di.application.ApplicationComponent;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.networking.WintecApi;
import com.dimfcompany.signpdfapp.sync.Synchronizer;
import com.dimfcompany.signpdfapp.utils.FileManager;

import javax.inject.Inject;

public abstract class BaseJobService extends JobService
{
    @Inject
    Synchronizer synchronizer;
    @Inject
    FileManager fileManager;
    @Inject
    WintecApi wintecApi;
    @Inject
    protected LocalDatabase localDatabase;

    @Override
    public void onCreate()
    {
        super.onCreate();
        getApplicationComponent().inject(this);
    }

    private ApplicationComponent getApplicationComponent()
    {
        return ((AppClass)getApplicationContext()).getApplicationComponent();
    }
}
