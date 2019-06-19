package com.dimfcompany.signpdfapp.ui.act_access_dialog;

import com.dimfcompany.signpdfapp.base.adapters.AdapterRvUsers;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.ui.act_access.ActAccessMvp;

import java.util.List;

public interface ActAccessDialogMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void bindUser(Model_User user);
        int getSelectedRoleId();
    }

    interface ViewListener
    {
        void clickedOk();
        void clickedCancel();
    }
}
