package com.dimfcompany.signpdfapp.base.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dimfcompany.signpdfapp.base.AppClass;
import com.dimfcompany.signpdfapp.base.viewmvcfactory.ViewMvcFactory;
import com.dimfcompany.signpdfapp.di.application.ApplicationComponent;
import com.dimfcompany.signpdfapp.di.presenter.PresenterComponent;
import com.dimfcompany.signpdfapp.di.presenter.PresenterModule;
import com.dimfcompany.signpdfapp.ui.act_sign.ActSign;
import com.dimfcompany.signpdfapp.utils.NavigationManager;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity
{
    private static final String TAG = "BaseActivity";
    private boolean injectorUsed;

    @Inject
    protected ViewMvcFactory viewMvcFactory;
    @Inject
    protected NavigationManager navigationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        makePortrait();
    }

    protected PresenterComponent getPresenterComponent()
    {
        if(injectorUsed)
        {
            throw new RuntimeException("Injector already used, it cant be used twice");
        }
        injectorUsed = true;

        return getApplicationComponent().newPresenterComponent(new PresenterModule(this));
    }

    ApplicationComponent getApplicationComponent()
    {
        return ((AppClass)getApplicationContext()).getApplicationComponent();
    }

    public static void startScreen(AppCompatActivity activity, Class actClass, @Nullable Integer request_code)
    {
        Intent intent = new Intent(activity, actClass);

        if (request_code == null)
        {
            activity.startActivity(intent);
        } else
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
}
