package com.dimfcompany.signpdfapp.ui.act_user_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.networking.helpers.HelperUser;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import javax.inject.Inject;

public class ActUserPage extends BaseActivity implements ActUserPageMvp.ViewListener, HelperUser.CallbackGetUserFull
{
    private static final String TAG = "ActUserPage";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, int user_id)
    {
        Intent intent = new Intent(activity, ActUserPage.class);
        intent.putExtra(Constants.EXTRA_USER_ID, user_id);

        if (request_code == null)
        {
            activity.startActivity(intent);
        }
        else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    @Inject
    GlobalHelper globalHelper;
    @Inject
    MessagesManager messagesManager;
    @Inject
    HelperUser helperUser;

    ActUserPageMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActUserPageMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        loadUser();
    }

    private void loadUser()
    {
        if (!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        messagesManager.showProgressDialog();
        helperUser.getUserFull(getUserId(), this);
    }

    @Override
    public void clickedDocuments()
    {
        navigationManager.toActUserDocsDialog(null, getUserId());
    }

    @Override
    public void clickedEdit()
    {
        navigationManager.toActUserAuthDialog(Constants.RQ_EDIT_USER, getUserId());
    }

    @Override
    public void clickedToggleApporoved()
    {
        if (!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        messagesManager.showProgressDialog();
        helperUser.toggleUserApproved(getUserId(), null, new HelperUser.CallbackToggleApproved()
        {
            @Override
            public void onSuccessToggleApproved()
            {
                helperUser.getUserFull(getUserId(), ActUserPage.this);
            }

            @Override
            public void onErrorToggleApproved()
            {
                messagesManager.dismissProgressDialog();
                messagesManager.showRedAlerter("Не удалось изменить одобрение");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == Constants.RQ_EDIT_USER)
            {
                loadUser();
            }
        }
    }

    private int getUserId()
    {
        int id = getIntent().getIntExtra(Constants.EXTRA_USER_ID, 999999);
        if (id == 999999)
        {
            throw new RuntimeException("Error wrong user id");
        }
        return id;
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mvpView.unregisterListener(this);
    }

    @Override
    public void onSuccessGetUserFull(Model_User user, String last_version)
    {
        messagesManager.dismissProgressDialog();
        mvpView.bindUser(user, last_version);
    }

    @Override
    public void onErrorGetUSerFull()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Ошибка при загрузке информации о пользователе");
    }
}
