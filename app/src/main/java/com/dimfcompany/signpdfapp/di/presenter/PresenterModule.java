package com.dimfcompany.signpdfapp.di.presenter;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.dimfcompany.signpdfapp.base.viewmvcfactory.ViewMvcFactory;
import com.dimfcompany.signpdfapp.local_db.raw.CrudHelper;
import com.dimfcompany.signpdfapp.local_db.sharedprefs.SharedPrefsHelper;
import com.dimfcompany.signpdfapp.networking.Downloader;
import com.dimfcompany.signpdfapp.networking.helpers.HelperDocuments;
import com.dimfcompany.signpdfapp.utils.DocumentManipulator;
import com.dimfcompany.signpdfapp.utils.FileManager;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.MyLocationManager;
import com.dimfcompany.signpdfapp.utils.MessagesManager;
import com.dimfcompany.signpdfapp.utils.NavigationManager;
import com.dimfcompany.signpdfapp.utils.PdfCreator;
import com.dimfcompany.signpdfapp.utils.StringManager;
import com.dimfcompany.signpdfapp.utils.dates.DatePickerDialog;
import com.dimfcompany.signpdfapp.utils.dates.MyDatePicker;

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
    ViewMvcFactory getViewMvcFactory(LayoutInflater layoutInflater, FileManager fileManager, StringManager stringManager, GlobalHelper globalHelper, MessagesManager messagesManager, SharedPrefsHelper sharedPrefsHelper)
    {
        return new ViewMvcFactory(layoutInflater, fileManager, stringManager, globalHelper, messagesManager, sharedPrefsHelper);
    }

    @Provides
    NavigationManager getNavigationManager(AppCompatActivity activity)
    {
        return new NavigationManager(activity);
    }


    @Provides
    PdfCreator getPdfCreator(Context context, FileManager fileManager, CrudHelper crudHelper)
    {
        return new PdfCreator(context, fileManager, crudHelper);
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
    GlobalHelper gertGlobalHelper(Context context)
    {
        return new GlobalHelper(context);
    }

    @Provides
    MyLocationManager getLocationManager(AppCompatActivity activity)
    {
        return new MyLocationManager(activity);
    }

    @Provides
    DocumentManipulator getManipulator(AppCompatActivity activity, Downloader downloader, HelperDocuments helperDocuments)
    {
        return new DocumentManipulator(activity,downloader, helperDocuments);
    }

    @Provides
    MyDatePicker getDatePicker(AppCompatActivity activity)
    {
        return new DatePickerDialog(activity);
    }
}
