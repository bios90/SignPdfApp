package com.dimfcompany.signpdfapp.ui.act_register;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;

public interface ActRegisterMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        String getEmail();
        String getFirstName();
        String getLastName();
        String getPassword1();
        String getPassword2();
    }

    interface ViewListener
    {
        void clickerRegister();
    }
}
