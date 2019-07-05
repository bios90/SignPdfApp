package com.dimfcompany.signpdfapp.networking.helpers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

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
        void onSuccessGetUsers(List<Model_User> users, String app_version_last);

        void onErrorGetUsers();
    }

    public interface CallbackChangeRole
    {
        void onSuccessChangeRole();

        void onErrorChangeRole();
    }

    public interface CallbackGetUserFull
    {
        void onSuccessGetUserFull(Model_User user, String last_version);

        void onErrorGetUSerFull();
    }

    public interface CallbackToggleApproved
    {
        void onSuccessToggleApproved();

        void onErrorToggleApproved();
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
                String app_version = wintecApi.getAppLastVersion().execute().body();

                if (users == null || app_version == null)
                {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onErrorGetUsers());
                    return;
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccessGetUsers(users, app_version));
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

                Log.e(TAG, "changeRole: Response is " + response);

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


    public void getUserFull(int user_id, CallbackGetUserFull callback)
    {
        new Thread(() ->
        {
            try
            {
                Model_User user = wintecApi.getUserFull(user_id).execute().body();
                String last_version = wintecApi.getAppLastVersion().execute().body();

                if (user == null || last_version == null)
                {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onErrorGetUSerFull());
                    return;
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccessGetUserFull(user, last_version));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e(TAG, "Exception " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callback.onErrorGetUSerFull());
            }
        }).start();
    }


    public void toggleUserApproved(int user_id, @Nullable Integer approved_value, CallbackToggleApproved callback)
    {
        new Thread(() ->
        {
            try
            {
                String response = wintecApi.toggleUserApproved(user_id, approved_value).execute().body();
                if (response == null || !response.equals("success"))
                {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onErrorToggleApproved());
                    return;
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccessToggleApproved());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e(TAG, "Exception " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callback.onErrorToggleApproved());
            }
        }).start();
    }

    public void insertOrUpdateUser(@Nullable Integer user_id, String first_name, String last_name, String email, String password, int verified, int admin_approved, int role_id, HelperAuth.CallbackRegister callback)
    {
        new Thread(() ->
        {
            try
            {
                String response = wintecApi.insertOrUpdateUserNew(user_id, first_name, last_name, email, password, verified, admin_approved, role_id).execute().body();

                if (response.equals("success"))
                {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccessRegister());

                    return;
                }

                if (response.contains("email_in_use"))
                {
                    new Handler(Looper.getMainLooper()).post(() -> callback.onErrorRegister("Данный email уже зарегистрирован"));
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e(TAG, "Exception " + e.getMessage());
                new Handler(Looper.getMainLooper()).post(() -> callback.onErrorRegister("Ошибка при регистрации"));
            }
        }).start();
    }
}
