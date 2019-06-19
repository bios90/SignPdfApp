package com.dimfcompany.signpdfapp.ui.act_admin_menu;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;

public interface ActAdminMenuMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {

    }

    interface ViewListener
    {
        void clickedMyDocs();
        void clickedAllDocs();
        void clickedLocation();
        void clickedAccess();
        void clickedNewDogovor();
        void clickedProfile();
    }
}
