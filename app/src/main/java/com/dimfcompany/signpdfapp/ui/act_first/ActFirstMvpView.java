package com.dimfcompany.signpdfapp.ui.act_first;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.BuildConfig;
import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;

public class ActFirstMvpView extends BaseObservableViewAbstr<ActFirstMvp.ViewListener>
        implements ActFirstMvp.MvpView
{
    private static final String TAG = "ActFirstMvpView";

    EditText et_email, et_password;
    TextView tv_forgot_pass;
    RelativeLayout la_enter;
    TextView tv_register, tv_version;

    public ActFirstMvpView(LayoutInflater layoutInflater, ViewGroup viewGroup)
    {
        setRootView(layoutInflater.inflate(R.layout.act_first, viewGroup, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_forgot_pass = findViewById(R.id.tv_forgot_pass);
        la_enter = findViewById(R.id.la_enter);
        tv_register = findViewById(R.id.tv_register);
        tv_version = findViewById(R.id.tv_version);
    }

    @Override
    public String getEmail()
    {
        return et_email.getText().toString().trim();
    }

    @Override
    public String getPassword()
    {
        return et_password.getText().toString().trim();
    }

    private void setListeners()
    {
        la_enter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedLogin();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedRegister();
            }
        });

        tv_forgot_pass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedForgotPass();
            }
        });
    }

    @Override
    public void bindVersion()
    {
        tv_version.setText("Версия: " + GlobalHelper.APP_VERSION());
    }
}
