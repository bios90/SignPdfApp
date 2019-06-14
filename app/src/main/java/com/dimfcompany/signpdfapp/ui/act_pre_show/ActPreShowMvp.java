package com.dimfcompany.signpdfapp.ui.act_pre_show;

import android.widget.LinearLayout;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;

public interface ActPreShowMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        LinearLayout getLaForItems();
    }

    interface ViewListener
    {
        void clickedBack();
        void clickedPlus();
        void clickedMinus();
    }
}
