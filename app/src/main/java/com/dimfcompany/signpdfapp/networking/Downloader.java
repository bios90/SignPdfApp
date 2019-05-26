package com.dimfcompany.signpdfapp.networking;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

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

    public enum DocumentFileType
    {
        TYPE_SIGNATURE,
        TYPE_CHECK,
        TYPE_DOCUMENT
    }

    public interface CallbackDownloadDocumentFiles
    {
        void onSuccessDownloadFiles();

        void onErrorDownloadFiles();
    }

    public interface CallbackDownloadFile
    {
        void onSuccessDownload(File file);

        void onErrorDownload();
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
                downloadDocumentFile(document, DocumentFileType.TYPE_SIGNATURE);
            }

            if (document.getPdf_file_name() != null)
            {
                downloadDocumentFile(document, DocumentFileType.TYPE_DOCUMENT);
            }

            if (document.getCheck_file_name() != null)
            {
                downloadDocumentFile(document, DocumentFileType.TYPE_CHECK);
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


    public static void downloadFileAsync(final String urlString, final File file, final CallbackDownloadFile callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    download(urlString, file);
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessDownload(file);
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
                            callback.onErrorDownload();
                        }
                    });
                }
            }
        }).start();
    }

    public void downloadDocumentFileAsync(final Model_Document document, final DocumentFileType type, final CallbackDownloadFile callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final File file = downloadDocumentFile(document, type);
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessDownload(file);
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
                            callback.onErrorDownload();
                        }
                    });
                }
            }
        }).start();
    }


    @Nullable
    public File downloadDocumentFile(Model_Document document, DocumentFileType type)
    {
        String url = null;
        File file = null;

        switch (type)
        {
            case TYPE_SIGNATURE:
                if (document.getSignature_file_name() == null)
                {
                    Log.e(TAG, "downloadDocumentFile: error no signatureFileName");
                    return null;
                }
                file = fileManager.createFile(document.getSignature_file_name(), null, Constants.FOLDER_TEMP_FILES);
                url = stringManager.getUserSignaturesUrl() + document.getSignature_file_name();
                break;

            case TYPE_DOCUMENT:
                if (document.getPdf_file_name() == null)
                {
                    Log.e(TAG, "downloadDocumentFile: error no documentPDfFile");
                    return null;
                }
                file = fileManager.createFile(document.getPdf_file_name(), null, Constants.FOLDER_CONTRACTS);
                url = stringManager.getUserDocumentsUrl() + document.getPdf_file_name();
                break;

            case TYPE_CHECK:
                if (document.getCheck_file_name() == null)
                {
                    Log.e(TAG, "downloadDocumentFile: error no checkFileName");
                    return null;
                }
                file = fileManager.createFile(document.getCheck_file_name(), null, Constants.FOLDER_CHECKS);
                url = stringManager.getUserChecksUrl() + document.getCheck_file_name();
                break;
        }

        if (file != null && file.exists() && url != null)
        {
            try
            {
                download(url, file);
                Log.d(TAG, "downloadDocumentFile: Dwonloaded ok");
                return file;
            } catch (IOException e)
            {
                Log.e(TAG, "downloadDocumentFile: Exception on Downloading");
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    public static void download(String urlString, File file) throws IOException
    {
        int count;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.connect();

        InputStream input = new BufferedInputStream(url.openStream(), 8192);
        OutputStream output = new FileOutputStream(file);

        byte[] data = new byte[1024];

        while ((count = input.read(data)) != -1)
        {
            output.write(data, 0, count);
        }

        output.flush();

        output.close();
        input.close();
    }
}
