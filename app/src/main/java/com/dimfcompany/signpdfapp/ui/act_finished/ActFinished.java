package com.dimfcompany.signpdfapp.ui.act_finished;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class ActFinished extends BaseActivity implements ActFinishedMvp.ViewListener, AdapterFinished.CardFinishedCallback
{
    private static final String TAG = "ActFinished";

    @Inject
    LocalDatabase localDatabase;
    @Inject
    FileManager fileManager;
    @Inject
    MessagesManager messagesManager;
    @Inject
    GlobalHelper globalHelper;

    ActFinishedMvp.MvpView mvpView;

    List<Model_Document> documents;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActFinishedMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());

        loadLocalData();
    }

    private void loadLocalData()
    {
        documents = localDatabase.getAllSavedDocuments();
        mvpView.bindDocuments(documents, this);
    }



    @Override
    public void clickedCard(final Model_Document document)
    {
//        messagesManager.showFinishedDocument(new MessagesManager.DialogFinishedListener()
//        {
//            @Override
//            public void clickedOpenDogovor()
//            {
//                File file = fileManager.getFileFromTemp(document.getPdf_file_name(), Constants.FOLDER_CONTRACTS, null);
//                if (!file.exists())
//                {
//                    messagesManager.showRedAlerter("Ошибка", "Файл не найден");
//                    return;
//                }
//
//                try
//                {
//                    GlobalHelper.openPdf(ActFinished.this, file);
//                } catch (Exception e)
//                {
//                    Log.e(TAG, "Error on pf intent " + e.getMessage());
//                    messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для просмотра pdf");
//                }
//            }
//
//            @Override
//            public void clickedOpenCheck()
//            {
//                File file = fileManager.getFileFromTemp(document.getCheck_file_name(), Constants.FOLDER_CHECKS, null);
//                if (!file.exists())
//                {
//                    messagesManager.showRedAlerter("Ошибка", "Файл не найден");
//                    return;
//                }
//
//                try
//                {
//                    GlobalHelper.openPdf(ActFinished.this, file);
//                } catch (Exception e)
//                {
//                    Log.e(TAG, "Error on pf intent " + e.getMessage());
//                    messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для просмотра pdf");
//                }
//            }
//
//            @Override
//            public void clickedSendDogovor()
//            {
//                File file = fileManager.getFileFromTemp(document.getPdf_file_name(), Constants.FOLDER_CONTRACTS, null);
//                if (!file.exists())
//                {
//                    messagesManager.showRedAlerter("Ошибка", "Файл не найден");
//                    return;
//                }
//
//                try
//                {
//                    GlobalHelper.shareFile(ActFinished.this, file);
//                } catch (Exception e)
//                {
//                    Log.e(TAG, "Error on pf intent " + e.getMessage());
//                    messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для отправки файлов");
//                }
//            }
//
//            @Override
//            public void clickedSendCheck()
//            {
//                File file = fileManager.getFileFromTemp(document.getCheck_file_name(), Constants.FOLDER_CHECKS, null);
//                if (!file.exists())
//                {
//                    messagesManager.showRedAlerter("Ошибка", "Файл не найден");
//                    return;
//                }
//
//                try
//                {
//                    GlobalHelper.shareFile(ActFinished.this, file);
//                } catch (Exception e)
//                {
//                    Log.e(TAG, "Error on pf intent " + e.getMessage());
//                    messagesManager.showRedAlerter("Ошибка", "На устройстве ну установлены приложения для отправки файлов");
//                }
//            }
//
//            @Override
//            public void clickedPrintCheck()
//            {
//                File file = fileManager.getFileFromTemp(document.getCheck_file_name(), Constants.FOLDER_CHECKS, null);
//                globalHelper.sendToPrint(file);
//            }
//
//            @Override
//            public void clickedEdit()
//            {
//                GlobalHelper.resetDocumentIds(document);
//                navigationManager.toSignActivity(null,document);
//            }
//
//            @Override
//            public void clickedDelete()
//            {
//                showDeleteDialog(document);
//            }
//        });
    }


    private void showDeleteDialog(final Model_Document document)
    {
        messagesManager.showSimpleDialog("Удалить", "Удалить документ " + document.getCode() + "?", "Удалить", "Отмена", new MessagesManager.DialogButtonsListener()
        {
            @Override
            public void onOkClicked(DialogInterface dialog)
            {
                dialog.dismiss();
                localDatabase.deleteDocumentFull(document);
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
    public void clickedPhone(Model_Document document)
    {
        String phone = document.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        mvpView.unregisterListener(this);
        super.onDestroy();
    }
}
