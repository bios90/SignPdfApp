package com.dimfcompany.signpdfapp.networking.helpers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.networking.Downloader;
import com.dimfcompany.signpdfapp.networking.WintecApi;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HelperDocuments
{
    private static final String TAG = "HelperDocuments";

    public interface CallbackGetAllAllDocuments
    {
        void onSuccessGetAllAllDocuments(List<Model_Document> documents);

        void onErrorGetAllAllDocuments();
    }

    public interface CallbackGetFullDocument
    {
        void onSuccessGetFullDocument(Model_Document document);

        void onErrorGetFullDocument();
    }

    public interface CallbackDeleteDocumentOnServer
    {
        void onSuccessDeleteOnServer();

        void onErrorDeleteOnServer();
    }


    private final WintecApi wintecApi;
    private final Downloader downloader;

    public HelperDocuments(WintecApi wintecApi, Downloader downloader)
    {
        this.wintecApi = wintecApi;
        this.downloader = downloader;
    }

    public void getAllAllDocumets(final CallbackGetAllAllDocuments callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final List<Model_Document> documents = wintecApi.getAllUSersAllDocs().execute().body();

                    if (documents == null)
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                callback.onErrorGetAllAllDocuments();
                            }
                        });
                        return;
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessGetAllAllDocuments(documents);
                        }
                    });

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "Exception " + e.getMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onErrorGetAllAllDocuments();
                        }
                    });
                }
            }
        }).start();
    }


    public void getDocumentWithFullInfo(final long document_id, final CallbackGetFullDocument callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final Model_Document document = wintecApi.getDocumentWithFullInfo(document_id).execute().body();

                    if (document == null)
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                callback.onErrorGetFullDocument();
                            }
                        });
                        return;
                    }

                    List<Model_Document> documents = new ArrayList<>();
                    documents.add(document);
                    downloader.downloadDocumentFiles(documents);

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessGetFullDocument(document);
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "Exception " + e.getMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onErrorGetFullDocument();
                        }
                    });
                }
            }
        }).start();
    }


    public void deleteDocumentOnServer(final long document_id, final CallbackDeleteDocumentOnServer callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String response = wintecApi.deleteDocumentOnServer(document_id).execute().body();

                    if (response == null || !response.equals("success"))
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                callback.onErrorDeleteOnServer();
                            }
                        });
                        return;
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessDeleteOnServer();
                        }
                    });

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "Exception " + e.getMessage());
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onErrorDeleteOnServer();
                        }
                    });
                }
            }
        }).start();
    }

    public Completable updateDocumentLocation(long document_id, double lat, double lon)
    {
        return wintecApi.update_document_location(document_id, lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                .ignoreElements();
    }
}
