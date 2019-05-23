package com.dimfcompany.signpdfapp.sync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.networking.Downloader;
import com.dimfcompany.signpdfapp.networking.WintecApi;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class SyncManager implements Synchronizer
{
    private static final String TAG = "SyncManager";

    public interface CallbackInsertWithSync
    {
        void onSuccessInsert(boolean inserted_to_server);

        void onErrorInsert();
    }

    public interface CallbackSyncronizeNoSynced
    {
        void onSuccessSync();

        void onErrorSync();
    }

    public interface CallbackSyncFromServer
    {
        void onSuccessSyncFromServer();

        void onErrorSyncFromServer();
    }


    private final Context context;
    private final WintecApi wintecApi;
    private final FileManager fileManager;
    private final LocalDatabase localDatabase;
    private final Gson gson;
    private final SharedPrefsHelper sharedPrefsHelper;
    private final Downloader downloader;

    public SyncManager(Context context, WintecApi wintecApi, FileManager fileManager, LocalDatabase localDatabase, Gson gson, SharedPrefsHelper sharedPrefsHelper, Downloader downloader)
    {
        this.context = context;
        this.wintecApi = wintecApi;
        this.fileManager = fileManager;
        this.localDatabase = localDatabase;
        this.gson = gson;
        this.sharedPrefsHelper = sharedPrefsHelper;
        this.downloader = downloader;
    }

    @Override
    public void insertDocumentWithSync(final Model_Document document, final CallbackInsertWithSync callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    boolean uploaded = false;
                    insertLocally(document);

                    if (isNetworkAvailable())
                    {
                        syncNewCreated();
                        uploaded = true;
                    }
                    else
                        {
                            putSynchronizeTask();
                        }

                    final boolean finalUploaded = uploaded;
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessInsert(finalUploaded);
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
                            callback.onErrorInsert();
                        }
                    });
                }
            }
        }).start();
    }

