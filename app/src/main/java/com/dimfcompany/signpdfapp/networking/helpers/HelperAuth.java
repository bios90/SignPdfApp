package com.dimfcompany.signpdfapp.networking.helpers;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.networking.WintecApi;
import com.google.gson.Gson;

import retrofit2.Response;

public class HelperAuth
{
    private static final String TAG = "HelperAuth";

    public interface CallbackPassReset
    {
        void onSuccessPassReset();

        void onErrorPassReset();
    }

    public interface CallbackLogin
    {
        void onSuccessLogin(Model_User user);

        void onErrorLogin(String error);
    }

    public interface CallbackRegister
    {
        void onSuccessRegister();

        void onErrorRegister(String text);
    }

    private final WintecApi wintecApi;
    private final Gson gson;

    public HelperAuth(WintecApi wintecApi, Gson gson)
    {
        this.wintecApi = wintecApi;
        this.gson = gson;
    }

    public void register(final String first_name, final String last_name, final String email, final String password, final String fb_token, final CallbackRegister callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String response = wintecApi.registerNewUser(first_name, last_name, email, password, fb_token).execute().body();

                    Log.e(TAG, "run: Response is " + response);

                    if (response.equals("success"))
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                callback.onSuccessRegister();
                            }
                        });

                        return;
                    }

                    if (response.contains("email_in_use"))
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                callback.onErrorRegister("Данный email уже зарегистрирован");
                            }
                        });
                    }
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
                            callback.onErrorRegister("Ошибка при регистрации");
                        }
                    });
                }
            }
        }).start();
    }

    public void passReset(final String email, final CallbackPassReset callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String response = wintecApi.resetPassword(email).execute().body();
                    if (!response.equals("success"))
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                callback.onErrorPassReset();
                            }
                        });
                        return;
                    }

                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessPassReset();
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
                            callback.onErrorPassReset();
                        }
                    });
                }
            }
        }).start();
    }

    public void login(final String email, final String pass, final String fb_token,final String app_version, final CallbackLogin callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String response = wintecApi.login(email, pass, fb_token, app_version).execute().body();
                    Model_User user = null;
                    try
                    {
                        user = gson.fromJson(response, Model_User.class);
                    }
                    catch (Exception e)
                    {
                        Log.e(TAG, "run: Exception on casting");
                    }

                    if (user == null)
                    {
                        new Handler(Looper.getMainLooper()).post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                callback.onErrorLogin(response);
                            }
                        });
                        return;
                    }

                    Model_User finalUser = user;
                    new Handler(Looper.getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            callback.onSuccessLogin(finalUser);
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
                            callback.onErrorLogin("catch error");
                        }
                    });
                }
            }
        }).start();
    }
}
