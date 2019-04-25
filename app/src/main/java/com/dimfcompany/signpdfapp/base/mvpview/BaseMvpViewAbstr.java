package com.dimfcompany.signpdfapp.base.mvpview;

import android.view.View;

public class BaseMvpViewAbstr implements BaseMvpView
{
    private View rootView;

    protected void setRootView(View view)
    {
        this.rootView = view;
    }

    protected <T extends View>T findViewById(int id)
    {
        return getRootView().findViewById(id);
    }

    @Override
    public View getRootView()
    {
        return rootView;
    }
}
