package com.dimfcompany.signpdfapp.ui.act_register;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;

public class ActRegisterMvpView extends BaseObservableViewAbstr<ActRegisterMvp.ViewListener>
    implements ActRegisterMvp.MvpView
{
    private static final String TAG = "ActRegisterMvpView";

    EditText et_email,et_first_name,et_last_name,et_password1,et_password2;
    RelativeLayout la_register;

    public ActRegisterMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_register,parent,false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        et_email = findViewById(R.id.et_email);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        la_register = findViewById(R.id.la_register);
    }

    private void setListeners()
    {
        la_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickerRegister();
            }
        });
    }

    @Override
    public String getEmail()
    {
        return et_email.getText().toString().trim();
    }

    @Override
    public String getFirstName()
    {
        return et_first_name.getText().toString().trim();
    }

    @Override
    public String getLastName()
    {
        return et_last_name.getText().toString().trim();
    }

    @Override
    public String getPassword1()
    {
        return et_password1.getText().toString().trim();
    }

    @Override
    public String getPassword2()
    {
        return et_password2.getText().toString().trim();
    }
}
