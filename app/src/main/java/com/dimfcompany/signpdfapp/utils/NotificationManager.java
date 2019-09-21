package com.dimfcompany.signpdfapp.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.base.AppClass;
import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.ui.act_admin.ActAdmin;
import com.dimfcompany.signpdfapp.ui.act_first.ActFirst;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class NotificationManager
{
    private static final String TAG = "NotificationManager";
    private final Gson gson;
    private final SharedPrefsHelper sharedPrefsHelper;
    private final Context appContext;

    public NotificationManager(Gson gson, SharedPrefsHelper sharedPrefsHelper, Context appContext)
    {
        this.gson = gson;
        this.sharedPrefsHelper = sharedPrefsHelper;
        this.appContext = appContext;
    }

    public void notify(final RemoteMessage remoteMessage)
    {
        new Thread(() ->
        {
            try
            {
                Log.e(TAG, "notify: Called Notify");
                Map<String, String> data = remoteMessage.getData();

                String type = data.get("type");
                if (type == null)
                {
                    Log.e(TAG, "notify: Exception type is NULL");
                    return;
                }

                if (type.equals("new_doc"))
                {
                    showNewDocNotify(remoteMessage);
                }

                if (type.equals("role_change"))
                {
                    makeRoleChange(remoteMessage);
                }

                if (type.equals("logout"))
                {
                    make_logout();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    private void makeRoleChange(RemoteMessage remoteMessage)
    {
        Log.e(TAG, "makeRoleChange: success got response and will changeRole");

        Model_User user = sharedPrefsHelper.getUserFromSharedPrefs();
        if (user == null)
        {
            Log.e(TAG, "makeRoleChange: User null not logged in");
            return;
        }

        int role_id = Integer.valueOf(remoteMessage.getData().get("role_id"));

        if (role_id == user.getRole_id())
        {
            Log.e(TAG, "makeRoleChange: User role id is " + user.getRole_id() + " nad getted id is " + role_id);
            return;
        }

        user.setRole_id(role_id);

        sharedPrefsHelper.saveUserToShared(user);

        make_logout();
    }

    private void make_logout()
    {
        Log.e(TAG, "make_logout: got logout task");

        sharedPrefsHelper.clearUserLocalData();

        if (isAppOnForeground())
        {
            Intent dialogIntent = new Intent(appContext, ActFirst.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            appContext.startActivity(dialogIntent);
        }
        else
        {
            appContext.sendBroadcast(new Intent(Constants.BROADCAST_KILL_APPLICATION));
        }
    }

    private void showNewDocNotify(RemoteMessage remoteMessage)
    {
        String documentStr = remoteMessage.getData().get("document");
        final Model_Document document = gson.fromJson(documentStr, Model_Document.class);

        if (document == null)
        {
            Log.e(TAG, "showNewDocNotify: Error on creating ShowNEwDOcNotufy - document is null");
            return;
        }


        new Handler(Looper.getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                Notification notification = getNewDocNotification(document);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    showHighNotification(notification);
                }
                else
                {
                    showLowNotification(notification);
                }
            }
        });
    }

    private Notification getNewDocNotification(Model_Document document)
    {
        String title = "Добавлен новый документ | Сумма " + StringManager.formatNum(document.getSum(), false);

        String message = "";
        if (document.getAdress() != null)
        {
            message += "Адрес: " + document.getAdress();
        }


        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(AppClass.getApp(), ActAdmin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(AppClass.getApp(), GlobalHelper.getRandomInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(AppClass.getApp(), Constants.NOTIFICATION_CHANEL1)
                .setSmallIcon(R.drawable.ic_cloud)
                .setLargeIcon(BitmapFactory.decodeResource(AppClass.getApp().getResources(), R.drawable.logo_new))
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setOnlyAlertOnce(false)
                .setSound(sound);

        if (!TextUtils.isEmpty(message))
        {
            builder.setContentText(message);
        }

        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void showHighNotification(Notification notification)
    {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.app.NotificationManager notificationManager = AppClass.getApp().getSystemService(android.app.NotificationManager.class);
        NotificationChannel notificationChannel = new NotificationChannel
                (Constants.NOTIFICATION_CHANEL1, "Добавлен новый документ", android.app.NotificationManager.IMPORTANCE_HIGH);

        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        notificationChannel.setDescription("Напомнинания о добоавлении нового документа");
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setSound(sound, att);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.setVibrationPattern(new long[]{0, 300, 1000});

        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(GlobalHelper.getRandomInt(), notification);
    }

    private void showLowNotification(Notification notification)
    {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(AppClass.getApp());
        notificationManagerCompat.notify(GlobalHelper.getRandomInt(), notification);
    }

    private boolean isAppOnForeground()
    {
        ActivityManager activityManager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
        {
            return false;
        }
        final String packageName = appContext.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses)
        {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName))
            {
                return true;
            }
        }
        return false;
    }
}
