package com.dimfcompany.signpdfapp.base.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.AppClass;
import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.viewmvcfactory.ViewMvcFactory;
import com.dimfcompany.signpdfapp.di.application.ApplicationComponent;
import com.dimfcompany.signpdfapp.di.presenter.PresenterComponent;
import com.dimfcompany.signpdfapp.di.presenter.PresenterModule;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSign;
import com.dimfcompany.signpdfapp.utils.NavigationManager;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity
{
    private static final String TAG = "BaseActivity";
    private boolean injectorUsed;

    @Inject
    protected ViewMvcFactory viewMvcFactory;
    @Inject
    protected NavigationManager navigationManager;
    @Inject
    SharedPrefsHelper sharedPrefsHelper;

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    BroadcastReceiver recieverKillApp = getRecieverKillApp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makePortrait();
        registerReceiver(recieverKillApp, new IntentFilter(Constants.BROADCAST_KILL_APPLICATION));
    }

    protected PresenterComponent getPresenterComponent()
    {
        if (injectorUsed)
        {
            throw new RuntimeException("Injector already used, it cant be used twice");
        }
        injectorUsed = true;

        return getApplicationComponent().newPresenterComponent(new PresenterModule(this));
    }

    ApplicationComponent getApplicationComponent()
    {
        return ((AppClass) getApplicationContext()).getApplicationComponent();
    }

    public static void startScreen(AppCompatActivity activity, Class actClass, @Nullable Integer request_code)
    {
        Intent intent = new Intent(activity, actClass);

        if (request_code == null)
        {
            activity.startActivity(intent);
        }
        else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    protected void makeLandscape()
    {
        try
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error on screen rotaion block In Base Activity");
        }
    }


    protected void makePortrait()
    {
        try
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Error on screen rotaion block In Base Activity");
        }
    }

    protected void finishWithResultCancel()
    {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    protected void finishWithResultOk()
    {
        setResult(Activity.RESULT_OK);
        finish();
    }

    protected void finishWithResultOk(Intent return_intent)
    {
        setResult(Activity.RESULT_OK, return_intent);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(recieverKillApp);
        compositeDisposable.clear();
    }

    protected void checkForBlocked()
    {
        Model_User user = sharedPrefsHelper.getUserFromSharedPrefs();
        if (user == null || user.getRole_id() == 999)
        {
            finish();
        }
    }

    private static BroadcastReceiver getRecieverKillApp()
    {
        return new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (intent.getAction().equals(Constants.BROADCAST_KILL_APPLICATION))
                {
                    Log.e(TAG, "onReceive: got broadcast to kill activity");
                    ((BaseActivity) context).finish();
                }
            }
        };
    }
}
