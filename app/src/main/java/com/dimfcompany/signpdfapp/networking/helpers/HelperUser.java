package com.dimfcompany.signpdfapp.networking.helpers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.networking.WintecApi;
import com.google.gson.Gson;

import java.util.List;

public class HelperUser
{
    private static final String TAG = "HelperUser";

    public interface CallbackUserRoleName
    {
        void onSuccessGetUserRole(String roleName);
    }

    public interface CallbackGetDocsCount
    {
        void onSuccessGetDocsCount(Integer count);
    }

    public interface CallbackGetAllDocuments
    {
        void onSuccessGetAllDocuments(List<Model_Document> documents);

        void onErrorGetAllDocuments();
    }

    public interface CallbackGetUsers
    {
        void onSuccessGetUsers(List<Model_User> users);

        void onErrorGetUsers();
    }

    public interface CallbackChangeRole
    {
        void onSuccessChangeRole();

        void onErrorChangeRole();
    }

    public interface CallbackGetUserFull
    {
        void onSuccessGetUserFull(Model_User user);
        void onErrorGetUSerFull();
    }

    private final WintecApi wintecApi;
    private final Gson gson;

    public HelperUser(WintecApi wintecApi, Gson gson)
    {
        this.wintecApi = wintecApi;
        this.gson = gson;
    }

    public void getUserRoleName(final int user_id, final CallbackUserRoleName callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final String role_name = wintecApi.getUserRoleName(user_id).execute().body();

                    if (role_name == null)
                    {
                        return;
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessGetUserRole(role_name);
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "Exception " + e.getMessage());
                }
            }
        }).start();
    }

    public void getDocsCount(final int user_id, final CallbackGetDocsCount callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final Integer docsCount = wintecApi.getUserDocsCount(user_id).execute().body();

                    if (docsCount == null)
                    {
                        return;
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessGetDocsCount(docsCount);
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(TAG, "Exception " + e.getMessage());
                }
            }
        }).start();
    }

    public void getAllDocuments(final int user_id, final CallbackGetAllDocuments callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    final List<Model_Document> documents = wintecApi.getAllDocuments(user_id).execute().body();
                    if (documents == null)
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                callback.onErrorGetAllDocuments();
                            }
                        });
                        return;
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessGetAllDocuments(documents);
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
                            callback.onErrorGetAllDocuments();
                        }
                    });
                }
            }
        }).start();
    }

    public void getUsers(final String search, final String sort, CallbackGetUsers callback)
    {
        new Thread(() ->
        {
            try
            {
                List<Model_User> users = wintecApi.getUsers(search, sort).execute().body();

                if (users == null)
                {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onErrorGetUsers());
                    return;
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccessGetUsers(users));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e(TAG, "Exception " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callback.onErrorGetUsers());
            }
        }).start();
    }


    public void changeRole(int user_id, int role_id, CallbackChangeRole callback)
    {
        new Thread(() ->
        {
            try
            {
                String response = wintecApi.changeRole(user_id, role_id).execute().body();
                if (!response.equals("success"))
                {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onErrorChangeRole());
                    return;
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccessChangeRole());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e(TAG, "Exception " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callback.onErrorChangeRole());
            }
        }).start();
    }


    public void getUserFull(int user_id,CallbackGetUserFull callback)
    {
        new Thread(() ->
        {
            try
            {
                Model_User user = wintecApi.getUserFull(user_id).execute().body();
                if(user == null)
                {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onErrorGetUSerFull());
                    return;
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccessGetUserFull(user));
            } catch (Exception e)
            {
                e.printStackTrace();
                Log.e(TAG, "Exception " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callback.onErrorGetUSerFull());
            }
        }).start();
    }


}
