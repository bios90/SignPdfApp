package com.dimfcompany.signpdfapp.base.mvpview;

public interface BaseObservableView<ListenerType> extends BaseMvpView
{
    void registerListener(ListenerType listener);
    void unregisterListener(ListenerType listener);

    public ListenerType getListener();
}
