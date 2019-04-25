package com.dimfcompany.signpdfapp.base.mvpview;

import android.view.View;

public class BaseObservableViewAbstr<ListenerType> extends BaseMvpViewAbstr implements BaseObservableView<ListenerType>
{

    private ListenerType listener;

    @Override
    public void registerListener(ListenerType listener)
    {
        this.listener = listener;
    }

    @Override
    public void unregisterListener(ListenerType listener)
    {
        if(this.listener == listener)
        {
            this.listener = null;
        }
    }

    @Override
    public ListenerType getListener()
    {
        return listener;
    }
}
