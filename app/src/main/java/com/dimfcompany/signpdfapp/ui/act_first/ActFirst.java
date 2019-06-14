package com.dimfcompany.signpdfapp.ui.act_first;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.networking.WintecApi;
import com.dimfcompany.signpdfapp.networking.helpers.HelperAuth;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.NavigationManager;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.dimfcompany.signpdfapp.utils.ValidationManager;

import java.util.List;

import javax.inject.Inject;

public class ActFirst extends BaseActivity implements ActFirstMvp.ViewListener, HelperAuth.CallbackPassReset, HelperAuth.CallbackLogin
{
    private static final String TAG = "ActFirst";

    ActFirstMvp.MvpView mvpView;

    @Inject
    MessagesManager messagesManager;
    @Inject
    HelperAuth helperAuth;
    @Inject
    GlobalHelper globalHelper;
    @Inject
    SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActFirstMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        checkForLogin();
        mvpView.bindVersion();
    }

    @Override
    public void clickedRegister()
    {
        navigationManager.toRegisterActivity(null);
    }

    @Override
    public void clickedForgotPass()
    {
        messagesManager.showEtDialog("Восстановление пароля", "Введите ваш email, на него будет отправлена инструкция по восстановлению", "Восстановить", "Отмена", new MessagesManager.DialogWithEtListener()
        {
            @Override
            public void onOkDialogWithEt(Dialog dialog, String text)
            {
                dialog.dismiss();
                makePassReset(text);
            }

            @Override
            public void onCancelDialogWithEt(Dialog dialog)
            {
                dialog.dismiss();
            }
        });
    }

    private void makePassReset(String text)
    {
        if(!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        messagesManager.showProgressDialog();
        helperAuth.passReset(text,this);
    }

    @Override
    public void clickedLogin()
    {
        if(!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        String email = mvpView.getEmail();
        String password = mvpView.getPassword();
        String fb_token = sharedPrefsHelper.getUserToken();

        if(!ValidationManager.validateForLogin(email,password,fb_token))
        {
            List<String> errors = ValidationManager.getLoginListOfErrors(email,password,fb_token);
            String message = StringManager.listOfStringToSingle(errors,"\n");
            messagesManager.showRedAlerter(message);
            return;
        }


        messagesManager.showProgressDialog();
        helperAuth.login(email,password,fb_token,this);
    }

    @Override
    protected void onDestroy()
    {
        mvpView.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onSuccessPassReset()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showGreenAlerter("Успешно","На ваш email отправлена инструкция по восстановлению");
    }

    @Override
    public void onErrorPassReset()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Не удалось восстановить, проверьте введенные данные");
    }

    @Override
    public void onSuccessLogin(Model_User user)
    {
        messagesManager.dismissProgressDialog();
        sharedPrefsHelper.saveUserToShared(user);
        toMainScreen();
    }

    @Override
    public void onErrorLogin()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Ошибка входа, проверьте введенные данные");
    }

    private void toMainScreen()
    {
        navigationManager.toActMainNew(null);
        finish();
    }

    private void checkForLogin()
    {
        Model_User user = sharedPrefsHelper.getUserFromSharedPrefs();
        if(user != null)
        {
            toMainScreen();
        }
    }
}
