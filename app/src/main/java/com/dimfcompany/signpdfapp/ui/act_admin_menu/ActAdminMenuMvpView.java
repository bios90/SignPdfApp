package com.dimfcompany.signpdfapp.ui.act_admin_menu;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.mvpview.BaseObservableViewAbstr;
import com.dimfcompany.signpdfapp.models.Model_User;

public class ActAdminMenuMvpView extends BaseObservableViewAbstr<ActAdminMenuMvp.ViewListener>
        implements ActAdminMenuMvp.MvpView
{
    private static final String TAG = "ActAdminMenuMvpView";

    RelativeLayout la_my_docs, la_all_docs, la_location, la_access, la_new_dogovor;
    TextView tv_profile;

    public ActAdminMenuMvpView(LayoutInflater layoutInflater, @Nullable ViewGroup parent)
    {
        setRootView(layoutInflater.inflate(R.layout.act_admin_menu, parent, false));
        initViews();
        setListeners();
    }

    private void initViews()
    {
        la_my_docs = findViewById(R.id.la_my_docs);
        la_all_docs = findViewById(R.id.la_all_docs);
        la_location = findViewById(R.id.la_location);
        la_access = findViewById(R.id.la_access);
        la_new_dogovor = findViewById(R.id.la_new_dogovor);
        tv_profile = findViewById(R.id.tv_profile);
    }

    private void setListeners()
    {
        la_my_docs.setOnClickListener((view)->
        {
            getListener().clickedMyDocs();
        });

        la_all_docs.setOnClickListener((view)->
        {
            getListener().clickedAllDocs();
        });

        la_location.setOnClickListener((view)->
        {
            getListener().clickedLocation();
        });

        la_access.setOnClickListener((view)->
        {
            getListener().clickedAccess();
        });

        la_new_dogovor.setOnClickListener((view)->
        {
            getListener().clickedNewDogovor();
        });

        tv_profile.setOnClickListener((view)->
        {
            getListener().clickedProfile();
        });
    }
}
