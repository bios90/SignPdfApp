package com.dimfcompany.signpdfapp.ui.act_admin;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.base.activity.BaseActivity;
import com.dimfcompany.signpdfapp.base.adapters.AdapterFinished;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.networking.Downloader;
import com.dimfcompany.signpdfapp.networking.WintecApi;
import com.dimfcompany.signpdfapp.networking.helpers.HelperDocuments;
import com.dimfcompany.signpdfapp.utils.DocumentManipulator;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.ImageManager;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ActAdmin extends BaseActivity implements ActAdminMvp.ViewListener, AdapterFinished.CardFinishedCallback
{
    private static final String TAG = "ActAdmin";

    ActAdminMvp.MvpView mvpView;

    @Inject
    GlobalHelper globalHelper;
    @Inject
    MessagesManager messagesManager;
    @Inject
    HelperDocuments helperDocuments;
    @Inject
    Downloader downloader;
    @Inject
    WintecApi wintecApi;

    public static final int OPEN = 7000;
    public static final int SHARE = 7001;
    public static final int PRINT = 7002;

    private Model_Document lastCalledForGeoChange = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getPresenterComponent().inject(this);
        mvpView = viewMvcFactory.getActAdminMvpView(null);
        mvpView.registerListener(this);
        setContentView(mvpView.getRootView());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        loadAllDocuments();
    }

    private void loadAllDocuments()
    {
        if (!globalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        messagesManager.showProgressDialog();
        helperDocuments.getAllAllDocumets(new HelperDocuments.CallbackGetAllAllDocuments()
        {
            @Override
            public void onSuccessGetAllAllDocuments(List<Model_Document> documents)
            {
                messagesManager.dismissProgressDialog();
                mvpView.bindDocuments(documents, ActAdmin.this);
            }

            @Override
            public void onErrorGetAllAllDocuments()
            {
                messagesManager.dismissProgressDialog();
                messagesManager.showRedAlerter("Не удалось загрузить договора");
            }
        });
    }

    @Override
    public void clickedLocal()
    {
        navigationManager.toActMainNew(null);
        finish();
    }

    @Override
    public void clickedProfile()
    {
        navigationManager.toActProfileDialog(null);
    }

    @Override
    protected void onDestroy()
    {
        mvpView.unregisterListener(this);
        super.onDestroy();
    }

    @Override
    public void clickedAdress(Model_Document document)
    {
        lastCalledForGeoChange = document;

        LatLng latLng = null;

        if (document.getLat() != 0 && document.getLon() != 0)
        {
            latLng = new LatLng(document.getLat(), document.getLon());
        }

        navigationManager.toActGeoChoosing(Constants.RQ_GEO_CHOOSING_DIALOG, latLng);
    }

    @Override
    public void clickedCard(final Model_Document document)
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
                        loadAllDocuments();
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

    private void makeEdit(Model_Document doc)
    {
        if (!globalHelper.isNetworkAvailable())
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
                            GlobalHelper.openPdf(ActAdmin.this, file);
                            break;
                        case SHARE:
                            GlobalHelper.shareFile(ActAdmin.this, file);
                            break;
                        case PRINT:
                            file = ImageManager.getImageFileFromPdf(file);
                            Log.e(TAG, "onSuccessDownload: File Name is "+file.getName() );
//                            GlobalHelper.sendToPrint(ActAdmin.this, file);
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

    @Override
    public void clickedPhone(Model_Document document)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case Constants.RQ_GEO_CHOOSING_DIALOG:
                    LatLng latLng = data.getParcelableExtra(Constants.EXTRA_LAT_LNG);
                    updateDocumentLocation(latLng);
                    break;
            }
        }
    }

    private void updateDocumentLocation(LatLng latLng)
    {
        if (!GlobalHelper.isNetworkAvailable())
        {
            messagesManager.showNoInternetAlerter();
            return;
        }

        compositeDisposable.add(
                wintecApi.update_document_location(lastCalledForGeoChange.getId(), latLng.latitude, latLng.longitude)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> messagesManager.showProgressDialog())
                        .doOnTerminate(() -> messagesManager.dismissProgressDialog())
                        .flatMap(s ->
                        {
                            if (s.equals("success"))
                            {
                                return Observer::onComplete;
                            }
                            else
                            {
                                throw new RuntimeException("not success location update update");
                            }
                        })
                        .ignoreElements()
                        .subscribe(() ->
                        {
                            messagesManager.showGreenAlerter("Геолокация документа изменена");
                            loadAllDocuments();
                        }, throwable ->
                        {
                            throwable.printStackTrace();
                            messagesManager.showRedAlerter("Ошибка при изменении геолокации");
                        }));
    }
}
