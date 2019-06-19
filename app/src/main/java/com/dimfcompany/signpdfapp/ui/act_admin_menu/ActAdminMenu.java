package com.dimfcompany.signpdfapp.ui.act_admin_menu;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_User;

import javax.inject.Inject;

public class ActAdminMenu extends BaseActivity implements ActAdminMenuMvp.ViewListener
{
    private static final String TAG = "ActAdminMenu";

    ActAdminMenuMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActAdminMenuMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        checkForBlocked();
    }

    @Override
    public void clickedMyDocs()
    {
        navigationManager.toActMainNew(null);
    }

    @Override
    public void clickedAllDocs()
    {
        navigationManager.toActAdmin(null);
    }

    @Override
    public void clickedLocation()
    {

    }

    @Override
    public void clickedAccess()
    {
        navigationManager.toActAccess(null);
    }

    @Override
    public void clickedNewDogovor()
    {
        navigationManager.toSignActivity(null, null);
    }

    @Override
    public void clickedProfile()
    {
        navigationManager.toActProfileDialog(null);
    }

}
