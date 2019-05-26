package com.dimfcompany.signpdfapp.di.application;

import android.app.Application;

import androidx.room.Room;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.local_db.room.AppDatabase;
import com.dimfcompany.signpdfapp.local_db.room.RoomCrudHelper;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.networking.Downloader;
import com.dimfcompany.signpdfapp.networking.WintecApi;
import com.dimfcompany.signpdfapp.networking.helpers.HelperAuth;
import com.dimfcompany.signpdfapp.networking.helpers.HelperDocuments;
import com.dimfcompany.signpdfapp.networking.helpers.HelperUser;
import com.dimfcompany.signpdfapp.sync.SyncManager;
import com.dimfcompany.signpdfapp.sync.Synchronizer;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class ApplicationModule
{
    private final Application application;
    private final Gson gson;

    public ApplicationModule(Application application)
    {
        this.application = application;
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }


    @Singleton
    @Provides
    Retrofit getRetrofit()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);


        return new Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }

    @Singleton
    @Provides
    AppDatabase getAppDatabase()
    {
        return Room.databaseBuilder(application, AppDatabase.class, "wintec_db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

    @Singleton
    @Provides
    Gson getGson()
    {
        return gson;
    }

    @Singleton
    @Provides
    Application getApplication()
    {
        return application;
    }

    @Provides
    SharedPrefsHelper getSharedPrefsHelper(Application application, Gson gson)
    {
        return new SharedPrefsHelper(application, gson);
    }

    @Provides
    StringManager getStringManager(Application application, SharedPrefsHelper sharedPrefsHelper)
    {
        return new StringManager(application, sharedPrefsHelper);
    }

    @Provides
    Downloader getDownloader(FileManager fileManager, StringManager stringManager)
    {
        return new Downloader(fileManager, stringManager);
    }

    @Singleton
    @Provides
    WintecApi getWintecApi()
    {
        return getRetrofit().create(WintecApi.class);
    }

    @Provides
    HelperAuth getHelperAuth(WintecApi wintecApi, Gson gson)
    {
        return new HelperAuth(wintecApi, gson);
    }

    @Provides
    HelperUser getHelperUser(WintecApi wintecApi, Gson gson)
    {
        return new HelperUser(wintecApi, gson);
    }

    @Provides
    HelperDocuments getHelperDocuments(WintecApi wintecApi,Downloader downloader)
    {
        return new HelperDocuments(wintecApi, downloader);
    }

    @Provides
    FileManager getFileManager(Application application)
    {
        return new FileManager(application);
    }

    @Provides
    LocalDatabase getLocalDatabase(AppDatabase appDatabase)
    {
        return new RoomCrudHelper(appDatabase);
    }

    @Provides
    Synchronizer getSynchronizer(Application application, WintecApi wintecApi, FileManager fileManager, LocalDatabase localDatabase, Gson gson,SharedPrefsHelper sharedPrefsHelper, Downloader downloader)
    {
        return new SyncManager(application, wintecApi, fileManager, localDatabase, gson, sharedPrefsHelper, downloader);
    }


}
