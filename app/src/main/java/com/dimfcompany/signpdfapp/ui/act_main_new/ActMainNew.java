package com.dimfcompany.signpdfapp.ui.act_main_new;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.networking.Downloader;
import com.dimfcompany.signpdfapp.networking.helpers.HelperUser;
import com.dimfcompany.signpdfapp.sync.SyncManager;
import com.dimfcompany.signpdfapp.sync.Synchronizer;
import com.dimfcompany.signpdfapp.sync.UpdateFinishedBroadcastReceiver;
import com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin;
import com.dimfcompany.signpdfapp.ui.act_main.ActMain;
import com.dimfcompany.signpdfapp.utils.DocumentManipulator;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import static com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin.OPEN;
import static com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin.PRINT;
import static com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin.SHARE;

public class ActMainNew extends BaseActivity implements ActMainNewMvp.ViewListener, AdapterFinished.CardFinishedCallback, UpdateFinishedBroadcastReceiver.CallbackUiUpdate, SyncManager.CallbackSyncFromServer
{
    private static final String TAG = "ActMainNew";

    ActMainNewMvp.MvpView mvpView;

    @Inject
    LocalDatabase localDatabase;
    @Inject
    MessagesManager messagesManager;
    @Inject
    FileManager fileManager;
    @Inject
    GlobalHelper globalHelper;
    @Inject
    HelperUser helperUser;
    @Inject
    SharedPrefsHelper sharedPrefsHelper;
    @Inject
    Synchronizer synchronizer;

    List<Model_Document> documents;
    UpdateFinishedBroadcastReceiver updateFinishedBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActMainNewMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
        updateFinishedBroadcastReceiver = new UpdateFinishedBroadcastReceiver(this);
        checkForAdmin();
        checkForBlocked();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        loadLocalData();
        registerReceiver(updateFinishedBroadcastReceiver, new IntentFilter(Constants.BROADCAST_UPDATE_FINISHED_UI));

        if (globalHelper.isNetworkAvailable())
        {
            checkForServerSync();
        }
    }

    private void checkForServerSync()
    {
        int user_id = sharedPrefsHelper.getUserFromSharedPrefs().getId();

        helperUser.getDocsCount(user_id, new HelperUser.CallbackGetDocsCount()
        {
            @Override
            public void onSuccessGetDocsCount(Integer count)
            {
                if (count != null && count > documents.size())
                {
                    messagesManager.showSimpleDialog("Синхронизация", "Найдены договора,сохранненые на сервере. Выполнить синхронизацию сейчас?", "Синхронизировать", "Отмена", new MessagesManager.DialogButtonsListener()
                    {
                        @Override
                        public void onOkClicked(DialogInterface dialog)
                        {
                            dialog.dismiss();
                            if (!globalHelper.isNetworkAvailable())
                            {
                                messagesManager.showNoInternetAlerter();
                                return;
                            }

                            messagesManager.showProgressDialog();
                            synchronizer.makeSyncFromServer(ActMainNew.this);
                        }

                        @Override
                        public void onCancelClicked(DialogInterface dialog)
                        {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void clickedNewDocument()
    {
        navigationManager.toSignActivity(null, null);
    }

    @Override
    public void clickedProfile()
    {
        navigationManager.toActProfileDialog(null);
    }

    private void loadLocalData()
    {
        documents = localDatabase.getAllSavedDocuments();
        mvpView.bindDocuments(documents, this);
    }

    @Override
    public void clickedCard(final Model_Document document)
    {
        boolean hasVaucher = document.getVaucher() != null;

        messagesManager.showFinishedDocument(hasVaucher, new MessagesManager.DialogFinishedListener()
        {
            @Override
            public void clickedOpenDogovor()
            {
                manipulateDocument(document, DocumentManipulator.DocumentFileType.TYPE_DOCUMENT, OPEN);
            }

            @Override
            public void clickedOpenCheck()
            {
                manipulateDocument(document, DocumentManipulator.DocumentFileType.TYPE_CHECK, OPEN);
            }

            @Override
            public void clickedOpenVaucher()
            {
                manipulateDocument(document, DocumentManipulator.DocumentFileType.TYPE_VAUCHER, OPEN);
            }

            @Override
            public void clickedSendDogovor()
            {
                manipulateDocument(document, DocumentManipulator.DocumentFileType.TYPE_DOCUMENT, SHARE);
            }

            @Override
            public void clickedSendCheck()
            {
                manipulateDocument(document, DocumentManipulator.DocumentFileType.TYPE_CHECK, SHARE);
            }

            @Override
            public void clickedSendVaucher()
            {
                manipulateDocument(document, DocumentManipulator.DocumentFileType.TYPE_VAUCHER, SHARE);
            }

            @Override
            public void clickedPrintCheck()
            {
                manipulateDocument(document, DocumentManipulator.DocumentFileType.TYPE_CHECK, PRINT);
            }

            @Override
            public void clickedPrintVaucher()
            {
                manipulateDocument(document, DocumentManipulator.DocumentFileType.TYPE_VAUCHER, PRINT);
            }

            @Override
            public void clickedEdit(Dialog dialog)
            {
                GlobalHelper.resetDocumentIds(document);
                navigationManager.toSignActivity(null, document);
            }

            @Override
            public void clickedDelete()
            {
                showDeleteDialog(document);
            }
        });
    }

    @Override
    public void clickedPhone(Model_Document document)
    {

    }


    @Override
    public void updateFinishedCardUi()
    {
        loadLocalData();
    }

    private void showDeleteDialog(final Model_Document document)
    {
        messagesManager.showSimpleDialog("Удалить", "Удалить документ " + document.getCode() + "?", "Удалить", "Отмена", new MessagesManager.DialogButtonsListener()
        {
            @Override
            public void onOkClicked(DialogInterface dialog)
            {
                dialog.dismiss();
                localDatabase.deleteDocumentSoft(document);
                loadLocalData();
            }

            @Override
            public void onCancelClicked(DialogInterface dialog)
            {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void clickedAdmin()
    {
        navigationManager.toActAdmin(null);
        finish();
    }

    private void checkForAdmin()
    {
//        Model_User user = sharedPrefsHelper.getUserFromSharedPrefs();
//        if (user != null && user.getRole_id() == 7)
//        {
//            navigationManager.toActAdminMenu(null);
//            finish();
//        }
    }


    @Override
    protected void onStop()
    {
        try
        {
            unregisterReceiver(updateFinishedBroadcastReceiver);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        super.onStop();
    }

    private void manipulateDocument(Model_Document document, DocumentManipulator.DocumentFileType type, final int action)
    {
        File file = fileManager.getDocumentFile(document, type);

        if (file == null || !file.exists())
        {
            messagesManager.showRedAlerter("Файл не найлен");
            return;
        }

        switch (action)
        {
            case OPEN:
                GlobalHelper.openPdf(ActMainNew.this, file);
                break;
            case SHARE:
                GlobalHelper.shareFile(ActMainNew.this, file);
                break;
            case PRINT:
                GlobalHelper.sendToPrint(ActMainNew.this,file);
                break;
        }
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
        updateFinishedCardUi();
    }

    @Override
    public void onErrorSyncFromServer()
    {
        messagesManager.dismissProgressDialog();
        messagesManager.showRedAlerter("Не удалось загрузить документы");
    }
}
