package com.dimfcompany.signpdfapp.ui.act_profile_dialog;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;

public interface ActProfileDialogMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void setUserName(String userName);
        void setUserEmail(String userEmail);
        void setDogovorCount(int count);
        void setRoleName(String roleName);
    }

    interface ViewListener
    {
        void clickedExit();
        void clickedSync();
    }
}
