package com.dimfcompany.signpdfapp.ui.act_admin_menu;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_User;

import javax.inject.Inject;

public class ActAdminMenu extends BaseActivity implements ActAdminMenuMvp.ViewListener
{
    private static final String TAG = "ActAdminMenu";

    ActAdminMenuMvp.MvpView mvpView;

    @Inject
    SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActAdminMenuMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        checkForBlocked();
        checkForNotAdmin();
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
        navigationManager.toActGeo(null);
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

    private void checkForNotAdmin()
    {
        Model_User user = sharedPrefsHelper.getUserFromSharedPrefs();

        Log.e(TAG, "checkForNotAdmin: user role id is "+user.getRole_id() );
        if (user == null || user.getRole_id() != 7)
        {
            finish();
        }
    }

}
