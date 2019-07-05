package com.dimfcompany.signpdfapp.utils;

import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.networking.Downloader;
import com.dimfcompany.signpdfapp.networking.helpers.HelperDocuments;

import java.io.File;

public class DocumentManipulator
{
    public enum DocumentFileType
    {
        TYPE_SIGNATURE,
        TYPE_CHECK,
        TYPE_DOCUMENT,
        TYPE_VAUCHER
    }

    public enum ActionType
    {
        OPEN,
        SHARE,
        PRINT,
        EDIT,
        DELETE
    }

    private final Downloader downloader;
    private final AppCompatActivity activity;
    private final HelperDocuments helperDocuments;

    public DocumentManipulator(AppCompatActivity activity, Downloader downloader, HelperDocuments helperDocuments)
    {
        this.activity = activity;
        this.downloader = downloader;
        this.helperDocuments = helperDocuments;
    }

    public interface ManipulateCallback
    {
        void onNoInternetError();

        void onDownloadError();

        void onSuccessDownloadForEdit(Model_Document document);

        void onErrorDownloadForEdit();

        void onSuccessDeleteOnServer();

        void onErrorDeleteOnServer();

        void onSuccessAction();
    }

    public void manipulateFromServer(Model_Document document, DocumentFileType fileType, ActionType actionType, ManipulateCallback callback)
    {
        if (!GlobalHelper.isNetworkAvailable())
        {
            callback.onNoInternetError();
            return;
        }

        downloader.downloadDocumentFileAsync(document, fileType, new Downloader.CallbackDownloadFile()
        {
            @Override
            public void onSuccessDownload(File file)
            {

                try
                {
                    switch (actionType)
                    {
                        case OPEN:
                            GlobalHelper.openPdf(activity, file);
                            break;
                        case SHARE:
                            GlobalHelper.shareFile(activity, file);
                            break;
                        case PRINT:
                            GlobalHelper.sendToPrint(activity, file);
                            break;
                        case EDIT:
                            makeEdit(document, callback);
                            break;
                        case DELETE:
                            makeDeleteOnServer(document, callback);
                            break;
                    }

                    callback.onSuccessAction();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    callback.onDownloadError();
                }
            }

            @Override
            public void onErrorDownload()
            {
                callback.onDownloadError();
            }
        });
    }

    private void makeEdit(Model_Document doc, ManipulateCallback callback)
    {
        if (!GlobalHelper.isNetworkAvailable())
        {
            callback.onNoInternetError();
            return;
        }

        helperDocuments.getDocumentWithFullInfo(doc.getId(), new HelperDocuments.CallbackGetFullDocument()
        {
            @Override
            public void onSuccessGetFullDocument(Model_Document document)
            {
                GlobalHelper.resetDocumentIds(document);
                callback.onSuccessDownloadForEdit(document);
            }

            @Override
            public void onErrorGetFullDocument()
            {
                callback.onErrorDownloadForEdit();
            }
        });
    }

    private void makeDeleteOnServer(Model_Document document, ManipulateCallback callback)
    {
        if (!GlobalHelper.isNetworkAvailable())
        {
            callback.onNoInternetError();
            return;
        }


        helperDocuments.deleteDocumentOnServer(document.getId(), new HelperDocuments.CallbackDeleteDocumentOnServer()
        {
            @Override
            public void onSuccessDeleteOnServer()
            {
                callback.onSuccessDeleteOnServer();
            }

            @Override
            public void onErrorDeleteOnServer()
            {
                callback.onErrorDeleteOnServer();
            }
        });
    }
}
