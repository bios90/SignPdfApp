package com.dimfcompany.signpdfapp.ui.act_profile_dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.networking.Downloader;
import com.dimfcompany.signpdfapp.networking.helpers.HelperUser;
import com.dimfcompany.signpdfapp.sync.SyncManager;
import com.dimfcompany.signpdfapp.sync.Synchronizer;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import java.util.List;

import javax.inject.Inject;

public class ActProfileDialog extends BaseActivity implements ActProfileDialogMvp.ViewListener, HelperUser.CallbackGetDocsCount, HelperUser.CallbackUserRoleName, SyncManager.CallbackSyncFromServer
{
    private static final String TAG = "ActProfileDialog";

    @Inject
    MessagesManager messagesManager;
    @Inject
    SharedPrefsHelper sharedPrefsHelper;
    @Inject
    HelperUser helperUser;
    @Inject
    GlobalHelper globalHelper;
    @Inject
    Downloader downloader;
    @Inject
    LocalDatabase localDatabase;
    @Inject
    FileManager fileManager;
    @Inject
    Synchronizer synchronizer;

    Model_User user;

    ActProfileDialogMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActProfileDialogMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        user = sharedPrefsHelper.getUserFromSharedPrefs();
        if (user == null)
        {
            return;
        }

        String name = user.getLast_name() + " " + user.getFirst_name();

        mvpView.setUserName(name);
        mvpView.setUserEmail(user.getEmail());

        loadInfo();
    }

    private void loadInfo()
    {
        if (user == null || !globalHelper.isNetworkAvailable())
        {
            return;
        }

        helperUser.getDocsCount(user.getId(), this);
        helperUser.getUserRoleName(user.getId(), this);
    }

    @Override
    public void clickedSync()
    {
        if (!GlobalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        if (user == null)
        {
            return;
        }

        messagesManager.showProgressDialog();
        synchronizer.syncronizeNotSynced(new SyncManager.CallbackSyncronizeNoSynced()
        {
            @Override
            public void onSuccessSync()
            {
                synchronizer.makeSyncFromServer(ActProfileDialog.this);
            }

            @Override
            public void onErrorSync()
            {
                Log.e(TAG, "onErrorSync: Error on pre sync");
                messagesManager.dismissProgressDialog();
                messagesManager.showRedAlerter("Не удалось загрузить документы");
            }
        });
    }

    @Override
    public void clickedExit()
    {
        messagesManager.showSimpleDialog("Выход", "Выйти из аккаунта?", "Выйти", "Отмена", new MessagesManager.DialogButtonsListener()
        {
            @Override
            public void onOkClicked(DialogInterface dialog)
            {
                dialog.dismiss();
                sharedPrefsHelper.clearUserLocalData();
                navigationManager.toActFirst(null);
                finish();
            }

            @Override
            public void onCancelClicked(DialogInterface dialog)
            {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSuccessGetDocsCount(Integer count)
    {
        mvpView.setDogovorCount(count);
    }

    @Override
    public void onSuccessGetUserRole(String roleName)
    {
        mvpView.setRoleName(roleName);
    }

    @Override
    protected void onDestroy()
    {
        mvpView.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void onSuccessSyncFromServer()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showGreenAlerter("База успешно синхронизирована");
        sendBroadcast(new Intent(Constants.BROADCAST_UPDATE_FINISHED_UI));
    }

    @Override
    public void onErrorSyncFromServer()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Не удалось загрузить документы");
    }

    @Override
    public void clickedEditUser()
    {
        navigationManager.toActUserAuthDialog(null, user.getId());
    }
}
