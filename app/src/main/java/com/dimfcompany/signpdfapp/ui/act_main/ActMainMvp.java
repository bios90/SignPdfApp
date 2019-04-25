package com.dimfcompany.signpdfapp.ui.act_main;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;

public interface ActMainMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {

    }

    interface ViewListener
    {
        void clickedMakeNewSign();
        void clickedFinishedSign();
    }
}
