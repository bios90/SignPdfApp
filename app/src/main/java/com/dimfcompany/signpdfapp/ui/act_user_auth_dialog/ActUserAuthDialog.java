package com.dimfcompany.signpdfapp.ui.act_user_auth_dialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.networking.helpers.HelperAuth;
import com.dimfcompany.signpdfapp.networking.helpers.HelperUser;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.dimfcompany.signpdfapp.utils.ValidationManager;

import java.util.List;

import javax.inject.Inject;

public class ActUserAuthDialog extends BaseActivity implements ActUserAuthDialogMvp.ViewListener, HelperUser.CallbackGetUserFull
{
    private static final String TAG = "ActUserAuthDialog";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, Integer user_id)
    {
        Intent intent = new Intent(activity, ActUserAuthDialog.class);
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

    ActUserAuthDialogMvp.MvpView mvpView;

    @Inject
    GlobalHelper globalHelper;
    @Inject
    MessagesManager messagesManager;
    @Inject
    HelperUser helperUser;

    boolean edit_mode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActUserAuthMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (getUserId() != 999999)
        {
            if (!globalHelper.isNetworkAvailable())
            {
                messagesManager.showNoInternetAlerter();
                return;
            }

            edit_mode = true;
            messagesManager.showProgressDialog();
            helperUser.getUserFull(getUserId(), this);
        }
    }

    @Override
    public void clickedOk()
    {
        Integer user_id = null;
        if (getUserId() != 999999)
        {
            user_id = getUserId();
        }

        String email = mvpView.getEtEmailText();
        String first_name = mvpView.getEtEFirstNameText();
        String last_name = mvpView.getEtELastNameText();
        String password1 = mvpView.getEtPassword1();
        String password2 = mvpView.getEtPassword2();
        int role_id = mvpView.getRoleId();
        int email_verified = mvpView.getEmailVerified();
        int approved = mvpView.getAdminApproved();

        Log.e(TAG, "clickedOk: Verified " + email_verified);
        Log.e(TAG, "clickedOk: Approved " + approved);

        if (!ValidationManager.validateUserAuthNew(first_name, last_name, email, password1, password2, role_id, edit_mode))
        {
            List<String> errors = ValidationManager.getUserAuthNewErrors(first_name, last_name, email, password1, password2, role_id, edit_mode);
            String message = StringManager.listOfStringToSingle(errors);

            messagesManager.showRedAlerter(message);
            return;
        }

        if (!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }


        messagesManager.showProgressDialog();
        helperUser.insertOrUpdateUser(user_id, first_name, last_name, email, password1, email_verified, approved, role_id, new HelperAuth.CallbackRegister()
        {
            @Override
            public void onSuccessRegister()
            {
                messagesManager.dismissProgressDialog();
                finishWithResultOk();
            }

            @Override
            public void onErrorRegister(String text)
            {
                messagesManager.dismissProgressDialog();
                String message = null;

                if (edit_mode)
                {
                    message = "Не удалось отредактировать данные";
                }
                else
                {
                    message = "Не удалось создать нового пользователя";
                }

                messagesManager.showRedAlerter(message);
            }
        });
    }

    @Override
    public void clickedCancel()
    {
        finishWithResultCancel();
    }

    private int getUserId()
    {
        return getIntent().getIntExtra(Constants.EXTRA_USER_ID, 999999);
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
        mvpView.bindUser(user);
    }

    @Override
    public void onErrorGetUSerFull()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Не удалось загрузить данные пользвателя");
    }
}
