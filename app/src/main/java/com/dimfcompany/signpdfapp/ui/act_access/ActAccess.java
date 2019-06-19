package com.dimfcompany.signpdfapp.ui.act_access;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.base.adapters.AdapterRvUsers;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.networking.helpers.HelperUser;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import java.util.List;

import javax.inject.Inject;

public class ActAccess extends BaseActivity implements ActAccessMvp.ViewListener, HelperUser.CallbackGetUsers, AdapterRvUsers.UsersListener
{
    private static final String TAG = "ActAccess";

    ActAccessMvp.MvpView mvpView;

    @Inject
    GlobalHelper globalHelper;
    @Inject
    MessagesManager messagesManager;
    @Inject
    HelperUser helperUser;


    private String search;
    private int sort = 999999;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActAccessMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        makeSearch(null, null);
    }

    @Override
    public void clickedSearch()
    {
        navigationManager.toActSearchDialog(Constants.RQ_SEARCH_DIALOG, search, sort);
    }

    private void makeSearch(@Nullable String search, @Nullable String sort)
    {
        if (!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        helperUser.getUsers(search, sort, this);
    }

    @Override
    public void onSuccessGetUsers(List<Model_User> users)
    {
        messagesManager.dismissProgressDialog();
        mvpView.setUsers(users, this);
        bindSearchSortText();
    }

    private void bindSearchSortText()
    {
        String searchStr = " - ";
        if (search != null)
        {
            searchStr = search;
        }

        String sortStr = GlobalHelper.getSortStringFromIntRus(sort);
        if (sortStr == null)
        {
            sortStr = " - ";
        }

        mvpView.bindSearchText(searchStr);
        mvpView.bindSortText(sortStr);
    }

    @Override
    public void onErrorGetUsers()
    {
        messagesManager.showRedAlerter("Ошибка при загрузке пользоввателей");
        messagesManager.dismissProgressDialog();
    }

    @Override
    public void clickedCard(Model_User user)
    {
        navigationManager.toActUserDocsDialog(null,user.getId());
    }

    @Override
    public void clickedRole(Model_User user)
    {
        navigationManager.toActAccessDialog(Constants.RQ_ACCESS_DIALOG, user);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case (Constants.RQ_SEARCH_DIALOG):
                    search = data.getStringExtra(Constants.EXTRA_SEARCH);
                    sort = data.getIntExtra(Constants.EXTRA_SORT, 999999);
                    makeSearch(search, GlobalHelper.getSortStringFromInt(sort));
                    break;

                case (Constants.RQ_ACCESS_DIALOG):
                    int user_id = ((Model_User) data.getSerializableExtra(Constants.EXTRA_USER)).getId();
                    int role_id = data.getIntExtra(Constants.EXTRA_ROLE_ID, 999999);
                    changeRole(user_id, role_id);
                    break;
            }
        }
    }

    private void changeRole(int user_id, int role_id)
    {
        if (!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        messagesManager.showProgressDialog();
        helperUser.changeRole(user_id, role_id, new HelperUser.CallbackChangeRole()
        {
            @Override
            public void onSuccessChangeRole()
            {
                messagesManager.dismissProgressDialog();
                messagesManager.showGreenAlerter("Доступ пользователя изменен");
                makeSearch(search, GlobalHelper.getSortStringFromInt(sort));
            }

            @Override
            public void onErrorChangeRole()
            {
                messagesManager.dismissProgressDialog();
                messagesManager.showRedAlerter("Не удалось изменить доступ");
                makeSearch(search, GlobalHelper.getSortStringFromInt(sort));
            }
        });
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mvpView.unregisterListener(this);
    }
}
