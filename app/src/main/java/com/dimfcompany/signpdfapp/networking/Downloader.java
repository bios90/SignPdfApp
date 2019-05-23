package com.dimfcompany.signpdfapp.networking;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.StringManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Downloader
{
    private static final String TAG = "Downloader";

    public interface CallbackDownloadDocumentFiles
    {
        void onSuccessDownloadFiles();
        void onErrorDownloadFiles();
    }

    private final FileManager fileManager;
    private final StringManager stringManager;

    public Downloader(FileManager fileManager, StringManager stringManager)
    {
        this.fileManager = fileManager;
        this.stringManager = stringManager;
    }

    public void downloadDocumentFiles(final List<Model_Document> documents) throws IOException
    {
        for (Model_Document document : documents)
        {
            if (document.getSignature_file_name() != null)
            {
                File file = fileManager.createFileWithName(document.getSignature_file_name(), Constants.FOLDER_TEMP_FILES);
                String url = stringManager.getUserSignaturesUrl() + document.getSignature_file_name();
                download(url, file);

                Log.d(TAG, "downloadDocumentFilesAsync: downloading Signature file " + url + "  **** To file " + file.getName());
            }

            if (document.getPdf_file_name() != null)
            {
                File file = fileManager.createFileWithName(document.getPdf_file_name(), Constants.FOLDER_CONTRACTS);
                String url = stringManager.getUserDocumentsUrl() + document.getPdf_file_name();
                download(url, file);

                Log.d(TAG, "downloadDocumentFilesAsync: downloading Document file " + url + "  **** To file " + file.getName());
            }

            if (document.getCheck_file_name() != null)
            {
                File file = fileManager.createFileWithName(document.getCheck_file_name(), Constants.FOLDER_CHECKS);
                String url = stringManager.getUserChecksUrl() + document.getCheck_file_name();
                download(url, file);

                Log.d(TAG, "downloadDocumentFilesAsync: downloading Check file " + url + "  **** To file " + file.getName());
            }
        }
    }

    public void downloadDocumentFilesAsync(final List<Model_Document> documents, final CallbackDownloadDocumentFiles callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    downloadDocumentFiles(documents);

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessDownloadFiles();
                        }
                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "Exception " + e.getMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onErrorDownloadFiles();
                        }
                    });
                }
            }
        }).start();
    }

    public static void download(String urlString, File file) throws IOException
    {
        int count;
        URL url = new URL(urlString);
        URLConnection conection = url.openConnection();
        conection.connect();

        InputStream input = new BufferedInputStream(url.openStream(), 8192);
        OutputStream output = new FileOutputStream(file);

        byte[] data = new byte[1024];

        while ((count = input.read(data)) != -1)
        {
            output.write(data, 0, count);
        }

        // flushing output
        output.flush();

        // closing streams
        output.close();
        input.close();
    }
}
