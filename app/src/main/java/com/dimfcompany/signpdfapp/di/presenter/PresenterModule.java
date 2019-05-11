package com.dimfcompany.signpdfapp.di.presenter;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.viewmvcfactory.ViewMvcFactory;
import com.dimfcompany.signpdfapp.local_db.raw.CrudHelper;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.local_db.room.AppDatabase;
import com.dimfcompany.signpdfapp.local_db.room.RoomCrudHelper;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.NavigationManager;
import com.dimfcompany.signpdfapp.utils.PdfCreator;
import com.dimfcompany.signpdfapp.utils.StringManager;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule
{
    private final AppCompatActivity activity;

    public PresenterModule(AppCompatActivity activity)
    {
        this.activity = activity;
    }

    @Provides
    Context getContext()
    {
        return activity;
    }

    @Provides
    AppCompatActivity getActivity()
    {
        return activity;
    }

    @Provides
    LayoutInflater getLayoutInfalter()
    {
        return LayoutInflater.from(activity);
    }

    @Provides
    ViewMvcFactory getViewMvcFactory(LayoutInflater layoutInflater, FileManager fileManager, StringManager stringManager,GlobalHelper globalHelper, MessagesManager messagesManager)
    {
        return new ViewMvcFactory(layoutInflater, fileManager, stringManager, globalHelper, messagesManager);
    }

    @Provides
    NavigationManager getNavigationManager(AppCompatActivity activity)
    {
        return new NavigationManager(activity);
    }

    @Provides
    FileManager getFileManager(AppCompatActivity activity)
    {
        return new FileManager(activity);
    }

    @Provides
    PdfCreator getPdfCreator(Context context,FileManager fileManager, CrudHelper crudHelper)
    {
        return  new PdfCreator(context,fileManager, crudHelper);
    }

    @Provides
    CrudHelper getCrudHelper(Context context)
    {
        return new CrudHelper(context);
    }

    @Provides
    MessagesManager messagesManager(AppCompatActivity activity)
    {
        return new MessagesManager(activity);
    }

    @Provides
    StringManager getStringManager(AppCompatActivity activity)
    {
        return new StringManager(activity);
    }

    @Provides
    GlobalHelper gertGlobalHelper(Context context)
    {
        return new GlobalHelper(context);
    }

    @Provides
    LocalDatabase getLocalDatabase(AppDatabase appDatabase)
    {
        return new RoomCrudHelper(appDatabase);
    }
}
