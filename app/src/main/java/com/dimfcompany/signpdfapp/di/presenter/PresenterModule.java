package com.dimfcompany.signpdfapp.di.presenter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.dimfcompany.signpdfapp.base.viewmvcfactory.ViewMvcFactory;
import com.dimfcompany.signpdfapp.sqlite.CrudHelper;
import com.dimfcompany.signpdfapp.utils.FileManager;
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
    ViewMvcFactory getViewMvcFactory(LayoutInflater layoutInflater, FileManager fileManager, StringManager stringManager)
    {
        return new ViewMvcFactory(layoutInflater, fileManager, stringManager);
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

}
