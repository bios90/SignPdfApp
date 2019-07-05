package com.dimfcompany.signpdfapp.ui.act_profile_dialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;

public class ActProfileDialogMvpView extends BaseObservableViewAbstr<ActProfileDialogMvp.ViewListener>
        implements ActProfileDialogMvp.MvpView
{
    private static final String TAG = "ActProfileDialogMvpView";

    TextView tv_name, tv_email, tv_role, tv_all_docs_count;
    RelativeLayout la_sync, la_exit, la_edit_user;

    public ActProfileDialogMvpView(LayoutInflater layoutInflater, ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_profile_dialog, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_role = findViewById(R.id.tv_role);
        tv_all_docs_count = findViewById(R.id.tv_all_docs_count);
        la_sync = findViewById(R.id.la_sync);
        la_exit = findViewById(R.id.la_exit);
        la_edit_user = findViewById(R.id.la_edit_user);
    }

    private void setListeners()
    {
        la_exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedExit();
            }
        });

        la_sync.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedSync();
            }
        });

        la_edit_user.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getListener().clickedEditUser();
            }
        });
    }

    @Override
    public void setUserName(String userName)
    {
        tv_name.setText(userName);
    }

    @Override
    public void setUserEmail(String userEmail)
    {
        tv_email.setText(userEmail);
    }

    @Override
    public void setDogovorCount(int count)
    {
        tv_all_docs_count.setText(String.valueOf(count));
    }

    @Override
    public void setRoleName(String roleName)
    {
        tv_role.setText(roleName);
    }
}
