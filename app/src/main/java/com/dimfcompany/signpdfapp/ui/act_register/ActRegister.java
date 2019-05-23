package com.dimfcompany.signpdfapp.ui.act_register;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.networking.helpers.HelperAuth;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.dimfcompany.signpdfapp.utils.ValidationManager;

import java.util.List;

import javax.inject.Inject;

public class ActRegister extends BaseActivity implements ActRegisterMvp.ViewListener, HelperAuth.CallbackRegister
{
    private static final String TAG = "ActRegister";

    ActRegisterMvp.MvpView mvpView;

    @Inject
    MessagesManager messagesManager;
    @Inject
    HelperAuth helperAuth;
    @Inject
    SharedPrefsHelper sharedPrefsHelper;
    @Inject
    GlobalHelper globalHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActRegisterMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    public void clickerRegister()
    {
        String email = mvpView.getEmail();
        String first_name = mvpView.getFirstName();
        String last_name = mvpView.getLastName();
        String password1 = mvpView.getPassword1();
        String password2 = mvpView.getPassword2();

        if(!ValidationManager.validateForRegister(first_name,last_name,email,password1,password2))
        {
            List<String> errors = ValidationManager.getRegistrationListOfErrors(first_name,last_name,email,password1,password2);
            String message = StringManager.listOfStringToSingle(errors,"\n");
            messagesManager.showRedAlerter(message);
            return;
        }

        String fb_token = sharedPrefsHelper.getUserToken();
        if(TextUtils.isEmpty(fb_token))
        {
            Log.e(TAG, "clickerRegister: Error no fb_toker" );
            messagesManager.showRedAlerter("Ошибка при регистрации, fb_token");
            return;
        }

        if(!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        messagesManager.showProgressDialog();
        helperAuth.register(first_name,last_name,email,password1,fb_token,this);
    }

    @Override
    protected void onDestroy()
    {
        mvpView.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onSuccessRegister()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showGreenAlerter("","Вы успешно зарегистрированны. Пройдите по ссылке в письме для завершения регистрации");
    }

    @Override
    public void onErrorRegister(String text)
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter(text);
    }
}
