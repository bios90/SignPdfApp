package com.dimfcompany.signpdfapp.networking.fbmessaging;

import android.util.Log;

import com.dimfcompany.signpdfapp.base.AppClass;
import com.dimfcompany.signpdfapp.di.application.ApplicationComponent;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

public class FbMessagingService extends FirebaseMessagingService
{
    private static final String TAG = "FbMessagingService";

    @Inject
    SharedPrefsHelper sharedPrefsHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        getApplicationComponent().inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        //TODO what todo on message recieved
    }

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: Got new FB toker : "+s);
        sharedPrefsHelper.saveUserToken(s);
        sharedPrefsHelper.clearUserLocalData();
    }

    private ApplicationComponent getApplicationComponent()
    {
        return ((AppClass)getApplicationContext()).getApplicationComponent();
    }
}
