package com.dimfcompany.signpdfapp.sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import com.dimfcompany.signpdfapp.base.AppClass;
import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseJobService;
import com.dimfcompany.signpdfapp.di.application.ApplicationComponent;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.networking.WintecApi;
import com.dimfcompany.signpdfapp.utils.FileManager;

import java.util.List;

import javax.inject.Inject;

public class SyncJobService extends JobService
{
    private static final String TAG = "SyncJobService";


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



    @Override
    public boolean onStartJob(final JobParameters params)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if(localDatabase.hasNotSynced())
                {
                    synchronizer.syncronizeNotSynced(new SyncManager.CallbackSyncronizeNoSynced()
                    {
                        @Override
                        public void onSuccessSync()
                        {
                            Log.e(TAG, "onSuccessSync: Job finished successfully!" );
                            jobFinished(params, false);
                            sendBroadcast(new Intent(Constants.BROADCAST_UPDATE_FINISHED_UI));
                        }

                        @Override
                        public void onErrorSync()
                        {
                            jobFinished(params, true);
                        }
                    });
                }
            }
        }).start();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params)
    {
        return false;
    }

}