//    @Override
//    public void insertDocumentWithSync(final Model_Document document, final CallbackInsertWithSync callback)
//    {
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                try
//                {
//
//                    if (isNetworkAvailable())
//                    {
//                        boolean insertedRemote = false;
//                        Model_Document insertedOnServer = insertOnServer(document);
//                        if (insertedOnServer != null)
//                        {
//                            insertedOnServer.setSync_status(1);
//                            localDatabase.insertDocument(insertedOnServer);
//                            insertedRemote = true;
//                        } else
//                        {
//                            document.setSync_status(0);
//                            localDatabase.insertDocument(document);
//                            insertedRemote = false;
//                        }
//
//                        final boolean finalInsertedRemote = insertedRemote;
//                        new Handler(Looper.getMainLooper()).post(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                callback.onSuccessInsert(finalInsertedRemote);
//                            }
//                        });
//                    }
//                    else
//                    {
//                        insertLocally(document);
//                        new Handler(Looper.getMainLooper()).post(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                callback.onSuccessInsert(false);
//                            }
//                        });
//                    }
//
//
//                } catch (Exception e)
//                {
//                    e.printStackTrace();
//                    Log.e(TAG, "Exception " + e.getMessage());
//                    new Handler(Looper.getMainLooper()).post(new Runnable()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            callback.onErrorInsert();
//                        }
//                    });
//                }
//            }
//        }).start();
//    }

    @Override
    public void syncronizeNotSynced(final CallbackSyncronizeNoSynced callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    syncNewCreated();
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessSync();
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
                            callback.onErrorSync();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void deleteDocumentWithSync(Model_Document document)
    {

    }

    @Override
    public void putSynchronizeTask()
    {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);

        if (isJobServiceOn(context, Constants.JOB_ID_SYNC))
        {
            scheduler.cancel(Constants.JOB_ID_SYNC);
        }

        ComponentName componentName = new ComponentName(context, SyncJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(Constants.JOB_ID_SYNC, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        int resultCode = scheduler.schedule(jobInfo);

        if (resultCode == JobScheduler.RESULT_SUCCESS)
        {
            Log.d(TAG, "sheduleJob: Job Setted");
        } else
        {
            Log.d(TAG, "sheduleJob: Job sheduling failed");
        }
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void syncNewCreated() throws IOException
    {
        List<Model_Document> notSyncedDocuments = localDatabase.getNotSyncedDocuments();
        for (Model_Document document : notSyncedDocuments)
        {
            Model_Document insertedDocument = insertOnServer(document);
            if (insertedDocument != null)
            {
                insertedDocument.setSync_status(1);
                localDatabase.deleteDocumentFull(document);
                localDatabase.insertDocument(insertedDocument);
            } else
            {
                Log.e(TAG, "syncNewCreated: Doc is null");
            }
        }
    }

    private void insertLocally(Model_Document document)
    {
        localDatabase.insertDocument(document);
    }


    private Model_Document insertOnServer(Model_Document document) throws IOException
    {
        MultipartBody.Part part_document = modelToPartBody(document, "document");

        MultipartBody.Part part_pdf_file = null;
        MultipartBody.Part part_check_file = null;
        MultipartBody.Part part_signature_file = null;

        File file_pdf = fileManager.getFileFromTemp(document.getPdf_file_name(), Constants.FOLDER_CONTRACTS, null);
        if (file_pdf != null && file_pdf.exists())
        {
            part_pdf_file = fileToPartBody(file_pdf, "pdf_file");
        }

        File file_check = fileManager.getFileFromTemp(document.getCheck_file_name(), Constants.FOLDER_CHECKS, null);
        if (file_check != null && file_check.exists())
        {
            part_check_file = fileToPartBody(file_check, "check_file");
        }

        File file_signature = fileManager.getFileFromTemp(document.getSignature_file_name(), null);
        if (file_signature != null && file_signature.exists())
        {
            part_signature_file = fileToPartBody(file_signature, "signature_file");
        }

        Model_Document insertedDocument = wintecApi.insertDocument(part_document, part_pdf_file, part_check_file, part_signature_file).execute().body();

        if (insertedDocument == null)
        {
            Log.e(TAG, "insertOnServer: Error on uploading to server, will save locally((");
        }

        return insertedDocument;
    }



    @Override
    public void makeSyncFromServer(final CallbackSyncFromServer callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    int user_id = sharedPrefsHelper.getUserFromSharedPrefs().getId();
                    final List<Model_Document> documents = wintecApi.getAllDocuments(user_id).execute().body();

                    localDatabase.deleteAllLocalData();
                    fileManager.deleteAllFiles();

                    for (Model_Document document : documents)
                    {
                        document.setSync_status(1);
                        localDatabase.insertDocument(document);
                    }

                    downloader.downloadDocumentFiles(documents);

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessSyncFromServer();
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
                            callback.onErrorSyncFromServer();
                        }
                    });
                }
            }
        }).start();
    }


    private MultipartBody.Part modelToPartBody(Object object, String field_name)
    {
        String workerGson = gson.toJson(object);
        return MultipartBody.Part.createFormData(field_name, workerGson);
    }

    private MultipartBody.Part fileToPartBody(File file, String field_name)
    {
        MultipartBody.Part bodyFile = null;

        if (file != null && file.exists())
        {
            RequestBody requestPhoto = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            bodyFile = MultipartBody.Part.createFormData(field_name, file.getName(), requestPhoto);
        }

        return bodyFile;
    }

    public static boolean isJobServiceOn(Context context, int job_id)
    {
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        boolean hasBeenScheduled = false;

        for (JobInfo jobInfo : scheduler.getAllPendingJobs())
        {
            if (jobInfo.getId() == job_id)
            {
                hasBeenScheduled = true;
                break;
            }
        }

        return hasBeenScheduled;
    }
}
