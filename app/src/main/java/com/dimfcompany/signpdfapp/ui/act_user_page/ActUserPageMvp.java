package com.dimfcompany.signpdfapp.ui.act_user_page;

import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_User;

public interface ActUserPageMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void bindUser(Model_User user, String last_version);
    }

    interface ViewListener
    {
        void clickedDocuments();

        void clickedEdit();

        void clickedToggleApporoved();
    }
}
