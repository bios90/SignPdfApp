package com.dimfcompany.signpdfapp.ui.act_main;

import android.os.Bundle;


import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.activity.BaseActivity;

public class ActMain extends BaseActivity implements ActMainMvp.ViewListener
{
    private static final String TAG = "ActMain";

    ActMainMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActMainMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    public void clickedMakeNewSign()
    {
        navigationManager.toSignActivity(null,null);
    }

    @Override
    public void clickedFinishedSign()
    {
        navigationManager.toActFinished(null);
    }
}
