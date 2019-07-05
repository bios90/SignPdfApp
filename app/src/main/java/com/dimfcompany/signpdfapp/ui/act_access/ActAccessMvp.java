package com.dimfcompany.signpdfapp.ui.act_access;

import com.dimfcompany.signpdfapp.base.adapters.AdapterRvUsers;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableView;
import com.dimfcompany.signpdfapp.models.Model_User;

import java.util.List;

public interface ActAccessMvp
{
    interface MvpView extends BaseObservableView<ViewListener>
    {
        void setUsers(List<Model_User> users, String app_last_version, AdapterRvUsers.UsersListener listener);

        void bindSearchText(String search);

        void bindSortText(String sort);
    }

    interface ViewListener
    {
        void clickedSearch();

        void clickedAddUser();
    }
}
