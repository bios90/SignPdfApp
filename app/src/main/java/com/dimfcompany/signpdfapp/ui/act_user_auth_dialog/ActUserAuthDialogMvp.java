package com.dimfcompany.signpdfapp.ui.act_user_auth_dialog;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_User;

public interface ActUserAuthDialogMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void bindUser(Model_User user);
        String getEtEmailText();
        String getEtEFirstNameText();
        String getEtELastNameText();
        String getEtPassword1();
        String getEtPassword2();
        int getRoleId();
        int getEmailVerified();
        int getAdminApproved();
    }

    interface ViewListener
    {
        void clickedOk();
        void clickedCancel();
    }
}
