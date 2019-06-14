package com.dimfcompany.signpdfapp.ui.act_first;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;

public interface ActFirstMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        String getEmail();
        String getPassword();
        void bindVersion();
    }

    interface ViewListener
    {
        void clickedRegister();
        void clickedForgotPass();
        void clickedLogin();
    }
}
