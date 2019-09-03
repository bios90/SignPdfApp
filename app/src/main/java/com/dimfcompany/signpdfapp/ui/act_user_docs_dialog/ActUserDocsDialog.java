package com.dimfcompany.signpdfapp.ui.act_user_docs_dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.networking.Downloader;
import com.dimfcompany.signpdfapp.networking.helpers.HelperDocuments;
import com.dimfcompany.signpdfapp.networking.helpers.HelperUser;
import com.dimfcompany.signpdfapp.ui.act_access_dialog.ActAccessDialog;
import com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin;
import com.dimfcompany.signpdfapp.utils.DocumentManipulator;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.ImageManager;
import com.dimfcompany.signpdfapp.utils.MessagesManager;

import java.io.File;

import javax.inject.Inject;

import static com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin.OPEN;
import static com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin.PRINT;
import static com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin.SHARE;

public class ActUserDocsDialog extends BaseActivity implements ActUserDocsDialogMvp.ViewListener, AdapterFinished.CardFinishedCallback
{
    private static final String TAG = "ActUserDocsDialog";

    public static void startScreenOver(AppCompatActivity activity, @Nullable Integer request_code, int user_id)
    {
        Intent intent = new Intent(activity, ActUserDocsDialog.class);
        intent.putExtra(Constants.EXTRA_USER_ID, user_id);

        if (request_code == null)
        {
            activity.startActivity(intent);
        }
        else
        {
            activity.startActivityForResult(intent, request_code);
        }
    }

    @Inject
    GlobalHelper globalHelper;
    @Inject
    MessagesManager messagesManager;
    @Inject
    HelperUser helperUser;
    @Inject
    Downloader downloader;
    @Inject
    HelperDocuments helperDocuments;

    ActUserDocsDialogMvp.MvpView mvpView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActUserDialogMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        loadUser();
    }

    private void loadUser()
    {
        if (!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        messagesManager.showProgressDialog();
        helperUser.getUserFull(getUserId(), new HelperUser.CallbackGetUserFull()
        {
            @Override
            public void onSuccessGetUserFull(Model_User user, String alst_version)
            {
                messagesManager.dismissProgressDialog();
                mvpView.bindUser(user, ActUserDocsDialog.this);
            }

            @Override
            public void onErrorGetUSerFull()
            {
                messagesManager.dismissProgressDialog();
                messagesManager.showRedAlerter("Ошибка при загрузке документов");
            }
        });
    }

    private int getUserId()
    {
        int id = getIntent().getIntExtra(Constants.EXTRA_USER_ID, 999999);
        if (id == 999999)
        {
            throw new RuntimeException("Error wrong user id");
        }
        return id;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mvpView.unregisterListener(this);
    }


    @Override
    public void clickedCard(Model_Document document)
    {
        boolean hasVaucher = document.getVaucher_file_name() != null;

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
                manipulateDocument(document, DocumentManipulator.DocumentFileType.TYPE_CHECK, SHARE);
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
                dialog.dismiss();
                makeEdit(document);
            }

            @Override
            public void clickedDelete()
            {
                makeDeleteOnServer(document);
            }

            @Override
            public void clickedAddress()
            {

            }
        });
    }

    private void manipulateDocument(Model_Document document, DocumentManipulator.DocumentFileType type, final int action)
    {
        if (!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        messagesManager.showProgressDialog();
        downloader.downloadDocumentFileAsync(document, type, new Downloader.CallbackDownloadFile()
        {
            @Override
            public void onSuccessDownload(File file)
            {
                messagesManager.dismissProgressDialog();

                try
                {
                    switch (action)
                    {
                        case OPEN:
                            GlobalHelper.openPdf(ActUserDocsDialog.this, file);
                            break;
                        case SHARE:
                            GlobalHelper.shareFile(ActUserDocsDialog.this, file);
                            break;
                        case PRINT:
                            GlobalHelper.sendToPrint(ActUserDocsDialog.this, file);
                            break;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorDownload()
            {
                messagesManager.dismissProgressDialog();
                messagesManager.showRedAlerter("Ошибка при загрузке файла");
            }
        });
    }


    private void makeEdit(Model_Document doc)
    {
        if (!GlobalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
        }

        messagesManager.showProgressDialog();
        helperDocuments.getDocumentWithFullInfo(doc.getId(), new HelperDocuments.CallbackGetFullDocument()
        {
            @Override
            public void onSuccessGetFullDocument(Model_Document document)
            {
                messagesManager.dismissProgressDialog();
                GlobalHelper.resetDocumentIds(document);
                navigationManager.toSignActivity(null, document);
            }

            @Override
            public void onErrorGetFullDocument()
            {
                messagesManager.dismissProgressDialog();
                messagesManager.showRedAlerter("Не удалось загрузить документ");
            }
        });


    }

    private void makeDeleteOnServer(final Model_Document document)
    {
        messagesManager.showSimpleDialog("Удаление", "Удалить с сервера документ " + document.getCode() + "?", "Удалить", "Отмена", new MessagesManager.DialogButtonsListener()
        {
            @Override
            public void onOkClicked(DialogInterface dialog)
            {
                dialog.dismiss();
                if (!globalHelper.isNetworkAvailable())
                {
                    messagesManager.showNoInternetAlerter();
                }

                messagesManager.showProgressDialog();

                helperDocuments.deleteDocumentOnServer(document.getId(), new HelperDocuments.CallbackDeleteDocumentOnServer()
                {
                    @Override
                    public void onSuccessDeleteOnServer()
                    {
                        messagesManager.dismissProgressDialog();
                        loadUser();
                    }

                    @Override
                    public void onErrorDeleteOnServer()
                    {
                        messagesManager.dismissProgressDialog();
                        messagesManager.showRedAlerter("Не удалось удалить документ");
                    }
                });
            }

            @Override
            public void onCancelClicked(DialogInterface dialog)
            {

            }
        });
    }

    @Override
    public void clickedAdress(Model_Document document)
    {

    }

    @Override
    public void clickedPhone(Model_Document document)
    {

    }
}
