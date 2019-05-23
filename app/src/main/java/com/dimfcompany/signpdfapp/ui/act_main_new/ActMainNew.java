package com.dimfcompany.signpdfapp.ui.act_main_new;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.base.adapters.Adapter_Finished;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.networking.helpers.HelperUser;
import com.dimfcompany.signpdfapp.sync.SyncJobService;
import com.dimfcompany.signpdfapp.sync.SyncManager;
import com.dimfcompany.signpdfapp.sync.Synchronizer;
import com.dimfcompany.signpdfapp.sync.UpdateFinishedBroadcastReceiver;
import com.dimfcompany.signpdfapp.ui.act_finished.ActFinished;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class ActMainNew extends BaseActivity implements ActMainNewMvp.ViewListener, Adapter_Finished.CardFinishedCallback, UpdateFinishedBroadcastReceiver.CallbackUiUpdate, SyncManager.CallbackSyncFromServer
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
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        loadLocalData();
        registerReceiver(updateFinishedBroadcastReceiver, new IntentFilter(Constants.BROADCAST_UPDATE_FINISHED_UI));

        if(globalHelper.isNetworkAvailable())
        {
            checkForServerSync();
        }
    }

    private void checkForServerSync()
    {
        int user_id = sharedPrefsHelper.getUserFromSharedPrefs().getId();
        if(documents.size() != 0)
        {
            return;
        }
        helperUser.getDocsCount(user_id, new HelperUser.CallbackGetDocsCount()
        {
            @Override
            public void onSuccessGetDocsCount(Integer count)
            {
                if(count != null && count > 0)
                {
                    messagesManager.showSimpleDialog("Синхронизация", "Найдены договора,сохранненые на сервере. Выполнить синхронизацию сейчас?", "Синхронизировать", "Отмена", new MessagesManager.DialogButtonsListener()
                    {
                        @Override
                        public void onOkClicked(DialogInterface dialog)
                        {
                            dialog.dismiss();
                            if(!globalHelper.isNetworkAvailable())
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
        messagesManager.showFinishedDocument(new MessagesManager.DialogFinishedListener()
        {
            @Override
            public void clickedOpenDogovor()
            {
                File file = fileManager.getFileFromTemp(document.getPdf_file_name(), Constants.FOLDER_CONTRACTS, null);
                if (!file.exists())
                {
                    messagesManager.showRedAlerter("Ошибка", "Файл не найден");
                    return;
                }

                try
                {
                    GlobalHelper.openPdf(ActMainNew.this, file);
                } catch (Exception e)
                {
                    Log.e(TAG, "Error on pf intent " + e.getMessage());
                    messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для просмотра pdf");
                }
            }

            @Override
            public void clickedOpenCheck()
            {
                File file = fileManager.getFileFromTemp(document.getCheck_file_name(), Constants.FOLDER_CHECKS, null);
                if (!file.exists())
                {
                    messagesManager.showRedAlerter("Ошибка", "Файл не найден");
                    return;
                }

                try
                {
                    GlobalHelper.openPdf(ActMainNew.this, file);
                } catch (Exception e)
                {
                    Log.e(TAG, "Error on pf intent " + e.getMessage());
                    messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для просмотра pdf");
                }
            }

            @Override
            public void clickedSendDogovor()
            {
                File file = fileManager.getFileFromTemp(document.getPdf_file_name(), Constants.FOLDER_CONTRACTS, null);
                if (!file.exists())
                {
                    messagesManager.showRedAlerter("Ошибка", "Файл не найден");
                    return;
                }

                try
                {
                    GlobalHelper.shareFile(ActMainNew.this, file);
                } catch (Exception e)
                {
                    Log.e(TAG, "Error on pf intent " + e.getMessage());
                    messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для отправки файлов");
                }
            }

            @Override
            public void clickedSendCheck()
            {
                File file = fileManager.getFileFromTemp(document.getCheck_file_name(), Constants.FOLDER_CHECKS, null);
                if (!file.exists())
                {
                    messagesManager.showRedAlerter("Ошибка", "Файл не найден");
                    return;
                }

                try
                {
                    GlobalHelper.shareFile(ActMainNew.this, file);
                } catch (Exception e)
                {
                    Log.e(TAG, "Error on pf intent " + e.getMessage());
                    messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для отправки файлов");
                }
            }

            @Override
            public void clickedPrintCheck()
            {
                File file = fileManager.getFileFromTemp(document.getCheck_file_name(), Constants.FOLDER_CHECKS, null);
                globalHelper.sendToPrint(file);
            }

            @Override
            public void clickedEdit()
            {
                GlobalHelper.resetDocumentIds(document);
                navigationManager.toSignActivity(null, document);
                finish();
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
    protected void onStop()
    {
        try
        {
            unregisterReceiver(updateFinishedBroadcastReceiver);
        } catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        super.onStop();
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
